package com.lickhunter.web.constants;

public enum ErrorCode {
    //TODO enhance exception handling and error message
    RESOURCE_NOT_FOUND(4000, "Resournce not found."),
    TOO_MANY_REQUESTS(4100, "Binance API requests limitation reached.");

    private final int code;
    private final String value;

    ErrorCode(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
