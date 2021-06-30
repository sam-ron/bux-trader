package com.bux.trader.entity.repository;

import javax.persistence.*;

@Entity
public class TradePosition {

    @Id
    private String productId;
    private String buyPrice;
    private Float upperLimit;
    private Float lowerLimit;

    public TradePosition() {

    }

    public TradePosition(String productId, String buyPrice, Float upperLimit, Float lowerLimit) {
        this.productId = productId;
        this.buyPrice = buyPrice;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Float getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Float upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Float getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Float lowerLimit) {
        this.lowerLimit = lowerLimit;
    }
}
