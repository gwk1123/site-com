package com.sibecommon.service.transform;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.sibecommon.ota.gds.Routing;
import com.sibecommon.ota.gds.Segment;
import com.sibecommon.ota.site.*;
import com.sibecommon.repository.entity.*;
import com.sibecommon.service.ota.OtaRuleFilter;
import com.sibecommon.utils.comparator.PolicyGlobalComparator;
import com.sibecommon.utils.constant.PolicyConstans;
import com.sibecommon.utils.constant.SibeConstants;
import com.sibecommon.utils.exception.CustomSibeException;
import com.sibecommon.utils.redis.impl.ExchangeRateRepositoryImpl;
import com.sibecommon.utils.redis.impl.SiteRulesSwitchRepositoryImpl;
import com.sibecommon.ota.site.*;
import com.sibecommon.repository.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sibecommon.ota.site.*;
import com.sibecommon.repository.entity.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yangdehua on 18/2/24.
 */
@Component
public class TransformCommonPolicy {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TransformCommonPolicy.class);
    private static final String PRODUCT_TYPE_NORMAL = "1";
    private static final String PRODUCT_TYPE_K_SEAT = "2";
    private static final String OTA_SITE_CTRIP001 = "CT001";
    private static final String OTA_SITE_CTRIP002 = "CT002";
    private static final String OTA_SITE_TBSXYPT = "TBSXYPT";
    private static final String OTA_SITE_TBSXYJP = "TBSXYJP";
    private static final String OTA_SITE_SXG = "SXG";
    private static final String OTA_SITE_TN001 = "TN001";
    private static final String OTA_SITE_LY001 = "LY001";
    private static final String OTA_SITE_MFW001 = "MFW001";
    private static final String OTA_OWT = "OWT";

    @Autowired
    private ExchangeRateRepositoryImpl exchangeRateRepository;
    @Autowired
    private SiteRulesSwitchRepositoryImpl siteRulesSwitchRepository;



    /**
     * 细节政策匹配
     *
     * @param sibeSearchRequest the ota search request
     * @param routingList       the routing list
     * @return list list
     */
    public List<SibeRouting> matchPolicy(SibeSearchRequest sibeSearchRequest,
                                         List<SibeRouting> routingList) {
        long startTime = SystemClock.now();

        //2.将请求行程类型转换为政策行程类型
        String policyTripType = sibeSearchRequest.getTripType();
        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.5.1 matchPolicy:" + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");

        //3.补充每个行程的基本信息
        //给出每个行程的主航段、是否中转、中转点、是否联运、联运航司
        routingList.forEach(
            routing -> {
                // todo 20190222
                addInformationToRoutingOperator(routing);
            }
        );

        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.5.2 matchPolicy:" + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");


        List<PolicyInfo> apiPolicyInfoRedisSet = sibeSearchRequest.getPolicyInfos();
        List<PolicyGlobal> apiPolicyGlobalRedisSet = sibeSearchRequest.getPolicyGlobals();

        //4.1 根据销售航司对政策进行分组，只获取产品类型为正常的政策
        Map<String, List<PolicyInfo>> carrierPolicyMap =
            apiPolicyInfoRedisSet
                .stream()
                .filter(apiPolicyInfoRedis -> (apiPolicyInfoRedis != null && apiPolicyInfoRedis.getProductType().equals(PRODUCT_TYPE_NORMAL)))
                .collect(Collectors.groupingBy(PolicyInfo::getAirline));

        //4.2  根据销售航司对政策进行分组，只获取产品类型为K位的政策，目前仅仅针对携程平台
        Map<String, List<PolicyInfo>> carrierPolicyMapForKSeatProduct = null;
        boolean processKSeat = false; //K位处理开关

            //校验OTA-规则产品类型开关
            List<SiteRulesSwitch> otaSwitchValueRedisSet = sibeSearchRequest.getSiteRulesSwitch();
            String parameterKey = "OTA-29"; //OTA-产品类型
            String productRuleType = "29"; //产品类型规则编号

            //判断开关是否有开启，如果没有开启则忽略此规则
            if (otaSwitchValueRedisSet
                .stream()
                .anyMatch(value -> (parameterKey.equals(value.getParameterKey()) && "TRUE".equals(value.getParameterValue())))) {
                if (sibeSearchRequest.
                    getOtaRules()
                    .stream()
                    .filter(Objects::nonNull)
                    .anyMatch(apiControlRuleOtaRedis -> apiControlRuleOtaRedis.getRuleType().equals(productRuleType)
                        && apiControlRuleOtaRedis.getParameter1().contains(PRODUCT_TYPE_K_SEAT))) {

                    carrierPolicyMapForKSeatProduct =
                        apiPolicyInfoRedisSet
                            .stream()
                            .filter(apiPolicyInfoRedis -> (apiPolicyInfoRedis != null && apiPolicyInfoRedis.getProductType().equals(PRODUCT_TYPE_K_SEAT)))
                            .filter(apiPolicyInfoRedis -> (apiPolicyInfoRedis != null && StringUtils.isNotBlank(apiPolicyInfoRedis.getManualSeatCabin())))
                            .collect(Collectors.groupingBy(PolicyInfo::getAirline));

                    processKSeat = true;
                }
            }

        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.5.3 matchPolicy:" + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");


        //6.获得条件匹配优先级操作列表
        List<Integer> conditionMatchPriorityList = generateConditionMatchPriorityList();

        //7.按照始发地和目的地生成始发地目的地匹配code，按照优先级放入list中
        List<String> cityList = sibeSearchRequest.getCityPrioritycList();

        //8.计算这些officeId是否在站点的过夜时间段中，给出一个结果map，key：pcc，value：（过夜留钱的金额）
        Map<String, BigDecimal> officeIdIsOverNightMap = generateOfficeIdTimeOverNightInfo(sibeSearchRequest);

        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.5.4 matchPolicy:" + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");

        //10.
        final boolean processKSeatSign = processKSeat;
        final Map<String, List<PolicyInfo>> carrierPolicyMapForKSeat = carrierPolicyMapForKSeatProduct;

        //币种对汇率
        Map <String ,BigDecimal> rateMap = exchangeRateRepository.findAllExchangeRate() ;

        List<SibeRouting> validatedRoutingList = routingList
            .stream()
            .map(routing -> {


                        //设置OTA币种
                        SiteRulesSwitch siteRulesSwitch = siteRulesSwitchRepository.findSiteRulesSwitchByGroupKeyAndParameterKey("OTA_SITE_SWITCH_"+sibeSearchRequest.getSite(),"CURRENCY");
                        if(null != siteRulesSwitch){
                            sibeSearchRequest.setOtaCurrency(siteRulesSwitch.getParameterValue());
                        }else{
                            LOGGER.error("OTA币种未维护"+sibeSearchRequest.getOta()+"_"+sibeSearchRequest.getSite());
                            throw new CustomSibeException(SibeConstants.RESPONSE_MSG_999, "汇率数据错误【ExchangeUnit不是100】 ", sibeSearchRequest.getUuid(),"search");
                        }

                     //汇率
                    if(null == rateMap.get(sibeSearchRequest.getOtaCurrency()+routing.getCurrencyGDS())){
                            //汇率计算价格
                            BigDecimal otaRate = null ;
                            if(routing.getCurrencyGDS().equals(sibeSearchRequest.getOtaCurrency())){
                                otaRate = BigDecimal.valueOf(1) ;
                            }else{
                                //查找汇率
                                ExchangeRate sibeExchangeRateRedisOTA = exchangeRateRepository.findExchangeRate(sibeSearchRequest.getOtaCurrency()+routing.getCurrencyGDS());
                                if (sibeExchangeRateRedisOTA.getExchangeUnit()!=100){
                                    throw new CustomSibeException(SibeConstants.RESPONSE_MSG_2, "汇率数据错误【ExchangeUnit不是100】 ", sibeSearchRequest.getUuid(),"search");
                                }
                                otaRate = sibeExchangeRateRedisOTA.getForexSellRate().divide(BigDecimal.valueOf(100));
                            }
                            rateMap.put(sibeSearchRequest.getOtaCurrency()+routing.getCurrencyGDS(),otaRate) ;
                    }

                    routing.setGdsToOTARate(rateMap.get(sibeSearchRequest.getOtaCurrency()+routing.getCurrencyGDS()));
                    //ota币种
                    routing.setCurrencyOTA(sibeSearchRequest.getOtaCurrency());

                    //0.是否使用手工价格标志位
                    boolean useManualPrice = false;

                    //1. 初始化
                    TransformCommonPolicy.initializeRoutingPolicy(routing, sibeSearchRequest);

                    //2. 匹配细节政策
                    //2.1携程需要先进行K位置政策，再处理普通产品政策
                    Optional<PolicyInfo> matchedPolicyInfo = Optional.ofNullable(null);
                    if (processKSeatSign
                        && ((sibeSearchRequest.getTripType().equals("OW") && routing.getFromSegments().size() <= 2)
                        || ((sibeSearchRequest.getTripType().equals("RT")) && routing.getFromSegments().size() == 1 && routing.getRetSegments().size() == 1))) {

                        List<Integer> conditionMatchPriorityListForKSeat = generateConditionMatchPriorityListForKSeat();
                        matchedPolicyInfo = matchPolicyByDepCityAndArrCity(routing, cityList, carrierPolicyMapForKSeat, policyTripType, conditionMatchPriorityListForKSeat,sibeSearchRequest,routing.getGdsToOTARate(),"2");

                        //不存在K位政策,则需要匹配普通政策
                        if (!matchedPolicyInfo.isPresent()) {
                            matchedPolicyInfo = matchPolicyByDepCityAndArrCity(routing, cityList, carrierPolicyMap, policyTripType, conditionMatchPriorityList,sibeSearchRequest,routing.getGdsToOTARate(),"1");

                            //3. 匹配全局政策
                            Optional<PolicyGlobal> matchedGlobalPolicyInfo = matchGlobalPolicy(apiPolicyGlobalRedisSet, routing, policyTripType);

                            //4. 政策加减留钱
                            useManualPrice = false;
                            processRoutingPolicyAndPrice(matchedPolicyInfo, matchedGlobalPolicyInfo,
                                officeIdIsOverNightMap.get(routing.getOfficeId()),
                                routing, sibeSearchRequest, useManualPrice);

                        } else {
                            //存在K位政策，进行K位政策处理
                            //不进行全局政策，不进行过夜票面留钱
                            Optional<PolicyGlobal> matchedGlobalPolicyInfo = Optional.ofNullable(null);

                            useManualPrice = true;
                            processRoutingPolicyAndPrice(matchedPolicyInfo, matchedGlobalPolicyInfo, null, routing, sibeSearchRequest, useManualPrice);

                            //修改航段中的舱位
                            String cabin = matchedPolicyInfo.get().getManualSeatCabin();
                            if (sibeSearchRequest.getTripType().equals("OW")) {
                                if (routing.getFromSegments().size() == 1) {
                                    routing.getFromSegments().get(0).setCabin(cabin);
                                } else if (routing.getFromSegments().size() == 2) {
                                    String[] cabinArray = cabin.split("/");
                                    routing.getFromSegments().get(0).setCabin(cabinArray[0]);
                                    routing.getFromSegments().get(1).setCabin(cabinArray[1]);
                                }
                            } else if (sibeSearchRequest.getTripType().equals("RT")) {
                                String[] cabinArray = cabin.split("/");
                                routing.getFromSegments().get(0).setCabin(cabinArray[0]);
                                routing.getRetSegments().get(0).setCabin(cabinArray[1]);
                            }

                        }
                    } else {
                        matchedPolicyInfo = matchPolicyByDepCityAndArrCity(routing, cityList, carrierPolicyMap, policyTripType, conditionMatchPriorityList,sibeSearchRequest,routing.getGdsToOTARate(),"1");

                        //3. 匹配全局政策
                        Optional<PolicyGlobal> matchedGlobalPolicyInfo = matchGlobalPolicy(apiPolicyGlobalRedisSet, routing, policyTripType);

                        //4. 政策加减留钱
                        useManualPrice = false;
                        processRoutingPolicyAndPrice(matchedPolicyInfo, matchedGlobalPolicyInfo, officeIdIsOverNightMap.get(routing.getOfficeId()), routing, sibeSearchRequest, useManualPrice);
                    }

                    return routing;

            })
            //价格止损，当亏损值大于指定比例*GDS原始总价时，则直接过滤掉该方案
            .filter(routing ->(OtaRuleFilter.otaPriceStopLoss(sibeSearchRequest, routing)))
            .collect(Collectors.toList());

        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.5.5 matchPolicy:" + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");
        return validatedRoutingList;
    }

    /**
     * 根据给出的汇率、细节政策、全局政策、过夜留钱，将这些信息设置到RoutingPolicy中，并且计算出成人、儿童相应价格
     *
     * @param
     * @param matchedPolicyInfo
     * @param matchedGlobalPolicyInfo
     * @param overNightPrice
     * @param routing
     */
    public static void processRoutingPolicyAndPrice(Optional<PolicyInfo> matchedPolicyInfo,
                                                     Optional<PolicyGlobal> matchedGlobalPolicyInfo,
                                                     BigDecimal overNightPrice,
                                                     SibeRouting routing,
                                                     SibeSearchRequest sibeSearchRequest,
                                                     boolean useManuallyPrice) {
        //1.综合政策、汇率信息，将这些信息设置到routingPolicy中
        collectInfoToRoutingPolicy(routing, matchedPolicyInfo, matchedGlobalPolicyInfo);

        SibePolicy sibePolicy = routing.getSibeRoutingData().getSibePolicy();

        //返点
        BigDecimal commition = sibePolicy.getCommition();

        //成人票面留钱
        BigDecimal policyAdultPricePlus = sibePolicy.getAdultPricePlus();
        //成人票面税费
        BigDecimal policyAdultTaxPlus = sibePolicy.getAdultTaxPlus();
        //儿童价格类型
        Integer childPricePlusType = sibePolicy.getChildPricePlusType();
        //儿童折扣
        BigDecimal childPlusDiscount = new BigDecimal(sibePolicy.getChildPlusDiscount());

        //全局成人票面留钱
        BigDecimal globalAdultPricePlus = sibePolicy.getGlobalAdultPricePlus();
        //全局成人票面税费
        BigDecimal globalAdultTaxPlus = sibePolicy.getGlobalAdultTaxPlus();
        //全局儿童票面留钱
        BigDecimal globalChildPricePlus = sibePolicy.getGlobalChildPricePlus();
        //全局儿童票面税费
        BigDecimal globalChildTaxPlus = sibePolicy.getGlobalChildTaxPlus();


        //转换成OTA价格
        BigDecimal gdsAdultTicketPriceOTA = getBigGDStoOTA(routing.getAdultPriceGDS(),routing.getGdsToOTARate()) ;
        BigDecimal gdsAdultTicketTaxOTA =  getBigGDStoOTA(routing.getAdultTaxGDS(),routing.getGdsToOTARate()) ;

        BigDecimal zero = new BigDecimal(0);
        if (useManuallyPrice
            && sibePolicy.getManualAdultPrice() != null && sibePolicy.getManualAdultPrice().compareTo(zero) == 1
            && sibePolicy.getManualAdultTax() != null && sibePolicy.getManualAdultTax().compareTo(zero) == 1) {
            //GDS成人票面价 //GDS成人税费
            gdsAdultTicketPriceOTA = matchedPolicyInfo.get().getManualAdultPrice();
            gdsAdultTicketTaxOTA = matchedPolicyInfo.get().getManualAdultTax();

        }

        //3.计算成人价格
        //成人票面 = （GDS票面 * (1-细节返点) + 细节留钱）+ 全局成人票面留钱
        BigDecimal oneHundred = new BigDecimal(100);
        //返点%
        BigDecimal discountForCalculation = (oneHundred.subtract(commition)).divide(oneHundred);

        //返回给OTA平台 成人票面CNY (没有加全局留钱)
        BigDecimal adultTicketPriceOTA = (gdsAdultTicketPriceOTA.multiply(discountForCalculation)).add(policyAdultPricePlus);
        //返回给OTA平台 成人税费= （GDS税费 + 细节税费留钱）
        BigDecimal adultTaxOTA = (gdsAdultTicketTaxOTA.add(policyAdultTaxPlus));

        //计算儿童价格，
        //注意：此仅仅没有匹配上明细政策才有效
        //成人票面CNY+全局儿童票面留钱
        BigDecimal childTicketPriceOTA = adultTicketPriceOTA.add(globalChildPricePlus);
        //成人税费CNY+全局儿童税费留钱
        BigDecimal childTaxOTA = adultTaxOTA.add(globalChildTaxPlus);

        //返回给OTA平台 成人票面CNY 加上全局留钱
        adultTicketPriceOTA = adultTicketPriceOTA.add(globalAdultPricePlus);
        //返回给OTA平台 成人税费CNY + 全局成人税费留钱
        adultTaxOTA = adultTaxOTA.add(globalAdultTaxPlus);

        if (childPricePlusType == PolicyConstans.POLICY_CHILD_PRICE_TYPE_ASSIGN) {
            //1.儿童自定价格
            //儿童票面 = GDS成人票面价（CNY) * 儿童折扣 + 全局儿童票面留钱
            //儿童折扣
            BigDecimal childDiscountForCalculation = childPlusDiscount.divide(oneHundred);

            //儿童票面
            childTicketPriceOTA = (gdsAdultTicketPriceOTA.multiply(childDiscountForCalculation)).add(globalChildPricePlus);
            //儿童税费 = GDS成人税费 + 全局儿童税费留钱
            childTaxOTA = gdsAdultTicketTaxOTA.add(globalChildTaxPlus);
        } else if (childPricePlusType == PolicyConstans.POLICY_CHILD_PRICE_TYPE_SAME_AS_ADULT) {
            //2.儿童与成人同价
            childTicketPriceOTA = adultTicketPriceOTA; //成人与儿童同价
            childTaxOTA = adultTaxOTA; //成人税费与儿童税费同价
        }

        //5.计算过夜留钱
        if (overNightPrice != null) {
            adultTicketPriceOTA = adultTicketPriceOTA.add(overNightPrice);
            childTicketPriceOTA = childTicketPriceOTA.add(overNightPrice);
        }

        //6.将计算好的价格设置到Routing和RoutingPolicy中
        Integer adultTicketPriceIntegerOTA = (adultTicketPriceOTA.setScale(0, BigDecimal.ROUND_UP)).intValue();
        Integer adultTaxIntegerOTA = (adultTaxOTA.setScale(0, BigDecimal.ROUND_UP)).intValue();
        Integer childTicketPriceIntegerOTA = (childTicketPriceOTA.setScale(0, BigDecimal.ROUND_UP)).intValue();
        Integer childTaxIntegerOTA = (childTaxOTA.setScale(0, BigDecimal.ROUND_UP)).intValue();


        routing.setAdultPriceOTA(adultTicketPriceIntegerOTA);
        routing.setAdultTaxOTA(adultTaxIntegerOTA);
        routing.setChildPriceOTA(childTicketPriceIntegerOTA);
        routing.setChildTaxOTA(childTaxIntegerOTA);


        //发票更新
        routing.setInvoiceType(sibePolicy.getInvoiceType());

        sibePolicy.setPolicyAdultPriceOTA(adultTicketPriceIntegerOTA);
        sibePolicy.setPolicyAdultTaxOTA(adultTaxIntegerOTA);
        sibePolicy.setPolicyChildPriceOTA(childTicketPriceIntegerOTA);
        sibePolicy.setPolicyChildTaxOTA(childTaxIntegerOTA);

    }


    /**
     * 根据给出的汇率、细节政策、全局政策，将这些信息设置到RoutingPolicy中
     *
     * @param
     * @param routing
     * @param matchedPolicyInfo
     * @param matchedGlobalPolicyInfo
     */
    private static void collectInfoToRoutingPolicy(SibeRouting routing,
                                                   Optional<PolicyInfo> matchedPolicyInfo,
                                                   Optional<PolicyGlobal> matchedGlobalPolicyInfo) {
        SibePolicy sibePolicy = routing.getSibeRoutingData().getSibePolicy();
        if (matchedPolicyInfo.isPresent()) {
            //Policy ID
            sibePolicy.setPolicyId(matchedPolicyInfo.get().getId());
            //行李额来源 行李额来源 1-使用GDS行李额 2-使用政策指定行李额
            sibePolicy.setBaggageType(matchedPolicyInfo.get().getBaggageType());
            //政策指定行李额
//            sibePolicy.setPolicyInfoBaggages(matchedPolicyInfo.get().getApiPolicyInfoBaggages());
            //改签信息
//            sibePolicy.setPolicyInfoChanges(matchedPolicyInfo.get().getApiPolicyInfoChanges());
            //退票信息
//            sibePolicy.setPolicyInfoRefunds(matchedPolicyInfo.get().getApiPolicyInfoRefunds());

            //返点
            sibePolicy.setCommition(matchedPolicyInfo.get().getCommition());
            //成人票面留钱
            sibePolicy.setAdultPricePlus(matchedPolicyInfo.get().getAdultPrice());
            //成人税费留钱
            sibePolicy.setAdultTaxPlus(matchedPolicyInfo.get().getAdultTax());

            //手工成人票面价
            if (matchedPolicyInfo.get().getManualAdultPrice() != null) {
                sibePolicy.setManualAdultPrice(matchedPolicyInfo.get().getManualAdultPrice());
            }

            //手工成人税费
            if (matchedPolicyInfo.get().getManualAdultTax() != null) {
                sibePolicy.setManualAdultTax(matchedPolicyInfo.get().getManualAdultTax());
            }

            //儿童定价方式
            sibePolicy.setChildPricePlusType(matchedPolicyInfo.get().getChildPriceType());
            //儿童自定义票价
            if (matchedPolicyInfo.get().getChildPriceType() == PolicyConstans.POLICY_CHILD_PRICE_TYPE_ASSIGN) {
                //儿童折扣%
                sibePolicy.setChildPlusDiscount(matchedPolicyInfo.get().getChildDiscount());
            }

            //出票说明
            sibePolicy.setOtherIssueTicketMsg(matchedPolicyInfo.get().getTicketRuleNotes());
            //报销凭证
            sibePolicy.setInvoiceType(matchedPolicyInfo.get().getInvoiceType());
            //产品类型
            sibePolicy.setProductType(matchedPolicyInfo.get().getProductType());
        }

        if (matchedGlobalPolicyInfo.isPresent()) {
            sibePolicy.setGlobalPolicyId(matchedGlobalPolicyInfo.get().getId());
            //成人票面留钱
            sibePolicy.setGlobalAdultPricePlus(matchedGlobalPolicyInfo.get().getAdultPrice());
            //成人税费留钱
            sibePolicy.setGlobalAdultTaxPlus(matchedGlobalPolicyInfo.get().getAdultTax());
            //儿童票面留钱
            sibePolicy.setGlobalChildPricePlus(matchedGlobalPolicyInfo.get().getChildPrice());
            //儿童税费留钱
            sibePolicy.setGlobalChildTaxPlus(matchedGlobalPolicyInfo.get().getChildTax());
        }

        //汇率
        sibePolicy.setGdsToOTARate(routing.getGdsToOTARate());
    }


    /**
     * 为routing初始化RoutingPolicy
     *
     * @param routing
     */
    public static SibeRouting initializeRoutingPolicy(SibeRouting routing, SibeSearchRequest sibeSearchRequest) {
        BigDecimal zero = new BigDecimal(0);
        BigDecimal negativeOne = new BigDecimal(-1);

        SibePolicy sibePolicy = new SibePolicy();

        sibePolicy.setPolicyAdultPriceOTA(0);
        sibePolicy.setPolicyAdultTaxOTA(0);
        sibePolicy.setPolicyChildPriceOTA(0);
        sibePolicy.setPolicyChildTaxOTA(0);

        //行李额来源 1-使用GDS行李额 2-使用指定行李额 默认使用
        sibePolicy.setBaggageType(1);
        sibePolicy.setGdsToOTARate(routing.getGdsToOTARate());
        //设置GDS转人民币
        sibePolicy.setGdsToCNYRate(routing.getGdsToCNYRate());
        sibePolicy.setPolicyId(-1L);
        sibePolicy.setCommition(zero);
        sibePolicy.setAdultPricePlus(zero);
        sibePolicy.setAdultTaxPlus(zero);

        //默认是境外电子凭证
        sibePolicy.setInvoiceType(PolicyConstans.INVOICE_TYPE_ELECTRONIC_INVOICE);

        sibePolicy.setChildPricePlusType(PolicyConstans.POLICY_HAVE_NOT_CHILD_PRICE_TYPE);

        sibePolicy.setChildPlusDiscount(0);

        sibePolicy.setGlobalPolicyId(-1L);
        sibePolicy.setGlobalAdultPricePlus(zero);
        sibePolicy.setGlobalAdultTaxPlus(zero);
        sibePolicy.setGlobalChildPricePlus(zero);
        sibePolicy.setGlobalChildTaxPlus(zero);

        sibePolicy.setOtherIssueTicketMsg(sibeSearchRequest.getOtherIssueMsg());
        //设置产品类型，默认为 1：直连产品类型
        sibePolicy.setProductType("1");

        //设置手工成人票面价
        sibePolicy.setManualAdultPrice(negativeOne);
        //设置手工成人税费
        sibePolicy.setManualAdultTax(negativeOne);

        //设置原始舱位
        StringBuilder cabinBuilder = new StringBuilder();
        for (SibeSegment segment : routing.getFromSegments()) {
            cabinBuilder.append(segment.getCabin()).append("-");
        }
        if (routing.getRetSegments() != null && routing.getRetSegments().size() > 0) {
            for (SibeSegment segment : routing.getRetSegments()) {
                cabinBuilder.append(segment.getCabin()).append("-");
            }
        }
        sibePolicy.setOriginalCabins(cabinBuilder.toString());

        routing.getSibeRoutingData().setSibePolicy(sibePolicy);

        return routing;
    }


    /**
     * 将routingList中的销售航司进行汇总
     *
     * @param routingList
     * @return
     */
    private static Set<String> classifyValidatingCarriersOfRoutingList(List<Routing> routingList) {
        Set<String> allValidatingCarrierSet = new HashSet<>();
        for (Routing routing : routingList) {
            allValidatingCarrierSet.add(routing.getValidatingCarrier());
        }
        return allValidatingCarrierSet;
    }


    private static BigDecimal processOverNight(Map<String, Boolean> officeIdIsOverNightMap,
                                               Routing routing,
                                               OtaRuleOverNightInfo overNightInfo) {
        if (officeIdIsOverNightMap == null
            || officeIdIsOverNightMap.get(routing.getOfficeId()) == null
            || officeIdIsOverNightMap.get(routing.getOfficeId()) == false) {
            return null;
        } else {

            return overNightInfo.getPrice();
        }
    }

    /**
     * 计算这些officeId是否在站点的过夜时间段中，给出一个结果map，key：pcc，value：
     *
     * @return
     */
    public static Map<String, BigDecimal> generateOfficeIdTimeOverNightInfo(SibeSearchRequest sibeSearchRequest) {

        Map<String, BigDecimal> officeIdTimeOverNightMap = new HashMap<>();

        //1.过夜时间,如果没有配置，则
        OtaRuleOverNightInfo otaRuleOverNightInfo = OtaRuleFilter.getOtaSiteOverNightInfo(sibeSearchRequest);

        //2.循环请求PCC列表
        ////如果officeId当地时间在站点跨夜时间段内，则需要在票面价格上加上跨夜价格
        sibeSearchRequest.getSearchRouteMap().entrySet()
            .stream()
            .filter(searchRoute -> (officeIdIsOverNightPrice(getOfficeTimeNow(searchRoute.getValue().getSearchPcc()), otaRuleOverNightInfo)))
            .forEach(searchRoute -> {
                    officeIdTimeOverNightMap.put(searchRoute.getValue().getSearchPcc().getPccCode(), otaRuleOverNightInfo.getPrice());
                }
            );

        return officeIdTimeOverNightMap;
    }


    /**
     * 获取officeId当地的时间LocalTime
     *
     * @return
     */
    private static LocalTime getOfficeTimeNow(GdsPcc sibeGdsPccRedis) {
        LocalTime beijingTimeNow = LocalTime.now();
        LocalTime utcTime = beijingTimeNow.plusHours(-8);
        BigDecimal timeZoneMinutes = (new BigDecimal(sibeGdsPccRedis.getTimeZone())).multiply(new BigDecimal(60));
        timeZoneMinutes = timeZoneMinutes.setScale(0, RoundingMode.UP);
        LocalTime officeTimeNow = utcTime.plusMinutes(timeZoneMinutes.longValue());
        return officeTimeNow;
    }

    /**
     * 如果officeId当地时间在站点跨夜时间段内，则需要在票面价格上加上跨夜价格
     *
     * @param officeTimeNow
     * @param overNightInfo
     * @return
     */
    private static boolean officeIdIsOverNightPrice(LocalTime officeTimeNow, OtaRuleOverNightInfo overNightInfo) {
        if (overNightInfo == null) {
            return false;
        }
        if (officeTimeNow.getHour() >= 22) {
            return officeTimeNow.isAfter(overNightInfo.getStart());
        } else if (officeTimeNow.getHour() >= 0 && officeTimeNow.getHour() <= 2) {
            return officeTimeNow.isBefore(overNightInfo.getEnd());
        } else {
            return false;
        }

    }

    /**
     * 匹配全局政策
     *
     * @param routing
     */
    public static Optional<PolicyGlobal> matchGlobalPolicy(List<PolicyGlobal> apiPolicyGlobalRedisSet, SibeRouting routing, String policyTripType) {
        int codeShare = routing.getCodeShareFlag().intValue();
       /* if (routing.isAllSegmentCodeShare()) {
            codeShare = PolicyConstans.PERMIT_TYPE_YES; //共享
        } else if (routing.isAllSegmentUnCodeShare()) {
            codeShare = PolicyConstans.PERMIT_TYPE_NO; //不共享
        } else {
            codeShare = PolicyConstans.PERMIT_TYPE_UNLIMITED; //不限
        }*/

        //销售航司
        String carrier = routing.getValidatingCarrier();
        /*运价类型
         *PUB：公布运价
         *PRV：私有运价
         *LCC：廉价航司产品
         */
        String fareType = routing.getFareType();

        //混合航司
        int interlineSign = routing.getInterlineSign();
        int permitTransit = routing.getTransitSign();
        String gds = routing.getReservationType();

        Optional<PolicyGlobal> apiPolicyGlobalRedis =
            apiPolicyGlobalRedisSet
                .stream()
                .filter(Objects::nonNull)
                .filter(policyGlobal -> (carrier.equals(policyGlobal.getAirline()) || PolicyConstans.POLICY_AIRLINE_ALL.equals(policyGlobal.getAirline())))
                .filter(policyGlobal -> (SibeUtil.contains(StringUtils.isBlank(policyGlobal.getBookGdsChannel())?"1A/1G/1S":policyGlobal.getBookGdsChannel(),gds,"/")))
                .filter(policyGlobal -> (policyTripType.equals(policyGlobal.getTripType())) || PolicyConstans.POLICY_TRIP_TYPE_ALL.equals((policyGlobal.getTripType())))
                .filter(policyGlobal -> (codeShare == policyGlobal.getPermitCodeShare()) || (policyGlobal.getPermitCodeShare() == 3)) //1 是,2 否,3 不限
                .filter(policyGlobal -> (permitTransit == policyGlobal.getPermitTransit()) || (policyGlobal.getPermitTransit() == 3))
                .filter(policyGlobal -> (fareType.equals(policyGlobal.getPriceType()) || PolicyConstans.POLICY_PRICE_TYPE_ALL.equals(policyGlobal.getPriceType())))
                .filter(policyGlobal -> (interlineSign == policyGlobal.getPermitInterline()) || (policyGlobal.getPermitInterline() == 3))
                .sorted(new PolicyGlobalComparator().reversed())
                .findFirst();
        return apiPolicyGlobalRedis;
    }


    public  static Optional<PolicyInfo> matchPolicyByDepCityAndArrCity(SibeRouting routing,
                                                                               List<String> cityList,
                                                                               Map<String, List<PolicyInfo>> carrierPolicyMap,
                                                                               String tripType,
                                                                               List<Integer> conditionMatchPriorityList,
                                                                               SibeSearchRequest sibeSearchRequest,BigDecimal priceRate,String product) {

        //如果没有该航司的细节政策，则直接返回
        if (carrierPolicyMap == null || carrierPolicyMap.size() == 0) {
            return Optional.ofNullable(null);
        }

        //得到当前销售航司的所有细节政策
        List<PolicyInfo> policyInfoList = carrierPolicyMap.get(routing.getValidatingCarrier());
        if (policyInfoList == null || policyInfoList.size() == 0) {
            return Optional.ofNullable(null);
        }

        Set<String> depSet=new HashSet<>();
        Set<String> arrSet=new HashSet<>();
        for(String str:cityList){
            depSet.add(str.split("/")[0]);
            arrSet.add(str.split("/")[1]);
        }
        List<PolicyInfo>apiPolicyInfoRedisSortList=new ArrayList<>();
        //根据出发地目的地级别进行政策查询
        for (String city : cityList) {
            String[] cityArray = StringUtils.split(city, "/");
           // Optional<ApiPolicyInfoRedis> apiPolicyInfoRedis =
            List<PolicyInfo> apiPolicyInfoRedisList=
                policyInfoList
                    .stream()
                    .filter(policyInfo -> (routing.getValidatingCarrier().equals(policyInfo.getAirline())))//添加航司过滤
                    .filter(policyInfo -> {
                        if (((Arrays.stream(StringUtils.split(policyInfo.getDepCity(), "/")).anyMatch(depCity -> (depCity.equals(cityArray[0]))) || routingDepCityAirlineCondition(routing,policyInfo,tripType))
                            && ((StringUtils.isBlank(policyInfo.getDepCityExcept())?true:Arrays.stream(StringUtils.split(policyInfo.getDepCityExcept(), "/")).noneMatch(depCity -> (depSet.contains(depCity)))) && routingDepCityExceptCondition(routing,policyInfo,tripType)))
                            && ((Arrays.stream(StringUtils.split(policyInfo.getArrCity(), "/")).anyMatch(arrCity -> (arrCity.equals(cityArray[1]))) || routingArrCitysAirlineCondition(routing,policyInfo,tripType))
                            && ((StringUtils.isBlank(policyInfo.getArrCityExcept())?true:Arrays.stream(StringUtils.split(policyInfo.getArrCityExcept(), "/")).noneMatch(arrCity -> (arrSet.contains(arrCity)))) && routingArrCityExceptCondition(routing,policyInfo,tripType))) ) {
                            //依次匹配所有政策条件，所有条件匹配成功返回true，否则返回false
//                            LOGGER.info("依次匹配所有政策条件，所有条件匹配成功返回true，否则返回false");
                            return routingMatchPolicyAllCondition(routing, policyInfo, conditionMatchPriorityList, tripType,null);
                        }
                        return false;
                    }).collect(Collectors.toList());
                   /* .sorted(new PolicyInfoComparator().reversed())
                    .findFirst();*/

           /* if (apiPolicyInfoRedis.isPresent()) {
                return apiPolicyInfoRedis;
            }*/
            apiPolicyInfoRedisSortList.addAll(apiPolicyInfoRedisList);
        }
        if(apiPolicyInfoRedisSortList.size()>0 && apiPolicyInfoRedisSortList != null){
            Optional<PolicyInfo> apiPolicyInfoRedis = apiPolicyInfoPriceSort(sibeSearchRequest,priceRate,apiPolicyInfoRedisSortList,product,routing);
            if(apiPolicyInfoRedis.isPresent()){
                return apiPolicyInfoRedis;
            }
        }

        return Optional.ofNullable(null);
    }

    /*
    * 分别对K位、普通产品进行排序
    * **/
    private static Optional<PolicyInfo> apiPolicyInfoPriceSort(SibeSearchRequest sibeSearchRequest,BigDecimal priceRate,List<PolicyInfo> apiPolicyInfoRedisList,String product,SibeRouting routing){
        if("1".equals(product)){
            Collections.sort(apiPolicyInfoRedisList, new Comparator<PolicyInfo>() {
                @Override
                public int compare(PolicyInfo o1, PolicyInfo o2) {
                    BigDecimal oneHundred = new BigDecimal(100);
                    BigDecimal commition1 = o1.getCommition();
                    BigDecimal discountForCalculation1 = (oneHundred.subtract(commition1)).divide(oneHundred);
                    BigDecimal commition2 = o2.getCommition();
                    BigDecimal discountForCalculation2 = (oneHundred.subtract(commition2)).divide(oneHundred);
                    BigDecimal gdsAdultTicketPriceHKD = new BigDecimal(routing.getAdultPriceGDS());
                    BigDecimal gdsAdultTicketTaxHKD = new BigDecimal(routing.getAdultTaxGDS());
                    BigDecimal gdsAdultTicketPriceCNY = gdsAdultTicketPriceHKD.multiply(priceRate); //priceRate HKG对RMB汇率
                    //GDS成人税费(CNY)
                    BigDecimal gdsAdultTicketTaxCNY = gdsAdultTicketTaxHKD.multiply(priceRate);
                    if ("OWT001".equals(sibeSearchRequest.getSite())) {
                        BigDecimal PriceCNYOne = (gdsAdultTicketPriceHKD.multiply(discountForCalculation1)).add(o1.getAdultPrice()).add(gdsAdultTicketTaxHKD).add(o1.getAdultTax());
                        BigDecimal PriceCNYTwo = (gdsAdultTicketPriceHKD.multiply(discountForCalculation2)).add(o2.getAdultPrice()).add(gdsAdultTicketTaxHKD).add(o2.getAdultTax());
                        if (PriceCNYOne.compareTo(PriceCNYTwo) > 0) {
                            return 1;
                        } else if (PriceCNYOne.compareTo(PriceCNYTwo) < 0) {
                            return -1;
                        }else {
                            if(o1.getUpdateTime().isAfter(o2.getUpdateTime())){
                                return -1;
                            }else if(o1.getUpdateTime().isBefore(o2.getUpdateTime())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    } else {
                        BigDecimal PriceCNYOne = (gdsAdultTicketPriceCNY.multiply(discountForCalculation1)).add(o1.getAdultPrice()).add(gdsAdultTicketTaxCNY).add(o1.getAdultTax());
                        BigDecimal PriceCNYTwo = (gdsAdultTicketPriceCNY.multiply(discountForCalculation2)).add(o2.getAdultPrice()).add(gdsAdultTicketTaxCNY).add(o2.getAdultTax());
                        if (PriceCNYOne.compareTo(PriceCNYTwo) > 0) {
                            return 1;
                        } else if (PriceCNYOne.compareTo(PriceCNYTwo) < 0) {
                            return -1;
                        }else {
                            if(o1.getUpdateTime().isAfter(o2.getUpdateTime())){
                                return -1;
                            }else if(o1.getUpdateTime().isBefore(o2.getUpdateTime())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }

                    }
                }
            });
        }else {
            Collections.sort(apiPolicyInfoRedisList, new Comparator<PolicyInfo>() {
                @Override
                public int compare(PolicyInfo o1, PolicyInfo o2) {
                    BigDecimal oneHundred = new BigDecimal(100);
                    BigDecimal commition1 = o1.getCommition();
                    BigDecimal discountForCalculation1 = (oneHundred.subtract(commition1)).divide(oneHundred);
                    BigDecimal commition2 = o2.getCommition();
                    BigDecimal discountForCalculation2 = (oneHundred.subtract(commition2)).divide(oneHundred);
                    BigDecimal policy1TotalPrice = ((o1.getManualAdultPrice().multiply(discountForCalculation1)).add(o1.getAdultPrice())).add(o1.getManualAdultTax()).add(o1.getAdultTax());
                    BigDecimal policy2TotalPrice = ((o2.getManualAdultPrice().multiply(discountForCalculation2)).add(o2.getAdultPrice())).add(o2.getManualAdultTax()).add(o2.getAdultTax());
                   if(policy1TotalPrice.compareTo(policy2TotalPrice) > 0){
                       return 1;
                   }else if(policy1TotalPrice.compareTo(policy2TotalPrice) < 0){
                       return -1;
                   }else {
                       if(o1.getUpdateTime().isAfter(o2.getUpdateTime())){
                           return -1;
                       }else if(o1.getUpdateTime().isBefore(o2.getUpdateTime())){
                           return 1;
                       }else {
                           return 0;
                       }
                   }
                }
            });
        }
        return Optional.ofNullable(apiPolicyInfoRedisList.get(0));
    }


    /*
    * 出发地除外、匹配机场码
    * */
    public static boolean routingDepCityExceptCondition(SibeRouting routing,PolicyInfo policyInfoRedis,String tripType){
        if(StringUtils.isNotBlank(policyInfoRedis.getDepCityExcept())) {
            if (policyInfoRedis.getDepCityExcept().contains("#")) {
                if ("RT".equals(tripType)) {
                    String depAirport = routing.getFromSegments().get(0).getDepAirport();
                    String arrAirport = routing.getRetSegments().get(routing.getRetSegments().size() - 1).getArrAirport();
                    if (depAirport.equals(arrAirport)) {
                        String[] depCityExcept = policyInfoRedis.getDepCityExcept().split("/");
                        for (String depExcept : depCityExcept) {
                            if (depExcept.contains("#")) {
                                if (depExcept.equals("#" + depAirport)) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    } else {
                        return true;
                    }
                } else {
                    String depAirport = routing.getFromSegments().get(0).getDepAirport();
                    String[] depCityExcept = policyInfoRedis.getDepCityExcept().split("/");
                    for (String depExcept : depCityExcept) {
                        if (depExcept.contains("#")) {
                            if (depExcept.equals("#" + depAirport)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }

            } else {
                return true;
            }
        }else {
            return  true;
        }

    }

    /*
    * 目的地除外匹配机场码
    * **/

    public static boolean routingArrCityExceptCondition(SibeRouting routing,PolicyInfo policyInfoRedis,String tripType){
        if(StringUtils.isNotBlank(policyInfoRedis.getArrCityExcept())) {
            if (policyInfoRedis.getArrCityExcept().contains("#")) {
                if ("RT".equals(tripType)) {
                    String arrAirport = routing.getFromSegments().get(routing.getFromSegments().size() - 1).getArrAirport();
                    String depAirport = routing.getRetSegments().get(0).getDepAirport();
                    if (depAirport.equals(arrAirport)) {
                        String[] arrCityExcept = policyInfoRedis.getArrCityExcept().split("/");
                        for (String arrExcept : arrCityExcept) {
                            if (arrExcept.contains("#")) {
                                if (arrExcept.equals("#" + depAirport)) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    } else {
                        return true;
                    }
                } else {
                    String depAirport = routing.getFromSegments().get(routing.getFromSegments().size() - 1).getArrAirport();
                    String[] arrCityExcept = policyInfoRedis.getArrCityExcept().split("/");
                    for (String arrExcept : arrCityExcept) {
                        if (arrExcept.contains("#")) {
                            if (arrExcept.equals("#" + depAirport)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }

            } else {
                return true;
            }
        }else {
            return true;
        }
    }


    /*
    * GDS返回的方案匹配政策出发的、目地的中的机场码
    * */
    public static boolean routingDepCityAirlineCondition(SibeRouting routing,PolicyInfo policyInfo,String tripType){
        String[] depCitys=policyInfo.getDepCity().split("/");
        for(String s:depCitys){
            if(s.contains("#")){
                boolean isOwValid = false,isRtValid = false;
                String depAirport=routing.getFromSegments().get(0).getDepAirport();
                if(s.equals("#"+depAirport)){
                    isOwValid = true;
                }
                if(tripType.equals("RT")){
                    String arrAirport=routing.getRetSegments().get(routing.getRetSegments().size()-1).getArrAirport();
                    if(depAirport.equals(arrAirport)){
                        isRtValid = true;
                    }
                }
                if(tripType.equals("OW")){
                    if(isOwValid){
                        return true;
                    }else {
                        continue;
                    }
                }else{
                    if(isOwValid && isRtValid){
                        return true;
                    }else {
                        continue;
                    }
                }
            }
        }
        return  false;
    }

    public static boolean routingArrCitysAirlineCondition(SibeRouting routing,PolicyInfo policyInfo,String tripType){
         String[] arrCitys=policyInfo.getArrCity().split("/");
        for(String s:arrCitys){
            if(s.contains("#")){
                boolean isOwValid = false,isRtValid = false;
                String arrAirpor=routing.getFromSegments().get(routing.getFromSegments().size()-1).getArrAirport();
                if(s.equals("#"+arrAirpor)){
                    isOwValid = true;
                }
                if(tripType.equals("RT")){
                  String depAirpor=routing.getRetSegments().get(0).getDepAirport();
                  if(arrAirpor.equals(depAirpor)){
                      isRtValid = true;
                  }
                }
                if(tripType.equals("OW")){
                    if(isOwValid){
                        return true;
                    }else {
                        continue;
                    }
                }else{
                    if(isOwValid && isRtValid){
                        return true;
                    }else {
                        continue;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 依次匹配所有政策条件，所有条件匹配成功返回true，否则返回false
     *
     * @param routing
     * @param apiPolicyInfo
     * @param conditionMatchPriorityList
     * @param tripType
     * @return
     */
    public static boolean routingMatchPolicyAllCondition(SibeRouting routing,
                                                          PolicyInfo apiPolicyInfo,
                                                          List<Integer> conditionMatchPriorityList,
                                                          String tripType, Map<String,String> policyMap) {
        int conditionMatchNumber = 0;

        conditionSign:
        for (; conditionMatchNumber < conditionMatchPriorityList.size(); conditionMatchNumber++) {

            Integer condition = conditionMatchPriorityList.get(conditionMatchNumber);
            switch (condition) {
                case SibeConstants.MP_TRIP_TYPE:
                    boolean tripDateValidateSign = TransformCommonPolicyMatchTool.policyMatchTripDate(apiPolicyInfo, tripType);
                    if (tripDateValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_SEAT_CABIN:
                    boolean seatCabinValidateSign = TransformCommonPolicyMatchTool.policyMatchSeatCabin(apiPolicyInfo, tripType, routing);
                    if (seatCabinValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_BOOK_GDS_CHANNEL:
                    boolean bookGdsChannelValidateSign = TransformCommonPolicyMatchTool.policyBookGdsChannel(apiPolicyInfo,routing);
                    if(bookGdsChannelValidateSign){
                        continue;
                    }else{
                        break conditionSign;
                    }

                case SibeConstants.MP_PRICE_TYPE:
                    boolean priceTypeValidateSign = TransformCommonPolicyMatchTool.policyMatchPriceType(apiPolicyInfo, routing);
                    if (priceTypeValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_TRANSFER_TYPE:
                    boolean transferTypeValidateSign = TransformCommonPolicyMatchTool.policyMatchTransferType(apiPolicyInfo, routing);
                    if (transferTypeValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_INTERLINE_TYPE:
                    boolean interlineTypeValidateSign = TransformCommonPolicyMatchTool.policyMatchInterlineType(apiPolicyInfo, routing);
                    if (interlineTypeValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_CODE_SHARE_TYPE:
                    if (apiPolicyInfo.getPermitCodeShare() == PolicyConstans.POLICY_YES_NO_UNLIMITED_UNLIMITED) {
                        continue;
                    } else if (apiPolicyInfo.getPermitCodeShare().intValue() == routing.getCodeShareFlag().intValue()) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_CODE_SHARE_AIRLINE:
                    boolean codeShareValidateSign = TransformCommonPolicyMatchTool.policyCodeShareAirline(apiPolicyInfo, routing);
                    if (codeShareValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_CODE_SHARE_AIRLINE_EXCEPT:
                    boolean codeShareAirlineExceptValidateSign = TransformCommonPolicyMatchTool.policyCodeShareAirlineExcept(apiPolicyInfo, routing);
                    if (codeShareAirlineExceptValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                    case SibeConstants.MP_INTERLINE_AIRLINE:
                    boolean interlineAirlineValidateSign = TransformCommonPolicyMatchTool.policyMatchInterlineAirline(apiPolicyInfo, routing);
                    if (interlineAirlineValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_INTERLINE_AIRLINE_EXCEPT:
                    boolean interlineAirlineExceptValidateSign = TransformCommonPolicyMatchTool.policyMatchInterlineAirlineExcept(apiPolicyInfo, routing);
                    if (interlineAirlineExceptValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_OUTBOUND_DATE:
                    boolean outboundDateValidateSign = TransformCommonPolicyMatchTool.policyMatchTravelDate(
                        apiPolicyInfo.getOutboundDateStart(), apiPolicyInfo.getOutboundDateEnd(), routing.getFromSegments());
                    if (outboundDateValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_INBOUND_DATE:
                    boolean inboundDateValidateSign = TransformCommonPolicyMatchTool.policyMatchTravelDate(
                        apiPolicyInfo.getInboundDateStart(), apiPolicyInfo.getInboundDateEnd(), routing.getRetSegments());
                    if (inboundDateValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_SALE_DATE:
                    LocalDate searchDate = LocalDate.now();
                    boolean saleDateValidateSign = TransformCommonPolicyMatchTool.policyMatchSaleDate(apiPolicyInfo, searchDate);
                    if (saleDateValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_TRANSFER_POINT:
                    //指定中转点
                    boolean transferPointValidateSign = TransformCommonPolicyMatchTool.policyMatchTransferPoint(apiPolicyInfo, routing);
                    if (transferPointValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }

                case SibeConstants.MP_OUTBOUND_DAY_TIME:
                    //去程班期
                    //todo 暂时使用北京时间
                    boolean outBoundDayTimeValidateSign = TransformCommonPolicyMatchTool.policyMatchDayTime(apiPolicyInfo.getOutboundDayTime(), routing.getFromSegments());
                    if (outBoundDayTimeValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }
                case SibeConstants.MP_INBOUND_DAY_TIME:
                    //回程班期
                    //todo 暂时使用北京时间
                    boolean inBoundDayTimeValidateSign = TransformCommonPolicyMatchTool.policyMatchDayTime(apiPolicyInfo.getInboundDayTime(), routing.getRetSegments());
                    if (inBoundDayTimeValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }
                case SibeConstants.MP_SEAT_GRADE:
                    //适用舱等
                    boolean setaGradeValidateSign = TransformCommonPolicyMatchTool.policyMatchSetaGrade(apiPolicyInfo, routing, tripType);
                    if (setaGradeValidateSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }
                case SibeConstants.MP_FLIGHT_AVAILABLE:
                    //可售航班
                    boolean availableFlightSign = TransformCommonPolicyMatchTool.policyMatchFlightAvailable(apiPolicyInfo, routing, tripType);
                    if (availableFlightSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }
                case SibeConstants.MP_FLIGHT_PROHIBITED:
                    //禁售航班
                    boolean prohibitedFlightSign = TransformCommonPolicyMatchTool.policyMatchFlightProhibited(apiPolicyInfo, routing, tripType);
                    if (prohibitedFlightSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }
                case SibeConstants.MP_ADVANCE_SALE_DAY:
                    //提前销售天数
                    boolean advanceSaleDaySign = TransformCommonPolicyMatchTool.policyMatchAdvanceSaleDay(apiPolicyInfo, routing);
                    if (advanceSaleDaySign) {
                        continue;
                    } else {
                        break conditionSign;
                    }
                case SibeConstants.MP_OUTBOUND_DATE_EXCEPT:
                    //去程除外旅行日期
                    boolean outboundDateExceptSign = TransformCommonPolicyMatchTool.policyMatchTravelDateExcept(
                        apiPolicyInfo.getOutboundTravelDateExcept(), routing.getFromSegments());
                    if (outboundDateExceptSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }
                case SibeConstants.MP_INBOUND_DATE_EXCEPT:
                    //回程除外旅行日期
                    boolean inboundDateExceptSign = TransformCommonPolicyMatchTool.policyMatchTravelDateExcept(
                        apiPolicyInfo.getInboundTravelDateExcept(), routing.getRetSegments());
                    if (inboundDateExceptSign) {
                        continue;
                    } else {
                        break conditionSign;
                    }
                case SibeConstants.MP_SEAT_CABIN_EXCEPT:
                    //适用舱位除外
                    boolean seatCabinExcept = TransformCommonPolicyMatchTool.policySeatCabinExcept(apiPolicyInfo, routing);
                    if (seatCabinExcept) {
                        continue;
                    } else {
                        break conditionSign;
                    }
                case SibeConstants.MP_RET_MIN_AND_MAX_TIME:
                    //最小和最大停留时间（分）
                    boolean retMinAndMaxTime = TransformCommonPolicyMatchTool.policyRetMinAndMaxTime(apiPolicyInfo, routing,tripType);
                    if (retMinAndMaxTime) {
                        continue;
                    } else {
                        break conditionSign;
                    }

            }
        }
        //如果所有条件都匹配成功，则需要遍历完conditionMatchPriorityList，
        //如果遍历变量与list长度相等则代表遍历完，所有条件完全匹配返回true，否则返回false
        if (conditionMatchNumber == conditionMatchPriorityList.size()) {
            return true;
        } else {
            if(policyMap != null) {
                policyMap.put("policyStatus", String.valueOf(conditionMatchNumber));
            }
            return false;
        }
    }


    /**
     * 补充每个行程具体操作方法
     * 给出每个行程的是否中转、中转点、是否混合、混合航司
     *
     * @param routing
     */
    public static void addInformationToRoutingOperator(SibeRouting routing) {

        List<SibeSegment> segmentList = new ArrayList<>();
        segmentList.addAll(routing.getFromSegments());
        segmentList.addAll(routing.getRetSegments());

        //1.所有航段都是共享
        routing.setAllSegmentCodeShare(segmentList.stream().allMatch(segment -> (segment.getCodeShare())));
        //2.所有航段都是不是共享
        routing.setAllSegmentUnCodeShare(segmentList.stream().noneMatch(segment -> (segment.getCodeShare())));

        //3.判断航程中是否中转 todo 不支持多程
        if (routing.getRetSegments() != null && routing.getRetSegments().size() > 0) {
            if (routing.getFromSegments().size() > 1 && routing.getRetSegments().size() > 1) {
                routing.setTransitSign(PolicyConstans.ROUTING_TRANSFER_TYPE_TRANSFER);
            } else if (routing.getFromSegments().size() == 1 && routing.getRetSegments().size() == 1) {
                routing.setTransitSign(PolicyConstans.ROUTING_TRANSFER_TYPE_DIRECT);
            } else {
                routing.setTransitSign(PolicyConstans.ROUTING_TRANSFER_TYPE_MIX);
            }
        } else {
            if (routing.getFromSegments().size() > 1) {
                routing.setTransitSign(PolicyConstans.ROUTING_TRANSFER_TYPE_TRANSFER);
            } else {
                routing.setTransitSign(PolicyConstans.ROUTING_TRANSFER_TYPE_DIRECT);
            }
        }

        //得到共享航司的航段，将共享航司放入permitCodeShareAirline中
        List<SibeSegment> codeShareSegmentList = segmentList
            .stream()
            .filter(segment ->(segment.getCodeShare()))
            .collect(Collectors.toList());

        if(codeShareSegmentList.size() > 0){
            routing.setCodeShareFlag(PolicyConstans.PERMIT_TYPE_CONTAIN);
            routing.setPermitCodeShareAirline(codeShareSegmentList
                .stream()
                .map(segment ->(segment.getOperatingCarrier()))
                .collect(Collectors.toList()));
        }else {
            routing.setCodeShareFlag(PolicyConstans.PERMIT_TYPE_NO); // 非共享
        }


        //4. 得到混合航司的航段,将混合航司放入interlineAirline Set中
        List<SibeSegment> segmentSet = segmentList
            .stream()
            .filter(segment -> (!segment.getCarrier().equals(routing.getValidatingCarrier())))
            .collect(Collectors.toList());

        //混合航司的航段集合如果大于0则为：存在混合航司
        if (segmentSet.size() > 0) {
            routing.setInterlineSign(PolicyConstans.INTERLINE_SIGN_CONTAIN); //包含混航
            //将混合航司放入interlineAirline Set中
            routing.setInterlineAirline(segmentSet
                .stream()
                .map(segment -> (segment.getCarrier()))
                .collect(Collectors.toSet())
            );
        } else {
            routing.setInterlineSign(PolicyConstans.INTERLINE_SIGN_NO); //非混航
        }

        //5.设置中转点
        //根据航程序号分组，并放入flightIndicatorMap中
        Map<Integer, List<SibeSegment>> flightIndicatorMap =
            segmentList.stream().collect(Collectors.groupingBy(SibeSegment::getFlightIndicator));

        Set<String> transferPointSet = new HashSet<>();
        //循环flightIndicatorMap
        flightIndicatorMap.entrySet().forEach(
            segmentMap -> {
                //得到每一航程中最大的航段序号
                int maxItemNumber = segmentMap.getValue().stream().max(Comparator.comparing(segment -> segment.getItemNumber())).get().getItemNumber();
                int minItemNumber = segmentMap.getValue().stream().min(Comparator.comparing(segment -> segment.getItemNumber())).get().getItemNumber();
                //过滤掉每一航程中航段序号最大的航段，将其他航段的到达地存入routing.transferPoint; //中转点
               /* segmentMap.getValue()
                    .stream()
                    .filter(segment -> (segment.getItemNumber() != maxItemNumber))
                    .collect(Collectors.toList())
                    .forEach(
                        segment -> {
                            routing.getTransferPoint().add(segment.getArrAirport());
                        });*/
                 if(segmentMap.getValue().size() > 1) {
                     segmentMap.getValue().stream().forEach(segment -> {
                         if (segment.getItemNumber() == minItemNumber) {
                             transferPointSet.add(segment.getArrAirport());
                         }
                         if (segment.getItemNumber() == maxItemNumber) {
                             transferPointSet.add(segment.getDepAirport());
                         }
                         if (segment.getItemNumber() != minItemNumber && segment.getItemNumber() != maxItemNumber) {
                             transferPointSet.add(segment.getArrAirport());
                             transferPointSet.add(segment.getDepAirport());
                         }
                     });
                 }
            });
        routing.getTransferPoint().addAll(new ArrayList(transferPointSet));
    }

    /**
     * 生成优先级匹配条件list
     *
     * @return
     */
    public static List<Integer> generateConditionMatchPriorityList() {
        List<Integer> conditionMatchPriorityList = new ArrayList<>();
        conditionMatchPriorityList.add(SibeConstants.MP_TRIP_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_SEAT_CABIN);
        conditionMatchPriorityList.add(SibeConstants.MP_SEAT_GRADE);
        conditionMatchPriorityList.add(SibeConstants.MP_PRICE_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_FLIGHT_AVAILABLE);
        conditionMatchPriorityList.add(SibeConstants.MP_FLIGHT_PROHIBITED);
        conditionMatchPriorityList.add(SibeConstants.MP_TRANSFER_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_TRANSFER_POINT);
        conditionMatchPriorityList.add(SibeConstants.MP_INTERLINE_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_CODE_SHARE_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_INTERLINE_AIRLINE);
        conditionMatchPriorityList.add(SibeConstants.MP_INTERLINE_AIRLINE_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_OUTBOUND_DATE);
        conditionMatchPriorityList.add(SibeConstants.MP_INBOUND_DATE);
        conditionMatchPriorityList.add(SibeConstants.MP_OUTBOUND_DATE_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_INBOUND_DATE_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_OUTBOUND_DAY_TIME);
        conditionMatchPriorityList.add(SibeConstants.MP_INBOUND_DAY_TIME);
        conditionMatchPriorityList.add(SibeConstants.MP_SALE_DATE);
        conditionMatchPriorityList.add(SibeConstants.MP_ADVANCE_SALE_DAY);
        conditionMatchPriorityList.add(SibeConstants.MP_CODE_SHARE_AIRLINE);
        conditionMatchPriorityList.add(SibeConstants.MP_CODE_SHARE_AIRLINE_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_SEAT_CABIN_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_BOOK_GDS_CHANNEL);
        conditionMatchPriorityList.add(SibeConstants.MP_RET_MIN_AND_MAX_TIME);
        return conditionMatchPriorityList;
    }

    /**
     * 生成优先级匹配条件list
     *
     * @return
     */
    public static List<Integer> generateConditionMatchPriorityListForKSeat() {
        List<Integer> conditionMatchPriorityList = new ArrayList<>();
        conditionMatchPriorityList.add(SibeConstants.MP_TRIP_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_SEAT_CABIN);
        conditionMatchPriorityList.add(SibeConstants.MP_SEAT_GRADE);
        conditionMatchPriorityList.add(SibeConstants.MP_PRICE_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_FLIGHT_AVAILABLE);
        conditionMatchPriorityList.add(SibeConstants.MP_FLIGHT_PROHIBITED);
        conditionMatchPriorityList.add(SibeConstants.MP_TRANSFER_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_TRANSFER_POINT);
        conditionMatchPriorityList.add(SibeConstants.MP_INTERLINE_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_CODE_SHARE_TYPE);
        conditionMatchPriorityList.add(SibeConstants.MP_INTERLINE_AIRLINE);
        conditionMatchPriorityList.add(SibeConstants.MP_INTERLINE_AIRLINE_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_OUTBOUND_DATE);
        conditionMatchPriorityList.add(SibeConstants.MP_INBOUND_DATE);
        conditionMatchPriorityList.add(SibeConstants.MP_OUTBOUND_DATE_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_INBOUND_DATE_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_OUTBOUND_DAY_TIME);
        conditionMatchPriorityList.add(SibeConstants.MP_INBOUND_DAY_TIME);
        conditionMatchPriorityList.add(SibeConstants.MP_SALE_DATE);
        conditionMatchPriorityList.add(SibeConstants.MP_ADVANCE_SALE_DAY);
        conditionMatchPriorityList.add(SibeConstants.MP_CODE_SHARE_AIRLINE);
        conditionMatchPriorityList.add(SibeConstants.MP_CODE_SHARE_AIRLINE_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_SEAT_CABIN_EXCEPT);
        conditionMatchPriorityList.add(SibeConstants.MP_BOOK_GDS_CHANNEL);
        conditionMatchPriorityList.add(SibeConstants.MP_RET_MIN_AND_MAX_TIME);
        return conditionMatchPriorityList;
    }

    /**
     * 判断某个行程是否为中转，是返回true，不是中转返回false
     *
     * @param segmentList
     * @return
     */
    private boolean transitValidate(List<Segment> segmentList) {
        if (segmentList.size() == 1) {
            return false;
        } else if (segmentList.size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成细节政策主key，key格式为 prefix_平台_站点_产品类型_航司
     *
     * @param otaCode
     * @param otaSiteCode
     * @param productType
     * @param airline
     * @return
     */
    private String generateMasterKey(String otaCode, String otaSiteCode, String productType, String airline) {
        StringBuilder builder = new StringBuilder();
        builder.append(PolicyConstans.POLICY_CACHE_PREFIX);
        builder.append(otaCode).append("_");
        builder.append(otaSiteCode).append("_");
        builder.append(productType).append("_");
        builder.append(airline);
        return builder.toString();
    }

    //计算汇率后的价格
    public static BigDecimal getBigGDStoOTA(Integer num,BigDecimal rate){
        if( null == num){
            return  null ;
        }else{
            return BigDecimal.valueOf(num).multiply(rate) ;
        }
    }
}
