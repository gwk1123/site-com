package comm.ota.site;

import comm.config.SibeProperties;
import comm.repository.entity.OtaRule;
import comm.repository.entity.RouteConfig;

import java.util.Map;
import java.util.Set;

/**
 * The type Ota base request.
 */
public class SibeBaseRequest{

    //
    private SibeProperties sibeProperties;
    //
    private Map<String,Object> sibeService;

    //成⼈人数，0-9
    private Integer adultNumber;

    //⼉童⼈数，0-9
    private Integer childNumber;

    //婴儿人数，0-9
    private Integer infantNumber;

    //出发地城市 IATA 三字码代码 【城市/机场】 如果为多程，会按照 PEK/HKG，HKG/SHA 格式请求
    private String fromCity;

    //目的地城市 IATA 三字码代码【城市/机场】
    private String toCity;

    //出发日期，格式为 YYYYMMDD（如果为多程，按照20130729,20130804的方式传输数据)
    private String fromDate;

    //回程日期，格式为 YYYYMMDD
    private String retDate;

    //接口身份标识用户名（渠道唯一标识) 相当于APP KEY
    private String cid;

    //行程类型 OW:单程, RT:往返, MT:多程
    private String tripType;

    //关联所有请求返回（原数据直接返回）
    private String uuid;

    //OTA平台代码
    private String ota;

    //OTA站点代码
    private String site;

    //生单,支付校验使用到的密钥
    private String skey;

    //Data加密的密钥
    private String datakey;

    //请求GDS平台
    private String gds;

    //调用GDS时使用的对应账号（OfficeID或PCC）
    private String officeId;

    //请求GDS的所使用的appKey
    private String appKey;


    //-------------------------------

    //出票提示 todo 暂时先放到这里，后续再进行优化
    private String otherIssueMsg;

    //直连订单编码
    private  String orderNo;

    //限制请求GDS超时配置
    private Integer requestGDSTimeOut;

    //限制请求超时时间
    private Integer timeOutTime;

    //限制中转次数 OTA-9 限制中转次数
    private Integer transitTimes;

    //OTA请求开始时间
    private Long startTime;

    //限制生单起飞时间变化差(提前） 单位分钟
    private Integer depTimeEarly;

    //限制生单起飞时间变化差(延时） 单位分钟
    private Integer depTimeDelay;

    //Routing
    private SibeRouting routing ;

    //OTA站点规则配置信息
    private Set<OtaRule> otaRuleRedisSet;

    //原始路由信息 key：GDS-PCC, value:routeObject
    private Map<String, SibeRoute> searchRouteMap;

    //刷新PCC
    private Map<String,RouteConfig> refreshGDSMap;

    //ota订单编码
    private  String otaOrderNo;

    //站点航线选择GDS
    private String otaSiteAirRouteChooseGDS;

    //2018/08/06 OneWorldTrip开发F：头等舱；C：商务型；S：高端经济舱；Y：经济舱/二等舱
    private String cabinGrade;

    //经过业务处理的路由 key：GDS-PCC, value:routeObject,优先级高于原始路由
//    private Map<String,SibeRoute> businessSearchRouteMap;

    //用于记录生成该方案的GDS请求时间
    private  Long gdsCacheTimeLapse;

    public Long getGdsCacheTimeLapse() {
        return gdsCacheTimeLapse;
    }

    public void setGdsCacheTimeLapse(Long gdsCacheTimeLapse) {
        this.gdsCacheTimeLapse = gdsCacheTimeLapse;
    }

    public Map<String, RouteConfig> getRefreshGDSMap() {
        return refreshGDSMap;
    }

    public void setRefreshGDSMap(Map<String, RouteConfig> refreshGDSMap) {
        this.refreshGDSMap = refreshGDSMap;
    }

    public String getCabinGrade() {
        return cabinGrade;
    }

    public void setCabinGrade(String cabinGrade) {
        this.cabinGrade = cabinGrade;
    }

    public String getOtaSiteAirRouteChooseGDS() {
        return otaSiteAirRouteChooseGDS;
    }

    public void setOtaSiteAirRouteChooseGDS(String otaSiteAirRouteChooseGDS) {
        this.otaSiteAirRouteChooseGDS = otaSiteAirRouteChooseGDS;
    }

//    public Map<String, SibeRoute> getBusinessSearchRouteMap() {
//        return businessSearchRouteMap;
//    }
//
//    public void setBusinessSearchRouteMap(Map<String, SibeRoute> businessSearchRouteMap) {
//        this.businessSearchRouteMap = businessSearchRouteMap;
//    }

    /**
     * Gets routing.
     *
     * @return the routing
     */
    public SibeRouting getRouting() {
        if(routing==null){
            routing=new SibeRouting();
        }
        return routing;
    }

    /**
     * Gets adult number.
     *
     * @return the adult number
     */
    public Integer getAdultNumber() {
        return adultNumber;
    }

    /**
     * Sets adult number.
     *
     * @param adultNumber the adult number
     */
    public void setAdultNumber(Integer adultNumber) {
        this.adultNumber = adultNumber;
    }

    /**
     * Gets child number.
     *
     * @return the child number
     */
    public Integer getChildNumber() {
        return childNumber;
    }

    /**
     * Sets child number.
     *
     * @param childNumber the child number
     */
    public void setChildNumber(Integer childNumber) {
        this.childNumber = childNumber;
    }

    /**
     * Gets infant number.
     *
     * @return the infant number
     */
    public Integer getInfantNumber() {
        return infantNumber;
    }

    /**
     * Sets infant number.
     *
     * @param infantNumber the infant number
     */
    public void setInfantNumber(Integer infantNumber) {
        this.infantNumber = infantNumber;
    }

    /**
     * Gets transit times.
     *
     * @return the transit times
     */
    public Integer getTransitTimes() {
        return transitTimes;
    }

    /**
     * Sets transit times.
     *
     * @param transitTimes the transit times
     */
    public void setTransitTimes(Integer transitTimes) {
        this.transitTimes = transitTimes;
    }

    /**
     * Sets routing.
     *
     * @param routing the routing
     */
    public void setRouting(SibeRouting routing) {
        this.routing = routing;
    }

    /**
     * Gets search route map.
     *
     * @return the search route map
     */
    public Map<String, SibeRoute> getSearchRouteMap() {
        return searchRouteMap;
    }

    /**
     * Sets search route map.
     *
     * @param searchRouteMap the search route map
     */
    public void setSearchRouteMap(Map<String, SibeRoute> searchRouteMap) {
        this.searchRouteMap = searchRouteMap;
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
     * Gets ota.
     *
     * @return the ota
     */
    public String getOta() {
        return ota;
    }

    /**
     * Sets ota.
     *
     * @param ota the ota
     */
    public void setOta(String ota) {
        this.ota = ota;
    }

    /**
     * Gets site.
     *
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * Sets site.
     *
     * @param site the site
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * Gets skey.
     *
     * @return the skey
     */
    public String getSkey() {
        return skey;
    }

    /**
     * Sets skey.
     *
     * @param skey the skey
     */
    public void setSkey(String skey) {
        this.skey = skey;
    }

    /**
     * Gets datakey.
     *
     * @return the datakey
     */
    public String getDatakey() {
        return datakey;
    }

    /**
     * Sets datakey.
     *
     * @param datakey the datakey
     */
    public void setDatakey(String datakey) {
        this.datakey = datakey;
    }

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
     * Gets other issue msg.
     *
     * @return the other issue msg
     */
    public String getOtherIssueMsg() {
        return otherIssueMsg;
    }

    /**
     * Sets other issue msg.
     *
     * @param otherIssueMsg the other issue msg
     */
    public void setOtherIssueMsg(String otherIssueMsg) {
        this.otherIssueMsg = otherIssueMsg;
    }

    /**
     * Gets gds.
     *
     * @return the gds
     */
    public String getGds() {
        return gds;
    }

    /**
     * Sets gds.
     *
     * @param gds the gds
     */
    public void setGds(String gds) {
        this.gds = gds;
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
     * Gets app key.
     *
     * @return the app key
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * Sets app key.
     *
     * @param appKey the app key
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * Gets order no.
     *
     * @return the order no
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * Sets order no.
     *
     * @param orderNo the order no
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


    /**
     * Gets ota rule redis set.
     *
     * @return the ota rule redis set
     */
    public Set<OtaRule> getOtaRuleRedisSet() {
        return otaRuleRedisSet;
    }

    /**
     * Sets ota rule redis set.
     *
     * @param otaRuleRedisSet the ota rule redis set
     */
    public void setOtaRuleRedisSet(Set<OtaRule> otaRuleRedisSet) {
        this.otaRuleRedisSet = otaRuleRedisSet;
    }


    /**
     * Gets request gds time out.
     *
     * @return the request gds time out
     */
    public Integer getRequestGDSTimeOut() {
        return requestGDSTimeOut;
    }

    /**
     * Sets request gds time out.
     *
     * @param requestGDSTimeOut the request gds time out
     */
    public void setRequestGDSTimeOut(Integer requestGDSTimeOut) {
        this.requestGDSTimeOut = requestGDSTimeOut;
    }

    /**
     * Gets time out time.
     *
     * @return the time out time
     */
    public Integer getTimeOutTime() {
        return timeOutTime;
    }

    /**
     * Sets time out time.
     *
     * @param timeOutTime the time out time
     */
    public void setTimeOutTime(Integer timeOutTime) {
        this.timeOutTime = timeOutTime;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets dep time early.
     *
     * @return the dep time early
     */
    public Integer getDepTimeEarly() {
        return depTimeEarly;
    }

    /**
     * Sets dep time early.
     *
     * @param depTimeEarly the dep time early
     */
    public void setDepTimeEarly(Integer depTimeEarly) {
        this.depTimeEarly = depTimeEarly;
    }

    /**
     * Gets dep time delay.
     *
     * @return the dep time delay
     */
    public Integer getDepTimeDelay() {
        return depTimeDelay;
    }

    /**
     * Sets dep time delay.
     *
     * @param depTimeDelay the dep time delay
     */
    public void setDepTimeDelay(Integer depTimeDelay) {
        this.depTimeDelay = depTimeDelay;
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
        return null == toCity ? "" : toCity ;
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
     * Gets sibe properties.
     *
     * @return the sibe properties
     */
    public SibeProperties getSibeProperties() {
        return sibeProperties;
    }

    /**
     * Sets sibe properties.
     *
     * @param sibeProperties the sibe properties
     */
    public void setSibeProperties(SibeProperties sibeProperties) {
        this.sibeProperties = sibeProperties;
    }

    /**
     * Gets sibe service.
     *
     * @return the sibe service
     */
    public Map<String, Object> getSibeService() {
        return sibeService;
    }

    /**
     * Sets sibe service.
     *
     * @param sibeService the sibe service
     */
    public void setSibeService(Map<String, Object> sibeService) {
        this.sibeService = sibeService;
    }

    public String getOtaOrderNo() {
        return otaOrderNo;
    }

    public void setOtaOrderNo(String otaOrderNo) {
        this.otaOrderNo = otaOrderNo;
    }
}
