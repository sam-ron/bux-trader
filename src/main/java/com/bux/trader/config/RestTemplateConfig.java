package com.bux.trader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Value("${bux.auth.bearer.token}")
    private String authToken;

    @Value("${bux.auth.accept.lang}")
    private String lang;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", authToken);
            headers.add("Accept-language", lang);
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");
            return execution.execute(request, body);
        }));
        return restTemplate;
    }

}
