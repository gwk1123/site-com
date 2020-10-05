package com.sibecommon.service.ota;

import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.service.handler.AbstractRuleHandler;
import com.sibecommon.utils.exception.CustomSibeException;
import org.springframework.stereotype.Component;

/**
 * Created by yangdehua on 18/2/10.
 */

@Component
public class OtaReuqestRuleGreator {

    private AbstractRuleHandler otaRuleHandler;

    public OtaReuqestRuleGreator() {

        //1. OTA-限制多程<包括缺口程>
        otaRuleHandler = new OtaRuleMultiWayHandle();

        //2. OTA-航线白名单
        OtaRuleWhiteListHandle otaRuleWhiteListHandle= new OtaRuleWhiteListHandle();
        otaRuleHandler.setNextHandler(otaRuleWhiteListHandle);

        //3. 限制旅行日期范围
        OtaRuleTravelDateHandle otaRuleTravelDateHandle= new OtaRuleTravelDateHandle();
        otaRuleWhiteListHandle.setNextHandler(otaRuleTravelDateHandle);

        //4.OAT-航线黑名单
        OtaRuleBlackListHandle otaRuleBlackListHandle= new OtaRuleBlackListHandle();
        otaRuleTravelDateHandle.setNextHandler(otaRuleBlackListHandle);

        //5.OTA-限制销售时间
        OtaRuleSellDataHandle otaRuleSellDataHandle= new OtaRuleSellDataHandle();
        otaRuleBlackListHandle.setNextHandler(otaRuleSellDataHandle);
    }

    public void create(SibeSearchRequest sibeSearchRequest) throws CustomSibeException {
        otaRuleHandler.handleRequest(sibeSearchRequest);
    }

}
