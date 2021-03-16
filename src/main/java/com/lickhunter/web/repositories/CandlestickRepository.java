package com.lickhunter.web.repositories;

import com.binance.client.model.market.Candlestick;
import com.lickhunter.web.entities.public_.tables.records.CandlestickRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lickhunter.web.entities.public_.tables.Candlestick.CANDLESTICK;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class CandlestickRepository {

    private final DSLContext dsl;

    public void insert(String symbol, Candlestick candlestick) {
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
                .execute();
    }

    public List<CandlestickRecord> getCandleStickBySymbol(String symbol) {
        return dsl.selectFrom(CANDLESTICK)
                .where(CANDLESTICK.SYMBOL.eq(symbol))
                .fetch();
    }
}
