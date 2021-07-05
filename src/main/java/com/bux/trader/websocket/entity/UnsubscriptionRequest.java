package com.bux.trader.websocket.entity;

public class UnsubscriptionRequest {

    private String productId;

    public UnsubscriptionRequest(String productId){
        this.productId = productId;
    }

    public String toJson() {
        return "{\n" +
                "\"unsubscribeFrom\": [\n" +
                "\"trading.product." + productId + "\"\n" +
                "]}";
    }
}
