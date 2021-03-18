package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.entities.public_.tables.records.AccountRecord;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final FileService fileService;

    public void getAccountInformation() throws Exception {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        accountRepository.insertOrUpdate(syncRequestClient.getAccountInformation(), settings.getKey());
    }

    public Boolean isOpenOrderIsolationActive(String key, Double isolationPercentage) {
        Optional<AccountRecord> accountRecord = accountRepository.findByAccountId(key);
        isolationPercentage = Objects.nonNull(isolationPercentage) ? isolationPercentage : 0.0;
        if(accountRecord.isPresent()) {
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
}
