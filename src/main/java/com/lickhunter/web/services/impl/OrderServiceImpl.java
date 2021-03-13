package com.lickhunter.web.services.impl;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ApplicationConfig config;

    public void test() {
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(config.getKey(), config.getSecret(),
                options);

    }
}
