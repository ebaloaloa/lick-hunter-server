package com.lickhunter.web.repositories;

import com.binance.client.model.trade.AccountInformation;
import com.lickhunter.web.entities.public_.tables.records.AccountRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

}
