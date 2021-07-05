package com.bux.trader.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TradeQuote {
    private String securityId;
    private Float currentPrice;
}
