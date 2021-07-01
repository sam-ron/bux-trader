package com.bux.trader.service;

import com.bux.trader.config.exception.PriceConfigurationException;
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

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${bux.api.url}")
    private String apiUrl;

    @Value("${bux.trade.upper.limit}")
    private Float buyUpperLimit;

    @Value("${bux.trade.lower.limit}")
    private Float buyLowerLimit;

    @Value("${bux.trade.buy.amount}")
    private Float tradeSize;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TradeRepository tradeRepository;

    private static final String BUY_ENDPOINT = "users/me/trades";
    private static final String SELL_ENDPOINT = "users/me/portfolio/positions/";


    public boolean processQuote(TradeQuote tradeQuote) throws PriceConfigurationException{
        // If there's no open trade on given product ID then open one
        if(tradeRepository.findByProductId(tradeQuote.getSecurityId()) == null){
            TradePosition tradePosition = createTradePosition(tradeQuote);
            buyOrder(tradePosition);
        }
        // If there is an open trade then determine how to proceed
        else{
            log.info("Trading quote received for product {}, updated price: {} ", tradeQuote.getSecurityId(), tradeQuote.getCurrentPrice());
            TradePosition tradePosition = tradeRepository.findByProductId(tradeQuote.getSecurityId());
            return checkStatus(tradePosition, tradeQuote);
        }
        return true;
    }

    /**
     * if the price is still within the threshold then return true to continue receiving quotes,
     * otherwise price limits were exceeded, sell order and return false to stop receiving quotes
     */
    private boolean checkStatus(TradePosition tradePosition, TradeQuote tradeQuote){
        if (tradeQuote.getCurrentPrice() > buyLowerLimit && tradeQuote.getCurrentPrice() < buyUpperLimit){
            return true;
        }
        else{
            sellOrder(tradePosition);
            return false;
        }
    }

     private BuyResponse buyOrder(TradePosition tradePosition){
        if(buyLowerLimit >= buyUpperLimit){
             throw new PriceConfigurationException("Buy/Sell limits are not configured correctly.");
        }

        BuyRequest request = createBuyRequest(tradePosition);
        ResponseEntity<BuyResponse> response = restTemplate.postForEntity(getBuyUrl(), request, BuyResponse.class);

        if(response.getStatusCode().equals(HttpStatus.OK)){
            tradePosition.setPositionId(response.getBody().getPositionId());
            tradeRepository.save(tradePosition);
            log.info("Buy Order Successful. Product {}, Buy Price {}, Upper Limit {}, Lower Limit {}  ",
                    tradePosition.getProductId(), tradePosition.getBuyPrice(), buyUpperLimit, buyLowerLimit);
        }
        return response.getBody();
    }

    private SellResponse sellOrder(TradePosition tradePosition){
        ResponseEntity<SellResponse> response = restTemplate.exchange(
                getSellUrl(tradePosition.getPositionId()), HttpMethod.DELETE, null, SellResponse.class);

        //TODO API Exception handling
        if(response.getStatusCode().equals(HttpStatus.OK)){
            tradeRepository.delete(tradePosition);
            log.info("Sell Order Successful. Product {}, Buy Price {} , Current Price {}",
                    tradePosition.getProductId(), tradePosition.getBuyPrice(), response.getBody().getPrice().getAmount());
        }
        return response.getBody();
    }

    private BuyRequest createBuyRequest(TradePosition tradePosition){
        return BuyRequest.builder()
                .productId(tradePosition.getProductId())
                .investingAmount(new Amount("BUX", 2, tradeSize))
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

    private TradePosition createTradePosition(TradeQuote tradeQuote){
        return new TradePosition(tradeQuote.getSecurityId(), tradeQuote.getCurrentPrice(), buyUpperLimit, buyLowerLimit);
    }
}
