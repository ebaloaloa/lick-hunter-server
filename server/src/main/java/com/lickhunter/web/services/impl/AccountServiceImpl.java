package com.lickhunter.web.services.impl;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.IncomeType;
import com.binance.client.model.trade.AccountInformation;
import com.binance.client.model.trade.Income;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.entities.tables.records.AccountRecord;
import com.lickhunter.web.entities.tables.records.IncomeHistoryRecord;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.repositories.IncomeHistoryRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.LickHunterService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final IncomeHistoryRepository incomeHistoryRepository;
    private final LickHunterService lickHunterService;

    @SneakyThrows
    public AccountInformation getAccountInformation() {
        Settings settings = lickHunterService.getLickHunterSettings();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        AccountInformation accountInformation = syncRequestClient.getAccountInformation();
        accountRepository.insertOrUpdate(accountInformation, settings.getKey());
        return accountInformation;
    }

    @SneakyThrows
    public Boolean isOpenOrderIsolationActive(String key, Double isolationPercentage) {
        Optional<AccountRecord> accountRecord = accountRepository.findByAccountId(key);
        isolationPercentage = Objects.nonNull(isolationPercentage) ? isolationPercentage : 0.0;
        if(accountRecord.isPresent()) {
            if(accountRecord.get().getTotalMarginBalance() == 0.0) {
                throw new Exception("Account Balance is 0. Make sure you have enough balance!");
            }
            return BigDecimal.valueOf(accountRecord.get().getTotalPositionInitialMargin())
                        .divide(BigDecimal.valueOf(accountRecord.get().getTotalMarginBalance()), 2, RoundingMode.HALF_DOWN)
                        .multiply(BigDecimal.valueOf(100))
                    .compareTo(BigDecimal.valueOf(isolationPercentage))
                    > 0;
        }
        return false;
    }

    public Boolean isMaxOpenActive(String key, Long maxOpen) {
        maxOpen = Objects.nonNull(maxOpen) ? maxOpen : 0;
        return (long) positionRepository.findActivePositionsByAccountId(key).size() >= maxOpen;
    }

    @SneakyThrows
    public List<Income> getIncomeHistory(String symbol, IncomeType incomeType, Long startTime, Long endTime, Integer limit) {
        Settings settings = lickHunterService.getLickHunterSettings();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        List<Income> incomeList = syncRequestClient.getIncomeHistory(symbol, incomeType, startTime, endTime, limit);
        incomeList.forEach(i -> {
            i.setTime(i.getTime()/1000);
            incomeHistoryRepository.insertOrUpdate(i, settings.getKey());
        });
        return incomeList;
    }

    public BigDecimal getDailyPnl() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<IncomeHistoryRecord> incomeHistoryRecords = incomeHistoryRepository.findByFromAndToDate(startOfDay, endOfDay);
        return BigDecimal.valueOf(incomeHistoryRecords.stream()
                .filter(incomeHistoryRecord -> !incomeHistoryRecord.getIncomeType().equals(IncomeType.TRANSFER.toString()))
                .map(IncomeHistoryRecord::getIncome)
                .reduce(0.0, Double::sum));
    }

    @SneakyThrows
    public void futuresTransfer(String asset, Double amount, int type) {
        Settings settings = lickHunterService.getLickHunterSettings();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setUrl("https://api.binance.com");
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret(), requestOptions);
        syncRequestClient.futuresTransfer(asset, amount, type);
    }
}
