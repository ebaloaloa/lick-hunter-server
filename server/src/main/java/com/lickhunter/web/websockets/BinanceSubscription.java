package com.lickhunter.web.websockets;

import com.binance.client.SubscriptionClient;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import com.binance.client.model.market.MarkPrice;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.constants.UserDataEventConstants;
import com.lickhunter.web.events.BinanceEvents;
import com.lickhunter.web.models.market.ExchangeInformation;
import com.lickhunter.web.repositories.*;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


@Component
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class BinanceSubscription {
    private final MarketService marketService;
    private final CandlestickRepository candlestickRepository;
    private final SymbolRepository symbolRepository;
    private final AssetRepository assetRepository;
    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final FileService fileService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Async
    public void subscribeCandleStickData() throws Exception {
        log.info("Subscribing to Binance candlesticks data");
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SubscriptionClient subscriptionClient = SubscriptionClient.create(settings.getKey(), settings.getSecret());
        ExchangeInformation exchangeInformation = marketService.getExchangeInformation();
        try {
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
                                candlestickRepository.insert(s.getSymbol(), candlestick, CandlestickInterval.FIFTEEN_MINUTES);
                                startTime.set(candlestick.getOpenTime());
                            }
                        }), e -> log.error(String.format("Error during candlestick subscription event: %s", e.getMessage())));
                    });
            log.info("Subscribed to Binance Candlestick data");
        } catch (Exception e) {
            subscriptionClient.unsubscribeAll();
            log.error("Unsubscribed to Candle Stick Events");
            subscribeCandleStickData();
        }
    }

    @Async
    public void subscribeUserData() throws Exception {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        String listenKey = syncRequestClient.startUserDataStream();
        Thread listenKeyKeepALive = new Thread(() -> syncRequestClient.keepUserDataStream(listenKey));
        executorService.scheduleWithFixedDelay(listenKeyKeepALive, 0, 45, TimeUnit.MINUTES);
        SubscriptionClient subscriptionClient = SubscriptionClient.create(settings.getKey(), settings.getSecret());
        subscriptionClient.subscribeUserDataEvent(listenKey, ((event) -> {
                switch(UserDataEventConstants.valueOf(event.getEventType())) {
                    case ACCOUNT_UPDATE:
                        log.debug(event.toString());
                        //TODO find a way to update maintenance margin
                        positionRepository.update(event.getAccountUpdate().getPositions(), settings.getKey());
                        assetRepository.updateFromPosition(event.getAccountUpdate(), settings.getKey());
                        accountRepository.updateFromAsset(settings.getKey());
                        publishBinanceEvent(UserDataEventConstants.ACCOUNT_UPDATE.getValue());
                        break;
                    case MARGIN_CALL:
                        //TODO add notifications for margin calls
                        publishBinanceEvent(UserDataEventConstants.MARGIN_CALL.getValue());
                        break;
                    case ORDER_TRADE_UPDATE:
                        //TODO implement position updates here. **HISTORICAL DATA**
                        //  executionType=TRADE
                        //  orderStatus=FILLED
                        //  isReduceOnly=true
                        publishBinanceEvent(UserDataEventConstants.ORDER_TRADE_UPDATE.getValue());
                        break;
                    case ACCOUNT_CONFIG_UPDATE:
                        //TODO implement leverage change and update position table
//                        positionRepository.update(event.getAccountUpdate().getPositions(), settings.getKey());
                        publishBinanceEvent(UserDataEventConstants.ACCOUNT_CONFIG_UPDATE.getValue());
                    default:
                        //TODO add more event types here
                        // ACCOUNT_CONFIG_UPDATE = leverage
                        log.warn("Event not identified.");
                }
        }), e -> log.error(String.format("Error during User Data Subscription event: %s, type: %s", e.getMessage(), e.getErrType())));
    }

    @Async
    public void subscribeMarkPrice() throws Exception {
        log.info("Subscribing to Binance Mark Price data.");
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SubscriptionClient subscriptionClient = SubscriptionClient.create(settings.getKey(), settings.getSecret());
        try {
            marketService.getExchangeInformation().getSymbols()
                    .forEach(s -> subscriptionClient.subscribeMarkPriceEvent(s.getSymbol().toLowerCase(), event -> {
                        if(event.getEventType().contains("markPriceUpdate")) {
                            MarkPrice markPrice = new MarkPrice();
                            markPrice.setMarkPrice(event.getMarkPrice());
                            markPrice.setSymbol(event.getSymbol());
                            markPrice.setTime(event.getEventTime());
                            markPrice.setLastFundingRate(event.getFundingRate());
                            markPrice.setNextFundingTime(event.getNextFundingTime());
                            symbolRepository.insert(markPrice);
                        }
                    }, e-> log.error("Error during mark price subscription event: %s", e.getMessage())));
            log.info("Subscribed to Binance Mark Price data");
        } catch (Exception e) {
            subscriptionClient.unsubscribeAll();
            log.error("Unsubscribed to Mark Price Events.");
            subscribeMarkPrice();
        }
    }

    private void publishBinanceEvent(final String message) {
        BinanceEvents binanceEvents = new BinanceEvents(this, message);
        applicationEventPublisher.publishEvent(binanceEvents);
    }
}
