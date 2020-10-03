package com.sibecommon.ota.gds;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sibecommon.utils.compression.CompressUtil;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdehua on 10/26/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Routing implements Serializable {

    private static final long serialVersionUID = -7132780016171046894L;
    private int id;
    private BigDecimal adultPrice;
    private BigDecimal adultTax;
    private String adultAgeRestriction;
    private BigDecimal childPrice;
    private BigDecimal childTax;
    private BigDecimal infantsPrice;
    private BigDecimal infantsTax;

    //汇率
    private BigDecimal exchange;
    private String eligibility;
    private String nationality;
    private String forbiddenNationality;
    private int priceType;
    private int applyType;
    private int adultTaxType;
    private int childTaxType;
    private String reservationType;
    private String productType;
    private String officeId;

    private String minStay;
    private String maxStay;
    private int minPassengerCount;
    private int maxPassengerCount;
    private String fareBasis;
    private String validatingCarrier;
    private AirlineAncillaries airlineAncillaries;
    private Rule rule;
    private List<Segment> fromSegments;
    private List<Segment> retSegments;
    private String pricingMethod;

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
//
//    //LCC 航司支持的支付方式和手续费
//    private List<PayOptions> payOptions;

    //VJ(专用) 相当于data作用，用于保存后续请求需要的信息，单程长度约650个字符，往返长度为单程两倍
    private String bookingKey;

    //今通:可保存必要信息，验价时需要原值回传
    private String data;

    //币种
    private String currency ;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    //深度复制方法,需要对象及对象所有的对象属性都实现序列化　
    public Routing myclone() {
        Routing outer = null;
        ByteArrayOutputStream baos = null ;
        ObjectOutputStream oos = null ;
        ByteArrayInputStream bais = null ;
        ObjectInputStream ois = null ;
        try {
            // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
             baos = new ByteArrayOutputStream();
             oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            // 将流序列化成对象
             bais = new ByteArrayInputStream(baos.toByteArray());
             ois = new ObjectInputStream(bais);
            outer = (Routing) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            CompressUtil.closeStream(ois);
            CompressUtil.closeStream(bais);
            CompressUtil.closeStream(oos);
            CompressUtil.closeStream(baos);
        }
        return outer;
    }


    public List<Segment> getFromSegments() {

        if (fromSegments == null) {
            fromSegments = new ArrayList<Segment>();
        }
        return this.fromSegments;
    }


    public List<Segment> getRetSegments() {
        if (retSegments == null) {
            retSegments = new ArrayList<Segment>();
        }
        return this.retSegments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getAdultAgeRestriction() {
        return adultAgeRestriction;
    }

    public void setAdultAgeRestriction(String adultAgeRestriction) {
        this.adultAgeRestriction = adultAgeRestriction;
    }

    public BigDecimal getExchange() {
        return exchange;
    }

    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getForbiddenNationality() {
        return forbiddenNationality;
    }

    public void setForbiddenNationality(String forbiddenNationality) {
        this.forbiddenNationality = forbiddenNationality;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public int getAdultTaxType() {
        return adultTaxType;
    }

    public void setAdultTaxType(int adultTaxType) {
        this.adultTaxType = adultTaxType;
    }

    public int getChildTaxType() {
        return childTaxType;
    }

    public void setChildTaxType(int childTaxType) {
        this.childTaxType = childTaxType;
    }

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getMinStay() {
        return minStay;
    }

    public void setMinStay(String minStay) {
        this.minStay = minStay;
    }

    public String getMaxStay() {
        return maxStay;
    }

    public void setMaxStay(String maxStay) {
        this.maxStay = maxStay;
    }

    public int getMinPassengerCount() {
        return minPassengerCount;
    }

    public void setMinPassengerCount(int minPassengerCount) {
        this.minPassengerCount = minPassengerCount;
    }

    public int getMaxPassengerCount() {
        return maxPassengerCount;
    }

    public void setMaxPassengerCount(int maxPassengerCount) {
        this.maxPassengerCount = maxPassengerCount;
    }

    public String getFareBasis() {
        return fareBasis;
    }

    public void setFareBasis(String fareBasis) {
        this.fareBasis = fareBasis;
    }

    public String getValidatingCarrier() {
        return validatingCarrier;
    }

    public void setValidatingCarrier(String validatingCarrier) {
        this.validatingCarrier = validatingCarrier;
    }

    public AirlineAncillaries getAirlineAncillaries() {
        return airlineAncillaries;
    }

    public void setAirlineAncillaries(AirlineAncillaries airlineAncillaries) {
        this.airlineAncillaries = airlineAncillaries;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public void setFromSegments(List<Segment> fromSegments) {
        this.fromSegments = fromSegments;
    }

    public void setRetSegments(List<Segment> retSegments) {
        this.retSegments = retSegments;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPricingMethod() {
        return pricingMethod;
    }

    public void setPricingMethod(String pricingMethod) {
        this.pricingMethod = pricingMethod;
    }

    public BigDecimal getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(BigDecimal adultPrice) {
        this.adultPrice = adultPrice;
    }

    public BigDecimal getAdultTax() {
        return adultTax;
    }

    public void setAdultTax(BigDecimal adultTax) {
        this.adultTax = adultTax;
    }

    public BigDecimal getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(BigDecimal childPrice) {
        this.childPrice = childPrice;
    }

    public BigDecimal getChildTax() {
        return childTax;
    }

    public void setChildTax(BigDecimal childTax) {
        this.childTax = childTax;
    }

    public BigDecimal getInfantsPrice() {
        return infantsPrice;
    }

    public void setInfantsPrice(BigDecimal infantsPrice) {
        this.infantsPrice = infantsPrice;
    }

    public BigDecimal getInfantsTax() {
        return infantsTax;
    }

    public void setInfantsTax(BigDecimal infantsTax) {
        this.infantsTax = infantsTax;
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
//
//    public List<PayOptions> getPayOptions() {
//        return payOptions;
//    }
//
//    public void setPayOptions(List<PayOptions> payOptions) {
//        this.payOptions = payOptions;
//    }
}
