package com.sibecommon.service.handler;

import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.utils.exception.CustomSibeException;
import org.springframework.stereotype.Component;

/**
 * Created by yangdehua on 18/2/10.
 */
@Component
public class GdsRequestRuleCreator {

    private AbstractRuleHandler gdsRuleHandler;


    public GdsRequestRuleCreator() {

        //1. GDS-航线黑名单规则类型
        gdsRuleHandler = new GdsRuleAirRouteBlackListHandle();

        //2.GDS限制方案数量
        GdsRuleNumberOfUnitsHandle gdsRuleNumberOfUnitsHandle  = new GdsRuleNumberOfUnitsHandle();
        gdsRuleHandler.setNextHandler(gdsRuleNumberOfUnitsHandle);

        //3.GDS航司黑名单
        GdsRuleCarrierBlackListHandle GdsRuleCarrierBlackListHandle = new GdsRuleCarrierBlackListHandle();
        gdsRuleNumberOfUnitsHandle.setNextHandler(GdsRuleCarrierBlackListHandle);

        //4.航司指定GDS来源设置
        GdsRuleCarrierSourceSetHandle gdsRuleCarrierSourceSetHandle =new GdsRuleCarrierSourceSetHandle();
        GdsRuleCarrierBlackListHandle.setNextHandler(gdsRuleCarrierSourceSetHandle);

        //5.航司航线白名单
        GdsRuleAirlineWhiteListHandle gdsRuleAirlineWhiteListHandle = new GdsRuleAirlineWhiteListHandle();
        gdsRuleCarrierSourceSetHandle.setNextHandler(gdsRuleAirlineWhiteListHandle);

        //todo lcc GDS规则
    }

    public void create(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {
        gdsRuleHandler.handleRequest(sibeSearchRequest);
    }

}
