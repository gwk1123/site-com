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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yangdehua on 18/2/10.
 */
public class OtaRuleTravelDateHandle extends AbstractRuleHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public static final String RULE_TYPE="19"; //除外限制旅行日期范围开关
    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {
        String tripType = sibeSearchRequest.getTripType();
        String uuid = sibeSearchRequest.getUuid();
        List<SiteRulesSwitch> otaSwitchValueRedisSet = sibeSearchRequest.getOtaSiteRulesSwitchs();
        String parameterKey = "OTA-19"; //﻿﻿除外限制旅行日期范围开关
        String msg = "【OTA-除外限制旅行日期范围】";

        //判断开关是否有开启，如果没有开启则忽略此规则
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
                .anyMatch(otaRule -> {
                        if (RULE_TYPE.equals(otaRule.getRuleType())
                            && SibeUtil.contains(otaRule.getOrigin(), cityArray[0], "/")
                            && SibeUtil.contains(otaRule.getDestination(), cityArray[1], "/")) {
                            //只要有一个匹配失败，则抛出异常
                          finalTravelDateList.forEach(reqdate -> {
                                    if (reqdate.getTime() >= (DateUtil.cenvertStringToDate(otaRule.getParameter1(), "yyyy/MM/dd")).getTime() &&
                                        reqdate.getTime() <= (DateUtil.cenvertStringToDate(otaRule.getParameter2(), "yyyy/MM/dd")).getTime()) {
                                        // LOGGER.debug("uuid:" + uuid + " OTA站点规则，旅行日期:" + reqdate + msg + "返回限制响应信息:" + SearchCommonConstants.RULE_FAIL_MSG_TRAVEL_DATE_VALIDATE);
                                        throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_105, msg, uuid,"search");
                                    }

                                });

                            //获得当前时间的时分
                            Date currentTime = new Date();
                            DateFormat dateFormat=new SimpleDateFormat("HH:mm");

                            //获得多少小时不销售内容
                            String timeQuantum = otaRule.getParameter3();
                            //没有填写时间段的时候
                            if(!timeQuantum.contains(":")){
                                Integer restrictedHours = Integer.parseInt(otaRule.getParameter3());
                                LocalDateTime restrictedTime =LocalDateTime.now().plusHours(restrictedHours);
                                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                String localTime = fmt.format(restrictedTime);
                                LocalDate date = LocalDate.parse(localTime, fmt);
                                LocalDate reqTime = DateUtil.getDateToLocalDate(finalTravelDateList.get(0));
                                if (date.isAfter(reqTime)){
                                    throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_105, msg+restrictedHours+"小时内不销售", uuid,"search");
                                }
                            }else{ //填写了时间段的时候
                                String[] timeArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(timeQuantum,"/"); //9:00-11:00~24
                                Arrays
                                    .asList(timeArray)
                                    .stream()
                                    .forEach(time->{
                                        String getTime = time.substring(0,time.indexOf("|"));  //01:00-02:00
                                        String[] littleArray =  getTime.split("-");//时间段的开始时间和结束时间  数组
                                        try {
                                            Date d1 = dateFormat.parse(littleArray[0]); //开始时间
                                            Date d2 = dateFormat.parse(littleArray[1]); //结束时间
                                            Date d3 = currentTime;                      //当前时间
                                            if((d3.equals(d1) || d1.before(d3))
                                                || (d3.equals(d2) || d3.before(d2))) {
                                                Integer restrictedHours = Integer.parseInt(time.substring(time.indexOf("|")+1));
                                                LocalDateTime restrictedTime =LocalDateTime.now().plusHours(restrictedHours);
                                                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                String localTime = fmt.format(restrictedTime);
                                                LocalDate date = LocalDate.parse(localTime, fmt);
                                                LocalDate reqTime = DateUtil.getDateToLocalDate(finalTravelDateList.get(0));
                                                if (date.isAfter(reqTime)){
                                                    throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_105, msg+restrictedHours+"小时内不销售", uuid,"search");
                                                }
                                            }
                                        } catch (ParseException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    });
                            }

                            finalTravelDateList.forEach(reqdate -> {
                                if (reqdate.getTime() >= (DateUtil.cenvertStringToDate(otaRule.getParameter1(), "yyyy/MM/dd")).getTime() &&
                                    reqdate.getTime() <= (DateUtil.cenvertStringToDate(otaRule.getParameter2(), "yyyy/MM/dd")).getTime()) {
                                    // LOGGER.debug("uuid:" + uuid + " OTA站点规则，旅行日期:" + reqdate + msg + "返回限制响应信息:" + SearchCommonConstants.RULE_FAIL_MSG_TRAVEL_DATE_VALIDATE);
                                    throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_105, msg, uuid, "search");
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
        // todo 如果开关打开了，没有配置旅行日期范围，则会抛出异常
//        LOGGER.warn("uuid:" + uuid +  msg + "没有配置旅行日期范围，则会抛出异常:"+SearchCommonConstants.RULE_FAIL_MSG_TRAVEL_DATE_VALIDATE);
//        throw new CustomLYException(AliTripConstants.RETURN_CODE_OTHER_ERROR,
//            SearchCommonConstants.RULE_FAIL_MSG_TRAVEL_DATE_VALIDATE, uuid);
    }
}

