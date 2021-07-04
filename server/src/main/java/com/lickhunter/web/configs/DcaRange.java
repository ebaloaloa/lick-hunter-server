package com.lickhunter.web.configs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "percentFromAverage",
        "numberOfBuys"
})
public class DcaRange {
    @JsonProperty("percentFromAverage")
    private BigDecimal percentFromAverage;
    @JsonProperty("numberOfBuys")
    private String numberOfBuys;
}
