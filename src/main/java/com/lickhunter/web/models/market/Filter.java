
package com.lickhunter.web.models.market;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "minPrice",
    "maxPrice",
    "filterType",
    "tickSize",
    "stepSize",
    "maxQty",
    "minQty",
    "limit",
    "notional",
    "multiplierDown",
    "multiplierUp",
    "multiplierDecimal"
})
public class Filter {

    @JsonProperty("minPrice")
    private String minPrice;
    @JsonProperty("maxPrice")
    private String maxPrice;
    @JsonProperty("filterType")
    private String filterType;
    @JsonProperty("tickSize")
    private String tickSize;
    @JsonProperty("stepSize")
    private String stepSize;
    @JsonProperty("maxQty")
    private String maxQty;
    @JsonProperty("minQty")
    private String minQty;
    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("notional")
    private String notional;
    @JsonProperty("multiplierDown")
    private String multiplierDown;
    @JsonProperty("multiplierUp")
    private String multiplierUp;
    @JsonProperty("multiplierDecimal")
    private String multiplierDecimal;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("minPrice")
    public String getMinPrice() {
        return minPrice;
    }

    @JsonProperty("minPrice")
    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    @JsonProperty("maxPrice")
    public String getMaxPrice() {
        return maxPrice;
    }

    @JsonProperty("maxPrice")
    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    @JsonProperty("filterType")
    public String getFilterType() {
        return filterType;
    }

    @JsonProperty("filterType")
    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    @JsonProperty("tickSize")
    public String getTickSize() {
        return tickSize;
    }

    @JsonProperty("tickSize")
    public void setTickSize(String tickSize) {
        this.tickSize = tickSize;
    }

    @JsonProperty("stepSize")
    public String getStepSize() {
        return stepSize;
    }

    @JsonProperty("stepSize")
    public void setStepSize(String stepSize) {
        this.stepSize = stepSize;
    }

    @JsonProperty("maxQty")
    public String getMaxQty() {
        return maxQty;
    }

    @JsonProperty("maxQty")
    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }

    @JsonProperty("minQty")
    public String getMinQty() {
        return minQty;
    }

    @JsonProperty("minQty")
    public void setMinQty(String minQty) {
        this.minQty = minQty;
    }

    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty("limit")
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @JsonProperty("notional")
    public String getNotional() {
        return notional;
    }

    @JsonProperty("notional")
    public void setNotional(String notional) {
        this.notional = notional;
    }

    @JsonProperty("multiplierDown")
    public String getMultiplierDown() {
        return multiplierDown;
    }

    @JsonProperty("multiplierDown")
    public void setMultiplierDown(String multiplierDown) {
        this.multiplierDown = multiplierDown;
    }

    @JsonProperty("multiplierUp")
    public String getMultiplierUp() {
        return multiplierUp;
    }

    @JsonProperty("multiplierUp")
    public void setMultiplierUp(String multiplierUp) {
        this.multiplierUp = multiplierUp;
    }

    @JsonProperty("multiplierDecimal")
    public String getMultiplierDecimal() {
        return multiplierDecimal;
    }

    @JsonProperty("multiplierDecimal")
    public void setMultiplierDecimal(String multiplierDecimal) {
        this.multiplierDecimal = multiplierDecimal;
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
