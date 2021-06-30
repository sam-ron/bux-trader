package com.bux.trader.service.rest;

import com.bux.trader.entity.TradingQuote;
import com.bux.trader.service.rest.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class TradingService {

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

    public void readQuote(TradingQuote tradingQuote){
        System.out.println("readQuote");
        if(!tradeActive){
            buy(tradingQuote);
            logger.info("Successfully bought at: " + tradingQuote.getCurrentPrice());
        }

    }

    private BuyResponse buy(TradingQuote tradingQuote){
        System.out.println("buy");
        BuyRequest request = createBuyRequest(tradingQuote);
        ResponseEntity<BuyResponse> response = restTemplate().postForEntity(buyUrl, request, BuyResponse.class);
        System.out.println(response.getStatusCode());
        return response.getBody();
    }

    private BuyRequest createBuyRequest(TradingQuote tradingQuote){
        BuyRequest request = new BuyRequest();
        request.setProductId(tradingQuote.getSecurityId());
        request.setInvestingAmount(new Amount("BUX", 2, 10));
        request.setLeverage(2);
        request.setDirection(Direction.BUY);
        request.setSource(new Source("OTHER"));

        return request;
    }
}
