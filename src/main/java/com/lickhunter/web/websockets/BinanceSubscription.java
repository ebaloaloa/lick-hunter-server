package com.lickhunter.web.websockets;

import com.binance.client.SubscriptionClient;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import com.binance.client.model.market.MarkPrice;
import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.models.market.ExchangeInformation;
import com.lickhunter.web.repositories.CandlestickRepository;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.services.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;


@Component
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class BinanceSubscription {
    private final MarketService marketService;
    private final CandlestickRepository candlestickRepository;
    private final SymbolRepository symbolRepository;
    private final ApplicationConfig config;

    @Async
    public void subscribeCandleStickData() throws ServiceException {
        log.info("Subscribing to Binance candlesticks data");
        SubscriptionClient subscriptionClient = SubscriptionClient.create(config.getKey(), config.getSecret());
        ExchangeInformation exchangeInformation = marketService.getExchangeInformation();
        exchangeInformation.getSymbols()
                .forEach(s -> {
                    AtomicReference<Long> startTime = new AtomicReference<>(1L);
                    subscriptionClient.subscribeCandlestickEvent(s.getSymbol().toLowerCase(), CandlestickInterval.FIFTEEN_MINUTES, ((event) -> {
                        if (startTime.get().compareTo(event.getStartTime()) != 0) {
                            //TODO create converter
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
                    }), e -> log.error(String.format("Error during candlestick subscription event: %s", e.getMessage())));
                });
        log.info("Subscribed to Binance Candlestick data");

    }

    @Async
    public void subscribeUserData() {
        SyncRequestClient syncRequestClient = SyncRequestClient.create(config.getKey(), config.getSecret());
        String listenKey = syncRequestClient.startUserDataStream();
        syncRequestClient.keepUserDataStream(listenKey);
        SubscriptionClient subscriptionClient = SubscriptionClient.create(config.getKey(), config.getSecret());
        subscriptionClient.subscribeUserDataEvent(listenKey, ((event) -> {
            System.out.println(event);
        }), e -> {
            log.error(String.format("Error during user data subscription event: %s", e.getMessage()));
        });
    }

//    @Async
    public void subscribeMarkPrice() throws ServiceException {
        log.info("Subscribing to Binance Mark Price data.");
        SubscriptionClient subscriptionClient = SubscriptionClient.create(config.getKey(), config.getSecret());
        marketService.getExchangeInformation().getSymbols()
                .forEach(s -> {
                    subscriptionClient.subscribeMarkPriceEvent(s.getSymbol().toLowerCase(), event -> {
                        if(event.getEventType().contains("markPriceUpdate")) {
                            MarkPrice markPrice = new MarkPrice();
                            markPrice.setMarkPrice(event.getMarkPrice());
                            markPrice.setSymbol(event.getSymbol());
                            markPrice.setTime(event.getEventTime());
                            markPrice.setLastFundingRate(event.getFundingRate());
                            markPrice.setNextFundingTime(event.getNextFundingTime());
                            symbolRepository.insert(markPrice);
                        }
                    }, e-> log.error("Error during mark price subscription event: %s", e.getMessage()));
                });
        log.info("Subscribed to Binance Mark Price data");
    }
}
