package com.lickhunter.web.repositories;

import com.binance.client.model.trade.Position;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.lickhunter.web.entities.public_.tables.Position.POSITION;

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
}
