package com.lickhunter.web.controllers;

import com.binance.client.model.enums.CandlestickInterval;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.websockets.BinanceSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/application")
@RestController
@RequiredArgsConstructor
public class ApplicationController extends BaseController {

    private final BinanceSubscription binanceSubscription;
    private final MarketService marketService;
    private final AccountService accountService;

    @GetMapping("/candlestick/subscribe")
    public ResponseEntity<?> subscribeCandleStickData() throws ServiceException {
        binanceSubscription.subscribeCandleStickData();
        return ResponseEntity.ok(null);
    }

    @GetMapping("/candlestick")
    public ResponseEntity<?> getCandleStickData(CandlestickInterval interval, int limit) throws ServiceException {
        marketService.getCandleStickData(interval, limit);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/userdata")
    public ResponseEntity<?> subscribeUserData() throws ServiceException {
        binanceSubscription.subscribeUserData();
        return ResponseEntity.ok(null);
    }

    @GetMapping("/markprice")
    public ResponseEntity<?> getMarkPriceData() throws ServiceException {
        marketService.getMarkPriceData();
        return ResponseEntity.ok(null);
    }

    @GetMapping("/subscribe/markprice")
    public ResponseEntity<?> subscribeMarkPrice() throws ServiceException {
        binanceSubscription.subscribeMarkPrice();
        return ResponseEntity.ok(null);
    }

    @GetMapping("/account_information")
    public ResponseEntity<?> getAccountInformation() throws ServiceException {
        accountService.getAccountInformation();
        return ResponseEntity.ok(null);
    }
}
