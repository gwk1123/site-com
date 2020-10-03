package com.sibecommon.ota.ctrip.transform;



import com.sibecommon.config.SibeProperties;
import com.sibecommon.ota.ctrip.model.*;
import com.sibecommon.ota.site.*;
import com.sibecommon.utils.constant.PolicyConstans;
import com.sibecommon.ota.ctrip.model.*;
import com.sibecommon.ota.site.*;
import org.apache.commons.lang3.StringUtils;
import com.sibecommon.ota.ctrip.model.*;
import com.sibecommon.ota.site.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangdehua on 18/3/31.
 */
public class TransformCtripCommon {

    /**
     * Convert routing to ota ctrip routing.
     *
     * @param <T>         the type parameter
     * @param sibeRouting the sibe routing
     * @param sibeRequest the sibe request
     * @return the ctrip routing
     */
    public static <T extends SibeBaseRequest> CtripRouting convertRoutingToOTA(SibeRouting sibeRouting, T sibeRequest, SibeProperties sibeProperties) {

        if (!(sibeRequest instanceof SibeSearchRequest)) {
            sibeRouting.setSibeRoutingData(sibeRequest.getRouting().getSibeRoutingData());
            sibeRouting.setRule(sibeRequest.getRouting().getSibeRoutingData().getSibeRule());
        }

        CtripRouting ctripRouting = new CtripRouting();

        //可保存必要信息，验价时会放在请求报文中传给供应商；最大 1000 个字符 。
        ctripRouting.setData(sibeRouting.getSibeRoutingData().getEncryptData());
        //成人单价，不含税
        ctripRouting.setAdultPrice(sibeRouting.getAdultPriceOTA());
        //成人税费【注意不能是0，若存在为0的情况，请与我们联系】
        ctripRouting.setAdultTax(sibeRouting.getAdultTaxOTA());
        //儿童公布价，不含税
        ctripRouting.setChildPrice(sibeRouting.getChildPriceOTA());
        //儿童税费 1）对于含儿童的查询，必须返回；2）不能是0，若存在为0的情况，请与我们联系。
        ctripRouting.setChildTax(sibeRouting.getChildTaxOTA());
        //婴儿单价，不含税【对于含婴儿的查询，必须返回】
        ctripRouting.setInfantPrice(sibeRouting.getInfantsPriceOTA());
        //婴儿税费1）对于含婴儿的查询，必须返回；2）可以为0。
        ctripRouting.setInfantTax(sibeRouting.getInfantsTaxOTA());
        //成人税费类型：0 未含税 / 1 已含税 【正常赋0，如赋1请提前告知】
        ctripRouting.setAdultTaxType(0);
        //儿童税费类型：0 未含税 / 1 已含税 【正常赋0，如赋1请提前告知】
        ctripRouting.setChildTaxType(0);
        //报价类型：0 普通价 / 1 留学生价 【请全部赋0】
        ctripRouting.setPriceType(0);
        //报价类型：0 预定价 / 1 申请价 【请全部赋0】
        ctripRouting.setApplyType(0);

        //适用年龄区间【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
        // 1）使用“-”表示“至”，例如*-12，表示12岁及以下；
        // 2）置空表示无限制
        ctripRouting.setAdultAgeRestriction("");
        /*【公布运价强校验】
         *  1）旅客资质，标准三字码：
         *  	NOR：普通成人
         *  	LAB：劳务人员
         *  	SEA：海员
         *  	SNR：老年人
         *  	STU：学生
         *  	YOU：青年
         *  2）如果投放非NOR的政策，请提前告知我们。
         */
        ctripRouting.setEligibility("NOR");

        /*
         * 允许国籍，使用标准国家二字码
         * 【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
         *
         * 1）置空表示无限制（一般都是置空的）；
         * 2）若多个使用“/”分割；
         * 3）与forbiddenNationality只能2选1，若同时出现，为非法数据，将被过滤。
        */
        ctripRouting.setNationality("");

        /*禁用国籍，使用标准国家二字码
         * 【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
         * 1）置空表示无限制（一般都是置空的）；
         * 2）若多个使用“/”分割；
         * 3）与nationality只能2选1，若同时出现，为非法数据，将被过滤。
         */
        //ctripRouting.setForbiddenNationality("");

        //【公布运价强校验】产品类型：0 旅行套餐 /1 商务优选/2 特惠推荐 新上线供应商请赋值为0
        //ctripRouting.setPlanCategory(0);

        //报销凭证：T 行程单 / F 发票 / E 电子发票 默认发票；廉航票台可赋值为E

        /**
         * 20180726 袁健需求 一部全局指定传 电子发票，二部全局指定传 旅游发票
         * 20180903 袁健需求 一部全局指定传 电子发票
         */
        if("CT001".equals(sibeRequest.getSite())){
            ctripRouting.setInvoiceType("E");
        }else if ("CT002".equals(sibeRequest.getSite())){
            ctripRouting.setInvoiceType("F");
        }else{
            ctripRouting.setInvoiceType(convertInvoiceTypeToOTA(sibeRouting.getInvoiceType()));
        }

        //最短停留时间【单位是天】【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
        //ctripRouting.setMinStay();
        //最长停留时间【单位是天】【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
        //ctripRouting.setMaxStay();
        //最小出行人数【无返回，默认为1】
        ctripRouting.setMinPassengerCount(1);

        //最大出行人数【无返回，默认为9】
        ctripRouting.setMaxPassengerCount(sibeRouting.getMaxPassengerCount());

        //订位Office号【可为空】
        ctripRouting.setBookingOfficeNo("");

        //出票Office号【可为空】
        ctripRouting.setTicketingOfficeNo("");

        //【公布运价强校验】出票航司
        // 1）整个行程只能赋一个航司；
        // 2）如不赋值会取行程第一航段的carrier作为出票航司；
        // 3）此字段非常重要，请务必准确赋值。
        ctripRouting.setValidatingCarrier(sibeRouting.getValidatingCarrier());

        /*
         * 【公布运价强校验】政策来源
         * 1）非公布运价此字段可不赋值；
         * 2）公布运价此字段必须按要求返回，否则产品将按照未知订座系统，输出到旅行套餐；
         * 3）使用IATA标准2字代码
         * 	1E：TravelSky
         * 	1A：Amadeus
         * 	1B：Abacus
         * 	1S：Sabre
         * 	1P：WorldSpan
         * 	1G：Galileo
         * OT：未知订座系统来源
         *
         */
        ctripRouting.setReservationType(sibeRouting.getReservationType());

        //【公布运价强校验】运价类型
        // 1）公布运价请赋值为：PUB：公布运价；
        // 2）非公布运价此字段置空即可；
        // 3）公布运价此字段必须按要求返回，否则产品将按照其他产品属性，输出到旅行套餐。
        ctripRouting.setProductType(sibeRouting.getFareType());
        //【公布运价强校验】每航段1个，使用“ ; ”分割
        ctripRouting.setFareBasis(sibeRouting.getFareBasis());

        /*

        todo 增值服务信息
        //增值服务信息
        //行李增值服务（true 包含/false不包含，默认false）
        ctripRouting.getAirlineAncillaries().setBaggageService(true);

        //有无免费行李额（True为无免费行李额；默认False，包含免费行李额，或部分无免费行李额）
        ctripRouting.getAirlineAncillaries().setUnFreeBaggage(sibeRouting.get);

        //值机服务状态 0：不支持在线值机 / 1：支持在线值机 / 2：必须在线值机（此选项代表供应商打包值机价格到机票）
        ctripRouting.getAirlineAncillaries().setCheckInServiceStatus(1);
        */

        //去程航段按顺序 多程行程信息全部输出到此节点
        List<SibeSegment> fromSegmentList = sibeRouting.getFromSegments();
        ctripRouting.setFromSegments(convertSegmentListToOTA(fromSegmentList));
        //多程行程信息全部输出到此节点
        if (sibeRequest.getTripType().equals("RT")) {
            List<SibeSegment> retSegmentList = sibeRouting.getRetSegments();
            ctripRouting.setRetSegments(convertSegmentListToOTA(retSegmentList));
        }

        //免费行李额信息、退改签信息
        processRuleToOTA(ctripRouting, sibeRouting, sibeRequest,sibeProperties);

        return ctripRouting;
    }


    private static String convertInvoiceTypeToOTA(String policyInvoiceType) {
        //报销凭证：T 行程单 / F 发票 / E 电子发票 默认发票；廉航票台可赋值为E
        if (PolicyConstans.INVOICE_TYPE_FULL_TRAVEL_ITINERARY.equals(policyInvoiceType)) {
            //"1" 全额行程单
            return "T";
        } else if (PolicyConstans.INVOICE_TYPE_TRAVEL_INVOICE.equals(policyInvoiceType)) {
            //"2";旅游发票
            return "F";
        } else if (PolicyConstans.INVOICE_TYPE_ELECTRONIC_INVOICE.equals(policyInvoiceType)) {
            return "E";
            //3: 境外电子凭证
        } else {
            return "E";
        }
    }


    private static <T extends SibeBaseRequest> void processRuleToOTA(CtripRouting ctripRouting, SibeRouting sibeRouting, T sibeRequest,SibeProperties sibeProperties) {
        //【已弃用】行李额规定
        //【已弃用】服务费规定
        CtripRule ctripRule = new CtripRule();


        SibeRule sibeRule = sibeRouting.getSibeRoutingData().getSibeRule();


        //成人规则信息
        if (sibeRequest.getAdultNumber() != null && sibeRequest.getAdultNumber() > 0) {
            //格式化行李额规定
            processFormatBaggageInfoToOTA(ctripRule, sibeRule, 0,sibeRequest);
            //退票规定
            processRefundInfoToOTA(ctripRule, ctripRouting, sibeRule, 0,sibeRequest.getSite(),sibeRouting,sibeProperties);
            //改期规定
            processChangeInfoToOTA(ctripRule, ctripRouting, sibeRule, 0,sibeRequest.getSite(),sibeRouting,sibeProperties);
        }


        //儿童规则信息
        if (sibeRequest.getChildNumber() != null && sibeRequest.getChildNumber() >= 0) {
            //格式化行李额规定
            processFormatBaggageInfoToOTA(ctripRule, sibeRule, 1,sibeRequest);
            //退票规定
            processRefundInfoToOTA(ctripRule, ctripRouting,  sibeRule, 1,sibeRequest.getSite(),sibeRouting,sibeProperties);
            //改期规定
            processChangeInfoToOTA(ctripRule, ctripRouting,  sibeRule, 1,sibeRequest.getSite(),sibeRouting,sibeProperties);
        }

        //婴儿规则信息
        if (sibeRequest.getInfantNumber() != null && sibeRequest.getInfantNumber() >= 0) {
            //格式化行李额规定
            processFormatBaggageInfoToOTA(ctripRule, sibeRule, 2,sibeRequest);
            //退票规定
            processRefundInfoToOTA(ctripRule,  ctripRouting, sibeRule, 2,sibeRequest.getSite(),sibeRouting,sibeProperties);
            //改期规定
            processChangeInfoToOTA(ctripRule,  ctripRouting, sibeRule, 2,sibeRequest.getSite(),sibeRouting,sibeProperties);
        }


        //备注信息，最大 300 个字符
        ctripRule.setNote(sibeRequest.getOtherIssueMsg());
        //是否要使用携程退改签：（true 使用；false 不使用）
        ctripRule.setUseCtripRule(true);

        //公布运价相关参数，地理区间见运价集群编码 PUB PRV
        ctripRule.setTariffNo("PUB".equals(ctripRouting.getProductType())?"PUB":"PRI");

        //公布运价相关参数
        ctripRule.setRuleNo(null);
        //退改签匹配模式：（0准确匹配；1模糊匹配）
        ctripRule.setFareRuleMatchMode(null);

        ctripRouting.setRule(ctripRule);
    }

    private static <T extends SibeBaseRequest> void processRefundInfoToOTA(CtripRule ctripRule,CtripRouting ctripRouting,
                                                                           SibeRule sibeRule, int passengerType,String site,SibeRouting sibeRouting,
                                                                           SibeProperties sibeProperties) {


        //格式化行李额规定
        //退票规定
        sibeRule.getRefundInfoList()
            .stream()
            .filter(sibeRefund -> (sibeRefund.getPassengerType() == 0))
            .forEach(refund -> {

                CtripRefund ctripRefund = new CtripRefund();
                // 退票类型
                // 0：客票全部未使用；
                // 1：客票部分使用【即去程已使用，在往返行程中使用，代表回程的退票信息】
                // 【单程时0；往返时0、1都要有】
                ctripRefund.setRefundType(refund.getRefundType());
                /**
                 * 20180703 针对携程二部进行退改修改 redmine #1874
                 *  携程一部也需要对退改进行修改
                 *  携程一部和携程二部的私有运价与公布运价，退改费用修改为票面价的69%
                 */
                String  validatingCarrier = ( null == sibeRouting.getSibeRoutingData() || null == sibeRouting.getSibeRoutingData().getValidatingCarrier() ? sibeRouting.getValidatingCarrier(): sibeRouting.getSibeRoutingData().getValidatingCarrier());
                if("CT001".equals(site) || "CT002".equals(site)){
                    if(null == ctripRefund.getRefundType() || 1 == ctripRefund.getRefundType()){
                        ctripRefund.setRefundStatus("T");
                    }else if(StringUtils.isNotEmpty(sibeProperties.getOta().getCtripNonAirlineEndorse()) &&
                        sibeProperties.getOta().getCtripNonAirlineEndorse().indexOf(validatingCarrier) >= 0){
                        ctripRefund.setRefundStatus("H");
                        BigDecimal refundFee = new BigDecimal(Math.floor(ctripRouting.getAdultPrice()*sibeProperties.getOta().getCtripRefund()));
                        ctripRefund.setRefundFee(refundFee);
                    }else {
                        ctripRefund.setRefundStatus("T");
                    }
                }else {
                    //退票标识
                    // T：不可退
                    // H：有条件退
                    // F：免费退
                    // E：按航司客规【公布运价专用】  todo 不能使用 『E』
                    ctripRefund.setRefundStatus("E".equals(refund.getRefundStatus())?"T":refund.getRefundStatus());
                    //退票费
                    // 1）当refundStatus =H,必须赋值；
                    // 2）若refundStatus =T/F,此字段可不赋值。
                    if (refund.getRefundFee() == null) {
                        ctripRefund.setRefundFee(new BigDecimal(0));
                    } else {
                        if(refund.getRefundFeeMode() == 2) {
                            BigDecimal oneHundred = new BigDecimal(100);
                            BigDecimal discountForCalculation = refund.getRefundFee().divide(oneHundred);
                            ctripRefund.setRefundFee(new BigDecimal(sibeRouting.getAdultPriceGDS()).multiply(sibeRouting.getSibeRoutingData().getSibePolicy().getGdsToOTARate()).multiply(discountForCalculation));
                        }else {
                            ctripRefund.setRefundFee(refund.getRefundFee());
                        }
                    }
                    //中文退票备注
                    ctripRefund.setCnRefRemark(refund.getCnRefRemark());
                    //英文退票备注
                    ctripRefund.setEnRefRemark(refund.getEnRefRemark());
                }

                // 退票费币种 当refundStatus =H，必须赋值。
                ctripRefund.setCurrency("CNY");
                //乘客类型，0 成人/1 儿童/2 婴儿
                // 1）对于多乘客类型的查询、验价，必须按乘客类型返回；如成人+儿童的查询，成人和儿童的退改签都要有。
                ctripRefund.setPassengerType(passengerType);

                //是否允许NoShow退票 T：不可退； H：有条件退；F：免费退；E：按航司客规为准【公布运价专用】
                if("CT003".equals(site)){
                    ctripRefund.setRefNoshow("E".equals(refund.getRefNoshow())?"T":refund.getRefNoshow());
                    //退票时航班起飞前多久算NoShow，单位：小时 1）若无法确认此时间，请默认赋0。
                    ctripRefund.setRefNoShowCondition(null == refund.getRefNoShowCondition() ? 0 :refund.getRefNoShowCondition());
                    //NoShow退票费用 1）当IsRefNoshow =H，必须赋值；2）展示给客人的noshow退票费= refundFee+ refNoshowFee。
                    if(refund.getRefNoshowFee() == null){
                        ctripRefund.setRefNoshowFee(new BigDecimal(0));
                    }else {
                        ctripRefund.setRefNoshowFee(refund.getRefNoshowFee());
                    }
                }else {
                    ctripRefund.setRefNoshow("T");
                    //退票时航班起飞前多久算NoShow，单位：小时 1）若无法确认此时间，请默认赋0。
                    ctripRefund.setRefNoShowCondition(0);
                }


                ctripRule.getRefundInfoList().add(ctripRefund);
            });

    }

    private static <T extends SibeBaseRequest> void processChangeInfoToOTA(CtripRule ctripRule,CtripRouting ctripRouting,SibeRule sibeRule,
                                                                           int passengerType,String site,SibeRouting sibeRouting,
                                                                           SibeProperties sibeProperties) {
        //格式化行李额规定
        //退票规定
        sibeRule.getChangesInfoList()
            .stream()
            .filter(sibeRefund -> (sibeRefund.getPassengerType() == 0))
            .forEach(change -> {

                CtripChange ctripChange = new CtripChange();
                // 退票类型
                // 0：客票全部未使用；
                // 1：客票部分使用【即去程已使用，在往返行程中使用，代表回程的退票信息】
                // 【单程时0；往返时0、1都要有】
                ctripChange.setChangesType(change.getChangesType());

                /**
                 * 20180703 针对携程二部进行退改修改 redmine #1874
                 *  携程二部的私有运价，退改费用修改为票面价的69%
                 */
                String  validatingCarrier = ( null == sibeRouting.getSibeRoutingData() || null == sibeRouting.getSibeRoutingData().getValidatingCarrier() ? sibeRouting.getValidatingCarrier(): sibeRouting.getSibeRoutingData().getValidatingCarrier());
                if ("CT002".equals(site) || "CT001".equals(site)){

                    if(null == change.getChangesType() || 1 == change.getChangesType()){
                        ctripChange.setChangesStatus("T");
                    }else if(StringUtils.isNotEmpty(sibeProperties.getOta().getCtripNonAirlineEndorse()) &&
                        sibeProperties.getOta().getCtripNonAirlineEndorse().indexOf(validatingCarrier) >= 0){
                        ctripChange.setChangesStatus("T");
                    }else{
                        ctripChange.setChangesStatus("H");
                        BigDecimal changesFee = new BigDecimal(Math.floor(ctripRouting.getAdultPrice()*sibeProperties.getOta().getCtripEndorse()));
                        ctripChange.setChangesFee(changesFee);
                    }
                    ctripChange.setRevNoshow("T");

                }else {

                    //退票标识
                    // T：不可退
                    // H：有条件退
                    // F：免费退
                    // E：按航司客规【公布运价专用】 todo 携程不允许使用『E』
                    ctripChange.setChangesStatus("E".equals(change.getChangesStatus())?"T":change.getChangesStatus());
                    //退票费
                    // 1）当ChangeStatus =H,必须赋值；
                    // 2）若ChangeStatus =T/F,此字段可不赋值。
                    if(change.getChangesFee() == null){
                        ctripChange.setChangesFee(new BigDecimal(0));
                    }else {
                        if(change.getChangeFeeMode() == 2) {
                            BigDecimal oneHundred = new BigDecimal(100);
                            BigDecimal discountForCalculation = change.getChangesFee().divide(oneHundred);
                            ctripChange.setChangesFee(new BigDecimal(sibeRouting.getAdultPriceGDS()).multiply(sibeRouting.getSibeRoutingData().getSibePolicy().getGdsToOTARate()).multiply(discountForCalculation));
                        }else {
                            ctripChange.setChangesFee(change.getChangesFee());
                        }
                    }

                    //中文退票备注
                    ctripChange.setCnRevRemark(change.getCnRevRemark());
                    //英文退票备注
                    ctripChange.setEnRevRemark(change.getEnRevRemark());

                    //是否允许NoShow退票 T：不可退； H：有条件退；F：免费退；E：按航司客规为准【公布运价专用】 todo 携程不允许使用『E』
                    ctripChange.setRevNoshow("E".equals(change.getRevNoshow())?"T":change.getRevNoshow());
                }


                // 退票费币种 当ChangeStatus =H，必须赋值。
                ctripChange.setCurrency("CNY");
                //乘客类型，0 成人/1 儿童/2 婴儿
                // 1）对于多乘客类型的查询、验价，必须按乘客类型返回；如成人+儿童的查询，成人和儿童的退改签都要有。
                ctripChange.setPassengerType(passengerType);

                //退票时航班起飞前多久算NoShow，单位：小时 1）若无法确认此时间，请默认赋0。
                ctripChange.setRevNoShowCondition(null == change.getRevNoShowCondition() ? 0 :change.getRevNoShowCondition());
                //NoShow退票费用 1）当IsRefNoshow =H，必须赋值；2）展示给客人的noshow退票费= ChangeFee+ refNoshowFee。
                if(!"T".equals(ctripChange.getRevNoshow())) {
                    if (change.getRevNoshowFee() == null) {
                        ctripChange.setRevNoshowFee(new BigDecimal(0));
                    } else {
                        ctripChange.setRevNoshowFee(change.getRevNoshowFee());
                    }
                }


                ctripRule.getChangesInfoList().add(ctripChange);
            });
    }

    private static <T extends SibeBaseRequest> void processFormatBaggageInfoToOTA(CtripRule ctripRule, SibeRule sibeRule, int passengerType, T sibeRequest) {
        //格式化行李额规定
        sibeRule
            .getFormatBaggageInfoList()
            .stream()
            .filter(sibeRefund -> (sibeRefund.getPassengerType() == 0))
            .forEach(formatBaggage -> {

                CtripFormatBaggage ctripFormatBaggage = new CtripFormatBaggage();
                //航段序号，从1开始 1）注意是按航段赋值，而不是按去程/回程赋值
                ctripFormatBaggage.setSegmentNo(formatBaggage.getSegmentNo());
                //乘客类型，0 成人/1 儿童/2 婴儿 1）对于多乘客类型的查询、验价，必须按乘客类型返回；如成人+儿童的查询，成人和儿童的行李额都要有。
                ctripFormatBaggage.setPassengerType(passengerType);
                //行李额件数，单位PC，枚举值如下：
                // 0无免费托运行李，此时baggageWeight需赋值为-1；
                // -1表示计重制，对应的baggageWeight表示每人可携带的总重量(此时baggageWeight必须赋正值，否则过滤）；
                // >0表示计件制，对应的baggageWeight表示每件行李重量（若计件制时不知每件行李额的重量，baggageWeight必须赋值为-1）。
                ctripFormatBaggage.setBaggagePiece(formatBaggage.getBaggagePiece());
                //行李额重量，单位KG，必须赋值，跟BaggagePiece配合使用

                //针对非廉航行李额计件制，重量默认返回20KG/件 20180616，CT003是廉航票台
                if("CT003".equals(sibeRequest.getSite())){
                    if(formatBaggage.getBaggagePiece()==0) {
                        ctripFormatBaggage.setBaggageWeight(0);
                    }else{
                        ctripFormatBaggage.setBaggageWeight(formatBaggage.getBaggageWeight()>0?formatBaggage.getBaggageWeight():-1);
                    }

                }else {
                    if(formatBaggage.getBaggagePiece()==0){
                        ctripFormatBaggage.setBaggageWeight(0);
                    }else {
                        ctripFormatBaggage.setBaggageWeight(formatBaggage.getBaggageWeight()>0?formatBaggage.getBaggageWeight():20);
                    }
                }

                //中文行李额备注
                if(StringUtils.isNotEmpty(formatBaggage.getCnBaggage()) && !"null".equals(formatBaggage.getCnBaggage())) {
                    ctripFormatBaggage.setCnBaggage(formatBaggage.getCnBaggage());
                }
                //英文行李额备注
                if(StringUtils.isNotEmpty(formatBaggage.getEnBaggage()) && !"null".equals(formatBaggage.getEnBaggage())) {
                    ctripFormatBaggage.setEnBaggage(formatBaggage.getEnBaggage());
                }
                ctripRule.getFormatBaggageInfoList().add(ctripFormatBaggage);
            });
    }


    private static List<CtripSegment> convertSegmentListToOTA(List<SibeSegment> fromSegmentList) {
        List<CtripSegment> ctripSegmentList = new ArrayList<>();
        for (SibeSegment sibeSegment : fromSegmentList) {
            CtripSegment ctripSegment = new CtripSegment();

            //航司 IATA 二字码，必须与 flightNumber 航司相同
            ctripSegment.setCarrier(sibeSegment.getCarrier());
            // 航班号，如：CA123。航班号数字前若有多余的数字 0，必须去掉；如 CZ006 需返回 CZ6
            ctripSegment.setFlightNumber(sibeSegment.getCarrier() + sibeSegment.getFlightNumber());
            // 出发机场；IATA 三字码
            ctripSegment.setDepAirport(sibeSegment.getDepAirport());
            // 出发航站楼；使用简写，例如T1
            ctripSegment.setDepTime(sibeSegment.getDepTerminal());
            // 起飞日期时间，格式：YYYYMMDDHHMM  例：201203100300 表示 2012 年 3 月 10 日 3 时 0 分
            ctripSegment.setDepTime(sibeSegment.getDepTime());
            // 到达机场 IATA 三字码
            ctripSegment.setArrAirport(sibeSegment.getArrAirport());
            // 抵达航站楼，使用简写，例如T1
            ctripSegment.setArrTerminal(sibeSegment.getArrTerminal());
            // 到达日期时间，格式：YYYYMMDDHHMM  例：201203101305 表示 2012 年 3 月 10 日 13 时 5 分
            ctripSegment.setArrTime(sibeSegment.getArrTime());
            // 经停城市； IATA 三字码
            ctripSegment.setStopCities(sibeSegment.getStopCities());
            // 经停机场； IATA 三字码
            ctripSegment.setStopAirports(sibeSegment.getStopAirports());
            // 代码共享标识（true 代码共享/false 非代码共享）
            ctripSegment.setCodeShare(sibeSegment.getCodeShare());
            // 实际承运航司 若codeShare=true， operatingCarrier不能为空。
            ctripSegment.setOperatingCarrier(StringUtils.isBlank(sibeSegment.getOperatingCarrier()) ? null : sibeSegment.getOperatingCarrier());
            // 实际承运航班号
            if (StringUtils.isNotBlank(sibeSegment.getOperatingCarrier()) && StringUtils.isNotBlank(sibeSegment.getOperatingFlightNo())) {
                ctripSegment.setOperatingFlightNo(sibeSegment.getOperatingCarrier() + sibeSegment.getOperatingFlightNo());
            }
            // 子舱位
            ctripSegment.setCabin(sibeSegment.getCabin());
            // 舱等；头等：F；商务：C；超经：S；经济：Y【目前仅支持返回Y】
            ctripSegment.setCabinGrade(convertCabinGradeToCtrip(sibeSegment.getCabinGrade()));
            // 机型 ，IATA标准3字码,并过滤下列机型运价信息BUS|ICE|LCH|LMO|MTL|RFS|TGV|THS|THT|TRN|TSL|
            ctripSegment.setAircraftCode(sibeSegment.getAircraftCode());
            // 飞行时长；单位为分钟，通过时差转换后的结果。
            ctripSegment.setDuration(sibeSegment.getDuration());

            ctripSegmentList.add(ctripSegment);
        }
        return ctripSegmentList;
    }


    /**
     * Convert trip type to sibe string.
     *
     * @param tripType the trip type
     * @return the string
     */
    public final static String convertTripTypeToSibe(final String tripType) {
        if ("1".equals(tripType)) {
            return "OW"; //单程
        } else if ("2".equals(tripType)) {
            return "RT"; //往返
        } else if ("3".equals(tripType)) {
            return "MT"; //多程
        } else {
            return null;
        }
    }





    //Y: 经济舱-Economy Class;
    //W: 经济特舱-Economy Class Premium;
    //M: 经济优惠舱-Economy Class Discounted
    //F: 头等舱-First Class;
    //C: 商务舱-Business Class;
    private static String convertCabinGradeToCtrip(String cabinGrade) {
        if(StringUtils.isEmpty(cabinGrade)){
            return "";
        }
        switch (cabinGrade) {
            case "Y":
                return "Y"; //经济舱 -->经济
            case "W":
                return "S"; //经济特舱-->超经
            case "M":
                return "Y"; //经济优惠舱-->经济
            case "F":
                return "F"; //头等舱-->头等
            case "C":
                return "C"; //商务舱-->商务
            default:
                return "Y"; // -->经济
        }
    }

    /**
     * 头等：F；商务：C；超经：S；经济：Y【目前仅支持返回Y】
     *
     * @param cabinGrade
     * @return
     */
    private static String convertCabinClassToSibe(String cabinGrade) {
        switch (cabinGrade) {
            case "F":
                return "F";
            case "C":
                return "C";
            case "S":
                return "W";
            case "Y":
                return "Y";
            default:
                return "Y";
        }
    }






    /**
     * 处理行李额信息
     * @param formatBaggageInfoList
     * @return
     */
    private static String replaceBaggageDataToSibeData(List<CtripFormatBaggage> formatBaggageInfoList) {
        StringBuilder otaBaggageBuilder = new StringBuilder();
        for (CtripFormatBaggage ctripFormatBaggage : formatBaggageInfoList) {
            otaBaggageBuilder.append(ctripFormatBaggage.getSegmentNo());
            otaBaggageBuilder.append(",");
            otaBaggageBuilder.append(ctripFormatBaggage.getPassengerType());
            otaBaggageBuilder.append(",");
            otaBaggageBuilder.append(ctripFormatBaggage.getBaggagePiece());
            otaBaggageBuilder.append(",");
            otaBaggageBuilder.append(ctripFormatBaggage.getBaggageWeight());
            otaBaggageBuilder.append(",");
            //目前细节政策中没有手提行李额，data的存和取，需要保持一致，所以先默认为空
            otaBaggageBuilder.append("");
            otaBaggageBuilder.append(",");
            otaBaggageBuilder.append("");
            otaBaggageBuilder.append(",");
            otaBaggageBuilder.append("".equals(ctripFormatBaggage.getCnBaggage())?"":ctripFormatBaggage.getCnBaggage());   // 备注
            otaBaggageBuilder.append("^");
        }
        //删除后面多余的"\\^"
        if(otaBaggageBuilder.length()>0){
            otaBaggageBuilder.deleteCharAt(otaBaggageBuilder.length() - 1);
        }
        return otaBaggageBuilder.toString();
    }

    /**
     * 处理改签想信息
     * @param ctripChanges
     * @return
     */
    private static List<SibeChange> replaceChangeDataToSibeData(List<CtripChange> ctripChanges) {
        List<SibeChange> sibeChanges = new ArrayList<>();

        for(CtripChange ctripChange : ctripChanges) {

            // 乘客类型|改签类型|改签标识|是否允许NoShow改签|改签币种
            // ,NoShow改签必须在此日期之前申请
            // ,航班起飞前多久算NoShow
            // ,退改签费
            // ,NoShow改签费用
            // ,改签费用说明-飞猪专用

            SibeChange sibeChange = new SibeChange();

            sibeChange.setPassengerType(ctripChange.getPassengerType());
            sibeChange.setChangesType(ctripChange.getChangesType());
            sibeChange.setChangesStatus(ctripChange.getChangesStatus());
            sibeChange.setRevNoshow(ctripChange.getRevNoshow());
            sibeChange.setCurrency(ctripChange.getCurrency());
            sibeChange.setNoshowOperationDealine("");
            sibeChange.setRevNoShowCondition(ctripChange.getRevNoShowCondition()==null?0:ctripChange.getRevNoShowCondition());
            sibeChange.setChangesFee(ctripChange.getChangesFee());
            sibeChange.setRevNoshowFee(ctripChange.getRevNoshowFee()==null?new BigDecimal(0):ctripChange.getRevNoshowFee());
            sibeChange.setChangeFeeStr(ctripChange.getCnRevRemark()==null?"":ctripChange.getCnRevRemark());
            sibeChange.setChangeFeeMode(1);

            sibeChanges.add(sibeChange);
        }

        return sibeChanges;
    }

    /**
     * 处理退票信息
     * @param ctripRefunds
     * @return
     */
    private static List<SibeRefund> replaceRefundDataToSibeData(List<CtripRefund> ctripRefunds) {

        List<SibeRefund> sibeRefunds = new ArrayList<>();

        for(CtripRefund ctripRefund : ctripRefunds) {

            // 乘客类型|退票类型|退票标识|是否允许NoShow退票|退票费币种
            // ,NoShow退票必须在此日期之前申请noshowOperationDealine
            // ,航班起飞前多久算NoShow  refNoShowCondition
            // ,退票费   refundFee
            // ,NoShow退票费用   refNoshowFee
            // ,费用说明-飞猪专用

            SibeRefund newSibeRefund = new SibeRefund();

            newSibeRefund.setPassengerType(ctripRefund.getPassengerType());
            newSibeRefund.setRefundType(ctripRefund.getRefundType());
            newSibeRefund.setRefundStatus(ctripRefund.getRefundStatus());
            newSibeRefund.setRefNoshow(ctripRefund.getRefNoshow());
            newSibeRefund.setCurrency(ctripRefund.getCurrency());
            newSibeRefund.setNoshowOperationDealine("");
            newSibeRefund.setRefNoShowCondition(ctripRefund.getRefNoShowCondition()==null?0:ctripRefund.getRefNoShowCondition());
            newSibeRefund.setRefundFee(ctripRefund.getRefundFee());
            newSibeRefund.setRefNoshowFee(ctripRefund.getRefNoshowFee()==null?new BigDecimal(0):ctripRefund.getRefNoshowFee());
            newSibeRefund.setRefundFeeStr(ctripRefund.getCnRefRemark()==null?"":ctripRefund.getCnRefRemark());
            newSibeRefund.setRefundFeeMode(1);

            sibeRefunds.add(newSibeRefund);
        }

        return sibeRefunds;
    }
}
