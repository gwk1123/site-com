package com.sibecommon.utils.async.impl;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.sibecommon.ota.site.SibeRoute;
import com.sibecommon.repository.entity.AllAirports;
import com.sibecommon.repository.entity.GdsPcc;
import com.sibecommon.repository.entity.SiteRulesSwitch;
import com.sibecommon.service.impl.SibeSearchCommServiceImpl;
import com.sibecommon.service.transform.SibeUtil;
import com.sibecommon.utils.constant.Constants;
import com.sibecommon.utils.constant.SibeConstants;
import com.sibecommon.utils.exception.CustomSibeException;
import com.sibecommon.utils.redis.GdsCacheService;
import com.sibecommon.config.SibeProperties;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.ota.site.SibeSearchResponse;
import com.sibecommon.sibe.SibeSearchCommService;
import com.sibecommon.utils.async.AsynchronousRefreshService;
import com.sibecommon.utils.async.SibeSearchAsyncService;
import com.sibecommon.utils.redis.impl.AllAirportRepositoryImpl;
import com.sibecommon.utils.redis.impl.GdsPccRepositoryImpl;
import com.sibecommon.utils.redis.impl.SiteRulesSwitchRepositoryImpl;
import com.sibecommon.utils.redis.util.RedisCacheKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * The type Search util.
 */
@Component
public class SibeSearchAsyncServiceImpl implements SibeSearchAsyncService {

    private final Logger LOGGER = LoggerFactory.getLogger(SibeSearchAsyncServiceImpl.class);


    @Autowired
    private SibeSearchCommService sibeSearchCommService;
    @Autowired
    private SibeProperties sibeProperties;
    @Autowired
    private AsynchronousRefreshService asynchronousRefreshService;
    @Autowired
    private GdsCacheService gdsCacheService;
    @Autowired
    private GdsPccRepositoryImpl gdsPccRepositoryImpl;
    @Autowired
    private SiteRulesSwitchRepositoryImpl siteRulesSwitchRepositoryImpl;



    /**
     * B2C查询
     *
     * @param sibeSearchRequest the sibe search request
     * @throws Exception the exception
     */
//    @Async("requestGdsExecutor")
    @Override
    public void requestGdsAsyncB2C(SibeSearchRequest sibeSearchRequest) {

        //缓存cacheKey
        String redisKey = RedisCacheKeyUtil.getAirlineCacheKey(sibeSearchRequest);
        if(sibeSearchRequest.getStartTime() == null) {
            sibeSearchRequest.setStartTime(SystemClock.now());
        }
        //Redis Set KEY (sibe+站点代码）
        sibeSearchRequest.setTripCacheKey(redisKey);
        sibeSearchRequest.setSite("");//todo 暂时传入空字符串，避免后续流程报错

        sibeSearchCommService.transformCity(sibeSearchRequest);

        if(sibeSearchRequest.getFromCityRedis()==null || sibeSearchRequest.getToCityRedis()==null) {
            LOGGER.warn("uuid:" + sibeSearchRequest.getUuid() +
                " 出发地"+sibeSearchRequest.getFromCity()
                +"或者目的地"+sibeSearchRequest.getToCity()
                +"不合法："+sibeSearchRequest.getTripCacheKey()
                + " 耗费时间：" + (SystemClock.now()-sibeSearchRequest.getStartTime()));
            throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_106, "出发地目的地不合法 "+ SibeConstants.RESPONSE_MSG_999, sibeSearchRequest.getUuid(),"UnKnow");
        }

        //10.获得该出发地和目的地的优先级列表
        List<String> cityList = SibeUtil.getCityPriority(sibeSearchRequest.getFromCityRedis(), sibeSearchRequest.getToCityRedis());
        sibeSearchRequest.setCityPrioritycList(cityList);

        List<GdsPcc> sibeGdsPccRedis = gdsPccRepositoryImpl.findAll();
        sibeSearchRequest.setGdsPccs(sibeGdsPccRedis);

//        List<SiteRulesSwitch> gdsSiteRulesSwitchs = siteRulesSwitchRepositoryImpl.findSiteRulesSwitchesByGroupKey("GDS_SWITCH");
//        sibeSearchRequest.setGdsSiteRulesSwitchs(gdsSiteRulesSwitchs);

        sibeSearchCommService.constructSibeSearchRequestByRoute(sibeSearchRequest);
        SibeSearchCommServiceImpl.getGDSCacheTimeSetting(sibeSearchRequest);

        sibeSearchRequest.setAdultNumber(1);
        sibeSearchRequest.setChildNumber(0);
        sibeSearchRequest.setInfantNumber(0);
        sibeSearchRequest.setDatakey(sibeProperties.getOta().getDataKey());

        asynchronousRefreshService.updateSearchOtaCache(sibeSearchCommService.search(sibeSearchRequest),sibeSearchRequest,Constants.METHOD_TYPE_SEARCH,0);

    }

//
//    /**
//     * 验价，组装Search请求参数,并进行异步请求GDS.
//     *
//     * @param sibeVerifyRequest the ota verify request
//     * @throws Exception the exception
//     */
//    @Async("requestGdsExecutor")
//    @Override
//    public void requestGdsAsync(SibeVerifyRequest sibeVerifyRequest) {
//        if(sibeVerifyRequest.getStartTime() == null) {
//            sibeVerifyRequest.setStartTime(SystemClock.now());
//        }
//
//        //验价，组装Search请求参数,并进行异步请求GDS
//        SibeSearchRequest sibeSearchRequest = TransformCommonOta.convertVerifyRequestToSibe(sibeVerifyRequest);
//
//        sibeSearchCommService.constructSibeSearchRequestSecond(sibeSearchRequest);
//        sibeSearchCommService.constructSibeSearchRequestByRoute(sibeSearchRequest);
//        SibeSearchCommServiceImpl.getGDSCacheTimeSetting(sibeSearchRequest);
//
//        //删除GDS缓存，暂时同程删除
//        if("LY".equals(sibeVerifyRequest.getOta())){
//            redisAirlineSolutionsSibeService.delete(sibeSearchRequest.getTripCacheKey());
//        }
//        requestGdsAsync(sibeSearchRequest, Constants.METHOD_TYPE_VERIFY);
//    }


    /**
     * 如果存在Key且缓存刷新时间已经过期，则返回1 和缓存结果
     *
     * @param sibeSearchRequest the ota search request
     * @throws Exception the exception
     */
    @Async("requestGdsExecutor")
    @Override
    public void requestGdsAsync(SibeSearchRequest sibeSearchRequest, int methodType){

        if(sibeSearchRequest.getStartTime() == null) {
            sibeSearchRequest.setStartTime(SystemClock.now());
        }
        //存在Key且缓存刷新时间已经过期,发起GDS请求 要求异步处理
        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 异步请求GDS:"+sibeSearchRequest.getTripCacheKey());

        //6.异步更新缓存GDS响应结果，并刷新其平台的缓存，需要先删除原来的缓存，再更新缓存
        //启用异步刷新search（来自verify和order的异步search），必须先删除缓存，避免无法更新站点缓存
        if (methodType != Constants.METHOD_TYPE_SEARCH) {
            deleteOTASiteCacheKey(sibeSearchRequest);
        }

        //异步搜索的时候请求GDS的时候，先判断幂等性，并发请求重复请求GDS问题(暂时只处理搜索)
        if(methodType == Constants.METHOD_TYPE_SEARCH&& sibeProperties.getGds().isSearchIdempotent()) {
            Map<String, SibeRoute> refreshGDSMap = getRefreshRouteMap(sibeSearchRequest);
            //如果refreshGDSMap长度为0，说明不需要请求GDS,所有的searchPCC已经有其他线程在请求中，放弃这次异步请求
            if (refreshGDSMap.size() == 0) {
                return;
            }
        }
        // 2. 请求GDS
        List<SibeSearchResponse> sibeSearchResponseList = sibeSearchCommService.search(sibeSearchRequest);

       if(null !=sibeSearchResponseList && sibeSearchResponseList.size() > 0 ) {
           asynchronousRefreshService.updateSearchOtaCache(sibeSearchResponseList, sibeSearchRequest, methodType, 1);
       }

    }

    /**
     *
     * @param sibeSearchRequest  sibeSearchRequest
     * @return 并发搜索幂等性问题
     */
    private Map<String, SibeRoute> getRefreshRouteMap(SibeSearchRequest sibeSearchRequest) {

        Map<String, SibeRoute> refreshGDSMap = new HashMap<>(8);
        Map<String, SibeRoute> requestRouteMap;
        if (sibeSearchRequest.getRefreshGDSMap() != null && sibeSearchRequest.getRefreshGDSMap().size() > 0) {
            requestRouteMap = sibeSearchRequest.getRefreshGDSMap();
        } else {
            requestRouteMap = sibeSearchRequest.getSearchRouteMap();
        }
        for (Map.Entry<String, SibeRoute> entry : requestRouteMap.entrySet()) {
            String key = sibeSearchRequest.getTripCacheKey() + "_" + entry.getKey() + "_" + Constants.IDEMPOTENT_SEARCH_KEY;
            //1.1.从redis查找，如果没有查找到key，就加入refreshGDsMap,并且存入到redis中
            //2.2.如果查找到redis,不加入refreshGDsMap,说明有其他的请求已经在请求GDS了。
            Object result = gdsCacheService.findSaveSearchKey(key);
            if (result == null) {
                gdsCacheService.saveSearchKey(key);
                refreshGDSMap.put(entry.getKey(), entry.getValue());
            } else {
                LOGGER.info( sibeSearchRequest.getTripCacheKey() + "_" + entry.getKey()+":节省一次GDS请求");
            }
            sibeSearchRequest.setRefreshGDSMap(refreshGDSMap);
        }
        return refreshGDSMap;
    }

    /***
     * 删除该行程key对应的站点缓存
     * @param sibeSearchRequest
     */
    public void deleteOTASiteCacheKey(SibeSearchRequest sibeSearchRequest) {
        String siteInfo = sibeProperties.getRedis().getRefreshOtaSites();
        if(StringUtils.isNotBlank(siteInfo)){
            String[] siteInfoArray = siteInfo.split(",");
            StringBuilder builder = new StringBuilder();
            Arrays.stream(siteInfoArray).forEach(item-> {
                builder.setLength(0);
                builder.append(sibeSearchRequest.getTripCacheKey()).append("_").append(item);
                gdsCacheService.delete(builder.toString());
            });
        }

    }

    /**
     * 站点缓存刷新时间过期后，异步请求GDS缓存
     */
    @Async("requestGdsExecutor")
    @Override
    public void asynRequestGDSBySiteCacheExpired(SibeSearchRequest sibeSearchRequest) {
        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 1 进入requestGds:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

        //因为站点缓存已经超过刷新时间，必须删除站点缓存
        gdsCacheService.delete(sibeSearchRequest.getTripCacheOTASiteKey());

        sibeSearchCommService.constructSibeSearchRequestByRoute(sibeSearchRequest);
        sibeSearchCommService.getGDSCache(sibeSearchRequest);
        if(sibeSearchRequest.getRefreshGDSMap() != null && sibeSearchRequest.getRefreshGDSMap().size() > 0){
            requestGdsAsync(sibeSearchRequest, Constants.METHOD_TYPE_SEARCH);
        }

    }

}
