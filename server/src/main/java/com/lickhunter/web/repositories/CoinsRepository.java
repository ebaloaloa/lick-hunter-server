package com.lickhunter.web.repositories;

import com.lickhunter.web.entities.public_.tables.records.CoinsRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.lickhunter.web.entities.public_.tables.Coins.COINS;
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

    public Optional<CoinsRecord> findBySymbol(String symbol) {
        return dsl.selectFrom(COINS)
                .where(COINS.SYMBOL.eq(symbol))
                .fetchOptional();
    }
}
