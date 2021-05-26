package com.lickhunter.web.repositories;

import com.binance.client.model.trade.Asset;
import com.binance.client.model.user.AccountUpdate;
import com.lickhunter.web.entities.tables.records.AssetRecord;
import com.lickhunter.web.entities.tables.records.PositionRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.lickhunter.web.entities.tables.Asset.ASSET;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class AssetRepository {

    private final DSLContext dsl;
    private final PositionRepository positionRepository;

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

    public void updateFromPosition(AccountUpdate accountUpdate, String accountId) {
        List<PositionRecord> positionRecords = positionRepository.findByAccountId(accountId).stream()
                .filter(p -> p.getInitialMargin().compareTo(0.0) != 0)
                .collect(Collectors.toList());
        Double margin = positionRecords.stream()
                .map(PositionRecord::getInitialMargin)
                .reduce(0.0, Double::sum);
        Double unrealizedProfit = positionRecords.stream()
                .map(PositionRecord::getUnrealizedProfit)
                .reduce(0.0, Double::sum);
        //TODO enhance logic for different assets : USDT, BNB, BUSD
        accountUpdate.getBalances().forEach(
                balanceUpdate -> dsl.update(ASSET)
                        .set(ASSET.INITIAL_MARGIN, margin)
                        .set(ASSET.POSITION_INITIAL_MARGIN, margin)
                        .set(ASSET.MAX_WITHDRAW_AMOUNT, balanceUpdate.getWalletBalance().doubleValue() - margin)
                        .set(ASSET.MARGIN_BALANCE, balanceUpdate.getWalletBalance().doubleValue())
                        .set(ASSET.UNREALIZED_PROFIT, unrealizedProfit)
                        .where(ASSET.ACCOUNT_ID.eq(accountId))
                        .and(ASSET.ASSET_.eq(balanceUpdate.getAsset()))
                        .execute()
        );
    }

    public List<AssetRecord> findByAccountId(String accountId) {
        return dsl.selectFrom(ASSET)
                .where(ASSET.ACCOUNT_ID.eq(accountId))
                .fetch();
    }
}
