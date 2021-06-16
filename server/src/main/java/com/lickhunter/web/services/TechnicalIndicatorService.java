package com.lickhunter.web.services;

import com.binance.client.model.enums.CandlestickInterval;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

public interface TechnicalIndicatorService {
    BarSeries getBarSeries(String symbol, CandlestickInterval timeframe);
    Strategy bollingerBandsStrategy(BarSeries series, Double markprice);
}
