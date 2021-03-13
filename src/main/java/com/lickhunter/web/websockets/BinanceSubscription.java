package com.lickhunter.web.websockets;

import com.binance.client.RequestOptions;
import com.binance.client.SubscriptionClient;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.models.market.ExchangeInformation;
import com.lickhunter.web.repositories.CandlestickRepository;
import com.lickhunter.web.services.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class BinanceSubscription {
    private final MarketService marketService;
    private final CandlestickRepository candlestickRepository;
    private final SubscriptionClient subscriptionClient = SubscriptionClient.create(ApplicationConfig.API_KEY, ApplicationConfig.API_SECRET);
    private final ApplicationConfig config;

    public void subscribeCandleStickData() throws ServiceException {
        log.info("Subscribing to candlesticks data");
        ExchangeInformation exchangeInformation = marketService.getExchangeInformation();
        exchangeInformation.getSymbols().stream()
                .forEach(s -> {
                    AtomicReference<Long> startTime = new AtomicReference<>(1L);
                    subscriptionClient.subscribeCandlestickEvent(s.getSymbol().toLowerCase(), CandlestickInterval.ONE_MINUTE, ((event) -> {
                        if (startTime.get().compareTo(event.getStartTime()) != 0) {
                            Candlestick candlestick = new Candlestick();
                            candlestick.setOpenTime(event.getStartTime());
                            candlestick.setOpen(event.getOpen());
                            candlestick.setHigh(event.getHigh());
                            candlestick.setLow(event.getClose());
                            candlestick.setClose(event.getClose());
                            candlestick.setVolume(event.getVolume());
                            candlestick.setCloseTime(event.getCloseTime());
                            candlestick.setQuoteAssetVolume(event.getQuoteAssetVolume());
                            candlestick.setNumTrades(event.getNumTrades().intValue());
                            candlestick.setTakerBuyBaseAssetVolume(event.getTakerBuyBaseAssetVolume());
                            candlestick.setTakerBuyQuoteAssetVolume(event.getTakerBuyQuoteAssetVolume());
                            candlestickRepository.insert(s.getSymbol(), candlestick);
                            startTime.set(candlestick.getOpenTime());
                        }
                    }), e -> {
                        log.error(String.format("Error during candlestick subscription event: %s", e.getMessage()));
                    });
                });
        log.info("Subscribed to Candlestick data");
    }

    public void unsubscribeClient() {
        log.info("Unsubscribing to Binance Websocket.");
        subscriptionClient.unsubscribeAll();
        log.info("Successfully unsubscribed to Binance Websocket.");
    }

    public void subscribeUserData() {
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(config.getKey(), config.getSecret(),
                options);
        String listenKey = syncRequestClient.startUserDataStream();
        syncRequestClient.keepUserDataStream(listenKey);
        SubscriptionClient client = SubscriptionClient.create(config.getKey(), config.getSecret());
        client.subscribeUserDataEvent(listenKey, ((event) -> {
            System.out.println(event);
        }), e -> {
            log.error(String.format("Error during user data subscription event: %s", e.getMessage()));
        });
    }
}
