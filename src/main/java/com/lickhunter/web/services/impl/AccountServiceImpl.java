package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.trade.AccountInformation;
import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ApplicationConfig config;
    private final AccountRepository accountRepository;

    public void getAccountInformation() {
        SyncRequestClient syncRequestClient = SyncRequestClient.create(config.getKey(), config.getSecret());
        accountRepository.insertOrUpdate(syncRequestClient.getAccountInformation(), config.getKey());
    }

    private Boolean isOpenOrderIsolationActive(AccountInformation accountInformation, BigDecimal isolationPercentage) {
        return accountInformation.getTotalPositionInitialMargin()
                .divide(accountInformation.getTotalMarginBalance(), 2, RoundingMode.HALF_DOWN)
                .multiply(BigDecimal.valueOf(100)).compareTo(isolationPercentage) > 0;
    }
}
