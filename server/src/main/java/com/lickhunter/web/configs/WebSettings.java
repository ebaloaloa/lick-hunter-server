package com.lickhunter.web.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSettings {

    @JsonProperty("vwapTimeframe")
    private String vwapTimeframe;
    @JsonProperty("vwapLength")
    private Integer vwapLength;
    @JsonProperty("active")
    private String active;
    @JsonProperty("defaultSettings")
    private String defaultSettings;
    @JsonProperty("dailyReinvestment")
    private BigDecimal dailyReinvestment;
    @JsonProperty("safe")
    private String safe;
    @JsonProperty("user-agent")
    private String userAgent;
    @JsonProperty("userDefinedSettings")
    private Map<String, UserDefinedSettings> userDefinedSettings = new HashMap<>();

}