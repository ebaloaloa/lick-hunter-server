
package com.lickhunter.web.models.sentiments;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "type",
    "limit",
    "page",
    "total_rows",
    "btc"
})
@Generated("jsonschema2pojo")
public class Config {

    @JsonProperty("data")
    private String data;
    @JsonProperty("type")
    private String type;
    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("total_rows")
    private Integer totalRows;
    @JsonProperty("btc")
    private Btc btc;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("data")
    public String getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(String data) {
        this.data = data;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty("limit")
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    @JsonProperty("total_rows")
    public Integer getTotalRows() {
        return totalRows;
    }

    @JsonProperty("total_rows")
    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    @JsonProperty("btc")
    public Btc getBtc() {
        return btc;
    }

    @JsonProperty("btc")
    public void setBtc(Btc btc) {
        this.btc = btc;
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
