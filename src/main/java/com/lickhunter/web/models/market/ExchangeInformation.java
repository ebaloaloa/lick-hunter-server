
package com.lickhunter.web.models.market;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "timezone",
    "serverTime",
    "futuresType",
    "rateLimits",
    "exchangeFilters",
    "symbols"
})
public class ExchangeInformation {

    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("serverTime")
    private BigDecimal serverTime;
    @JsonProperty("futuresType")
    private String futuresType;
    @JsonProperty("rateLimits")
    private List<RateLimit> rateLimits = null;
    @JsonProperty("exchangeFilters")
    private List<Object> exchangeFilters = null;
    @JsonProperty("symbols")
    private List<Symbol> symbols = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }

    @JsonProperty("timezone")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @JsonProperty("serverTime")
    public BigDecimal getServerTime() {
        return serverTime;
    }

    @JsonProperty("serverTime")
    public void setServerTime(BigDecimal serverTime) {
        this.serverTime = serverTime;
    }

    @JsonProperty("futuresType")
    public String getFuturesType() {
        return futuresType;
    }

    @JsonProperty("futuresType")
    public void setFuturesType(String futuresType) {
        this.futuresType = futuresType;
    }

    @JsonProperty("rateLimits")
    public List<RateLimit> getRateLimits() {
        return rateLimits;
    }

    @JsonProperty("rateLimits")
    public void setRateLimits(List<RateLimit> rateLimits) {
        this.rateLimits = rateLimits;
    }

    @JsonProperty("exchangeFilters")
    public List<Object> getExchangeFilters() {
        return exchangeFilters;
    }

    @JsonProperty("exchangeFilters")
    public void setExchangeFilters(List<Object> exchangeFilters) {
        this.exchangeFilters = exchangeFilters;
    }

    @JsonProperty("symbols")
    public List<Symbol> getSymbols() {
        return symbols;
    }

    @JsonProperty("symbols")
    public void setSymbols(List<Symbol> symbols) {
        this.symbols = symbols;
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
