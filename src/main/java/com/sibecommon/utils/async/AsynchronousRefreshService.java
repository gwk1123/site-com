package com.sibecommon.utils.async;

import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.ota.site.SibeSearchResponse;

import java.util.List;

public interface AsynchronousRefreshService {

    void updateSearchOtaCache(List<SibeSearchResponse> sibeSearchResponseList, SibeSearchRequest sibeSearchRequest, int methodType, int upCurrentSite);

}
