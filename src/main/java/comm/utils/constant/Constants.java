package comm.utils.constant;

/**
 * Application constants.
 */
public final class Constants {
    /**
     * caffeine cache 默认最大条目数
     */
    public static final int CAFFEINE_MAX_SIZE = 10000;
    /**
     * caffeine cache 默认失效时间
     */
    public static final int CAFFEINE_EXPIRE_TIME_SECONDS = 3;

    public static final  String  IDEMPOTENT_SEARCH_KEY = "concurrency_control";

    public static final  long  IDEMPOTENT_SEARCH_TIME_SECONDS = 60;


    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";

    //平台站点标识
    public final static String CTRIP_PLATFORM = "CTRIP";
    public final static String CTRIP_ONE = "CT001";
    public final static String CTRIP_TWO = "CT002";
    public final static String CTRIP_THE = "CT003";
    public final static String TUNIU_PLATFORM = "TUNIU";
    public final static String TUNIU_ONE = "TN001";

    // 去哪儿平台站点标识
    public final static String  QUNAR_PLATFORM = "QUNAR";
    public final static String QUNAR_SXG = "SXG"; // 森鑫源航空飞旅特惠
    public final static String QUNAR_SBH = "SBH"; // 森鑫源国际飞旅
    public final static String QUNAR_SXI = "SXI"; // 森鑫源航空
    public final static String QUNAR_SXM = "SXM"; // 森鑫源航空飞旅
    public final static String QUNAR_SXL = "SXL"; // 森鑫源航空特惠
    public final static String QUNAR_SBI = "SBI"; // 畅游五洲飞旅特惠
    public final static String QUNAR_XNW = "XNW"; //

    //飞猪平台站点标识
    public final static String TB_PLATFORM = "FLIGGY"; // 飞猪平台代码
    public final static String TB_SITE_TBSXYPT = "TBSXYPT"; // 飞猪森鑫源国际普通
    public final static String TB_SITE_TBSXYJP = "TBSXYJP"; // 飞猪森鑫源国际金牌
    public final static String TB_SITE_TBSXYJS = "TBSXYJS"; // 飞猪森鑫源国际急速
    public final static String TB_SITE_TBSXYBTJS = "TBSXYBTJS"; // 飞猪森鑫源商旅急速
    public final static String TB_SITE_TBSXYBTPT = "TBSXYBTPT"; // 飞猪森鑫源商旅普通
    public final static String TB_SITE_TBSXYBTJP = "TBSXYBTJP"; // 飞猪森鑫源商旅金牌
    //驴妈妈平台
    public final static String LVMAMA = "LVMAMA";
    public final static String MEITUAN_MT001 = "MT001";

    public final static String LY_PLATFORM="LY";
    public final static String MAFENGWO_PLATFORM="MAFENGWO";
    public final static String MFW_JS="MFWJS";
    public final static String MEITUAN_PLATFORM="MEITUAN";
    public final static String OWT_PLATFORM="OWT";

    //飞猪
    public final static String LVMAMA_SWITCH = "OTA_SITE_SWITCH_LVMAMA"; //
    public final static String TB_TBSXYPT_SWITCH = "OTA_SITE_SWITCH_TBSXYPT"; // 飞猪森鑫源国际普通开关
    public final static String TB_TBSXYJP_SWITCH = "OTA_SITE_SWITCH_TBSXYJP"; // 飞猪森鑫源国际金牌开关
    public final static String TB_TBSXYJS_SWITCH = "OTA_SITE_SWITCH_TBSXYJS"; // 飞猪森鑫源国际急速开关
    public static final String  TB_TBSXYBTJS_SWITCH = "OTA_SITE_SWITCH_TBSXYBTJS";// 飞猪森鑫源商旅急速开关
    public static final String  TB_TBSXYBTPT_SWITCH = "OTA_SITE_SWITCH_TBSXYBTPT";// 飞猪森鑫源商旅急速开关

    // 今通
    public final static String JT_PLATFORM = "JINRI"; // 今通代码
    public final static String JT_SITE_JT001 = "JT001"; // 今通行程报价

    /**
     * 京东站点
     */
    public final static String JD_PLATFORM = "JD";

    public final static String TAOBAO_TOP = "tbjp";

    public final static String BASE_CITY = "base_city_";

    //去程标识
    public final static int GO = 0;
    //回程标识
    public final static int THE_RETURN_TRIP = 1;
    //共享航司
    public final static int CS_YES = 1;
    //非共享航司
    public final static int CS_NO = 0;

    public final static String VO_BLACK = "airlineCabinBlackList";
    public static final String OTA_RULE_TYPE_AIRLINE_WHITENESS  = "8";//ota航线白名单
    public static final String OTA_RULE_LIMIT_TRANSIT_TIMES = "9"; //限制中专次数
    public static final String OTA_RULE_LIMIT_TRANSFER_STOP_TIME = "10"; //限制中转停留时间
    public static final String OTA_RULE_RESTRICTED_AIRPORT_LINKS = "12"; //限制机场链接
    public static final String OTA_RULE_LIMIT_SHARED_AIRLINE = "13"; //限制共享航司
    public static final String OTA_RULE_OTA_CHOICEGDS = "35"; //航线选择GDS来源
    public static final String OTA_RULE_LIMIT_MIX_AIRLINE = "14"; //限制混合航司
    public static final String OTA_RULE_ALLOW_OPERATING_CARRIER_EMPTY = "16";//允许承运航司为空
    public static final String OTA_RULE_ALLOW_MULTIPLE_TRIP = "17"; //允许多程
    public static final String OTA_RULE_LIMIT_TRAVEL_DATE_RANGE = "19"; //限制旅行日期范围
    public static final String OTA_RULE_LIMIT_SELL_DATA = "31"; //限制销售时间
    public static final String OTA_RULE_LIMIT_REQUEST_TIMEOUT = "22"; //限制请求超时
    public static final String OTA_RULE_LIMIT_PRICE_CHANGE = "23";//限制验价变价
    public static final String OTA_RULE_LIMIT_CACHE_TIME = "24";//限制缓存时间
    public static final String OTA_RULE_LIMIT_ORDER_TIME_CHANGE =" 25";//限制生单起飞时间变化差
    public static final String OTA_RULE_LIMIT_STOP_LOSS = "26";//限制止损
    public static final String OTA_RULE_LEAVE_MONEY_OVERNIGHT = "27";//隔夜留钱
    public static final String OTA_RULE_PRODUCT_TYPE  = "29";//产品类型
    public static final String OTA_RULE_ROUTE_BLACK_LIST = "36";//航线黑名单

    public static final String GDS_RULE_TYPE_SD = "4"; //GDS航司黑名单
    public static final String GDS_RULE_TYPE_RS = "11";//GDS返回最大结果数
    public static final String GDS_RULE_TYPE_BLACK_VO = "18";//GDS verify order 黑名单有效期
    public static final String GDS_RULE_TYPE_BLACK_VO_KEY="GDS_18";
    public static final String GDS_RULE_TYPE_AIRFIEID = "7";//GDS航线黑名单
    public static final String GDS_RULE_ALL = "ALL";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy/MM/dd";
    public static final String DATE_FORMAT_YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final int SUCCEED_STATUS = 0;//成功
    public static final String GLOBAL_POLICY_PRE = "api_gp";

    public static final String AIRPORT_CODE = "1";

    public static final String CITY_CODE = "2";

    public static final String COUNTRY_CODE = "4";

    public static final String REGIONAL_CODE = "8";

    public static final String TC_AREA_CODE = "16";

    public static final String SEARCH = "search";
    public static final String VERIFY = "verify";
    public static final String ORDER = "order";
    public static final String VERIFY_SEARCH = "verify/search";
    public static final String ORDER_SEARCH = "order/search";
    public static final String AUX_SEARCH = "aux_search";
    public static final String AUX_VERIFY = "aux_verify";
    public static final String AUX_ORDER = "aux_order";
    public static final String CTRIP_ONE_SITE = "CT001";
    public static final String CTRIP_TWO_SITE = "CT002";
    public static final String CTRIP_THE_SITE = "CT003";
    public static final String CTRIP_PUB = "CTRIP-P01";
    public static final String CTRIP_PRV = "CTRIP-P02";
    public static final String TUNIU_PUB = "TUNIU-P06";
    public static final String TUNIU_PRV = "TUNIU-P07";
    public static final String QUNAR_PUB = "QUNAR-P06";
    public static final String QUNAR_PRV = "QUNAR-P07";
    public static final String LVMAMA_PUB = "LVMAMA-P01";
    public static final String LVMAMA_PRV = "LVMAMA-P02";
    public static final String MEITUAN_PUB = "MEITUAN-P01";
    public static final String MEITUAN_PRV = "MEITUAN-P02";
    public static final String JINRI_PUB = "JINRI-P01";
    public static final String JINRI_PRV = "JINRI-P02";
    public static final String FLIGHT_CLASS_TYPE_N = "N";
    public static final String FLIGHT_CLASS_TYPE_I = "I";
    public static final String CNY ="CNY";
    public static final String ONE_WAY = "1";
    public static final String BACK_AND_FORTH = "2";
    public static final String MULTIPASS = "5";

    //直连订单创建
    public static final String RESERVATION_STATUS_INIT = "2";
    //直连订单创建成功，等待支付
    public static final String RESERVATION_STATUS_UNPAID = "0";
    //直连订单创建成功，已经支付
    public static final String RESERVATION_STATUS_PAID= "1";
    public static final String GENDER_MALE = "MALE";
    public static final String GENDER_FEMALE = "FEMALE";
    public static final String PASSENGER_TYPE_ADU = "ADU";
    public static final String PASSENGER_TYPE_CHD = "CHD";
    public static final String PASSENGER_TYPE_INF = "INF";
    public static final String CARD_TYPE_PASSPORT = "PASSPORT";
    public static final String CARD_TYPE_HMP = "HMP";
    public static final String CARD_TYPE_TP = "TP";
    public static final String CARD_TYPE_MTP = "MTP";
    public static final String CARD_TYPE_RP = "RP";
    public static final String CARD_TYPE_SEA = "SEA";

    public static final String AIRPORT_CACHE_PREFIX = "base_airport_";
    public static final String PLACE_INFO_CITY = "city_code";

    public static final int OTA_RULE_OVERNIGHT = 27;
    public static final String OTA_RULE_OVERNIGHT_TIME_ZONE_START = "start";
    public static final String OTA_RULE_OVERNIGHT_TIME_ZONE_END = "end";


    public static final String PRICE_RATE_HKD_TO_CNY = "HKD";
    public static final String PRICE_RATE_CACHE_NAME = "exchange_rate_key";
    public static final int PASSENGERTYPE_ADU = 0;
    public static final int PASSENGERTYPE_CHD = 1;
    public static final int IS_BAGAGGER_GDS = 1;
    public static final int IS_BAGAGGER_OK = 2;
    public static final String CTRIP_P03 = "CTRIP-P03";
    public static final String TUNIU_P03 = "TUNIU-P03";
    public static final String MEITUAN_P03 = "MEITUAN-P03";
    public static final String QUNAR_P03 = "QUNAR-P03";
    public static final String LVMAMA_P03 = "LVMAMA-P03";
    public static final String JINRI_P03 = "JINRI-P03";

    public static final String  AMADEUS_URL = "gds.amadeus.url";
    public static final String  AMADEUS_SEARCH_URL = "gds.amadeus.search";
    public static final String  AMADEUS_PNRRETRIEVE = "/api/amadeus/air/pnrRetrieve";
    public static final String  AMADEUS_PNRCANCEL = "/api/amadeus/air/pnrCancel";
    public static final String  AMADEUS_SEARCH = "/api/amadeus/air/search";
    public static final String  AMADEUS_VERIFY = "/api/amadeus/air/verify";
    public static final String  AMADEUS_ORDER = "/api/amadeus/air/order";
    public static final String  AMADEUS_GETPRICE = "/api/amadeus/air/getPrice";
    public static final String  AMADEUS_ORDERNOPRICE = "/api/amadeus/air/orderNoPrice";


    public static final String  GALILEO_URL = "gds.galileo.url";
    public static final String  GALILEO_SEARCH_URL = "gds.galileo.search";
    public static final String  GALILEO_PNRRETRIEVE = "/api/galileo/air/pnrRetrieve";
    public static final String  GALILEO_PNRCANCEL = "/api/galileo/air/pnrCancel";
    public static final String  GALILEO_SEARCH = "/api/galileo/air/search";
    public static final String  GALILEO_VERIFY = "/api/galileo/air/verify";
    public static final String  GALILEO_ORDER = "/api/galileo/air/order";
    public static final String  GALILEO_GETPRICE = "/api/galileo/air/getPrice";
    public static final String  GALILEO_ORDERNOPRICE = "/api/galileo/air/orderNoPrice";

    public static final String  ORDER_CONTACT_NAME = "HONG KONG NG CHOW INTERNATIONAL TRAVEL LTD - WEBSVC";
    public static final String  ORDER_CONTACT_MOBILE = "852-31055092";
    public static final String  ORDER_CONTACT_ADDRESS = "FLAT G, 15/F, KINGLAND APARTMENTS, 739 NATHAN ROAD MONGKOK, KOWLOON, HK";
    public static final String  SESSION_ID = "fd888r9feskof894dcvaq23d";
    public static final String  SWITCH = "OTA_SITE_SWITCH";
    public static final String  CTRIP_ONE_SWITCH = "OTA_SITE_SWITCH_CT001";
    public static final String  CTRIP_TWO_SWITCH = "OTA_SITE_SWITCH_CT002";
    public static final String  CTRIP_THE_SWITCH = "OTA_SITE_SWITCH_CT003";
    public static final String  TUNIU_ONE_SWITCH = "OTA_SITE_SWITCH_TN001";
    public static final String  QUNAR_SXG_SWITCH = "OTA_SITE_SWITCH_SXG";
    public static final String  QUNAR_SBH_SWITCH = "OTA_SITE_SWITCH_SBH";
    public static final String  QUNAR_SXI_SWITCH = "OTA_SITE_SWITCH_SXI";
    public static final String  QUNAR_SXM_SWITCH = "OTA_SITE_SWITCH_SXM";
    public static final String  QUNAR_SXL_SWITCH = "OTA_SITE_SWITCH_SXL";
    public static final String  QUNAR_SBI_SWITCH = "OTA_SITE_SWITCH_SBI";
    public static final String  QUNAR_XNW_SWITCH = "OTA_SITE_SWITCH_XNW";
    public static final String  MEITUAN_MT001_SWITCH = "OTA_SITE_SWITCH_MT001";
    public static final String OTA_ALL = "OTA-ALL";
    public static final String OTA_PREIFX = "OTA-";
    public static final long POINTS = 10;
    public static final String INVOICETYPE = "ctrip.invoicetype";


    public static final String[] BRUSH = {
        TB_TBSXYBTPT_SWITCH
    };
    public static final String SXY_DATA_KEY = "sxysibe.ota.data.key";

    public static final String OTA_PRODUCT_TYPE_ALITRIP = "FLIGGY-P03";
    public static final int POLICY_HAVE_NOT_POLOCY = -1;

    public final static String GALILEO = "1G"; //1G 的简单标识
    public final static String AMADEUS ="1A";//1A 的简单标识
    public final static String SABRE ="1S";//1A 的简单标识
    public final static String LCC ="LCC";//1A 的简单标识

    public final static String CABIN_GRADE_ECONOMY ="Y";//GDS返回的舱等 Y: 经济舱-Economy Class
    public final static String CABIN_GRADE_ECONOMY_PREMIUM ="W";//GDS返回的舱等 W: 经济特舱-Economy Class Premium
    public final static String CABIN_GRADE_ECONOMY_DISCOUNTED ="M";//GDS返回的舱等 M: 经济优惠舱-Economy Class Discounted
    public final static String CABIN_GRADE_BUSSINESS ="C";//GDS返回的舱等 C: 商务舱-Business Class
    public final static String CABIN_GRADE_FIRST ="F";//GDS返回的舱等 F: 头等舱-First Class

    public static final String REFUND_OR_CHANGE_TYPE_NO = "T";//GDS退票改签标识 不可退改
    public static final String REFUND_OR_CHANGE_TYPE_HAVE_CONDITION = "H";//GDS退票改签标识 有条件退改
    public static final String REFUND_OR_CHANGE_TYPE_FREE = "F";//GDS退票改签标识 免费退改
    public static final String REFUND_OR_CHANGE_ACCORDING_TO_AIRLINE = "E";//GDS退票改签标识 以航司规定为准

    public static final int REFUND_OR_CHANGE_TYPE_ALL_NO_USE = 0;//GDS客票全部未使用
    public static final int REFUND_OR_CHANGE_TYPE_PART_USE = 1;//GDS客票部分使用

    public static final int BAGGAGE_WEIGHT_KG_DEFAULT = 20;//默认行李额重量
    public static final int SEARCH_CONTENT_CACHE_VALID_TIME = 20;//ota站点search缓存默认有效时间

    public static final String REQUEST_GDS_TYPE_VERIFY_PRICE = "1";//请求类型，1：验价
    public static final String REQUEST_GDS_TYPE_VERIFY_CABIN = "2";//请求类型，2：验舱；3验价+验舱
    public static final String REQUEST_GDS_TYPE_VERIFY_PRICE_AND_CABIN = "3";//请求类型 3验价+验舱

    public static final String GDS_TRIP_TYPE_ONE_WAY = "1"; //gds行程类型 单程
    public static final String GDS_TRIP_TYPE_ROUND_WAY = "2"; //gds行程类型 往返
    public static final String GDS_TRIP_TYPE_OPEN_JAW = "3";//gds行程类型 多程

    public static final int GDS_RETURN_STATUS_SUCCESS = 0;//gds返回状态 成功
    public static final int GDS_RETURN_STATUS_ERROR = -1;//gds返回状态 失败

    public static final String REQUEST_RECORD_TYPE_VERIFY = "verify";//写入请求记录表类型 验价
    public static final String REQUEST_RECORD_TYPE_ORDER = "order";//写入请求记录表类型 生单

    public static final int REQUEST_RECORD_SIGN_SUCCESS = 0;//写入请求记录表成功与否标志 成功
    public static final int REQUEST_RECORD_SIGN_FAIL = 1;//写入请求记录表成功与否标志 失败

    public static final String CONFIG_GALILEO_ORDER_NAME = "gds.galileo.order.contact.name";
    public static final String CONFIG_GALILEO_ORDER_ADDRESS = "gds.galileo.order.contact.address";
    public static final String CONFIG_GALILEO_ORDER_MOBILE = "gds.galileo.order.contact.mobile";
    public static final String CONFIG_GALILEO_ORDER_CTCT = "gds.galileo.order.contact.ctct";
    public static final String CONFIG_AMADEUS_ORDER_NAME = "gds.amadeus.order.contact.name";
    public static final String CONFIG_AMADEUS_ORDER_ADDRESS = "gds.amadeus.order.contact.address";
    public static final String CONFIG_AMADEUS_ORDER_MOBILE = "gds.amadeus.order.contact.mobile";
    public static final String CONFIG_AMADEUS_ORDER_CTCT = "gds.amadeus.order.contact.ctct";

    public static final int ORDER_TYPE_PNR_CANCEL_STATUS_PENDING = 0;//PNR等待取消
    public static final int ORDER_TYPE_PNR_CANCEL_STATUS_CANCEL_SUCESS = 1;//1：取消成功
    public static final int ORDER_TYPE_PNR_CANCEL_STATUS_CANCEL_FAIL = 2;//2：取消失败

    public static final String GDS_GENDER_MALE = "M"; //gds乘客类型
    public static final String GDS_GENDER_FEMALE = "F"; //gds乘客类型

    public static final String GDS_CARD_TYPE_PASSORT = "PP"; //gds证件类型
    public static final String GDS_CARD_TYPE_GA = "GA"; //港澳通行证
    public static final String GDS_CARD_TYPE_TW = "TW"; //台湾通行证
    public static final String GDS_CARD_TYPE_TB = "TB"; //台胞证
    public static final String GDS_CARD_TYPE_HX = "HX"; //回乡证
    public static final String GDS_CARD_TYPE_HY = "HY"; //国际海员证

    public static final String ORDER_TYPE_CARD_TYPE_PASSPORT = "PASSPORT"; //护照
    public static final String ORDER_TYPE_CARD_TYPE_HMP = "HMP"; //港澳通行证
    public static final String ORDER_TYPE_CARD_TYPE_TP = "TP"; //台湾通行证
    public static final String ORDER_TYPE_CARD_TYPE_MTP = "MTP"; //台胞证
    public static final String ORDER_TYPE_CARD_TYPE_RP = "RP"; //回乡证
    public static final String ORDER_TYPE_CARD_TYPE_SEA = "SEA"; //国际海员证

    public static final int GDS_AGE_TYPE_ADULT = 0; //成人
    public static final int GDS_AGE_TYPE_CHILD = 1; //儿童
    public static final int GDS_AGE_TYPE_INF = 2; //婴儿

    public static final String ORDER_AGE_TYPE_ADULT = "ADU"; //成人
    public static final String ORDER_AGE_TYPE_CHILD = "CHD"; //儿童
    public static final String ORDER_AGE_TYPE_INF = "INF"; //婴儿

    public static final String ORDER_PASSENGER_TYPE_NORMAL = "NOR"; //订单乘客类型NOR

    public static final String DEFAUL_USER_NAME = "System"; //默认用户名

    public static final String ORDER_STATUS_NORMAL_STATUS_LIST = "HK,KL,KK,TK"; //预定后航段的正常状态
    public static final String RETURN_MSG_VERIFY_PRICE_CHANGE = "verify fail,price change";
    public static final String RETURN_MSG_VERIFY_PRICE_NOT_FOUND = "verify fail,price not found";
    public static final String RETURN_MSG_VERIFY_OTHER_ERROR = "verify fail,other error";
    public static final String RETURN_MSG_ORDER_GDS_ORDER_FAIL = "gds order fail";
    public static final String RETURN_MSG_ORDER_RT_ERROR = "The flight pnr response error";
    public static final String RETURN_MSG_SESSION_ID_ERROR = "Sessionid error";
    public static final String RETURN_MSG_OTHER_ERROR = "other error";
    public static final String RETURN_MSG_PAY_ERROR_STATUS_ERROR = "PNR has failed, the segment state error is returned";
    public static final String RETURN_MSG_PAY_ERROR_PNR_CANCELED = "pnr canceled!";
    public static final String RETURN_MSG_CARD_NUMBER_IS_TOO_LONG = "card number is too long";

    public static final String DATA_NO_CONTENT_SIGN = "no";


    public static final int METHOD_TYPE_SEARCH = 1;//search过程生成
    public static final int METHOD_TYPE_VERIFY = 2;//verify过程生成
    public static final int METHOD_TYPE_ORDER = 3;//
    public static final int METHOD_TYPE_PAY = 4;//

    public static final int METHOD_TYPE_FROM_AND_OPEN_JAW_SIGN = 1;//去程或者多程标志
    public static final int METHOD_TYPE_RET_SIGN = 2;//回程标志

    public static final String GDS_PRICE_TYPE_PUB = "PUB";
    public static final String GDS_PRICE_TYPE_PRV = "PRV";
    public static final String GDS_PRICE_TYPE_LCC = "LCC";

    public static final int REDIS_OPERATION_SAVE_OR_UPDATE = 1;//缓存操作类型 新增或者更新
    public static final int REDIS_OPERATION_DELETE = 2;//缓存操作类型 删除


    /**
     * 虚拟卡类型:VC1A 、VCTP 、VC1G
     */
    public static final String VC_TYPE_1A = "VC1A";
    /**
     * 虚拟卡类型:VC1A 、VCTP 、VC1G
     */
    public static final String VC_TYPE_TP = "VCTP";
    /**
     * 虚拟卡类型:VC1A 、VCTP 、VC1G
     */
    public static final String VC_TYPE_1G = "VC1G";


    /**
     * GDS类型:1A
     */
    public static final String GDS_TYPE_1A = "1A";
    /**
     * GDS类型:1G
     */
    public static final String GDS_TYPE_1G = "1G";
    /**
     * GDS类型:1S
     */
    public static final String GDS_TYPE_1S = "1S";
    /**
     * GDS类型:LCC
     */
    public static final String GDS_TYPE_LCC = "LCC";
    /**
     * GDS类型:JT
     */
    public static final String GDS_TYPE_JT = "JT";
    /**
     * GDS类型:VJ
     */
    public static final String GDS_TYPE_VJ = "VJ";
    /**
     * GDS类型:9C
     */
    public static final String GDS_TYPE_9C = "9C";


    //普通验价
    public static final String PRODUCT_TYPE_NORMAL = "1";
    //K位验价
    public static final String PRODUCT_TYPE_K_SEAT = "2";

    private Constants() {
    }
}
