package com.bux.trader.entity.rest;

public class BuyRequest {

    private String productId;
    private Amount amount;
    private Integer leverage;
    private Direction direction;
    private Source source;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Amount getInvestingAmount() {
        return amount;
    }

    public void setInvestingAmount(Amount amount) {
        this.amount = amount;
    }

    public Integer getLeverage() {
        return leverage;
    }

    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

}
