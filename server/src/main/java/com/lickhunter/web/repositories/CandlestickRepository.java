package com.lickhunter.web.repositories;

import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import com.lickhunter.web.entities.tables.records.CandlestickRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .and(CANDLESTICK.OPEN_TIME.eq(candlestick.getOpenTime()))
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
                .set(CANDLESTICK.OPEN_TIME, candlestick.getOpenTime())
                .set(CANDLESTICK.OPEN, candlestick.getOpen().doubleValue())
                .set(CANDLESTICK.HIGH, candlestick.getHigh().doubleValue())
                .set(CANDLESTICK.LOW, candlestick.getLow().doubleValue())
                .set(CANDLESTICK.CLOSE, candlestick.getClose().doubleValue())
                .set(CANDLESTICK.VOLUME, candlestick.getVolume().doubleValue())
                .set(CANDLESTICK.QUOTE_ASSET_VOLUME, candlestick.getQuoteAssetVolume().doubleValue())
                .set(CANDLESTICK.CLOSE_TIME, candlestick.getCloseTime())
                .set(CANDLESTICK.NUMBER_OF_TRADES, candlestick.getNumTrades())
                .set(CANDLESTICK.TAKER_BUY_BASE_VOLUME, candlestick.getTakerBuyBaseAssetVolume().doubleValue())
                .set(CANDLESTICK.TAKER_BUY_QUOTE_VOLUME, candlestick.getTakerBuyQuoteAssetVolume().doubleValue())
                .set(CANDLESTICK.TIMEFRAME, candlestickInterval.name())
                .execute();
    }

    public void update(String symbol, Candlestick candlestick, CandlestickInterval candlestickInterval) {
        dsl.update(CANDLESTICK)
                .set(CANDLESTICK.SYMBOL, symbol)
                .set(CANDLESTICK.OPEN_TIME, candlestick.getOpenTime())
                .set(CANDLESTICK.OPEN, candlestick.getOpen().doubleValue())
                .set(CANDLESTICK.HIGH, candlestick.getHigh().doubleValue())
                .set(CANDLESTICK.LOW, candlestick.getLow().doubleValue())
                .set(CANDLESTICK.CLOSE, candlestick.getClose().doubleValue())
                .set(CANDLESTICK.VOLUME, candlestick.getVolume().doubleValue())
                .set(CANDLESTICK.QUOTE_ASSET_VOLUME, candlestick.getQuoteAssetVolume().doubleValue())
                .set(CANDLESTICK.CLOSE_TIME, candlestick.getCloseTime())
                .set(CANDLESTICK.NUMBER_OF_TRADES, candlestick.getNumTrades())
                .set(CANDLESTICK.TAKER_BUY_BASE_VOLUME, candlestick.getTakerBuyBaseAssetVolume().doubleValue())
                .set(CANDLESTICK.TAKER_BUY_QUOTE_VOLUME, candlestick.getTakerBuyQuoteAssetVolume().doubleValue())
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
