package com.lickhunter.web.services.impl;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.trade.AccountInformation;
import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ApplicationConfig config;

    public void test() {
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(config.getKey(), config.getSecret(),
                options);
        AccountInformation accountInformation = syncRequestClient.getAccountInformation();
    }
    private Boolean isOpenOrderIsolationActive(AccountInformation accountInformation, BigDecimal isolationPercentage) {
        return accountInformation.getTotalPositionInitialMargin()
                .divide(accountInformation.getTotalMarginBalance())
                .multiply(BigDecimal.valueOf(100)).compareTo(isolationPercentage) > 0;
    }
}
