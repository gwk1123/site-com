package com.sibecommon.ota.gds;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by yangdehua on 10/26/16.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GDSSearchRequestDTO {

    // 访问方的唯一标识
    private String apiKey;

    // 行程类型，1：单程；2：往返；3：多段；
    private String tripType;

    // 出发地城市 IATA 三字码代码
    // 如果为多程，会按照 PEK/HKG，HKG/SHA 格式请求，第一程为北京->香港，第二程为香港->上海
    private String fromCity;

    //目的地城市 IATA 三字码代码
    private String toCity;

    //出发日期，格式为 YYYYMMDD
    //如果为多程，20130729,20130804方式传输数据
    @JsonFormat(pattern = "YYYYMMDD")
    private String fromDate;

    //回程日期，格式为 YYYYMMDD（留空表示单程/多程）
    @JsonFormat(pattern = "YYYYMMDD")
    private String retDate;

    // 成⼈人数，0-9
    private int adultNumber;

    //⼉童人数，0-9
    private int childNumber;
    // 婴儿
    private int infantNumber;


    // 舱位等级:
    // Y: 经济舱-Economy Class;
    // W: 经济特舱-Economy Class Premium;
    // M: 经济优惠舱-Economy Class Discounted
    // F: 头等舱-First Class;
    // C: 公务舱-Business Class;
    private String cabin;

    //航空公司
    private String airline;
    // 请求GDS平台
    // 1E：TravelSky
    // 1A：Amadeus
    // 1B：Abacus
    // 1S：Sabre
    // 1P：WorldSpan
    // 1G：Galileo
    private String reservationType;

    // 调用GDS时使用的对应账号（OfficeId或PCC）
    private String officeId;

    // 关联所有请求返回（原数据直接返回）
    private String uid;

    //请求返回记录最大数
    private int numberOfUnits;

    private String requestSource;

    //拒绝航空公司
    private String prohibitedCarriers;

    //飞行类型1：含中转含直飞2：仅仅中转3：仅直飞
    private Integer flightType;

    //目前是以港币
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getFlightType() {
        return flightType;
    }

    public void setFlightType(Integer flightType) {
        this.flightType = flightType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getRetDate() {
        return retDate;
    }

    public void setRetDate(String retDate) {
        this.retDate = retDate;
    }

    public int getAdultNumber() {
        return adultNumber;
    }

    public void setAdultNumber(int adultNumber) {
        this.adultNumber = adultNumber;
    }

    public int getChildNumber() {
        return childNumber;
    }

    public void setChildNumber(int childNumber) {
        this.childNumber = childNumber;
    }

    public int getInfantNumber() {
        return infantNumber;
    }

    public void setInfantNumber(int infantNumber) {
        this.infantNumber = infantNumber;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public String getProhibitedCarriers() {
        return prohibitedCarriers;
    }

    public void setProhibitedCarriers(String prohibitedCarriers) {
        this.prohibitedCarriers = prohibitedCarriers;
    }
}


