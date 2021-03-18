package com.lickhunter.web.constants;

public enum ApplicationConstants {

    SETTINGS("settings.json"),
    COINS("coins.json"),
    WEB_SETTINGS("web-settings.json"),
    TICKER_QUERY("query.json");

    private final String value;

    ApplicationConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
