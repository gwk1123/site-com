package comm.utils.async.impl;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import comm.config.SibeProperties;
import comm.ota.ctrip.model.CtripSearchResponse;
import comm.ota.site.SibeBaseResponse;
import comm.ota.site.SibeRouting;
import comm.ota.site.SibeSearchRequest;
import comm.ota.site.SibeSearchResponse;
import comm.service.transform.TransformSearchGds;
import comm.sibe.OtaSearchResponse;
import comm.sibe.SibeSearchCommService;
import comm.utils.async.AsynchronousRefreshService;
import comm.utils.redis.GdsCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class AsynchronousRefreshServiceImpl implements AsynchronousRefreshService {

    private Logger logger =LoggerFactory.getLogger(AsynchronousRefreshServiceImpl.class);

    @Autowired
    private SibeProperties sibeProperties;
    @Autowired
    private GdsCacheService gdsCacheService;
    @Autowired
    private SibeSearchCommService sibeSearchCommService;
    @Autowired
    private TransformSearchGds transformSearchGds;
    @Autowired
    private OtaSearchResponse otaSearchResponse;


    @Override
    @Async("asyncPool")
    public void updateSearchOtaCache(List<SibeSearchResponse> sibeSearchResponseList, SibeSearchRequest sibeSearchRequest, int methodType, int upCurrentSite) {
        //是否刷新其它站点数据至redis

        if(sibeProperties.getRedis().isRefreshOtherSiteSwitch()) {
            logger.debug("uuid:" + sibeSearchRequest.getUuid() + " 0 是否刷新其它站点数据至redis:"+sibeProperties.getRedis().isRefreshOtherSiteSwitch());
            logger.debug("uuid:" + sibeSearchRequest.getUuid() + " 0 刷新站点:"+sibeProperties.getRedis().getRefreshOtaSites());
            List<SibeSearchResponse> sibeSearchResponseListNoNull= sibeSearchResponseList.stream().filter(Objects::nonNull).collect(Collectors.toList());
            //刷新其它站点相关列表配置
            Set<String> otaSiteLists = org.springframework.util.StringUtils.commaDelimitedListToSet(sibeProperties.getRedis().getRefreshOtaSites());

            otaSiteLists.forEach(otaSite -> {
                String otaSiteArray[] = StringUtils.split(otaSite, "-");
                logger.info("uuid:" + sibeSearchRequest.getUuid() + " 1 刷新redis: 平台：" + otaSiteArray[0] + "站点" + otaSiteArray[1]);
                saveOrUpdateOther(otaSiteArray[0], otaSiteArray[1], sibeSearchResponseListNoNull, sibeSearchRequest, methodType,true);
            });
        }
    }



    /**
     * 保存其他站点缓存
     * @param otaCode  otaCode
     * @param siteCode siteCode
     * @param sibeSearchResponseList
     * @param tmpRequest  搜索请求参数
     * @param methodType 方法类型
     * @param isNewUUid 是否需要生成新的uuid,  如果isNewUUid = false,因为uuid一样生成的data会覆盖redis已有的data.
     * @param <T> response
     */
    private <T extends SibeBaseResponse> void saveOrUpdateOther(String otaCode, String siteCode,
                                                                List<SibeSearchResponse> sibeSearchResponseList,
                                                                SibeSearchRequest tmpRequest, int methodType, boolean isNewUUid) {
        //todo 性能优化点
        SibeSearchRequest sibeSearchRequestNew = SibeSearchRequest.deepCopy(tmpRequest);
        if (isNewUUid) {
            String uuid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmmss")) + UUID.randomUUID().toString().split("-")[4];
            sibeSearchRequestNew.setUuid(uuid);
        }
        sibeSearchRequestNew.setOta(otaCode);
        sibeSearchRequestNew.setSite(siteCode);
        sibeSearchCommService.constructOtherSiteSearchRequest(sibeSearchRequestNew);
        sibeSearchCommService.constructSibeSearchRequestByRoute(sibeSearchRequestNew);

        SibeSearchResponse sibeSearchResponse = new SibeSearchResponse();
        sibeSearchResponse.setStatus(0);
        sibeSearchResponse.setTimeLapse(SystemClock.now());
        sibeSearchResponse.setMsg("success");

        List<SibeRouting> routingList= transformSearchGds.filterProcessRouting(sibeSearchResponseList, sibeSearchRequestNew);


        if (routingList == null || routingList.size() == 0) {
            logger.error("uuid:" + sibeSearchRequestNew.getUuid() + " 异步刷新缓存，无数据");
            return ;
        }
        sibeSearchResponse.setRoutings(routingList);

        logger.info("uuid:" + sibeSearchRequestNew.getUuid() + "2 switch 站点:"+otaCode+"-"+sibeSearchRequestNew.getSite());
        //data存储在redis中
        sibeSearchRequestNew.setTripCacheOTASiteKey(this.otaCacheKetMethod(otaCode,siteCode,sibeSearchRequestNew.getTripCacheKey()));
        logger.debug("uuid:"+sibeSearchRequestNew.getUuid() +" 11.1 进进saveOrUpdateString:"+ (System.currentTimeMillis()-sibeSearchRequestNew.getStartTime())/(1000) +"秒");
        gdsCacheService.saveDataToRedis(sibeSearchResponse,sibeSearchRequestNew);
        logger.debug("uuid:"+sibeSearchRequestNew.getUuid() +" 11.2 进进saveOrUpdateString:"+ (System.currentTimeMillis()-sibeSearchRequestNew.getStartTime())/(1000) +"秒");
        switch(otaCode)
        {
            case "CTRIP":

                logger.info("uuid:" + sibeSearchRequestNew.getUuid() + " 3 CTRIP 站点:" + otaCode + "-" + sibeSearchRequestNew.getSite());
                CtripSearchResponse ctripSearchResponse = (CtripSearchResponse) otaSearchResponse.transformSearchResponse(sibeSearchResponse,sibeSearchRequestNew);
                gdsCacheService.saveOrUpdateString(ctripSearchResponse,sibeSearchRequestNew.getTripCacheOTASiteKey(),
                        sibeSearchRequestNew.getOtaCacheValidTime() * 60);
                break;

//            case "FLIGGY":
//                LOGGER.info("uuid:" + sibeSearchRequestNew.getUuid() + " 3 FLIGGY 站点:" + otaCode + "-" + sibeSearchRequestNew.getSite());
//                BiFunction<SibeSearchResponse, SibeSearchRequest, FliggySearchResponse> transformToFliggy = new TransformFliggySearchResponse(sibeProperties).getToOta();
//                FliggySearchResponse fliggySearchResponse = transformToFliggy.apply(sibeSearchResponse, sibeSearchRequestNew);
//                airlineSolutionsRedisRepository.saveOrUpdateString(fliggySearchResponse,
//                        sibeSearchRequestNew.getTripCacheOTASiteKey(),
//                        sibeSearchRequestNew.getOtaCacheValidTime() * 60);
//                //飞猪推送接口
//                pushSearchToFiggy(sibeSearchRequestNew, fliggySearchResponse, methodType);
//                break;
//
//            case "QUNAR":
//                LOGGER.info("uuid:" + sibeSearchRequestNew.getUuid() + "3 QUNAR 站点:" + otaCode + "-" + sibeSearchRequestNew.getSite());
//                BiFunction<SibeSearchResponse, SibeSearchRequest, QunarSearchResponse> transformToQunar = new TransformQunarSearchResponse().getToOta();
//                airlineSolutionsRedisRepository.saveOrUpdateString(transformToQunar.apply(sibeSearchResponse, sibeSearchRequestNew),
//                        sibeSearchRequestNew.getTripCacheOTASiteKey(),
//                        sibeSearchRequestNew.getOtaCacheValidTime() * 60);
//                break;
//
//            case "LY":
//                BiFunction<SibeSearchResponse, SibeSearchRequest, LySearchResponse> transformToLy = new TransformLySearchResponse(sibeProperties).getToOta();
//                airlineSolutionsRedisRepository.saveOrUpdateString(transformToLy.apply(sibeSearchResponse, sibeSearchRequestNew),
//                        sibeSearchRequestNew.getTripCacheOTASiteKey(),
//                        sibeSearchRequestNew.getOtaCacheValidTime() * 60);
//                break;
//
//            case "MEITUAN":
//                BiFunction<SibeSearchResponse, SibeSearchRequest, MeituanSearchResponse> transformToMeitua = new TransformMeituanSearchResponse().getToOta();
//                airlineSolutionsRedisRepository.saveOrUpdateString(transformToMeitua.apply(sibeSearchResponse, sibeSearchRequestNew),
//                        sibeSearchRequestNew.getTripCacheOTASiteKey(),
//                        sibeSearchRequestNew.getOtaCacheValidTime() * 60);
//                break;
//
//            case "TUNIU":
//                BiFunction<SibeSearchResponse, SibeSearchRequest, TuniuSearchResponse> transformToTuniu = new TransformTuniuSearchResponse().getToOta();
//                airlineSolutionsRedisRepository.saveOrUpdateString(transformToTuniu.apply(sibeSearchResponse, sibeSearchRequestNew),
//                        sibeSearchRequestNew.getTripCacheOTASiteKey(),
//                        sibeSearchRequestNew.getOtaCacheValidTime() * 60);
//                break;
//            case "LVMAMA":
//                BiFunction<SibeSearchResponse, SibeSearchRequest, LvmamaSearchResponse> transformToLvmama = new TransformLvmamaSearchResponse().getToOta();
//                airlineSolutionsRedisRepository.saveOrUpdateString(transformToLvmama.apply(sibeSearchResponse, sibeSearchRequestNew),
//                        sibeSearchRequestNew.getTripCacheOTASiteKey(),
//                        sibeSearchRequestNew.getOtaCacheValidTime() * 60);
//                break;
//
//            case "JD":
//                BiFunction<SibeSearchResponse, SibeSearchRequest, JDSearchResponse> transformToJd = new TransformJdSearchResponse().getToOta();
//                JDSearchResponse jdSearchResponse = transformToJd.apply(sibeSearchResponse, sibeSearchRequestNew);
//                if (null != jdSearchResponse.getData() && null != jdSearchResponse.getData().getResults() && jdSearchResponse.getData().getResults().size() > 0) {
//                    //保存GDS
//                    airlineSolutionsRedisRepository.saveOrUpdateString(jdSearchResponse,
//                            sibeSearchRequestNew.getTripCacheOTASiteKey(),
//                            sibeSearchRequestNew.getOtaCacheValidTime() * 60);
//                }
//                break;
//            case "OWT":
//                BiFunction<SibeSearchResponse, SibeSearchRequest, OWTSearchResponse> transformToOwt = new TransformOWTSearchResponse().getToOta();
//                airlineSolutionsRedisRepository.saveOrUpdateString(transformToOwt.apply(sibeSearchResponse, sibeSearchRequestNew),
//                        sibeSearchRequestNew.getTripCacheOTASiteKey(),
//                        sibeSearchRequestNew.getOtaCacheValidTime() * 60);
//                break;
//            case "MAFENGWO":
//                BiFunction<SibeSearchResponse, SibeSearchRequest, MafengwoSearchResponse> transformToMafengwo = new TransformMafengwoSearchResponse().getToOta();
//                airlineSolutionsRedisRepository.saveOrUpdateString(transformToMafengwo.apply(sibeSearchResponse, sibeSearchRequestNew),
//                        sibeSearchRequestNew.getTripCacheOTASiteKey(),
//                        sibeSearchRequestNew.getOtaCacheValidTime() * 60);
//                break;
            default:
                break;
        }

    }

    public String otaCacheKetMethod(String otaCode, String siteCode, String tripCacheKey) {
        return tripCacheKey + "_" + otaCode + "-" + siteCode;
    }


}
