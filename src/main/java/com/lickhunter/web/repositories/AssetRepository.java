package com.lickhunter.web.repositories;

import com.binance.client.model.trade.Asset;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.lickhunter.web.entities.public_.tables.Asset.ASSET;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class AssetRepository {

    private final DSLContext dsl;

    public void insert(Asset asset, String accountId) {
        dsl.insertInto(ASSET)
                .set(ASSET.ASSET_, asset.getAsset())
                .set(ASSET.ACCOUNT_ID, accountId)
                .set(ASSET.INITIAL_MARGIN, asset.getInitialMargin().doubleValue())
                .set(ASSET.MAINT_MARGIN, asset.getMaintMargin().doubleValue())
                .set(ASSET.MARGIN_BALANCE, asset.getMarginBalance().doubleValue())
                .set(ASSET.MAX_WITHDRAW_AMOUNT, asset.getMaxWithdrawAmount().doubleValue())
                .set(ASSET.OPEN_ORDER_INITIAL_MARGIN, asset.getOpenOrderInitialMargin().doubleValue())
                .set(ASSET.POSITION_INITIAL_MARGIN, asset.getPositionInitialMargin().doubleValue())
                .set(ASSET.UNREALIZED_PROFIT, asset.getUnrealizedProfit().doubleValue())
                .execute();
    }

    public void update(Asset asset, String accountId) {
        dsl.update(ASSET)
                .set(ASSET.ASSET_, asset.getAsset())
                .set(ASSET.ACCOUNT_ID, accountId)
                .set(ASSET.INITIAL_MARGIN, asset.getInitialMargin().doubleValue())
                .set(ASSET.MAINT_MARGIN, asset.getMaintMargin().doubleValue())
                .set(ASSET.MARGIN_BALANCE, asset.getMarginBalance().doubleValue())
                .set(ASSET.MAX_WITHDRAW_AMOUNT, asset.getMaxWithdrawAmount().doubleValue())
                .set(ASSET.OPEN_ORDER_INITIAL_MARGIN, asset.getOpenOrderInitialMargin().doubleValue())
                .set(ASSET.POSITION_INITIAL_MARGIN, asset.getPositionInitialMargin().doubleValue())
                .set(ASSET.UNREALIZED_PROFIT, asset.getUnrealizedProfit().doubleValue())
                .where(ASSET.ACCOUNT_ID.eq(accountId))
                .and(ASSET.ASSET_.eq(asset.getAsset()))
                .execute();
    }
}
