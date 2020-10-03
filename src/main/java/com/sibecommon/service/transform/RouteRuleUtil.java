package com.sibecommon.service.transform;

import com.google.common.base.Splitter;
import com.sibecommon.ota.site.SibeRoute;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.repository.entity.GdsPcc;
import com.sibecommon.repository.entity.OtaRule;
import com.sibecommon.repository.entity.RouteConfig;
import com.sibecommon.utils.constant.Constants;
import com.sibecommon.utils.constant.SibeConstants;
import com.sibecommon.utils.exception.CustomSibeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The type Route rule utility.
 */
public class RouteRuleUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(RouteRuleUtil.class);

    /**
     * Gets route.
     *
     * @param sibeSearchRequest the ota search request
     */
    public static void getRoute(SibeSearchRequest sibeSearchRequest) {

        String uuid = sibeSearchRequest.getUuid();
        String fromCity = getFromCity(sibeSearchRequest);
        String toCity = getToCity(sibeSearchRequest);
        if (StringUtils.isEmpty(toCity) || StringUtils.isEmpty(fromCity)) {
            LOGGER.error("uuid:" + uuid + " 匹配路由-起飞城市:" + fromCity + ",抵达城市：" + toCity);
            throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_1, fromCity + "-" + toCity + " " + SibeConstants.RESPONSE_MSG_1, uuid, "UnKnow");
        }
        List<RouteConfig> routeConfigRedisSet = sibeSearchRequest.getRouteConfigRedisSet();
        //1.请求PCC列表
        Set<GdsPcc> sibeGdsPccRedisSet = sibeSearchRequest.getGdsPccRedisSet();
        Map<String, SibeRoute> searchRouteMap = new HashMap<>();
        //路由查询
        List<String> cityList = sibeSearchRequest.getCityPrioritycList();
        for (int y = 0; y < cityList.size(); y++) {
            String[] cityArray = StringUtils.split(cityList.get(y), "/");

            Optional<RouteConfig> apiRouteConfigRedis =
                    routeConfigRedisSet
                            .stream()
                            .filter(Objects::nonNull)
                            .filter(routeConfigRedis -> (SibeUtil.contains(routeConfigRedis.getOrigin(), cityArray[0], "/")
                                    && SibeUtil.contains(routeConfigRedis.getDestination(), cityArray[1], "/")
                            ))
                            .findFirst();
            //路由匹配成功
            if (apiRouteConfigRedis.isPresent()) {
                Map<String, GdsPcc> orderRouteMap = new HashMap();

                Splitter
                        .on("/")
                        .omitEmptyStrings()
                        .trimResults()
                        .split(apiRouteConfigRedis.get().getOrderOfficeNo())
                        .forEach(
                                route -> {
                                    String[] routeArray = StringUtils.split(route, "-");
                                    Optional<GdsPcc> sibeGdsPccRedis =
                                            sibeGdsPccRedisSet
                                                    .stream()
                                                    .filter(Objects::nonNull)
                                                    .filter(pcc -> (pcc.getPccCode().equals(routeArray[1])))
                                                    .findFirst();

                                    if (sibeGdsPccRedis.isPresent()) {
                                        orderRouteMap.put(routeArray[0], sibeGdsPccRedis.get());
                                    } else {
                                        LOGGER.error("uuid:" + uuid + " 生单路由PCC:" + routeArray[1] + "不存在PCC配置表中");
                                    }
                                }
                        );

                Splitter
                        .on("/")
                        .omitEmptyStrings()
                        .trimResults()
                        .split(apiRouteConfigRedis.get().getSearchOfficeNo())
                        .forEach(
                                route -> {
                                    String[] routeArray = StringUtils.split(route, "-");
                                    Optional<GdsPcc> sibeGdsPccRedis =
                                            sibeGdsPccRedisSet
                                                    .stream()
                                                    .filter(pcc -> (pcc.getPccCode().equals(routeArray[1])))
                                                    .findFirst();

                                    if (sibeGdsPccRedis.isPresent()) {
                                        SibeRoute sibeRoute = new SibeRoute();
                                        sibeRoute.setOrderPcc(orderRouteMap.get(routeArray[0]));
                                        sibeRoute.setSearcPcc(sibeGdsPccRedis.get());
                                        searchRouteMap.put(routeArray[0] + "-" + routeArray[1], sibeRoute);
                                    } else {
                                        LOGGER.error("uuid:" + uuid + "查询路由PCC:" + routeArray[1] + "不存在PCC配置表中");
                                    }
                                }
                        );

                break;
            }
        }

        if (searchRouteMap == null || searchRouteMap.entrySet().size() == 0) {
            throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_100, "没有找到路由配置PCC " + SibeConstants.RESPONSE_MSG_100, uuid, "search");
        }
        sibeSearchRequest.setSearchRouteMap(searchRouteMap);

        final String RuleType = "35";
        String parameterKey = "OTA-35";
        String msg = "【OTA航线GDS选择】";

        List<OtaRule> airrouteConfigRedisSet = sibeSearchRequest.getOtaRules();

        if (sibeSearchRequest
                .getSiteRulesSwitch() != null && sibeSearchRequest
                .getSiteRulesSwitch()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(value -> (parameterKey.equals(value.getParameterKey()) && !"TRUE".equals(value.getParameterValue())))) {
            LOGGER.warn("uuid:" + uuid + " OTA站点规则，站点" + sibeSearchRequest.getSite() + msg + "开关没有开启");
            return;
        }


        for (String city : cityList) {
            String[] cityArray1 = StringUtils.split(city, "/");
            if (airrouteConfigRedisSet != null) {
                Optional<OtaRule> apiControlRuleOtaRedis = airrouteConfigRedisSet
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(airrouteConfigRedis -> (
                                RuleType.equals(airrouteConfigRedis.getRuleType()) &&
                                        SibeUtil.contains(airrouteConfigRedis.getOrigin(), cityArray1[0], "/") &&
                                        SibeUtil.contains(airrouteConfigRedis.getDestination(), cityArray1[1], "/")
                        ))
                        .findFirst();
                if (apiControlRuleOtaRedis.isPresent()) {
                    String parameter1 = apiControlRuleOtaRedis.get().getParameter1();
                    sibeSearchRequest.setOtaSiteAirRouteChooseGDS(parameter1);
                    return;
                }
            }

        }
    }

    public static boolean useList(String[] arr, List<String> targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    public static boolean useLoop(String[] arr, List<String> listString) {
        for (String s : arr) {
            if (s.equals(listString)) {
                return true;
            }
        }
        return false;
    }


    //获取出发地
    public static String getFromCity(SibeSearchRequest sibeSearchRequest) {
        //兼容多程，多程取第一程第一个段出发地 ，第一程第一个段 目的地
        if (Constants.MULTIPASS.equals(sibeSearchRequest.getTripType())) {
            String[] citys = StringUtils.splitByWholeSeparatorPreserveAllTokens(sibeSearchRequest.getFromCity(), ",");
            String[] fromCitys = StringUtils.splitByWholeSeparatorPreserveAllTokens(citys[0], "/");
            return fromCitys[0];
        } else {
            return sibeSearchRequest.getFromCity();
        }
    }

    //获取出发时间
    public static String getFromDate(SibeSearchRequest sibeSearchRequest) {
        //兼容多程，多程取第一程第一个段出发地 ，第一程第一个段 目的地
        if (Constants.MULTIPASS.equals(sibeSearchRequest.getTripType())) {
            String[] fromDate = StringUtils.splitByWholeSeparatorPreserveAllTokens(sibeSearchRequest.getFromDate(), ",");
            return fromDate[0];
        } else {
            return sibeSearchRequest.getFromDate();
        }
    }


    //获取目的地
    public static String getToCity(SibeSearchRequest sibeSearchRequest) {
        //兼容多程，多程取第一程第一个段出发地 ，第一程第一个段 目的地
        if (Constants.MULTIPASS.equals(sibeSearchRequest.getTripType())) {
            String[] citys = StringUtils.splitByWholeSeparatorPreserveAllTokens(sibeSearchRequest.getFromCity(), ",");
            String[] toCitys = StringUtils.splitByWholeSeparatorPreserveAllTokens(citys[0], "/");
            return toCitys[1];
        } else {
            return sibeSearchRequest.getToCity();
        }
    }

}
