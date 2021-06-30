package com.bux.trader.entity.websocket;

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
