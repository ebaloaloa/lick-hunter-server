package com.lickhunter.web.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * A representation of the Sentiments query object
 */
@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentsTO {

    /**
     * Api endpoint request
     */
    private String endpoint;
    /**
     * A comma-separated list of symbols to fetch data for.
     * Example: LTC
     */
    private String symbol;

    /**
     * Default: 24
     * Number of time series data points to include for the asset. Maximum of 720 data points accepted, to not use time series data set data_points=0
     * Example: 24
     */
    private Integer dataPoints;

    /**
     * A comma-separated list of change intervals to provide metrics for. 1d,1w,1m,3m,6m,1y. Output will include the sum of the selected interval (such as 3 months) the previous sum of the time period before and the percent change
     * Example: 6m,1y
     */
    private String change;

    /**
     * Provide an interval string value of either "hour" or "day". Defaults to "hour" if ommited.
     */
    private String interval;
}
