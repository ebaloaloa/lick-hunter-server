package com.lickhunter.web.constants;

public enum UserDataEventConstants {

    ACCOUNT_UPDATE("ACCOUNT_UPDATE"),
    MARGIN_CALL("MARGIN_CALL"),
    ORDER_TRADE_UPDATE("ORDER_TRADE_UPDATE"),
    ACCOUNT_CONFIG_UPDATE("ACCOUNT_CONFIG_UPDATE");

    private final String value;

    UserDataEventConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
