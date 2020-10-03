package com.sibecommon.ota.ctrip.model;
import java.io.Serializable;

/**
 * Created by gwk on 18/3/11.
 */
public class CtripSearchRequest implements Serializable {
    //接口身份标识用户名（渠道唯一标识) 相当于APP KEY
    private String uuid;

    //接口身份标识用户名（渠道唯一标识) 相当于APP KEY
    private String cid;

    //行程类型，1：单程；2：往返；3：多程；
    private String tripType;

    //出发地城市 IATA 三字码代码 【城市/机场】 如果为多程，会按照 PEK/HKG，HKG/SHA 格式请求
    private String fromCity;

    //目的地城市 IATA 三字码代码【城市/机场】
    private String toCity;

    //出发日期，格式为 YYYYMMDD（如果为多程，按照20130729,20130804的方式传输数据)
    private String fromDate;

    //回程日期，格式为 YYYYMMDD
    private String retDate;

    //成⼈人数，1-9
    private int adultNumber;

    //⼉童人数，0-9
    private int childNumber;

    //婴儿人数，0-9
    private int infantNumber;

    //搜索请求渠道来源 F：FlightIntlOnline；M：Mobile(移动端） 若为F，要求在7S内返回结果；若为M，要求在5S内返回结果
    private String channel;

    //是否需要压缩 默认不压缩；如果为T，压缩编码
    private String isCompressEncode;

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets uuid.
     *
     * @param uuid the uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets cid.
     *
     * @return the cid
     */
    public String getCid() {
        return cid;
    }

    /**
     * Sets cid.
     *
     * @param cid the cid
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     * Gets trip type.
     *
     * @return the trip type
     */
    public String getTripType() {
        return tripType;
    }

    /**
     * Sets trip type.
     *
     * @param tripType the trip type
     */
    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    /**
     * Gets from city.
     *
     * @return the from city
     */
    public String getFromCity() {
        return fromCity;
    }

    /**
     * Sets from city.
     *
     * @param fromCity the from city
     */
    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    /**
     * Gets to city.
     *
     * @return the to city
     */
    public String getToCity() {
        return toCity;
    }

    /**
     * Sets to city.
     *
     * @param toCity the to city
     */
    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    /**
     * Gets from date.
     *
     * @return the from date
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Sets from date.
     *
     * @param fromDate the from date
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Gets ret date.
     *
     * @return the ret date
     */
    public String getRetDate() {
        return retDate;
    }

    /**
     * Sets ret date.
     *
     * @param retDate the ret date
     */
    public void setRetDate(String retDate) {
        this.retDate = retDate;
    }

    /**
     * Gets adult number.
     *
     * @return the adult number
     */
    public int getAdultNumber() {
        return adultNumber;
    }

    /**
     * Sets adult number.
     *
     * @param adultNumber the adult number
     */
    public void setAdultNumber(int adultNumber) {
        this.adultNumber = adultNumber;
    }

    /**
     * Gets child number.
     *
     * @return the child number
     */
    public int getChildNumber() {
        return childNumber;
    }

    /**
     * Sets child number.
     *
     * @param childNumber the child number
     */
    public void setChildNumber(int childNumber) {
        this.childNumber = childNumber;
    }

    /**
     * Gets infant number.
     *
     * @return the infant number
     */
    public int getInfantNumber() {
        return infantNumber;
    }

    /**
     * Sets infant number.
     *
     * @param infantNumber the infant number
     */
    public void setInfantNumber(int infantNumber) {
        this.infantNumber = infantNumber;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets channel.
     *
     * @param channel the channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Gets is compress encode.
     *
     * @return the is compress encode
     */
    public String getIsCompressEncode() {
        return isCompressEncode;
    }

    /**
     * Sets is compress encode.
     *
     * @param isCompressEncode the is compress encode
     */
    public void setIsCompressEncode(String isCompressEncode) {
        this.isCompressEncode = isCompressEncode;
    }
}
