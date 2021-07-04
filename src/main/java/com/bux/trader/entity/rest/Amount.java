package com.bux.trader.entity.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Amount {
    private String currency;
    private Integer decimals;
    private double amount;
}