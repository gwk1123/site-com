package comm.ota.ctrip.transform;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import comm.config.SibeProperties;
import comm.ota.ctrip.model.CtripSearchRequest;
import comm.ota.site.SibeSearchRequest;
import comm.utils.constant.SibeConstants;
import comm.utils.exception.CustomSibeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class TransformCtripSearchRequest {

    @Autowired
    private SibeProperties sibeProperties;

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TransformCtripSearchRequest.class);


    public SibeSearchRequest toSearchRequest(CtripSearchRequest ota){



         SibeSearchRequest sibeSearchRequest = new SibeSearchRequest();
         //请求开始时间,判断请求超时使用
         sibeSearchRequest.setStartTime(SystemClock.now());
         //CID检测
         String errorMsg= "search请求参数错误, cid不能为空或者cid错误";
         if (StringUtils.isEmpty(ota.getCid())||(!sibeProperties.getOta().getCid().equals(ota.getCid()))){
             LOGGER.error("uuid: 000000000 " + errorMsg);
             throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_1, errorMsg,"000000000","search");
         }

         sibeSearchRequest.setCid(ota.getCid());
         //3.得到UUID
         String uuid= ota.getUuid();
         if (StringUtils.isEmpty(uuid)){
             //如果请求参数中没有UUID,则产生UUID
             //uuid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmmss")) +"_"+ UUID.randomUUID().toString().replaceAll("-", "");
             uuid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmmss")) + StringUtils.split(UUID.randomUUID().toString(),"-")[4];
         }
         sibeSearchRequest.setUuid(uuid);

         //行程类型
         if(StringUtils.isEmpty(ota.getTripType())){
             errorMsg= "search请求参数错误，行程类型不能为空";
             LOGGER.error("uuid:" + uuid + " " + errorMsg);
             throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_1, errorMsg,uuid,"search");
         }
         String gdsTripType = ota.getTripType();
         sibeSearchRequest.setTripType(gdsTripType);

         //出发地
         if(StringUtils.isEmpty(ota.getTripType())){
             errorMsg= "search请求参数错误，出发地不能为空";
             LOGGER.error("uuid:" + uuid + " " + errorMsg);
             throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_1, errorMsg,uuid,"search");
         }
         sibeSearchRequest.setFromCity(ota.getFromCity());

         //目的地
         if(StringUtils.isEmpty(ota.getTripType())){
             errorMsg= "search请求参数错误，目的地不能为空";
             LOGGER.error("uuid:" + uuid + " " + errorMsg);
             throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_1, errorMsg,uuid,"search");
         }
         sibeSearchRequest.setToCity(ota.getToCity());

         //去程日期
         if(StringUtils.isEmpty(ota.getTripType())){
             errorMsg= "search请求参数错误，去程日期不能为空";
             LOGGER.error("uuid:" + uuid + " " + errorMsg);
             throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_1, errorMsg,uuid,"search");
         }
         sibeSearchRequest.setFromDate(ota.getFromDate());

         //回程日期
         //行程类型,1:单程; 2:往返; 5: 多程
         if("2".equals(ota.getTripType()) && StringUtils.isEmpty(ota.getRetDate())){
             errorMsg= "search请求参数错误，回程日期不能为空";
             LOGGER.error("uuid:" + uuid + " " + errorMsg);
             throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_1, errorMsg,uuid,"search");
         }
         sibeSearchRequest.setRetDate(ota.getRetDate());

         //F：FlightIntlOnline；M：Mobile 1）若为F，要求在7S内返回结果；若为M，要求在5S内返回结果
         if("K".equals(ota.getChannel())){
             //todo 需要进一步了解积分票的情况
             LOGGER.error("积分票查询");
         }
         sibeSearchRequest.setChannel(ota.getChannel());

         //默认不压缩；如果为T，压缩编码；
         sibeSearchRequest.setIsCompressEncode(ota.getIsCompressEncode());

         //人数设置
         sibeSearchRequest.setAdultNumber(ota.getAdultNumber() == 0 ? 1: ota.getAdultNumber());
         sibeSearchRequest.setChildNumber(ota.getChildNumber());
         sibeSearchRequest.setInfantNumber(ota.getInfantNumber());

         return sibeSearchRequest;

     }

}
