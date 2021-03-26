package com.lickhunter.web.constants;

public enum TradeConstants {
    CROSSED("crossed"),
    ISOLATED("isolated");

    private final String value;

    TradeConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
