package com.bux.trader.service.rest.entity;

public class Source {
    private String sourceType;

    public Source(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
