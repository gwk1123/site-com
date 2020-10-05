package com.sibecommon.service.handler;

import com.sibecommon.repository.entity.GdsRule;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.service.transform.SibeUtil;
import com.sibecommon.utils.exception.CustomSibeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yangdehua on 18/2/10.
 */

@Component
public class GdsRuleCarrierBlackListHandle extends AbstractRuleHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {

        String uuid = sibeSearchRequest.getUuid();
        String gds = sibeSearchRequest.getGds();
        String officeId = sibeSearchRequest.getOfficeId();
        String parameterKey = "GDS-4"; //﻿﻿航线黑名单开关开关
        String msg = "【GDS-航线航司黑名单】";
        String RULE_TYPE = "4"; //GDS-航线黑名单规则类型

        //判断开关是否有开启，如果没有开启则忽略此规则
        if (sibeSearchRequest
            .getSiteRulesSwitch()
            .stream()
            .anyMatch(value -> (parameterKey.equals(value.getParameterKey()) && !"TRUE".equals(value.getParameterValue())))) {
            LOGGER.error("uuid:" + uuid + " " + sibeSearchRequest.getGds() + msg + "开关没有开启");
            return;
        }

        //航线航司黑名单
        StringBuilder prohibitedCarrierBuilder = new StringBuilder();
        List<String> cityList = sibeSearchRequest.getCityPrioritycList();
        for (String city : cityList) {
            String[] cityArray = StringUtils.split(city, "/");
            //注意：GDS规则配置，PCC项目可以为空，为空则表示全部
            List<GdsRule> apiControlRuleGdsRedisList = sibeSearchRequest
                .getGdsRules()
                .stream()
                .filter(gdsRule -> (
                    RULE_TYPE.equals(gdsRule.getRuleType())
                        && gds.equals(gdsRule.getGdsCode())
                        && SibeUtil.contains(gdsRule.getPccCode(), officeId, "/")
                        && SibeUtil.contains(gdsRule.getOrigin(),cityArray[0],"/")
                        && SibeUtil.contains(gdsRule.getDestination(),cityArray[1],"/")))
                .collect(Collectors.toList());

            if (apiControlRuleGdsRedisList != null && apiControlRuleGdsRedisList.size() > 0) {
                apiControlRuleGdsRedisList.forEach(gdsRule -> {
                    prohibitedCarrierBuilder.append(gdsRule.getParameter1()).append("/");
                });
            }
        }
        String[] split = StringUtils.split(prohibitedCarrierBuilder.toString(), "/");

        if(split != null && split.length > 0){
            String carriers = Stream.of(split).distinct().collect(Collectors.joining(","));
            sibeSearchRequest.setProhibitedCarriers(carriers);
        }
    }
}

