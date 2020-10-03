package com.sibecommon.service.handler;

import com.sibecommon.repository.entity.GdsRule;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.service.transform.SibeUtil;
import com.sibecommon.utils.exception.CustomSibeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class GdsRuleCarrierSourceSetHandle extends AbstractRuleHandler {

    private final Logger LOGGER= LoggerFactory.getLogger(this.getClass());

    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest)throws CustomSibeException {
        String uuid=sibeSearchRequest.getUuid();
        String gds=sibeSearchRequest.getGds();
        String parameterKey="GDS-28";
        String RULE_TYPE="28";
        String msg="【GDS-航司指定GDS来源设置】";

        if( sibeSearchRequest
            .getSiteRulesSwitch()
            .stream()
            .anyMatch(value->(parameterKey.equals(value.getParameterKey())&&!"TRUE".equals(value.getParameterValue()))))
        {
            LOGGER.error("uuid:"+uuid+" "+sibeSearchRequest.getGds()+msg+"开关没有开启");
            return;
        }

        List<GdsRule> apiControlRuleGdsRedisList=new ArrayList<>();
        List<String> cityList= sibeSearchRequest.getCityPrioritycList();
        for(String city:cityList){
            String[] cityArray= StringUtils.split(city,"/");
             apiControlRuleGdsRedisList = sibeSearchRequest
                .getGdsRuleSet()
                .stream()
                .filter(Objects::nonNull)
                .filter(ruleGds -> (
                    RULE_TYPE.equals(ruleGds.getRuleType())
                    && (!gds.equals(ruleGds.getGdsCode()))
                    && SibeUtil.contains(ruleGds.getOrigin(),cityArray[0],"/")
                    && SibeUtil.contains(ruleGds.getDestination(),cityArray[1],"/")
                    )
                ).collect(Collectors.toList());
        }

        if (apiControlRuleGdsRedisList != null && apiControlRuleGdsRedisList.size() > 0){
            StringBuilder prohibitedCarrierBuilder = new StringBuilder();
            apiControlRuleGdsRedisList.forEach(gdsRule -> prohibitedCarrierBuilder.append(org.springframework.util.StringUtils.replace(gdsRule.getParameter1(),"/",",")).append(","));

            //删除后面多余的"逗号"
            if(prohibitedCarrierBuilder.length()>0){
                prohibitedCarrierBuilder.deleteCharAt(prohibitedCarrierBuilder.length() - 1);
            }
            if(sibeSearchRequest.getProhibitedCarriers() !=null && sibeSearchRequest.getProhibitedCarriers().length()>0){
                sibeSearchRequest.setProhibitedCarriers(sibeSearchRequest.getProhibitedCarriers()+","+prohibitedCarrierBuilder.toString());
                return;
            }else {
                sibeSearchRequest.setProhibitedCarriers(prohibitedCarrierBuilder.toString());
                return;
            }
        }

        return ;

    }

}
