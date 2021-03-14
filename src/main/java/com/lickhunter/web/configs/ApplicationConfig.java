package com.lickhunter.web.configs;

import com.fasterxml.jackson.annotation.*;
import com.lickhunter.web.factory.JsonPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "exchange",
        "key",
        "secret",
        "auto_qty",
        "longQty",
        "shortQty",
        "percentBal",
        "maxPosition",
        "dcaOne",
        "factorOne",
        "dcaTwo",
        "factorTwo",
        "leverage",
        "takeprofit",
        "stoploss",
        "auth",
        "delay",
        "discordwebhook",
        "sleeponstop"
})
@Component
@PropertySource(value = "classpath:settings.json", factory = JsonPropertySourceFactory.class)
@ConfigurationProperties
//TODO allow user to select custom location of settings.json
public class ApplicationConfig {
    @JsonProperty("exchange")
    private String exchange;
    @JsonProperty("key")
    private String key;
    @JsonProperty("secret")
    private String secret;
    @JsonProperty("auto_qty")
    private String autoQty;
    @JsonProperty("longQty")
    private String longQty;
    @JsonProperty("shortQty")
    private String shortQty;
    @JsonProperty("percentBal")
    private String percentBal;
    @JsonProperty("maxPosition")
    private String maxPosition;
    @JsonProperty("dcaOne")
    private String dcaOne;
    @JsonProperty("factorOne")
    private String factorOne;
    @JsonProperty("dcaTwo")
    private String dcaTwo;
    @JsonProperty("factorTwo")
    private String factorTwo;
    @JsonProperty("leverage")
    private String leverage;
    @JsonProperty("takeprofit")
    private String takeprofit;
    @JsonProperty("stoploss")
    private String stoploss;
    @JsonProperty("auth")
    private String auth;
    @JsonProperty("delay")
    private String delay;
    @JsonProperty("discordwebhook")
    private String discordwebhook;
    @JsonProperty("sleeponstop")
    private String sleeponstop;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("exchange")
    public String getExchange() {
        return exchange;
    }

    @JsonProperty("exchange")
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("secret")
    public String getSecret() {
        return secret;
    }

    @JsonProperty("secret")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @JsonProperty("auto_qty")
    public String getAutoQty() {
        return autoQty;
    }

    @JsonProperty("auto_qty")
    public void setAutoQty(String autoQty) {
        this.autoQty = autoQty;
    }

    @JsonProperty("longQty")
    public String getLongQty() {
        return longQty;
    }

    @JsonProperty("longQty")
    public void setLongQty(String longQty) {
        this.longQty = longQty;
    }

    @JsonProperty("shortQty")
    public String getShortQty() {
        return shortQty;
    }

    @JsonProperty("shortQty")
    public void setShortQty(String shortQty) {
        this.shortQty = shortQty;
    }

    @JsonProperty("percentBal")
    public String getPercentBal() {
        return percentBal;
    }

    @JsonProperty("percentBal")
    public void setPercentBal(String percentBal) {
        this.percentBal = percentBal;
    }

    @JsonProperty("maxPosition")
    public String getMaxPosition() {
        return maxPosition;
    }

    @JsonProperty("maxPosition")
    public void setMaxPosition(String maxPosition) {
        this.maxPosition = maxPosition;
    }

    @JsonProperty("dcaOne")
    public String getDcaOne() {
        return dcaOne;
    }

    @JsonProperty("dcaOne")
    public void setDcaOne(String dcaOne) {
        this.dcaOne = dcaOne;
    }

    @JsonProperty("factorOne")
    public String getFactorOne() {
        return factorOne;
    }

    @JsonProperty("factorOne")
    public void setFactorOne(String factorOne) {
        this.factorOne = factorOne;
    }

    @JsonProperty("dcaTwo")
    public String getDcaTwo() {
        return dcaTwo;
    }

    @JsonProperty("dcaTwo")
    public void setDcaTwo(String dcaTwo) {
        this.dcaTwo = dcaTwo;
    }

    @JsonProperty("factorTwo")
    public String getFactorTwo() {
        return factorTwo;
    }

    @JsonProperty("factorTwo")
    public void setFactorTwo(String factorTwo) {
        this.factorTwo = factorTwo;
    }

    @JsonProperty("leverage")
    public String getLeverage() {
        return leverage;
    }

    @JsonProperty("leverage")
    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    @JsonProperty("takeprofit")
    public String getTakeprofit() {
        return takeprofit;
    }

    @JsonProperty("takeprofit")
    public void setTakeprofit(String takeprofit) {
        this.takeprofit = takeprofit;
    }

    @JsonProperty("stoploss")
    public String getStoploss() {
        return stoploss;
    }

    @JsonProperty("stoploss")
    public void setStoploss(String stoploss) {
        this.stoploss = stoploss;
    }

    @JsonProperty("auth")
    public String getAuth() {
        return auth;
    }

    @JsonProperty("auth")
    public void setAuth(String auth) {
        this.auth = auth;
    }

    @JsonProperty("delay")
    public String getDelay() {
        return delay;
    }

    @JsonProperty("delay")
    public void setDelay(String delay) {
        this.delay = delay;
    }

    @JsonProperty("discordwebhook")
    public String getDiscordwebhook() {
        return discordwebhook;
    }

    @JsonProperty("discordwebhook")
    public void setDiscordwebhook(String discordwebhook) {
        this.discordwebhook = discordwebhook;
    }

    @JsonProperty("sleeponstop")
    public String getSleeponstop() {
        return sleeponstop;
    }

    @JsonProperty("sleeponstop")
    public void setSleeponstop(String sleeponstop) {
        this.sleeponstop = sleeponstop;
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