package com.sibecommon.feign;

import com.sibecommon.ota.gds.GDSSearchRequestDTO;
import com.sibecommon.ota.gds.GDSSearchResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "cq",url = "${sxysibe.gds.cq.search-url}",decode404 = false, configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface CqFeignSearchClient {

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/cq/search",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<GDSSearchResponseDTO> search(final GDSSearchRequestDTO request);

}
