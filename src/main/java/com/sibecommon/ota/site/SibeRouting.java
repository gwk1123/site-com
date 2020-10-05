package com.sibecommon.ota.site;


import com.sibecommon.utils.compression.CompressUtil;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by yangdehua on 18/3/11.
 */
public class SibeRouting implements Serializable{

    private static final long serialVersionUID = 1587422156784438657L;

    //航班方案序号
    private Integer id;

    //GDS返回 成人公布价（以CNY为单位），不含税【文档所有跟价格、税费相关的参数，都是以人民币为单位】
    private Integer publishPriceGDS;

    //GDS返回 成人单价，不含税
    private Integer  adultPriceGDS;

    //GDS返回 成人税费【注意不能是0，若存在为0的情况，请与我们联系】
    private Integer  adultTaxGDS;

    //GDS返回 儿童公布价，不含税
    private Integer childPublishPriceGDS;

    //GDS返回 儿童公布价，不含税
    private Integer  childPriceGDS;

    //GDS返回 儿童税费 1）对于含儿童的查询，必须返回；2）不能是0，若存在为0的情况，请与我们联系。
    private Integer  childTaxGDS;

    //GDS返回 婴儿公布价
    private Integer infantPublishPriceGDS;

    //GDS返回 婴儿单价，不含税【对于含婴儿的查询，必须返回】
    private Integer infantPriceGDS;

    //GDS返回 婴儿税费1）对于含婴儿的查询，必须返回；2）可以为0。
    private Integer infantTaxGDS;

    //成人税费类型：0 未含税 / 1 已含税 【正常赋0，如赋1请提前告知】
    private Integer  adultTaxType;

    //儿童税费类型：0 未含税 / 1 已含税 【正常赋0，如赋1请提前告知】
    private Integer  childTaxType;

    //报价类型:0 普通价 / 1 留学生价 /3-劳工/4-新移民 /5-海员/6-老人/7-青年 【请全部赋0】
    private Integer  priceType;

    //报价类型：0 预定价 / 1 申请价 【请全部赋0】
    private Integer  applyType;

    //汇率
    private BigDecimal exchange;

    //适用年龄区间【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
    // 1）使用“-”表示“至”，例如*-12，表示12岁及以下；
    // 2）置空表示无限制
    private String adultAgeRestriction;

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
    private String eligibility;

    /*
     * 允许国籍，使用标准国家二字码
     * 【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
     * 1）置空表示无限制（一般都是置空的）；
     * 2）若多个使用“/”分割；
     * 3）与forbiddenNationality只能2选1，若同时出现，为非法数据，将被过滤。
     */
    private String nationality;

    /*禁用国籍，使用标准国家二字码
     * 【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
     * 1）置空表示无限制（一般都是置空的）；
     * 2）若多个使用“/”分割；
     * 3）与nationality只能2选1，若同时出现，为非法数据，将被过滤。
     */
    private String forbiddenNationality;

    //
    private Integer planCategory;

    //报销凭证：1 行程单 /2 旅游发票 /3 境外电子凭证 默认发票
    private String invoiceType;

    //最短停留时间【单位是天】【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
    private String minStay;

    //最长停留时间【单位是天】【如要使用此字段请提前通知我们，盲目使用会影响价格展示】
    private String maxStay;

    //最小出行人数【无返回，默认为1】
    private Integer minPassengerCount;

    //最大出行人数【无返回，默认为9】
    private Integer maxPassengerCount;

    //订位Office号【可为空】
    private String bookingOfficeNo;

    //出票Office号【可为空】
    private String ticketingOfficeNo;

    //【公布运价强校验】出票航司
    // 1）整个行程只能赋一个航司；
    // 2）如不赋值会取行程第一航段的carrier作为出票航司；
    // 3）此字段非常重要，请务必准确赋值。
    private String validatingCarrier;

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
    private String reservationType;

    //【公布运价强校验】运价类型
    // 1）PUB: 公布运价；
    // 2）PRV: 私有运价
    // 3）LCC: 廉航运价。
    private String fareType;

    //【公布运价强校验】每航段1个，使用“ ; ”分割
    private String fareBasis;


    //去程航段按顺序 多程行程信息全部输出到此节点
    private List<SibeSegment> fromSegments;

    //多程行程信息全部输出到此节点
    private List<SibeSegment>  retSegments;

    //免费行李额信息、退改签信息

    /*
     * GDS 退改签信息及行李额信息
     */
    private SibeRule rule;


    //----------------政策中添加的属性----

    //中转标志
    private Integer transitSign;

    //中转点
    private List<String> transferPoint;

    //混合航司标志
    private Integer interlineSign;

    //行程中的混合航司set
    private Set<String> interlineAirline;

    //所有航段共享标志
    private boolean isAllSegmentCodeShare;

    //所有航段非共享标志
    private boolean isAllSegmentUnCodeShare;

    //销售儿童票价
    private Integer childPriceCNY1;
    private Integer childPriceOTA;

    //销售儿童税费
    private Integer childTaxCNY1;
    private Integer childTaxOTA;

    //销售儿童票价
    private Integer infantsPriceCNY1;
    private Integer infantsPriceOTA;

    //销售儿童税费
    private Integer infantsTaxCNY1;
    private Integer infantsTaxOTA;

    //销售成人票价
    private Integer adultPriceCNY1;
    private Integer adultPriceOTA;

    //销售婴儿税费
    private Integer adultTaxCNY1;
    private Integer adultTaxOTA;

    //Office ID
    private String officeId;

    //uid
    private String uid;

    //GDS币种到人民币汇率
    private BigDecimal gdsToCNYRate;

    //GDS币种转换成OTA汇率
    private BigDecimal gdsToOTARate;

    //可保存必要信息，验价时会放在请求报文中传给供应商；最大 1000 个字符 。
    private SibeRoutingData  sibeRoutingData;

    //LCC的flightId
    private String flightId;

    //LCC的订单总价
    private BigDecimal totalFare;

    //LCC 客户端能接受的订单总价
    private BigDecimal confirmedPrice;

    //LCC 生单时是否需要乘客护照号码的布尔标志，此参数由verify接口返回
    private Boolean passportNoRequired;

    //LCC 生单时是否需要乘客护照详细信息的布尔标志，此参数由verify接口返回
    private Boolean passportDetailsRequired;

    //LCC 此预订的最低信用卡到期日期(格式：yyyy-MM-dd’T’HH:mm:ss)，该参数由verify接口返回
    private String expiryDateCC;

    //行程中的共享航司
    private List<String> permitCodeShareAirline;

    //行程中的共享航司标志位
    private Integer codeShareFlag;

    //30.VJ 相当于data作用，用于保存后续请求需要的信息，单程长度约650个字符，往返长度为单程两倍
    private String bookingKey;

    //data 今通:可保存必要信息，验价时需要原值回传
    private String data;

    //GDS币种
    private String currencyGDS ;
    //OTA币种
    private String currencyOTA ;

    private LocalDateTime lastTicketingDate ;

    private  Long gdsCacheTimeLapse;

    //用于记录生成该方案的GDS请求时间
    public Long getGdsCacheTimeLapse() {
        return gdsCacheTimeLapse;
    }

    public void setGdsCacheTimeLapse(Long gdsCacheTimeLapse) {
        this.gdsCacheTimeLapse = gdsCacheTimeLapse;
    }

    public LocalDateTime getLastTicketingDate() {
        return lastTicketingDate;
    }

    public void setLastTicketingDate(LocalDateTime lastTicketingDate) {
        this.lastTicketingDate = lastTicketingDate;
    }

    public String getCurrencyOTA() {
        return currencyOTA;
    }

    public void setCurrencyOTA(String currencyOTA) {
        this.currencyOTA = currencyOTA;
    }

    public String getCurrencyGDS() {
        return currencyGDS;
    }

    public void setCurrencyGDS(String currencyGDS) {
        this.currencyGDS = currencyGDS;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBookingKey() {
        return bookingKey;
    }

    public void setBookingKey(String bookingKey) {
        this.bookingKey = bookingKey;
    }

    public Integer getCodeShareFlag() {
        return codeShareFlag;
    }

    public void setCodeShareFlag(Integer codeShareFlag) {
        this.codeShareFlag = codeShareFlag;
    }

    public List<String> getPermitCodeShareAirline() {
        return permitCodeShareAirline;
    }

    public void setPermitCodeShareAirline(List<String> permitCodeShareAirline) {
        this.permitCodeShareAirline = permitCodeShareAirline;
    }

    /**
     * Deep copy sibe routing.
     *
     * @param originSibeRouting the origin sibe routing
     * @return the sibe routing
     */
    public static SibeRouting deepCopy(SibeRouting originSibeRouting) {
        SibeRouting sibeRouting = null;
        ByteArrayOutputStream byteArrayOutputStream = null ;
        ObjectOutputStream oos = null ;
        ByteArrayInputStream bais = null ;
        ObjectInputStream ois = null ;
        try {
            // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
            byteArrayOutputStream = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(originSibeRouting);
            //将流序列化成对象
            bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ois = new ObjectInputStream(bais);
            sibeRouting = (SibeRouting) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            CompressUtil.closeStream(ois);
            CompressUtil.closeStream(bais);
            CompressUtil.closeStream(oos);
            CompressUtil.closeStream(byteArrayOutputStream);
        }
        return sibeRouting;
    }


    public Integer getChildTaxOTA() {
        return childTaxOTA;
    }

    public void setChildTaxOTA(Integer childTaxOTA) {
        this.childTaxOTA = childTaxOTA;
    }

    public Integer getInfantsPriceOTA() {
        return infantsPriceOTA;
    }

    public void setInfantsPriceOTA(Integer infantsPriceOTA) {
        this.infantsPriceOTA = infantsPriceOTA;
    }

    public Integer getInfantsTaxOTA() {
        return infantsTaxOTA;
    }

    public void setInfantsTaxOTA(Integer infantsTaxOTA) {
        this.infantsTaxOTA = infantsTaxOTA;
    }

    public Integer getAdultPriceOTA() {
        return adultPriceOTA;
    }

    public void setAdultPriceOTA(Integer adultPriceOTA) {
        this.adultPriceOTA = adultPriceOTA;
    }

    public Integer getAdultTaxOTA() {
        return adultTaxOTA;
    }

    public void setAdultTaxOTA(Integer adultTaxOTA) {
        this.adultTaxOTA = adultTaxOTA;
    }

//----------------政策中添加的属性----

    /**
     * Gets sibe routing data.
     *
     * @return the sibe routing data
     */
    public SibeRoutingData getSibeRoutingData() {
        if(sibeRoutingData==null){
            sibeRoutingData=new SibeRoutingData();
        }
        return sibeRoutingData;
    }


    /**
     * Sets sibe routing data.
     *
     * @param sibeRoutingData the sibe routing data
     */
    public void setSibeRoutingData(SibeRoutingData sibeRoutingData) {
        this.sibeRoutingData = sibeRoutingData;
    }

    /**
     * Gets publish price gds.
     *
     * @return the publish price gds
     */
    public Integer getPublishPriceGDS() {
        return publishPriceGDS;
    }

    /**
     * Sets publish price gds.
     *
     * @param publishPriceGDS the publish price gds
     */
    public void setPublishPriceGDS(Integer publishPriceGDS) {
        this.publishPriceGDS = publishPriceGDS;
    }

    /**
     * Gets adult price gds.
     *
     * @return the adult price gds
     */
    public Integer getAdultPriceGDS() {
        return adultPriceGDS;
    }

    /**
     * Sets adult price gds.
     *
     * @param adultPriceGDS the adult price gds
     */
    public void setAdultPriceGDS(Integer adultPriceGDS) {
        this.adultPriceGDS = adultPriceGDS;
    }

    /**
     * Gets adult tax gds.
     *
     * @return the adult tax gds
     */
    public Integer getAdultTaxGDS() {
        return adultTaxGDS;
    }

    /**
     * Sets adult tax gds.
     *
     * @param adultTaxGDS the adult tax gds
     */
    public void setAdultTaxGDS(Integer adultTaxGDS) {
        this.adultTaxGDS = adultTaxGDS;
    }

    /**
     * Gets child publish price gds.
     *
     * @return the child publish price gds
     */
    public Integer getChildPublishPriceGDS() {
        return childPublishPriceGDS;
    }

    /**
     * Sets child publish price gds.
     *
     * @param childPublishPriceGDS the child publish price gds
     */
    public void setChildPublishPriceGDS(Integer childPublishPriceGDS) {
        this.childPublishPriceGDS = childPublishPriceGDS;
    }

    /**
     * Gets child price gds.
     *
     * @return the child price gds
     */
    public Integer getChildPriceGDS() {
        return childPriceGDS;
    }

    /**
     * Sets child price gds.
     *
     * @param childPriceGDS the child price gds
     */
    public void setChildPriceGDS(Integer childPriceGDS) {
        this.childPriceGDS = childPriceGDS;
    }

    /**
     * Gets child tax gds.
     *
     * @return the child tax gds
     */
    public Integer getChildTaxGDS() {
        return childTaxGDS;
    }

    /**
     * Sets child tax gds.
     *
     * @param childTaxGDS the child tax gds
     */
    public void setChildTaxGDS(Integer childTaxGDS) {
        this.childTaxGDS = childTaxGDS;
    }

    /**
     * Gets infant publish price gds.
     *
     * @return the infant publish price gds
     */
    public Integer getInfantPublishPriceGDS() {
        return infantPublishPriceGDS;
    }

    /**
     * Sets infant publish price gds.
     *
     * @param infantPublishPriceGDS the infant publish price gds
     */
    public void setInfantPublishPriceGDS(Integer infantPublishPriceGDS) {
        this.infantPublishPriceGDS = infantPublishPriceGDS;
    }

    /**
     * Gets infant price gds.
     *
     * @return the infant price gds
     */
    public Integer getInfantPriceGDS() {
        return infantPriceGDS;
    }

    /**
     * Sets infant price gds.
     *
     * @param infantPriceGDS the infant price gds
     */
    public void setInfantPriceGDS(Integer infantPriceGDS) {
        this.infantPriceGDS = infantPriceGDS;
    }

    /**
     * Gets infant tax gds.
     *
     * @return the infant tax gds
     */
    public Integer getInfantTaxGDS() {
        return infantTaxGDS;
    }

    /**
     * Sets infant tax gds.
     *
     * @param infantTaxGDS the infant tax gds
     */
    public void setInfantTaxGDS(Integer infantTaxGDS) {
        this.infantTaxGDS = infantTaxGDS;
    }

    /**
     * Gets adult tax type.
     *
     * @return the adult tax type
     */
    public Integer getAdultTaxType() {
        return adultTaxType;
    }

    /**
     * Sets adult tax type.
     *
     * @param adultTaxType the adult tax type
     */
    public void setAdultTaxType(Integer adultTaxType) {
        this.adultTaxType = adultTaxType;
    }

    /**
     * Gets child tax type.
     *
     * @return the child tax type
     */
    public Integer getChildTaxType() {
        return childTaxType;
    }

    /**
     * Sets child tax type.
     *
     * @param childTaxType the child tax type
     */
    public void setChildTaxType(Integer childTaxType) {
        this.childTaxType = childTaxType;
    }

    /**
     * Gets price type.
     *
     * @return the price type
     */
    public Integer getPriceType() {
        return priceType;
    }

    /**
     * Sets price type.
     *
     * @param priceType the price type
     */
    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    /**
     * Gets apply type.
     *
     * @return the apply type
     */
    public Integer getApplyType() {
        return applyType;
    }

    /**
     * Sets apply type.
     *
     * @param applyType the apply type
     */
    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    /**
     * Gets exchange.
     *
     * @return the exchange
     */
    public BigDecimal getExchange() {
        return exchange;
    }

    /**
     * Sets exchange.
     *
     * @param exchange the exchange
     */
    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }

    /**
     * Gets adult age restriction.
     *
     * @return the adult age restriction
     */
    public String getAdultAgeRestriction() {
        return adultAgeRestriction;
    }

    /**
     * Sets adult age restriction.
     *
     * @param adultAgeRestriction the adult age restriction
     */
    public void setAdultAgeRestriction(String adultAgeRestriction) {
        this.adultAgeRestriction = adultAgeRestriction;
    }

    /**
     * Gets eligibility.
     *
     * @return the eligibility
     */
    public String getEligibility() {
        return eligibility;
    }

    /**
     * Sets eligibility.
     *
     * @param eligibility the eligibility
     */
    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    /**
     * Gets nationality.
     *
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets nationality.
     *
     * @param nationality the nationality
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * Gets forbidden nationality.
     *
     * @return the forbidden nationality
     */
    public String getForbiddenNationality() {
        return forbiddenNationality;
    }

    /**
     * Sets forbidden nationality.
     *
     * @param forbiddenNationality the forbidden nationality
     */
    public void setForbiddenNationality(String forbiddenNationality) {
        this.forbiddenNationality = forbiddenNationality;
    }

    /**
     * Gets plan category.
     *
     * @return the plan category
     */
    public Integer getPlanCategory() {
        return planCategory;
    }

    /**
     * Sets plan category.
     *
     * @param planCategory the plan category
     */
    public void setPlanCategory(Integer planCategory) {
        this.planCategory = planCategory;
    }

    /**
     * Gets invoice type.
     *
     * @return the invoice type
     */
    public String getInvoiceType() {
        return invoiceType;
    }

    /**
     * Sets invoice type.
     *
     * @param invoiceType the invoice type
     */
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     * Gets min stay.
     *
     * @return the min stay
     */
    public String getMinStay() {
        return minStay;
    }

    /**
     * Sets min stay.
     *
     * @param minStay the min stay
     */
    public void setMinStay(String minStay) {
        this.minStay = minStay;
    }

    /**
     * Gets max stay.
     *
     * @return the max stay
     */
    public String getMaxStay() {
        return maxStay;
    }

    /**
     * Sets max stay.
     *
     * @param maxStay the max stay
     */
    public void setMaxStay(String maxStay) {
        this.maxStay = maxStay;
    }

    /**
     * Gets min passenger count.
     *
     * @return the min passenger count
     */
    public Integer getMinPassengerCount() {
        return minPassengerCount;
    }

    /**
     * Sets min passenger count.
     *
     * @param minPassengerCount the min passenger count
     */
    public void setMinPassengerCount(Integer minPassengerCount) {
        this.minPassengerCount = minPassengerCount;
    }

    /**
     * Gets max passenger count.
     *
     * @return the max passenger count
     */
    public Integer getMaxPassengerCount() {
        return maxPassengerCount;
    }

    /**
     * Sets max passenger count.
     *
     * @param maxPassengerCount the max passenger count
     */
    public void setMaxPassengerCount(Integer maxPassengerCount) {
        this.maxPassengerCount = maxPassengerCount;
    }

    /**
     * Gets booking office no.
     *
     * @return the booking office no
     */
    public String getBookingOfficeNo() {
        return bookingOfficeNo;
    }

    /**
     * Sets booking office no.
     *
     * @param bookingOfficeNo the booking office no
     */
    public void setBookingOfficeNo(String bookingOfficeNo) {
        this.bookingOfficeNo = bookingOfficeNo;
    }

    /**
     * Gets ticketing office no.
     *
     * @return the ticketing office no
     */
    public String getTicketingOfficeNo() {
        return ticketingOfficeNo;
    }

    /**
     * Sets ticketing office no.
     *
     * @param ticketingOfficeNo the ticketing office no
     */
    public void setTicketingOfficeNo(String ticketingOfficeNo) {
        this.ticketingOfficeNo = ticketingOfficeNo;
    }

    /**
     * Gets validating carrier.
     *
     * @return the validating carrier
     */
    public String getValidatingCarrier() {
        return validatingCarrier;
    }

    /**
     * Sets validating carrier.
     *
     * @param validatingCarrier the validating carrier
     */
    public void setValidatingCarrier(String validatingCarrier) {
        this.validatingCarrier = validatingCarrier;
    }

    /**
     * Gets reservation type.
     *
     * @return the reservation type
     */
    public String getReservationType() {
        return reservationType;
    }

    /**
     * Sets reservation type.
     *
     * @param reservationType the reservation type
     */
    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    /**
     * Gets fare type.
     *
     * @return the fare type
     */
    public String getFareType() {
        return fareType;
    }

    /**
     * Sets fare type.
     *
     * @param fareType the fare type
     */
    public void setFareType(String fareType) {
        this.fareType = fareType;
    }

    /**
     * Gets fare basis.
     *
     * @return the fare basis
     */
    public String getFareBasis() {
        return fareBasis;
    }

    /**
     * Sets fare basis.
     *
     * @param fareBasis the fare basis
     */
    public void setFareBasis(String fareBasis) {
        this.fareBasis = fareBasis;
    }

    /**
     * Gets from segments.
     *
     * @return the from segments
     */
    public List<SibeSegment> getFromSegments() {
        if (fromSegments == null) {
            fromSegments = new ArrayList<>();
        }
        return this.fromSegments;
    }

    /**
     * Sets from segments.
     *
     * @param fromSegments the from segments
     */
    public void setFromSegments(List<SibeSegment> fromSegments) {
        this.fromSegments = fromSegments;
    }

    /**
     * Gets ret segments.
     *
     * @return the ret segments
     */
    public List<SibeSegment> getRetSegments() {
        if (retSegments == null) {
            retSegments = new ArrayList<>();
        }
        return this.retSegments;
    }

    /**
     * Sets ret segments.
     *
     * @param retSegments the ret segments
     */
    public void setRetSegments(List<SibeSegment> retSegments) {
        this.retSegments = retSegments;
    }

    /**
     * Gets rule.
     *
     * @return the rule
     */
    public SibeRule getRule() {
        if(rule==null){
            rule = new SibeRule();
        }
        return rule;
    }

    /**
     * Sets rule.
     *
     * @param rule the rule
     */
    public void setRule(SibeRule rule) {
        this.rule = rule;
    }


    /**
     * Gets transit sign.
     *
     * @return the transit sign
     */
    public Integer getTransitSign() {
        return transitSign;
    }

    /**
     * Sets transit sign.
     *
     * @param transitSign the transit sign
     */
    public void setTransitSign(Integer transitSign) {
        this.transitSign = transitSign;
    }

    /**
     * Gets transfer point.
     *
     * @return the transfer point
     */
    public List<String> getTransferPoint() {
        if (transferPoint == null) {
            transferPoint=new ArrayList<>();
        }
        return this.transferPoint;
    }

    /**
     * Sets transfer point.
     *
     * @param transferPoint the transfer point
     */
    public void setTransferPoint(List<String> transferPoint) {
        this.transferPoint = transferPoint;
    }

    /**
     * Gets interline sign.
     *
     * @return the interline sign
     */
    public Integer getInterlineSign() {
        return interlineSign;
    }

    /**
     * Sets interline sign.
     *
     * @param interlineSign the interline sign
     */
    public void setInterlineSign(Integer interlineSign) {
        this.interlineSign = interlineSign;
    }

    /**
     * Gets interline airline.
     *
     * @return the interline airline
     */
    public Set<String> getInterlineAirline() {
        return interlineAirline;
    }

    /**
     * Sets interline airline.
     *
     * @param interlineAirline the interline airline
     */
    public void setInterlineAirline(Set<String> interlineAirline) {
        this.interlineAirline = interlineAirline;
    }

    /**
     * Is all segment code share boolean.
     *
     * @return the boolean
     */
    public boolean isAllSegmentCodeShare() {
        return isAllSegmentCodeShare;
    }

    /**
     * Sets all segment code share.
     *
     * @param allSegmentCodeShare the all segment code share
     */
    public void setAllSegmentCodeShare(boolean allSegmentCodeShare) {
        isAllSegmentCodeShare = allSegmentCodeShare;
    }

    /**
     * Is all segment un code share boolean.
     *
     * @return the boolean
     */
    public boolean isAllSegmentUnCodeShare() {
        return isAllSegmentUnCodeShare;
    }

    /**
     * Sets all segment un code share.
     *
     * @param allSegmentUnCodeShare the all segment un code share
     */
    public void setAllSegmentUnCodeShare(boolean allSegmentUnCodeShare) {
        isAllSegmentUnCodeShare = allSegmentUnCodeShare;
    }

    public Integer getChildPriceCNY1() {
        return childPriceCNY1;
    }

    public void setChildPriceCNY1(Integer childPriceCNY1) {
        this.childPriceCNY1 = childPriceCNY1;
    }

    public Integer getChildTaxCNY1() {
        return childTaxCNY1;
    }

    public void setChildTaxCNY1(Integer childTaxCNY1) {
        this.childTaxCNY1 = childTaxCNY1;
    }

    public Integer getInfantsPriceCNY1() {
        return infantsPriceCNY1;
    }

    public void setInfantsPriceCNY1(Integer infantsPriceCNY1) {
        this.infantsPriceCNY1 = infantsPriceCNY1;
    }

    public Integer getInfantsTaxCNY1() {
        return infantsTaxCNY1;
    }

    public void setInfantsTaxCNY1(Integer infantsTaxCNY1) {
        this.infantsTaxCNY1 = infantsTaxCNY1;
    }

    public Integer getAdultPriceCNY1() {
        return adultPriceCNY1;
    }

    public void setAdultPriceCNY1(Integer adultPriceCNY1) {
        this.adultPriceCNY1 = adultPriceCNY1;
    }

    public Integer getAdultTaxCNY1() {
        return adultTaxCNY1;
    }

    public void setAdultTaxCNY1(Integer adultTaxCNY1) {
        this.adultTaxCNY1 = adultTaxCNY1;
    }

    /**
     * Gets office id.
     *
     * @return the office id
     */
    public String getOfficeId() {
        return officeId;
    }

    /**
     * Sets office id.
     *
     * @param officeId the office id
     */
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public BigDecimal getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(BigDecimal totalFare) {
        this.totalFare = totalFare;
    }

    public BigDecimal getConfirmedPrice() {
        return confirmedPrice;
    }

    public void setConfirmedPrice(BigDecimal confirmedPrice) {
        this.confirmedPrice = confirmedPrice;
    }

    public Boolean getPassportNoRequired() {
        return passportNoRequired;
    }

    public void setPassportNoRequired(Boolean passportNoRequired) {
        this.passportNoRequired = passportNoRequired;
    }

    public Boolean getPassportDetailsRequired() {
        return passportDetailsRequired;
    }

    public void setPassportDetailsRequired(Boolean passportDetailsRequired) {
        this.passportDetailsRequired = passportDetailsRequired;
    }

    public String getExpiryDateCC() {
        return expiryDateCC;
    }

    public void setExpiryDateCC(String expiryDateCC) {
        this.expiryDateCC = expiryDateCC;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (fromSegments != null ? fromSegments.hashCode() : 0);
        result = 31 * result + (retSegments != null ? retSegments.hashCode() : 0);
        return result;
    }

    public Integer getChildPriceOTA() {
        return childPriceOTA;
    }

    public void setChildPriceOTA(Integer childPriceOTA) {
        this.childPriceOTA = childPriceOTA;
    }

    public BigDecimal getGdsToCNYRate() {
        return gdsToCNYRate;
    }

    public void setGdsToCNYRate(BigDecimal gdsToCNYRate) {
        this.gdsToCNYRate = gdsToCNYRate;
    }

    public BigDecimal getGdsToOTARate() {
        return gdsToOTARate;
    }

    public void setGdsToOTARate(BigDecimal gdsToOTARate) {
        this.gdsToOTARate = gdsToOTARate;
    }
}
