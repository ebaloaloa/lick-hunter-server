
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
    "symbol",
    "pair",
    "contractType",
    "deliveryDate",
    "onboardDate",
    "status",
    "maintMarginPercent",
    "requiredMarginPercent",
    "baseAsset",
    "quoteAsset",
    "marginAsset",
    "pricePrecision",
    "quantityPrecision",
    "baseAssetPrecision",
    "quotePrecision",
    "underlyingType",
    "underlyingSubType",
    "settlePlan",
    "triggerProtect",
    "filters",
    "orderTypes",
    "timeInForce"
})
public class Symbol {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("pair")
    private String pair;
    @JsonProperty("contractType")
    private String contractType;
    @JsonProperty("deliveryDate")
    private BigDecimal deliveryDate;
    @JsonProperty("onboardDate")
    private BigDecimal onboardDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("maintMarginPercent")
    private String maintMarginPercent;
    @JsonProperty("requiredMarginPercent")
    private String requiredMarginPercent;
    @JsonProperty("baseAsset")
    private String baseAsset;
    @JsonProperty("quoteAsset")
    private String quoteAsset;
    @JsonProperty("marginAsset")
    private String marginAsset;
    @JsonProperty("pricePrecision")
    private Integer pricePrecision;
    @JsonProperty("quantityPrecision")
    private Integer quantityPrecision;
    @JsonProperty("baseAssetPrecision")
    private Integer baseAssetPrecision;
    @JsonProperty("quotePrecision")
    private Integer quotePrecision;
    @JsonProperty("underlyingType")
    private String underlyingType;
    @JsonProperty("underlyingSubType")
    private List<Object> underlyingSubType = null;
    @JsonProperty("settlePlan")
    private Integer settlePlan;
    @JsonProperty("triggerProtect")
    private String triggerProtect;
    @JsonProperty("filters")
    private List<Filter> filters = null;
    @JsonProperty("orderTypes")
    private List<String> orderTypes = null;
    @JsonProperty("timeInForce")
    private List<String> timeInForce = null;
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

    @JsonProperty("pair")
    public String getPair() {
        return pair;
    }

    @JsonProperty("pair")
    public void setPair(String pair) {
        this.pair = pair;
    }

    @JsonProperty("contractType")
    public String getContractType() {
        return contractType;
    }

    @JsonProperty("contractType")
    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    @JsonProperty("deliveryDate")
    public BigDecimal getDeliveryDate() {
        return deliveryDate;
    }

    @JsonProperty("deliveryDate")
    public void setDeliveryDate(BigDecimal deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @JsonProperty("onboardDate")
    public BigDecimal getOnboardDate() {
        return onboardDate;
    }

    @JsonProperty("onboardDate")
    public void setOnboardDate(BigDecimal onboardDate) {
        this.onboardDate = onboardDate;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("maintMarginPercent")
    public String getMaintMarginPercent() {
        return maintMarginPercent;
    }

    @JsonProperty("maintMarginPercent")
    public void setMaintMarginPercent(String maintMarginPercent) {
        this.maintMarginPercent = maintMarginPercent;
    }

    @JsonProperty("requiredMarginPercent")
    public String getRequiredMarginPercent() {
        return requiredMarginPercent;
    }

    @JsonProperty("requiredMarginPercent")
    public void setRequiredMarginPercent(String requiredMarginPercent) {
        this.requiredMarginPercent = requiredMarginPercent;
    }

    @JsonProperty("baseAsset")
    public String getBaseAsset() {
        return baseAsset;
    }

    @JsonProperty("baseAsset")
    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    @JsonProperty("quoteAsset")
    public String getQuoteAsset() {
        return quoteAsset;
    }

    @JsonProperty("quoteAsset")
    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    @JsonProperty("marginAsset")
    public String getMarginAsset() {
        return marginAsset;
    }

    @JsonProperty("marginAsset")
    public void setMarginAsset(String marginAsset) {
        this.marginAsset = marginAsset;
    }

    @JsonProperty("pricePrecision")
    public Integer getPricePrecision() {
        return pricePrecision;
    }

    @JsonProperty("pricePrecision")
    public void setPricePrecision(Integer pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    @JsonProperty("quantityPrecision")
    public Integer getQuantityPrecision() {
        return quantityPrecision;
    }

    @JsonProperty("quantityPrecision")
    public void setQuantityPrecision(Integer quantityPrecision) {
        this.quantityPrecision = quantityPrecision;
    }

    @JsonProperty("baseAssetPrecision")
    public Integer getBaseAssetPrecision() {
        return baseAssetPrecision;
    }

    @JsonProperty("baseAssetPrecision")
    public void setBaseAssetPrecision(Integer baseAssetPrecision) {
        this.baseAssetPrecision = baseAssetPrecision;
    }

    @JsonProperty("quotePrecision")
    public Integer getQuotePrecision() {
        return quotePrecision;
    }

    @JsonProperty("quotePrecision")
    public void setQuotePrecision(Integer quotePrecision) {
        this.quotePrecision = quotePrecision;
    }

    @JsonProperty("underlyingType")
    public String getUnderlyingType() {
        return underlyingType;
    }

    @JsonProperty("underlyingType")
    public void setUnderlyingType(String underlyingType) {
        this.underlyingType = underlyingType;
    }

    @JsonProperty("underlyingSubType")
    public List<Object> getUnderlyingSubType() {
        return underlyingSubType;
    }

    @JsonProperty("underlyingSubType")
    public void setUnderlyingSubType(List<Object> underlyingSubType) {
        this.underlyingSubType = underlyingSubType;
    }

    @JsonProperty("settlePlan")
    public Integer getSettlePlan() {
        return settlePlan;
    }

    @JsonProperty("settlePlan")
    public void setSettlePlan(Integer settlePlan) {
        this.settlePlan = settlePlan;
    }

    @JsonProperty("triggerProtect")
    public String getTriggerProtect() {
        return triggerProtect;
    }

    @JsonProperty("triggerProtect")
    public void setTriggerProtect(String triggerProtect) {
        this.triggerProtect = triggerProtect;
    }

    @JsonProperty("filters")
    public List<Filter> getFilters() {
        return filters;
    }

    @JsonProperty("filters")
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    @JsonProperty("orderTypes")
    public List<String> getOrderTypes() {
        return orderTypes;
    }

    @JsonProperty("orderTypes")
    public void setOrderTypes(List<String> orderTypes) {
        this.orderTypes = orderTypes;
    }

    @JsonProperty("timeInForce")
    public List<String> getTimeInForce() {
        return timeInForce;
    }

    @JsonProperty("timeInForce")
    public void setTimeInForce(List<String> timeInForce) {
        this.timeInForce = timeInForce;
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
