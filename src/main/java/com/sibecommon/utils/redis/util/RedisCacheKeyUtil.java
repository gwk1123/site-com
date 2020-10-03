package com.sibecommon.utils.redis.util;

import com.google.common.base.Splitter;
import com.sibecommon.repository.entity.*;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.repository.entity.*;
import com.sibecommon.utils.constant.DirectConstants;
import org.apache.commons.lang3.StringUtils;
import com.sibecommon.repository.entity.*;

public class RedisCacheKeyUtil {

    public static String getPolicyGlobalRedisCacheKey(PolicyGlobal policyGlobal) {
        StringBuilder sb = new StringBuilder();
        sb.append(policyGlobal.getId());
        sb.append("_");
        sb.append(policyGlobal.getOtaSiteCode());
        sb.append("_");
        sb.append(getPolicyGlobalSingle(policyGlobal.getAirline(),2));
        return sb.toString();
    }


    public static String getPolicyGlobalSingle(String str,int length){
        if (StringUtils.isNotEmpty(str)
                && StringUtils.trim(str).length()==length
                && StringUtils.containsNone(str,"/")){
            return StringUtils.trim(str);
        }else {
            return DirectConstants.POLICY_AIRLINE_ALL;
        }
    }


    public static String getPolicyInfoRedisCacheKey(PolicyInfo apiPolicyInfoRedis) {
        StringBuilder sb = new StringBuilder();
        sb.append(apiPolicyInfoRedis.getId());
        sb.append("_");
        sb.append(apiPolicyInfoRedis.getAirline());
        sb.append("_");
        sb.append(apiPolicyInfoRedis.getTripType());
        return sb.toString();
    }

    public static String getOtaRuleCacheKey(final OtaRule otaRule) {
        //1.ota规则key:站点_规则类型（特例：航线黑白名单：站点_规则类型_起飞地_抵达地）
        StringBuilder sb = new StringBuilder();
        sb.append(otaRule.getId());
        sb.append("_");
        sb.append(otaRule.getOtaSiteCode());
        sb.append("_");
        sb.append(otaRule.getRuleType());
        return sb.toString();
    }

    public static String getSiteRulesSwitchCacheKey(SiteRulesSwitch siteRulesSwitch){
        StringBuilder sb=new StringBuilder();
        sb.append(siteRulesSwitch.getGroupKey());
        sb.append("_");
        sb.append(siteRulesSwitch.getParameterKey());
        return sb.toString();
    }

    public static String getGdsPccCacheKey(GdsPcc gdsPcc){
        StringBuilder sb=new StringBuilder();
        sb.append(gdsPcc.getGdsCode());
        sb.append("_");
        sb.append(gdsPcc.getPccCode());
        return sb.toString();
    }

    public static String getGdsCacheKey(Gds gds){
        StringBuilder sb=new StringBuilder();
        sb.append(gds.getGdsCode());
        return sb.toString();
    }

    public static String getOtaCacheKey(Ota ota){
        StringBuilder sb=new StringBuilder();
        sb.append(ota.getOtaCode());
        return sb.toString();
    }

    public static String getOtaSitecCacheKey(OtaSite otaSite){
        StringBuilder sb=new StringBuilder();
        sb.append(otaSite.getOtaCode());
        sb.append("_");
        sb.append(otaSite.getOtaSiteCode());
        return sb.toString();
    }

    public static String getRouteConfigCacheKey(RouteConfig routeConfig){
        StringBuilder sb=new StringBuilder();
        sb.append(routeConfig.getId());
        return sb.toString();
    }

    public static String getAirlineCacheKey(SibeSearchRequest sibeSearchRequest){
        String tripType=sibeSearchRequest.getTripType();
        StringBuilder sb = new StringBuilder();
        //单程
        if("1".equals(tripType)){
            sb.append(sibeSearchRequest.getFromCity());
            sb.append(sibeSearchRequest.getToCity());
            sb.append(sibeSearchRequest.getFromDate());
        }else{
            //往返
            if("2".equals(tripType)){
                sb.append(sibeSearchRequest.getFromCity());
                sb.append(sibeSearchRequest.getToCity());
                sb.append(sibeSearchRequest.getFromDate());
                sb.append(sibeSearchRequest.getRetDate());
            }else {
                //MT:多程
                Splitter
                        .on(",")
                        .omitEmptyStrings()
                        .trimResults()
                        .splitToList(sibeSearchRequest.getFromCity())
                        .forEach(
                                trip->Splitter
                                        .on("/")
                                        .omitEmptyStrings()
                                        .trimResults()
                                        .split(trip)
                                        .forEach(city->sb.append(city))
                        );

                Splitter
                        .on(",")
                        .omitEmptyStrings()
                        .trimResults()
                        .splitToList(sibeSearchRequest.getFromDate())
                        .forEach(
                                tripDate->sb.append(tripDate)
                        );
            }
        }
        return sb.toString();
    }


    public static String  getExchangeRateCacheKey(ExchangeRate exchangeRate){
        StringBuilder sb=new StringBuilder();
        sb.append(exchangeRate.getCurrency());
        sb.append(exchangeRate.getExchangeCurrency());
        return sb.toString();
    }

    public static String  getCarrierCabinBlackCacheKey(CarrierCabinBlack carrierCabinBlack){
        StringBuilder builder = new StringBuilder();
        builder.append(carrierCabinBlack.getCarrier());
        builder.append("_");
        builder.append(carrierCabinBlack.getFlightNumber());
        builder.append("_");
        builder.append(carrierCabinBlack.getDepTime());
        builder.append("_");
        builder.append(carrierCabinBlack.getCabin());
        builder.append("_");
        builder.append(carrierCabinBlack.getOfficeId());
        return builder.toString();
    }

    public static String getGdsRuleCacheKey(GdsRule gdsRule){
        StringBuilder sb = new StringBuilder();
        sb.append(gdsRule.getId());
        sb.append("_");
        sb.append(gdsRule.getRuleType());
        return sb.toString();
    }

    public static String getAllAirportsCacheKey(AllAirports allAirports){
        StringBuilder sb = new StringBuilder();
        sb.append(allAirports.getId());
        sb.append("_");
        sb.append(allAirports.getGcode());
        sb.append("_");
        sb.append(allAirports.getCcode());
        sb.append("_");
        sb.append(allAirports.getCode());
        return sb.toString();
    }

}
