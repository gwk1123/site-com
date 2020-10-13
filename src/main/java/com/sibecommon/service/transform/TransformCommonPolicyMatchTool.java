package com.sibecommon.service.transform;

import com.sibecommon.ota.site.FlightRules;
import com.sibecommon.ota.site.TravelDateRange;
import com.sibecommon.utils.comparator.SegmentComparator;
import com.sibecommon.utils.constant.PolicyConstans;
import com.sibecommon.ota.site.SibeRouting;
import com.sibecommon.ota.site.SibeSegment;
import com.sibecommon.repository.entity.PolicyInfo;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The type Policy single condition match tool.
 */
public class TransformCommonPolicyMatchTool {
    /**
     * 适用舱等匹配
     * @param apiPolicyInfo the api policy info
     * @param routing the routing
     * @param tripType the trip type
     * @return boolean
     */
    public static boolean policyMatchSetaGrade(PolicyInfo apiPolicyInfo, SibeRouting routing, String tripType) {
        //政策中适用舱等为空表示不限制，则进行下一个条件的判断
        if(StringUtils.isBlank(apiPolicyInfo.getSeatGrade())){
            return true;
        }else{
            if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ONE_WAY)){
                return seatCabinOrseatGrateValidate(PolicyConstans.MASTER_SEGMENT_VALIDATE_GRADE,routing.getFromSegments(), apiPolicyInfo.getSeatGrade());

            }else if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ROUND_WAY)){
                boolean fromSegmentValidateSign = seatCabinOrseatGrateValidate(PolicyConstans.MASTER_SEGMENT_VALIDATE_GRADE, routing.getFromSegments(), apiPolicyInfo.getSeatGrade());
                boolean retSegmentValidateSign = seatCabinOrseatGrateValidate(PolicyConstans.MASTER_SEGMENT_VALIDATE_GRADE, routing.getRetSegments(), apiPolicyInfo.getSeatGrade());
                if(fromSegmentValidateSign && retSegmentValidateSign){
                    return true;
                }else {
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * 政策中转点匹配，routing与政策匹配则返回true，否则返回false
     * @param apiPolicyInfo the api policy info
     * @param routing the routing
     * @return boolean
     */
    public static boolean policyMatchTransferPoint(PolicyInfo apiPolicyInfo, SibeRouting routing) {
        //允许中转才进行该判断
        if(apiPolicyInfo.getPermitTransit() == PolicyConstans.POLICY_YES_NO_UNLIMITED_YES){
            //如果指定中转点为空，则表示不限，匹配该政策，返回true
            if(StringUtils.isBlank(apiPolicyInfo.getTransferPoint())){
                return true;
            }else {
                //政策中有中转点
                //如果行程中没有中转点，则不能匹配该政策
                if(routing.getTransferPoint() == null || routing.getTransferPoint().size() == 0){
                    return false;
                }else {
                    //如果行程中存在一个 政策中没有的转机点，则不能匹配该政策
                    for(String transferPoint : routing.getTransferPoint()){
                        if(! apiPolicyInfo.getTransferPoint().contains("#"+transferPoint)){
                            return false;
                        }
                    }

                    return true;
                }
            }
        }else {
            //政策不为中转，则不需要匹配该条件，直接返回true
            return true;
        }

    }

    /**
     * 政策销售日期匹配，当前时间与政策中的销售日期匹配
     * 当前时间 >= 销售开始时间 && 当前时间 <= 销售结束时间，匹配成功返回true，否则返回false
     * @param apiPolicyInfo the api policy info
     * @param searchDate the search date
     * @return boolean
     */
    public static boolean policyMatchSaleDate(PolicyInfo apiPolicyInfo, LocalDate searchDate) {
        //销售日期，为空表示不限制
        if(apiPolicyInfo.getSaleDateStart() == null && apiPolicyInfo.getSaleDateEnd() == null){
            return true;
        }else {
            boolean startDateValidate = searchDate.equals(apiPolicyInfo.getSaleDateStart()) || searchDate.isAfter(apiPolicyInfo.getSaleDateStart());
            boolean endDateValidate = searchDate.equals(apiPolicyInfo.getSaleDateEnd()) || searchDate.isBefore(apiPolicyInfo.getSaleDateEnd());

            if(startDateValidate && endDateValidate){
                return true ;
            }else {
                return false;
            }
        }
    }

    /*
    * 政策中GDS类型的匹配
    * */
    public static boolean policyBookGdsChannel(PolicyInfo apiPolicyInfo,SibeRouting routing){
        if(StringUtils.isBlank(apiPolicyInfo.getBookGdsChannel())){
                return true;
        }else {
            if(StringUtils.isBlank(routing.getReservationType())){
                return false;
            }
            if(SibeUtil.contains(apiPolicyInfo.getBookGdsChannel(),routing.getReservationType(),"/")){
                return true;
            }
        }
        return false;
    }
    /*
    * 政策中的office号
    * */
   /* public static boolean policyBookOfficeNo(ApiPolicyInfoRedis apiPolicyInfo,SibeRouting routing){
        if(StringUtils.isBlank(apiPolicyInfo.getBookOfficeNo())){
            return true;
        }else {
            if(StringUtils.isBlank(routing.getOfficeId())){
                return false;
            }
            if(SibeUtil.contains(apiPolicyInfo.getBookOfficeNo(),routing.getOfficeId(),"/")){
                return true;
            }
        }
        return false;
    }*/


    /**
     * 政策旅行日期匹配，routing与政策匹配返回true，否则返回false
     * @param startDate the start date
     * @param endDate the end date
     * @param segmentList the segment list
     * @return boolean
     */
    public static boolean policyMatchTravelDate(LocalDate startDate, LocalDate endDate , List<SibeSegment> segmentList) {
        //去程旅行日期，开始和结束
        //如果行程不存在，则无需进行旅行日期的匹配，认为匹配成功
        if(segmentList == null || segmentList.size() == 0){
            return true;
        }
        //为空表示不限制，直接进行下一个条件的判断
        if(startDate == null && endDate == null){
            return true ;
        }else {

            boolean dateValidateSign = validateDateCondition(segmentList, startDate, endDate);
            if(dateValidateSign){
                return true ;
            }else {
                return false;
            }
        }
    }

    /**
     * 除外联运航司匹配
     * routing与政策匹配返回true，不匹配返回false
     * @param apiPolicyInfo the api policy info
     * @param routing the routing
     * @return boolean
     */
    public static boolean policyMatchInterlineAirlineExcept(PolicyInfo apiPolicyInfo, SibeRouting routing) {
        //政策 混合航司情况为是或者不限 才进行除外联运航司的校验
        if(apiPolicyInfo.getPermitInterline() == PolicyConstans.PERMIT_TYPE_CONTAIN
            || apiPolicyInfo.getPermitInterline() == PolicyConstans.PERMIT_TYPE_UNLIMITED){
            //如果政策中的除外混合航司为空，则为不限，代表匹配上该政策
            if(StringUtils.isBlank(apiPolicyInfo.getInterlineAirlineExcept())){
                return true;
            }else {
                //如果政策为 除外混合航司：是
                //政策中的除外混合航司不为空，则需要判断行程中的混合航司是否在其中，在其中则为不满足该政策
                if(apiPolicyInfo.getPermitInterline() == PolicyConstans.PERMIT_TYPE_CONTAIN){
                    //行程中的联运航司不为空，则获取行程中的联运航司一一匹配政策中的联运航司，如果有一个航司不匹配，则该政策不匹配
                    if(routing.getInterlineAirline() != null && routing.getInterlineAirline().size() > 0){
                        for(String interlineAirline : routing.getInterlineAirline()){
                            if(apiPolicyInfo.getInterlineAirlineExcept().contains(interlineAirline)){
                                return false;
                            }
                        }

                        return true;
                    }else {
                        //行程中的联运航司为空，则直接不符合
                        return false;
                    }
                    //如果政策为混合航司：不限
                }else if(apiPolicyInfo.getPermitInterline() == PolicyConstans.PERMIT_TYPE_UNLIMITED){
                    //如果该行程为混合航司行程，则需要进行除外混合航司的匹配，否则视为匹配直接返回true
                    if(routing.getInterlineSign().intValue() == PolicyConstans.PERMIT_TYPE_CONTAIN){
                        for(String interlineAirline : routing.getInterlineAirline()){
                            if(apiPolicyInfo.getInterlineAirlineExcept().contains(interlineAirline)){
                                return false;
                            }
                        }

                        return true;
                    }else {
                        return true;
                    }
                }
            }
        }else {
            //混合航司：否 不需要比较该条件，直接返回true
            return true ;
        }

        return false;
    }

    /**
     * 适用混合航司匹配
     * 政策混合类型为是 和 不限时，则进行匹配；否则不匹配该条件返回true
     * 政策中的适用混合航司为空，则表示适用联运航司不限，匹配成功返回true
     * 政策中的适用混合航司不为空，则比较routing中的适用混合航司是否都存在于政策的适用混合航司中
     * @param apiPolicyInfo the api policy info
     * @param routing the routing
     * @return boolean
     */
    public static boolean policyMatchInterlineAirline(PolicyInfo apiPolicyInfo, SibeRouting routing) {
        //政策 混合航司情况为是或者不限 才进行联运航司的校验
        if(apiPolicyInfo.getPermitInterline() == PolicyConstans.PERMIT_TYPE_CONTAIN
            || apiPolicyInfo.getPermitInterline() == PolicyConstans.PERMIT_TYPE_UNLIMITED){
            //如果政策中的混合航司为空，则为不限，代表匹配上该政策
            if(StringUtils.isBlank(apiPolicyInfo.getInterlineAirline())){
                return true;
            }else {
                //如果政策为 混合航司：是
                //政策中的混合航司不为空，则需要判断行程中的混合航司是否吻合
                if(apiPolicyInfo.getPermitInterline() == PolicyConstans.PERMIT_TYPE_CONTAIN){
                    //行程中的联运航司不为空，则获取行程中的联运航司一一匹配政策中的联运航司，如果有一个航司不匹配，则该政策不匹配
                    if(routing.getInterlineAirline() != null && routing.getInterlineAirline().size() > 0){
                        for(String interlineAirline : routing.getInterlineAirline()){
                            if(! apiPolicyInfo.getInterlineAirline().contains(interlineAirline)){
                                return false;
                            }
                        }

                        return true;
                    }else {
                        //行程中的联运航司为空，则直接不符合
                        return false;
                    }
                    //如果政策为混合航司：不限
                }else if(apiPolicyInfo.getPermitInterline() == PolicyConstans.PERMIT_TYPE_UNLIMITED){
                    //如果该行程为混合航司行程，则需要进行适用混合航司的匹配，否则视为匹配直接返回true
                    if(routing.getInterlineSign().intValue() == PolicyConstans.PERMIT_TYPE_CONTAIN){
                        for(String interlineAirline : routing.getInterlineAirline()){
                            if(! apiPolicyInfo.getInterlineAirline().contains(interlineAirline)){
                                return false;
                            }
                        }

                        return true;
                    }else {
                        return true;
                    }
                }
            }
        }else {
            //混合航司：否 不需要比较该条件，直接返回true
            return true ;
        }

        return false;
    }

    /**
     * 对政策中 是否联运 进行匹配。
     * 政策中 是否联运 为不限 或者 政策中的是否联运与routing中的联运类型一致则返回true，否则返回false
     * @param apiPolicyInfo the api policy info
     * @param routing the routing
     * @return boolean
     */
    public static boolean policyMatchInterlineType(PolicyInfo apiPolicyInfo, SibeRouting routing) {
        if(apiPolicyInfo.getPermitInterline() == PolicyConstans.POLICY_YES_NO_UNLIMITED_UNLIMITED
            || routing.getInterlineSign().intValue() == apiPolicyInfo.getPermitInterline()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 对政策中 中转类型 进行匹配。
     * 政策中 中转类型 为不限 或者 政策中的中转类型与routing中的中转类型一致则返回true，否则返回false
     * @param apiPolicyInfo the api policy info
     * @param routing the routing
     * @return boolean
     */
    public static boolean policyMatchTransferType(PolicyInfo apiPolicyInfo, SibeRouting routing) {
        if(apiPolicyInfo.getPermitTransit() == PolicyConstans.POLICY_YES_NO_UNLIMITED_UNLIMITED
            || routing.getTransitSign().intValue() == apiPolicyInfo.getPermitTransit()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 对政策中 价格类型 进行匹配。
     * 政策中行程类型为不限 或者 政策中的价格类型与routing中的价格类型一致则返回true，否则返回false
     * @param apiPolicyInfo the api policy info
     * @param routing the routing
     * @return boolean
     */
    public static boolean policyMatchPriceType(PolicyInfo apiPolicyInfo, SibeRouting routing) {
        if(apiPolicyInfo.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL) || apiPolicyInfo.getPriceType().equals(routing.getFareType())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 对政策中 适用舱位 进行匹配。
     * 政策中行程类型为不限 或者 政策中的行程类型与routing中的行程类型一致则返回true，否则返回false
     * @param apiPolicyInfo the api policy info
     * @param tripTpe the trip tpe
     * @param routing the routing
     * @return boolean
     */
    public static boolean policyMatchSeatCabin(PolicyInfo apiPolicyInfo, String tripTpe, SibeRouting routing) {
        if(tripTpe.equals(PolicyConstans.POLICY_TRIP_TYPE_ONE_WAY)){
            boolean fromMasterSegmentResult = seatCabinOrseatGrateValidate(PolicyConstans.MASTER_SEGMENT_VALIDATE_CABIN, routing.getFromSegments(), apiPolicyInfo.getSeatCabin());
            return fromMasterSegmentResult;

        }else if(tripTpe.equals(PolicyConstans.POLICY_TRIP_TYPE_ROUND_WAY)){
            boolean fromMasterSegmentResult = seatCabinOrseatGrateValidate(PolicyConstans.MASTER_SEGMENT_VALIDATE_CABIN, routing.getFromSegments(), apiPolicyInfo.getSeatCabin());
            boolean retMasterSegmentResult = seatCabinOrseatGrateValidate(PolicyConstans.MASTER_SEGMENT_VALIDATE_CABIN, routing.getRetSegments(), apiPolicyInfo.getSeatCabin());

            return (fromMasterSegmentResult && retMasterSegmentResult);
        }

        return false;
    }

    /**
     * 对政策中 行程类型 进行匹配。 政策中行程类型为不限 或者 政策中的行程类型与routing中的行程类型一致则返回true，否则返回false
     * @param apiPolicyInfo the api policy info
     * @param tripType the trip type
     * @return boolean
     */
    public static boolean policyMatchTripDate(PolicyInfo apiPolicyInfo, String tripType) {

        if(apiPolicyInfo.getTripType().equals(PolicyConstans.POLICY_TRIP_TYPE_ALL)){
            return true;
        }
        else if(apiPolicyInfo.getTripType().equals(tripType)){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param segmentList
     * @param dateStart
     * @return
     */
    /**
     * 校验该航程的第一个航段的起飞时间是否在指定区间中(depTime >= startDate && depTime<=endDate)，在返回true，否则返回false
     * @param segmentList 一个行程列表
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return boolean
     */
    public static boolean validateDateCondition(List<SibeSegment> segmentList, LocalDate startDate, LocalDate endDate){
        Collections.sort(segmentList, new SegmentComparator());
        SibeSegment firstSegment = segmentList.get(0);
        String depTimeString = firstSegment.getDepTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDate depTime = LocalDate.parse(depTimeString, formatter);

        boolean startDateValidate = depTime.equals(startDate) || depTime.isAfter(startDate);
        boolean endDateValidate = depTime.equals(endDate) || depTime.isBefore(endDate);

        return startDateValidate && endDateValidate;
    }

    /**
     * 适用共享航司匹配
     * 政策共享类型为包含 和 不限时，则进行匹配；否则不匹配该条件返回true
     * 政策中的适用共享航司为空，则表示适用共享航司不限，匹配成功返回true
     * 政策中的适用共享航司不为空，则比较routing中的适用共享航司是否都存在于政策的适用共享航司中
     * @param apiPolicyInfo the api policy info
     * @param routing the routing
     * @return boolean
     */
    public static boolean policyCodeShareAirline(PolicyInfo apiPolicyInfo, SibeRouting routing) {
        //政策 混合航司情况为是或者不限 才进行联运航司的校验
        if(apiPolicyInfo.getPermitCodeShare() == PolicyConstans.PERMIT_TYPE_CONTAIN
            || apiPolicyInfo.getPermitCodeShare() == PolicyConstans.PERMIT_TYPE_UNLIMITED){
            //如果政策中的混合航司为空，则为不限，代表匹配上该政策
            if(StringUtils.isBlank(apiPolicyInfo.getCodeShareAirline())){
                return true;
            }else {
                //如果政策为 混合航司：是
                //政策中的混合航司不为空，则需要判断行程中的混合航司是否吻合
                if(apiPolicyInfo.getPermitCodeShare() == PolicyConstans.PERMIT_TYPE_CONTAIN){
                    //行程中的联运航司不为空，则获取行程中的联运航司一一匹配政策中的联运航司，如果有一个航司不匹配，则该政策不匹配
                    if(routing.getPermitCodeShareAirline() != null && routing.getPermitCodeShareAirline().size() > 0){
                        for(String codeShareAirline : routing.getPermitCodeShareAirline()){
                            if(StringUtils.isNotBlank(codeShareAirline)) {
                                if (!apiPolicyInfo.getCodeShareAirline().contains(codeShareAirline)) {
                                    return false;
                                }
                            }else {
                                return false;
                            }
                        }

                        return true;
                    }else {
                        //行程中的联运航司为空，则直接不符合
                        return false;
                    }
                    //如果政策为混合航司：不限
                }else if(apiPolicyInfo.getPermitCodeShare() == PolicyConstans.PERMIT_TYPE_UNLIMITED){
                    //如果该行程为混合航司行程，则需要进行适用混合航司的匹配，否则视为匹配直接返回true
                    if(routing.getCodeShareFlag().intValue() == PolicyConstans.PERMIT_TYPE_CONTAIN){
                        for(String codeShareAirline : routing.getPermitCodeShareAirline()){
                            if(StringUtils.isNotBlank(codeShareAirline)) {
                                if (!apiPolicyInfo.getCodeShareAirline().contains(codeShareAirline)) {
                                    return false;
                                }
                            }else {
                                return false;
                            }
                        }

                        return true;
                    }else {
                        return true;
                    }
                }
            }
        }else {
            //混合航司：否 不需要比较该条件，直接返回true
            return true ;
        }

        return false;
    }


    /**
     * 除外共享航司匹配
     * routing与政策匹配返回true，不匹配返回false
     * @param apiPolicyInfo the api policy info
     * @param routing the routing
     * @return boolean
     */
    public static boolean policyCodeShareAirlineExcept(PolicyInfo apiPolicyInfo, SibeRouting routing) {
        //政策 混合航司情况为是或者不限 才进行除外联运航司的校验
        if(apiPolicyInfo.getPermitCodeShare() == PolicyConstans.PERMIT_TYPE_CONTAIN
            || apiPolicyInfo.getPermitCodeShare() == PolicyConstans.PERMIT_TYPE_UNLIMITED){
            //如果政策中的除外混合航司为空，则为不限，代表匹配上该政策
            if(StringUtils.isBlank(apiPolicyInfo.getCodeShareAirlineExcept())){
                return true;
            }else {
                //如果政策为 除外混合航司：是
                //政策中的除外混合航司不为空，则需要判断行程中的混合航司是否在其中，在其中则为不满足该政策
                if(apiPolicyInfo.getPermitCodeShare() == PolicyConstans.PERMIT_TYPE_CONTAIN){
                    //行程中的联运航司不为空，则获取行程中的联运航司一一匹配政策中的联运航司，如果有一个航司不匹配，则该政策不匹配
                    if(routing.getPermitCodeShareAirline() != null && routing.getPermitCodeShareAirline().size() > 0){
                        for(String codeShareAirline : routing.getPermitCodeShareAirline()){
                            if(StringUtils.isNotBlank(codeShareAirline)) {
                                if (apiPolicyInfo.getCodeShareAirlineExcept().contains(codeShareAirline)) {
                                    return false;
                                }
                            }else {
                                return false;
                            }
                        }

                        return true;
                    }else {
                        //行程中的联运航司为空，则直接不符合
                        return false;
                    }
                    //如果政策为混合航司：不限
                }else if(apiPolicyInfo.getPermitCodeShare() == PolicyConstans.PERMIT_TYPE_UNLIMITED){
                    //如果该行程为混合航司行程，则需要进行除外混合航司的匹配，否则视为匹配直接返回true
                    if(routing.getCodeShareFlag().intValue() == PolicyConstans.PERMIT_TYPE_CONTAIN){
                        for(String codeShareAirline : routing.getPermitCodeShareAirline()){
                            if(StringUtils.isNotBlank(codeShareAirline)) {
                                if (apiPolicyInfo.getCodeShareAirlineExcept().contains(codeShareAirline)) {
                                    return false;
                                }
                            }else {
                                return false;
                            }
                        }

                        return true;
                    }else {
                        return true;
                    }
                }
            }
        }else {
            //混合航司：否 不需要比较该条件，直接返回true
            return true ;
        }

        return false;
    }

//
//    /**
//     * 判断所有航段是否为共享，当全部航段为共享，则返回true；否则返回false
//     * @param tripType the trip type
//     * @param routing the routing
//     * @return boolean
//     */
//    public static boolean isAllSegmentCodeShare(String tripType, Routing routing) {
//        boolean isCodeShare = true;
//        if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ONE_WAY)){
//            for(Segment segment : routing.getFromSegments()) {
//                if(isCodeShare == true){
//                    isCodeShare = segment.getCodeShare();
//                }
//
//            }
//        }else if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ROUND_WAY)){
//            for(Segment segment : routing.getFromSegments()) {
//                if(isCodeShare == true){
//                    isCodeShare = segment.getCodeShare();
//                }
//            }
//
//            for(Segment segment : routing.getRetSegments()) {
//                if(isCodeShare == true){
//                    isCodeShare = segment.getCodeShare();
//                }
//
//            }
//        }
//
//        return isCodeShare;
//    }
//
//    /**
//     * 判断所有航段是否都为非共享，全部为非共享返回true，否则有任意一段共享则返回false
//     * @param tripType the trip type
//     * @param routing the routing
//     * @return boolean
//     */
//    public static boolean isAllSegmentUnCodeShare(String tripType, Routing routing) {
//        boolean isCodeShare = false;
//        if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ONE_WAY)){
//            for(Segment segment : routing.getFromSegments()) {
//                if(isCodeShare == false){
//                    isCodeShare = segment.getCodeShare();
//                }
//
//            }
//        }else if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ROUND_WAY)){
//            for(Segment segment : routing.getFromSegments()) {
//                if(isCodeShare == false){
//                    isCodeShare = segment.getCodeShare();
//                }
//            }
//
//            for(Segment segment : routing.getRetSegments()) {
//                if(isCodeShare == false){
//                    isCodeShare = segment.getCodeShare();
//                }
//
//            }
//        }
//
//        return !isCodeShare;
//    }

//    /**
//     * 校验所有航程主航段舱等 舱位是否符合政策
//     * @param master_segment_validate_type 主航段校验类型 舱位/舱等
//     * @param segmentList the segment list
//     * @param apiPolicyInfo the api policy info
//     * @return 校验通过true ，校验失败false
//     */
//    public static boolean masterSegmentValidate(int master_segment_validate_type, List<Segment> segmentList, ApiPolicyInfoRedis apiPolicyInfo) {
//        if(master_segment_validate_type == PolicyConstans.MASTER_SEGMENT_VALIDATE_CABIN){
//            //如果政策中的舱位列表为空，则表示不限
//            if(StringUtils.isBlank(apiPolicyInfo.getSeatCabin())){
//                return true;
//            }else {
//                //如果政策中的舱位列表不为空，则需要找到行程中的主航段，对主航段的舱位进行校验
//                for(Segment segment : segmentList){
//                    if(segment.getMasterSegmentSign() != null && segment.getMasterSegmentSign() == PolicyConstans.SEGMENT_TYPE_MASTER){
//                        if(apiPolicyInfo.getSeatCabin().contains(segment.getCabin())){
//                            return true;
//                        }
//                    }
//                }
//            }
//
//            return false;
//        }else if(master_segment_validate_type == PolicyConstans.MASTER_SEGMENT_VALIDATE_GRADE){
//            //如果政策中的舱等列表为空，则表示不限
//            if(StringUtils.isBlank(apiPolicyInfo.getSeatGrade())){
//                return true;
//            }else {
//                //如果政策中的舱等列表不为空，则需要找到行程中的主航段，对主航段的舱等进行校验
//                for(Segment segment : segmentList){
//                    if(segment.getMasterSegmentSign() != null && segment.getMasterSegmentSign() == PolicyConstans.SEGMENT_TYPE_MASTER){
//                        if(apiPolicyInfo.getSeatGrade().contains(segment.getCabinGrade())){
//                            return true;
//                        }
//                    }
//                }
//            }
//
//            return false;
//        }
//
//        return false;
//    }

    /**
     * 对一个行程中的所有航段进行舱位或者舱等的校验
     * @param cabinOtGrateType the cabin ot grate type
     * @param segmentList the segment list
     * @param policyCabinOrGrate 舱位或者舱等列表
     * @return boolean
     */
    public static boolean seatCabinOrseatGrateValidate(int cabinOtGrateType, List<SibeSegment> segmentList, String policyCabinOrGrate){
        //如果政策中的舱位列表或者舱等为空，则表示不限
        if(StringUtils.isBlank(policyCabinOrGrate)){
            return true;
        }else {
            //如果政策中的舱位列表/舱等列表不为空，则需要对每个航段进行舱位/舱等的校验，如果有任意航段不符合，则直接返回不匹配
            if(cabinOtGrateType == PolicyConstans.MASTER_SEGMENT_VALIDATE_CABIN){
                for(SibeSegment segment : segmentList){
                    if(StringUtils.isNotEmpty(segment.getCabin()) && ! policyCabinOrGrate.contains(segment.getCabin())){
                        return false;
                    }
                }
            }else if(cabinOtGrateType == PolicyConstans.MASTER_SEGMENT_VALIDATE_GRADE){
                for(SibeSegment segment : segmentList){
                    if(StringUtils.isNotEmpty(segment.getCabinGrade()) && ! policyCabinOrGrate.contains(segment.getCabinGrade())){
                        return false;
                    }
                }
            }

            return true;
        }
    }


    /**
     * 对航程中的所有航段的航班号进行校验，所有航段符合政策中的可售航班则返回true，否则返回false
     * @param apiPolicyInfo
     * @param routing
     * @param tripType
     * @return
     */
    public static boolean policyMatchFlightAvailable(PolicyInfo apiPolicyInfo, SibeRouting routing, String tripType) {
        //政策中可售航班为空表示不限制，则进行下一个条件的判断
        if(StringUtils.isBlank(apiPolicyInfo.getAvailableFlight())){
            return true;
        }else{
            List<FlightRules> flightRulesList = new ArrayList<>();
            Map<String,String> availableFlightMap = new HashMap<>();
            generateFlightInfo(apiPolicyInfo.getAvailableFlight(), availableFlightMap, flightRulesList);

            if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ONE_WAY)){
                return availableFlightValidate(routing.getFromSegments(), flightRulesList, availableFlightMap);

            }else if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ROUND_WAY)){
                boolean fromSegmentValidateSign = availableFlightValidate(routing.getFromSegments(), flightRulesList, availableFlightMap);
                boolean retSegmentValidateSign = availableFlightValidate(routing.getRetSegments(), flightRulesList, availableFlightMap);
                if(fromSegmentValidateSign && retSegmentValidateSign){
                    return true;
                }else {
                    return false;
                }
            }
        }

        return false;
    }


    /**
     * 对每个航段进行可售航班的校验
     * @param segmentList
     * @param flightRulesList
     * @param flightMap
     * @return
     */
    private static boolean availableFlightValidate(List<SibeSegment> segmentList,List<FlightRules> flightRulesList, Map<String,String> flightMap) {
        segmentPoint:for (SibeSegment segment : segmentList) {

            //先进行单个可售航班的校验，如果单个可售航班中没有，则需要在可售航班范围中判断
            if(flightMap.get(segment.getFlightNumber()) == null){
                Integer flightNumberInteger = Integer.valueOf(segment.getFlightNumber());
                //若该航段的航班在任意一个可售航班范围中，则跳转至下一个航段的校验
                for (FlightRules flightRules : flightRulesList) {
                    if ((flightNumberInteger >= flightRules.getStartFlight() && flightNumberInteger <= flightRules.getEndFlight())) {
                        continue segmentPoint;
                    }
                }

                //如果航段的航班进行了所有可售航班范围的校验，但是未跳转至下一个航段，则代表其不处于任何一个可售航班范围中，此时无法匹配该规则，返回false
                return false;
            }
        }

        //经过所有校验，但是没有返回false，则代表所有航段符合可售航班，则可以匹配该政策，返回true
        return true;
    }

    /**
     * 对航程中的所有航段的航班号进行校验，任意一个航段在禁售航班内，则返回false，否则返回true
     * @param apiPolicyInfo
     * @param routing
     * @param tripType
     * @return
     */
    public static boolean policyMatchFlightProhibited(PolicyInfo apiPolicyInfo, SibeRouting routing, String tripType) {
        //政策中禁售航班为空表示不限制，则进行下一个条件的判断
        if(StringUtils.isBlank(apiPolicyInfo.getProhibitedFlight())){
            return true;
        }else{
            Map<String,String> prohibitedFlightMap = new HashMap<>();
            List<FlightRules> flightRulesList = new ArrayList<>();
            generateFlightInfo(apiPolicyInfo.getProhibitedFlight(), prohibitedFlightMap, flightRulesList);

            if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ONE_WAY)){
                return prohibitedFlightValidate(routing.getFromSegments(), flightRulesList, prohibitedFlightMap);

            }else if(tripType.equals(PolicyConstans.POLICY_TRIP_TYPE_ROUND_WAY)){
                boolean fromSegmentValidateSign = prohibitedFlightValidate(routing.getFromSegments(), flightRulesList, prohibitedFlightMap);
                boolean retSegmentValidateSign = prohibitedFlightValidate(routing.getRetSegments(), flightRulesList, prohibitedFlightMap);
                if(fromSegmentValidateSign && retSegmentValidateSign){
                    return true;
                }else {
                    return false;
                }
            }
        }

        return false;

    }

    /**
     * 根据输入的可售或者禁售航班信息，得到可售/禁售航班map，或者可售/禁售航班范围List
     * @param flightInfo
     * @param flightMap
     * @param flightRulesList
     */
    private static void generateFlightInfo(String flightInfo, Map<String, String> flightMap, List<FlightRules> flightRulesList) {
        String[] flightArray = flightInfo.split(",");

        Arrays.stream(flightArray).forEach(item->{
            if(item.contains("-")){
                String[] itemArray = item.split("-");
                FlightRules flightRules = new FlightRules();
                flightRules.setStartFlight(Integer.valueOf(itemArray[0]));
                flightRules.setEndFlight(Integer.valueOf(itemArray[1]));
                flightRulesList.add(flightRules);
            }else {
                flightMap.put(item, item);
            }
        });

    }

    /**
     * 对每个航段进行禁售航班校验
     * @param segmentList
     * @param flightRulesList
     * @param prohibitedFlightMap
     * @return
     */
    private static boolean prohibitedFlightValidate(List<SibeSegment> segmentList, List<FlightRules> flightRulesList, Map<String,String> prohibitedFlightMap) {

        for (SibeSegment segment : segmentList) {
            //判断是否在单个禁售航班中，如果在单个禁售航班中则马上返回fasle，否则需要判断是否在禁售航班范围中
            if(prohibitedFlightMap.get(segment.getFlightNumber()) == null){
                Integer flightNumberInteger = Integer.valueOf(segment.getFlightNumber());
                for (FlightRules flightRules : flightRulesList) {
                    if(flightNumberInteger >= flightRules.getStartFlight() && flightNumberInteger <= flightRules.getEndFlight()){
                        return false;
                    }
                }
            }else {
                return false;
            }

        }

        return true;
    }

    /**
     * 提前销售天数校验，满足则返回true，不满足则返回false
     * @param apiPolicyInfo
     * @param routing
     * @return
     */
    public static boolean policyMatchAdvanceSaleDay(PolicyInfo apiPolicyInfo, SibeRouting routing) {
        //如果提前销售天数为空，则代表不限制，直接返回满足该条件true
        if(apiPolicyInfo.getAdvanceSaleDay() == null){
            return true;
        }else {
            //获取去程第一段的起飞时间，增加上政策的提前销售天数，与当前时间进行对比
            String depTimeString = routing.getFromSegments().get(0).getDepTime();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            LocalDate depTime = LocalDate.parse(depTimeString, fmt);

            long advanceSaleDay = 0 - apiPolicyInfo.getAdvanceSaleDay();
            LocalDate depTimeDeadLine = depTime.plusDays(advanceSaleDay);

            LocalDate now = LocalDate.now();
            if(now.isBefore(depTimeDeadLine) || now.equals(depTimeDeadLine)){
                return true;
            }else {
                return false;
            }
        }
    }

    /***
     * 对航程中的第一段进行除外日期判断，如果处于除外日期，则返回false，否则返回true
     * @param travelDateExceptInfo
     * @param segmentList
     * @return
     */
    public static boolean policyMatchTravelDateExcept(String travelDateExceptInfo, List<SibeSegment> segmentList) {
        //对航段进行校验，航段为空则直接通过该规则，返回true
        if(segmentList == null || segmentList.size() == 0){
            return true;
        }

        //旅行除外日期为空则代表不限制，可以通过该规则的校验，返回true
        if(StringUtils.isBlank(travelDateExceptInfo)){
            return true;
        }else {
            List<TravelDateRange> travelDateRangeList = new ArrayList<>();
            generateTravelDateRangeList(travelDateExceptInfo, travelDateRangeList);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            LocalDate depDate = LocalDate.parse(segmentList.get(0).getDepTime(), fmt);
            for (TravelDateRange travelDateRange : travelDateRangeList) {
                if ( (depDate.isAfter(travelDateRange.getStartDate()) || depDate.equals(travelDateRange.getStartDate()))
                    && (depDate.isBefore(travelDateRange.getEndDate()) || depDate.equals(travelDateRange.getEndDate())) ) {
                    return false;
                }
            }
        }

        return true;
    }

    /*
    * 适用舱位除外
    * **/
    public static boolean policySeatCabinExcept(PolicyInfo apiPolicyInfo, SibeRouting routing){
        if(apiPolicyInfo.getSeatCabinExcept() == null){
            return true;
        }else {
            List<SibeSegment> segmentList=new ArrayList<>();
            segmentList.addAll(routing.getFromSegments());
            segmentList.addAll(routing.getRetSegments());
            for(SibeSegment sibeSegment:segmentList){
               if(SibeUtil.contains(apiPolicyInfo.getSeatCabinExcept(),sibeSegment.getCabin(),"/")){
                   return false;
               }
            }
        }
        return true;
    }

    /*
    * 最小和最大停留时间（分）
    * */
    public static boolean policyRetMinAndMaxTime(PolicyInfo apiPolicyInfo, SibeRouting routing,String tripType){
        if((apiPolicyInfo.getRetMinTime() == null && apiPolicyInfo.getRetMaxTime() == null) || "OW".equals(tripType)){
            return true;
        }else {
                if("RT".equals(tripType) && ("RT".equals(apiPolicyInfo.getTripType()) || "ALL".equals(apiPolicyInfo.getTripType()))){
                        DateTimeFormatter fmt =DateTimeFormatter.ofPattern("yyyyMMddHHmm");
                        LocalDateTime detDate = LocalDateTime.parse(routing.getFromSegments().get(0).getDepTime(),fmt);
                        LocalDateTime arrDate = LocalDateTime.parse(routing.getRetSegments().get(0).getDepTime(),fmt);
                        long durationMinutes = Duration.between(detDate,arrDate).toMinutes();
                        if(apiPolicyInfo.getRetMinTime() != null && apiPolicyInfo.getRetMaxTime() == null){
                            if(durationMinutes>=apiPolicyInfo.getRetMinTime()){
                                return true;
                            }
                        }else if(apiPolicyInfo.getRetMinTime() == null && apiPolicyInfo.getRetMaxTime() != null){
                            if(durationMinutes<=apiPolicyInfo.getRetMaxTime()){
                                return true;
                            }
                        }else if(apiPolicyInfo.getRetMinTime() != null && apiPolicyInfo.getRetMaxTime() != null){
                            if(durationMinutes>=apiPolicyInfo.getRetMinTime() && durationMinutes<=apiPolicyInfo.getRetMaxTime()){
                                return true;
                            }
                        }
                }
        }
        return false;
    }


    /**
     * 根据录入的除外日期字符串，生成除外日期对象List
     * @param travelDateInfo
     * @param travelDateRangeList
     */
    private static void generateTravelDateRangeList(String travelDateInfo, List<TravelDateRange> travelDateRangeList) {
        String[] travelDateArray = travelDateInfo.split("/");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Arrays.stream(travelDateArray).forEach(item->{
            LocalDate startDate;
            LocalDate endDate;
            if(item.contains(">")) {
                String[] itemArray = item.split(">");
                 startDate = LocalDate.parse(itemArray[0], fmt);
                 endDate = LocalDate.parse(itemArray[1], fmt);
            }else {
                 startDate = LocalDate.parse(item, fmt);
                 endDate = LocalDate.parse(item, fmt);
            }
            TravelDateRange travelDateRange = new TravelDateRange();
            travelDateRange.setStartDate(startDate);
            travelDateRange.setEndDate(endDate);
            travelDateRangeList.add(travelDateRange);
        });
    }


    /**
     * 班期流程
     * @param dayTime
     * @param segmentList
     * @return
     */
    public static boolean policyMatchDayTime(String dayTime, List<SibeSegment> segmentList) {
        //如果班期为空或者行程为空，则直接校验通过，返回true
        if(StringUtils.isBlank(dayTime) || segmentList == null || segmentList.size() ==0){
            return true;
        }else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            LocalDate depDate = LocalDate.parse(segmentList.get(0).getDepTime(), fmt);
            String depDateWeek = depDate.getDayOfWeek().getValue() + "";
            if(dayTime.contains(depDateWeek)){
                return true;
            }else {
                return false;
            }
        }
    }


}
