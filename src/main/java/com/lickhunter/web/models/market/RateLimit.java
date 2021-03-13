
package com.lickhunter.web.models.market;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rateLimitType",
    "interval",
    "intervalNum",
    "limit"
})
public class RateLimit {

    @JsonProperty("rateLimitType")
    private String rateLimitType;
    @JsonProperty("interval")
    private String interval;
    @JsonProperty("intervalNum")
    private Integer intervalNum;
    @JsonProperty("limit")
    private Integer limit;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("rateLimitType")
    public String getRateLimitType() {
        return rateLimitType;
    }

    @JsonProperty("rateLimitType")
    public void setRateLimitType(String rateLimitType) {
        this.rateLimitType = rateLimitType;
    }

    @JsonProperty("interval")
    public String getInterval() {
        return interval;
    }

    @JsonProperty("interval")
    public void setInterval(String interval) {
        this.interval = interval;
    }

    @JsonProperty("intervalNum")
    public Integer getIntervalNum() {
        return intervalNum;
    }

    @JsonProperty("intervalNum")
    public void setIntervalNum(Integer intervalNum) {
        this.intervalNum = intervalNum;
    }

    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty("limit")
    public void setLimit(Integer limit) {
        this.limit = limit;
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
