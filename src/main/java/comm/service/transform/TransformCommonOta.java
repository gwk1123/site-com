package comm.service.transform;


import comm.config.SibeProperties;
import comm.ota.site.SibeBaseRequest;

public class TransformCommonOta {


    /**
     * 转化站点数据
     * @param sibeBaseRequest
     * @param sibeProperties
     * @param <T>
     */
    public static <T extends SibeBaseRequest> void constructSibeBaseRequest(T sibeBaseRequest, SibeProperties sibeProperties){
        sibeBaseRequest.setOta(sibeProperties.getOta().getOta());
        sibeBaseRequest.setSite(sibeProperties.getOta().getSite());
        sibeBaseRequest.setSkey(sibeProperties.getOta().getSkey());
        sibeBaseRequest.setDatakey(sibeProperties.getOta().getDataKey());
        sibeBaseRequest.setOtherIssueMsg(sibeProperties.getOta().getOtherIssueTicketMsg());
    }



}
