package com.sibecommon.service.ota;

import com.sibecommon.ota.site.*;
import com.sibecommon.ota.site.*;
import com.sibecommon.service.transform.RouteRuleUtil;
import com.sibecommon.ota.site.*;
import com.sibecommon.repository.entity.OtaRule;
import com.sibecommon.service.transform.SibeUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yangdehua on 18/2/23.
 */
public class OtaRuleFilter {

    private static Logger LOGGER = LoggerFactory.getLogger(OtaRuleFilter.class);
    private static String OTA_OWT = "OWT";


    /**
     * Ota rule filter boolean.
     * 返回TRUE的航线保留
     *
     * @param sibeSearchRequest the ota search request
     * @param routing           the routing
     * @return the boolean
     */
    public static boolean  otaRuleFilter(final SibeSearchRequest sibeSearchRequest, final SibeRouting routing, Map<String,String> ruleMap) {

        //站点
        final String groupKey = "OTA_SITE_SWITCH_" + sibeSearchRequest.getSite();

        if (routing.getFromSegments()==null || (routing.getFromSegments().size()==0)) {
            return false;
        }

        Map<String,String> ruleTypeMap = new HashMap<>();
        if(ruleMap==null||ruleMap.isEmpty()){
            //非单程直达，则需要校验
            if (!("OW".equals(sibeSearchRequest.getTripType()) && (routing.getFromSegments().size() == 1))) {
                ruleTypeMap.put("OTA-10", "OTA-10");
            }
            //往返，则需要校验
            if ("RT".equals(sibeSearchRequest.getTripType())) {
                //限制机场连接开关
                ruleTypeMap.put("OTA-12", "OTA-12");
            }
            //限制共享航司开关
            ruleTypeMap.put("OTA-13", "OTA-13");
            //限制混合航司开关
            ruleTypeMap.put("OTA-14", "OTA-14");
            //允许承运航司为空开关
            ruleTypeMap.put("OTA-16", "OTA-16");
          //  ruleTypeMap.put("OTA-19", "OTA-19"); //限制旅行日期范围开关
            //除外限制中转次数开关
            ruleTypeMap.put("OTA-9", "OTA-9");
            //航司航线黑名单
            ruleTypeMap.put("OTA-33", "OTA-33");
            //限制销售时间
            ruleTypeMap.put("OTA-31", "OTA-31");
            //航司自动上线和下线
            ruleTypeMap.put("OTA-37","OTA-37");

        }else {
            ruleTypeMap.putAll(ruleMap);
        }

        /*
         * 1.判断开关是否有开启，如果没有开启则忽略此规则
         * 2.并过滤相关的规则, 只要有一条限制没有满足，则返回False
         */
        return !sibeSearchRequest
            .getSiteRulesSwitch()
            .stream()
            .filter(Objects::nonNull)
            .filter(valueRedis -> (groupKey.equals(valueRedis.getGroupKey())
                && ruleTypeMap.containsKey(valueRedis.getParameterKey())
                && "TRUE".equals(valueRedis.getParameterValue())))
            .anyMatch(valueRedis -> {
                boolean flag=false;
                switch (valueRedis.getParameterKey()) {
                    case "OTA-10": {
                        flag = restrictedTransitTime(sibeSearchRequest, routing);
                        break;
                    }
                    case "OTA-12": {
                        flag = restrictedAirportLinks(sibeSearchRequest, routing);
                        break;
                    }
                    case "OTA-13": {
                        flag = restrictedShareTheShippingDepartment(sibeSearchRequest, routing);
                        break;
                    }
                    case "OTA-14": {
                        flag = restrictedMixingDivision(sibeSearchRequest, routing);
                        break;
                    }
                    case "OTA-16": {
                        flag = allowTheCarrierToBeEmpty(sibeSearchRequest, routing);
                        break;
                    }
                   /* case "OTA-19": {
                        flag = restrictedFilterHour(sibeSearchRequest, routing);
                        setSibeRoutingStatus(flag,routing);
                        break;
                    }*/
//                    case "OTA-31": {
//                        flag = restrictedFilterHour(sibeSearchRequest, routing);
//                        break;
//                    }
                    case "OTA-9": {
                        flag = restrictedFilterTransitTimes(sibeSearchRequest, routing);
                        break;
                    }
                    case "OTA-33":{
                       flag = otaAirrouteBlacklistHandle(sibeSearchRequest, routing) ;
                        break;
                    }
//                    case "OTA-37":{
//                        flag = otaOfflineAirLineHand(sibeSearchRequest, routing) ;
//                        break;
//                    }
                    case "OTA-30":{
                        //只执行，不更新flag标志
                        processLowCabin(sibeSearchRequest, routing) ;
                        break;
                    }


                    default: {
                        flag = false;
                        break;
                    }
                }
                return flag;

            });
    }




    /**
     * 航司自动上线下线
     * @param sibeSearchRequest sibeSearchRequest
     * @param routing routing
     * @return 是否在自动下线列表中,返回true，过滤方案。
     */
//    public static boolean otaOfflineAirLineHand(SibeSearchRequest sibeSearchRequest,SibeRouting routing) {
//
//        //【OTA-航司自动下线名单】
//        //出发城市和到达城市
//        String fromCity = sibeSearchRequest.getFromCityRedis().getCityCode();
//        String toCity = sibeSearchRequest.getToCityRedis().getCityCode();
//        //销售航司
//        String airline = routing.getValidatingCarrier();
//
//        if (StringUtils.isEmpty(fromCity)|| StringUtils.isEmpty(toCity)|| StringUtils.isEmpty(airline)){
//                return  false;
//        }
//
//        if (sibeSearchRequest.getApiOfflineAirLineRedisSet()!=null&&sibeSearchRequest.getApiOfflineAirLineRedisSet().size()>0) {
//            return sibeSearchRequest.getApiOfflineAirLineRedisSet().stream().filter(k->k!=null&&
//                !StringUtils.isEmpty(k.getAirline())).
//                anyMatch(o -> {
//                    if (!StringUtils.isEmpty(o.getDepCity())&&!StringUtils.isEmpty(o.getArrCity())) {
//                        return fromCity.equals(o.getDepCity()) && toCity.equals(o.getArrCity()) && airline.equals(o.getAirline());
//                    } else {
//                        return airline.equals(o.getAirline());
//                    }
//                });
//
//        } else {
//            return  false;
//        }
//
//    }

    /**
     * 航司航线黑名单
     * @param sibeSearchRequest
     * @param routing
     * @return
     */
    public static boolean otaAirrouteBlacklistHandle(SibeSearchRequest sibeSearchRequest,SibeRouting routing){
        String RULE_TYPE="33";
        final String msg = "【OTA-航司航线黑名单】";
        String tripType= sibeSearchRequest.getTripType();
        LocalDateTime timeNow =LocalDateTime.now();
        int year = timeNow.getYear();
        int month = timeNow.getMonth().getValue();
        int day = timeNow.getDayOfMonth();
        LocalDateTime effectiveTime = LocalDateTime.of(year, month, day, 00, 00, 00);
        LocalDateTime fromDate = LocalDateTime.parse(RouteRuleUtil.getFromDate(sibeSearchRequest)+"000000", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        List<String> cityList= sibeSearchRequest.getCityPrioritycList();
        for(String city:cityList) {
            String[] cityArray = StringUtils.split(city, "/");
            if (sibeSearchRequest
                .getOtaRules()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(gdsRule -> {
                    if(RULE_TYPE.equals(gdsRule.getRuleType())
                        && SibeUtil.contains(gdsRule.getParameter1(), tripType, "/")
                        // && SibeUtil.containsNotEmpty(gdsRule.getParameter2(), routing.getValidatingCarrier(),"/")
//                    && SibeUtil.contains(gdsRule.getOrigin(), cityArray[0],"/")
//                    && SibeUtil.contains(gdsRule.getDestination(), cityArray[1],"/")
                        && bothWaysFlag(gdsRule, cityArray, sibeSearchRequest)
                        && carrierCabin(gdsRule, routing)
//                        && (gdsRule.getEffectiveFrom().isBefore(effectiveTime) || effectiveTime.equals(gdsRule.getEffectiveFrom()))
//                        && (gdsRule.getEffectiveTo().isAfter(effectiveTime) || effectiveTime.equals(gdsRule.getEffectiveTo()))
                        && (gdsRule.getTravelPeriodFrom().isBefore(fromDate) || fromDate.equals(gdsRule.getTravelPeriodFrom()))
                        && (gdsRule.getTravelPeriodTo().isAfter(fromDate) || fromDate.equals(gdsRule.getTravelPeriodTo()))){
                        return true;
                    } return false;
                })) {
                return true;
            }
        }
        return false;
    }

    /**
     * 双向开关校验
     * @return
     */
    public static boolean bothWaysFlag (OtaRule apiControlRuleOtaRedis, String[] cityName, SibeSearchRequest sibeSearchRequest){
        if(apiControlRuleOtaRedis.getBothWaysFlag()==2 ){
            if(SibeUtil.contains(apiControlRuleOtaRedis.getOrigin(),cityName[0],"/")
                && SibeUtil.contains(apiControlRuleOtaRedis.getDestination(),cityName[1],"/")){
                return true;
            }else if (SibeUtil.contains(apiControlRuleOtaRedis.getDestination(),cityName[0],"/")
                && SibeUtil.contains(apiControlRuleOtaRedis.getOrigin(),cityName[1],"/")){
                return true;
            }
            return  false ;

        }else if (apiControlRuleOtaRedis.getBothWaysFlag()==1 && SibeUtil.contains(apiControlRuleOtaRedis.getOrigin(),cityName[0],"/")
            && SibeUtil.contains(apiControlRuleOtaRedis.getDestination(),cityName[1],"/") ){
            return true;
        }
        return false;
    }

    public static boolean carrierCabin(OtaRule apiControlRuleOtaRedis,SibeRouting routing){
        List<SibeSegment>segmentList=new ArrayList<>();
        segmentList.addAll(routing.getFromSegments());
        segmentList.addAll(routing.getRetSegments());
        //AA(Q)
        String[] carriers=apiControlRuleOtaRedis.getParameter2().split(",");
        for(String item:carriers){
            item=item.toUpperCase();
            //Q
            String cabins=item.substring(item.lastIndexOf("(")+1,item.lastIndexOf(")"));
            //AA
            String carrier=item.substring(0,item.lastIndexOf("("));
            for(SibeSegment segment:segmentList){
                if(carrier.equals(segment.getCarrier())){
                    if(SibeUtil.contains(cabins,segment.getCabin(),"/")){
                            return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Restricted filter transit times boolean.
     *
     * @param sibeSearchRequest the sibe search request
     * @param routing           the routing
     * @return the boolean
     */
    public static boolean restrictedFilterTransitTimes(SibeSearchRequest sibeSearchRequest, SibeRouting routing) {
        //OTA-限制中转次数>
        final String RULE_TYPE = "9";
        final String msg = "【OTA-限制中转次数】";
        //限制中转次数
        int transitTimes = 3;

        Optional<OtaRule> apiControlRuleOtaRedis =sibeSearchRequest
            .getOtaRules()
            .stream()
            .filter(Objects::nonNull)
            .filter(otaRuleRedis ->(RULE_TYPE.equals(otaRuleRedis.getRuleType())))
            .findFirst();

        if (apiControlRuleOtaRedis.isPresent()){
            transitTimes = Integer.parseInt(apiControlRuleOtaRedis.get().getParameter1());
        }

        sibeSearchRequest.setTransitTimes(transitTimes);

        Map<Integer, List<SibeSegment>> segmentMap = Stream.of(routing.getFromSegments(),routing.getRetSegments())
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(SibeSegment::getFlightIndicator));

        int finalTransitTimes = transitTimes<=0?1:transitTimes;

        // LOGGER.debug("uuid:"+routing.getUid()+" "+routing.getReservationType()+"-"+routing.getOfficeId()+msg + "匹配成功,将过滤掉此方案, 中转次数限制为："+finalTransitTimes);
        if(segmentMap.entrySet()
            .stream()
            .anyMatch(x->(x.getValue().size()> finalTransitTimes))){
            return true;
        }

        //没有匹配成功，不需要过滤，返回false
        return false;
    }


    /**
     * Restricted transit time boolean.
     * 中转时间过滤，仅仅针对多程
     * 返回值true：需要过滤
     * 返回值false：不需要过滤
     *
     * @param sibeSearchRequest the ota search request
     * @param routing           the routing
     * @return the boolean
     */
    public static boolean restrictedTransitTime(SibeSearchRequest sibeSearchRequest, SibeRouting routing){
        //OTA-限制中转停留时间>
        final String RULE_TYPE = "10";
        final String msg = "【OTA-限制中转停留时间】";
        //合并List
        List<SibeSegment> segmentList = Stream.of(routing.getFromSegments(),routing.getRetSegments())
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        List<Integer> segmentTransitTimeList =new ArrayList();

        //航程航段数超过1的，先使用航程分组，再判断size是否大于1
        segmentList
            .stream()
            .sorted(Comparator.comparing(SibeSegment::getItemNumber))
            .collect(Collectors.groupingBy(SibeSegment::getFlightIndicator))
            .entrySet()
            .stream()
            .filter(Objects::nonNull)
            .map(flightIndicatorMap->flightIndicatorMap.getValue())
            .filter(flightIndicatorList->(flightIndicatorList.size()>1))
            .forEach(segmentList2->{
                for (int i = 1; i < segmentList2.size(); i++) {
                    //前一航段的到达时间
                    org.joda.time.format.DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHHmm");
                    DateTime arrTime = DateTime.parse(segmentList2.get(i - 1).getArrTime(),dateTimeFormatter);
                    //后一航段的起飞时间
                    DateTime depTime = DateTime.parse(segmentList2.get(i ).getDepTime(),dateTimeFormatter);
                    Period p = new Period(arrTime, depTime, PeriodType.minutes());
                    segmentTransitTimeList.add(p.getMinutes());

//                    // LOGGER.debug("uuid:"+routing.getUid()+" GDS规则"+msg
//                        +"前一航程段："+segmentList2.get(i-1).getFlightIndicator()
//                        +"-"+segmentList2.get(i - 1).getItemNumber()
//                        +" ArrTime:" +segmentList2.get(i - 1).getArrTime()
//                        +" 后一航程段："+segmentList2.get(i).getFlightIndicator()
//                        +"-"+segmentList2.get(i).getItemNumber()
//                        +" DepTime:"+segmentList2.get(i ).getDepTime() +" 转机时间"+p.getMinutes()+"分钟");

                }
            });

        //如果航程中的航段数少于2则,没有匹配成功，不需要过滤，返回false
        if (segmentTransitTimeList.size()==0) {
            return false;
        }
        //List<String> cityList= sibeSearchRequest.getCityPrioritycList();
        //for(String city:cityList){
            //String[] cityArray= StringUtils.split(city,"/");
            //成功匹配其中一个则退出方法，过滤掉方案
            if(sibeSearchRequest
                .getOtaRules()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(otaRuleRedis ->(
                    RULE_TYPE.equals(otaRuleRedis.getRuleType())
                    //&& SibeUtil.contains(otaRuleRedis.getOrigin(),cityArray[0],"/")
                    //&& SibeUtil.contains(otaRuleRedis.getDestination(),cityArray[1],"/")
                    && segmentTransitTimeList
                        .stream()
                        .anyMatch(segmentTransitTime->{
                        if(Integer.parseInt(otaRuleRedis.getParameter1()) > segmentTransitTime){
                            return true;
                        }else {
                            return false;
                        }})))){
                return true;
            }
        //}
        //没有匹配成功，不需要过滤，返回false
        return false;
    }


    /**
     * Restricted airport links boolean.
     * 机场链接过滤
     * 仅仅针对中转点，不以机场过滤，
     *
     * @param sibeSearchRequest the ota search request
     * @param routing           the routing
     * @return the boolean
     */
    public static boolean restrictedAirportLinks(SibeSearchRequest sibeSearchRequest, SibeRouting routing){
        //OTA-限制机场连接
        final String RULE_TYPE = "12";
        final String msg = "【OTA-限制机场连接】";

        //合并List
        List<SibeSegment> segmentList = Stream.of(routing.getFromSegments(),routing.getRetSegments())
            .flatMap(Collection::stream)
            .collect(Collectors.toList());


        //先使用航程分组，再判断size是否大于1
        boolean airportLinksFlag=segmentList
            .stream()
            .sorted(Comparator.comparing(SibeSegment::getItemNumber))
            .collect(Collectors.groupingBy(SibeSegment::getFlightIndicator))
            .entrySet()
            .stream()
            .filter(Objects::nonNull)
            .map(flightIndicatorMap->flightIndicatorMap.getValue())
            .filter(flightIndicatorList->(flightIndicatorList.size()>1))
            .anyMatch(segmentList2->{
                for (int i = 1; i < segmentList2.size(); i++) {
                    //前一航段的到达机场
                    String  arrAirport = segmentList2.get(i - 1).getArrAirport();
                    //后一航段的到达机场
                    String depAirport =  segmentList2.get(i ).getDepAirport();

                    // LOGGER.debug("uuid:"+routing.getUid()+" "+routing.getReservationType()+"-"+routing.getOfficeId()+msg+"前一航程段："+segmentList2.get(i-1).getFlightIndicator() +"-"+segmentList2.get(i - 1).getItemNumber() +" 到达机场:" +segmentList2.get(i - 1).getArrAirport() +" 后一航程段："+segmentList2.get(i).getFlightIndicator() +"-"+segmentList2.get(i).getItemNumber() +" 出发机场:"+segmentList2.get(i ).getDepAirport());
                    if(!arrAirport.equals(depAirport)) {
                        return true;
                    }
                }
                return false;
            });

        //如果所有中转机场是相同的 则不需要过滤，返回false
        if (airportLinksFlag){
            //限制机场连接
            //List<String> cityList= sibeSearchRequest.getCityPrioritycList();
            //for(String city:cityList){
                //String[] cityArray= StringUtils.split(city,"/");
                // LOGGER.debug("uuid:"+routing.getUid()+" "+routing.getReservationType()+"-"+routing.getOfficeId()+msg+" 出发地："+cityArray[0] +" 目的地："+cityArray[1] + "匹配成功,将过滤掉此方案");
                //成功匹配其中一个则退出方法，过滤掉方案
                if(sibeSearchRequest
                    .getOtaRules()
                    .stream()
                    .filter(Objects::nonNull)
                    .anyMatch(otaRuleRedis -> (
                        RULE_TYPE.equals(otaRuleRedis.getRuleType())
                        //&& SibeUtil.contains(otaRuleRedis.getOrigin(),cityArray[0],"/")
                        //&& SibeUtil.contains(otaRuleRedis.getDestination(),cityArray[1],"/")
                        && "1".equals(otaRuleRedis.getParameter1())
                    ))) {
                    return true;
                }
            //}
        }else{
            return false;
        }

        //没有匹配成功，不需要过滤，返回false
        return false;
    }

    /**
     * Allow the carrier to be empty boolean.
     * 承运航司为空过滤
     *
     * @param sibeSearchRequest the ota search request
     * @param routing           the routing
     * @return the boolean
     */
    public static boolean allowTheCarrierToBeEmpty(SibeSearchRequest sibeSearchRequest, SibeRouting routing){
        //OTA-限制承运航司>
        final String RULE_TYPE = "16";
        final String msg = "【OTA-限制承运航司为空】";

        //List<String> cityList= sibeSearchRequest.getCityPrioritycList();
        List<SibeSegment> segmentList = new ArrayList<>();


        segmentList.addAll(routing.getFromSegments());
        segmentList.addAll(routing.getRetSegments());


        Stream.of(routing.getFromSegments(),routing.getRetSegments())
            .flatMap(Collection::stream)
            .collect(Collectors.toList());


        //for(String city:cityList){
            //String[] cityArray= StringUtils.split(city,"/");
            //成功匹配其中一个则退出方法，过滤掉方案
            if(sibeSearchRequest
                .getOtaRules()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(gdsRule -> {
                    //限制承运航司不允许为空，如果为空返回TRUE
                    // LOGGER.debug("uuid:"+routing.getUid()+" "+routing.getReservationType()+"-"+routing.getOfficeId()+msg + " OTA规则ID:" + otaRuleRedis.getId());
                    if (RULE_TYPE.equals(gdsRule.getRuleType())
                        //&& SibeUtil.contains(gdsRule.getOrigin(),cityArray[0],"/")
                        //&& SibeUtil.contains(gdsRule.getDestination(),cityArray[1],"/")
                        //2不允许
                        && "2".equals(gdsRule.getParameter1()) ){
                        if (segmentList.stream().anyMatch(segment -> (StringUtils.isEmpty(segment.getOperatingCarrier())))){
                            return true;
                        }}
                    return false;
                })){
                return true;
            }
            //}
        //没有匹配成功，不需要过滤，返回false
        return false;
    }


    /**
     * 限制混合航司
     *
     * @param sibeSearchRequest the ota search request
     * @param routing           the routing
     * @return the boolean
     */
    public static boolean restrictedMixingDivision(SibeSearchRequest sibeSearchRequest, SibeRouting routing){
        //OTA-限制混合航司开关>
        final String RULE_TYPE = "14";
        final String msg = "【OTA-限制混合航司】";
        //限制中转停留时间
        //List<String> cityList= sibeSearchRequest.getCityPrioritycList();
        Set<String> carrierSet =
            Stream.of(routing.getFromSegments(),routing.getRetSegments())
            .flatMap(Collection::stream)
            .map(x->x.getCarrier())
            .collect(Collectors.toSet());

        if (carrierSet.size()>1) {
            //for (String city : cityList) {
                //String[] cityArray = StringUtils.split(city, "/");
                //成功匹配其中一个则退出方法，过滤掉方案
                if (sibeSearchRequest
                    .getOtaRules()
                    .stream()
                    .filter(Objects::nonNull)
                    .anyMatch(otaRuleRedis -> {
                        if (RULE_TYPE.equals(otaRuleRedis.getRuleType())
                           // && SibeUtil.contains(otaRuleRedis.getOrigin(),cityArray[0],"/")
                            //&& SibeUtil.contains(otaRuleRedis.getDestination(),cityArray[1],"/")
                            && "1".equals(otaRuleRedis.getParameter1()))
                        {
                            return true;
                        }else {
                            return false;
                        }}))
                {
                    return true;
                }
            //}
        }
        //没有匹配成功，不需要过滤，返回false
        return false;
    }


    /**
     * 限制共享航司
     *
     * @param sibeSearchRequest the ota search request
     * @param routing           the routing
     * @return the boolean
     */
    public static boolean restrictedShareTheShippingDepartment(SibeSearchRequest sibeSearchRequest, SibeRouting routing){
        //OTA-限制共享航司开关>
        final String RULE_TYPE = "13";
        final String msg = "【OTA-限制共享航司】";
        //List<String> cityList= sibeSearchRequest.getCityPrioritycList();

        boolean codeShareFlag=
            Stream.of(routing.getFromSegments(),routing.getRetSegments())
                .flatMap(Collection::stream)
                .anyMatch(segment -> (segment.getCodeShare()));

        if (codeShareFlag) {
            //for (String city : cityList) {
                //String[] cityArray = StringUtils.split(city, "/");
                if (sibeSearchRequest
                    .getOtaRules()
                    .stream()
                    .filter(Objects::nonNull)
                    .anyMatch(otaRuleRedis -> {
                       //return processRestrictedShare(codeShareFlag, RULE_TYPE, cityArray, otaRuleRedis, routing);
                        return processRestrictedShare(codeShareFlag, RULE_TYPE, otaRuleRedis, routing);
                        }
                    )){
                    return true;
                }
            }
        //}

        //行程中没有codeShare，则直接可以匹配该规则，返回false
        return false;
    }

    /**
     * 过滤则返回true，否则返回false
     * @param codeShareFlag
     * @param rule_type
     * @param otaRuleRedis
     * @param routing
     * @return
     */
    private static boolean processRestrictedShare(boolean codeShareFlag, String rule_type, OtaRule otaRuleRedis, SibeRouting routing) {

        //匹配上限制共享规则，则进行共享相关校验
        if(otaRuleRedis != null && rule_type.equals(otaRuleRedis.getRuleType()) &&
           // cityArray[0].equals(otaRuleRedis.getOrigin()) &&
            //cityArray[1].equals(otaRuleRedis.getDestination()) &&
            "1".equals(otaRuleRedis.getParameter1())) {

            //如果填写了除外共享航司航班号,则判断航段中的销售航司是否处于除外列表中
            if(StringUtils.isNotBlank(otaRuleRedis.getParameter2())){
                Map<String, RestrictCodeShareFlightInfo> restrictCodeShareFlightInfoMap = new HashMap<>();

                //输入格式类似于AA100-500,600/BB200-400/AA700
                //1.先分割出每个航司需要过滤的航班号
                String[] flightInfoArray = otaRuleRedis.getParameter2().split("/");

                //2.再分割出航司和航班号
                //boolean returnFlag = false;
                for (String carriers : flightInfoArray) {
                    String excludedCarrier = carriers.substring(0,2);
                    String flightNums = carriers.substring(2);

                    RestrictCodeShareFlightInfo restrictCodeShareFlightInfo;
                    if(restrictCodeShareFlightInfoMap.get(excludedCarrier) == null){
                        restrictCodeShareFlightInfo = new RestrictCodeShareFlightInfo();
                        restrictCodeShareFlightInfoMap.put(excludedCarrier, restrictCodeShareFlightInfo);
                    }else {
                        restrictCodeShareFlightInfo = restrictCodeShareFlightInfoMap.get(excludedCarrier);
                    }

                    //3.分割出航班号区间
                    String[] flightNumArray = flightNums.split(",");
                    for (String flightNumberRange : flightNumArray) {
                        String[] flightNumberRangeStringArray = flightNumberRange.split("-");
                        int[] flightNumberRangeIntArray = stringArrayToIntArray(flightNumberRangeStringArray);
                        Arrays.sort(flightNumberRangeIntArray);

                        if(flightNumberRangeIntArray.length == 1){
                            restrictCodeShareFlightInfo.getFlightNumberMap().put(flightNumberRangeIntArray+"", flightNumberRangeIntArray+"");
                        }else {
                            FlightRules flightRules = new FlightRules();
                            flightRules.setStartFlight(flightNumberRangeIntArray[0]);
                            flightRules.setEndFlight(flightNumberRangeIntArray[1]);
                            restrictCodeShareFlightInfo.getFlightRulesList().add(flightRules);
                        }
                    }
                }

                boolean validateFromSegmentSign= validateSegmentListByCodeShareRule(routing.getFromSegments(), restrictCodeShareFlightInfoMap);
                boolean validateRetSegmentSign= validateSegmentListByCodeShareRule(routing.getRetSegments(), restrictCodeShareFlightInfoMap);

                if(! (validateFromSegmentSign && validateRetSegmentSign)){
                    return true;
                }else {
                    return false;
                }

            }else {
                //如果没有填写了除外共享航司航班号,则直接判断是否为共享
                if(codeShareFlag){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 校验航段list中的所有航段是否都满足共享规则的要求，都满足返回true，否则返回false
     * @param segmentList
     *
     * @return
     */
    private static boolean validateSegmentListByCodeShareRule(List<SibeSegment> segmentList,Map<String,RestrictCodeShareFlightInfo> restrictCodeShareFlightInfoMap) {
        if(segmentList == null || segmentList.size() == 0){
            return true;
        }

        for (SibeSegment segment : segmentList) {
            //判断该航段是否为共享航段，共享航段才需要进行除外列表校验
            if(! segment.getCodeShare()){
                continue;
            }

            //判航段中的销售航司
            if (restrictCodeShareFlightInfoMap.get(segment.getCarrier()) == null) {
                return false;
            }

            RestrictCodeShareFlightInfo restrictCodeShareFlightInfo = restrictCodeShareFlightInfoMap.get(segment.getCarrier());
            if(restrictCodeShareFlightInfo.getFlightNumberMap().get(segment.getFlightNumber()) == null){
                int flightNumber = Integer.valueOf(segment.getFlightNumber());
                //是否满足航班飞范围要求
                boolean matchRangeSign = false;
                for(FlightRules flightRules : restrictCodeShareFlightInfo.getFlightRulesList()){
                    if(flightNumber >= flightRules.getStartFlight() && flightNumber <= flightRules.getEndFlight()){
                        matchRangeSign = true;
                        break;
                    }
                }

                if(! matchRangeSign){
                    return false;
                }
            }
        }

        return true;
    }

    private static int[] stringArrayToIntArray(String[] stringArray) {
        int[] intArray = new int[2];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }

    /**
     * 过滤起飞小时段
     *
     * @param sibeSearchRequest the ota search request
     * @param routing           the routing
     * @return the boolean
     * @throws Exception the exception
     */
//    public static boolean restrictedFilterHour(SibeSearchRequest sibeSearchRequest, SibeRouting routing) {
//        final String RULE_TYPE = "31"; //OTA-限制销售时间开关>
//        final String msg = "【OTA-销售时间】";
//        List<String> cityList= sibeSearchRequest.getCityPrioritycList();
//
//        String depTimeStr =routing.getFromSegments().get(0).getDepTime();
//        LocalDateTime depTime = LocalDateTime.parse(depTimeStr, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYYMMDDHHMM));
//
//        String tripType = sibeSearchRequest.getTripType();
//        //得到旅行日期列表
//        List<Date> travelDateList = null;
//        if ("OW".equals(tripType)) {
//            travelDateList = new ArrayList<>();
//            travelDateList.add(DateUtil.cenvertStringToDate(sibeSearchRequest.getFromDate(), "yyyyMMdd"));
//        } else if ("RT".equals(tripType)) {
//            travelDateList = new ArrayList<>();
//            travelDateList.add(DateUtil.cenvertStringToDate(sibeSearchRequest.getFromDate(), "yyyyMMdd"));
//            travelDateList.add(DateUtil.cenvertStringToDate(sibeSearchRequest.getRetDate(), "yyyyMMdd"));
//        } else if ("MT".equals(tripType)) {
//            travelDateList = Splitter
//                .on(",")
//                .omitEmptyStrings()
//                .trimResults()
//                .splitToList(sibeSearchRequest.getFromDate())
//                .stream()
//                .map(x -> (DateUtil.cenvertStringToDate(x, "yyyyMMdd")))
//                .collect(Collectors.toList());
//        }
//
//        //for(String city:cityList){
//            //String[] cityArray= StringUtils.split(city,"/");
//            List<Date> finalTravelDateList = travelDateList;
//            //成功匹配其中一个则退出方法，过滤掉方案
//            if(sibeSearchRequest
//                .getOtaRuleRedisSet()
//                .stream()
//                .filter(Objects::nonNull)
//                .anyMatch(otaRuleRedis -> {
//                    if (RULE_TYPE.equals(otaRuleRedis.getRuleType())
//                        //&& SibeUtil.contains(otaRuleRedis.getOrigin(),cityArray[0],"/")
//                        //&& SibeUtil.contains(otaRuleRedis.getDestination(),cityArray[1],"/")
//                        ) {
//
//                        for(Date reqdate : finalTravelDateList){
//                            if (reqdate.getTime() >= (DateUtil.cenvertStringToDate(otaRuleRedis.getParameter1(), "yyyy/MM/dd")).getTime() &&
//                                reqdate.getTime() <= (DateUtil.cenvertStringToDate(otaRuleRedis.getParameter2(), "yyyy/MM/dd")).getTime()) {
//                                //获得当前时间的时分
//                                DateFormat dateFormat=new SimpleDateFormat("HH:mm");
//                                Calendar now = Calendar.getInstance();
//
//                                String dateTime = now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE);
//
//                                //获得多少小时不销售内容
//                                String timeQuantum = otaRuleRedis.getParameter3();
//                                //没有填写时间段的时候
//                                if(!timeQuantum.contains(":")){
//                                    Integer restrictedHours = Integer.parseInt(otaRuleRedis.getParameter3());
//                                    LocalDateTime restrictedTime =LocalDateTime.now().plusHours(restrictedHours);
//                                    if (restrictedTime.isAfter(depTime)){
//                                        return true;
//                                    }
//                                }else { //填写了时间段的时候
//                                    String[] timeArray = StringUtils.split(timeQuantum, "/"); //09:00-11:00|24
//                                    for(String time:timeArray){
//                                        String getTime = time.substring(0, time.indexOf("|"));  //01:00-02:00
//                                        String[] littleArray = StringUtils.split(getTime,"-") ;//时间段的开始时间和结束时间  数组
//
//                                            Date d1 = dateFormat.parse(littleArray[0],new ParsePosition(0)); //开始时间
//                                            Date d2 = dateFormat.parse(littleArray[1],new ParsePosition(0)); //结束时间
//                                            Date d3 = dateFormat.parse(dateTime,new ParsePosition(0));                      //当前时间
//                                            if ((d3.equals(d1) || (d1.before(d3)&&d3.before(d2)) || d3.equals(d2))) {
//                                                Integer restrictedHours = Integer.parseInt(time.substring(time.indexOf("|") + 1));
//                                                LocalDateTime restrictedTime = LocalDateTime.now().plusHours(restrictedHours);
//                                                if (restrictedTime.isAfter(depTime)) {
//                                                    return  true;
//                                                }
//                                            }
//                                        }
//
//                                    }
//
//                                }
//                        }
//                    }
//                    return false;
//                })) {
//                return true;
//            }
//        //}
//        //没有匹配成功，不需要过滤，返回false
//        return false;
//    }


    /**
     * Gets ota site over night info.
     * 过夜票面留钱
     *
     * @param sibeSearchRequest the ota search request
     * @return the ota site over night info
     */
    public static OtaRuleOverNightInfo getOtaSiteOverNightInfo(SibeSearchRequest sibeSearchRequest) {

        final String RULE_TYPE = "27"; //OTA-过夜票面留钱开关>
        final String msg = "【OTA-过夜票面留钱】";
        //List<String> cityList= sibeSearchRequest.getCityPrioritycList();

        //for(String city:cityList){
            //String[] cityArray= StringUtils.split(city,"/");
            Optional<OtaRuleOverNightInfo> otaSiteOverNightInfo=sibeSearchRequest
                .getOtaRules()
                .stream()
                .filter(Objects::nonNull)
                .filter(otaRuleRedis ->  (
                    RULE_TYPE.equals(otaRuleRedis.getRuleType())
                    //&& SibeUtil.contains(otaRuleRedis.getOrigin(),cityArray[0],"/")
                    //&& SibeUtil.contains(otaRuleRedis.getDestination(),cityArray[1],"/")
                        ))
                .map(otaRuleRedis->{
                    LocalTime overNightStart =LocalTime.parse(otaRuleRedis.getParameter1(), DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime overNightend =LocalTime.parse(otaRuleRedis.getParameter2(), DateTimeFormatter.ofPattern("HH:mm"));
                    BigDecimal money = new BigDecimal(otaRuleRedis.getParameter3());
                    OtaRuleOverNightInfo overNight = new OtaRuleOverNightInfo();
                    overNight.setEnd(overNightend);
                    overNight.setPrice(money);
                    overNight.setStart(overNightStart);
                    return overNight; })
                .findFirst();

                if(otaSiteOverNightInfo.isPresent()) {
                    return otaSiteOverNightInfo.get();
                }
        //}
        //没有匹配成功，则设置默认过夜留钱金额
        LocalTime overNightStart =LocalTime.parse("23:30", DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime overNightend =LocalTime.parse("00:30", DateTimeFormatter.ofPattern("HH:mm"));
        BigDecimal money = new BigDecimal("5000");
        OtaRuleOverNightInfo overNight = new OtaRuleOverNightInfo();
        overNight.setEnd(overNightend);
        overNight.setPrice(money);
        overNight.setStart(overNightStart);
        return overNight;

    }

    /**
     * 限制止损
     *
     * @param sibeSearchRequest the ota search request
     * @return the int
     */
    public static void restrictedStopLoss(SibeSearchRequest sibeSearchRequest) {
        final String RULE_TYPE = "26"; //OTA-限制止损开关>
        final String msg = "【OTA-限制止损】";

        //List<String> cityList= sibeSearchRequest.getCityPrioritycList();

        //for(String city:cityList){
            //String[] cityArray= StringUtils.split(city,"/");
            Optional<Integer> apiControlRuleOtaRedis =
                sibeSearchRequest
                    .getOtaRules()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(otaRuleRedis ->(
                        RULE_TYPE.equals(otaRuleRedis.getRuleType())
                        //&& SibeUtil.contains(otaRuleRedis.getOrigin(),cityArray[0],"/")
                        //&& SibeUtil.contains(otaRuleRedis.getDestination(),cityArray[1],"/")
                    ))
                    .map(otaRuleRedis->(Integer.parseInt(otaRuleRedis.getParameter1()))) //限制止损 %
                    .findFirst();

            if (apiControlRuleOtaRedis.isPresent() ){
                int stopLossPercentage =apiControlRuleOtaRedis.get();
                // LOGGER.debug("uuid:"+sibeSearchRequest.getUuid()+" "+sibeSearchRequest.getSite()+msg+" 限制止损值为："+stopLossPercentage);
                sibeSearchRequest.setStopLossPercentage(stopLossPercentage);
                return;
            }
        //}
        //如果没有匹配，则默认配置为10000块 todo 需要优化
        sibeSearchRequest.setStopLossPercentage(10000);
    }

    /**
     * 价格止损，当亏损值大于指定比例*GDS原始总价时，则直接过滤掉该方案
     *
     * @param sibeSearchRequest the ota search request
     * @param routing           the routing
     * @return the boolean
     */
    public static boolean otaPriceStopLoss(SibeSearchRequest sibeSearchRequest, SibeRouting routing){
        //TODO K位产品不做校验
        if("2".equals(routing.getSibeRoutingData().getSibePolicy().getProductType())){
            return true;
        }

        //限制止损 %
        BigDecimal stopLossPercentage = BigDecimal.valueOf(sibeSearchRequest.getStopLossPercentage());

        if(stopLossPercentage==null || stopLossPercentage.compareTo(BigDecimal.valueOf(70))==1 ){
            stopLossPercentage = BigDecimal.valueOf(70);
        }

        //成人票面价+成人税费（港币）
        Integer originalGDSAdultTotalPriceHKD = routing.getAdultPriceGDS() + routing.getAdultTaxGDS();
        //儿童票面价+儿童税费（港币） 注意：没有儿童价格，使用成人价格计算
        Integer originalGDSChildTotalPriceHKD = routing.getAdultPriceGDS() + routing.getAdultTaxGDS();

        //汇率
        BigDecimal priceRate = routing.getSibeRoutingData().getSibePolicy().getGdsToOTARate();

        //不经过加减留钱的 GDS 价格 转平台 价格
        //成人票面价+成人税费（人民币） GDS返回
        BigDecimal originalGDSAdultTotalPriceGDS;
        //儿童票面价+儿童税费（人民币） GDS返回
        BigDecimal originalGDSChildTotalPriceGDS;

        originalGDSAdultTotalPriceGDS = (new BigDecimal(originalGDSAdultTotalPriceHKD)).multiply(priceRate);
        originalGDSChildTotalPriceGDS =  (new BigDecimal(originalGDSChildTotalPriceHKD)).multiply(priceRate);

//        if(! sibeSearchRequest.getOta().equals(OTA_OWT)){
//            originalGDSAdultTotalPriceGDS = (new BigDecimal(originalGDSAdultTotalPriceHKD)).multiply(priceRate);
//            originalGDSChildTotalPriceGDS =  (new BigDecimal(originalGDSChildTotalPriceHKD)).multiply(priceRate);
//        }else {
//            originalGDSAdultTotalPriceGDS = new BigDecimal(originalGDSAdultTotalPriceHKD);
//            originalGDSChildTotalPriceGDS =  new BigDecimal(originalGDSChildTotalPriceHKD);
//        }

        //成人票面价+成人税费（人民币） 经过政策计算的
        Integer saleAdultTotalPriceInteger;
        //儿童票面价+儿童税费（人民币） 经过政策计算的
        Integer saleChildTotalPriceInteger;

        saleAdultTotalPriceInteger = routing.getAdultPriceOTA() + routing.getAdultTaxOTA();
        saleChildTotalPriceInteger = routing.getChildPriceOTA() + routing.getChildTaxOTA();
//        if(! sibeSearchRequest.getOta().equals(OTA_OWT)){
//            saleAdultTotalPriceInteger = routing.getAdultPriceOTA() + routing.getAdultTaxOTA();
//            saleChildTotalPriceInteger = routing.getChildPriceOTA() + routing.getChildTaxOTA();
//        }else {
//            saleAdultTotalPriceInteger = routing.getAdultPriceHKD() + routing.getAdultTaxHKD();
//            saleChildTotalPriceInteger = routing.getChildPriceHKD() + routing.getChildTaxHKD();
//        }

        BigDecimal saleAdultTotalPrice = new BigDecimal(saleAdultTotalPriceInteger);
        BigDecimal saleChildTotalPrice = new BigDecimal(saleChildTotalPriceInteger);

        //成人亏损金额 政策后价格-GDS原始价格（人民币）
        BigDecimal adultLoss = saleAdultTotalPrice.subtract(originalGDSAdultTotalPriceGDS);

        //儿童亏损金额 政策后价格-GDS原始价格（人民币）
        BigDecimal childLoss = saleChildTotalPrice.subtract(originalGDSChildTotalPriceGDS);

        //当(成人价格)出现亏损的时候
        if( adultLoss.compareTo(new BigDecimal(0))!=1){
            BigDecimal adultLossPositive = adultLoss.abs();
            //如果亏损的金额 > 允许亏损的金额，则过滤
            // LOGGER.debug("uuid:"+routing.getUid()+" "+routing.getReservationType()+"-"+routing.getOfficeId()+"【OTA规则-限制止损】成人价格达到止损值,销售价格:"+saleAdultTotalPrice +"GDS原价格:"+ originalGDSAdultTotalPriceCNY +"止损比例为:"+stopLossPercentage+"%");
            if(adultLossPositive.compareTo(originalGDSAdultTotalPriceGDS.multiply(stopLossPercentage.multiply(BigDecimal.valueOf(0.01)))) == 1) {
                return false;
            }
        }

        //当(儿童价格)出现亏损的时候
        if( childLoss.compareTo(new BigDecimal(0))!=1){
            BigDecimal childLossPositive = childLoss.abs();
            //如果亏损的金额 > 允许亏损的金额，则过滤
            // LOGGER.debug("uuid:"+routing.getUid()+ " " + routing.getReservationType()+"-"+routing.getOfficeId() +"【OTA规则-限制止损】儿童价格达到止损值,销售价格:" + saleAdultTotalPrice +"GDS原价格:"+ originalGDSAdultTotalPriceCNY  +"止损比例为:"+stopLossPercentage+"%");
            if(childLossPositive.compareTo(originalGDSChildTotalPriceGDS.multiply(stopLossPercentage.multiply(BigDecimal.valueOf(0.01)))) == 1){
                return false;
            }

        }

        return true;
    }


    /**
     * 指定低舱位功能
     */
    public static void processLowCabin(SibeSearchRequest sibeSearchRequest, SibeRouting routing) {
        //4.3 指定低舱位功能
        //判断指定舱位开关
        final String RULE_TYPE = "30"; //OTA-指定舱位规则
        Map<String, String> assignCabinMap = new HashMap<>();
        sibeSearchRequest.getOtaRules()
            .stream()
            .filter(Objects::nonNull)
            .filter(otaRule -> (RULE_TYPE.equals(otaRule.getRuleType())))
            .forEach(otaRule->{
                String lowCabin = otaRule.getParameter2();
                String[] carrierCabinArray = lowCabin.split("/");

                Arrays.stream(carrierCabinArray).forEach(item -> {
                    String[] itemArray = item.split(":");
                    //格式：NH:O/AA:P
                    assignCabinMap.put(itemArray[0].toUpperCase(), itemArray[1].toUpperCase());
                });

            });

        processAssignCabin(assignCabinMap,  routing);


    }


    /***
     * 更改指定舱位
     * @param assignCabinMap
     * @param routing
     */
    private static void processAssignCabin(Map<String,String> assignCabinMap, SibeRouting routing) {

        routing
            .getFromSegments()
            .stream()
            .filter(Objects::nonNull)
            .forEach(
                sibeSegment -> {
                    sibeSegment.setOriginalCabin(sibeSegment.getCabin());
                    if(StringUtils.isNotBlank(assignCabinMap.get(sibeSegment.getCarrier()))){
                        sibeSegment.setCabin(assignCabinMap.get(sibeSegment.getCarrier()));
                    }
                }
        );

        routing
            .getRetSegments()
            .stream()
            .filter(Objects::nonNull)
            .forEach(
                sibeSegment -> {
                    sibeSegment.setOriginalCabin(sibeSegment.getCabin());
                    if(StringUtils.isNotBlank(assignCabinMap.get(sibeSegment.getCarrier()))){
                        sibeSegment.setCabin(assignCabinMap.get(sibeSegment.getCarrier()));
                    }
            }
        );

        routing.getSibeRoutingData().setUseAssignCabin("1");
    }


    /**
     * 获得站点的缓存更新时间，缓存有效时间，并写入SibeSearchRequest
     *
     * @param sibeSearchRequest the ota search request
     * @return the int
     */
    public static void getCacheRefreshTime(SibeSearchRequest sibeSearchRequest) {
        final String RULE_TYPE = "24"; //限制缓存时间

        Optional<OtaRule> apiControlRuleOtaRedis =
            sibeSearchRequest.getOtaRules()
                .stream()
                .filter(Objects::nonNull)
                .filter(otaRuleRedis ->(RULE_TYPE.equals(otaRuleRedis.getRuleType())))
                .findFirst();

        //todo 此规则暂时不需要添加航线纬度,后续再完善
        int cacheValidTime = 10; //默认缓存时间30分钟
        int cacheRefreshTime = 5; //默认刷新时间5分钟

        if (apiControlRuleOtaRedis.isPresent()) {
            cacheValidTime = Integer.parseInt(apiControlRuleOtaRedis.get().getParameter1());
            cacheRefreshTime = Integer.parseInt(apiControlRuleOtaRedis.get().getParameter2());
        }
        sibeSearchRequest.setOtaCacheValidTime(cacheValidTime);
        sibeSearchRequest.setOtaCacheRefreshTime(cacheRefreshTime);
    }

    /**
     * 获得站点的超时
     *
     * @param sibeSearchRequest the ota search request
     * @return the int
     */
    public static void getTimeOutTime(SibeSearchRequest sibeSearchRequest) {
        final String RULE_TYPE = "22"; //OTA-限制请求超时>
        Optional<OtaRule> apiControlRuleOtaRedis =sibeSearchRequest
            .getOtaRules()
            .stream()
            .filter(Objects::nonNull)
            .filter(otaRuleRedis ->( RULE_TYPE.equals(otaRuleRedis.getRuleType())))
            .findFirst();

        //todo 此规则暂时不需要添加航线纬度,后续再完善
        int timeOutTime = 30; //默认缓存时间30秒
        if (apiControlRuleOtaRedis.isPresent())
            timeOutTime = Integer.parseInt(apiControlRuleOtaRedis.get().getParameter1());
        sibeSearchRequest.setTimeOutTime(timeOutTime);
    }


    /**
     * 限制生单起飞时间变化差
     *
     * @param <T>             the type parameter
     * @param sibeBaseRequest the ota request
     * @return the int
     */
    public static <T extends SibeBaseRequest> void getDepTimeDeviation(T sibeBaseRequest) {
        final String RULE_TYPE = "25"; //OTA-限制生单起飞时间变化差>

        Optional<OtaRule> apiControlRuleOtaRedis =sibeBaseRequest
            .getOtaRules()
            .stream()
            .filter(Objects::nonNull)
            .filter(otaRuleRedis ->( RULE_TYPE.equals(otaRuleRedis.getRuleType())))
            .findFirst();

        int depTimeEarly=0; //限制生单起飞时间变化差(提前）
        int depTimeDelay=0; //限制生单起飞时间变化差(延时）

        //todo 此规则暂时不需要添加航线纬度,后续再完善
        if (apiControlRuleOtaRedis.isPresent()) {
            depTimeEarly = Integer.parseInt(apiControlRuleOtaRedis.get().getParameter1());
            depTimeDelay = Integer.parseInt(apiControlRuleOtaRedis.get().getParameter2());
        }
        sibeBaseRequest.setDepTimeEarly(depTimeEarly);
        sibeBaseRequest.setDepTimeDelay(depTimeDelay);
    }


}
