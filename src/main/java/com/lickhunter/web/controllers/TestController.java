package com.lickhunter.web.controllers;

import com.binance.client.SubscriptionClient;
import com.binance.client.SyncRequestClient;
import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final MarketService marketService;
    private final AccountService accountService;
    private final ApplicationConfig config;

    @GetMapping
    public ResponseEntity<?> testEndpoint() throws ServiceException {
        // Start user data stream
        SyncRequestClient syncRequestClient = SyncRequestClient.create(config.getKey(), config.getSecret());
        String listenKey = syncRequestClient.startUserDataStream();
        System.out.println("listenKey: " + listenKey);

        // Keep user data stream
        syncRequestClient.keepUserDataStream(listenKey);

        // Close user data stream
        syncRequestClient.closeUserDataStream(listenKey);

        SubscriptionClient client = SubscriptionClient.create(config.getKey(), config.getAuth());

        client.subscribeUserDataEvent(listenKey, ((event) -> {
            log.info(event.toString());
        }), null);
        return ResponseEntity.ok(marketService.getTickerByQuery(new TickerQueryTO()
                .withMinimumTradingAge(30)
                .withPercentageFromAllTimeHigh(5L)));
    }
}
