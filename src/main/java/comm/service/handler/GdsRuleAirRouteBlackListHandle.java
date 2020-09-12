package comm.service.handler;

import comm.ota.site.SibeSearchRequest;
import comm.service.transform.SibeUtil;
import comm.utils.constant.SibeConstants;
import comm.utils.exception.CustomSibeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created by yangdehua on 18/2/10.
 */

@Component
public class GdsRuleAirRouteBlackListHandle extends AbstractRuleHandler {

    private final Logger LOGGER=LoggerFactory.getLogger(this.getClass());

    @Override
    public void doHandleRequest(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {
        String uuid=sibeSearchRequest.getUuid();
        String gds=sibeSearchRequest.getGds();
        String officeId=sibeSearchRequest.getOfficeId();
        String parameterKey= "GDS-7"; //﻿﻿航线黑名单开关开关
        String msg="【GDS-航线黑名单】";
        String RULE_TYPE="7"; //GDS-航线黑名单规则类型

        //判断开关是否有开启，如果没有开启则忽略此规则
        if( sibeSearchRequest
            .getSiteRulesSwitch()
            .stream()
            .filter(Objects::nonNull)
            .filter(value->(parameterKey.equals(value.getParameterKey())))
            .anyMatch(value->(!"TRUE".equals(value.getParameterValue()))))
        {
            LOGGER.info("uuid:"+uuid+" "+sibeSearchRequest.getGds()+msg+"开关没有开启");
            return;
        }

        //航线黑名单
        List<String> cityList= SibeUtil
            .getCityPriority(sibeSearchRequest.getFromCityRedis(),sibeSearchRequest.getToCityRedis());

        for(String city:cityList){
            String[] cityArray= StringUtils.split(city,"/");
            //注意：GDS规则配置，PCC项目可以为空，为空则表示全部
            if(sibeSearchRequest
                .getGdsRuleSet()
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(gdsRule -> (
                    RULE_TYPE.equals(gdsRule.getRuleType())
                    && gds.equals(gdsRule.getGdsCode())
                    && SibeUtil.contains(gdsRule.getPccCode(),officeId,"/")
                    && SibeUtil.contains(gdsRule.getOrigin(),cityArray[0],"/")
                    && SibeUtil.contains(gdsRule.getDestination(),cityArray[1],"/")
                    ))){
                //在航线白名单中，抛出异常
                LOGGER.info("uuid:"+uuid+" "+sibeSearchRequest.getGds()+msg+sibeSearchRequest.getFromCity()+"-"+sibeSearchRequest.getToCity()+"不请求GDS "+cityArray[0]+"-"+cityArray[0]);
                throw new CustomSibeException(SibeConstants.RESPONSE_STATUS_103, msg, uuid,"search");
            }
        }
        return;
    }
}

