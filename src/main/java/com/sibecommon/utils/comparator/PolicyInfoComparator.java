package com.sibecommon.utils.comparator;

import com.sibecommon.utils.constant.PolicyConstans;
import com.sibecommon.repository.entity.PolicyInfo;
import com.sibecommon.service.transform.TransformCommonPolicy;
import com.sibecommon.service.transform.TransformCommonPolicyCompareTool;
import com.sibecommon.utils.constant.SibeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * The type Api policy info dto comparator.
 */
public class PolicyInfoComparator implements Comparator<PolicyInfo> {
    private final int INTERLINE_SIGN_UNLIMITED = 3; //不限联运
    private final int POLICY_YES_NO_UNLIMITED_UNLIMITED = 3; //允许类型 不限
    private Logger LOGGER= LoggerFactory.getLogger(PolicyInfoComparator.class);

    @Override
    public int compare(PolicyInfo policy1, PolicyInfo policy2) {
        List<Integer> conditionMatchPriorityList = TransformCommonPolicy.generateConditionMatchPriorityList();
        return policyCompare(conditionMatchPriorityList, policy1, policy2);
    }

    private static int policyCompare(List<Integer> conditionMatchPriorityList,
                                     PolicyInfo policy1, PolicyInfo policy2) {

        for (int conditionMatchNumber = 0; conditionMatchNumber < conditionMatchPriorityList.size(); conditionMatchNumber++) {

            Integer condition = conditionMatchPriorityList.get(conditionMatchNumber);
            switch (condition) {
                case SibeConstants.MP_TRIP_TYPE:
                    int tripDateCompareResult = TransformCommonPolicyCompareTool.policyCompareTripDate(policy1, policy2);
                    if (tripDateCompareResult != 0) {
                        return tripDateCompareResult;
                    }
                    break;

                case SibeConstants.MP_SEAT_CABIN:
                    int seatCabinCompareResult = TransformCommonPolicyCompareTool.policyCompareSeatCabin(policy1, policy2);
                    if (seatCabinCompareResult != 0) {
                        return seatCabinCompareResult;
                    }
                    break;

                case SibeConstants.MP_PRICE_TYPE:
                    int priceTypeCompareResult = TransformCommonPolicyCompareTool.policyComparePriceType(policy1, policy2);
                    if (priceTypeCompareResult != 0) {
                        return priceTypeCompareResult;
                    }
                    break;

                case SibeConstants.MP_TRANSFER_TYPE:
                    int transferTypeCompareResult = TransformCommonPolicyCompareTool.policyCompareTransferType(policy1, policy2);
                    if (transferTypeCompareResult != 0) {
                        return transferTypeCompareResult;
                    }
                    break;

                case SibeConstants.MP_INTERLINE_TYPE:
                    int interlineTypeCompareResult = TransformCommonPolicyCompareTool.policyCompareInterlineType(policy1, policy2);
                    if (interlineTypeCompareResult != 0) {
                        return interlineTypeCompareResult;
                    }
                    break;

                case SibeConstants.MP_CODE_SHARE_TYPE:
                    int codeShareTypeCompareResult = TransformCommonPolicyCompareTool.policyCompareCodeShareType(policy1, policy2);
                    if (codeShareTypeCompareResult != 0) {
                        return codeShareTypeCompareResult;
                    }
                    break;

                case SibeConstants.MP_INTERLINE_AIRLINE:
                    int interlineAirlineCompareResult = TransformCommonPolicyCompareTool.policyCompareInterlineAirline(policy1, policy2);
                    if (interlineAirlineCompareResult != 0) {
                        return interlineAirlineCompareResult;
                    }
                    break;

                case SibeConstants.MP_INTERLINE_AIRLINE_EXCEPT:
                    int interlineAirlineExceptCompareResult = TransformCommonPolicyCompareTool.policyCompareInterlineAirlineExcept(policy1, policy2);
                    if (interlineAirlineExceptCompareResult != 0) {
                        return interlineAirlineExceptCompareResult;
                    }
                    break;

                case SibeConstants.MP_OUTBOUND_DATE:
                    int outboundDateCompareResult = TransformCommonPolicyCompareTool.policyCompareTravelDate(
                        policy1.getOutboundDateStart(), policy2.getOutboundDateStart());
                    if (outboundDateCompareResult != 0) {
                        return outboundDateCompareResult;
                    }
                    break;

                case SibeConstants.MP_INBOUND_DATE:
                    int inboundDateCompareResult = TransformCommonPolicyCompareTool.policyCompareTravelDate(
                        policy1.getInboundDateStart(), policy2.getInboundDateEnd());
                    if (inboundDateCompareResult != 0) {
                        return inboundDateCompareResult;
                    }
                    break;

                case SibeConstants.MP_SALE_DATE:
                    int saleDateCompareResult = TransformCommonPolicyCompareTool.policyCompareTravelDate(
                        policy1.getSaleDateStart(), policy2.getSaleDateStart());
                    if (saleDateCompareResult != 0) {
                        return saleDateCompareResult;
                    }
                    break;

                case SibeConstants.MP_TRANSFER_POINT:
                    //指定中转点
                    int transferPointCompareResult = TransformCommonPolicyCompareTool.policyCompareTransferPoint(policy1, policy2);
                    if (transferPointCompareResult != 0) {
                        return transferPointCompareResult;
                    }
                    break;

                case SibeConstants.MP_OUTBOUND_DAY_TIME:
                    //去程班期
                    //todo 暂时使用北京时间
                    int outBoundDayTimeCompareResult = TransformCommonPolicyCompareTool.policyCompareDateTime(policy1.getOutboundDayTime(), policy2.getOutboundDayTime());
                    if (outBoundDayTimeCompareResult != 0) {
                        return outBoundDayTimeCompareResult;
                    }
                    break;

                case SibeConstants.MP_INBOUND_DAY_TIME:
                    //回程班期
                    //todo 暂时使用北京时间
                    int inBoundDayTimeCompareResult = TransformCommonPolicyCompareTool.policyCompareDateTime(policy1.getInboundDayTime(), policy2.getInboundDayTime());
                    if (inBoundDayTimeCompareResult != 0) {
                        return inBoundDayTimeCompareResult;
                    }
                    break;

                case SibeConstants.MP_SEAT_GRADE:
                    //适用舱等
                    int seatGradeCompareResult = TransformCommonPolicyCompareTool.policyCompareSeatGrade(policy1, policy2);
                    if (seatGradeCompareResult != 0) {
                        return seatGradeCompareResult;
                    }
                    break;

                case SibeConstants.MP_FLIGHT_AVAILABLE:
                    //可售航班
                    int availableFlightCompareResult = TransformCommonPolicyCompareTool.policyCompareFlightAvailable(policy1, policy2);
                    if (availableFlightCompareResult != 0) {
                        return availableFlightCompareResult;
                    }
                    break;

                case SibeConstants.MP_FLIGHT_PROHIBITED:
                    //禁售航班
                    int prohibitedFlightCompareResult = TransformCommonPolicyCompareTool.policyCompareFlightProhibited(policy1, policy2);
                    if (prohibitedFlightCompareResult != 0) {
                        return prohibitedFlightCompareResult;
                    }
                    break;

                case SibeConstants.MP_ADVANCE_SALE_DAY:
                    //提前销售天数
                    int advanceSaleDayCompareResult = TransformCommonPolicyCompareTool.policyCompareAdvanceSaleDay(policy1, policy2);
                    if (advanceSaleDayCompareResult != 0) {
                        return advanceSaleDayCompareResult;
                    }
                    break;

                case SibeConstants.MP_OUTBOUND_DATE_EXCEPT:
                    //去程除外旅行日期
                    int outboundDateExceptCompareResult = TransformCommonPolicyCompareTool.policyCompareTravelDateRange(
                        policy1.getOutboundTravelDateExcept(), policy2.getOutboundTravelDateExcept());
                    if (outboundDateExceptCompareResult != 0) {
                        return outboundDateExceptCompareResult;
                    }
                    break;

                case SibeConstants.MP_INBOUND_DATE_EXCEPT:
                    //回程除外旅行日期
                    int inboundDateExceptCompareResult = TransformCommonPolicyCompareTool.policyCompareTravelDateRange(
                        policy1.getInboundTravelDateExcept(), policy2.getInboundTravelDateExcept());
                    if (inboundDateExceptCompareResult != 0) {
                        return inboundDateExceptCompareResult;
                    }
                    break;
            }
        }

        //更新时间的校验
        int updateTimeCompareResult = TransformCommonPolicyCompareTool.policyCompareUpdateTime(
            policy1.getUpdateTime(), policy2.getUpdateTime());

        return updateTimeCompareResult;
    }








    /**
     * 判断两个政策的对象类型是否相同，两个允许类型同时为null 或者 同时不为null则相同
     * @param conditionOne
     * @param conditionTwo
     * @return
     */
    private boolean nullConditionEqual(Object conditionOne, Object conditionTwo) {
        if(conditionOne == null && conditionTwo == null){
            return true;
        }else if(conditionOne != null && conditionTwo != null){
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
    private boolean permitConditionEqual(int conditionOne, int conditionTwo) {
        if(conditionOne == POLICY_YES_NO_UNLIMITED_UNLIMITED && conditionTwo == POLICY_YES_NO_UNLIMITED_UNLIMITED){
            return true;
        }else if(conditionOne != POLICY_YES_NO_UNLIMITED_UNLIMITED && conditionTwo != POLICY_YES_NO_UNLIMITED_UNLIMITED){
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
    private boolean priceTypeConditionEqual(PolicyInfo apiPolicyInfoOne, PolicyInfo apiPolicyInfoTwo) {
        if(apiPolicyInfoOne.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL) && apiPolicyInfoTwo.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL)){
            return true;
        }else if(!apiPolicyInfoOne.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL) && !apiPolicyInfoTwo.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL)){
            return true;
        }else {
            return false;
        }
    }





}
