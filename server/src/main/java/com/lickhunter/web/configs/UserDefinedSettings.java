package com.lickhunter.web.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDefinedSettings {
    @JsonProperty("maxOpen")
    private Integer maxOpen;
    @JsonProperty("openOrderIsolationPercentage")
    private Double openOrderIsolationPercentage;
    @JsonProperty("longOffset")
    private Double longOffset;
    @JsonProperty("shortOffset")
    private Double shortOffset;
    @JsonProperty("lickValue")
    private Integer lickValue;
    @JsonProperty("marginPercentNotification")
    private Integer marginPercentNotification;
    @JsonProperty("marginType")
    private String marginType;
    @JsonProperty("leverage")
    private Integer leverage;
    @JsonProperty("autoLickValue")
    private Boolean autoLickValue;
}
