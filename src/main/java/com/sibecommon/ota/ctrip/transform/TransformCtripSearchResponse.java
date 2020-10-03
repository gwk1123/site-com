package com.sibecommon.ota.ctrip.transform;

import com.sibecommon.config.SibeProperties;
import com.sibecommon.ota.ctrip.model.CtripRouting;
import com.sibecommon.ota.ctrip.model.CtripSearchResponse;
import com.sibecommon.ota.site.SibeRouting;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.ota.site.SibeSearchResponse;
import com.sibecommon.sibe.OtaSearchResponse;
import com.sibecommon.ota.site.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransformCtripSearchResponse implements OtaSearchResponse {

    @Autowired
    private SibeProperties sibeProperties ;

    @Override
    public Object  transformSearchResponse(SibeSearchResponse sibeResponse , SibeSearchRequest sibeRequest){
        CtripSearchResponse ctripSearchResponse = new CtripSearchResponse();
        List<CtripRouting> ctripRoutingList = new ArrayList<>();

        for(SibeRouting sibeRouting:sibeResponse.getRoutings()){
            ctripRoutingList.add(TransformCtripCommon.convertRoutingToOTA(sibeRouting, sibeRequest,sibeProperties));
        }

        // 处理sibe原始data
//        Map<String, SibeRouting> sibeDataMap = new HashMap<>();
//        for (SibeRouting sibeRouting : sibeResponse.getRoutings()) {
//            sibeDataMap.put(sibeRouting.getSibeRoutingData().getEncryptData(), sibeRouting);
//        }
//        List<SibeRouting> sibeRoutingList = TransformCtripCommon.processSibeRoutingData(ctripRoutingList, sibeDataMap);
//        sibeResponse.setRoutings(sibeRoutingList);

        ctripSearchResponse.setStatus(0);
        ctripSearchResponse.setMsg("成功");
        //用于判断是否刷新Redis缓存
        ctripSearchResponse.setTimeLapse(sibeResponse.getTimeLapse());
        ctripSearchResponse.setRoutings(ctripRoutingList);

        return ctripSearchResponse;
    }

}
