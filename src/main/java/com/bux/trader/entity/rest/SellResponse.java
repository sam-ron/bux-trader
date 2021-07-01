package com.bux.trader.entity.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellResponse {
    private String id;
    private String position;
    private Product product;
    private Amount profitLoss;
    private Amount amount;
    private Amount price;
    private Integer leverage;
    private Direction direction;
    private String type;
    private Long dateCreated;
}
