package com.bux.trader.rest.controller;

import com.bux.trader.repository.entity.TradePosition;
import com.bux.trader.rest.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class TradeController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${bux.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    private static final String BUY_ENDPOINT = "users/me/trades";
    private static final String SELL_ENDPOINT = "users/me/portfolio/positions/";


    public BuyResponse buyOrder(TradePosition tradePosition)  {

        BuyRequest request = createBuyRequest(tradePosition);
        ResponseEntity<BuyResponse> response = restTemplate.postForEntity(getBuyUrl(), request, BuyResponse.class);

        return response.getBody();
    }

    public SellResponse sellOrder(TradePosition tradePosition){
        ResponseEntity<SellResponse> response = restTemplate.exchange(
                getSellUrl(tradePosition.getPositionId()), HttpMethod.DELETE, null, SellResponse.class);

        return response.getBody();
    }


    private BuyRequest createBuyRequest(TradePosition tradePosition){
        return BuyRequest.builder()
                .productId(tradePosition.getProductId())
                .investingAmount(new Amount("BUX", 2, tradePosition.getBuyAmount()))
                .leverage(2)
                .direction(Direction.BUY)
                .source(new Source("OTHER"))
                .build();
    }

    private String getBuyUrl(){
        return apiUrl + BUY_ENDPOINT;
    }

    private String getSellUrl(String positionId){
        return apiUrl + SELL_ENDPOINT + positionId;
    }

}
