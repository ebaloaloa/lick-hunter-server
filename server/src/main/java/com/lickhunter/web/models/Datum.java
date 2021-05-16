
package com.lickhunter.web.models;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "symbol",
    "total_events",
    "total_usdt",
    "average_usdt",
    "average_amount",
    "median_usdt",
    "median_amount",
    "previous"
})
@Generated("jsonschema2pojo")
public class Datum {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("total_events")
    private Integer totalEvents;
    @JsonProperty("total_usdt")
    private Double totalUsdt;
    @JsonProperty("average_usdt")
    private Double averageUsdt;
    @JsonProperty("average_amount")
    private String averageAmount;
    @JsonProperty("median_usdt")
    private String medianUsdt;
    @JsonProperty("median_amount")
    private String medianAmount;
    @JsonProperty("previous")
    private Previous previous;
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

    @JsonProperty("total_events")
    public Integer getTotalEvents() {
        return totalEvents;
    }

    @JsonProperty("total_events")
    public void setTotalEvents(Integer totalEvents) {
        this.totalEvents = totalEvents;
    }

    @JsonProperty("total_usdt")
    public Double getTotalUsdt() {
        return totalUsdt;
    }

    @JsonProperty("total_usdt")
    public void setTotalUsdt(Double totalUsdt) {
        this.totalUsdt = totalUsdt;
    }

    @JsonProperty("average_usdt")
    public Double getAverageUsdt() {
        return averageUsdt;
    }

    @JsonProperty("average_usdt")
    public void setAverageUsdt(Double averageUsdt) {
        this.averageUsdt = averageUsdt;
    }

    @JsonProperty("average_amount")
    public String getAverageAmount() {
        return averageAmount;
    }

    @JsonProperty("average_amount")
    public void setAverageAmount(String averageAmount) {
        this.averageAmount = averageAmount;
    }

    @JsonProperty("median_usdt")
    public String getMedianUsdt() {
        return medianUsdt;
    }

    @JsonProperty("median_usdt")
    public void setMedianUsdt(String medianUsdt) {
        this.medianUsdt = medianUsdt;
    }

    @JsonProperty("median_amount")
    public String getMedianAmount() {
        return medianAmount;
    }

    @JsonProperty("median_amount")
    public void setMedianAmount(String medianAmount) {
        this.medianAmount = medianAmount;
    }

    @JsonProperty("previous")
    public Previous getPrevious() {
        return previous;
    }

    @JsonProperty("previous")
    public void setPrevious(Previous previous) {
        this.previous = previous;
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
