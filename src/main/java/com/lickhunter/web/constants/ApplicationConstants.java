package com.lickhunter.web.constants;

public enum ApplicationConstants {

    SETTINGS("settings.json"),
    VAR_PAIRS("varPairs.json");

    private final String value;

    ApplicationConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
