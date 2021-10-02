
package com.lickhunter.web.models.liquidation;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "last_liquidation",
    "lick_amount",
    "lick_value",
    "mean_value",
    "median_value",
    "mode_value",
    "name"
})
@Generated("jsonschema2pojo")
public class Datum {

    @JsonProperty("last_liquidation")
    private Long lastLiquidation;
    @JsonProperty("lick_amount")
    private Integer lickAmount;
    @JsonProperty("lick_value")
    private Integer lickValue;
    @JsonProperty("mean_value")
    private Integer meanValue;
    @JsonProperty("median_value")
    private Integer medianValue;
    @JsonProperty("mode_value")
    private Integer modeValue;
    @JsonProperty("name")
    private String name;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("last_liquidation")
    public Long getLastLiquidation() {
        return lastLiquidation;
    }

    @JsonProperty("last_liquidation")
    public void setLastLiquidation(Long lastLiquidation) {
        this.lastLiquidation = lastLiquidation;
    }

    @JsonProperty("lick_amount")
    public Integer getLickAmount() {
        return lickAmount;
    }

    @JsonProperty("lick_amount")
    public void setLickAmount(Integer lickAmount) {
        this.lickAmount = lickAmount;
    }

    @JsonProperty("lick_value")
    public Integer getLickValue() {
        return lickValue;
    }

    @JsonProperty("lick_value")
    public void setLickValue(Integer lickValue) {
        this.lickValue = lickValue;
    }

    @JsonProperty("mean_value")
    public Integer getMeanValue() {
        return meanValue;
    }

    @JsonProperty("mean_value")
    public void setMeanValue(Integer meanValue) {
        this.meanValue = meanValue;
    }

    @JsonProperty("median_value")
    public Integer getMedianValue() {
        return medianValue;
    }

    @JsonProperty("median_value")
    public void setMedianValue(Integer medianValue) {
        this.medianValue = medianValue;
    }

    @JsonProperty("mode_value")
    public Integer getModeValue() {
        return modeValue;
    }

    @JsonProperty("mode_value")
    public void setModeValue(Integer modeValue) {
        this.modeValue = modeValue;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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
