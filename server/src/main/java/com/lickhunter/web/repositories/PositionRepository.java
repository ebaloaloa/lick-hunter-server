package com.lickhunter.web.repositories;

import com.binance.client.model.trade.Position;
import com.binance.client.model.user.PositionUpdate;
import com.lickhunter.web.entities.tables.records.PositionRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static com.lickhunter.web.entities.tables.Position.POSITION;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class PositionRepository {

    private final DSLContext dsl;

    public void insert(Position position, String accountId) {
        dsl.insertInto(POSITION)
                .set(POSITION.SYMBOL, position.getSymbol())
                .set(POSITION.ACCOUNT_ID, accountId)
                .set(POSITION.ISOLATED, position.getIsolated())
                .set(POSITION.LEVERAGE, position.getLeverage().doubleValue())
                .set(POSITION.INITIAL_MARGIN, position.getInitialMargin().doubleValue())
                .set(POSITION.MAINT_MARGIN, position.getMaintMargin().doubleValue())
                .set(POSITION.OPEN_ORDER_INITIAL_MARGIN, position.getOpenOrderInitialMargin().doubleValue())
                .set(POSITION.POSITION_INITIAL_MARGIN, position.getPositionInitialMargin().doubleValue())
                .set(POSITION.UNREALIZED_PROFIT, position.getUnrealizedProfit().doubleValue())
                .set(POSITION.ENTRY_PRICE, position.getEntryPrice())
                .set(POSITION.MAX_NOTIONAL, position.getMaxNotional())
                .set(POSITION.POSITION_SIDE, position.getPositionSide())
                .execute();
    }

    public void update(Position position, String accountId) {
        dsl.update(POSITION)
                .set(POSITION.SYMBOL, position.getSymbol())
                .set(POSITION.ACCOUNT_ID, accountId)
                .set(POSITION.ISOLATED, position.getIsolated())
                .set(POSITION.LEVERAGE, position.getLeverage().doubleValue())
                .set(POSITION.INITIAL_MARGIN, position.getInitialMargin().doubleValue())
                .set(POSITION.MAINT_MARGIN, position.getMaintMargin().doubleValue())
                .set(POSITION.OPEN_ORDER_INITIAL_MARGIN, position.getOpenOrderInitialMargin().doubleValue())
                .set(POSITION.POSITION_INITIAL_MARGIN, position.getPositionInitialMargin().doubleValue())
                .set(POSITION.UNREALIZED_PROFIT, position.getUnrealizedProfit().doubleValue())
                .set(POSITION.ENTRY_PRICE, position.getEntryPrice())
                .set(POSITION.MAX_NOTIONAL, position.getMaxNotional())
                .set(POSITION.POSITION_SIDE, position.getPositionSide())
                .where(POSITION.ACCOUNT_ID.eq(accountId))
                .and(POSITION.SYMBOL.eq(position.getSymbol()))
                .execute();
    }

    public void insert(List<PositionUpdate> positionUpdates, String accountId) {
        positionUpdates.forEach(positionUpdate -> dsl.insertInto(POSITION)
                .set(POSITION.SYMBOL, positionUpdate.getSymbol())
                .set(POSITION.ACCOUNT_ID, accountId)
                .set(POSITION.POSITION_INITIAL_MARGIN, positionUpdate.getAmount().doubleValue())
                .set(POSITION.UNREALIZED_PROFIT, positionUpdate.getUnrealizedPnl().doubleValue())
                .set(POSITION.ENTRY_PRICE, positionUpdate.getEntryPrice().toString())
                .execute());
    }

    public void insertOrUpdate(List<PositionUpdate> positionUpdates, String accountId) {
        positionUpdates.stream()
            .forEach(
                positionUpdate -> {
                    Optional<PositionRecord> positionRecord = this.findBySymbolAndPositionSideAndAccountId(positionUpdate.getSymbol(), positionUpdate.getPositionSide(), accountId);
                    if(positionRecord.isPresent()) {
                        dsl.update(POSITION)
                                .set(POSITION.INITIAL_MARGIN, positionUpdate.getAmount().abs()
                                        .multiply(positionUpdate.getEntryPrice())
                                        .divide(BigDecimal.valueOf(positionRecord.get().getLeverage()), RoundingMode.DOWN)
                                        .doubleValue())
                                .set(POSITION.POSITION_INITIAL_MARGIN, positionUpdate.getAmount().abs()
                                        .multiply(positionUpdate.getEntryPrice())
                                        .divide(BigDecimal.valueOf(positionRecord.get().getLeverage()), RoundingMode.DOWN)
                                        .doubleValue())
                                .set(POSITION.UNREALIZED_PROFIT, positionUpdate.getUnrealizedPnl().doubleValue())
                                .set(POSITION.ENTRY_PRICE, positionUpdate.getEntryPrice().toString())
                                .where(POSITION.ACCOUNT_ID.eq(accountId))
                                .and(POSITION.SYMBOL.eq(positionUpdate.getSymbol()))
                                .execute();
                    } else {

                    }
                    positionRecord.ifPresent(record -> dsl.update(POSITION)
                            .set(POSITION.INITIAL_MARGIN, positionUpdate.getAmount().abs()
                                    .multiply(positionUpdate.getEntryPrice())
                                    .divide(BigDecimal.valueOf(record.getLeverage()), RoundingMode.DOWN)
                                    .doubleValue())
                            .set(POSITION.POSITION_INITIAL_MARGIN, positionUpdate.getAmount().abs()
                                    .multiply(positionUpdate.getEntryPrice())
                                    .divide(BigDecimal.valueOf(record.getLeverage()), RoundingMode.DOWN)
                                    .doubleValue())
                            .set(POSITION.UNREALIZED_PROFIT, positionUpdate.getUnrealizedPnl().doubleValue())
                            .set(POSITION.ENTRY_PRICE, positionUpdate.getEntryPrice().toString())
                            .where(POSITION.ACCOUNT_ID.eq(accountId))
                            .and(POSITION.SYMBOL.eq(positionUpdate.getSymbol()))
                            .execute());
                }
        );
    }

    public Optional<PositionRecord> findBySymbolAndPositionSideAndAccountId(String symbol, String positionSide, String accountId) {
        return dsl.selectFrom(POSITION)
                .where(POSITION.ACCOUNT_ID.eq(accountId))
                .and(POSITION.SYMBOL.eq(symbol))
                .and(POSITION.POSITION_SIDE.eq(positionSide))
                .fetchOptional();
    }

    public List<PositionRecord> findByAccountId(String accountId) {
        return dsl.selectFrom(POSITION)
                .where(POSITION.ACCOUNT_ID.eq(accountId))
                .fetch();
    }

    public List<PositionRecord> findActivePositionsByAccountId(String accountId) {
        return dsl.selectFrom(POSITION)
                .where(POSITION.ACCOUNT_ID.eq(accountId))
                .and(POSITION.INITIAL_MARGIN.notEqual(0.0))
                .fetch();
    }
}
