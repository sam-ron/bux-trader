package com.bux.trader.service;

import com.bux.trader.entity.repository.TradePosition;
import com.bux.trader.entity.rest.TradeQuote;
import com.bux.trader.entity.rest.*;
import com.bux.trader.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TradeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${bux.api.url}")
    private String apiUrl;

    @Value("${bux.trade.upper.limit}")
    private Float buyUpperLimit;

    @Value("${bux.trade.lower.limit}")
    private Float buyLowerLimit;

    private static final String BUY_ENDPOINT = "users/me/trades";
    private static final String SELL_ENDPOINT = "users/me/portfolio/positions/{positionId}";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TradeRepository tradeRepository;



    public void processQuote(TradeQuote tradeQuote){
        System.out.println("readQuote");
        // If there's no active trade on given product ID then create one
        if(tradeRepository.findByProductId(tradeQuote.getSecurityId()) == null){
            System.out.println("checkingn db");
            TradePosition tradePosition = new TradePosition(
                    tradeQuote.getSecurityId(), tradeQuote.getCurrentPrice(), buyUpperLimit, buyLowerLimit);
            buyOrder(tradePosition);

        }
        // If there is an active trade then determine next step
        else{
            System.out.println("Updated price " + tradeQuote.getCurrentPrice());
            TradePosition tradePosition = tradeRepository.findByProductId(tradeQuote.getSecurityId());
            System.out.println("trade active for " + tradePosition.getProductId() + " opened at " + tradePosition.getBuyPrice());
        }


    }


     private BuyResponse buyOrder(TradePosition tradePosition){
        System.out.println("buy");
        BuyRequest request = createBuyRequest(tradePosition);
        ResponseEntity<BuyResponse> response = restTemplate.postForEntity(getBuyUrl(), request, BuyResponse.class);

         System.out.println("CODE" + response.getStatusCode());

        if(response.getStatusCode().equals(HttpStatus.OK)){
            System.out.println("INSERTING" + tradePosition.getBuyPrice() +"," +tradePosition.getProductId() + "," + tradePosition.getLowerLimit() );
            tradeRepository.save(tradePosition);
            System.out.println("trade successful");
            logger.info("Buy Order Successful. Response {}, Product {}, Price {}  ", response.getStatusCode(),
                    tradePosition.getProductId(), tradePosition.getBuyPrice());
        }
        return response.getBody();
    }

    private BuyRequest createBuyRequest(TradePosition tradePosition){
        BuyRequest request = new BuyRequest();
        request.setProductId(tradePosition.getProductId());
        request.setInvestingAmount(new Amount("BUX", 2, 10));
        request.setLeverage(2);
        request.setDirection(Direction.BUY);
        request.setSource(new Source("OTHER"));

        return request;
    }

    private String getBuyUrl(){
        return apiUrl+BUY_ENDPOINT;
    }
}
