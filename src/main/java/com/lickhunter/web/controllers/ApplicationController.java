package com.lickhunter.web.controllers;

import com.binance.client.model.enums.CandlestickInterval;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.websockets.BinanceSubscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/candlestick/subscribe")
    public ResponseEntity<?> subscribeCandleStickData() throws ServiceException {
        binanceSubscription.subscribeCandleStickData();
        return ResponseEntity.ok(null);
    }

    @GetMapping("/candlestick/unsubscribe")
    public ResponseEntity<?> unsubscribeCandleStickData() throws ServiceException {
        binanceSubscription.unsubscribeClient();
        return ResponseEntity.ok(null);
    }

    @GetMapping("/candlestick")
    public ResponseEntity<?> getCandleStickData() throws ServiceException {
        marketService.getCandleStickData(CandlestickInterval.FIFTEEN_MINUTES, 500);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/userdata")
    public ResponseEntity<?> subscribeUserData() throws ServiceException {
        binanceSubscription.subscribeUserData();
        return ResponseEntity.ok(null);
    }

}
