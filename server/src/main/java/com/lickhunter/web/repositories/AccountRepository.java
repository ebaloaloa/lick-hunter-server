package com.lickhunter.web.repositories;

import com.binance.client.model.trade.AccountInformation;
import com.lickhunter.web.entities.public_.tables.records.AccountRecord;
import com.lickhunter.web.entities.public_.tables.records.AssetRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.lickhunter.web.entities.public_.tables.Account.ACCOUNT;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
@Slf4j
public class AccountRepository {

    private final DSLContext dsl;
    private final AssetRepository assetRepository;
    private final PositionRepository positionRepository;

    public void insertOrUpdate(AccountInformation input, String key) {
        Optional<AccountRecord> record = dsl.selectFrom(ACCOUNT).fetchOptional();
        if(record.isPresent()) {
            this.update(input, key);
            input.getAssets()
                    .forEach(a -> assetRepository.update(a, key));
            input.getPositions()
                    .forEach(p -> positionRepository.update(p, key));
        } else {
            this.insert(input, key);
            input.getAssets()
                    .forEach(a -> assetRepository.insert(a, key));
            input.getPositions()
                    .forEach(p -> positionRepository.insert(p, key));
        }
    }

    public void insert(AccountInformation input, String key) {
        dsl.insertInto(ACCOUNT)
                .set(ACCOUNT.ID, key)
                .set(ACCOUNT.CAN_TRADE, input.getCanTrade())
                .set(ACCOUNT.CAN_WITHDRAW, input.getCanWithdraw())
                .set(ACCOUNT.FEE_TIER, input.getFeeTier().doubleValue())
                .set(ACCOUNT.MAX_WITHDRAW_AMOUNT, input.getFeeTier().doubleValue())
                .set(ACCOUNT.TOTAL_INITIAL_MARGIN, input.getTotalInitialMargin().doubleValue())
                .set(ACCOUNT.TOTAL_MAINTENANCE_MARGIN, input.getTotalMaintMargin().doubleValue())
                .set(ACCOUNT.TOTAL_MARGIN_BALANCE, input.getTotalMarginBalance().doubleValue())
                .set(ACCOUNT.TOTAL_OPEN_ORDER_INITIAL_MARGIN, input.getTotalOpenOrderInitialMargin().doubleValue())
                .set(ACCOUNT.TOTAL_POSITION_INITIAL_MARGIN, input.getTotalPositionInitialMargin().doubleValue())
                .set(ACCOUNT.TOTAL_UNREALIZED_PROFIT, input.getTotalUnrealizedProfit().doubleValue())
                .set(ACCOUNT.TOTAL_WALLET_BALANCE, input.getTotalWalletBalance().doubleValue())
                .set(ACCOUNT.UPDATE_TIME, input.getUpdateTime())
                .execute();
    }

    public void update(AccountInformation input, String key) {
        dsl.update(ACCOUNT)
                .set(ACCOUNT.CAN_TRADE, input.getCanTrade())
                .set(ACCOUNT.CAN_WITHDRAW, input.getCanWithdraw())
                .set(ACCOUNT.FEE_TIER, input.getFeeTier().doubleValue())
                .set(ACCOUNT.MAX_WITHDRAW_AMOUNT, input.getFeeTier().doubleValue())
                .set(ACCOUNT.TOTAL_INITIAL_MARGIN, input.getTotalInitialMargin().doubleValue())
                .set(ACCOUNT.TOTAL_MAINTENANCE_MARGIN, input.getTotalMaintMargin().doubleValue())
                .set(ACCOUNT.TOTAL_MARGIN_BALANCE, input.getTotalMarginBalance().doubleValue())
                .set(ACCOUNT.TOTAL_OPEN_ORDER_INITIAL_MARGIN, input.getTotalOpenOrderInitialMargin().doubleValue())
                .set(ACCOUNT.TOTAL_POSITION_INITIAL_MARGIN, input.getTotalPositionInitialMargin().doubleValue())
                .set(ACCOUNT.TOTAL_UNREALIZED_PROFIT, input.getTotalUnrealizedProfit().doubleValue())
                .set(ACCOUNT.TOTAL_WALLET_BALANCE, input.getTotalWalletBalance().doubleValue())
                .set(ACCOUNT.UPDATE_TIME, input.getUpdateTime())
                .where(ACCOUNT.ID.eq(key))
                .execute();
    }

    public void updateFromAsset(String accountId) {
        List<AssetRecord> assetRecords = assetRepository.findByAccountId(accountId);
        Double totalMargin = assetRecords.stream()
                .map(AssetRecord::getInitialMargin)
                .reduce(0.0, Double::sum);

        Double unrealizedProfit = assetRecords.stream()
                .map(AssetRecord::getUnrealizedProfit)
                .reduce(0.0, Double::sum);

        Double totalMarginBalance = assetRecords.stream()
                .map(AssetRecord::getMarginBalance)
                .reduce(0.0, Double::sum);

        dsl.update(ACCOUNT)
                .set(ACCOUNT.TOTAL_MARGIN_BALANCE, totalMarginBalance)
                .set(ACCOUNT.TOTAL_UNREALIZED_PROFIT, unrealizedProfit)
                .set(ACCOUNT.TOTAL_POSITION_INITIAL_MARGIN, totalMargin)
                .set(ACCOUNT.TOTAL_INITIAL_MARGIN, totalMargin)
                .set(ACCOUNT.TOTAL_WALLET_BALANCE, assetRepository.findByAccountId(accountId).stream()
                        .map(AssetRecord::getMarginBalance)
                        .reduce(0.0, Double::sum))
                .set(ACCOUNT.MAX_WITHDRAW_AMOUNT, totalMarginBalance - totalMargin)
                .where(ACCOUNT.ID.eq(accountId))
                .execute();
    }

    public Optional<AccountRecord> findByAccountId(String accountId) {
        return dsl.selectFrom(ACCOUNT)
                .where(ACCOUNT.ID.eq(accountId))
                .fetchOptional();
    }
}
