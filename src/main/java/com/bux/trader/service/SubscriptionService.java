package com.bux.trader.service;

import com.bux.trader.websocket.entity.SubscriptionRequest;
import com.bux.trader.websocket.entity.UnsubscriptionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;

@Service
public class SubscriptionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${bux.product.id}")
    private String productId;

    public void subscribeToProductChannel(Session session){
        String subscribeRequest = new SubscriptionRequest(productId).toJson();
        try {
            log.info("Subscribing to product: " + productId);
            session.getBasicRemote().sendText(subscribeRequest);
        } catch (IOException e) {
            log.error("Error subscribing to channel for product:" + productId);
        }
    }

    public void unsubscribeFromProductChannel(Session session){
        String subscribeRequest = new UnsubscriptionRequest(productId).toJson();
        try {
            log.info("Unsubscribing from product: " + productId);
            session.getBasicRemote().sendText(subscribeRequest);
        } catch (IOException e) {
            log.error("Error unsubscribing to channel from product:" + productId);
        }
    }
}
