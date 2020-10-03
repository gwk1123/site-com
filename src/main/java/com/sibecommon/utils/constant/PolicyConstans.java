package com.sibecommon.utils.constant;

public class PolicyConstans {
    public static final int SEGMENT_TYPE_MASTER = 1; //主航段
    public static final int SEGMENT_TYPE_SUB = 2; //附属航段

    public static final int ROUTING_TRANSFER_TYPE_TRANSFER = 1; //中转
    public static final int ROUTING_TRANSFER_TYPE_DIRECT = 2; //直达
    public static final int ROUTING_TRANSFER_TYPE_MIX = 3; //整个行程中包含中转和直飞

    public static final int INTERLINE_SIGN_YES = 1; //是联运
    public static final int INTERLINE_SIGN_CONTAIN = 4;//包含混合航司
    public static final int INTERLINE_SIGN_NO = 2; //非联运
    public static final int INTERLINE_SIGN_UNKNOW = 3; //因为GDS数据信息问题，无法获知联运信息

    public static final String TRIP_TYPE_ONE_WAY = "1";
    public static final String TRIP_TYPE_ROUND_WAY = "2";
    public static final String TRIP_TYPE_OPEN_JAW = "3";

    public static final String POLICY_CACHE_PREFIX = "api_p_";

    public static final int MASTER_SEGMENT_VALIDATE_CABIN= 1; //校验舱位
    public static final int MASTER_SEGMENT_VALIDATE_GRADE= 2; //校验舱等

    public static final int POLICY_YES_NO_UNLIMITED_YES = 1; //允许类型 不限
    public static final int POLICY_YES_NO_UNLIMITED_NO = 2; //允许类型 不限
    public static final int POLICY_YES_NO_UNLIMITED_UNLIMITED = 3; //允许类型 不限
    public static final int POLICY_YES_NO_UNLIMITED_CONTAIN= 4;


    public static final  int POLICY_CHILD_PRICE_TYPE_SAME_AS_ADULT = 2; //儿童与成人同价
    public static final  int POLICY_CHILD_PRICE_TYPE_ASSIGN = 4; //儿童自定义票价

    public static final int PERMIT_TYPE_YES= 1;//全局政策 是
    public static final int PERMIT_TYPE_NO= 2;//全局政策 否
    public static final int PERMIT_TYPE_UNLIMITED= 3;//全局政策 不限
    public static final int PERMIT_TYPE_CONTAIN = 4;

    public static final String CACHE_PCC_PREFIX = "base_pcc_";
    public static final String CACHE_PCC_NAME_TIME_ZONE = "time_zone";

    public static final String POLICY_TRIP_TYPE_ONE_WAY = "OW";
    public static final String POLICY_TRIP_TYPE_ROUND_WAY = "RT";
    public static final String POLICY_TRIP_TYPE_OPEN_JAW = "MT";

    public static final String POLICY_TRIP_TYPE_ALL = "ALL"; //行程类型不限
    public static final String POLICY_PRICE_TYPE_ALL= "ALL"; //运价类型不限
    public static final String POLICY_AIRLINE_ALL= "ALL"; //航司不限

    public static final int POLICY_HAVE_NOT_CHILD_PRICE_TYPE = -1;

    public static final String INVOICE_TYPE_FULL_TRAVEL_ITINERARY = "1";//全额行程单
    public static final String INVOICE_TYPE_TRAVEL_INVOICE = "2";//旅游发票
    public static final String INVOICE_TYPE_ELECTRONIC_INVOICE = "3";//境外电子凭证

    public static final int BAGGAGE_TYPE_GDS = 1;//GDS行李额
    public static final int BAGGAGE_TYPE_ASSIGN = 2;//指定行李额

    public static final int BAGGAGE_PASSENGER_TYPE_ADULT = 0;//乘客类型 成人
    public static final int BAGGAGE_PASSENGER_TYPE_CHILD = 1;//乘客类型 儿童
    public static final int BAGGAGE_PASSENGER_TYPE_INF = 2;//乘客类型 婴儿

}
