package com.lickhunter.web.websockets;

import com.binance.client.SubscriptionClient;
import com.binance.client.SubscriptionOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.enums.OrderState;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.TransactType;
import com.binance.client.model.market.Candlestick;
import com.binance.client.model.market.MarkPrice;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.constants.UserDataEventConstants;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
import com.lickhunter.web.events.BinanceEvents;
import com.lickhunter.web.repositories.CandlestickRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.LickHunterService;
import com.lickhunter.web.services.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Component
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class BinanceSubscription {
    private final CandlestickRepository candlestickRepository;
    private final SymbolRepository symbolRepository;
    private final PositionRepository positionRepository;
    private final FileService fileService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AccountService accountService;
    private final LickHunterScheduledTasks lickHunterScheduledTasks;
    private final LickHunterService lickHunterService;
    private final TradeService tradeService;

    @Value("${binance.candlesticks}")
    private String[] candlesticks;

    @Async
    public void subscribeCandleStickData() {
        log.info("Subscribing to Binance candlesticks data");
        SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
        subscriptionOptions.setUri("wss://fstream.binance.com");
        subscriptionOptions.setConnectionDelayOnFailure(3);
        SubscriptionClient subscriptionClient = SubscriptionClient.create(subscriptionOptions);
        List<String> symbols = symbolRepository.findAll().stream()
                .map(SymbolRecord::getSymbol)
                .filter(s -> s.matches("^.*USDT$"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        Arrays.stream(candlesticks).forEach(c -> {
            subscriptionClient.subscribeCandlestickEvent(symbols, CandlestickInterval.of(c), ((event) -> {
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
                candlestickRepository.insertOrUpdate(event.getSymbol(), candlestick, CandlestickInterval.of(c));
            }), e -> log.error(String.format("Error during candlestick subscription event: %s", e.getMessage())));
        });
        log.info("Subscribed to Binance Candlestick data");
    }

    @Async
    @SneakyThrows
    public void subscribeUserData() {
        log.info("Subscribing to User Data");
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        String listenKey = syncRequestClient.startUserDataStream();
        Thread listenKeyKeepALive = new Thread(() -> syncRequestClient.keepUserDataStream(listenKey));
        executorService.scheduleWithFixedDelay(listenKeyKeepALive, 0, 45, TimeUnit.MINUTES);
        SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
        subscriptionOptions.setReceiveLimitMs(1800000);
        subscriptionOptions.setUri("wss://fstream.binance.com/");
        subscriptionOptions.setConnectionDelayOnFailure(3);
        SubscriptionClient subscriptionClient = SubscriptionClient.create(subscriptionOptions);
        subscriptionClient.subscribeUserDataEvent(listenKey, ((event) -> {
                switch(UserDataEventConstants.valueOf(event.getEventType())) {
                    case ACCOUNT_UPDATE:
                        //TODO find a way to update maintenance margin
//                        positionRepository.insertOrUpdate(event.getAccountUpdate().getPositions(), settings.getKey());
//                        assetRepository.updateFromPosition(event.getAccountUpdate(), settings.getKey());
//                        accountRepository.updateFromAsset(settings.getKey());
                        publishBinanceEvent(UserDataEventConstants.ACCOUNT_UPDATE.getValue());
                        break;
                    case MARGIN_CALL:
                        //TODO add notifications for margin calls
                        publishBinanceEvent(UserDataEventConstants.MARGIN_CALL.getValue());
                        break;
                    case ORDER_TRADE_UPDATE:
                        //Update account information for new and closed positions
                        if((event.getOrderUpdate().getType().equalsIgnoreCase(OrderType.MARKET.name())
                            && event.getOrderUpdate().getExecutionType().equalsIgnoreCase(TransactType.TRADE.name())
                            && event.getOrderUpdate().getOrderStatus().equalsIgnoreCase(OrderState.FILLED.name()))
                            || (event.getOrderUpdate().getExecutionType().equalsIgnoreCase(TransactType.TRADE.name())
                                && event.getOrderUpdate().getType().equalsIgnoreCase(OrderType.LIMIT.name())
                                && event.getOrderUpdate().getOrderStatus().equalsIgnoreCase(OrderState.FILLED.name())
                                && event.getOrderUpdate().getIsReduceOnly().equals(true))) {
                            positionRepository.updateOrder(event.getOrderUpdate(), settings.getKey());
                            accountService.getAccountInformation();
                            Long buyCount = symbolRepository.updateNumberOfBuys(event.getOrderUpdate(), settings.getKey(), lickHunterService.getActiveSettings());
                            log.info(String.format("[ORDER UPDATE] symbol: %s, side: %s, buyCount: %s, realizedProfit: %s",
                                    event.getOrderUpdate().getSymbol(),
                                    event.getOrderUpdate().getSide(),
                                    buyCount,
                                    event.getOrderUpdate().getRealizedProfit()));
                            lickHunterScheduledTasks.writeToCoinsJson();
                        }
                        tradeService.takeProfitLimitOrders(event.getOrderUpdate(), settings.getKey());
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
        log.info("Successfully subscribed to User Data.");
    }

    @Async
    public void subscribeMarkPrice() {
        log.info("Subscribing to Binance Mark Price data.");
        SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
        subscriptionOptions.setUri("wss://fstream.binance.com/");
        subscriptionOptions.setConnectionDelayOnFailure(3);
        SubscriptionClient subscriptionClient = SubscriptionClient.create(subscriptionOptions);
        try {
            subscriptionClient.subscribeAllMarkPriceEvent(data -> {
                data.forEach(event -> {
                    if(event.getEventType().contains("markPriceUpdate")) {
                            MarkPrice markPrice = new MarkPrice();
                            markPrice.setMarkPrice(event.getMarkPrice());
                            markPrice.setSymbol(event.getSymbol());
                            markPrice.setTime(event.getEventTime());
                            markPrice.setLastFundingRate(event.getFundingRate());
                            markPrice.setNextFundingTime(event.getNextFundingTime());
                            symbolRepository.insertOrUpdate(markPrice);
                        }
                });
            }, e-> log.error("Error during mark price subscription event: %s", e.getMessage()));
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
