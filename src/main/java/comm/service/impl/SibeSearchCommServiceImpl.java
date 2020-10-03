package comm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import comm.config.SibeProperties;
import comm.feign.AmadeusFeignSearchClient;
import comm.feign.GalileoFeignSearchClient;
import comm.ota.gds.GDSSearchResponseDTO;
import comm.ota.site.*;
import comm.repository.entity.*;
import comm.service.handler.GdsRequestRuleCreator;
import comm.service.ota.OtaRuleFilter;
import comm.service.transform.RouteRuleUtil;
import comm.service.transform.SibeUtil;
import comm.service.transform.TransformSearchGds;
import comm.sibe.SibeSearchCommService;
import comm.utils.async.completableFuture.CompletableFutureCollector;
import comm.utils.constant.Constants;
import comm.utils.constant.SibeConstants;
import comm.utils.exception.CustomSibeException;
import comm.utils.redis.GdsCacheService;
import comm.utils.redis.impl.*;
import comm.utils.redis.util.RedisCacheKeyUtil;
//import feign.FeignException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
public class SibeSearchCommServiceImpl implements SibeSearchCommService {

    private Logger logger = LoggerFactory.getLogger(SibeSearchCommServiceImpl.class);
    @Autowired
    private AllAirportRepositoryImpl allAirportRepository;
    @Autowired
    private GdsCacheService gdsCacheService;
    @Autowired
    private SibeProperties sibeProperties;
    @Autowired
    @Qualifier(value = "taskGdsExecutor")
    private Executor requestGdsExecutor;
    @Autowired
    private GalileoFeignSearchClient galileoFeignSearchClient;
    @Autowired
    private AmadeusFeignSearchClient amadeusFeignSearchClient;
    @Autowired
    private TransformSearchGds transformSearchGds;
    @Autowired
    private GdsRequestRuleCreator gdsRequestRuleCreator;
    @Autowired
    private SiteRulesSwitchRepositoryImpl siteRulesSwitchRepositoryImpl;
    @Autowired
    private GdsRuleRepositoryImpl gdsRuleRepositoryImpl;
    @Autowired
    private CarrierCabinBlackRepositoryImpl carrierCabinBlackRepositoryImpl;
    @Autowired
    private RouteConfigRepositoryImpl routeConfigRepositoryImpl;
    @Autowired
    private OtaRuleRepositoryImpl otaRuleRepositoryImpl;

    /**
     * 请求GDS.
     *
     * @param sibeSearchRequest the ota search request
     * @return the list
     * @throws Exception the exception
     */
    @Override
    public List<SibeSearchResponse> search(SibeSearchRequest sibeSearchRequest) {

        // LOGGER.debug("uuid:"+sibeSearchRequest.getUuid()+" 开始请求GDS...");
        logger.debug("uuid:" + sibeSearchRequest.getUuid() + " 2.1  进入search:" + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");

        Map<String, SibeRoute> requestRouteMap;
        if (sibeSearchRequest.getRefreshGDSMap() != null && sibeSearchRequest.getRefreshGDSMap().size() > 0) {
            requestRouteMap = sibeSearchRequest.getRefreshGDSMap();
        } else {
            requestRouteMap = sibeSearchRequest.getSearchRouteMap();
        }

        List<CompletableFuture<SibeSearchResponse>> requestGDSByOfficeIdFutures =
                requestRouteMap.entrySet()
                        .stream() //集合大的时候，使用stream
                        .filter(Objects::nonNull)
                        .filter(searchRoute -> (
                                StringUtils.isBlank(sibeSearchRequest.getOtaSiteAirRouteChooseGDS())
                                        || (StringUtils.isNotBlank(sibeSearchRequest.getOtaSiteAirRouteChooseGDS()) && searchRoute.getValue() != null
                                        && StringUtils.contains(sibeSearchRequest.getOtaSiteAirRouteChooseGDS(), searchRoute.getValue().getSearcPcc().getGdsCode())
                                )
                        ))
                        .map(searchRoute -> {
                            //GDS请求参数
                            SibeSearchRequest newSibeSearchRequest = SibeSearchRequest.deepCopy(sibeSearchRequest);
                            newSibeSearchRequest.setGds(searchRoute.getValue().getSearcPcc().getGdsCode()); //GDS
                            newSibeSearchRequest.setOfficeId(searchRoute.getValue().getSearcPcc().getPccCode()); //OfficeId
                            logger.info("............gds = " + newSibeSearchRequest.getGds() + "....officeId = " + newSibeSearchRequest.getOfficeId());
                            //异常请求GDS
                            return CompletableFuture.supplyAsync(() -> {
                                return requestGDSByOfficeId(newSibeSearchRequest, searchRoute.getValue().getSearcPcc().getPccCode(), searchRoute.getValue().getSearcPcc().getGdsCode());
                            }, requestGdsExecutor);

                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());


        //得到CompletableFuture类型的任务集合
        CompletableFuture<Void> allFutures = requestGDSByOfficeIdFutures.stream().collect(CompletableFutureCollector.allComplete());

        logger.debug("uuid:" + sibeSearchRequest.getUuid() + " 2.2  进入search:" + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");

        //todo 暂时配置为80秒 fegin超时配置为30秒
        int seconds = sibeProperties.getGds().getFuseTime();
        logger.debug("uuid:" + sibeSearchRequest.getUuid() + " 2.2.1  进入search:" + "GDS超时熔断时间为" + seconds + "秒");
        //得到一个新的CompletableFuture对象，判断超时（20秒）
        CompletableFuture<Void> responseFuture = CompletableFutureCollector.within(allFutures, Duration.ofSeconds(seconds));

        logger.debug("uuid:" + sibeSearchRequest.getUuid() + " 2.3  进入search:" + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");

        //执行responseFuture对象，如果5秒没有全部处理完成，则抛出超时异常的异常
        responseFuture
                .exceptionally(throwable -> {
                    logger.error("uuid:" + sibeSearchRequest.getUuid()
                            + " 出发地" + sibeSearchRequest.getFromCity() + "-" + sibeSearchRequest.getToCity()
                            + " 出发日期" + sibeSearchRequest.getFromDate() + "-" + sibeSearchRequest.getRetDate()
                            + " 请求超时异常," + seconds + "秒都没有返回值", throwable);
                    return null;
                }).join();

        //返回Gds请求结果

        List<SibeSearchResponse> sibeSearchResponseList = requestGDSByOfficeIdFutures
                .stream()
                .filter(CompletableFuture::isDone)
                .filter(Objects::nonNull)
                .collect(CompletableFutureCollector.collectResult())
                .join();

        return sibeSearchResponseList.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


    /**
     * 请求GDS
     *
     * @param sibeSearchRequest the ota search request
     * @return the gds search response dto
     */
    private SibeSearchResponse requestGDSByOfficeId(SibeSearchRequest sibeSearchRequest, String officeId, String gds) {
        String uuid = sibeSearchRequest.getUuid(); //UUID
        long startTime = SystemClock.now();


        //1. GDS规则过滤（航司黑名单，航线黑名单，限制方案数量）
        try {
            gdsRequestRuleCreator.create(sibeSearchRequest);
        } catch (CustomSibeException ex) {
            logger.info("uuid:" + uuid + " GDS规则过滤CustomSibeException，将返回空", ex.getMsg());
            return null;
        } catch (Exception e) {
            logger.warn("uuid:" + uuid + " GDS规则过滤Exception，将返回空", e);
            return null;
        }

        //2. 组装请求参数并请求GDS
        ResponseEntity<GDSSearchResponseDTO> gDSSearchResponseDTO = null;

        SibeSearchResponse sibeSearchResponse = null;
        try {

            if (Constants.GDS_TYPE_1A.equals(sibeSearchRequest.getGds())) {
                String appKey = sibeProperties.getGds().getAmadeus().getAppKey();
                sibeSearchRequest.setAppKey(appKey);
                // LOGGER.debug("uuid:"+uuid+" 开始请求"+sibeSearchRequest.getGds()+",使用"+sibeSearchRequest.getOfficeId()+"配置,AppKey:"+appKey);
                gDSSearchResponseDTO = amadeusFeignSearchClient.search(TransformSearchGds.convertSearchRequestToGDS(sibeSearchRequest));

            } else if (Constants.GDS_TYPE_1G.equals(sibeSearchRequest.getGds())) {
                String appKey = sibeProperties.getGds().getGalileo().getAppKey();
                sibeSearchRequest.setAppKey(appKey);
                // LOGGER.debug("uuid:"+uuid+" 开始请求"+sibeSearchRequest.getGds()+",使用"+sibeSearchRequest.getOfficeId()+"配置,AppKey:"+appKey);
                gDSSearchResponseDTO = galileoFeignSearchClient.search(TransformSearchGds.convertSearchRequestToGDS(sibeSearchRequest));

            }

            if (gDSSearchResponseDTO == null) {
                logger.error("uuid:" + uuid + " search请求" + sibeSearchRequest.getGds()
                        + ",PCC:" + sibeSearchRequest.getOfficeId()
                        + " 响应方案数为：NULL"
                        + " 响应时间：" + (SystemClock.now() - startTime));
                return null;
            } else if (gDSSearchResponseDTO.getBody() != null && gDSSearchResponseDTO.getBody().getStatus() != 0) {
                logger.error("uuid:" + uuid + " search请求" + sibeSearchRequest.getGds()
                        + ",PCC:" + sibeSearchRequest.getOfficeId()
                        + " status:" + gDSSearchResponseDTO.getBody().getStatus()
                        + " msg:" + gDSSearchResponseDTO.getBody().getMsg()
                        + " 响应时间：" + (SystemClock.now() - startTime));
                return null;
            } else if (gDSSearchResponseDTO.getBody().getRoutings().size() == 0) {
                logger.error("uuid:" + uuid + " search请求" + sibeSearchRequest.getGds()
                        + ",PCC:" + sibeSearchRequest.getOfficeId()
                        + " status:" + gDSSearchResponseDTO.getBody().getStatus()
                        + " msg:" + gDSSearchResponseDTO.getBody().getMsg()
                        + " routing size=0"
                        + " 响应时间：" + (SystemClock.now() - startTime));
                return null;
            }
            logger.debug("uuid:" + sibeSearchRequest.getUuid() + " 0.1 requestGDSByOfficeId  " + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");
            sibeSearchResponse = transformSearchGds.convertSearchResponseToSibe(gDSSearchResponseDTO.getBody(), sibeSearchRequest);
            logger.debug("uuid:" + sibeSearchRequest.getUuid() + " 0.2 requestGDSByOfficeId  " + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");
            //GDS航司舱位黑名单处理
            processGDSRuleCarrierCabinBlackList(sibeSearchRequest, sibeSearchResponse);
            logger.debug("uuid:" + sibeSearchRequest.getUuid() + " 0.3 requestGDSByOfficeId  " + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");

        } catch (Exception e) {
            logger.error("uuid:" + uuid + " Exception search请求" + sibeSearchRequest.getGds() + ", PCC:" + sibeSearchRequest.getOfficeId() + "出错", e);
            return null;
        }

        logger.debug("uuid:" + sibeSearchRequest.getUuid() + " search请求" + sibeSearchRequest.getGds()
                + ",PCC:" + sibeSearchRequest.getOfficeId()
                + " 响应方案数为：" + gDSSearchResponseDTO.getBody().getRoutings().size()
                + " 响应时间：" + (SystemClock.now() - startTime));

        //保存GDS数据
        if (0 == sibeSearchResponse.getStatus() && null != sibeSearchResponse.getRoutings() && sibeSearchResponse.getRoutings().size() > 0) {
            logger.debug("是否刷新GDS至redis:" + sibeProperties.getRedis().isRefreshGdsSwitch());
            gdsCacheService.saveOrUpdate(sibeSearchResponse, sibeSearchRequest.getTripCacheKey(), sibeSearchResponse.getGds() + "-" + sibeSearchResponse.getOfficeId(), Long.valueOf(sibeSearchResponse.getCacheValidTime()) * 60, 1);
        }
        return sibeSearchResponse;
    }


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


    private void processGDSRuleCarrierCabinBlackList(SibeSearchRequest sibeSearchRequest, SibeSearchResponse sibeSearchResponse) {
        final String parameterKey = "GDS-18"; //航司舱位黑名单生效开关
        final String RULE_TYPE = "18"; //GDS-航司舱位黑名单生效开关
        //判断
        if (sibeSearchResponse == null || sibeSearchResponse.getRoutings() == null || sibeSearchResponse.getRoutings().size() == 0) {
            return;
        }
        //检测GDS规则-航司舱位黑名单开关
        if (sibeSearchRequest
                .getSiteRulesSwitch()
                .stream()
                .anyMatch(value -> (parameterKey.equals(value.getParameterKey()) && !"TRUE".equals(value.getParameterValue())))) {
            return;
        }

        //获取航司舱位黑名单的有效期时间
        List<GdsRule> apiControlRuleGdsRedisSet = gdsRuleRepositoryImpl.findGdsRulesByGdsCodeAndRuleType(sibeSearchRequest.getGds(), RULE_TYPE);
        Optional<GdsRule> apiControlRuleGdsRedis = apiControlRuleGdsRedisSet
                .stream()
                .filter(Objects::nonNull)
                .filter(gdsRuleRedis -> (RULE_TYPE.equals(gdsRuleRedis.getRuleType()) && sibeSearchRequest.getGds().equals(gdsRuleRedis.getGdsCode())))
                .findFirst();

        //默认6个小时 =6*60*60*1000
        int effectiveMinute = 21600000;
        if (apiControlRuleGdsRedis.isPresent() && apiControlRuleGdsRedis.get() != null) {
            effectiveMinute = apiControlRuleGdsRedis.get().getEffectiveMinute() * 60 * 1000;
        }

        //获取routingList中的所有航司
        Set<String> carriers = new HashSet<>();
        sibeSearchResponse.getRoutings().forEach(routing -> {
            routing.getFromSegments().forEach(sibeSegment -> carriers.add(sibeSegment.getCarrier()));
            routing.getRetSegments().forEach(sibeSegment -> carriers.add(sibeSegment.getCarrier()));
        });

        //根据航司从redis中获取航司舱位黑名单数据
        Set<CarrierCabinBlack> carrierCabinSet = carrierCabinBlackRepositoryImpl.findByCarriers(carriers);
        if (carrierCabinSet == null || carrierCabinSet.size() == 0) {
            return;
        }

        Date now = new Date();
        //将redis中获取出的航司舱位黑名单数据转换为map形式，key：唯一key，value具体数据
        Map<String, CarrierCabinBlack> carrierCabinBlackMap = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        final int finalEffectiveMinute = effectiveMinute;
        carrierCabinSet
                .stream()
                .filter(Objects::nonNull)
                .filter(cabinBlack -> (cabinBlack.getCreateTime() + finalEffectiveMinute) > now.getTime())
                .forEach(item -> {
                    builder.append(item.getCarrier())
                            .append(item.getFlightNumber())
                            .append(item.getCabin())
                            .append(item.getDepTime())
                            .append(item.getOfficeId());
                    carrierCabinBlackMap.put(builder.toString(), item);
                    builder.setLength(0);
                });

        builder.setLength(0);
        //删除方案
        sibeSearchResponse.getRoutings().removeIf(routing -> {
            List<SibeSegment> sibeSegmentList = new ArrayList<>();
            sibeSegmentList.addAll(routing.getFromSegments());
            sibeSegmentList.addAll(routing.getRetSegments());
            return sibeSegmentList
                    .stream()
                    .filter(sibeSegment -> {
                        String key = builder.append(sibeSegment.getCarrier())
                                .append(sibeSegment.getFlightNumber())
                                .append(sibeSegment.getCabin())
                                .append(sibeSegment.getDepTime())
                                .append(routing.getOfficeId())
                                .toString();
                        builder.setLength(0);
                        return carrierCabinBlackMap.containsKey(key);
                    })
                    .findFirst().isPresent();
        });
    }


    /**
     * 校验出发地和目的地是否合法，合法返回true，非法返回false
     * 并且将出发地和目的地设置到sibeSearchRequest
     * 如果出发地和目的地为机场码，则自动转换为城市码
     *
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
        } else {
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


    public AllAirports conversionCity(AllAirports allAirport) {
        AllAirports city = new AllAirports();
        city.setCcode(allAirport.getCcode());
        city.setGcode(allAirport.getGcode());
        city.setQcode(allAirport.getQcode());
        city.setZcode(allAirport.getZcode());
        return city;
    }


    /**
     * 获取GDS缓存，并且生成BusinessSearchRouteMap
     *
     * @param sibeSearchRequest
     * @return
     */
    @Override
    public SibeObtainGDSCache getGDSCache(SibeSearchRequest sibeSearchRequest) {

        getGDSCacheTimeSetting(sibeSearchRequest);

        List<SibeSearchResponse> sibeSearchResponseList = new ArrayList<>();

        Map<String, SibeRoute> businessSearchRouteMap = new HashMap<>();

        //0.根据原始航线路由与站点选择GDS数据源得到可以请求的航线路
        Map<String, SibeRoute> siteAllowSearchRouteMap = generateAllowSearchRouteMap(sibeSearchRequest);

        //key :GDS-PCC,value :GDS-PCC
        //不需要刷新的GDS map
        Map<String, String> noNeedToRefreshMap = new HashMap<>();

        //1.查找GDS缓存的内容
        Set<String> cacheKeySet = gdsCacheService.findAllKeys(sibeSearchRequest.getTripCacheKey());

        //2.根据GDS缓存内容与站点的航线选择GDS数据，进行交集，查找出具体的GDS缓存，并且放入缓存responseList中
        for (String redisCacheKey : cacheKeySet) {

            //删除GDS除路由外，的多余配置数据
            excessGDSpcc(sibeSearchRequest, redisCacheKey);
            //仅仅获取符合GDS 允许map中的缓存数据
            if (siteAllowSearchRouteMap.get(redisCacheKey) == null) {
                continue;
            }

            String gdsCode = redisCacheKey.split("-")[0];

            //选择的GDS不为空并且不存在的时跳过（GDS 配置）
            if (StringUtils.isNotBlank(sibeSearchRequest.getOtaSiteAirRouteChooseGDS()) &&
                    !sibeSearchRequest.getOtaSiteAirRouteChooseGDS().contains(gdsCode)) {
                continue;
            }


            SibeSearchResponse sibeSearchResponse = (SibeSearchResponse) gdsCacheService.findOne(sibeSearchRequest.getTripCacheKey(), redisCacheKey, 1);
            if (sibeSearchResponse != null) {
                Long gdsCreateTimeLapse = sibeSearchResponse.getTimeLapse();

                //找出超过刷新时间的，加入到BusinessSearchRouteMap业务路由中
                if (gdsCreateTimeLapse != null) {
                    long now = SystemClock.now();
                    long gdsCacheDiffTimeFromNow = (now - gdsCreateTimeLapse) / (1000 * 60);
                    if (gdsCacheDiffTimeFromNow >= sibeSearchRequest.getGdsCacheRefreshTimeMap().get(gdsCode)) {
                        if (sibeSearchRequest.getSearchRouteMap().get(redisCacheKey) != null) {
                            businessSearchRouteMap.put(redisCacheKey, sibeSearchRequest.getSearchRouteMap().get(redisCacheKey));
                        }
                    } else {
                        noNeedToRefreshMap.put(redisCacheKey, redisCacheKey);
                    }

                    //找出未达到有效时间的，加入到GDS缓存list中  判断没有意义，且不同GDS数据的缓存有效时间不一样
//                    if(gdsCacheDiffTimeFromNow < sibeSearchRequest.getGdsCacheValidTime()){
//                        sibeSearchResponseList.add(sibeSearchResponse.get());
//                    }
                    sibeSearchResponseList.add(sibeSearchResponse);
                }
            }
        }


        siteAllowSearchRouteMap.forEach((k, v) -> {
            if (businessSearchRouteMap.get(k) == null && noNeedToRefreshMap.get(k) == null) {
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
     * 删除GDS缓存 路由外多余的PCC 缓存
     *
     * @param sibeSearchRequest
     * @return
     */

    @Override
    @Async("repositoryTaskExecutor")
    public void excessGDSpcc(SibeSearchRequest sibeSearchRequest, String key) {
        if (null == sibeSearchRequest.getSearchRouteMap().get(key)) {
            gdsCacheService.deleteGDS(sibeSearchRequest.getTripCacheKey(), key);
        }
    }


    public static void getGDSCacheTimeSetting(SibeSearchRequest sibeSearchRequest) {
        //设置GDS默认刷新时间
        final int gdsCacheRefreshTime = 5; //默认刷新时间5分钟
        Map<String, Integer> gdsRefreshTimeMap = new HashMap<>();
        sibeSearchRequest.getSearchRouteMap().forEach((k, v) -> {
            gdsRefreshTimeMap.put(k.split("-")[0], gdsCacheRefreshTime);
        });

        sibeSearchRequest.setGdsCacheRefreshTimeMap(gdsRefreshTimeMap);

        //设置GDS默认有效时间
        sibeSearchRequest.setGdsCacheValidTime(10);//默认有效时间 10 分钟

        String gdsCacheRefreshTimeInfo = "";

        final String Rule_TYPE = "25";
        String parentKey = "GDS-25";
        String travelDateRange = "1";
        String exceptForTravelDateRange = "2";
        int gdsCacheValidTime = 10; //默认缓存时间10分钟

        if (sibeSearchRequest
                .getSiteRulesSwitch()
                .stream()
                .anyMatch(gdsRule -> (
                        parentKey.equals(gdsRule.getParameterKey()) && !"TRUE".equals(gdsRule.getParameterValue())))) {
            sibeSearchRequest.setGdsCacheValidTime(gdsCacheValidTime);
            return;
        }
        List<String> cityList = sibeSearchRequest.getCityPrioritycList();
        List<GdsRule> apiControlRuleGdsRedisList = new ArrayList<>();
        loop:
        for (String city : cityList) {
            String[] cityArray = city.split("/");
            sibeSearchRequest
                    .getGdsRuleSet()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(gdsRedis -> (
                            Rule_TYPE.equals(gdsRedis.getRuleType())
                                    && SibeUtil.contains(gdsRedis.getOrigin(), cityArray[0], "/")
                                    && SibeUtil.contains(gdsRedis.getDestination(), cityArray[1], "/")
//                                    && TravelDateRange(sibeSearchRequest,gdsRedis.getParameter3(),travelDateRange)
//                                    && TravelDateRange(sibeSearchRequest,gdsRedis.getParameter4(),exceptForTravelDateRange)
                    )).forEach(apiControlRuleGdsRedisList::add);
            if (apiControlRuleGdsRedisList.size() > 0) {
                break loop;
            }
        }
        if (apiControlRuleGdsRedisList.size() > 0 && apiControlRuleGdsRedisList != null) {

            if (apiControlRuleGdsRedisList.get(0).getParameter1() != null) {
                gdsCacheValidTime = Integer.parseInt(apiControlRuleGdsRedisList.get(0).getParameter1());
            }
            if (apiControlRuleGdsRedisList.get(0).getParameter2() != null) {
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


    @Override
    public void constructSibeSearchRequestByRoute(SibeSearchRequest sibeSearchRequest) {

        //1. 从Redis中拿到路由配置集合，并写入sibeSearchRequest对象
        // 所有路由
        List<RouteConfig> apiRouteConfigRedisSet = routeConfigRepositoryImpl.findAllRouteConfig();

        //gds规则
        List<GdsRule> apiControlRuleGdsRedisSet = gdsRuleRepositoryImpl.findAll();

        sibeSearchRequest.setRouteConfigRedisSet(apiRouteConfigRedisSet);
        sibeSearchRequest.setGdsRuleSet(apiControlRuleGdsRedisSet);
        //从redis里面获得GDS开关的配置，写入GdsSwitchRedisSet中去。

        List<SiteRulesSwitch> otaGDSSwtichRedis = siteRulesSwitchRepositoryImpl.findSiteRulesSwitchesByGroupKey("GDS_SWITCH");
        sibeSearchRequest.setSiteRulesSwitch(otaGDSSwtichRedis);

        //2. 获得当前请求的查询路由和生单路由，并写入sibeSearchRequest对象
        RouteRuleUtil.getRoute(sibeSearchRequest);
    }


    /**
     * 根据原始航线路由与站点选择GDS数据源得到可以请求的航线路由
     *
     * @param sibeSearchRequest
     * @return
     */
    private Map<String, SibeRoute> generateAllowSearchRouteMap(SibeSearchRequest sibeSearchRequest) {
        Map<String, SibeRoute> siteAllowSearchRouteMap = new HashMap<>();

        if (org.apache.commons.lang3.StringUtils.isNotBlank(sibeSearchRequest.getOtaSiteAirRouteChooseGDS())) {
            sibeSearchRequest.getSearchRouteMap().forEach((k, v) -> {
                String gdsCode = k.split("-")[0];
                if (sibeSearchRequest.getOtaSiteAirRouteChooseGDS().contains(gdsCode)) {
                    siteAllowSearchRouteMap.put(k, v);
                }
            });
        } else {
            sibeSearchRequest.getSearchRouteMap().forEach((k, v) -> siteAllowSearchRouteMap.put(k, v));
        }

        return siteAllowSearchRouteMap;
    }


    @Override
    public void constructOtherSiteSearchRequest(SibeSearchRequest sibeSearchRequest) {
        //ota规则
        List<OtaRule> apiControlRuleOtaRedisSet = otaRuleRepositoryImpl.findOtaRuleBySite(sibeSearchRequest.getSite());
        //ota 开关
        List<SiteRulesSwitch> otaSwitchValueRedisSet = siteRulesSwitchRepositoryImpl.findSiteRulesSwitchesByGroupKey("OTA_SITE_SWITCH_"+sibeSearchRequest.getSite());
        //ota规则集合
        sibeSearchRequest.setOtaRules(apiControlRuleOtaRedisSet);
        //OTA开关参数集合
        sibeSearchRequest.setSiteRulesSwitch(otaSwitchValueRedisSet);

        //获得限制止损值
        OtaRuleFilter.restrictedStopLoss(sibeSearchRequest);

        //缓存时间配置
        OtaRuleFilter.getCacheRefreshTime(sibeSearchRequest);
        //请求超时时间配置
        OtaRuleFilter.getTimeOutTime(sibeSearchRequest);

        //获取航线航司自动下线列表
//        Set<ApiOfflineAirLineRedis> apiOfflineAirLineRedisSet = apiOfflineAirLineListCaffeineRepository.findBySiteNotExpire(sibeSearchRequest.getSite());
//        sibeSearchRequest.setApiOfflineAirLineRedisSet(apiOfflineAirLineRedisSet);

    }

}
