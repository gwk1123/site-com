package comm.service.transform;


import comm.ota.gds.Baggage;
import comm.ota.gds.Routing;
import comm.ota.site.SibeFormatBaggage;
import comm.ota.site.SibeRouting;
import comm.ota.site.SibeSegment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by yangdehua on 18/2/11.
 */
public class TransformCommonGds {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TransformCommonGds.class);


    /**
     * Process routing to sibe.
     *
     * @param routing     the routing
     * @param sibeRouting the sibe routing
     */
//    public static void processRoutingToGDS(Routing routing, SibeRouting sibeRouting){
//
//        //运价类型
//        routing.setProductType(sibeRouting.getSibeRoutingData().getFareType());
//
//        //FareBase
//        routing.setFareBasis(sibeRouting.getSibeRoutingData().getFareBasis());
//
//        //成人单价
//        routing.setAdultPrice(new BigDecimal(sibeRouting.getSibeRoutingData().getAdultPriceGDS()));
//
//        //成人税费
//        routing.setAdultTax(new BigDecimal(sibeRouting.getSibeRoutingData().getAdultTaxGDS()));
//
//        //儿童单价
//        if(sibeRouting.getSibeRoutingData().getChildPriceGDS()!=null) {
//            routing.setChildPrice(new BigDecimal(sibeRouting.getSibeRoutingData().getChildPriceGDS()));
//        }
//
//        //儿童税费
//        if(sibeRouting.getSibeRoutingData().getChildTaxGDS()!=null) {
//            routing.setChildTax(new BigDecimal(sibeRouting.getSibeRoutingData().getChildTaxGDS()));
//        }
//        //儿童单价
//        if(sibeRouting.getSibeRoutingData().getInfantsPriceGDS()!=null) {
//            routing.setInfantsPrice(new BigDecimal(sibeRouting.getSibeRoutingData().getInfantsPriceGDS()));
//        }
//        //儿童税费
//        if(sibeRouting.getSibeRoutingData().getInfantsTaxGDS()!=null) {
//            routing.setInfantsTax(new BigDecimal(sibeRouting.getSibeRoutingData().getInfantsTaxGDS()));
//        }
//
//        //GDS
//        routing.setReservationType(sibeRouting.getSibeRoutingData().getSibeRoute().getOrderPcc().getGdsCode());
//
//        //PCC配置号
//        routing.setOfficeId(sibeRouting.getSibeRoutingData().getSibeRoute().getOrderPcc().getPccCode());
//
//
//        //todo 有重复的情况，后续再确认
//        routing.getFromSegments().forEach(segment -> {
//            Optional<SibeSegment> sibeSegment = sibeRouting
//                .getSibeRoutingData()
//                .getFromSegments()
//                .stream()
//                .filter(x -> (segment.getDepAirport().equals(x.getDepAirport())&&
//                    segment.getArrAirport().equals(x.getArrAirport())))
//                .findFirst();
//
//            if(sibeSegment.isPresent()){
//
//                segment.setSegmentKey(sibeSegment.get().getSegmentKey());
//                segment.setDepZone(sibeSegment.get().getDepZone());
//                segment.setArrZone(sibeSegment.get().getArrZone());
//                segment.setAvailabilitySource(sibeSegment.get().getAvailabilitySource());
//                segment.setFlightIndicator(String.valueOf(sibeSegment.get().getFlightIndicator()));
//                segment.setItemNumber(sibeSegment.get().getItemNumber());
//            }
//        });
//
//        //设置币种
//        routing.setCurrency(sibeRouting.getSibeRoutingData().getSibePolicy().getCurrencyGDS());
//
//        //todo 有重复的情况，后续再确认
//        if(routing.getRetSegments()!=null && routing.getRetSegments().size()> 0){
//            routing.getRetSegments().forEach(segment -> {
//                Optional<SibeSegment> retSegment = sibeRouting
//                    .getSibeRoutingData()
//                    .getRetSegments()
//                    .stream()
//                    .filter(x -> (segment.getDepAirport().equals(x.getDepAirport())&&
//                        segment.getDepTime().equals(x.getDepTime()) &&
//                        segment.getArrAirport().equals(x.getArrAirport())))
//                    .findFirst();
//
//
//                if(retSegment.isPresent()){
//                    // LOGGER.debug("往返中的回程");
//                    segment.setSegmentKey(retSegment.get().getSegmentKey());
//                    segment.setDepZone(retSegment.get().getDepZone());
//                    segment.setArrZone(retSegment.get().getArrZone());
//                    segment.setAvailabilitySource(retSegment.get().getAvailabilitySource());
//                    segment.setFlightIndicator(String.valueOf(retSegment.get().getFlightIndicator()));
//                    segment.setItemNumber(retSegment.get().getItemNumber());
//                }
//            });
//        }
//        return;
//    }

    /**
     * Convert response status to sibe integer.
     *
     * @param status the status
     * @return the integer
     */
    public  static Integer convertResponseStatusToSibe(Integer status) {
         /*
         * 0,成功；
         * 1,其他失败原因； -> 999
         * 2,请求参数错误； ->1
         * 3,程序异常； ->2
         * 4,舱位已售完；-> 3
         * 5,舱位数不足； -> 3
         * 6,GDS返回异常; -> 11
         * 9, order某航段预定失败并且cancel该PNR成功 -> 9
         * 10, order某航段预定失败并且cancel该PNR失败 -> 10
 *
         * RESPONSE_MSG_SUCCESS = "成功";
         * RESPONSE_MSG_1 = "请求参数错误";
         * RESPONSE_MSG_2 = "程序异常";
         * RESPONSE_MSG_3 = "舱位数不足，舱位已售完";
         * RESPONSE_MSG_4 = "查无价格";
         * RESPONSE_MSG_5 = "无法获取税费";
         * RESPONSE_MSG_6 = "退改签异常";
         * RESPONSE_MSG_7 = "网络异常";
         * RESPONSE_MSG_8 = "价格变化";
         * RESPONSE_MSG_9 = "order某航段预定失败并且cancel该PNR成功";
         * RESPONSE_MSG_10 = "order某航段预定失败并且cancel该PNR失败";
         * RESPONSE_MSG_11 = "GDS返回异常";
         * RESPONSE_MSG_100 = "GDS查询无航班数据";
         * RESPONSE_MSG_101 = "客户重复预订";
          */

        switch (status)
        {
            case 0:return 0;
            case 1:return 999;
            case 2:return 1;
            case 3:return 2;
            case 4:return 3;
            case 5:return 3;
            case 6:return 11;
            case 9:return 9;
            case 10:return 10;
            default: return 999;
        }
    }


    /**
     * Convert trip type to gds string.
     *
     * @param tripType the trip type
     * @return the string
     */
    public final static  String convertTripTypeToGds(final String tripType){
        if("OW".equals(tripType)){
            return "1";
        }else if("RT".equals(tripType)){
            return "2";
        }else if("MT".equals(tripType)){
            return "3";
        }else {
            return null;
        }
    }

    /**
     * Convert trip type to sibe string.
     *
     * @param tripType the trip type
     * @return the string
     */
    public final static String convertTripTypeToSibe(final int tripType){
        if(tripType ==1) {
            return "OW";
        } else if(tripType ==2) {
            return "RT";
        } else if(tripType ==3) {
            return "MT";
        } else {
            return null;
        }
    }


    /**
     * Convert baggage sibe format baggage.
     *
     * @param baggage the baggage
     * @return the sibe format baggage
     */
    public static SibeFormatBaggage convertBaggageToSibe(Baggage baggage) {
        SibeFormatBaggage  sibeFormatBaggage= new SibeFormatBaggage();
        String gdsAdultBaggage = baggage.getAdultBaggage();

        String[] gdsAdultBaggageArray = StringUtils.split(gdsAdultBaggage,",");
        String pieceInfo = gdsAdultBaggageArray[0];
        String weightInfo = gdsAdultBaggageArray[1];
        String pieceString = StringUtils.split(pieceInfo,"P")[0];
        String weightString = StringUtils.split(weightInfo,"K")[0];
        int piece = Integer.parseInt(pieceString);
        int weight = Integer.parseInt(weightString);

        if (weight > 0){
            //计重
            sibeFormatBaggage.setBaggagePiece(-1);
            sibeFormatBaggage.setBaggageWeight(weight);
        }else if(piece >0){
            //计价
            sibeFormatBaggage.setBaggagePiece(piece);
        }else{
            //无免费行李额
            sibeFormatBaggage.setBaggagePiece(0);
        }

        sibeFormatBaggage.setCnBaggage("");
        sibeFormatBaggage.setEnBaggage("");

        //成人
        sibeFormatBaggage.setPassengerType(0);
        sibeFormatBaggage.setSegmentNo(baggage.getSegmentNo());

        // 暂时无
        // sibeFormatBaggage.setWidth("");
        // sibeFormatBaggage.setHeight("")
        // sibeFormatBaggage.setLength("");
        return sibeFormatBaggage;

    }

    public static void processLccCutFlightNumber(SibeRouting routing){
        List<SibeSegment> segmentList = new ArrayList<>();
        segmentList.addAll(routing.getFromSegments());

        if(routing.getRetSegments() != null && routing.getRetSegments().size() > 0){
            segmentList.addAll(routing.getRetSegments());
        }

        for(SibeSegment sibeSegment : segmentList){
            if(sibeSegment.getFlightNumber().substring(0,2).equals(sibeSegment.getCarrier())){
                sibeSegment.setFlightNumber(sibeSegment.getFlightNumber().substring(2));
            }

            if(StringUtils.isNotBlank(sibeSegment.getOperatingFlightNo()) && StringUtils.isNotBlank(sibeSegment.getOperatingCarrier())){
                if(sibeSegment.getOperatingFlightNo().substring(0,2).equals(sibeSegment.getOperatingCarrier())){
                    sibeSegment.setOperatingFlightNo(sibeSegment.getOperatingFlightNo().substring(2));
                }
            }
        }
    }

    public static void processLccRestoreFlightNumber(SibeRouting routing){
        List<SibeSegment> segmentList = new ArrayList<>();
        segmentList.addAll(routing.getFromSegments());

        if(routing.getRetSegments() != null && routing.getRetSegments().size() > 0){
            segmentList.addAll(routing.getRetSegments());
        }

        for(SibeSegment sibeSegment : segmentList){
            if(! sibeSegment.getFlightNumber().substring(0,2).equals(sibeSegment.getCarrier())){
                sibeSegment.setFlightNumber(sibeSegment.getCarrier() + sibeSegment.getFlightNumber());
            }

            if(StringUtils.isNotBlank(sibeSegment.getOperatingFlightNo()) && StringUtils.isNotBlank(sibeSegment.getOperatingCarrier())){
                if(! sibeSegment.getOperatingFlightNo().substring(0,2).equals(sibeSegment.getCarrier())){
                    sibeSegment.setOperatingFlightNo(sibeSegment.getOperatingCarrier() + sibeSegment.getOperatingFlightNo());
                }
            }
        }
    }


}
