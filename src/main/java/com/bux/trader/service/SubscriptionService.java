package com.bux.trader.service;

import com.bux.trader.entity.websocket.SubscriptionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;

@Service
public class SubscriptionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${bux.product.id}")
    private String productId;

    public void SubscribeToProductChannel(Session session){
        String subscribeRequest = new SubscriptionRequest(productId).toJson();
        try {
            logger.info("Subscribing to product: " + productId);
            session.getBasicRemote().sendText(subscribeRequest);
        } catch (IOException e) {
            logger.error("Error subscribing to channel for product:" + productId);
        }

    }
}
