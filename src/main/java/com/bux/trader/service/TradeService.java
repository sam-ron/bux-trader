package com.bux.trader.service;

import com.bux.trader.entity.rest.TradeQuote;
import com.bux.trader.entity.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class TradeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${bux.auth.bearer.token}")
    private String authToken;

    @Value("${bux.auth.accept.lang}")
    private String lang;

    @Value("${bux.buy.url}")
    private String buyUrl;

    @Value("${bux.sell.url}")
    private String sellUrl;

    private boolean tradeActive = false;

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

    public void processQuote(TradeQuote tradeQuote){
        String productId = tradeQuote.getSecurityId();
        System.out.println("readQuote");
        if(!tradeActive){
            buy(tradeQuote);
            logger.info("Successfully bought at: " + tradeQuote.getCurrentPrice());
        }

    }

    private BuyResponse buy(TradeQuote tradeQuote){
        System.out.println("buy");
        BuyRequest request = createBuyRequest(tradeQuote);
        ResponseEntity<BuyResponse> response = restTemplate().postForEntity(buyUrl, request, BuyResponse.class);
        System.out.println(response.getStatusCode());
        return response.getBody();
    }

    private BuyRequest createBuyRequest(TradeQuote tradeQuote){
        BuyRequest request = new BuyRequest();
        request.setProductId(tradeQuote.getSecurityId());
        request.setInvestingAmount(new Amount("BUX", 2, 10));
        request.setLeverage(2);
        request.setDirection(Direction.BUY);
        request.setSource(new Source("OTHER"));

        return request;
    }
}
