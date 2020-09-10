package comm.feign;

import comm.ota.gds.GDSSearchRequestDTO;
import comm.ota.gds.GDSSearchResponseDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.FeignClientProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by gwk on 18/2/8.
 */

@FeignClient(name = "galileoapp" , url = "${sxysibe.gds.galileo.search-url}" ,decode404 = false, configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface GalileoFeignSearchClient {

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/galileo/air/search",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<GDSSearchResponseDTO> search(final GDSSearchRequestDTO request);


}
