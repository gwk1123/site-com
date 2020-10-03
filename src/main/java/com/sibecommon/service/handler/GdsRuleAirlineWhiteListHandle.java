package com.sibecommon.service.handler;

import com.sibecommon.repository.entity.GdsRule;
import com.sibecommon.service.transform.RouteRuleUtil;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.service.transform.SibeUtil;
import com.sibecommon.utils.exception.CustomSibeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GdsRuleAirlineWhiteListHandle extends AbstractRuleHandler {
    private final Logger LOGGER= LoggerFactory.getLogger(GdsRuleAirlineWhiteListHandle.class);
    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {
        String uuid=sibeSearchRequest.getUuid();
        String parameterKey="GDS-30";//航司航线白名单
        String msg="【GDS-航司航线白名单】";
        String RULE_TYPE="30";
        String gds=sibeSearchRequest.getGds();
        String office= sibeSearchRequest.getOfficeId();

        //判断开关是否开启，如果没有忽略此条
        if(sibeSearchRequest
            .getSiteRulesSwitch()
            .stream()
            .anyMatch(value ->(parameterKey.equals(value.getParameterKey()) && !"TRUE".equals(value.getParameterValue())))){
            LOGGER.warn("uuid:"+uuid+" "+sibeSearchRequest.getGds()+msg+"开关没有开启");
            return;
        }
        LocalDate time =LocalDate.now();
        LocalDate beginDateTime = LocalDate.parse(RouteRuleUtil.getFromDate(sibeSearchRequest), DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<String> cityList= sibeSearchRequest.getCityPrioritycList();
        List<GdsRule>apiControlRuleGdsRedisList= new ArrayList<>();
        for(String city:cityList) {
            String[] cityArray = StringUtils.split(city, "/");
              sibeSearchRequest
                .getGdsRuleSet()
                .stream()
                .filter(Objects::nonNull)
                .filter(gdsRule -> (
                    RULE_TYPE.equals(gdsRule.getRuleType())
                    && gds.equals(gdsRule.getGdsCode())
                    && SibeUtil.contains(gdsRule.getPccCode(),office,"/")
//                    && SibeUtil.contains(gdsRule.getOrigin(),cityArray[0],"/")//此验证为单向验证，所以注释
//                    && SibeUtil.contains(gdsRule.getDestination(),cityArray[1],"/")
                    && (gdsRule.getEffectiveFrom().isBefore(time) || time.equals(gdsRule.getEffectiveFrom()))
                    && (gdsRule.getEffectiveTo().isAfter(time) || time.equals(gdsRule.getEffectiveTo()))
//                    && (gdsRule.getTravelPeriodFrom().isBefore(beginDateTime) || beginDateTime.equals(gdsRule.getTravelPeriodFrom()))
//                    && (gdsRule.getTravelPeriodTo().isAfter(beginDateTime) || beginDateTime.equals(gdsRule.getTravelPeriodTo()))
                    && validateBothWays(gdsRule, cityArray))
                ).forEach(apiControlRuleGdsRedisList::add);
        }
            if(apiControlRuleGdsRedisList != null && apiControlRuleGdsRedisList.size() > 0){
                StringBuilder flightType =new StringBuilder();
                StringBuilder airline=new StringBuilder();
                for(GdsRule apiControlRuleGdsRedis:apiControlRuleGdsRedisList){
                    flightType.append(apiControlRuleGdsRedis.getParameter1());
                    airline.append(org.springframework.util.StringUtils.replace(apiControlRuleGdsRedis.getParameter2(),"/",",")).append(",");
                }

                sibeSearchRequest.setFlightType(1);

                if(flightType.length() > 0){
                    boolean isDirect=true;
                    boolean isTransfer=true;
                    String[] str=flightType.toString().split("");
                   for(int i=0;i<str.length;i++){
                       if(!"3".equals(str[i])){
                           isDirect = false;
                       }
                       if(!"2".equals(str[i])){
                           isTransfer = false;
                       }
                   }
                   if(isDirect){
                       sibeSearchRequest.setFlightType(3);
                   }
                   if(isTransfer){
                       sibeSearchRequest.setFlightType(2);
                   }
                }
                if(airline.length()>0) {
                    airline.deleteCharAt(airline.length() - 1);
                    sibeSearchRequest.setAirline(airline.toString());
                }
                return ;
            }
        return ;

    }

    /**
     * 双向规则验证
     * @param ruleGds
     * @param cityArray
     * @return
     */
    public Boolean validateBothWays(GdsRule ruleGds, String[] cityArray) {

        if(ruleGds.getBothWaysFlag() == 2) {

            if(SibeUtil.contains(ruleGds.getOrigin(), cityArray[0], "/") && SibeUtil.contains(ruleGds.getDestination(), cityArray[1], "/")) {
                return true;
            } else if(SibeUtil.contains(ruleGds.getDestination(), cityArray[0], "/") && SibeUtil.contains(ruleGds.getOrigin(), cityArray[1], "/")) {
                return true;
            }

        } else {
            if(ruleGds.getBothWaysFlag() == 1 && SibeUtil.contains(ruleGds.getOrigin(), cityArray[0], "/") && SibeUtil.contains(ruleGds.getDestination(), cityArray[1], "/")) {
                return true;
            }
        }

        return false;
    }
}
