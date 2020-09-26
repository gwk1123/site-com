package comm.utils.async;

import comm.ota.site.SibeSearchRequest;
import comm.ota.site.SibeSearchResponse;

import java.util.List;

public interface AsynchronousRefreshService {

    void updateSearchOtaCache(List<SibeSearchResponse> sibeSearchResponseList, SibeSearchRequest sibeSearchRequest, int methodType, int upCurrentSite);

}
