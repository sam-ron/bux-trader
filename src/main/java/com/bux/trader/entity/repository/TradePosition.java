package com.bux.trader.entity.repository;

import javax.persistence.*;

@Entity
public class TradePosition {

    @Id
    private String positionId;
    private String productId;
    private Float buyPrice;
    private Float upperLimit;
    private Float lowerLimit;

    public TradePosition() {

    }

    public TradePosition(String productId, Float buyPrice, Float upperLimit, Float lowerLimit) {
        this.productId = productId;
        this.buyPrice = buyPrice;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }


    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Float buyPrice) {
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
