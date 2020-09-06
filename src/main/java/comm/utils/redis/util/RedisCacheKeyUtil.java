package comm.utils.redis.util;

import com.google.common.base.Splitter;
import comm.ota.site.SibeSearchRequest;
import comm.repository.entity.OtaRule;
import comm.repository.entity.PolicyGlobal;
import comm.repository.entity.PolicyInfo;
import comm.utils.constant.DirectConstants;
import org.apache.commons.lang3.StringUtils;

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

}
