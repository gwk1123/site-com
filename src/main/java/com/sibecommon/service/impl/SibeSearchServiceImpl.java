package com.sibecommon.service.impl;


import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.sibecommon.ota.site.*;
import com.sibecommon.service.transform.TransformSearchGds;
import com.sibecommon.sibe.OtaSearchResponse;
import com.sibecommon.utils.async.SibeSearchAsyncService;
import com.sibecommon.utils.constant.Constants;
import com.sibecommon.utils.redis.GdsCacheService;
import com.sibecommon.config.SibeProperties;
import com.sibecommon.ota.site.*;
import com.sibecommon.ota.site.*;
import com.sibecommon.repository.entity.SiteRulesSwitch;
import com.sibecommon.service.transform.TransformCommonOta;
import com.sibecommon.sibe.SibeSearchCommService;
import com.sibecommon.sibe.SibeSearchService;
import com.sibecommon.utils.constant.SibeConstants;
import com.sibecommon.utils.exception.CustomSibeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class SibeSearchServiceImpl implements SibeSearchService {

    private Logger logger = LoggerFactory.getLogger(SibeSearchService.class);

    @Autowired
    private SibeProperties sibeProperties;
    @Autowired
    private SibeSearchCommService sibeSearchCommService;
    @Autowired
    private SibeSearchAsyncService sibeSearchAsyncService;
    @Autowired
    private GdsCacheService gdsCacheService;
    @Autowired
    private TransformSearchGds transformSearchGds;
    @Autowired
    private OtaSearchResponse otaSearchResponse;


    @Override
    public Object search(SibeSearchRequest sibeSearchRequest) throws Exception {

        String uuid = sibeSearchRequest.getUuid();
        //1 请求参数校验，cid校验
        this.cidValidate(sibeSearchRequest, sibeProperties);
        //2转化站点数据
        TransformCommonOta.constructSibeBaseRequest(sibeSearchRequest, sibeProperties);
        //3站点开关校验 // TODO
        this.siteCloseValidate (sibeSearchRequest);
        //4 获得Redis系统配置数据，将获取到的数据设置到sibeSearchRequest
        sibeSearchCommService.constructSibeSearchRequestSecond(sibeSearchRequest);
        //5.OTA规则校验
        //  (1)OTA-限制多程<包括缺口程> (2)OTA-航线白名单 (3)限制旅行日期范围
        //  (4)OAT-航线黑名单 // TODO

        //获取站点缓存 //// TODO
        SibeCacheResponse sibeCacheResponse = this.isOTASiteCacheExist(sibeSearchRequest);

        /*
         * 如果存在Key且缓存刷新时间没有过期，则返回0 和缓存结果
         * 如果存在Key且缓存刷新时间已经过期，则返回1 和缓存结果
         * 如果不存在Key，则返回2 和缓存结果
         * 如果存在Key,Value=wait，则返回3 和缓存结果
         */
        //7. cacheResult：0 得到缓存成功，无需刷缓存
        if ("0".equals(sibeCacheResponse.cacheExist)){
            return  sibeCacheResponse.getSearchResponse();
            //cacheResult：1  存在Key且缓存刷新时间已经过期，此时应该进行请求GDS流程，并且直接将站点缓存返回
        }else if ("1".equals(sibeCacheResponse.cacheExist)){
            //请求GDS流程
            sibeSearchAsyncService.asynRequestGDSBySiteCacheExpired(sibeSearchRequest);
            return sibeCacheResponse.getSearchResponse();
            //cacheResult：2 如果不存在站点缓存，则需要进行请求GDS流程
        }else if ("2".equals(sibeCacheResponse.cacheExist)) {
            //无缓存则发起GDS请求,要求同步返回
            return requestGDSBySiteCacheNotExist(sibeSearchRequest);
        }

        logger.warn("uuid:" + uuid + " 请求GDS,没有得到缓存:"+sibeSearchRequest.getTripCacheOTASiteKey()+  " 耗费时间：" + (SystemClock.now()-sibeSearchRequest.getStartTime()));
        throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_100,"无数据", uuid,"search");
    }



    private Object requestGDSBySiteCacheNotExist(SibeSearchRequest sibeSearchRequest) {

        logger.debug("uuid:"+sibeSearchRequest.getUuid() +" 1 进入requestGds:"+(SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

        //1.查找GSD缓存(路由过滤)
        sibeSearchCommService.constructSibeSearchRequestByRoute(sibeSearchRequest);

        SibeObtainGDSCache sibeObtainGDSCache = sibeSearchCommService.getGDSCache(sibeSearchRequest);
        List<SibeSearchResponse> sibeSearchResponseList = sibeObtainGDSCache.getSibeSearchResponseList();

        //2.无GDS缓存，根据站点的配置情况进行异步或者同步请求
        if(sibeSearchResponseList == null || sibeSearchResponseList.size()==0){
            if(sibeProperties.getSearch().isAsync()) {
                //异步请求GDS，返回无航班数据
                sibeSearchAsyncService.requestGdsAsync(sibeSearchRequest, Constants.METHOD_TYPE_SEARCH);
                throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_100,"无GDS缓存,异步查询GDS更新。", sibeSearchRequest.getUuid(),"search");
            }else {
                //同步请求GDS
                sibeSearchResponseList = sibeSearchCommService.search(sibeSearchRequest);
                if( sibeSearchResponseList.size() ==0 ){
                    throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_100,"GDS查询无数据,", sibeSearchRequest.getUuid(),"search");
                }
            }
        }else {
            //GDS过了刷新时间，但在有效时间内，异步刷新GDS
            if(sibeSearchRequest.getRefreshGDSMap() != null && sibeSearchRequest.getRefreshGDSMap().size() > 0){
                sibeSearchAsyncService.requestGdsAsync(sibeSearchRequest, Constants.METHOD_TYPE_SEARCH);
            }
        }

        logger.debug("uuid:"+sibeSearchRequest.getUuid() +" 响应的结果集数"+ sibeSearchResponseList.size()+" search 请求GDS消耗:"+ ((SystemClock.now()-sibeSearchRequest.getStartTime())/1000) +"秒");
        SibeSearchResponse sibeSearchResponse = new SibeSearchResponse();
        sibeSearchResponse.setStatus(0);
        //使用的是GDS的缓存生成时间
        sibeSearchResponse.setTimeLapse(sibeSearchResponseList.get(0).getTimeLapse());
        sibeSearchResponse.setMsg("success");

        //4.对方案进行相应处理
        logger.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1 进filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");
        List<SibeRouting> routingList= transformSearchGds.filterProcessRouting(sibeSearchResponseList, sibeSearchRequest);
        logger.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.2 出filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");
        if (routingList == null || routingList.size() == 0) {
            throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_100, SibeConstants.RESPONSE_MSG_100, sibeSearchRequest.getUuid(),"search");
        }
        sibeSearchResponse.setRoutings(routingList);
        //5.转换为OTA站点的对象
        logger.debug("uuid:"+sibeSearchRequest.getUuid() +" 5.1 进transformToOta:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");
        Object otaResponse = otaSearchResponse.transformSearchResponse(sibeSearchResponse, sibeSearchRequest);
        logger.debug("uuid:"+sibeSearchRequest.getUuid() +" 5.2 出transformToOta:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

        //5. 保存站点缓存
        logger.debug("uuid:"+sibeSearchRequest.getUuid() +" 5.3 进saveOrUpdateString:"+ (System.currentTimeMillis()-sibeSearchRequest.getStartTime())/(1000) +"秒");
        gdsCacheService.saveOrUpdateString(otaResponse,sibeSearchRequest.getTripCacheOTASiteKey(),sibeSearchRequest.getOtaCacheValidTime()*60);
        logger.debug("uuid:"+sibeSearchRequest.getUuid() +" 5.4 进进saveOrUpdateString:"+ (System.currentTimeMillis()-sibeSearchRequest.getStartTime())/(1000) +"秒");
        gdsCacheService.saveDataToRedis(sibeSearchResponse,sibeSearchRequest);
        logger.debug("uuid:"+sibeSearchRequest.getUuid() +" 5.5 进saveOrUpdatedata:"+ (System.currentTimeMillis()-sibeSearchRequest.getStartTime())/(1000) +"秒");

        //7.返回查询结果
        return otaSearchResponse;
    }




    /**
     * 判断缓存是否存在，如果存在则返回缓存值
     * 如果存在Key且缓存刷新时间没有过期，则返回0 和缓存结果
     * 如果存在Key且缓存刷新时间已经过期，则返回1 和缓存结果
     * 如果不存在Key，则返回2 和缓存结果
     * 如果存在Key,Value=wait，则返回3 和缓存结果
     *
     * @param <T>               the type parameter
     * @param sibeSearchRequest the ota search request
     * @return the list
     * @throws Exception the technical exception
     */
    private   <T extends SibeBaseResponse> SibeCacheResponse isOTASiteCacheExist(SibeSearchRequest sibeSearchRequest) throws Exception {

        Integer otaCacheRefreshTime =  sibeSearchRequest.getOtaCacheRefreshTime();
        Integer otaCacheValidTime =  sibeSearchRequest.getOtaCacheValidTime();
        String tripCacheOTASiteKey = sibeSearchRequest.getTripCacheOTASiteKey();

        //注意：时间单位分钟
        if (otaCacheRefreshTime == null || otaCacheRefreshTime < 1 ){
            otaCacheRefreshTime = 5;
        }

//        Optional<T> cacheObj = redisAirlineSolutionsService.findString(tripCacheOTASiteKey);
        Optional<T> cacheObj =null;

         T searchResponse=null;
        if(cacheObj.isPresent()){
            searchResponse=cacheObj.get();
        }

        String cacheExist;
        if (searchResponse == null) {
            // 如果不存在Key
            cacheExist = "2";
        } else {
            Long cacheCreateTimeLapse = searchResponse.getTimeLapse();
            if (cacheCreateTimeLapse != null && cacheCreateTimeLapse > 0) {
                long now = SystemClock.now();
                //转换为分钟值
                long otaCacheTimeDiffFromNow = (now - cacheCreateTimeLapse) / (1000 * 60);

                if (otaCacheTimeDiffFromNow > otaCacheValidTime) {
                    //存在无效的Key
                    cacheExist = "2";
                }else if (otaCacheTimeDiffFromNow > otaCacheRefreshTime) {
                    //如果存在Key且缓存刷新时间已经过期，则返回1 和缓存结果
                    cacheExist = "1";
                } else {
                    //存在Key且缓存刷新时间没有过期，则返回0 和缓存结果
                    cacheExist = "0";
                }
            } else {
                // 如果不存在Key
                cacheExist = "2";
            }
        }
        return new SibeCacheResponse(cacheExist,searchResponse);
    }

    /**
     * 校验CID
     *
     * @param sibeSearchRequest 基础search信息
     * @param sibeProperties    the sibe properties
     */
    private void cidValidate(final SibeSearchRequest sibeSearchRequest, SibeProperties sibeProperties) {
        String uuid = sibeSearchRequest.getUuid();
        if (!sibeProperties.getOta().getCid().equals(sibeSearchRequest.getCid())) {
            logger.error("uuid:" + sibeSearchRequest.getUuid() + " 错误" + "cid error or format error");
            throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_1, "cid ERROR" + SibeConstants.RESPONSE_MSG_1, uuid, "search");
        }
    }

    /**
     * 校验站点开关是否打开
     * @param sibeSearchRequest  基础search信息
     */
    private void siteCloseValidate (final SibeSearchRequest sibeSearchRequest) {

        String uuid=sibeSearchRequest.getUuid();
//        String otaSwitchKey =  RedisCacheKeyUtil.generateOtaSwitchKey(sibeSearchRequest.getSite());
//        SystemComTypeValueRedis systemComTypeValueRedis  =systemComTypeValueCaffeineRepository.findOne(otaSwitchKey);
        SiteRulesSwitch siteRulesSwitch= null;
        if(siteRulesSwitch==null){
            logger.error("uuid:"+ sibeSearchRequest.getUuid()+" "+sibeSearchRequest.getSite()+" 站点开关"+siteRulesSwitch.getParameterName() +"为空，redis没有获得值！");
        } else if(!"TRUE".equals(siteRulesSwitch.getParameterValue())){
            throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_999, sibeSearchRequest.getSite()+
                    "站点已关闭,或者没有获取到cache, Value:"+ siteRulesSwitch.getParameterValue(), uuid,"search");
        }

    }


}
