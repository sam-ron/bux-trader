package com.bux.trader.service;

import com.bux.trader.config.TradeConfig;
import com.bux.trader.config.exception.PriceConfigurationException;
import com.bux.trader.rest.controller.TradeController;
import com.bux.trader.repository.entity.TradePosition;
import com.bux.trader.rest.entity.BuyRequest;
import com.bux.trader.rest.entity.BuyResponse;
import com.bux.trader.rest.entity.SellResponse;
import com.bux.trader.rest.entity.TradeQuote;
import com.bux.trader.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TradeController tradeController;

    @Autowired
    private TradeConfig tradeConfig;


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
        if (tradeQuote.getCurrentPrice() > tradeConfig.getBuyLowerLimit() && tradeQuote.getCurrentPrice() < tradeConfig.getBuyUpperLimit()){
            return true;
        }
        else{
            sellOrder(tradePosition);
            return false;
        }
    }

     private void buyOrder(TradePosition tradePosition){
        if(tradeConfig.getBuyLowerLimit() >= tradeConfig.getBuyUpperLimit()){
             throw new PriceConfigurationException("Buy/Sell limits are not configured correctly.");
        }

        BuyResponse buyResponse = tradeController.buyOrder(tradePosition);
        tradePosition.setPositionId(buyResponse.getPositionId());
        tradeRepository.save(tradePosition);
        log.info("Buy Order Successful. Product {}, Buy Price {}, Upper Limit {}, Lower Limit {}  ",
                tradePosition.getProductId(), tradePosition.getBuyPrice(), tradeConfig.getBuyUpperLimit(), tradeConfig.getBuyLowerLimit());
    }

    private void sellOrder(TradePosition tradePosition){
        SellResponse sellResponse = tradeController.sellOrder(tradePosition);
        tradeRepository.delete(tradePosition);
        log.info("Sell Order Successful. Product {}, Buy Price {} , Current Price {}",
                tradePosition.getProductId(), tradePosition.getBuyPrice(), sellResponse.getPrice().getAmount());
    }


    private TradePosition createTradePosition(TradeQuote tradeQuote){
        return new TradePosition(tradeQuote.getSecurityId(), tradeQuote.getCurrentPrice(), tradeConfig.getBuyUpperLimit(),
                tradeConfig.getBuyLowerLimit(), tradeConfig.getBuyAmount());
    }
}
