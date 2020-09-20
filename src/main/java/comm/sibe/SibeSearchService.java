package comm.sibe;

import comm.ota.ctrip.model.CtripSearchResponse;
import comm.ota.site.SibeBaseResponse;
import comm.ota.site.SibeSearchRequest;

import java.util.function.BiFunction;

public interface SibeSearchService {

    public Object search(SibeSearchRequest sibeSearchRequest) throws Exception;
}
