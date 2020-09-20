package comm.sibe;

import comm.ota.site.SibeBaseResponse;
import comm.ota.site.SibeSearchRequest;
import comm.ota.site.SibeSearchResponse;

public interface OtaSearchResponse {

    public Object transformSearchResponse(SibeSearchResponse sibeResponse , SibeSearchRequest sibeRequest);

}
