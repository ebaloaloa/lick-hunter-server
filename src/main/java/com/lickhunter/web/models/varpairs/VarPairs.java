
package com.lickhunter.web.models.varpairs;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "botName",
    "maxPairs",
    "maxOpen",
    "openOrderIsolationPercentage",
    "tradingMode",
    "tradePairs",
    "fundingRateThreshold",
    "maxFundingRate",
    "tradingAge",
    "marginType",
    "refreshTime",
    "lhpcKey",
    "coins"
})
public class VarPairs {

    @JsonProperty("botName")
    private String botName;
    @JsonProperty("maxPairs")
    private String maxPairs;
    @JsonProperty("maxOpen")
    private String maxOpen;
    @JsonProperty("openOrderIsolationPercentage")
    private String openOrderIsolationPercentage;
    @JsonProperty("tradingMode")
    private String tradingMode;
    @JsonProperty("tradePairs")
    private String tradePairs;
    @JsonProperty("fundingRateThreshold")
    private Boolean fundingRateThreshold;
    @JsonProperty("maxFundingRate")
    private String maxFundingRate;
    @JsonProperty("tradingAge")
    private String tradingAge;
    @JsonProperty("marginType")
    private String marginType;
    @JsonProperty("refreshTime")
    private String refreshTime;
    @JsonProperty("lhpcKey")
    private String lhpcKey;
    @JsonProperty("coins")
    private List<Coin> coins = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("botName")
    public String getBotName() {
        return botName;
    }

    @JsonProperty("botName")
    public void setBotName(String botName) {
        this.botName = botName;
    }

    @JsonProperty("maxPairs")
    public String getMaxPairs() {
        return maxPairs;
    }

    @JsonProperty("maxPairs")
    public void setMaxPairs(String maxPairs) {
        this.maxPairs = maxPairs;
    }

    @JsonProperty("maxOpen")
    public String getMaxOpen() {
        return maxOpen;
    }

    @JsonProperty("maxOpen")
    public void setMaxOpen(String maxOpen) {
        this.maxOpen = maxOpen;
    }

    @JsonProperty("openOrderIsolationPercentage")
    public String getOpenOrderIsolationPercentage() {
        return openOrderIsolationPercentage;
    }

    @JsonProperty("openOrderIsolationPercentage")
    public void setOpenOrderIsolationPercentage(String openOrderIsolationPercentage) {
        this.openOrderIsolationPercentage = openOrderIsolationPercentage;
    }

    @JsonProperty("tradingMode")
    public String getTradingMode() {
        return tradingMode;
    }

    @JsonProperty("tradingMode")
    public void setTradingMode(String tradingMode) {
        this.tradingMode = tradingMode;
    }

    @JsonProperty("tradePairs")
    public String getTradePairs() {
        return tradePairs;
    }

    @JsonProperty("tradePairs")
    public void setTradePairs(String tradePairs) {
        this.tradePairs = tradePairs;
    }

    @JsonProperty("fundingRateThreshold")
    public Boolean getFundingRateThreshold() {
        return fundingRateThreshold;
    }

    @JsonProperty("fundingRateThreshold")
    public void setFundingRateThreshold(Boolean fundingRateThreshold) {
        this.fundingRateThreshold = fundingRateThreshold;
    }

    @JsonProperty("maxFundingRate")
    public String getMaxFundingRate() {
        return maxFundingRate;
    }

    @JsonProperty("maxFundingRate")
    public void setMaxFundingRate(String maxFundingRate) {
        this.maxFundingRate = maxFundingRate;
    }

    @JsonProperty("tradingAge")
    public String getTradingAge() {
        return tradingAge;
    }

    @JsonProperty("tradingAge")
    public void setTradingAge(String tradingAge) {
        this.tradingAge = tradingAge;
    }

    @JsonProperty("marginType")
    public String getMarginType() {
        return marginType;
    }

    @JsonProperty("marginType")
    public void setMarginType(String marginType) {
        this.marginType = marginType;
    }

    @JsonProperty("refreshTime")
    public String getRefreshTime() {
        return refreshTime;
    }

    @JsonProperty("refreshTime")
    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    @JsonProperty("lhpcKey")
    public String getLhpcKey() {
        return lhpcKey;
    }

    @JsonProperty("lhpcKey")
    public void setLhpcKey(String lhpcKey) {
        this.lhpcKey = lhpcKey;
    }

    @JsonProperty("coins")
    public List<Coin> getCoins() {
        return coins;
    }

    @JsonProperty("coins")
    public void setCoins(List<Coin> coins) {
        this.coins = coins;
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
