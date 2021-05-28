package com.lickhunter.web.repositories;

import com.lickhunter.web.entities.tables.records.CoinsRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.lickhunter.web.entities.tables.Coins.COINS;
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
@Slf4j
public class CoinsRepository {
    private final DSLContext dsl;

    public List<CoinsRecord> findAll() {
        return dsl.selectFrom(COINS)
                .fetch();
    }

    public void insertOrUpdate(String symbol, Double lickValue) {
        Optional<CoinsRecord> coinsRecord = this.findBySymbol(symbol);
        if(coinsRecord.isPresent()) {
            this.update(symbol, lickValue);
        } else {
            this.insert(symbol, lickValue);
        }
    }

    public Optional<CoinsRecord> findBySymbol(String symbol) {
        return dsl.selectFrom(COINS)
                .where(COINS.SYMBOL.eq(symbol))
                .fetchOptional();
    }

    public void update(String symbol, Double lickValue) {
        dsl.update(COINS)
                .set(COINS.LICK_VALUE, lickValue)
                .where(COINS.SYMBOL.eq(symbol))
                .execute();
    }
    public void insert(String symbol, Double lickValue) {
        dsl.insertInto(COINS)
            .set(COINS.SYMBOL, symbol)
            .set(COINS.LICK_VALUE, lickValue)
            .execute();
    }
}
