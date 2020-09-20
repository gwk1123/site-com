package comm.ota.site;



import comm.repository.entity.PolicyInfo;
import comm.repository.entity.RouteConfig;

import java.util.List;

/**
 * The type Sibe routing.
 */
public class SibeRoutingData {

    //加密的Data Str
    private String encryptData;
    //原始的Data Str
    private String decryptData;

    //出发地城市 IATA 三字码代码 【城市/机场】 如果为多程，会按照 PEK/HKG，HKG/SHA 格式请求
    private String fromCity;

    //目的地城市 IATA 三字码代码【城市/机场】
    private String toCity;

    //出发日期，格式为 YYYYMMDD（如果为多程，按照20130729,20130804的方式传输数据)
    private String fromDate;

    //回程日期，格式为 YYYYMMDD
    private String retDate;

    //uuid
    private String uid;

    //【公布运价强校验】运价类型
    // 1）PUB: 公布运价；
    // 2）PRV: 私有运价
    // 3）LCC: 廉航运价。
    private String fareType;

    //主销售航司
    private String validatingCarrier;

    //Fare Basis
    private String fareBasis;

    //GDS原始-成人票价
    private Integer AdultPriceGDS;

    //GDS原始-成人税费
    private Integer AdultTaxGDS;

    //GDS原始-小孩票价
    private Integer ChildPriceGDS;

    //GDS原始-税费
    private Integer ChildTaxGDS;

    //GDS原始-婴儿票价
    private Integer InfantsPriceGDS;

    //GDS原始-婴儿税费
    private Integer InfantsTaxGDS;

    /**
     * 返回给平台的规则信息（结合GDS规则信息与Polic规则信息）
     * 退改信息：
     * 如果Policy匹配上，则使用Policy退改规则信息
     * 如果没有匹配上，退改信息则三不准
     *
     * 行李额：
     * 如果Policy匹配上，则使用Policy行李额规则信息
     * 如果没有匹配上，则使用GDS行李额
     */
    private SibeRule sibeRule;

    //相关政策
    private PolicyInfo policyInfo;

    //相关政策
    private SibePolicy sibePolicy;

    //相关路由
    private SibeRoute sibeRoute;

    //去程航段按顺序 多程行程信息全部输出到此节点
    private List<SibeSegment> fromSegments;

    //多程行程信息全部输出到此节点
    private List<SibeSegment> retSegments;

    // 行程类型
    private String tripType;

    //是否使用指定舱位 1使用 2不使用
    private String useAssignCabin;

    //最大可预定数
    private String maxPassengerCount;

    //LCC flight ID
    private String flightId;

    //LCC passportNoRequired
    private String passportNoRequired;

    //LCC passportDetailsRequired
    private String passportDetailsRequired;

    //验价时返回的成人票面
    private Integer verifyGdsAdultPrice;

    //验价时返回的成人税费
    private Integer verifyGdsAdultTax;

    //验价时返回的儿童票面
    private Integer verifyGdsChildPrice;

    //验价时返回的儿童税费
    private Integer verifyGdsChildTax;

    //LCC 支付手续费
    private Integer lccPayHandlingFee;

    //VJ(专用) 相当于data作用，用于保存后续请求需要的信息，单程长度约650个字符，往返长度为单程两倍
    private String bookingKey;

    private String data;

    private String asOrderNo;

    private String sessionId;

    private  Long gdsCacheTimeLapse;

    public Long getGdsCacheTimeLapse() {
        return gdsCacheTimeLapse;
    }

    public void setGdsCacheTimeLapse(Long gdsCacheTimeLapse) {
        this.gdsCacheTimeLapse = gdsCacheTimeLapse;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAsOrderNo() {
        return asOrderNo;
    }

    public void setAsOrderNo(String asOrderNo) {
        this.asOrderNo = asOrderNo;
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

    public SibePolicy getSibePolicy() {
        return sibePolicy;
    }

    public void setSibePolicy(SibePolicy sibePolicy) {
        this.sibePolicy = sibePolicy;
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
     * Gets encrypt data.
     *
     * @return the encrypt data
     */
    public String getEncryptData() {
        return encryptData;
    }

    /**
     * Sets encrypt data.
     *
     * @param encryptData the encrypt data
     */
    public void setEncryptData(String encryptData) {
        this.encryptData = encryptData;
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
     * Gets adult price gds.
     *
     * @return the adult price gds
     */
    public Integer getAdultPriceGDS() {
        return AdultPriceGDS;
    }

    /**
     * Sets adult price gds.
     *
     * @param adultPriceGDS the adult price gds
     */
    public void setAdultPriceGDS(Integer adultPriceGDS) {
        AdultPriceGDS = adultPriceGDS;
    }

    /**
     * Gets adult tax gds.
     *
     * @return the adult tax gds
     */
    public Integer getAdultTaxGDS() {
        return AdultTaxGDS;
    }

    /**
     * Sets adult tax gds.
     *
     * @param adultTaxGDS the adult tax gds
     */
    public void setAdultTaxGDS(Integer adultTaxGDS) {
        AdultTaxGDS = adultTaxGDS;
    }

    /**
     * Gets child price gds.
     *
     * @return the child price gds
     */
    public Integer getChildPriceGDS() {
        return ChildPriceGDS;
    }

    /**
     * Sets child price gds.
     *
     * @param childPriceGDS the child price gds
     */
    public void setChildPriceGDS(Integer childPriceGDS) {
        ChildPriceGDS = childPriceGDS;
    }

    /**
     * Gets child tax gds.
     *
     * @return the child tax gds
     */
    public Integer getChildTaxGDS() {
        return ChildTaxGDS;
    }

    /**
     * Sets child tax gds.
     *
     * @param childTaxGDS the child tax gds
     */
    public void setChildTaxGDS(Integer childTaxGDS) {
        ChildTaxGDS = childTaxGDS;
    }

    /**
     * Gets infants price gds.
     *
     * @return the infants price gds
     */
    public Integer getInfantsPriceGDS() {
        return InfantsPriceGDS;
    }

    /**
     * Sets infants price gds.
     *
     * @param infantsPriceGDS the infants price gds
     */
    public void setInfantsPriceGDS(Integer infantsPriceGDS) {
        InfantsPriceGDS = infantsPriceGDS;
    }

    /**
     * Gets infants tax gds.
     *
     * @return the infants tax gds
     */
    public Integer getInfantsTaxGDS() {
        return InfantsTaxGDS;
    }

    /**
     * Sets infants tax gds.
     *
     * @param infantsTaxGDS the infants tax gds
     */
    public void setInfantsTaxGDS(Integer infantsTaxGDS) {
        InfantsTaxGDS = infantsTaxGDS;
    }

    /**
     * Gets from segments.
     *
     * @return the from segments
     */
    public List<SibeSegment> getFromSegments() {
        return fromSegments;
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
        return retSegments;
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
     * Gets sibe rule.
     *
     * @return the sibe rule
     */
    public SibeRule getSibeRule() {
        if(sibeRule==null){
            sibeRule = new SibeRule();
        }
        return sibeRule;
    }

    /**
     * Sets sibe rule.
     *
     * @param sibeRule the sibe rule
     */
    public void setSibeRule(SibeRule sibeRule) {
        this.sibeRule = sibeRule;
    }

    /**
     * Gets decrypt data.
     *
     * @return the decrypt data
     */
    public String getDecryptData() {
        return decryptData;
    }

    /**
     * Sets decrypt data.
     *
     * @param decryptData the decrypt data
     */
    public void setDecryptData(String decryptData) {
        this.decryptData = decryptData;
    }

    /**
     * Gets use assign cabin.
     *
     * @return the use assign cabin
     */
    public String getUseAssignCabin() {
        return useAssignCabin;
    }

    /**
     * Sets use assign cabin.
     *
     * @param useAssignCabin the use assign cabin
     */
    public void setUseAssignCabin(String useAssignCabin) {
        this.useAssignCabin = useAssignCabin;
    }

    /**
     * Gets max passenger count.
     *
     * @return the max passenger count
     */
    public String getMaxPassengerCount() {
        return maxPassengerCount;
    }

    /**
     * Sets max passenger count.
     *
     * @param maxPassengerCount the max passenger count
     */
    public void setMaxPassengerCount(String maxPassengerCount) {
        this.maxPassengerCount = maxPassengerCount;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getPassportNoRequired() {
        return passportNoRequired;
    }

    public void setPassportNoRequired(String passportNoRequired) {
        this.passportNoRequired = passportNoRequired;
    }

    public String getPassportDetailsRequired() {
        return passportDetailsRequired;
    }

    public void setPassportDetailsRequired(String passportDetailsRequired) {
        this.passportDetailsRequired = passportDetailsRequired;
    }

    public Integer getVerifyGdsAdultPrice() {
        return verifyGdsAdultPrice;
    }

    public void setVerifyGdsAdultPrice(Integer verifyGdsAdultPrice) {
        this.verifyGdsAdultPrice = verifyGdsAdultPrice;
    }

    public Integer getVerifyGdsAdultTax() {
        return verifyGdsAdultTax;
    }

    public void setVerifyGdsAdultTax(Integer verifyGdsAdultTax) {
        this.verifyGdsAdultTax = verifyGdsAdultTax;
    }

    public Integer getVerifyGdsChildPrice() {
        return verifyGdsChildPrice;
    }

    public void setVerifyGdsChildPrice(Integer verifyGdsChildPrice) {
        this.verifyGdsChildPrice = verifyGdsChildPrice;
    }

    public Integer getVerifyGdsChildTax() {
        return verifyGdsChildTax;
    }

    public void setVerifyGdsChildTax(Integer verifyGdsChildTax) {
        this.verifyGdsChildTax = verifyGdsChildTax;
    }

    public Integer getLccPayHandlingFee() {
        return lccPayHandlingFee;
    }

    public void setLccPayHandlingFee(Integer lccPayHandlingFee) {
        this.lccPayHandlingFee = lccPayHandlingFee;
    }

    public PolicyInfo getPolicyInfo() {
        return policyInfo;
    }

    public void setPolicyInfo(PolicyInfo policyInfo) {
        this.policyInfo = policyInfo;
    }

    public SibeRoute getSibeRoute() {
        return sibeRoute;
    }

    public void setSibeRoute(SibeRoute sibeRoute) {
        this.sibeRoute = sibeRoute;
    }
}
