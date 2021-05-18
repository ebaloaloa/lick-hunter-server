package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.IncomeType;
import com.binance.client.model.trade.AccountInformation;
import com.binance.client.model.trade.Income;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.entities.public_.tables.records.AccountRecord;
import com.lickhunter.web.entities.public_.tables.records.IncomeHistoryRecord;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.repositories.IncomeHistoryRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import lombok.RequiredArgsConstructor;
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
    private final FileService fileService;
    private final IncomeHistoryRepository incomeHistoryRepository;

    public AccountInformation getAccountInformation() throws Exception {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        AccountInformation accountInformation = syncRequestClient.getAccountInformation();
        accountRepository.insertOrUpdate(accountInformation, settings.getKey());
        return accountInformation;
    }

    public Boolean isOpenOrderIsolationActive(String key, Double isolationPercentage) throws Exception {
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
        return positionRepository.findActivePositionsByAccountId(key).stream().count() >= maxOpen;
    }

    public List<Income> getIncomeHistory(String symbol, IncomeType incomeType, Long startTime, Long endTime, Integer limit) throws Exception {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
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
        return incomeHistoryRecords.stream()
                .map(IncomeHistoryRecord::getIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
