package com.lickhunter.web.services;

import com.binance.client.model.enums.CandlestickInterval;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

public interface TechnicalIndicatorService {
    BarSeries getBarSeries(String symbol, CandlestickInterval timeframe);
    Strategy bollingerBandsStrategy(BarSeries series, Double markprice, int barCount);
    Strategy cciCorrectionStrategy(BarSeries series, int barCount);
    Strategy vwapShortStrategy(BarSeries series, int barCount, Double shortOffset, Double price);
    Strategy vwapLongStrategy(BarSeries series, int barCount, Double longOffset, Double price);
}
