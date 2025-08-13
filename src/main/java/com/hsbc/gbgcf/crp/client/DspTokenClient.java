package com.hsbc.gbgcf.crp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Client for interacting with the DSP Token service
 */
@FeignClient(name = "dsp-token-service", url = "${dsp.token.service.url}")
public interface DspTokenClient {
    
    /**
     * Get a token for authentication
     * 
     * @return the authentication token
     */
    @GetMapping("/token")
    String getToken();
}