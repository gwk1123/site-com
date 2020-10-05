package com.sibecommon.service.ota;

import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.repository.entity.SiteRulesSwitch;
import com.sibecommon.service.handler.AbstractRuleHandler;
import com.sibecommon.service.transform.SibeUtil;
import com.sibecommon.utils.constant.SibeConstants;
import com.sibecommon.utils.exception.CustomSibeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by yangdehua on 18/2/10.
 */
public class OtaRuleWhiteListHandle extends AbstractRuleHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    public static final String RULE_TYPE="8"; //OTA-航线白名单

    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {

        String tripType= sibeSearchRequest.getTripType();
        //todo 多程,不进行过滤,后续再完善
        if ("MT".equals(tripType)){
            return ;
        }

        String uuid = sibeSearchRequest.getUuid();
        List<SiteRulesSwitch> otaSwitchValueRedisSet = sibeSearchRequest.getOtaSiteRulesSwitchs();
        String parameterKey="OTA-8"; //﻿﻿限制OTA-航线白名单开关
        String msg="【OTA-航线白名单】";
        //判断开关是否有开启，如果没有开启则忽略此规则
        if( otaSwitchValueRedisSet
            .stream()
            .filter(Objects::nonNull)
            .anyMatch(value->(parameterKey.equals(value.getParameterKey())&&!"TRUE".equals(value.getParameterValue()))))
        {
            // LOGGER.debug("uuid:"+uuid+" OTA站点规则，站点"+sibeSearchRequest.getSite()+msg+"开关没有开启");
            return;
        }


        //过滤白名单
        List<String> cityList= SibeUtil.getCityPriority(sibeSearchRequest.getFromCityRedis(),sibeSearchRequest.getToCityRedis());

        //单程与往返
        if ("OW".equals(tripType)||"RT".equals(tripType)) {
            for (String city : cityList) {
                String[] cityArray = StringUtils.split(city,"/");
                if (sibeSearchRequest
                    .getOtaRules()
                    .stream()
                    .filter(Objects::nonNull)
                    .anyMatch(otaRule -> (
                        RULE_TYPE.equals(otaRule.getRuleType())
                        && SibeUtil.contains(otaRule.getParameter1(),tripType,"/")
                        && SibeUtil.contains(otaRule.getOrigin(),cityArray[0],"/")
                        && SibeUtil.contains(otaRule.getDestination(),cityArray[1],"/")))) {
                    // LOGGER.debug("uuid:" + uuid + " OTA站点规则，航线：" + sibeSearchRequest.getFromCity() + "-" + sibeSearchRequest.getToCity() + "" + msg + "匹配成功");
                    return;
                }
            }

        //不在航线白名单中，抛出异常
        String msgStr= "航线："+sibeSearchRequest.getFromCity()+"-"+sibeSearchRequest.getToCity()+msg+"匹配失败";
        // LOGGER.debug("uuid:"+uuid+msgStr);
        throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_102, msgStr, uuid,"search");
        }

    }
}

