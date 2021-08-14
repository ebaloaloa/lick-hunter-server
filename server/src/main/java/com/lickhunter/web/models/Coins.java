package com.lickhunter.web.models;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "symbol",
        "longoffset",
        "shortoffset"
})
public class Coins {
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("longoffset")
    private String longoffset;
    @JsonProperty("shortoffset")
    private String shortoffset;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("longoffset")
    public String getLongoffset() {
        return longoffset;
    }

    @JsonProperty("longoffset")
    public void setLongoffset(String longoffset) {
        this.longoffset = longoffset;
    }

    @JsonProperty("shortoffset")
    public String getShortoffset() {
        return shortoffset;
    }

    @JsonProperty("shortoffset")
    public void setShortoffset(String shortoffset) {
        this.shortoffset = shortoffset;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}