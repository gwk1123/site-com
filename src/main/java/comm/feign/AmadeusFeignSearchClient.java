package comm.feign;
import comm.ota.gds.GDSSearchRequestDTO;
import comm.ota.gds.GDSSearchResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yangdehua on 18/2/8.
 */

@FeignClient(name = "amadeusapp",url = "${sxysibe.gds.amadeus.search-url}" , decode404 = false, configuration = FeignClientProperties.FeignClientConfiguration.class )
public interface AmadeusFeignSearchClient {

    @RequestMapping(method = RequestMethod.POST,
        value = "/api/amadeus/air/search",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<GDSSearchResponseDTO> search(final GDSSearchRequestDTO request);



}
