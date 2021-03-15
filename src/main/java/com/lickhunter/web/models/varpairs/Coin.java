
package com.lickhunter.web.models.varpairs;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "symbol",
    "longoffset",
    "shortoffset",
    "lickvalue",
    "var_enabled",
    "var_staticList",
    "var_whiteList",
    "var_blackList",
    "tmp_onboarded",
    "tmp_color"
})
public class Coin {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("longoffset")
    private String longoffset;
    @JsonProperty("shortoffset")
    private String shortoffset;
    @JsonProperty("lickvalue")
    private String lickvalue;
    @JsonProperty("var_enabled")
    private Boolean varEnabled;
    @JsonProperty("var_staticList")
    private Boolean varStaticList;
    @JsonProperty("var_whiteList")
    private Boolean varWhiteList;
    @JsonProperty("var_blackList")
    private Boolean varBlackList;
    @JsonProperty("tmp_onboarded")
    private String tmpOnboarded;
    @JsonProperty("tmp_color")
    private String tmpColor;
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

    @JsonProperty("lickvalue")
    public String getLickvalue() {
        return lickvalue;
    }

    @JsonProperty("lickvalue")
    public void setLickvalue(String lickvalue) {
        this.lickvalue = lickvalue;
    }

    @JsonProperty("var_enabled")
    public Boolean getVarEnabled() {
        return varEnabled;
    }

    @JsonProperty("var_enabled")
    public void setVarEnabled(Boolean varEnabled) {
        this.varEnabled = varEnabled;
    }

    @JsonProperty("var_staticList")
    public Boolean getVarStaticList() {
        return varStaticList;
    }

    @JsonProperty("var_staticList")
    public void setVarStaticList(Boolean varStaticList) {
        this.varStaticList = varStaticList;
    }

    @JsonProperty("var_whiteList")
    public Boolean getVarWhiteList() {
        return varWhiteList;
    }

    @JsonProperty("var_whiteList")
    public void setVarWhiteList(Boolean varWhiteList) {
        this.varWhiteList = varWhiteList;
    }

    @JsonProperty("var_blackList")
    public Boolean getVarBlackList() {
        return varBlackList;
    }

    @JsonProperty("var_blackList")
    public void setVarBlackList(Boolean varBlackList) {
        this.varBlackList = varBlackList;
    }

    @JsonProperty("tmp_onboarded")
    public String getTmpOnboarded() {
        return tmpOnboarded;
    }

    @JsonProperty("tmp_onboarded")
    public void setTmpOnboarded(String tmpOnboarded) {
        this.tmpOnboarded = tmpOnboarded;
    }

    @JsonProperty("tmp_color")
    public String getTmpColor() {
        return tmpColor;
    }

    @JsonProperty("tmp_color")
    public void setTmpColor(String tmpColor) {
        this.tmpColor = tmpColor;
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
