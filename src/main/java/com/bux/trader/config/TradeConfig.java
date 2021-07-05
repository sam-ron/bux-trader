package com.bux.trader.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TradeConfig {

    @Value("${bux.trade.upper.limit}")
    private Float buyUpperLimit;

    @Value("${bux.trade.lower.limit}")
    private Float buyLowerLimit;

    @Value("${bux.trade.buy.amount}")
    private Double buyAmount;
}
