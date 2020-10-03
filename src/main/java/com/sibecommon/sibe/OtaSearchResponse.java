package com.sibecommon.sibe;

import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.ota.site.SibeSearchResponse;

public interface OtaSearchResponse {

    public Object transformSearchResponse(SibeSearchResponse sibeResponse , SibeSearchRequest sibeRequest);

}
