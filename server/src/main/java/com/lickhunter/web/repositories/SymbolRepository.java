package com.lickhunter.web.repositories;

import com.binance.client.model.market.MarkPrice;
import com.lickhunter.web.entities.lickhunterdb.tables.records.SymbolRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lickhunter.web.entities.lickhunterdb.tables.Symbol.SYMBOL;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class SymbolRepository {

    private final DSLContext dsl;

    public void insert(MarkPrice markPrice) {
        dsl.insertInto(SYMBOL)
                .set(SYMBOL.SYMBOL_, markPrice.getSymbol())
                .onDuplicateKeyUpdate()
                .set(SYMBOL.MARK_PRICE, markPrice.getMarkPrice().doubleValue())
                .set(SYMBOL.LAST_FUNDING_RATE, markPrice.getLastFundingRate().doubleValue())
                .set(SYMBOL.NEXT_FUNDING_TIME, markPrice.getNextFundingTime().doubleValue())
                .set(SYMBOL.TIME, markPrice.getTime())
                .execute();
    }

    public List<SymbolRecord> findAll() {
        return dsl.selectFrom(SYMBOL).fetch();
    }
}
