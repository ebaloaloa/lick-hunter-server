package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.trade.AccountInformation;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final FileService fileService;

    public void getAccountInformation() throws Exception {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        accountRepository.insertOrUpdate(syncRequestClient.getAccountInformation(), settings.getKey());
    }

    private Boolean isOpenOrderIsolationActive(AccountInformation accountInformation, BigDecimal isolationPercentage) {
        return accountInformation.getTotalPositionInitialMargin()
                .divide(accountInformation.getTotalMarginBalance(), 2, RoundingMode.HALF_DOWN)
                .multiply(BigDecimal.valueOf(100)).compareTo(isolationPercentage) > 0;
    }
}
