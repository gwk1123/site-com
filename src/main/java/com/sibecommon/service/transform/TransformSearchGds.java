package com.sibecommon.service.transform;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibecommon.ota.gds.GDSSearchResponseDTO;
import com.sibecommon.ota.gds.Routing;
import com.sibecommon.config.SibeProperties;
import com.sibecommon.ota.gds.GDSSearchRequestDTO;
import com.sibecommon.ota.site.*;
import com.sibecommon.ota.site.*;
import com.sibecommon.ota.site.*;
import com.sibecommon.repository.entity.PolicyGlobal;
import com.sibecommon.repository.entity.PolicyInfo;
import com.sibecommon.service.ota.OtaRuleFilter;
import com.sibecommon.utils.constant.PolicyConstans;
import com.sibecommon.utils.constant.SibeConstants;
import com.sibecommon.utils.exception.CustomSibeException;
import com.sibecommon.utils.redis.impl.ExchangeRateRepositoryImpl;
import com.sibecommon.utils.redis.impl.PolicyGlobalRepositoryImpl;
import com.sibecommon.utils.redis.impl.PolicyInfoRepositoryImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * Created by yangdehua on 18/2/11.
 */
@Component
public class TransformSearchGds {


    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TransformSearchGds.class);
    private ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    private PolicyInfoRepositoryImpl apiPolicyInfoRedisRepository;
    @Autowired
    private PolicyGlobalRepositoryImpl apiPolicyGlaobalRedisRepository;

    @Autowired
    private SibeProperties sibeProperties;

    @Autowired
    private ExchangeRateRepositoryImpl exchangeRateRepository;

    @Autowired
    private PolicyGlobalRepositoryImpl policyGlobalRepositoryImpl;
    @Autowired
    private TransformCommonPolicy transformCommonPolicy;

    /**
     * Convert search request to gds gds search request dto.
     *
     * @param sibeSearchRequest the ota search request
     * @return the gds search request dto
     */
    public final static GDSSearchRequestDTO convertSearchRequestToGDS(final SibeSearchRequest sibeSearchRequest) {

        GDSSearchRequestDTO gDSSearchRequestDTO = new GDSSearchRequestDTO();


        // 访问方的唯一标识
        gDSSearchRequestDTO.setApiKey(sibeSearchRequest.getAppKey());

        // 行程类型，1：单程；2：往返；3：多段；
        gDSSearchRequestDTO.setTripType(sibeSearchRequest.getTripType());

        // 出发地城市 IATA 三字码代码
        // 如果为多程，会按照 PEK/HKG，HKG/SHA 格式请求，第一程为北京->香港，第二程为香港->上海
        // todo 后继版本迭代更新优化 (目前不能够支持多程，需要解析多程代码）
        gDSSearchRequestDTO.setFromCity(sibeSearchRequest.getFromCity());

        //目的地城市 IATA 三字码代码
        gDSSearchRequestDTO.setToCity(sibeSearchRequest.getToCity());

        //出发日期，格式为 YYYYMMDD
        //如果为多程，20130729,20130804方式传输数据
        //@JsonFormat(pattern = "YYYYMMDD")
        gDSSearchRequestDTO.setFromDate(sibeSearchRequest.getFromDate());

        //回程日期，格式为 YYYYMMDD（留空表示单程/多程）
        //@JsonFormat(pattern = "YYYYMMDD")
        gDSSearchRequestDTO.setRetDate(sibeSearchRequest.getRetDate());

        // 成⼈人数，0-9
        gDSSearchRequestDTO.setAdultNumber(1);

        // todo 后继版本迭代更新优化 (目前我们请求GDS 只传一个成人）
        //⼉童人数，0-9
//        gDSSearchRequestDTO.setChildNumber(sibeSearchRequest.getChildNumber());
        // 婴儿
//        gDSSearchRequestDTO.setInfantNumber(sibeSearchRequest.getInfantNumber());


        // todo 后继版本迭代更新优化 (根据OTA传入舱位等级进行查询）
        // 舱位等级:
        // Y: 经济舱-Economy Class;
        // W: 经济特舱-Economy Class Premium;
        // M: 经济优惠舱-Economy Class Discounted
        // F: 头等舱-First Class;
        // C: 公务舱-Business Class;

        gDSSearchRequestDTO.setCabin("Y");


        //航空公司
        gDSSearchRequestDTO.setAirline(sibeSearchRequest.getCarriers());

        // 请求GDS平台
        // 1E：TravelSky
        // 1A：Amadeus
        // 1B：Abacus
        // 1S：Sabre
        // 1P：WorldSpan
        // 1G：Galileo
        gDSSearchRequestDTO.setReservationType(sibeSearchRequest.getGds());

        // 调用GDS时使用的对应账号（OfficeId或PCC）
        gDSSearchRequestDTO.setOfficeId(sibeSearchRequest.getOfficeId());

        // 关联所有请求返回（原数据直接返回）
        gDSSearchRequestDTO.setUid(sibeSearchRequest.getUuid());

        //请求渠道
        gDSSearchRequestDTO.setRequestSource(sibeSearchRequest.getOta() + "_" + sibeSearchRequest.getSite());

        //请求返回记录最大数
        gDSSearchRequestDTO.setNumberOfUnits(sibeSearchRequest.getNumberOfUnits());

        //拒绝航空公司 黑名单
        //todo
        gDSSearchRequestDTO.setProhibitedCarriers(sibeSearchRequest.getProhibitedCarriers());

        gDSSearchRequestDTO.setAirline(sibeSearchRequest.getAirline());

        gDSSearchRequestDTO.setFlightType(sibeSearchRequest.getFlightType());

        //不需要传，返回PCC运营当地币种
//        gDSSearchRequestDTO.setCurrency("HKD");


        return gDSSearchRequestDTO;

    }

    /**
     * Convert search response to sibe sibe search response.
     *
     * @param gDSSearchResponseDTO the g ds search response dto
     * @param sibeSearchRequest    the sibe search request
     * @return the sibe search response
     */
    public SibeSearchResponse convertSearchResponseToSibe(GDSSearchResponseDTO gDSSearchResponseDTO, SibeSearchRequest sibeSearchRequest) {

        if(gDSSearchResponseDTO==null) {return null;}

        SibeSearchResponse sibeSearchResponse= new SibeSearchResponse();

        //Status
        //0, 成功；
        //1,其他失败原因；
        //2,请求参数错误；
        //3,程序异常；
        //6,GDS返回异常
        sibeSearchResponse.setStatus(TransformCommonGds.convertResponseStatusToSibe(gDSSearchResponseDTO.getStatus()));

        //提示信息，长度小于 64
        sibeSearchResponse.setMsg(gDSSearchResponseDTO.getMsg());

        //UUID
        sibeSearchResponse.setUuid(gDSSearchResponseDTO.getUid());

        //GDS正常返回
        if(gDSSearchResponseDTO.getStatus() == 0 && gDSSearchResponseDTO.getRoutings() != null) {
            //报价信息
            sibeSearchResponse.setRoutings(routingToSibe(gDSSearchResponseDTO.getRoutings(),sibeSearchRequest));

            //Redis缓存有效时间
            sibeSearchResponse.setCacheValidTime(sibeSearchRequest.getGdsCacheValidTime());

            //Redis缓存有效时间
            sibeSearchResponse.setCacheRefreshTime(sibeSearchRequest.getOtaCacheRefreshTime());


            //缓存生成的时间
            sibeSearchResponse.setTimeLapse(SystemClock.now());

            //GDS
            sibeSearchResponse.setGds(sibeSearchRequest.getGds());

            //PCC
            sibeSearchResponse.setOfficeId(sibeSearchRequest.getOfficeId());
        }

        return sibeSearchResponse;
    }

    //转换routing
    List <SibeRouting> routingToSibe(List<Routing> sibeList, SibeSearchRequest sibeSearchRequest){

        if ( sibeList == null ) {
            return null;
        }

        List<SibeRouting> list = new ArrayList<SibeRouting>( sibeList.size() );

        //key 币种，值
        Map <String ,BigDecimal> rateMap = exchangeRateRepository.findAllExchangeRate() ;

        for ( Routing routing : sibeList ) {
            //添加officeID 和 GDS
            routing.setOfficeId(sibeSearchRequest.getOfficeId());
            routing.setReservationType(sibeSearchRequest.getGds());
            SibeRouting sibeRouting= this.toSibeRouting( routing );


            //汇率处理GDS 转人民币 汇率
            //设置人民币种

                //汇率计算价格
                 if( null == rateMap.get("CNY"+sibeRouting.getCurrencyGDS())){
                     throw new CustomSibeException(SibeConstants.RESPONSE_MSG_2, "汇率数据错误【ExchangeUnit不是100】 ", sibeSearchRequest.getUuid(),"search");
                 }


                sibeRouting.setGdsToCNYRate(rateMap.get("CNY"+sibeRouting.getCurrencyGDS()));

                sibeRouting.setAdultPriceCNY1(getInteger(sibeRouting.getAdultPriceGDS(),rateMap.get("CNY"+sibeRouting.getCurrencyGDS())));
                sibeRouting.setAdultTaxCNY1(getInteger(sibeRouting.getAdultTaxGDS(),rateMap.get("CNY"+sibeRouting.getCurrencyGDS())));
                sibeRouting.setChildPriceCNY1(getInteger(sibeRouting.getChildPriceGDS(),rateMap.get("CNY"+sibeRouting.getCurrencyGDS())));
                sibeRouting.setChildTaxCNY1(getInteger(sibeRouting.getChildTaxGDS(),rateMap.get("CNY"+sibeRouting.getCurrencyGDS())));
                sibeRouting.setInfantsPriceCNY1(getInteger(sibeRouting.getInfantPriceGDS(),rateMap.get("CNY"+sibeRouting.getCurrencyGDS())));
                sibeRouting.setInfantsTaxCNY1(getInteger(sibeRouting.getInfantTaxGDS(),rateMap.get("CNY"+sibeRouting.getCurrencyGDS())));



            list.add(sibeRouting );
        }

        return list;
    }


    /**
     * OTA规则过滤，及政策过滤
     *
     * @param sibeSearchResponseList the sibe search response list
     * @param sibeSearchRequest      the sibe search request
     * @return the list
     */
    public List<SibeRouting> filterProcessRouting(final List<SibeSearchResponse> sibeSearchResponseList,
                                                  final SibeSearchRequest sibeSearchRequest) {

        String uuid = sibeSearchRequest.getUuid();
        String site = sibeSearchRequest.getSite();

//        List<SibeRouting> routingList = new ArrayList<>();

        int i = 0 ;
        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 3.9.0 进filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");
/*

        //飞猪先试用共享航班已1G数据未准
        Map<String, SibeSegment>  shareMap1G =  new ConcurrentHashMap<>() ;
        if("TBSXYPT".equals(sibeSearchRequest.getSite())){
            sibeSearchResponseList.stream().forEach(sibeSearchResponse->{
                if( sibeSearchResponse.getStatus() == 0 ){
                    sibeSearchResponse.getRoutings().stream().forEach(sibeRouting->{
                        Stream.of(sibeRouting.getFromSegments(),sibeRouting.getRetSegments()).flatMap(Collection::stream).forEach(segment->{
                            if("1G".equals(sibeRouting.getReservationType()) && segment.getCodeShare()) {
                                shareMap1G.put(
                                    segment.getCarrier()+ segment.getFlightNumber()+ segment.getDepAirport()+ segment.getArrAirport()+ segment.getDepTime()+ segment.getCabin(),
                                    segment) ;
                            }
                        });
                    });
                }
            });
        }

        //方案
        Map<String ,SibeRouting> routingMap = new ConcurrentHashMap<>() ;

        //是否过滤配置人数的仓位
        int cabinCnt = filterSuccessCabinService.getSuccessCabin(sibeSearchRequest.getOta(),sibeSearchRequest.getSite());

        for(int y = 0 ; y < sibeSearchResponseList.size() ; y++ ){
            SibeSearchResponse sibeSearchResponse  = sibeSearchResponseList.get(y) ;
            if(sibeSearchResponse.getStatus()==0){
                for( int j = 0 ; j < sibeSearchResponse.getRoutings().size() ;j++){
                    SibeRouting sibeRouting = sibeSearchResponse.getRoutings().get(j) ;

                    //暂时飞猪普通 ,替换参数 共享标识，承运航司，承运航班号
                    if("TBSXYPT".equals(sibeSearchRequest.getSite()) && "1A".equals(sibeRouting.getReservationType())){
                        Stream.of(sibeRouting.getFromSegments(),sibeRouting.getRetSegments()).flatMap(Collection::stream).forEach(segment->{
                            SibeSegment  sibeSegment1G = shareMap1G.get( segment.getCarrier()+ segment.getFlightNumber()+ segment.getDepAirport()+ segment.getArrAirport()+ segment.getDepTime()+ segment.getCabin()) ;
                            if( null != sibeSegment1G ) {
                                segment.setCodeShare(sibeSegment1G.getCodeShare());
                                segment.setOperatingCarrier(sibeSegment1G.getOperatingCarrier());
                                segment.setOperatingFlightNo(sibeSegment1G.getOperatingFlightNo());
                            }
                        });
                    }

                    //第一个步骤 复制对象
                    SibeRouting routing = CopyUtils.deepCopy(sibeRouting) ;
//                    LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 3.9.1 复制对象:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

                    //id
                    i = i + 1;
                    //第二个步骤 设置ID
                    routing.setId(i);
                    routing.setUid(uuid + "-" + routing.getId()+ "-"+ routing.getReservationType());
                    routing.setGdsCacheTimeLapse(sibeSearchResponse.getTimeLapse());

                    //第二个步骤 OTA 规则
                    if(!OtaRuleFilter.otaRuleFilter(sibeSearchRequest, routing,null)){
//                        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 3.9.2 OTA 规则:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");
                         continue;
                    }
//                    LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 3.9.3 OTA 规则:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

                    //第三个不步骤过滤 含飞机外的方案
                    if(Stream.of(routing.getFromSegments(),routing.getRetSegments())
                        .flatMap(Collection::stream)
                        .anyMatch(segment->(StringUtils.isNotEmpty(segment.getAircraftCode())
                            && segment.getAircraftCode().length() >= 3
                            && SibeUtil.containsNotEmpty("BUS|ICE|LCH|LMO|MTL|RFS|TGV|THS|THT|TRN|TSL",segment.getAircraftCode().substring(0,3),"|")))){
//                        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 3.9.4 OTA 含飞机外的方案:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");
                        continue;
                    }
//                    LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 3.9.5 OTA 含飞机外的方案:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

                    //判断站点选择GDS选数据
                    if(!StringUtils.isEmpty(sibeSearchRequest.getOtaSiteAirRouteChooseGDS())){
                        if(!sibeSearchRequest.getOtaSiteAirRouteChooseGDS().contains(routing.getReservationType())){
                            continue;
                        }
                    }

                    //是否过滤配置人数的仓位
                   if( cabinCnt > 0 ){
                       if( routing.getMaxPassengerCount() <= cabinCnt ){
                           continue;
                       }
                   }

                       //去重
                    String routingKey =
                        Stream.of(routing.getFromSegments(),routing.getRetSegments())
                            .flatMap(Collection::stream)
                            .sorted(Comparator.comparingInt(SibeSegment::getItemNumber))
                            .map(segment->(segment.getItemNumber()
                                + segment.getCarrier()
                                + segment.getFlightNumber()
                                + segment.getDepAirport()
                                + segment.getArrAirport()
                                + segment.getDepTime()
                                + segment.getCabin()))
                            .collect(Collectors.joining("-"));

                    if(routingMap.containsKey(routingKey)){
                        //当存在 ，判断价格是否小于原来的方案
                        if((routing.getAdultPriceCNY1()+routing.getAdultTaxCNY1())
                            -(routingMap.get(routingKey).getAdultPriceCNY1()+routingMap.get(routingKey).getAdultTaxCNY1()) < 0 )
                        {
                            //替换原来的方案
                            routingMap.put(routingKey,routing) ;
                        }else{
                            continue;
                        }

                    }
//                    LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 3.9.6 方案去重:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

                    routingMap.put(routingKey,routing) ;

                }
            }

        }

        //转换list
        routingList = routingMap.values().stream()
            .collect(Collectors.toList());

        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 3.9.1 进filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");
*/



        List<SibeRouting> allRoutingList = new ArrayList<>();
        sibeSearchResponseList
            .stream()
            .filter(Objects::nonNull)
            .filter(sibeSearchResponse->(sibeSearchResponse.getStatus()==0))
            .forEach(sibeSearchResponse->{
                sibeSearchResponse.getRoutings().forEach(
                    sibeRouting-> {allRoutingList.add(SibeRouting.deepCopy(sibeRouting));}
                );
            });

        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.1 进filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

        //2. OTA规则过滤
        // LOGGER.debug("uuid:" + uuid + " 站点" + site + " OTA规则过滤之前方案数：" + allRoutingList.size());

        IntStream.range(0,allRoutingList.size())
            .forEach(index->{
                SibeRouting routing =allRoutingList.get(index);
                routing.setId(index + 1);
                routing.setUid(uuid + "-" + routing.getId()+ "-"+ routing.getReservationType());
//                routing .setStatus(0);
            });

        List<SibeRouting> routingList = allRoutingList
            .stream()
            //ota规则过滤
            .filter(routing -> (OtaRuleFilter.otaRuleFilter(sibeSearchRequest, routing,null)))
            //过滤非飞机航程的运价
            .filter(routing -> (
                !Stream.of(routing.getFromSegments(),routing.getRetSegments())
                    .flatMap(Collection::stream)
                    .anyMatch(segment->(StringUtils.isNotEmpty(segment.getAircraftCode())
                        && segment.getAircraftCode().length() >= 3
                        && SibeUtil.containsNotEmpty("BUS|ICE|LCH|LMO|MTL|RFS|TGV|THS|THT|TRN|TSL",segment.getAircraftCode().substring(0,3),"|")))))
            .collect(Collectors.toList());


        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.2 进filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

        //3. 去掉重复的 Routing,根据 航司，航班号出发日期，舱位
        // LOGGER.debug("uuid:" + uuid + " 站点" + site + " 去重之前方案数：" + routingList.size());
        routingList=processRepeatRouting(routingList);


        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.3 进filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");

        //4.1 得到当前方案所有航司相匹配的全局与明细政策信息，并写入sibeSearchRequest对象
        constructSibeSearchRequestPolicy(sibeSearchRequest,routingList);

        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.5 进filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");


        //4.2 政策匹配及政策处理
        // LOGGER.debug("uuid:" + uuid + " 站点" + site + " 政策处理前方案数：" + routingList.size());
        routingList = transformCommonPolicy.matchPolicy(sibeSearchRequest, routingList);

        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.6 进filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");


//        if(! org.springframework.util.StringUtils.isEmpty(sibeSearchRequest.getOtaSiteAirRouteChooseGDS())){
//            routingList = routingList.stream().filter(sibeRouting -> {
//                if(sibeSearchRequest.getOtaSiteAirRouteChooseGDS().contains(sibeRouting.getReservationType())){
//                    return true;
//                }else {
//                    return false;
//                }
//            }).collect(Collectors.toList());
//        }

        // LOGGER.debug("uuid:" + uuid + " 站点" + site + " 加密数据处理前方案数：" + routingList.size());

        //指定低舱位
        Map<String , String> ruleTypeMap = new HashMap<>();
        ruleTypeMap.put("OTA-30","OTA-30");

        //5. 加密数据
        routingList
            .stream()
            .forEach(routing -> {
                OtaRuleFilter.otaRuleFilter(sibeSearchRequest, routing, ruleTypeMap);
                //1. 处理行李额 todo 后续优化，让GDS返回格式化行李额
                processBaggageToFormat(routing);
                //2. 数据加密
                //注意：需先处理行李额，否则数据加密无法取到行李额信息
                processRouting(routing, sibeSearchRequest);

            });

        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.7 进filterProcessRouting:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");


        // LOGGER.debug("uuid:" + uuid + " 站点" + site + "最终返回方案数：" + routingList.size());
        return routingList;
    }



    /**
     * 得到当前方案所有航司相匹配的全局与明细政策信息
     * Construct ota search request policy.
     *
     * @param sibeSearchRequest the ota search request
     * @param sibeRoutingList   the sibe routing list
     */
    public  void constructSibeSearchRequestPolicy(SibeSearchRequest sibeSearchRequest, List<SibeRouting> sibeRoutingList) {

        //1.将方案按照销售航司进行分类
        Set<String> allValidatingCarrierSet =sibeRoutingList
            .stream()
            .map(routing -> (routing.getValidatingCarrier()))
            .collect(Collectors.toSet());
        String citiesCommaSeparated = allValidatingCarrierSet.stream().collect(Collectors.joining(","));
        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.3.10 进constructSibeSearchRequestPolicy 政策航司列表："+citiesCommaSeparated);

        //2根据站点，获取全局政策的keys
        Set<Object> siteKeysSet = policyGlobalRepositoryImpl.findBySiteKeys(sibeSearchRequest.getSite());

        //3 根据航司，过滤key
        //key: id_sitecode_airine
        Set<Object> siteKeys =  siteKeysSet
            .stream()
            .filter(key->(
                PolicyConstans.POLICY_AIRLINE_ALL.equals(StringUtils.split(key.toString(),"_")[2])
                || allValidatingCarrierSet.stream().anyMatch(carrier->(carrier.equals(StringUtils.split(key.toString(),"_")[2])))))
            .collect(Collectors.toSet());


//        String membersCommaSeparated = siteKeys.stream().map(x->(x.toString())).collect(Collectors.joining(","));

        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.3.12 进constructSibeSearchRequestPolicy:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");
        //根据members 获得得对应的全局政策


        List<PolicyGlobal> policyGlaobalSet  = policyGlobalRepositoryImpl.findBySiteKeys(siteKeys);


        sibeSearchRequest.setPolicyGlobals(policyGlaobalSet);
        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.3.2 进constructSibeSearchRequestPolicy:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");


        //2.根据站点，航司获取明细政策
        List<PolicyInfo> policyInfoList = apiPolicyInfoRedisRepository.findBySiteAndAirline(sibeSearchRequest,"",allValidatingCarrierSet);

        LOGGER.debug("uuid:"+sibeSearchRequest.getUuid() +" 4.1.3.4 进constructSibeSearchRequestPolicy:"+ (SystemClock.now()-sibeSearchRequest.getStartTime())/(1000) +"秒");
        sibeSearchRequest.setPolicyInfos(policyInfoList);
    }


    /**
     * 去掉重复的Routing信息
     * 根据每个航段的，航段编码，航司，航班号，出发机场，到达机场，出发时间，舱位进行比较
     *
     * @param sibeRoutingList the sibe routing list
     * @return the list
     */
    private static List<SibeRouting> processRepeatRouting(List<SibeRouting> sibeRoutingList) {

        //去掉重复比较器
        Comparator<SibeRouting> sibeRoutingcomparator = (routing1, routing2) -> {
            String routingStr1 =
                Stream.of(routing1.getFromSegments(),routing1.getRetSegments())
                    .flatMap(Collection::stream)
                    .sorted(Comparator.comparingInt(SibeSegment::getItemNumber))
                    .map(segment->(segment.getItemNumber()
                        + segment.getCarrier()
                        + segment.getFlightNumber()
                        + segment.getDepAirport()
                        + segment.getArrAirport()
                        + segment.getDepTime()
                        + segment.getCabin()))
                    .collect(Collectors.joining("-"));

            String routingStr2 =
                Stream.of(routing2.getFromSegments(),routing2.getRetSegments())
                    .flatMap(Collection::stream)
                    .sorted(Comparator.comparing(SibeSegment::getItemNumber))
                    .map(segment->(segment.getItemNumber()
                        + segment.getCarrier()
                        + segment.getFlightNumber()
                        + segment.getDepAirport()
                        + segment.getArrAirport()
                        + segment.getDepTime()
                        + segment.getCabin()))
                    .collect(Collectors.joining("-"));

            return routingStr1.compareTo(routingStr2);
        };

        //去重复返回
        List<SibeRouting> bb =  sibeRoutingList
            .stream()
            .sorted((s1, s2)->(s1.getAdultPriceCNY1()+s1.getAdultTaxCNY1()) - (s2.getAdultPriceCNY1()+s2.getAdultTaxCNY1()))
            .collect(Collectors.toList());


        List<SibeRouting> ss =  bb.stream()
            .collect(collectingAndThen(
                toCollection(() -> new TreeSet<>(sibeRoutingcomparator)),
                ArrayList::new));
        return ss;
    }


    /**
     * 处理Routing Data数据
     *
     * @param routing           the routing
     * @param sibeSearchRequest the sibe search request
     */
    private void processRouting( SibeRouting routing, SibeSearchRequest sibeSearchRequest){
        SibeRoutingData sibeRoutingData= routing.getSibeRoutingData();
        sibeRoutingData.setAdultPriceGDS( routing.getAdultPriceGDS() );
        sibeRoutingData.setAdultTaxGDS( routing.getAdultTaxGDS() );
        sibeRoutingData.setChildPriceGDS( routing.getChildPriceGDS() );
        sibeRoutingData.setChildTaxGDS( routing.getChildTaxGDS() );
        sibeRoutingData.setInfantsPriceGDS( routing.getInfantPriceGDS() );
        sibeRoutingData.setInfantsTaxGDS( routing.getInfantTaxGDS() );

        sibeRoutingData.setFromCity( sibeSearchRequest.getFromCity() );
        sibeRoutingData.setToCity( sibeSearchRequest.getToCity() );
        sibeRoutingData.setFromDate( sibeSearchRequest.getFromDate() );
        sibeRoutingData.setRetDate( sibeSearchRequest.getRetDate() );
        sibeRoutingData.setFareBasis( routing.getFareBasis() );
        sibeRoutingData.setFareType( routing.getFareType() );
        sibeRoutingData.setUid( routing.getUid() );
        sibeRoutingData.setSibeRule(routing.getRule());
        sibeRoutingData.setFromSegments( routing.getFromSegments());
        sibeRoutingData.setRetSegments( routing.getRetSegments() );
        sibeRoutingData.setSibePolicy(routing.getSibeRoutingData().getSibePolicy());
        sibeRoutingData.setSibeRoute(sibeSearchRequest.getSearchRouteMap().get(routing.getReservationType()+"-"+routing.getOfficeId()));

        routing.getSibeRoutingData().setEncryptData(sibeSearchRequest.getSite()+"-"+sibeSearchRequest.getUuid()+"|"+routing.getId());
        try {
            routing.getSibeRoutingData().setDecryptData(objectMapper.writeValueAsString(sibeRoutingData));
        } catch (JsonProcessingException e) {
            LOGGER.error("对象转data信息异常:{}",e.getMessage());
            e.printStackTrace();
        }
    }




    /**
     * 行李额转换
     *
     * @param sibeRouting the sibe routing
     */
    private static void processBaggageToFormat(SibeRouting sibeRouting){

        sibeRouting.getRule().getBaggageInfoList().forEach(sibeBaggage->{
            if(sibeBaggage.getAdultBaggage()!=null){
                //成人行李额
                Integer adultPc=0, adultWeight=0;
                try{
                    String[] adultBaggageArray= StringUtils.split(sibeBaggage.getAdultBaggage(),",");
                    adultPc = Integer.valueOf(StringUtils.split(adultBaggageArray[0],"P")[0]);
                    adultWeight = Integer.valueOf(StringUtils.split(adultBaggageArray[1],"K")[0]);
                }catch (NumberFormatException e){
                    LOGGER.warn("行李额格式："+sibeBaggage.getAdultBaggage());
                }

                SibeFormatBaggage adultSibeFormatBaggage = new SibeFormatBaggage();
                adultSibeFormatBaggage.setSegmentNo(sibeBaggage.getSegmentNo());
                adultSibeFormatBaggage.setPassengerType(0);

                //行李额件数，单位PC，枚举值如下：
                // 0无免费托运行李，此时baggageWeight需赋值为-1；
                // -1表示计重制，对应的baggageWeight表示每人可携带的总重量(此时baggageWeight必须赋正值，否则过滤）；
                // >0表示计件制，对应的baggageWeight表示每件行李重量（若计件制时不知每件行李额的重量，baggageWeight必须赋值为-1）。
                adultSibeFormatBaggage.setBaggagePiece(adultPc>0?adultPc:(adultWeight>0?-1:0));
                adultSibeFormatBaggage.setBaggageWeight(adultWeight<=0?-1:adultWeight);

                //成人手提行李额
                if(sibeBaggage.getAdultHandBaggage()!=null){
                    //成人手提行李额
                    Integer adultHandPc=0, adultHandWeight=0;
                    try{
                        String[] adultHandBaggageArray= StringUtils.split(sibeBaggage.getAdultHandBaggage(),",");
                        adultHandPc = Integer.valueOf(StringUtils.split(adultHandBaggageArray[0],"P")[0]);
                        adultHandWeight = Integer.valueOf(StringUtils.split(adultHandBaggageArray[1],"K")[0]);
                    }catch (NumberFormatException e){
                        LOGGER.warn("手提行李额格式："+sibeBaggage.getAdultHandBaggage());
                    }

                    //行李额件数，单位PC，枚举值如下：
                    // 0无免费托运行李，此时baggageWeight需赋值为-1；
                    // -1表示计重制，对应的baggageWeight表示每人可携带的总重量(此时baggageWeight必须赋正值，否则过滤）；
                    // >0表示计件制，对应的baggageWeight表示每件行李重量（若计件制时不知每件行李额的重量，baggageWeight必须赋值为-1）。
                    adultSibeFormatBaggage.setHandBaggagePiece(adultHandPc>0?adultHandPc:(adultHandWeight>0?-1:0));
                    adultSibeFormatBaggage.setHandBaggageWeight(adultHandWeight<=0?-1:adultHandWeight);
                }

                sibeRouting.getRule().getFormatBaggageInfoList().add(adultSibeFormatBaggage);
                sibeRouting.getSibeRoutingData().getSibeRule().getFormatBaggageInfoList().add(adultSibeFormatBaggage);

            }
            //儿童行李额
            String baggageInfo;
            if(sibeBaggage.getChildBaggage()!=null){
                baggageInfo = sibeBaggage.getChildBaggage();
            }else {
                baggageInfo = sibeBaggage.getAdultBaggage();
            }
            //儿童手提行李额
            String baggageHandInfo;
            if(sibeBaggage.getChildHandBaggage()!=null){
                baggageHandInfo = sibeBaggage.getChildHandBaggage();
            }else {
                baggageHandInfo = sibeBaggage.getAdultHandBaggage();
            }

            if(baggageInfo != null){
                //成人行李额
                Integer childPc=0, childweight=0;
                try{
                    String[] childBaggageArray= StringUtils.split(baggageInfo,",");
                    childPc = Integer.valueOf(StringUtils.split(childBaggageArray[0],"P")[0]);
                    childweight = Integer.valueOf(StringUtils.split(childBaggageArray[1],"K")[0]);
                }catch (NumberFormatException e){
                    LOGGER.warn("行李额格式："+sibeBaggage.getAdultBaggage());
                }

                SibeFormatBaggage childSibeFormatBaggage = new SibeFormatBaggage();
                childSibeFormatBaggage.setSegmentNo(sibeBaggage.getSegmentNo());
                childSibeFormatBaggage.setPassengerType(1);
                childSibeFormatBaggage.setBaggagePiece(childPc>0?childPc:(childweight>0?-1:0));
                childSibeFormatBaggage.setBaggageWeight(childweight<=0?-1:childweight);

                if(baggageHandInfo != null){
                    //成人行李额
                    Integer childHandPc=0, childHandWeight=0;
                    try{
                        String[] childHandBaggageArray= StringUtils.split(baggageHandInfo,",");
                        childHandPc = Integer.valueOf(StringUtils.split(childHandBaggageArray[0],"P")[0]);
                        childHandWeight = Integer.valueOf(StringUtils.split(childHandBaggageArray[1],"K")[0]);
                    }catch (NumberFormatException e){
                        LOGGER.warn("手提行李额格式："+baggageHandInfo);
                    }
                    childSibeFormatBaggage.setHandBaggagePiece(childHandPc>0?childHandPc:(childHandWeight>0?-1:0));
                    childSibeFormatBaggage.setHandBaggageWeight(childHandWeight<=0?-1:childHandWeight);
                }

                sibeRouting.getRule().getFormatBaggageInfoList().add(childSibeFormatBaggage);
                sibeRouting.getSibeRoutingData().getSibeRule().getFormatBaggageInfoList().add(childSibeFormatBaggage);
            }

        });
    }

    public Integer getInteger(Integer num,BigDecimal rate){
        if( null == num ){
            return null ;
        }else{
            return (BigDecimal.valueOf(num).multiply(rate)).setScale( 0, BigDecimal.ROUND_UP ).intValue() ;
        }
    }

    public  SibeRouting toSibeRouting(Routing routing ){
        String routimgStr = JSONObject.toJSONString(routing);
        SibeRouting sibeRouting= JSONObject.parseObject(routimgStr,SibeRouting.class);
        sibeRouting.setFareType(routing.getProductType());
        sibeRouting.setAdultPriceGDS(routing.getAdultPrice().intValue());
        sibeRouting.setAdultTaxGDS(routing.getAdultTax().intValue());
        sibeRouting.setChildPriceGDS(routing.getChildPrice().intValue());
        sibeRouting.setChildTaxGDS(routing.getChildTax().intValue());
        sibeRouting.setInfantPriceGDS(routing.getInfantsPrice().intValue());
        sibeRouting.setInfantTaxGDS(routing.getInfantsTax().intValue());
        sibeRouting.setCurrencyGDS(routing.getCurrency());
        return sibeRouting;
    }

}
