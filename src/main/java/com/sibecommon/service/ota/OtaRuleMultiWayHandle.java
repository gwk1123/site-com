package com.sibecommon.service.ota;

import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.service.handler.AbstractRuleHandler;
import com.sibecommon.service.transform.SibeUtil;
import com.sibecommon.utils.constant.SibeConstants;
import com.sibecommon.utils.exception.CustomSibeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Created by yangdehua on 18/2/10.
 */
public class OtaRuleMultiWayHandle extends AbstractRuleHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public static final String RULE_TYPE="17"; //OTA-限制多程<包括缺口程>
    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {
        String uuid = sibeSearchRequest.getUuid();
        String tripType= sibeSearchRequest.getTripType();
        String parameterKey="OTA-17"; //允许多程开关
        String msg="【OTA-限制多程,包括缺口程】";

        //航程类型如果不是多程，则返回
        if (!"MT".equals(tripType)) {
            // LOGGER.debug("uuid:"+uuid+" OTA站点规则，站点"+sibeSearchRequest.getSite()+msg+"航程类型:"+tripType+"略过校验");
            return;
        }

        //判断开关是否有开启，如果没有开启则忽略此规则
        if( sibeSearchRequest
            .getOtaSiteRulesSwitchs()
            .stream()
            .anyMatch(value->(parameterKey.equals(value.getParameterKey())&&!"TRUE".equals(value.getParameterValue()))))
        {
            LOGGER.warn("uuid:"+uuid+" OTA站点规则，站点"+sibeSearchRequest.getSite()+msg+"开关没有开启");
            return;
        }


        //允许多程
        List<String> cityList= SibeUtil.getCityPriority(sibeSearchRequest.getFromCityRedis(),sibeSearchRequest.getToCityRedis());

        for(String city:cityList){
            String[] cityArray= StringUtils.split(city,"/");
            if(sibeSearchRequest
                .getOtaRules()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(otaRule -> (
                    RULE_TYPE.equals(otaRule.getRuleType())
                    && SibeUtil.contains(otaRule.getOrigin(),cityArray[0],"/")
                    && SibeUtil.contains(otaRule.getDestination(),cityArray[1],"/")
                    && "1".equals(otaRule.getParameter1()) //允许多程
                ))){
                //匹配成功则返回
                // LOGGER.debug("uuid:"+uuid+" OTA站点规则，航线："+sibeSearchRequest.getFromCity()+"-"+sibeSearchRequest.getToCity()+""+msg+"匹配成功");
                return;
            }
        }
        //不允许多程，抛出异常
        // LOGGER.debug("uuid:"+uuid+" OTA站点规则，航线："+sibeSearchRequest.getFromCity()+"-"+sibeSearchRequest.getToCity()+msg+"匹配失败");
        throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_104, msg+SibeConstants.RESPONSE_MSG_999, uuid,"search");
    }
}

