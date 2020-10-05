package com.sibecommon.service.ota;

import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.repository.entity.SiteRulesSwitch;
import com.sibecommon.service.handler.AbstractRuleHandler;
import com.sibecommon.service.transform.SibeUtil;
import com.sibecommon.utils.constant.SibeConstants;
import com.sibecommon.utils.exception.CustomSibeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OtaRuleBlackListHandle extends AbstractRuleHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String RULE_TYPE = "36"; //OTA-航线黑名单


    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {

        String tripType=sibeSearchRequest.getTripType();
        if("MT".equals(tripType)){
            return ;
        }
        String uuid=sibeSearchRequest.getUuid();
        List<SiteRulesSwitch> otaSwitchValueRedisSet = sibeSearchRequest.getOtaSiteRulesSwitchs();
        String paramentKey = "OTA-36";
        String meg = "【OTA-航线黑名单】";
        if(otaSwitchValueRedisSet
            .stream()
            .filter(Objects::nonNull)
            .anyMatch(value -> (
                paramentKey.equals(value.getParameterKey()) && !"TRUE".equals(value.getParameterValue())))){
                return ;
        }
        List<String> cityList = SibeUtil.getCityPriority(sibeSearchRequest.getFromCityRedis(),sibeSearchRequest.getToCityRedis());
        if("OW".equals(tripType) || "RT".equals(tripType)){
            for(String city:cityList){
                String[] cityArray=city.split("/");
                if(sibeSearchRequest
                    .getOtaRules()
                    .stream()
                    .filter(Objects::nonNull)
                    .anyMatch(otaRule ->(
                        RULE_TYPE.equals(otaRule.getRuleType())
                        && SibeUtil.contains(otaRule.getParameter1(),tripType,"/")
                        && SibeUtil.contains(otaRule.getOrigin(),cityArray[0],"/")
                        && SibeUtil.contains(otaRule.getDestination(),cityArray[1],"/")
                    ))){
                    String megStr="航线"+sibeSearchRequest.getFromCity()+"-"+sibeSearchRequest.getToCity()+"已存在"+meg;
                    throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_102, megStr, uuid,"search");
                }
            }
          return;
        }
    }
}
