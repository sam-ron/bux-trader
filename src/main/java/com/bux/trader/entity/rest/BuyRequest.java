package com.bux.trader.entity.rest;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyRequest {
    private String productId;
    private Amount investingAmount;
    private Integer leverage;
    private Direction direction;
    private Source source;
}
