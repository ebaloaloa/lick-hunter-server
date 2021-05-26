package com.lickhunter.web.repositories;

import com.binance.client.model.market.MarkPrice;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
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

    public void insert(MarkPrice markPrice) {
        dsl.insertInto(SYMBOL)
                .set(SYMBOL.SYMBOL_, markPrice.getSymbol())
                .set(SYMBOL.MARK_PRICE, markPrice.getMarkPrice().doubleValue())
                .set(SYMBOL.LAST_FUNDING_RATE, markPrice.getLastFundingRate().doubleValue())
                .set(SYMBOL.NEXT_FUNDING_TIME, markPrice.getNextFundingTime().doubleValue())
                .set(SYMBOL.TIME, markPrice.getTime())
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

    public List<SymbolRecord> findAll() {
        return dsl.selectFrom(SYMBOL).fetch();
    }

    public Optional<SymbolRecord> findBySymbol(String symbol) {
        return dsl.selectFrom(SYMBOL)
                .where(SYMBOL.SYMBOL_.eq(symbol))
                .fetchOptional();
    }
}
