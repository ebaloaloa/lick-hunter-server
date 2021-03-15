package com.lickhunter.web.scheduler;

import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinanceScheduledTasks {

    private final AccountService accountService;
    private final MarketService marketService;

    @Scheduled(fixedDelay = 1000 * 30)
    public void getAccountInformation() throws Exception {
        accountService.getAccountInformation();
    }

    @Scheduled(fixedDelay = 1000 * 30)
    public void getMarkPriceData() throws Exception {
        marketService.getMarkPriceData();
    }
}
