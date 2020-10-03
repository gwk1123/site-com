package com.sibecommon.service.handler;

import com.sibecommon.repository.entity.GdsRule;
import com.sibecommon.utils.constant.PolicyConstans;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.service.transform.SibeUtil;
import com.sibecommon.utils.exception.CustomSibeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by yangdehua on 18/2/10.
 */

@Component
public class GdsRuleNumberOfUnitsHandle extends AbstractRuleHandler {

    private final Logger LOGGER=LoggerFactory.getLogger(this.getClass());



    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {

        String uuid=sibeSearchRequest.getUuid();
        String gds=sibeSearchRequest.getGds();
        String officeId=sibeSearchRequest.getOfficeId();
        String tripType=sibeSearchRequest.getTripType();
        String parameterKey="GDS-11"; //﻿﻿限制方案数量
        String msg="【GDS-限制方案数量】";
        final String RULE_TYPE="11"; //﻿限制方案数量
        //限制方案数量
        int numberOfUnits=200;

        // LOGGER.debug("uuid:"+uuid+" "+sibeSearchRequest.getGds()+msg+"开始匹配");
        //判断开关是否有开启，如果没有开启则忽略此规则
        if( sibeSearchRequest
            .getSiteRulesSwitch()
            .stream()
            .anyMatch(value->(parameterKey.equals(value.getParameterKey())&&!"TRUE".equals(value.getParameterValue())))){
            // LOGGER.debug("uuid:"+uuid+" "+sibeSearchRequest.getGds()+msg +"开关没有开启,默认方案数量200");
            sibeSearchRequest.setNumberOfUnits(numberOfUnits);
            return;
        }

        List<String> cityList=sibeSearchRequest.getCityPrioritycList();
        for(String city:cityList){
            String[] cityArray= StringUtils.split(city,"/");
            Set<GdsRule> apiControlRuleGdsRedisSet = sibeSearchRequest
                .getGdsRuleSet()
                .stream()
                .filter(Objects::nonNull)
                .filter(ruleGds -> (
                    RULE_TYPE.equals(ruleGds.getRuleType())
                    && gds.equals(ruleGds.getGdsCode())
                    && SibeUtil.contains(ruleGds.getPccCode(),officeId,"/")
                    && SibeUtil.contains(ruleGds.getOrigin(),cityArray[0],"/")
                    && SibeUtil.contains(ruleGds.getDestination(),cityArray[1],"/")
                    && (PolicyConstans.POLICY_TRIP_TYPE_ALL.equals(ruleGds.getParameter2()) || tripType.equals(ruleGds.getParameter2()))))
                .collect(Collectors.toSet());

            Optional<GdsRule> apiControlRuleGdsRedis = apiControlRuleGdsRedisSet
                .stream()
                .filter(ruleGds -> (StringUtils.isNotBlank(ruleGds.getPccCode())))
                .findFirst();

            if (!apiControlRuleGdsRedis.isPresent()) {
                apiControlRuleGdsRedis = apiControlRuleGdsRedisSet
                    .stream()
                    .filter(ruleGds -> (StringUtils.isBlank(ruleGds.getPccCode())))
                    .findFirst();
            }

            if (apiControlRuleGdsRedis.isPresent()) {
                String parameter1=apiControlRuleGdsRedis.get().getParameter1();
                sibeSearchRequest.setNumberOfUnits(Integer.valueOf(parameter1));
                return;
            }
        }
        // LOGGER.debug("uuid:"+uuid+" GDS规则,"+sibeSearchRequest.getGds()+msg+"没有匹配成功,默认方案数量200");
        sibeSearchRequest.setNumberOfUnits(numberOfUnits);
        return;
    }

}

