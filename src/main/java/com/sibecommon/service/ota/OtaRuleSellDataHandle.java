package com.sibecommon.service.ota;

import com.google.common.base.Splitter;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.repository.entity.SiteRulesSwitch;
import com.sibecommon.service.handler.AbstractRuleHandler;
import com.sibecommon.service.transform.SibeUtil;
import com.sibecommon.utils.common.DateUtil;
import com.sibecommon.utils.constant.SibeConstants;
import com.sibecommon.utils.exception.CustomSibeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class OtaRuleSellDataHandle extends AbstractRuleHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public static final String RULE_TYPE="OTA-31"; //限制销售时间
    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {
        String tripType = sibeSearchRequest.getTripType();
        String uuid = sibeSearchRequest.getUuid();
        List<SiteRulesSwitch> otaSwitchValueRedisSet = sibeSearchRequest.getOtaSiteRulesSwitchs();
        String parameterKey = "OTA-31"; //﻿﻿限制销售时间开关
        String msg = "【OTA-限制销售时间】";

        //判断限制销售时间开关
        if( otaSwitchValueRedisSet
            .stream()
            .filter(Objects::nonNull)
            .anyMatch(value->(parameterKey.equals(value.getParameterKey()) &&!"TRUE".equals(value.getParameterValue()))))
        {
            // LOGGER.debug("uuid:"+uuid+" OTA站点规则，站点"+sibeSearchRequest.getSite()+msg+"开关没有开启");
            return;
        }


        //得到旅行日期列表
        List<Date> travelDateList = null;
        if ("OW".equals(tripType)) {
            travelDateList = new ArrayList<>();
            travelDateList.add(DateUtil.cenvertStringToDate(sibeSearchRequest.getFromDate(), "yyyyMMdd"));
        } else if ("RT".equals(tripType)) {
            travelDateList = new ArrayList<>();
            travelDateList.add(DateUtil.cenvertStringToDate(sibeSearchRequest.getFromDate(), "yyyyMMdd"));
            travelDateList.add(DateUtil.cenvertStringToDate(sibeSearchRequest.getRetDate(), "yyyyMMdd"));
        } else if ("MT".equals(tripType)) {
            travelDateList = Splitter
                .on(",")
                .omitEmptyStrings()
                .trimResults()
                .splitToList(sibeSearchRequest.getFromDate())
                .stream()
                .map(x -> (DateUtil.cenvertStringToDate(x, "yyyyMMdd")))
                .collect(Collectors.toList());
        }

        List<String> cityList = SibeUtil.getCityPriority(sibeSearchRequest.getFromCityRedis(), sibeSearchRequest.getToCityRedis());

        for (String city : cityList) {
            String[] cityArray = StringUtils.split(city, "/");
            List<Date> finalTravelDateList = travelDateList;
            if (sibeSearchRequest
                .getOtaRules()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(otaRuletime -> {
                        if (RULE_TYPE.equals(otaRuletime.getRuleType())
                            && SibeUtil.contains(otaRuletime.getOrigin(), cityArray[0], "/")
                            && SibeUtil.contains(otaRuletime.getDestination(), cityArray[1], "/")) {
                            //只要有一个匹配失败，则抛出异常
                            finalTravelDateList.forEach(reqdate -> {
                                if (reqdate.getTime() < (DateUtil.cenvertStringToDate(otaRuletime.getParameter1(), "yyyy/MM/dd")).getTime() &&
                                    reqdate.getTime() > (DateUtil.cenvertStringToDate(otaRuletime.getParameter2(), "yyyy/MM/dd")).getTime()) {
                                    throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_105, "【OTA-限制销售时间】", uuid, "search");
                                }

                            });
                            return true;
                        }
                        return false;
                    }
                )) {
                return;
            }
        }

    }
}
