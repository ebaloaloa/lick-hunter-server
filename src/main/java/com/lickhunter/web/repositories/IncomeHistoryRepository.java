package com.lickhunter.web.repositories;

import com.binance.client.model.trade.Income;
import com.lickhunter.web.entities.public_.tables.records.IncomeHistoryRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.lickhunter.web.entities.public_.tables.IncomeHistory.INCOME_HISTORY;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class IncomeHistoryRepository {

    private final DSLContext dsl;

    public void insertOrUpdate(Income income, String accountId) {
        Optional<IncomeHistoryRecord> incomeHistoryRecordOptional = dsl.selectFrom(INCOME_HISTORY)
                .where(INCOME_HISTORY.TRX_ID.eq(Double.valueOf(income.getTranId())))
                .and(INCOME_HISTORY.ACCOUNT_ID.eq(accountId))
                .and(INCOME_HISTORY.INCOME_TYPE.eq(income.getIncomeType()))
        .fetchOptional();

        if(incomeHistoryRecordOptional.isPresent()) {
            this.update(income, accountId);
        } else {
            this.insert(income, accountId);
        }
    }

    public void update(Income income, String accountId) {
        dsl.update(INCOME_HISTORY)
                .set(INCOME_HISTORY.SYMBOL, income.getSymbol())
                .set(INCOME_HISTORY.INCOME_TYPE, income.getIncomeType())
                .set(INCOME_HISTORY.INCOME, income.getIncome().doubleValue())
                .set(INCOME_HISTORY.ASSET, income.getAsset())
                .set(INCOME_HISTORY.TIME, income.getTime().doubleValue())
                .where(INCOME_HISTORY.TRX_ID.eq(Double.valueOf(income.getTranId())))
                .and(INCOME_HISTORY.ACCOUNT_ID.eq(accountId))
                .and(INCOME_HISTORY.INCOME_TYPE.eq(income.getIncomeType()))
                .execute();
    }

    public void insert(Income income, String accountId) {
        dsl.insertInto(INCOME_HISTORY)
                .set(INCOME_HISTORY.TRX_ID, Double.valueOf(income.getTranId()))
                .set(INCOME_HISTORY.SYMBOL, income.getSymbol())
                .set(INCOME_HISTORY.INCOME_TYPE, income.getIncomeType())
                .set(INCOME_HISTORY.INCOME, income.getIncome().doubleValue())
                .set(INCOME_HISTORY.ASSET, income.getAsset())
                .set(INCOME_HISTORY.TIME, income.getTime().doubleValue())
                .set(INCOME_HISTORY.ACCOUNT_ID, accountId)
                .execute();
    }
}
