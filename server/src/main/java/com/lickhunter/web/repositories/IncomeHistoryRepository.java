package com.lickhunter.web.repositories;

import com.binance.client.model.trade.Income;
import com.lickhunter.web.entities.lickhunterdb.tables.records.IncomeHistoryRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static com.lickhunter.web.entities.lickhunterdb.tables.IncomeHistory.INCOME_HISTORY;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class IncomeHistoryRepository {

    private final DSLContext dsl;

    public void insertOrUpdate(Income income, String accountId) {
        Optional<IncomeHistoryRecord> incomeHistoryRecordOptional = dsl.selectFrom(INCOME_HISTORY)
                .where(INCOME_HISTORY.TRX_ID.eq(Long.valueOf(income.getTranId())))
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
                .set(INCOME_HISTORY.INCOME, income.getIncome())
                .set(INCOME_HISTORY.ASSET, income.getAsset())
                .set(INCOME_HISTORY.TIME, income.getTime())
                .where(INCOME_HISTORY.TRX_ID.eq(Long.valueOf(income.getTranId())))
                .and(INCOME_HISTORY.ACCOUNT_ID.eq(accountId))
                .and(INCOME_HISTORY.INCOME_TYPE.eq(income.getIncomeType()))
                .execute();
    }

    public void insert(Income income, String accountId) {
        dsl.insertInto(INCOME_HISTORY)
                .set(INCOME_HISTORY.TRX_ID, Long.valueOf(income.getTranId()))
                .set(INCOME_HISTORY.SYMBOL, income.getSymbol())
                .set(INCOME_HISTORY.INCOME_TYPE, income.getIncomeType())
                .set(INCOME_HISTORY.INCOME, income.getIncome())
                .set(INCOME_HISTORY.ASSET, income.getAsset())
                .set(INCOME_HISTORY.TIME, income.getTime())
                .set(INCOME_HISTORY.ACCOUNT_ID, accountId)
                .execute();
    }

    public List<IncomeHistoryRecord> findByFromAndToDate(LocalDateTime from, LocalDateTime to) {
        return dsl.selectFrom(INCOME_HISTORY)
                .where(INCOME_HISTORY.TIME.greaterOrEqual(from.atZone(ZoneId.systemDefault()).toEpochSecond()))
                .and(INCOME_HISTORY.TIME.lessOrEqual(to.atZone(ZoneId.systemDefault()).toEpochSecond()))
                .fetch();
    }
}
