package com.lickhunter.web.repositories;

import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import com.lickhunter.web.entities.tables.records.CandlestickRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.lickhunter.web.entities.tables.Candlestick.CANDLESTICK;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class CandlestickRepository {

    private final DSLContext dsl;

    public void insertOrUpdate(String symbol, Candlestick candlestick, CandlestickInterval candlestickInterval) {
        Optional<CandlestickRecord> candlestickRecord = dsl.selectFrom(CANDLESTICK)
                .where(CANDLESTICK.SYMBOL.eq(symbol))
                .and(CANDLESTICK.OPEN_TIME.eq(Optional.ofNullable(candlestick.getOpenTime()).orElse(null)))
                .and(CANDLESTICK.TIMEFRAME.eq(candlestickInterval.name()))
                .fetchOptional();

        if(candlestickRecord.isPresent()) {
            this.update(symbol, candlestick, candlestickInterval);
        } else {
            this.insert(symbol, candlestick, candlestickInterval);
        }

    }

    public void insert(String symbol, Candlestick candlestick, CandlestickInterval candlestickInterval) {
        dsl.insertInto(CANDLESTICK)
                .set(CANDLESTICK.SYMBOL, symbol)
                .set(CANDLESTICK.OPEN_TIME, Optional.ofNullable(candlestick.getOpenTime()).orElse(null))
                .set(CANDLESTICK.OPEN, Objects.nonNull(candlestick.getOpen()) ? candlestick.getOpen().doubleValue() : null)
                .set(CANDLESTICK.HIGH, Objects.nonNull(candlestick.getHigh()) ? candlestick.getHigh().doubleValue() : null)
                .set(CANDLESTICK.LOW, Objects.nonNull(candlestick.getLow()) ? candlestick.getLow().doubleValue() : null)
                .set(CANDLESTICK.CLOSE, Objects.nonNull(candlestick.getClose()) ? candlestick.getClose().doubleValue() : null)
                .set(CANDLESTICK.VOLUME, Objects.nonNull(candlestick.getVolume()) ? candlestick.getVolume().doubleValue() : null)
                .set(CANDLESTICK.QUOTE_ASSET_VOLUME, Objects.nonNull(candlestick.getQuoteAssetVolume()) ? candlestick.getQuoteAssetVolume().doubleValue() : null)
                .set(CANDLESTICK.CLOSE_TIME, Optional.ofNullable(candlestick.getCloseTime()).orElse(null))
                .set(CANDLESTICK.NUMBER_OF_TRADES, Optional.ofNullable(candlestick.getNumTrades()).orElse(null))
                .set(CANDLESTICK.TAKER_BUY_BASE_VOLUME, Objects.nonNull(candlestick.getTakerBuyBaseAssetVolume()) ? candlestick.getTakerBuyBaseAssetVolume().doubleValue() : null)
                .set(CANDLESTICK.TAKER_BUY_QUOTE_VOLUME, Objects.nonNull(candlestick.getTakerBuyQuoteAssetVolume()) ? candlestick.getTakerBuyQuoteAssetVolume().doubleValue() : null)
                .set(CANDLESTICK.TIMEFRAME, candlestickInterval.name())
                .execute();
    }

    public void update(String symbol, Candlestick candlestick, CandlestickInterval candlestickInterval) {
        dsl.update(CANDLESTICK)
                .set(CANDLESTICK.SYMBOL, symbol)
                .set(CANDLESTICK.OPEN_TIME, Optional.ofNullable(candlestick.getOpenTime()).orElse(null))
                .set(CANDLESTICK.OPEN, Objects.nonNull(candlestick.getOpen()) ? candlestick.getOpen().doubleValue() : null)
                .set(CANDLESTICK.HIGH, Objects.nonNull(candlestick.getHigh()) ? candlestick.getHigh().doubleValue() : null)
                .set(CANDLESTICK.LOW, Objects.nonNull(candlestick.getLow()) ? candlestick.getLow().doubleValue() : null)
                .set(CANDLESTICK.CLOSE, Objects.nonNull(candlestick.getClose()) ? candlestick.getClose().doubleValue() : null)
                .set(CANDLESTICK.VOLUME, Objects.nonNull(candlestick.getVolume()) ? candlestick.getVolume().doubleValue() : null)
                .set(CANDLESTICK.QUOTE_ASSET_VOLUME, Objects.nonNull(candlestick.getQuoteAssetVolume()) ? candlestick.getQuoteAssetVolume().doubleValue() : null)
                .set(CANDLESTICK.CLOSE_TIME, Optional.ofNullable(candlestick.getCloseTime()).orElse(null))
                .set(CANDLESTICK.NUMBER_OF_TRADES, Optional.ofNullable(candlestick.getNumTrades()).orElse(null))
                .set(CANDLESTICK.TAKER_BUY_BASE_VOLUME, Objects.nonNull(candlestick.getTakerBuyBaseAssetVolume()) ? candlestick.getTakerBuyBaseAssetVolume().doubleValue() : null)
                .set(CANDLESTICK.TAKER_BUY_QUOTE_VOLUME, Objects.nonNull(candlestick.getTakerBuyQuoteAssetVolume()) ? candlestick.getTakerBuyQuoteAssetVolume().doubleValue() : null)
                .set(CANDLESTICK.TIMEFRAME, candlestickInterval.name())
                .where(CANDLESTICK.TIMEFRAME.eq(candlestickInterval.name()))
                .and(CANDLESTICK.SYMBOL.eq(symbol))
                .and(CANDLESTICK.OPEN_TIME.eq(candlestick.getOpenTime()))
                .execute();
    }

    public List<CandlestickRecord> getCandleStickBySymbol(String symbol) {
        return dsl.selectFrom(CANDLESTICK)
                .where(CANDLESTICK.SYMBOL.eq(symbol))
                .fetch();
    }

    public List<CandlestickRecord> getCandleStickBySymbolAndTimeframe(String symbol, CandlestickInterval timeframe) {
        return dsl.selectFrom(CANDLESTICK)
                .where(CANDLESTICK.SYMBOL.eq(symbol))
                .and(CANDLESTICK.TIMEFRAME.eq(timeframe.name()))
                .orderBy(CANDLESTICK.OPEN_TIME.asc())
                .fetch();
    }
}
