package comm.ota.ctrip.transform;

import comm.config.SibeProperties;
import comm.ota.ctrip.model.CtripRouting;
import comm.ota.ctrip.model.CtripSearchResponse;
import comm.ota.site.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransformCtripSearchResponse {

    @Autowired
    private SibeProperties sibeProperties ;

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
