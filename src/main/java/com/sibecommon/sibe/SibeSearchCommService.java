package com.sibecommon.sibe;

import com.sibecommon.ota.site.SibeObtainGDSCache;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.ota.site.SibeSearchResponse;

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

    void constructOtherSiteSearchRequest(SibeSearchRequest sibeSearchRequest);

    /**
     * 转换城市码
     * @param sibeSearchRequest
     */
    void transformCity(SibeSearchRequest sibeSearchRequest);

}
