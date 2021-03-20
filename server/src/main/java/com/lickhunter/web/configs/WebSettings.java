package com.lickhunter.web.configs;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maxOpen",
        "openOrderIsolationPercentage",
        "longOffset",
        "shortOffset",
        "lickValue"
})
public class WebSettings {

    @JsonProperty("maxOpen")
    private Integer maxOpen;
    @JsonProperty("openOrderIsolationPercentage")
    private Integer openOrderIsolationPercentage;
    @JsonProperty("longOffset")
    private Integer longOffset;
    @JsonProperty("shortOffset")
    private Integer shortOffset;
    @JsonProperty("lickValue")
    private Integer lickValue;
    @JsonProperty("marginPercentNotification")
    private Integer marginPercentNotification;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("maxOpen")
    public Integer getMaxOpen() {
        return maxOpen;
    }

    @JsonProperty("maxOpen")
    public void setMaxOpen(Integer maxOpen) {
        this.maxOpen = maxOpen;
    }

    @JsonProperty("openOrderIsolationPercentage")
    public Integer getOpenOrderIsolationPercentage() {
        return openOrderIsolationPercentage;
    }

    @JsonProperty("openOrderIsolationPercentage")
    public void setOpenOrderIsolationPercentage(Integer openOrderIsolationPercentage) {
        this.openOrderIsolationPercentage = openOrderIsolationPercentage;
    }

    @JsonProperty("longOffset")
    public Integer getLongOffset() {
        return longOffset;
    }

    @JsonProperty("longOffset")
    public void setLongOffset(Integer longOffset) {
        this.longOffset = longOffset;
    }

    @JsonProperty("shortOffset")
    public Integer getShortOffset() {
        return shortOffset;
    }

    @JsonProperty("shortOffset")
    public void setShortOffset(Integer shortOffset) {
        this.shortOffset = shortOffset;
    }

    @JsonProperty("lickValue")
    public Integer getLickValue() {
        return lickValue;
    }

    @JsonProperty("lickValue")
    public void setLickValue(Integer lickValue) {
        this.lickValue = lickValue;
    }

    @JsonProperty("marginPercentNotification")
    public Integer getMarginPercentNotification() {
        return marginPercentNotification;
    }

    @JsonProperty("marginPercentNotification")
    public void setMarginPercentNotification(Integer marginPercentNotification) {
        this.marginPercentNotification = marginPercentNotification;
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