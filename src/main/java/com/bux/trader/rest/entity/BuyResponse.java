package com.bux.trader.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyResponse {
    private String id;
    private String positionId;
    private Product product;
    private Amount amount;
    private Amount price;
    private Integer leverage;
    private Direction direction;
    private String type;
    private Long dateCreated;

}
