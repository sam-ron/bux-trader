package com.bux.trader.entity.websocket;

public class SubscriptionRequest {

    private String productId;

    public SubscriptionRequest(String productId){
        this.productId = productId;
    }

    public String toJson() {
        return "{\n" +
                "\"subscribeTo\": [\n" +
                "\"trading.product." + productId + "\"\n" +
                "]}";
    }
}
