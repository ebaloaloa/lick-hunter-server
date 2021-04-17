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
        "lickValue",
        "marginType",
        "leverage",
        "autoLickValue"
})
public class WebSettings {

    @JsonProperty("maxOpen")
    private Integer maxOpen;
    @JsonProperty("openOrderIsolationPercentage")
    private Double openOrderIsolationPercentage;
    @JsonProperty("longOffset")
    private Double longOffset;
    @JsonProperty("shortOffset")
    private Double shortOffset;
    @JsonProperty("lickValue")
    private Integer lickValue;
    @JsonProperty("marginPercentNotification")
    private Integer marginPercentNotification;
    @JsonProperty("marginType")
    private String marginType;
    @JsonProperty("leverage")
    private Integer leverage;
    @JsonProperty("autoLickValue")
    private Boolean autoLickValue;
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
    public Double getOpenOrderIsolationPercentage() {
        return openOrderIsolationPercentage;
    }

    @JsonProperty("openOrderIsolationPercentage")
    public void setOpenOrderIsolationPercentage(Double openOrderIsolationPercentage) {
        this.openOrderIsolationPercentage = openOrderIsolationPercentage;
    }

    @JsonProperty("longOffset")
    public Double getLongOffset() {
        return longOffset;
    }

    @JsonProperty("longOffset")
    public void setLongOffset(Double longOffset) {
        this.longOffset = longOffset;
    }

    @JsonProperty("shortOffset")
    public Double getShortOffset() {
        return shortOffset;
    }

    @JsonProperty("shortOffset")
    public void setShortOffset(Double shortOffset) {
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

    @JsonProperty("marginType")
    public String getMarginType() {
        return marginType;
    }

    @JsonProperty("marginType")
    public void setMarginType(String marginType) {
        this.marginType = marginType;
    }

    @JsonProperty("leverage")
    public Integer getLeverage() {
        return leverage;
    }

    @JsonProperty("leverage")
    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }

    @JsonProperty("autoLickValue")
    public Boolean getAutoLickValue() {
        return autoLickValue;
    }

    @JsonProperty("autoLickValue")
    public void setAutoLickValue(Boolean autoLickValue) {
        this.autoLickValue = autoLickValue;
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