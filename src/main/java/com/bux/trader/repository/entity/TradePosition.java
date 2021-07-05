package com.bux.trader.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TradePosition {

    @Id
    private String positionId;
    private String productId;
    private Float buyPrice;
    private Float upperLimit;
    private Float lowerLimit;
    private Double buyAmount;

    public TradePosition(String productId, Float buyPrice, Float upperLimit, Float lowerLimit, Double buyAmount) {
        this.productId = productId;
        this.buyPrice = buyPrice;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.buyAmount = buyAmount;
    }

}
