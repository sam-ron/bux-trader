package com.bux.trader.entity.repository;

import lombok.AllArgsConstructor;
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

    public TradePosition(String productId, Float buyPrice, Float upperLimit, Float lowerLimit) {
        this.productId = productId;
        this.buyPrice = buyPrice;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

}
