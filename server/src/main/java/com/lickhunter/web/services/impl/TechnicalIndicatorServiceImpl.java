package com.lickhunter.web.services.impl;

import com.binance.client.model.enums.CandlestickInterval;
import com.lickhunter.web.entities.tables.records.CandlestickRecord;
import com.lickhunter.web.repositories.CandlestickRepository;
import com.lickhunter.web.services.TechnicalIndicatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ta4j.core.*;
import org.ta4j.core.indicators.CCIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TechnicalIndicatorServiceImpl implements TechnicalIndicatorService {

    private final CandlestickRepository candlestickRepository;

    public BarSeries getBarSeries(String symbol, CandlestickInterval timeframe) {
        BarSeries series = new BaseBarSeries(symbol);
        List<CandlestickRecord> candlestickRecordList = candlestickRepository.getCandleStickBySymbolAndTimeframe(symbol, timeframe);
        candlestickRecordList.forEach(candlestickRecord -> {
            Instant.ofEpochSecond(candlestickRecord.getCloseTime());
            ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochSecond(candlestickRecord.getCloseTime()), ZoneId.systemDefault());
            double open = candlestickRecord.getOpen();
            double high = candlestickRecord.getHigh();
            double low = candlestickRecord.getLow();
            double close = candlestickRecord.getClose();
            double volume = candlestickRecord.getVolume();
            series.addBar(date, open, high, low, close, volume);
        });
        return series;
    }

    public Strategy bollingerBandsStrategy(BarSeries series, Double markPrice, int barCount) {
        if (Objects.isNull(series)) {
            throw new IllegalArgumentException("Series cannot be null");
        }
        // Close price
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, barCount);
        StandardDeviationIndicator sd = new StandardDeviationIndicator(closePrice, barCount);

        // Bollinger bands
        BollingerBandsMiddleIndicator middleBBand = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsLowerIndicator lowBBand = new BollingerBandsLowerIndicator(middleBBand, sd);
        BollingerBandsUpperIndicator upBBand = new BollingerBandsUpperIndicator(middleBBand, sd);

        Rule entryRule = new OverIndicatorRule(lowBBand, markPrice) //long
                .or(new UnderIndicatorRule(upBBand, markPrice)); //short

        Rule exitRule = new UnderIndicatorRule(lowBBand, markPrice)
                .or(new OverIndicatorRule(upBBand, markPrice));

        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        return strategy;
    }

    @Override
    public Strategy cciCorrectionStrategy(BarSeries series, int barCount) {
        CCIIndicator cci = new CCIIndicator(series, barCount);
        Num plus100 = series.numOf(100);
        Num minus100 = series.numOf(-100);
        Rule entryRule = new OverIndicatorRule(cci, plus100)
                .or(new UnderIndicatorRule(cci, minus100));

        Rule exitRule = new UnderIndicatorRule(cci, plus100)
                .and(new OverIndicatorRule(cci, minus100));

        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        strategy.setUnstablePeriod(5);
        return strategy;
    }

    @Override
    public Strategy vwapShortStrategy(BarSeries series, int barCount, Double shortOffset, Double price) {
        ShortOffsetVWAPIndicator vwapIndicator = new ShortOffsetVWAPIndicator(series, barCount, shortOffset);
        Rule entryRule = new UnderIndicatorRule(vwapIndicator, series.numOf(price));
        Rule exitRule = new OverIndicatorRule(vwapIndicator, series.numOf(price));
        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        log.debug(String.format("[SHORT VWAP] symbol: %s, value: %s", series.getName(), vwapIndicator.getValue(series.getEndIndex())));
        return strategy;
    }

    @Override
    public Strategy vwapLongStrategy(BarSeries series, int barCount, Double longOffset, Double price) {
        LongOffsetVWAPIndicator vwapIndicator = new LongOffsetVWAPIndicator(series, barCount, longOffset);
        Rule entryRule = new OverIndicatorRule(vwapIndicator, series.numOf(price));
        Rule exitRule = new UnderIndicatorRule(vwapIndicator, series.numOf(price));
        Strategy strategy = new BaseStrategy(entryRule, exitRule);
        log.debug(String.format("[LONG VWAP] symbol: %s, value: %s", series.getName(), vwapIndicator.getValue(series.getEndIndex())));
        return strategy;
    }
}
