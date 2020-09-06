package comm.ota.site;


import comm.repository.entity.*;
import comm.utils.compression.CompressUtil;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The type Ota search request.
 */
public class SibeSearchRequest extends SibeBaseRequest implements Serializable {

    //搜索请求渠道来源 F：FlightIntlOnline；M：Mobile(移动端） 若为F，要求在7S内返回结果；若为M，要求在5S内返回结果
    private String channel;

    //是否需要压缩 默认不压缩；如果为T，压缩编码
    private String isCompressEncode;

    //查询Redis存储的Set Key
    private String tripCacheKey;//行程缓存主key

    //查询Redis存储的Hash Key
    private String tripCacheOTASiteKey;//行程缓存站点key

    //出发地相关的Redis信息，主要包括城市，国家，大区，TC区编码
    private AllAirports fromCityRedis;

    //目的地相关的Redis信息，主要包括城市，国家，大区，TC区编码
    private AllAirports toCityRedis;

    //机场-城市-国家-大区-TC区的匹配顺序
    private List<String> cityPrioritycList;

    //gds规则配置集合
    private Set<GdsRule> gdsRuleSet;

    //路由配置集合
    private List<RouteConfig> routeConfigRedisSet;

    //开关配置集合
//    private Set<SystemComTypeValueRedis> gdsSwitchValueRedisSet;

    //开关配置集合
//    private Set<SystemComTypeValueRedis> otaSwitchValueRedisSet;

    //new gds开关 ota开关
    private Set<SiteRulesSwitch> siteRulesSwitch;

    //所有PCC列表
    private Set<GdsPcc> gdsPccRedisSet;

    //实时汇率
//    private SibeExchangeRateRedis  sibeExchangeRateRedis;

    //OTA 币种
    private String otaCurrency ;

    //明细政策
    private Set<PolicyInfo> policyInfoRedisSet;

    //全局政策
    private Set<PolicyGlobal> policyGlobalRedisSet;

    //请求返回航班最大方案数
    private Integer numberOfUnits;

    //允许航空公司
    private String carriers;

    //拒绝航空公司(航司黑名单列表)
    private String prohibitedCarriers;

    //站点 Redis缓存有效时间
    private Integer otaCacheValidTime;

    //站点 Redis缓存刷新时间
    private Integer otaCacheRefreshTime;

    //GDS Redis缓存有效时间
    private Integer gdsCacheValidTime;

    //GDS Redis缓存刷新时间
    private Map<String,Integer> gdsCacheRefreshTimeMap;

    //限制止损
    private Integer stopLossPercentage;

    // 美团专用回传信息
    private String reqAddition;

    // 美团专用唯一标识
    private Long reqId;

    // 直飞过滤标识
    private Boolean directOnly;

    // ota请求过滤航司
    private String otaCarrier;

    //飞行类型1：含中转含直飞2：仅仅中转3：仅直飞
    private Integer flightType;

    //航空公司，使用逗号分隔
    private String airline;

//    private List<B2CSearchSiteInfo> b2CSearchSiteInfoList;

    //GDS总开关
//    private Set<SystemComTypeValueRedis> gdsSwitchRedisSet;

    //航司舱位黑名单
//    private List<ApiCarrierCabinBlackListRedis> apiCarrierCabinBlackListRedisList;

    //b2b查询过来需要判断
    private String jinriFlag;

    public Set<SiteRulesSwitch> getSiteRulesSwitch() {
        return siteRulesSwitch;
    }

    public void setSiteRulesSwitch(Set<SiteRulesSwitch> siteRulesSwitch) {
        this.siteRulesSwitch = siteRulesSwitch;
    }

    public Set<GdsRule> getGdsRuleSet() {
        return gdsRuleSet;
    }

    public void setGdsRuleSet(Set<GdsRule> gdsRuleSet) {
        this.gdsRuleSet = gdsRuleSet;
    }

    /**
     * 航司自动下线列表
     */
//    private Set<ApiOfflineAirLineRedis> apiOfflineAirLineRedisSet;
//
//    public Set<ApiOfflineAirLineRedis> getApiOfflineAirLineRedisSet() {
//        return apiOfflineAirLineRedisSet;
//    }
//
//    public void setApiOfflineAirLineRedisSet(Set<ApiOfflineAirLineRedis> apiOfflineAirLineRedisSet) {
//        this.apiOfflineAirLineRedisSet = apiOfflineAirLineRedisSet;
//    }
//
//    public List<ApiCarrierCabinBlackListRedis> getApiCarrierCabinBlackListRedisList() {
//        return apiCarrierCabinBlackListRedisList;
//    }
//
//    public void setApiCarrierCabinBlackListRedisList(List<ApiCarrierCabinBlackListRedis> apiCarrierCabinBlackListRedisList) {
//        this.apiCarrierCabinBlackListRedisList = apiCarrierCabinBlackListRedisList;
//    }



    public Integer getGdsCacheValidTime() {
        return gdsCacheValidTime;
    }

    public void setGdsCacheValidTime(Integer gdsCacheValidTime) {
        this.gdsCacheValidTime = gdsCacheValidTime;
    }

    public Map<String, Integer> getGdsCacheRefreshTimeMap() {
        return gdsCacheRefreshTimeMap;
    }

    public void setGdsCacheRefreshTimeMap(Map<String, Integer> gdsCacheRefreshTimeMap) {
        this.gdsCacheRefreshTimeMap = gdsCacheRefreshTimeMap;
    }

    public Integer getFlightType() {
        return flightType;
    }

    public void setFlightType(Integer flightType) {
        this.flightType = flightType;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getOtaCarrier() {
        return otaCarrier;
    }

    public void setOtaCarrier(String otaCarrier) {
        this.otaCarrier = otaCarrier;
    }

    public Boolean getDirectOnly() {
        return directOnly;
    }

    public void setDirectOnly(Boolean directOnly) {
        this.directOnly = directOnly;
    }

    /**
     * 深度复制方法,需要对象及对象所有的对象属性都实现序列化
     * Deep copy sibe search request.
     *
     * @param originSibeSearchRequest the origin sibe search request
     * @return the sibe search request
     */
    public static SibeSearchRequest deepCopy(SibeSearchRequest originSibeSearchRequest) {
        SibeSearchRequest sibeSearchRequest = null;
        ByteArrayOutputStream byteArrayOutputStream = null ;
        ObjectOutputStream oos = null ;
        ByteArrayInputStream bais = null ;
        ObjectInputStream ois = null ;
        try {
            // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
             byteArrayOutputStream = new ByteArrayOutputStream();
             oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(originSibeSearchRequest);
            //将流序列化成对象
             bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
             ois = new ObjectInputStream(bais);
            sibeSearchRequest = (SibeSearchRequest) ois.readObject();
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
        return sibeSearchRequest;
    }

    public String getReqAddition() {
        return reqAddition;
    }

    public void setReqAddition(String reqAddition) {
        this.reqAddition = reqAddition;
    }

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public String getJinriFlag() {
        return jinriFlag;
    }

    public void setJinriFlag(String jinriFlag) {
        this.jinriFlag = jinriFlag;
    }

    /**
     * Gets number of units.
     *
     * @return the number of units
     */
    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    /**
     * Sets number of units.
     *
     * @param numberOfUnits the number of units
     */
    public void setNumberOfUnits(Integer numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    /**
     * Gets stop loss percentage.
     *
     * @return the stop loss percentage
     */
    public Integer getStopLossPercentage() {
        return stopLossPercentage;
    }

    /**
     * Sets stop loss percentage.
     *
     * @param stopLossPercentage the stop loss percentage
     */
    public void setStopLossPercentage(Integer stopLossPercentage) {
        this.stopLossPercentage = stopLossPercentage;
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


    public String getTripCacheKey() {
        return tripCacheKey;
    }

    public void setTripCacheKey(String tripCacheKey) {
        this.tripCacheKey = tripCacheKey;
    }

    public String getTripCacheOTASiteKey() {
        return tripCacheOTASiteKey;
    }

    public void setTripCacheOTASiteKey(String tripCacheOTASiteKey) {
        this.tripCacheOTASiteKey = tripCacheOTASiteKey;
    }


    /**
     * Gets city priorityc list.
     *
     * @return the city priorityc list
     */
    public List<String> getCityPrioritycList() {
        return cityPrioritycList;
    }

    /**
     * Sets city priorityc list.
     *
     * @param cityPrioritycList the city priorityc list
     */
    public void setCityPrioritycList(List<String> cityPrioritycList) {
        this.cityPrioritycList = cityPrioritycList;
    }



    /**
     * Gets carriers.
     *
     * @return the carriers
     */
    public String getCarriers() {
        return carriers;
    }

    /**
     * Sets carriers.
     *
     * @param carriers the carriers
     */
    public void setCarriers(String carriers) {
        this.carriers = carriers;
    }

    /**
     * Gets prohibited carriers.
     *
     * @return the prohibited carriers
     */
    public String getProhibitedCarriers() {
        return prohibitedCarriers;
    }

    /**
     * Sets prohibited carriers.
     *
     * @param prohibitedCarriers the prohibited carriers
     */
    public void setProhibitedCarriers(String prohibitedCarriers) {
        this.prohibitedCarriers = prohibitedCarriers;
    }

    public Integer getOtaCacheValidTime() {
        return otaCacheValidTime;
    }

    public void setOtaCacheValidTime(Integer otaCacheValidTime) {
        this.otaCacheValidTime = otaCacheValidTime;
    }

    public Integer getOtaCacheRefreshTime() {
        return otaCacheRefreshTime;
    }

    public void setOtaCacheRefreshTime(Integer otaCacheRefreshTime) {
        this.otaCacheRefreshTime = otaCacheRefreshTime;
    }

    public String getOtaCurrency() {
        return otaCurrency;
    }

    public void setOtaCurrency(String otaCurrency) {
        this.otaCurrency = otaCurrency;
    }

    public AllAirports getFromCityRedis() {
        return fromCityRedis;
    }

    public void setFromCityRedis(AllAirports fromCityRedis) {
        this.fromCityRedis = fromCityRedis;
    }

    public AllAirports getToCityRedis() {
        return toCityRedis;
    }

    public void setToCityRedis(AllAirports toCityRedis) {
        this.toCityRedis = toCityRedis;
    }

    public List<RouteConfig> getRouteConfigRedisSet() {
        return routeConfigRedisSet;
    }

    public void setRouteConfigRedisSet(List<RouteConfig> routeConfigRedisSet) {
        this.routeConfigRedisSet = routeConfigRedisSet;
    }

    public Set<GdsPcc> getGdsPccRedisSet() {
        return gdsPccRedisSet;
    }

    public void setGdsPccRedisSet(Set<GdsPcc> gdsPccRedisSet) {
        this.gdsPccRedisSet = gdsPccRedisSet;
    }

    public Set<PolicyInfo> getPolicyInfoRedisSet() {
        return policyInfoRedisSet;
    }

    public void setPolicyInfoRedisSet(Set<PolicyInfo> policyInfoRedisSet) {
        this.policyInfoRedisSet = policyInfoRedisSet;
    }

    public Set<PolicyGlobal> getPolicyGlobalRedisSet() {
        return policyGlobalRedisSet;
    }

    public void setPolicyGlobalRedisSet(Set<PolicyGlobal> policyGlobalRedisSet) {
        this.policyGlobalRedisSet = policyGlobalRedisSet;
    }
}
