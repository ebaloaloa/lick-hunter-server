package com.lickhunter.web.controllers;

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

    @GetMapping
    public ResponseEntity<?> testEndpoint() throws ServiceException {
        return ResponseEntity.ok(
                marketService.getTickerByQuery(new TickerQueryTO()
                .withMinimumTradingAge(500)
                .withPercentageFromAllTimeHigh(10L)));
    }
}
