package com.lickhunter.web.repositories;

import com.binance.client.model.market.ExchangeInformation;
import com.binance.client.model.market.MarkPrice;
import com.binance.client.model.market.PriceChangeTicker;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
import com.lickhunter.web.models.market.Symbol;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.lickhunter.web.entities.tables.Symbol.SYMBOL;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class SymbolRepository {

    private final DSLContext dsl;

    public void insertOrUpdate(MarkPrice markPrice) {
        Optional<SymbolRecord> symbolRecord = this.findBySymbol(markPrice.getSymbol());
        if(symbolRecord.isPresent()) {
            this.update(markPrice);
        } else {
            this.insert(markPrice);
        }
    }

    public void insertOrUpdate(Symbol symbol) {
        Optional<SymbolRecord> symbolRecord = findBySymbol(symbol.getSymbol());
        if(symbolRecord.isPresent()) {
            this.update(symbol);
        } else {
            this.insert(symbol);
        }
    }

    public void insertOrUpdate(PriceChangeTicker priceChangeTicker) {
        Optional<SymbolRecord> symbolRecord = findBySymbol(priceChangeTicker.getSymbol());
        if(symbolRecord.isPresent()) {
            this.update(priceChangeTicker);
        } else {
            this.insert(priceChangeTicker);
        }
    }

    public void insert(MarkPrice markPrice) {
        dsl.insertInto(SYMBOL)
                .set(SYMBOL.SYMBOL_, markPrice.getSymbol())
                .set(SYMBOL.MARK_PRICE, markPrice.getMarkPrice().doubleValue())
                .set(SYMBOL.LAST_FUNDING_RATE, markPrice.getLastFundingRate().doubleValue())
                .set(SYMBOL.NEXT_FUNDING_TIME, markPrice.getNextFundingTime().doubleValue())
                .set(SYMBOL.TIME, markPrice.getTime())
                .execute();
    }

    public void insert(Symbol symbol) {
        dsl.insertInto(SYMBOL)
                .set(SYMBOL.SYMBOL_, symbol.getSymbol())
                .set(SYMBOL.ONBOARD_DATE, symbol.getOnboardDate().longValue())
                .execute();
    }

    public void insert(PriceChangeTicker priceChangeTicker) {
        dsl.insertInto(SYMBOL)
                .set(SYMBOL.SYMBOL_, priceChangeTicker.getSymbol())
                .set(SYMBOL.PRICE_CHANGE_PERCENT, priceChangeTicker.getPriceChangePercent().doubleValue())
                .set(SYMBOL.QUOTE_VOLUME, priceChangeTicker.getQuoteVolume().doubleValue())
                .execute();
    }

    public void update(MarkPrice markPrice) {
        dsl.update(SYMBOL)
                .set(SYMBOL.MARK_PRICE, markPrice.getMarkPrice().doubleValue())
                .set(SYMBOL.LAST_FUNDING_RATE, markPrice.getLastFundingRate().doubleValue())
                .set(SYMBOL.NEXT_FUNDING_TIME, markPrice.getNextFundingTime().doubleValue())
                .set(SYMBOL.TIME, markPrice.getTime())
                .where(SYMBOL.SYMBOL_.eq(markPrice.getSymbol()))
                .execute();
    }

    public void update(Symbol symbol) {
        dsl.update(SYMBOL)
                .set(SYMBOL.ONBOARD_DATE, symbol.getOnboardDate().longValue())
                .where(SYMBOL.SYMBOL_.eq(symbol.getSymbol()))
                .execute();
    }

    public void update(PriceChangeTicker priceChangeTicker) {
        dsl.update(SYMBOL)
                .set(SYMBOL.PRICE_CHANGE_PERCENT, priceChangeTicker.getPriceChangePercent().doubleValue())
                .set(SYMBOL.QUOTE_VOLUME, priceChangeTicker.getQuoteVolume().doubleValue())
                .where(SYMBOL.SYMBOL_.eq(priceChangeTicker.getSymbol()))
                .execute();
    }

    public List<SymbolRecord> findAll() {
        return dsl.selectFrom(SYMBOL).fetch();
    }

    public Optional<SymbolRecord> findBySymbol(String symbol) {
        return dsl.selectFrom(SYMBOL)
                .where(SYMBOL.SYMBOL_.eq(symbol))
                .fetchOptional();
    }

    public List<SymbolRecord> findBySymbols(List<String> symbol) {
        return dsl.selectFrom(SYMBOL)
                .where(SYMBOL.SYMBOL_.in(symbol))
                .fetch();
    }
}
