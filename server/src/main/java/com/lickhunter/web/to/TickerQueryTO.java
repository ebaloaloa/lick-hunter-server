package com.lickhunter.web.to;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * A representation of the Ticker query object
 */
@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TickerQueryTO extends AbstractTO {

    /**
     * The symbol of the currency
     */
    private List<String> symbol;

    /**
     * The price percentage change of the symbol
     */
    private BigDecimal maxPriceChangePercent;

    /**
     * The upper limit of trading volume in USD
     */
    private BigDecimal volumeUpperLimit;

    /**
     * The lower limit of trading volume in USD
     */
    @Min(value = 0, message = "Volume Lower Limit should not be less than 0")
    private BigDecimal volumeLowerLimit;

    /**
     * Minimum trading age of symbol.
     */
    @NotNull(message = "Minimum Trading Age cannot be null")
    private int minimumTradingAge;

    /**
     * Include symbols nearing all time high
     */
    @Min(value = 0, message = "Percentage From All Time High should not be less than 0")
    private Long percentageFromAllTimeHigh;

    /**
     * Exclude symbols from query
     */
    private List<String> exclude;

    /**
     * Enables automatic exclusion of coins based on percentage change within 24H.
     */
    private Boolean autoExclude;

    /**
     * Sets the percentage change for automatic exclusion.
     */
    private BigDecimal autoExcludePercentage;

    /**
     * Enables the Bollinger Bands Strategy
     */
    private Boolean bbStrategy;

    /**
     * Length or number of bars for Bollinger Bands
     */
    private int bbBarCount;
}
