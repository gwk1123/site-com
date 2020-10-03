package com.sibecommon.service.transform;


import com.sibecommon.utils.constant.PolicyConstans;
import com.sibecommon.repository.entity.PolicyInfo;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The type Policy single condition match tool.
 */
public class TransformCommonPolicyCompareTool {
    private final static int INTERLINE_SIGN_UNLIMITED = 3; //不限联运
    private final static int POLICY_YES_NO_UNLIMITED_UNLIMITED = 3; //允许类型 不限


    public static boolean tripTypeConditionEqual(String tripTypeOne, String tripTypeTwo) {
        if(tripTypeOne.equals(PolicyConstans.POLICY_TRIP_TYPE_ALL) && tripTypeTwo.equals(PolicyConstans.POLICY_TRIP_TYPE_ALL)){
            return true;
        }else if(!tripTypeOne.equals(PolicyConstans.POLICY_TRIP_TYPE_ALL) && !tripTypeTwo.equals(PolicyConstans.POLICY_TRIP_TYPE_ALL)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断两个政策的对象类型是否相同，两个允许类型同时为null 或者 同时不为null则相同
     * @param conditionOne
     * @param conditionTwo
     * @return
     */
    public static boolean nullConditionEqual(Object conditionOne, Object conditionTwo) {
        if(conditionOne == null && conditionTwo == null){
            return true;
        }else if(conditionOne != null && conditionTwo != null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断两个政策的字符串类型的条件是否相同，两个条件同时为空，或者同时有值，则为相同返回true，否则返回false
     * @param conditionOne
     * @param conditionTwo
     * @return
     */
    public static boolean stringConditionEqual(String conditionOne, String conditionTwo) {
        if(StringUtils.isNotBlank(conditionOne) && StringUtils.isNotBlank(conditionTwo)){
            return true;
        }else if(StringUtils.isBlank(conditionOne) && StringUtils.isBlank(conditionTwo)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断两个政策的价格类型是否相同，两个价格类型同时为ALL或者同时不为ALL则相同，相同则为相同返回true，否则返回false
     * @param apiPolicyInfoOne
     * @param apiPolicyInfoTwo
     * @return
     */
    public static boolean priceTypeConditionEqual(PolicyInfo apiPolicyInfoOne, PolicyInfo apiPolicyInfoTwo) {
        if(apiPolicyInfoOne.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL) && apiPolicyInfoTwo.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL)){
            return true;
        }else if(!apiPolicyInfoOne.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL) && !apiPolicyInfoTwo.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断两个政策的允许类型是否相同，两个允许类型同时为不限 或者 同时不为不限则相同
     * @param conditionOne
     * @param conditionTwo
     * @return
     */
    public static boolean permitConditionEqual(int conditionOne, int conditionTwo) {
        if(conditionOne == POLICY_YES_NO_UNLIMITED_UNLIMITED && conditionTwo == POLICY_YES_NO_UNLIMITED_UNLIMITED){
            return true;
        }else if(conditionOne != POLICY_YES_NO_UNLIMITED_UNLIMITED && conditionTwo != POLICY_YES_NO_UNLIMITED_UNLIMITED){
            return true;
        }else {
            return false;
        }
    }

    //字符串类条件对比
    private static int compareStringCondition(String condition1, String condition2){
        if(stringConditionEqual(condition1, condition2)){
            return 0;
        }else if(StringUtils.isNotBlank(condition1)
            && StringUtils.isBlank(condition2)){
            return 1;
        }else {
            return -1;
        }
    }

    //允许类型条件对比
    private static int comparePermitTypeCondition(Integer condition1, Integer condition2){
        if(permitConditionEqual(condition1, condition2)){
            return 0;
        }else if(condition1 != POLICY_YES_NO_UNLIMITED_UNLIMITED && condition2 == POLICY_YES_NO_UNLIMITED_UNLIMITED){
            return 1;
        }else {
            return -1;
        }
    }


    public static int policyCompareTripDate(PolicyInfo policy1, PolicyInfo policy2) {
        //行程类型，两个均有值则相等
        if(tripTypeConditionEqual(policy1.getTripType(), policy2.getTripType())){
            return 0;
        }else if(!policy1.getTripType().equals(PolicyConstans.POLICY_TRIP_TYPE_ALL)
            && policy2.getTripType().equals(PolicyConstans.POLICY_TRIP_TYPE_ALL)){
            return 1;
        }else {
            return -1;
        }
    }


    //适用舱位
    public static int policyCompareSeatCabin(PolicyInfo policy1, PolicyInfo policy2) {
       return compareStringCondition(policy1.getSeatCabin(), policy2.getSeatCabin());
    }




    public static int policyComparePriceType(PolicyInfo policy1, PolicyInfo policy2) {
        //价格类型（公有/私有/不限）
        if(priceTypeConditionEqual(policy1, policy2)){
            return 0;
        }else if(!policy1.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL)
            && policy2.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL)){
            return 1;
        }else {
            return -1;
        }

    }

    public static int policyCompareTransferType(PolicyInfo policy1, PolicyInfo policy2) {
        return comparePermitTypeCondition(policy1.getPermitTransit(), policy2.getPermitTransit());
    }

    public static int policyCompareInterlineType(PolicyInfo policy1, PolicyInfo policy2) {
        return comparePermitTypeCondition(policy1.getPermitInterline(), policy2.getPermitInterline());
    }

    public static int policyCompareCodeShareType(PolicyInfo policy1, PolicyInfo policy2) {
        return comparePermitTypeCondition(policy1.getPermitCodeShare(), policy2.getPermitCodeShare());
    }

    public static int policyCompareInterlineAirline(PolicyInfo policy1, PolicyInfo policy2) {
        return compareStringCondition(policy1.getInterlineAirline(), policy2.getInterlineAirline());
    }

    public static int policyCompareInterlineAirlineExcept(PolicyInfo policy1, PolicyInfo policy2) {
        return compareStringCondition(policy1.getInterlineAirlineExcept(), policy2.getInterlineAirlineExcept());
    }


    public static int policyCompareTravelDate(LocalDate outboundDateStart1, LocalDate outboundDateStart2) {
        if(nullConditionEqual(outboundDateStart1 , outboundDateStart2)){
            return 0;
        }else if(outboundDateStart1 != null && outboundDateStart2 == null){
            return 1;
        }else {
            return -1;
        }
    }

    public static int policyCompareTransferPoint(PolicyInfo policy1, PolicyInfo policy2) {
        return compareStringCondition(policy1.getTransferPoint(), policy2.getTransferPoint());
    }

    public static int policyCompareSeatGrade(PolicyInfo policy1, PolicyInfo policy2) {
       return compareStringCondition(policy1.getSeatGrade(), policy2.getSeatGrade());
    }

    public static int policyCompareFlightAvailable(PolicyInfo policy1, PolicyInfo policy2) {
        return compareStringCondition(policy1.getAvailableFlight(), policy2.getAvailableFlight());
    }

    public static int policyCompareFlightProhibited(PolicyInfo policy1, PolicyInfo policy2) {
        return compareStringCondition(policy1.getProhibitedFlight(), policy2.getProhibitedFlight());
    }

    public static int policyCompareAdvanceSaleDay(PolicyInfo policy1, PolicyInfo policy2) {
        if(policy1.getAdvanceSaleDay() == null && policy2.getAdvanceSaleDay() == null){
            return 0;
        }else if(policy1.getAdvanceSaleDay() != null && policy2.getAdvanceSaleDay() != null){
            if(policy1.getAdvanceSaleDay() > policy2.getAdvanceSaleDay()){
                return 1;
            }else if(policy1.getAdvanceSaleDay() < policy2.getAdvanceSaleDay()){
                return -1;
            }else {
                return 0;
            }
        }else {
            if(policy1.getAdvanceSaleDay() != null && policy2.getAdvanceSaleDay() == null){
                return 1;
            }else {
                return -1;
            }
        }
    }

    public static int policyCompareTravelDateRange(String outboundTravelDateExcept1, String outboundTravelDateExcept2) {
        return compareStringCondition(outboundTravelDateExcept1, outboundTravelDateExcept2);
    }

    public static int policyCompareUpdateTime(LocalDateTime updateTime1, LocalDateTime updateTime2) {
        //更新时间
        if(updateTime1.equals(updateTime2)){
            return 0;
        }else if(updateTime1.isAfter(updateTime2)){
            return 1;
        }else {
            return -1;
        }
    }

    /**
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static int policyCompareDateTime(String dateTime1, String dateTime2) {
        return compareStringCondition(dateTime1, dateTime2);
    }
}
