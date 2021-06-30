package com.bux.trader.entity.rest;

public class Amount {
    private String currency;
    private Integer decimals;
    private double amount;

    public Amount(String currency, Integer decimals, double amount) {
        this.currency = currency;
        this.decimals = decimals;
        this.amount = amount;
    }

    public Amount(){}

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
