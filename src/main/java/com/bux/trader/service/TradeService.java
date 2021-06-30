package com.bux.trader.service;

import com.bux.trader.entity.repository.TradePosition;
import com.bux.trader.entity.rest.TradeQuote;
import com.bux.trader.entity.rest.*;
import com.bux.trader.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TradeRepository tradeRepository;

    private static final String BUY_ENDPOINT = "users/me/trades";
    private static final String SELL_ENDPOINT = "users/me/portfolio/positions/";


    public boolean processQuote(TradeQuote tradeQuote){
        System.out.println("here1");
        // If there's no active trade on given product ID then create one
        if(tradeRepository.findByProductId(tradeQuote.getSecurityId()) == null){
            System.out.println("checkingn db");
            TradePosition tradePosition = new TradePosition(
                    tradeQuote.getSecurityId(), tradeQuote.getCurrentPrice(), buyUpperLimit, buyLowerLimit);
            System.out.println("NEW POSITION ID" + tradePosition.getPositionId());
            buyOrder(tradePosition);
        }
        // If there is an active trade then determine next step
        else{
            System.out.println("Updated price " + tradeQuote.getCurrentPrice());
            TradePosition tradePosition = tradeRepository.findByProductId(tradeQuote.getSecurityId());
            System.out.println("trade active for " + tradePosition.getProductId() + " opened at " + tradePosition.getBuyPrice());
            checkStatus(tradePosition, tradeQuote);
            return false;
        }
        return true;
    }

    private boolean checkStatus(TradePosition tradePosition, TradeQuote tradeQuote){
        if(!tradePosition.getProductId().equals(tradeQuote.getSecurityId())){
            System.out.println("ERROR. NOT EQUAL " + tradePosition.getProductId() + "/" + tradeQuote.getSecurityId());
            return false;
        }
        else{
            if (tradeQuote.getCurrentPrice() >= buyLowerLimit && tradeQuote.getCurrentPrice() <= buyUpperLimit){
                System.out.println("Not ready to sell");
                return true;
            }
            else{
                System.out.println("Sell");
                sellOrder(tradePosition);
                return false;
            }
        }

    }

     private BuyResponse buyOrder(TradePosition tradePosition){
        System.out.println("buying");
        BuyRequest request = createBuyRequest(tradePosition);
        ResponseEntity<BuyResponse> response = restTemplate.postForEntity(getBuyUrl(), request, BuyResponse.class);

        if(response.getStatusCode().equals(HttpStatus.OK)){
            System.out.println("INSERTING" + tradePosition.getBuyPrice() +"," +tradePosition.getProductId() + "," + tradePosition.getLowerLimit() );
            tradePosition.setPositionId(response.getBody().getPositionId());
            System.out.println("RESPONSE POSITIONID:" + tradePosition.getPositionId());
            tradeRepository.save(tradePosition);
            logger.info("Buy Order Successful. Product {}, Price {}  ",
                    tradePosition.getProductId(), tradePosition.getBuyPrice());
        }
        return response.getBody();
    }

    private SellResponse sellOrder(TradePosition tradePosition){
        System.out.println("selling:" + getSellUrl(tradePosition.getPositionId()));
        ResponseEntity<SellResponse> response = restTemplate.exchange(
                getSellUrl(tradePosition.getPositionId()), HttpMethod.DELETE, null, SellResponse.class);
        //TODO API Exception handling
        if(response.getStatusCode().equals(HttpStatus.OK)){
            System.out.println("SELLING" + tradePosition.getBuyPrice() +"," +tradePosition.getProductId() + "," + tradePosition.getLowerLimit() );
            tradeRepository.delete(tradePosition);
            logger.info("Sell Order Successful. Product {}, Buy Price {} , Current Price {}",
                    tradePosition.getProductId(), tradePosition.getBuyPrice(), response.getBody().getPrice().getAmount());
        }
        return response.getBody();
    }

    //TODO use lombok builder - builder pattern - immutable
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
        return apiUrl + BUY_ENDPOINT;
    }

    private String getSellUrl(String positionId){
        System.out.println("SELL URL POSITION ID: " + positionId);
        return apiUrl + SELL_ENDPOINT + positionId;
    }

    private boolean success(ResponseEntity response ){
        return response.getStatusCode().equals(HttpStatus.OK);
    }
}
