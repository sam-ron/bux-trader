package com.bux.trader.entity.rest;


public class TradeQuote {

    private String securityId;
    private Float currentPrice;

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = Float.parseFloat(currentPrice);
    }

}
