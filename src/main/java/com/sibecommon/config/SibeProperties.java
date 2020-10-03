package com.sibecommon.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yangdehua on 18/2/8.
 */
@ConfigurationProperties(prefix = "sxysibe", ignoreUnknownFields = false)
@Component
public class SibeProperties {


    private final Search search = new Search();
    private final Redis redis = new Redis();
    private final Ota ota = new Ota();
    private final Gds gds = new Gds();

    //压缩开关
    private final Compass compass= new Compass () ;

    private final Order order = new Order();

    /**
     * Gets search.
     *
     * @return the search
     */
    public Search getSearch() {
        return search;
    }

    /**
     * Gets redis.
     *
     * @return the redis
     */
    public Redis getRedis() {
        return redis;
    }

    /**
     * The type Search.
     */
    public static class Search {


        private boolean async = true;

        /**
         * Is async boolean.
         *
         * @return the boolean
         */
        public boolean isAsync() {
            return async;
        }

        /**
         * Sets async.
         *
         * @param async the async
         */
        public void setAsync(boolean async) {
            this.async = async;
        }


    }

    /**
     * The type Redis.
     */
    public static class Redis {

        private boolean refreshSwitch = false;

        private boolean refreshGdsSwitch=false;

        private boolean refreshOtherSiteSwitch=false;

        private String refreshOtaSites="";

        /**
         * Is refresh gds switch boolean.
         *
         * @return the boolean
         */
        public boolean isRefreshGdsSwitch() {
            return refreshGdsSwitch;
        }

        /**
         * Sets refresh gds switch.
         *
         * @param refreshGdsSwitch the refresh gds switch
         */
        public void setRefreshGdsSwitch(boolean refreshGdsSwitch) {
            this.refreshGdsSwitch = refreshGdsSwitch;
        }

        /**
         * Is refresh other site switch boolean.
         *
         * @return the boolean
         */
        public boolean isRefreshOtherSiteSwitch() {
            return refreshOtherSiteSwitch;
        }

        /**
         * Sets refresh other site switch.
         *
         * @param refreshOtherSiteSwitch the refresh other site switch
         */
        public void setRefreshOtherSiteSwitch(boolean refreshOtherSiteSwitch) {
            this.refreshOtherSiteSwitch = refreshOtherSiteSwitch;
        }

        /**
         * Gets refresh ota sites.
         *
         * @return the refresh ota sites
         */
        public String getRefreshOtaSites() {
            return refreshOtaSites;
        }

        /**
         * Sets refresh ota sites.
         *
         * @param refreshOtaSites the refresh ota sites
         */
        public void setRefreshOtaSites(String refreshOtaSites) {
            this.refreshOtaSites = refreshOtaSites;
        }

        /**
         * Is refresh switch boolean.
         *
         * @return the boolean
         */
        public boolean isRefreshSwitch() {
            return refreshSwitch;
        }

        /**
         * Sets refresh switch.
         *
         * @param refreshSwitch the refresh switch
         */
        public void setRefreshSwitch(boolean refreshSwitch) {
            this.refreshSwitch = refreshSwitch;
        }
    }

    /**
     * The type Gds.
     */
    public static class Gds {
        /**
         * GDS 超时时间 熔断时间
         */
        private  Integer fuseTime;


        /**
         * 并发幂等性开关
         */
        private  boolean searchIdempotent = false;

        /**
         * 缓存无效航线（无价格的航线，无效的航线）的失效时间（单位：时）
         */
        private  Long   invalidRouteSurvivalTime;
        private  final Amadeus amadeus = new Amadeus();
        private  final Amadeus galileo = new Amadeus();
        private  final Amadeus sabre = new Amadeus();
        private  final Amadeus lcc = new Amadeus();
        private  final Amadeus amadeusvc = new Amadeus();
        private  final Amadeus galileovc = new Amadeus();
        private  final Amadeus vjapp = new Amadeus();
        private  final Amadeus jinriapp=new Amadeus();
        private  final Amadeus airSpring =new Amadeus();

        public Integer getFuseTime() {
            return fuseTime;
        }

        public void setFuseTime(Integer fuseTime) {
            this.fuseTime = fuseTime;
        }

        public Long getInvalidRouteSurvivalTime() {
            return invalidRouteSurvivalTime;
        }

        public void setInvalidRouteSurvivalTime(Long invalidRouteSurvivalTime) {
            this.invalidRouteSurvivalTime = invalidRouteSurvivalTime;
        }

        public boolean isSearchIdempotent() {
            return searchIdempotent;
        }

        public void setSearchIdempotent(boolean searchIdempotent) {
            this.searchIdempotent = searchIdempotent;
        }

        @Override
        public String toString() {
            return "Gds{" +
                "fuseTime=" + fuseTime +
                ", amadeus=" + amadeus +
                ", galileo=" + galileo +
                ", sabre=" + sabre +
                ", lcc=" + lcc +
                ", amadeusvc=" + amadeusvc +
                ", galileovc=" + galileovc +
                '}';
        }

        /**
         * The type Amadeus.
         */
        public static class Amadeus {
            @Override
            public String toString() {
                return "Amadeus{" +
                    "appKey='" + appKey + '\'' +
                    ", searchUrl='" + searchUrl + '\'' +
                    ", url='" + url + '\'' +
                    ", officeId='" + officeId + '\'' +
                    ", contact=" + contact.toString() +
                    '}';
            }

            private  String appKey = "";
            private  String searchUrl = "";
            private  String otherUrl = "";
            private  String url = "";
            private  String officeId = "";
            private  final Contact contact = new Contact();

            /**
             * The type Contact.
             */
            public static class Contact {
                private  String name = "";
                private  String address = "";
                private  String postcode  = "";
                private  String email  = "";
                private  String mobile  = "";
                private  String ctct = "";
                private  String sex = "";
                private  String city = "";
                private  String phoneCountry = "";
                private  String phoneArea = "";
                private  String countryCode = "";
                private  String phoneNumber = "";

                @Override
                public String toString() {
                    return "Contact{" +
                        "name='" + name + '\'' +
                        ", address='" + address + '\'' +
                        ", postcode='" + postcode + '\'' +
                        ", email='" + email + '\'' +
                        ", mobile='" + mobile + '\'' +
                        ", ctct='" + ctct + '\'' +
                        ", sex='" + sex + '\'' +
                        ", city='" + city + '\'' +
                        ", phoneCountry='" + phoneCountry + '\'' +
                        ", phoneArea='" + phoneArea + '\'' +
                        ", phoneNumber='" + phoneNumber + '\'' +
                        ", countryCode='" + countryCode + '\'' +
                        '}';
                }

                public String getPhoneNumber() {
                    return phoneNumber;
                }

                public void setPhoneNumber(String phoneNumber) {
                    this.phoneNumber = phoneNumber;
                }

                /**
                 * Sets name.
                 *
                 * @param name the name
                 */
                public void setName(String name) {
                    this.name = name;
                }

                /**
                 * Sets address.
                 *
                 * @param address the address
                 */
                public void setAddress(String address) {
                    this.address = address;
                }

                /**
                 * Sets mobile.
                 *
                 * @param mobile the mobile
                 */
                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                /**
                 * Sets ctct.
                 *
                 * @param ctct the ctct
                 */
                public void setCtct(String ctct) {
                    this.ctct = ctct;
                }


                /**
                 * Gets name.
                 *
                 * @return the name
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Gets address.
                 *
                 * @return the address
                 */
                public String getAddress() {
                    return address;
                }

                /**
                 * Gets mobile.
                 *
                 * @return the mobile
                 */
                public String getMobile() {
                    return mobile;
                }

                /**
                 * Gets ctct.
                 *
                 * @return the ctct
                 */
                public String getCtct() {
                    return ctct;
                }

                public String getPostcode() {
                    return postcode;
                }

                public void setPostcode(String postcode) {
                    this.postcode = postcode;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getPhoneCountry() {
                    return phoneCountry;
                }

                public void setPhoneCountry(String phoneCountry) {
                    this.phoneCountry = phoneCountry;
                }

                public String getPhoneArea() {
                    return phoneArea;
                }

                public void setPhoneArea(String phoneArea) {
                    this.phoneArea = phoneArea;
                }

                public String getCountryCode() {
                    return countryCode;
                }

                public void setCountryCode(String countryCode) {
                    this.countryCode = countryCode;
                }
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
             * Sets search url.
             *
             * @param searchUrl the search url
             */
            public void setSearchUrl(String searchUrl) {
                this.searchUrl = searchUrl;
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
             * Gets search url.
             *
             * @return the search url
             */
            public String getSearchUrl() {
                return searchUrl;
            }


            /**
             * Gets contact.
             *
             * @return the contact
             */
            public Contact getContact() {
                return contact;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getOfficeId() {
                return officeId;
            }

            public void setOfficeId(String officeId) {
                this.officeId = officeId;
            }

            public String getOtherUrl() {
                return otherUrl;
            }

            public void setOtherUrl(String otherUrl) {
                this.otherUrl = otherUrl;
            }
        }

        /**
         * Gets amadeus.
         *
         * @return the amadeus
         */
        public Amadeus getAmadeus() {
            return amadeus;
        }

        /**
         * Gets galileo.
         *
         * @return the galileo
         */
        public Amadeus getGalileo() {
            return galileo;
        }

        /**
         * Gets sabre.
         *
         * @return the sabre
         */
        public Amadeus getSabre() {
            return sabre;
        }

        public Amadeus getLcc() {
            return lcc;
        }

        public Amadeus getAmadeusvc() {
            return amadeusvc;
        }

        public Amadeus getGalileovc() {
            return galileovc;
        }

        public Amadeus getVjapp() {
            return vjapp;
        }

        public Amadeus getJinriapp() {
            return jinriapp;
        }

        public Amadeus getAirSpring() {
            return airSpring;
        }
    }

    /**
     * The type Ota.
     */
    public static class Ota {
        private  String dataKey = "";
        private  String ota = "";
        private  String site = "";
        private  String cid = "";
        private  String jtcid = "";
        private  String skey = "";
        private  String loggerPath = "";
        private  Boolean loggerSaveFileFlag = Boolean.FALSE;
        private  String otherIssueTicketMsg = "";
        private Map<String,String> assignCabin = new HashMap<>();
        private Integer otaSiteCacheTime = null;
        private boolean pushFliggy = false;
        private String pushFliggyUrl = "";
        private long  offlineGroupStatisticsTime;


        //飞猪退改比例退改自由调整
        private String fliggyNonAirLine; //不可退的航司
        private String fliggyFareType;  //运价类型
        private String fliggyTakeoffTime;  //起飞时间
        private Double fliggyRefund;  //起飞前收费比
        private Double fliggyEndorse;  //改期起飞前收费比

        //共享航司延迟返回生单结果
        private Integer orderDelayedTime;  //延迟时间

        private String mfwjsOtherUrl="";

        private boolean mfwjsSwitch = false;

        //携程退改比例自由调整
        private String ctripNonAirlineEndorse;
        private Double ctripRefund;  //起飞前收费比
        private Double ctripEndorse;  //改期起飞前收费比

        /**
         * 2018/08/06
         * OneWorldTrip开发
         * @return
         */
        private Integer orderNo;

        //添加过滤方案
        private String filterSuccessCabin ;

        public String getFilterSuccessCabin() {
            return filterSuccessCabin;
        }

        public void setFilterSuccessCabin(String filterSuccessCabin) {
            this.filterSuccessCabin = filterSuccessCabin;
        }

        public Integer getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(Integer orderNo) {
            this.orderNo = orderNo;
        }

        public String getMfwjsOtherUrl() {
            return mfwjsOtherUrl;
        }

        public void setMfwjsOtherUrl(String mfwjsOtherUrl) {
            this.mfwjsOtherUrl = mfwjsOtherUrl;
        }

        public boolean isMfwjsSwitch() {
            return mfwjsSwitch;
        }

        public void setMfwjsSwitch(boolean mfwjsSwitch) {
            this.mfwjsSwitch = mfwjsSwitch;
        }

        public String getCtripNonAirlineEndorse() {
            return ctripNonAirlineEndorse;
        }

        public void setCtripNonAirlineEndorse(String ctripNonAirlineEndorse) {
            this.ctripNonAirlineEndorse = ctripNonAirlineEndorse;
        }

        public Double getCtripRefund() {
            return ctripRefund;
        }

        public void setCtripRefund(Double ctripRefund) {
            this.ctripRefund = ctripRefund;
        }

        public Double getCtripEndorse() {
            return ctripEndorse;
        }

        public void setCtripEndorse(Double ctripEndorse) {
            this.ctripEndorse = ctripEndorse;
        }

        @Override
        public String toString() {
            return "Ota{" +
                "dataKey='" + dataKey + '\'' +
                ", ota='" + ota + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", site='" + site + '\'' +
                ", cid='" + cid + '\'' +
                ", jtcid=" +jtcid + '\'' +
                ", skey='" + skey + '\'' +
                ", loggerPath='" + loggerPath + '\'' +
                ", loggerSaveFileFlag=" + loggerSaveFileFlag +
                ", otherIssueTicketMsg='" + otherIssueTicketMsg + '\'' +
                ", otherIssueTicketMsg='" + assignCabin + '\'' +
                ", otaSiteCacheTime='" + otaSiteCacheTime + '\'' +
                ", fliggyNonAirLine='" + fliggyNonAirLine + '\'' +
                ", fliggFareType='" + fliggyFareType + '\'' +
                ", fliggyTakeoffTime='" + fliggyTakeoffTime + '\'' +
                ", fliggyRefund='" + fliggyRefund + '\'' +
                ", fliggyEndorse='" + fliggyEndorse + '\'' +
                ", orderDelayedTime='" + orderDelayedTime + '\'' +
                '}';
        }

        public boolean isPushFliggy() {
            return pushFliggy;
        }

        public void setPushFliggy(boolean pushFliggy) {
            this.pushFliggy = pushFliggy;
        }

        public String getPushFliggyUrl() {
            return pushFliggyUrl;
        }

        public void setPushFliggyUrl(String pushFliggyUrl) {
            this.pushFliggyUrl = pushFliggyUrl;
        }

        public long getOfflineGroupStatisticsTime() {
            return offlineGroupStatisticsTime;
        }

        public void setOfflineGroupStatisticsTime(long offlineGroupStatisticsTime) {
            this.offlineGroupStatisticsTime = offlineGroupStatisticsTime;
        }

        public Integer getOrderDelayedTime() {
            return orderDelayedTime;
        }

        public void setOrderDelayedTime(Integer orderDelayedTime) {
            this.orderDelayedTime = orderDelayedTime;
        }

        /**
         * Sets data key.
         *
         * @param dataKey the data key
         */
        public void setDataKey(String dataKey) {
            this.dataKey = dataKey;
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
         * Sets site.
         *
         * @param site the site
         */
        public void setSite(String site) {
            this.site = site;
        }

        /**
         * Sets cid.
         *
         * @param cid the cid
         */
        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getJtcid() {
            return jtcid;
        }

        public void setJtcid(String jtcid) {
            this.jtcid = jtcid;
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
         * Sets logger path.
         *
         * @param loggerPath the logger path
         */
        public void setLoggerPath(String loggerPath) {
            this.loggerPath = loggerPath;
        }

        /**
         * Sets logger save file flag.
         *
         * @param loggerSaveFileFlag the logger save file flag
         */
        public void setLoggerSaveFileFlag(Boolean loggerSaveFileFlag) {
            this.loggerSaveFileFlag = loggerSaveFileFlag;
        }

        /**
         * Sets other issue ticket msg.
         *
         * @param otherIssueTicketMsg the other issue ticket msg
         */
        public void setOtherIssueTicketMsg(String otherIssueTicketMsg) {
            this.otherIssueTicketMsg = otherIssueTicketMsg;
        }

        /**
         * Gets data key.
         *
         * @return the data key
         */
        public String getDataKey() {
            return dataKey;
        }

        /**
         * Gets logger save file flag.
         *
         * @return the logger save file flag
         */
        public Boolean getLoggerSaveFileFlag() {
            return loggerSaveFileFlag;
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
         * Gets site.
         *
         * @return the site
         */
        public String getSite() {
            return site;
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
         * Gets skey.
         *
         * @return the skey
         */
        public String getSkey() {
            return skey;
        }

        /**
         * Gets logger path.
         *
         * @return the logger path
         */
        public String getLoggerPath() {
            return loggerPath;
        }

        /**
         * Gets other issue ticket msg.
         *
         * @return the other issue ticket msg
         */
        public String getOtherIssueTicketMsg() {
            return otherIssueTicketMsg;
        }

        public Map<String, String> getAssignCabin() {
            return assignCabin;
        }

        public void setAssignCabin(Map<String, String> assignCabin) {
            this.assignCabin = assignCabin;
        }

        public Integer getOtaSiteCacheTime() {
            return otaSiteCacheTime;
        }

        public void setOtaSiteCacheTime(Integer otaSiteCacheTime) {
            this.otaSiteCacheTime = otaSiteCacheTime;
        }

        public String getFliggyNonAirLine() {
            return fliggyNonAirLine;
        }

        public void setFliggyNonAirLine(String fliggyNonAirLine) {
            this.fliggyNonAirLine = fliggyNonAirLine;
        }

        public String getFliggyFareType() {
            return fliggyFareType;
        }

        public void setFliggyFareType(String fliggyFareType) {
            this.fliggyFareType = fliggyFareType;
        }

        public String getFliggyTakeoffTime() {
            return fliggyTakeoffTime;
        }

        public void setFliggyTakeoffTime(String fliggyTakeoffTime) {
            this.fliggyTakeoffTime = fliggyTakeoffTime;
        }

        public Double getFliggyRefund() {
            return fliggyRefund;
        }

        public void setFliggyRefund(Double fliggyRefund) {
            this.fliggyRefund = fliggyRefund;
        }

        public Double getFliggyEndorse() {
            return fliggyEndorse;
        }

        public void setFliggyEndorse(Double fliggyEndorse) {
            this.fliggyEndorse = fliggyEndorse;
        }
    }


    /**
     * Gets ota.
     *
     * @return the ota
     */
    public Ota getOta() {
        return ota;
    }

    /**
     * Gets gds.
     *
     * @return the gds
     */
    public Gds getGds() {
        return gds;
    }

    /**
     * 压缩
     * @return
     */
    public Compass getCompass() {
        return compass;
    }



    @Override
    public String toString() {
        return "SibeProperties{" +
            "search=" + search +
            ", redis=" + redis +
            ", ota=" + ota +
            ", gds=" + gds +
            ", compass=" + compass +
            '}';
    }

    public  static class  Compass{
        private String  switchgds ;
        private String  compresstype ;

        public String getSwitchgds() {
            return switchgds;
        }

        public void setSwitchgds(String switchgds) {
            this.switchgds = switchgds;
        }

        public String getCompresstype() {
            return compresstype;
        }

        public void setCompresstype(String compresstype) {
            this.compresstype = compresstype;
        }
    }

    public static class Order{
        /**
         * 切换GDS生单开关
         */
        private boolean switchOrder;

        /**
         * 9C取消重订时间（生单后多久进行取消重订）
         */
        private Integer expireTiem;

        public Integer getExpireTiem() {
            return expireTiem;
        }

        public void setExpireTiem(Integer expireTiem) {
            this.expireTiem = expireTiem;
        }

        public boolean isSwitchOrder() {
            return switchOrder;
        }

        public void setSwitchOrder(boolean switchOrder) {
            this.switchOrder = switchOrder;
        }
    }

    /**
     * 生单配置
     * @return
     */
    public Order getOrder() {
        return order;
    }
}
