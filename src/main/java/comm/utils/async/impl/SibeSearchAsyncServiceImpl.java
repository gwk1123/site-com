package comm.utils.async.impl;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import comm.config.SibeProperties;
import comm.ota.site.SibeRoute;
import comm.ota.site.SibeSearchRequest;
import comm.ota.site.SibeSearchResponse;
import comm.sibe.SibeSearchCommService;
import comm.utils.async.SibeSearchAsyncService;
import comm.utils.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * The type Search util.
 */
@Component
public class SibeSearchAsyncServiceImpl implements SibeSearchAsyncService {

    private final Logger LOGGER = LoggerFactory.getLogger(com.sxy.sibecommon.service.impl.sibe.SibeSearchAsyncServiceImpl.class);


    @Autowired
    private SibeGdsPccRedisRepository sibeGdsPccRedisRepository;

    @Autowired
    private SibeProperties sibeProperties;

    @Autowired
    private RedisSearchIdempotencyRepository redisSearchIdempotencyRepository;

    /**
     * B2C查询
     *
     * @param sibeSearchRequest the sibe search request
     * @throws Exception the exception
     */
    @Async("requestGdsExecutor")
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

        BaseCityRedis fromCityRedis = baseCityRedisRepository.findOne(sibeSearchRequest.getFromCity());
        BaseCityRedis toCityRedis = baseCityRedisRepository.findOne(sibeSearchRequest.getToCity());

        //todo 需要进一步优化，考虑平台请求是机场码，还是城市码
        if(fromCityRedis==null){
            BaseAirportRedis fromAirportRedis= baseAirportRedisRepository.findOne(sibeSearchRequest.getFromCity());
            if(fromAirportRedis!=null) {
                fromCityRedis = new BaseCityRedis();
                fromCityRedis.setAreaCode(fromAirportRedis.getAreaCode());
                fromCityRedis.setCityCode(fromAirportRedis.getCityCode());
                fromCityRedis.setCountryCode(fromAirportRedis.getCountryCode());
                fromCityRedis.setZoneCode(fromAirportRedis.getZoneCode());
            }
        }
        if (toCityRedis==null){
            BaseAirportRedis toAirportRedis =baseAirportRedisRepository.findOne(sibeSearchRequest.getToCity());
            if(toAirportRedis!=null) {
                // LOGGER.debug("uuid:"+sibeSearchRequest.getUuid()+" 目的地为机场码:"+sibeSearchRequest.getToCity()  );
                toCityRedis = new BaseCityRedis();
                toCityRedis.setAreaCode(toAirportRedis.getAreaCode());
                toCityRedis.setCityCode(toAirportRedis.getCityCode());
                toCityRedis.setCountryCode(toAirportRedis.getCountryCode());
                toCityRedis.setZoneCode(toAirportRedis.getZoneCode());
            }
        }

        if(fromCityRedis==null || toCityRedis==null) {
            LOGGER.warn("uuid:" + sibeSearchRequest.getUuid() +
                " 出发地"+sibeSearchRequest.getFromCity()
                +"或者目的地"+sibeSearchRequest.getToCity()
                +"不合法："+sibeSearchRequest.getTripCacheKey()
                + " 耗费时间：" + (SystemClock.now()-sibeSearchRequest.getStartTime()));
            throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_106, "出发地目的地不合法 "+ SibeConstants.RESPONSE_MSG_999, sibeSearchRequest.getUuid(),"UnKnow");
        }

        sibeSearchRequest.setFromCityRedis(fromCityRedis); //出发地
        sibeSearchRequest.setToCityRedis(toCityRedis); //目的地


        //10.获得该出发地和目的地的优先级列表
        List<String> cityList = SibeUtil.getCityPriority(sibeSearchRequest.getFromCityRedis(), sibeSearchRequest.getToCityRedis());
        sibeSearchRequest.setCityPrioritycList(cityList);

        Set<SibeGdsPccRedis> sibeGdsPccRedis = sibeGdsPccRedisRepository.findAll();
        sibeSearchRequest.setSibeGdsPccRedis(sibeGdsPccRedis);

        Set<SystemComTypeValueRedis> gdsSwitchValueRedisSet =systemComTypeValueRedisRepository.findByTypeAndDetCode("_API_SYSTEM_BASE_DATA","GDS_SWITCH_"+sibeSearchRequest.getGds());
        sibeSearchRequest.setGdsSwitchValueRedisSet(gdsSwitchValueRedisSet);

        sibeSearchCommService.constructSibeSearchRequestByRoute(sibeSearchRequest);
        SibeSearchCommServiceImpl.getGDSCacheTimeSetting(sibeSearchRequest);

        //todo
        if(sibeSearchRequest.getSearchRouteMap().get("LCC-PYTON") != null){
            if(sibeSearchRequest.getB2CSearchSiteInfoList() != null && sibeSearchRequest.getB2CSearchSiteInfoList().size() > 0){
                boolean allowRquestLCCPYTON = false;
                for (B2CSearchSiteInfo item : sibeSearchRequest.getB2CSearchSiteInfoList()) {
                    if ("OWT".equals(item.getOtaCode())) {
                        allowRquestLCCPYTON = true;
                        break;
                    }
                }

                if(! allowRquestLCCPYTON){
                    sibeSearchRequest.getSearchRouteMap().remove("LCC-PYTON");
                }
            }
        }


        sibeSearchRequest.setAdultNumber(1);
        sibeSearchRequest.setChildNumber(0);
        sibeSearchRequest.setInfantNumber(0);

        sibeSearchRequest.setDatakey(sibeProperties.getOta().getDataKey());

        redisAirlineSolutionsSibeService.updateRedisAirlineSolutions(sibeSearchCommService.search(sibeSearchRequest),sibeSearchRequest,Constants.METHOD_TYPE_SEARCH,0);

    }


    /**
     * 验价，组装Search请求参数,并进行异步请求GDS.
     *
     * @param sibeVerifyRequest the ota verify request
     * @throws Exception the exception
     */
    @Async("requestGdsExecutor")
    @Override
    public void requestGdsAsync(SibeVerifyRequest sibeVerifyRequest) {
        if(sibeVerifyRequest.getStartTime() == null) {
            sibeVerifyRequest.setStartTime(SystemClock.now());
        }

        //验价，组装Search请求参数,并进行异步请求GDS
        SibeSearchRequest sibeSearchRequest = TransformCommonOta.convertVerifyRequestToSibe(sibeVerifyRequest);

        sibeSearchCommService.constructSibeSearchRequestSecond(sibeSearchRequest);
        sibeSearchCommService.constructSibeSearchRequestByRoute(sibeSearchRequest);
        SibeSearchCommServiceImpl.getGDSCacheTimeSetting(sibeSearchRequest);

        //删除GDS缓存，暂时同程删除
        if("LY".equals(sibeVerifyRequest.getOta())){
            redisAirlineSolutionsSibeService.delete(sibeSearchRequest.getTripCacheKey());
        }
        requestGdsAsync(sibeSearchRequest, Constants.METHOD_TYPE_VERIFY);
    }


    /**
     * 生单，组装Search请求参数,并进行异步请求GDS
     *
     * @param sibeOrderRequest the ota order request
     * @throws Exception the exception
     */
    @Async("requestGdsExecutor")
    @Override
    public void requestGdsAsync(SibeOrderRequest sibeOrderRequest) {
        if(sibeOrderRequest.getStartTime() == null) {
            sibeOrderRequest.setStartTime(SystemClock.now());
        }

        //生单，组装Search请求参数,并进行异步请求GDS
        SibeSearchRequest sibeSearchRequest = TransformCommonOta.convertOrderRequestToSibe(sibeOrderRequest);
        sibeSearchCommService.constructSibeSearchRequestSecond(sibeSearchRequest);
        sibeSearchCommService.constructSibeSearchRequestByRoute(sibeSearchRequest);
        SibeSearchCommServiceImpl.getGDSCacheTimeSetting(sibeSearchRequest);

        //删除GDS缓存，暂时同程删除
        if("LY".equals(sibeOrderRequest.getOta())) {
            redisAirlineSolutionsSibeService.delete(sibeSearchRequest.getTripCacheKey());
        }
        requestGdsAsync(sibeSearchRequest, Constants.METHOD_TYPE_ORDER);
    }


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
           redisAirlineSolutionsSibeService.updateRedisAirlineSolutions(sibeSearchResponseList, sibeSearchRequest, methodType, 1);
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
            Object result = redisSearchIdempotencyRepository.findSaveSearchKey(key);
            if (result == null) {
                redisSearchIdempotencyRepository.saveSearchKey(key);
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
                redisAirlineSolutionsSibeService.delete(builder.toString());
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
        redisAirlineSolutionsSibeService.delete(sibeSearchRequest.getTripCacheOTASiteKey());

        sibeSearchCommService.constructSibeSearchRequestByRoute(sibeSearchRequest);
        sibeSearchCommService.getGDSCache(sibeSearchRequest, redisAirlineSolutionsSibeService);
        if(sibeSearchRequest.getRefreshGDSMap() != null && sibeSearchRequest.getRefreshGDSMap().size() > 0){
            requestGdsAsync(sibeSearchRequest, Constants.METHOD_TYPE_SEARCH);
        }

    }

}
