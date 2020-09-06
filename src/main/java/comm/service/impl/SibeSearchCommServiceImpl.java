package comm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import comm.ota.site.SibeObtainGDSCache;
import comm.ota.site.SibeRoute;
import comm.ota.site.SibeSearchRequest;
import comm.ota.site.SibeSearchResponse;
import comm.repository.entity.AllAirports;
import comm.repository.entity.GdsRule;
import comm.service.transform.SibeUtil;
import comm.sibe.SibeSearchCommService;
import comm.utils.constant.SibeConstants;
import comm.utils.exception.CustomSibeException;
import comm.utils.redis.GdsCacheService;
import comm.utils.redis.impl.AllAirportRepositoryImpl;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class SibeSearchCommServiceImpl implements SibeSearchCommService {

    private Logger logger=LoggerFactory.getLogger(SibeSearchCommServiceImpl.class);
    @Autowired
    private AllAirportRepositoryImpl allAirportRepository;
    @Autowired
    private GdsCacheService gdsCacheService;


    @Override
    public void constructSibeSearchRequestSecond(SibeSearchRequest sibeSearchRequest) {

        //1.生成缓存主key 出发地目的地出发时间抵达时间
        String tripCacheKey = RedisCacheKeyUtil.getAirlineCacheKey(sibeSearchRequest);

        //2.生成缓存站点key (sibe+站点代码）
        sibeSearchRequest.setTripCacheKey(tripCacheKey);
        sibeSearchRequest.setTripCacheOTASiteKey(tripCacheKey + "_" + sibeSearchRequest.getOta() + "-" + sibeSearchRequest.getSite()); //todo lcc

        // LOGGER.debug("uuid:"+sibeSearchRequest.getUuid()+" 缓存redisKey:"+redisKey  );
        //3.出发地和目的地处理
        boolean placeValidateResult = processDepartureArrivalPlace(sibeSearchRequest);
        if (!placeValidateResult) {
            logger.warn("uuid:" + sibeSearchRequest.getUuid() +
                    " 出发地" + sibeSearchRequest.getFromCity()
                    + "或者目的地" + sibeSearchRequest.getToCity()
                    + "不合法：" + sibeSearchRequest.getTripCacheOTASiteKey()
                    + " 耗费时间：" + (SystemClock.now() - sibeSearchRequest.getStartTime()));
            throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_106, "出发地目的地不合法 " + SibeConstants.RESPONSE_MSG_999, sibeSearchRequest.getUuid(), "search");
        }

        //4.查找GDS开关
//        Set<SystemComTypeValueRedis> gdsSwitchValueRedisSet =systemComTypeValueCaffeineRepository.findByTypeAndDetCode("_API_SYSTEM_BASE_DATA","GDS_SWITCH_"+sibeSearchRequest.getGds());
//        sibeSearchRequest.setGdsSwitchValueRedisSet(gdsSwitchValueRedisSet);

        //5.查找OTA开关
//        Set<SystemComTypeValueRedis>  otaSwitchValueRedisSet = systemComTypeValueCaffeineRepository.findByTypeAndDetCode("_API_SYSTEM_BASE_DATA","OTA_SITE_SWITCH_"+sibeSearchRequest.getSite());
//        sibeSearchRequest.setOtaSwitchValueRedisSet(otaSwitchValueRedisSet);


        //7.查找GDS规则
//        Set<ApiControlRuleGdsRedis> apiControlRuleGdsRedisSet = apiControlRuleGdsCaffeineRepository.findAll();
//        sibeSearchRequest.setGdsRuleRedisSet(apiControlRuleGdsRedisSet);

        //8.查找OTA规则
//        Set<ApiControlRuleOtaRedis> apiControlRuleOtaRedisSet = apiControlRuleOtaCaffeineRepository.findBySite(sibeSearchRequest.getSite());
//        sibeSearchRequest.setOtaRuleRedisSet(apiControlRuleOtaRedisSet);

        //9.查找PCC数据
//        Set<SibeGdsPccRedis> sibeGdsPccRedis = sibeGdsPccCaffeineRepository.findAll();
//        sibeSearchRequest.setSibeGdsPccRedis(sibeGdsPccRedis);

        //10.获得该出发地和目的地的优先级列表
        List<String> cityList = SibeUtil.getCityPriority(sibeSearchRequest.getFromCityRedis(), sibeSearchRequest.getToCityRedis());
        sibeSearchRequest.setCityPrioritycList(cityList);
        //11.设置路由
//        constructSibeSearchRequestByRoute(sibeSearchRequest);

        //12.获取GDS缓存时间配置
//        getGDSCacheTimeSetting(sibeSearchRequest);

        //13.获取站点缓存时间配置
//        OtaRuleFilter.getCacheRefreshTime(sibeSearchRequest); //todo lcc

        //14.请求超时时间配置
//        OtaRuleFilter.getTimeOutTime(sibeSearchRequest);//todo lcc

        //15.获得限制止损值
//        OtaRuleFilter.restrictedStopLoss(sibeSearchRequest);


    }


    /**
     * 校验出发地和目的地是否合法，合法返回true，非法返回false
     * 并且将出发地和目的地设置到sibeSearchRequest
     * 如果出发地和目的地为机场码，则自动转换为城市码
     * @param sibeSearchRequest
     * @return
     */
    private boolean processDepartureArrivalPlace(SibeSearchRequest sibeSearchRequest) {

        AllAirports fromAllAirport = null;
        AllAirports toAllAirport = null;
        List<AllAirports> fromCityList = allAirportRepository.findAllAirportsByCity(sibeSearchRequest.getFromCity());
        List<AllAirports> toCityList = allAirportRepository.findAllAirportsByCity(sibeSearchRequest.getToCity());

        if (CollectionUtils.isEmpty(fromCityList)) {
            AllAirports allAirport = allAirportRepository.findAllAirportByAirport(sibeSearchRequest.getFromCity());
            if (allAirport != null) {
                fromAllAirport = this.conversionCity(allAirport);
            }
        } else {
            fromAllAirport = this.conversionCity(fromCityList.get(0));
        }

        if (CollectionUtils.isEmpty(toCityList)) {
            AllAirports allAirport = allAirportRepository.findAllAirportByAirport(sibeSearchRequest.getToCity());
            if (allAirport != null) {
                toAllAirport = this.conversionCity(allAirport);
            }
        }else {
            toAllAirport = this.conversionCity(toCityList.get(0));
        }

        sibeSearchRequest.setFromCityRedis(fromAllAirport); //出发地
        // LOGGER.debug("uuid:"+sibeSearchRequest.getUuid()+" 出发地:"+fromCityRedis.toString()  );
        sibeSearchRequest.setToCityRedis(toAllAirport); //目的地
        // LOGGER.debug("uuid:"+sibeSearchRequest.getUuid()+" 目的地:"+toCityRedis.toString()  );

        if (fromAllAirport == null || toAllAirport == null) {
            return false;
        } else {
            return true;
        }

    }


    public AllAirports conversionCity(AllAirports allAirport){
        AllAirports city = new AllAirports();
        city.setCcode(allAirport.getCcode());
        city.setGcode(allAirport.getGcode());
        city.setQcode(allAirport.getQcode());
        city.setZcode(allAirport.getZcode());
        return city;
    }


    /**
     * 获取GDS缓存，并且生成BusinessSearchRouteMap
     * @param sibeSearchRequest
     * @return
     */
    @Override
    public SibeObtainGDSCache getGDSCache(SibeSearchRequest sibeSearchRequest) {

        getGDSCacheTimeSetting(sibeSearchRequest);

        List<SibeSearchResponse> sibeSearchResponseList = new ArrayList<>();

        Map<String, SibeRoute> businessSearchRouteMap = new HashMap<>();

        //0.根据原始航线路由与站点选择GDS数据源得到可以请求的航线路
        Map<String,SibeRoute> siteAllowSearchRouteMap = generateAllowSearchRouteMap(sibeSearchRequest);

        //key :GDS-PCC,value :GDS-PCC
        //不需要刷新的GDS map
        Map<String,String> noNeedToRefreshMap = new HashMap<>();

        //1.查找GDS缓存的内容
        Set<String> cacheKeySet = redisAirlineSolutionsSibeService.findAllKeys(sibeSearchRequest.getTripCacheKey());

        //2.根据GDS缓存内容与站点的航线选择GDS数据，进行交集，查找出具体的GDS缓存，并且放入缓存responseList中
        for (String redisCacheKey : cacheKeySet) {

            //删除GDS除路由外，的多余配置数据
            excessGDSpcc(sibeSearchRequest, redisCacheKey) ;
            //仅仅获取符合GDS 允许map中的缓存数据
            if(siteAllowSearchRouteMap.get(redisCacheKey) == null){
                continue;
            }

            String gdsCode = redisCacheKey.split("-")[0];
//            boolean siteAllowGDSSign = false;
//            if(org.apache.commons.lang3.StringUtils.isBlank(sibeSearchRequest.getOtaSiteAirRouteChooseGDS())){
//                siteAllowGDSSign = true;
//            }else{
//                if(sibeSearchRequest.getOtaSiteAirRouteChooseGDS().contains(gdsCode)){
//                    siteAllowGDSSign = true;
//                }
//            }
//
//            if(! siteAllowGDSSign){
//                continue;
//            }

            //选择的GDS不为空并且不存在的时跳过（GDS 配置）
            if(StringUtils.isNotBlank(sibeSearchRequest.getOtaSiteAirRouteChooseGDS()) &&
                    !sibeSearchRequest.getOtaSiteAirRouteChooseGDS().contains(gdsCode)){
                continue;
            }


            Optional<SibeSearchResponse> sibeSearchResponse = redisAirlineSolutionsSibeService.findOne(sibeSearchRequest.getTripCacheKey(), redisCacheKey);
            if (sibeSearchResponse.isPresent() && sibeSearchResponse.get() != null) {
                Long gdsCreateTimeLapse = sibeSearchResponse.get().getTimeLapse();

                //找出超过刷新时间的，加入到BusinessSearchRouteMap业务路由中
                if(gdsCreateTimeLapse != null){
                    long now = SystemClock.now();
                    long gdsCacheDiffTimeFromNow = (now - gdsCreateTimeLapse) / (1000 * 60);
                    if (gdsCacheDiffTimeFromNow >= sibeSearchRequest.getGdsCacheRefreshTimeMap().get(gdsCode)) {
                        if(sibeSearchRequest.getSearchRouteMap().get(redisCacheKey) != null){
                            businessSearchRouteMap.put(redisCacheKey, sibeSearchRequest.getSearchRouteMap().get(redisCacheKey));
                        }
                    }else {
                        noNeedToRefreshMap.put(redisCacheKey, redisCacheKey);
                    }

                    //找出未达到有效时间的，加入到GDS缓存list中  判断没有意义，且不同GDS数据的缓存有效时间不一样
//                    if(gdsCacheDiffTimeFromNow < sibeSearchRequest.getGdsCacheValidTime()){
//                        sibeSearchResponseList.add(sibeSearchResponse.get());
//                    }
                    sibeSearchResponseList.add(sibeSearchResponse.get());
                }
            }
        }


        siteAllowSearchRouteMap.forEach((k,v) -> {
            if(businessSearchRouteMap.get(k) == null && noNeedToRefreshMap.get(k) == null){
                businessSearchRouteMap.put(k, v);
            }
        });

        sibeSearchRequest.setRefreshGDSMap(businessSearchRouteMap);
        sibeSearchResponseList = sibeSearchResponseList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        SibeObtainGDSCache sibeObtainGDSCache = new SibeObtainGDSCache();
        sibeObtainGDSCache.setSibeSearchResponseList(sibeSearchResponseList);
        return sibeObtainGDSCache;

    }


    /**
     *  删除GDS缓存 路由外多余的PCC 缓存
     * @param sibeSearchRequest
     * @return
     */

    @Override
    @Async("repositoryTaskExecutor")
    public void excessGDSpcc(SibeSearchRequest sibeSearchRequest,String key) {
        if(null == sibeSearchRequest.getSearchRouteMap().get(key)){
            gdsCacheService.deleteGDS(sibeSearchRequest.getTripCacheKey(),key);
        }
    }


    public static void getGDSCacheTimeSetting(SibeSearchRequest sibeSearchRequest) {
        //设置GDS默认刷新时间
        final int gdsCacheRefreshTime = 5; //默认刷新时间5分钟
        Map<String,Integer> gdsRefreshTimeMap = new HashMap<>();
        sibeSearchRequest.getSearchRouteMap().forEach((k,v)->{
            gdsRefreshTimeMap.put(k.split("-")[0], gdsCacheRefreshTime);
        });

        sibeSearchRequest.setGdsCacheRefreshTimeMap(gdsRefreshTimeMap);

        //设置GDS默认有效时间
        sibeSearchRequest.setGdsCacheValidTime(10);//默认有效时间 10 分钟

        String gdsCacheRefreshTimeInfo = "";

        final String Rule_TYPE = "25";
        String parentKey = "GDS-25";
        String travelDateRange = "1";
        String exceptForTravelDateRange="2";
        int gdsCacheValidTime = 10; //默认缓存时间10分钟

        if(sibeSearchRequest
                .getSiteRulesSwitch()
                .stream()
                .anyMatch(gdsRule ->(
                        parentKey.equals(gdsRule.getParameterKey()) && !"TRUE".equals(gdsRule.getParameterValue())))){
            sibeSearchRequest.setGdsCacheValidTime(gdsCacheValidTime);
            return;
        }
        List<String> cityList= sibeSearchRequest.getCityPrioritycList();
        List<GdsRule> apiControlRuleGdsRedisList=new ArrayList<>();
        loop: for(String city:cityList){
            String[] cityArray=city.split("/");
            sibeSearchRequest
                    .getGdsRuleSet()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(gdsRedis -> (
                            Rule_TYPE.equals(gdsRedis.getRuleType())
                                    && SibeUtil.contains(gdsRedis.getOrigin(),cityArray[0],"/")
                                    && SibeUtil.contains(gdsRedis.getDestination(),cityArray[1],"/")
//                                    && TravelDateRange(sibeSearchRequest,gdsRedis.getParameter3(),travelDateRange)
//                                    && TravelDateRange(sibeSearchRequest,gdsRedis.getParameter4(),exceptForTravelDateRange)
                    )).forEach(apiControlRuleGdsRedisList::add);
            if(apiControlRuleGdsRedisList.size()>0){
                break loop;
            }
        }
        if (apiControlRuleGdsRedisList.size()>0 && apiControlRuleGdsRedisList != null) {

            if(apiControlRuleGdsRedisList.get(0).getParameter1() != null) {
                gdsCacheValidTime = Integer.parseInt(apiControlRuleGdsRedisList.get(0).getParameter1());
            }
            if(apiControlRuleGdsRedisList.get(0).getParameter2() != null) {
                gdsCacheRefreshTimeInfo = apiControlRuleGdsRedisList.get(0).getParameter2();
            }
        }
        sibeSearchRequest.setGdsCacheValidTime(gdsCacheValidTime);

        if (StringUtils.isNotBlank(gdsCacheRefreshTimeInfo)) {
            String[] gdsCacheRefreshTimeArray = gdsCacheRefreshTimeInfo.split("/");
            Arrays.stream(gdsCacheRefreshTimeArray).forEach(item -> {
                String[] info = item.split(":");
                gdsRefreshTimeMap.put(info[0], Integer.parseInt(info[1]));
            });
        }
        sibeSearchRequest.setGdsCacheRefreshTimeMap(gdsRefreshTimeMap);
    }


}
