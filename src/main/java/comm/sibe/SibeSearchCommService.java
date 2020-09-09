package comm.sibe;

import comm.ota.site.SibeObtainGDSCache;
import comm.ota.site.SibeSearchRequest;
import comm.ota.site.SibeSearchResponse;

import java.util.List;

public interface SibeSearchCommService {


    public void constructSibeSearchRequestSecond(SibeSearchRequest sibeSearchRequest);

    public SibeObtainGDSCache getGDSCache(SibeSearchRequest sibeSearchRequest);

    /**
     * 删除GDS多余缓存的配置信息
     * @param sibeSearchRequest
     * @param key
     */
    public void excessGDSpcc(SibeSearchRequest sibeSearchRequest,String key) ;


    public void constructSibeSearchRequestByRoute(SibeSearchRequest sibeSearchRequest);

    public List<SibeSearchResponse> search(SibeSearchRequest sibeSearchRequest);


}
