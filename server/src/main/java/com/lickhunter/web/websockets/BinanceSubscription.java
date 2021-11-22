package com.lickhunter.web.websockets;

import com.binance.client.SubscriptionClient;
import com.binance.client.SubscriptionOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.*;
import com.binance.client.model.market.Candlestick;
import com.binance.client.model.market.MarkPrice;
import com.binance.client.model.trade.Order;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.UserDefinedSettings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.constants.UserDataEventConstants;
import com.lickhunter.web.entities.tables.records.AccountRecord;
import com.lickhunter.web.entities.tables.records.PositionRecord;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
import com.lickhunter.web.events.BinanceEvents;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.repositories.CandlestickRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
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
    private final TechnicalIndicatorService technicalIndicatorService;
    private final AccountRepository accountRepository;
    @Qualifier("telegramNotification")
    @Autowired
    private NotificationService<String> telegramService;

    @Value("${binance.candlesticks}")
    private String[] candlesticks;
    @Value("${telegram.notification.dcaBuys}")
    private Boolean tgNotifdcaBuys;
    @Value("${telegram.notification.tradeClosed}")
    private Boolean tgNotifTradeClosed;
    @Value("${telegram.notification.liquidation}")
    private Boolean tgNotifLiquidation;

    public void subscribeCandleStickData() {
        log.info("Subscribing to Binance candlesticks data");
        List<String> symbols = symbolRepository.findAll().stream()
                .map(SymbolRecord::getSymbol)
                .filter(s -> s.matches("^.*USDT$"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
        subscriptionOptions.setReceiveLimitMs(1800000);
        SubscriptionClient subscriptionClient = SubscriptionClient.create(subscriptionOptions);
        Arrays.stream(candlesticks)
            .forEach(c -> {
                subscriptionClient.subscribeCandlestickEvent(symbols, CandlestickInterval.of(c), ((event) -> {
                        Candlestick candlestick = new Candlestick();
                        candlestick.setOpenTime(Optional.ofNullable(event.getStartTime()).orElse(null));
                        candlestick.setOpen(Optional.of(event.getOpen()).orElse(null));
                        candlestick.setHigh(Optional.ofNullable(event.getHigh()).orElse(null));
                        candlestick.setLow(Optional.ofNullable(event.getLow()).orElse(null));
                        candlestick.setClose(Optional.ofNullable(event.getClose()).orElse(null));
                        candlestick.setVolume(Optional.ofNullable(event.getVolume()).orElse(null));
                        candlestick.setCloseTime(Optional.ofNullable(event.getCloseTime()).orElse(null));
                        candlestick.setQuoteAssetVolume(Optional.ofNullable(event.getQuoteAssetVolume()).orElse(null));
                        candlestick.setNumTrades(Math.toIntExact(Optional.ofNullable(event.getNumTrades()).orElse(null)));
                        candlestick.setTakerBuyBaseAssetVolume(Optional.ofNullable(event.getTakerBuyBaseAssetVolume()).orElse(null));
                        candlestick.setTakerBuyQuoteAssetVolume(Optional.ofNullable(event.getTakerBuyQuoteAssetVolume()).orElse(null));
                        candlestickRepository.insertOrUpdate(event.getSymbol(), candlestick, CandlestickInterval.of(c));
                    }), e -> {
                        String message = String.format("Error during candlestick subscription event: %s", e.getMessage());
                        telegramService.send(message);
                        log.error(message);
                    });
            });
        log.info("Subscribed to Binance Candlestick data");
    }

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
        SubscriptionClient subscriptionClient = SubscriptionClient.create(subscriptionOptions);
        subscriptionClient.subscribeUserDataEvent(listenKey, ((event) -> {
            switch (UserDataEventConstants.valueOf(event.getEventType())) {
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
                    if ((event.getOrderUpdate().getType().equalsIgnoreCase(OrderType.MARKET.name())
                            && event.getOrderUpdate().getExecutionType().equalsIgnoreCase(TransactType.TRADE.name())
                            && event.getOrderUpdate().getOrderStatus().equalsIgnoreCase(OrderState.FILLED.name()))
                            || (event.getOrderUpdate().getExecutionType().equalsIgnoreCase(TransactType.TRADE.name())
                            && event.getOrderUpdate().getType().equalsIgnoreCase(OrderType.LIMIT.name())
                            && event.getOrderUpdate().getOrderStatus().equalsIgnoreCase(OrderState.FILLED.name())
                            && event.getOrderUpdate().getIsReduceOnly().equals(true))) {
                        positionRepository.updateOrder(event.getOrderUpdate(), settings.getKey());
                        Long buyCount = symbolRepository.updateNumberOfBuys(event.getOrderUpdate());
                        accountService.getAccountInformation();
                        String orderUpdateMessage = String.format("symbol: %s, side: %s, buyCount: %s, realizedProfit: %s",
                                event.getOrderUpdate().getSymbol(),
                                event.getOrderUpdate().getSide(),
                                buyCount + 1,
                                event.getOrderUpdate().getRealizedProfit());
                        if(tgNotifTradeClosed && event.getOrderUpdate().getRealizedProfit().compareTo(BigDecimal.ZERO) != 0) {
                            telegramService.send(String.format("[CLOSED] %s", orderUpdateMessage));
                        }
                        if(tgNotifdcaBuys && event.getOrderUpdate().getRealizedProfit().compareTo(BigDecimal.ZERO) == 0) {
                            telegramService.send(String.format("[NEW ORDER] %s", orderUpdateMessage));
                        }
                        log.info(String.format("[ORDER UPDATE] %s", orderUpdateMessage));
                        lickHunterScheduledTasks.writeToCoinsJson();
                    }
                    tradeService.takeProfitLimitOrders(event.getOrderUpdate());
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
        }), e -> {
            String message = String.format("Error during User Data Subscription event: %s, type: %s.", e.getMessage(), e.getErrType());
            telegramService.send(message);
            log.error(message);
        });
        log.info("Successfully subscribed to User Data.");
    }

    public void subscribeMarkPrice() {
        log.info("Subscribing to Binance Mark Price data.");
        SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
        subscriptionOptions.setReceiveLimitMs(1800000);
        SubscriptionClient subscriptionClient = SubscriptionClient.create(subscriptionOptions);
        subscriptionClient.subscribeAllMarkPriceEvent(data -> data.forEach(event -> {
            if(event.getEventType().contains("markPriceUpdate")) {
                MarkPrice markPrice = new MarkPrice();
                markPrice.setMarkPrice(event.getMarkPrice());
                markPrice.setSymbol(event.getSymbol());
                markPrice.setTime(event.getEventTime());
                markPrice.setLastFundingRate(event.getFundingRate());
                markPrice.setNextFundingTime(event.getNextFundingTime());
                symbolRepository.insertOrUpdate(markPrice);
            }
        }), e -> {
            String message = String.format("Error during mark price subscription event: %s.", e.getMessage());
            telegramService.send(message);
            log.error(message);
        });
    }

     public void subscribeLiquidation() {
         Settings settings = lickHunterService.getLickHunterSettings();
         SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
         SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
         subscriptionOptions.setReceiveLimitMs(1800000);
         SubscriptionClient subscriptionClient = SubscriptionClient.create(subscriptionOptions);
         subscriptionClient.subscribeAllLiquidationOrderEvent(data -> {
             List<SymbolRecord> symbolRecords = symbolRepository.findTradeableSymbols();
             symbolRecords.stream()
                     .filter(c -> c.getSymbol().equalsIgnoreCase(data.getSymbol()))
                     .forEach(c -> {
                         List<Order> order = syncRequestClient.getAllOrders(
                                 data.getSymbol(),
                                 null,
                                 null,
                                 null,
                                 null)
                                 .stream()
                                 .filter(o -> o.getStatus().equalsIgnoreCase(OrderState.FILLED.name())
                                                && o.getType().equalsIgnoreCase(OrderType.MARKET.name()))
                                 .sorted(Comparator.comparing(Order::getUpdateTime).reversed())
                                 .collect(Collectors.toList());
                         WebSettings webSettings = lickHunterService.getWebSettings();
                         UserDefinedSettings activeSettings = lickHunterService.getActiveSettings();
                         Boolean isMedian = Objects.nonNull(activeSettings.getAutoLickValue())
                                 ? lickHunterService.getActiveSettings().getAutoLickValue().equalsIgnoreCase("median")
                                 : null;
                         BigDecimal lickValue = Objects.isNull(isMedian) ?
                                 BigDecimal.valueOf(activeSettings.getLickValue()) :
                                 isMedian ?
                                         BigDecimal.valueOf(c.getLickMedian()) :
                                         BigDecimal.valueOf(c.getLickAverage());
                         BarSeries barSeries = technicalIndicatorService.getBarSeries(data.getSymbol(), CandlestickInterval.of(lickHunterService.getWebSettings().getVwapTimeframe()));
                         Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(data.getSymbol());
                         Optional<PositionRecord> positionRecord = positionRepository.findBySymbolAndAccountId(c.getSymbol(), settings.getKey());
                         BigDecimal qty = this.getQty(symbolRecord.get());
                         if(qty.compareTo(BigDecimal.ZERO) > 0 && positionRecord.isPresent()) {
                             //uptrend
                             if(data.getSide().equalsIgnoreCase(OrderSide.BUY.name())
                                    && ((positionRecord.get().getInitialMargin() != 0.0 && order.get(0).getSide().equalsIgnoreCase(OrderSide.SELL.name())))
                                        || positionRecord.get().getInitialMargin() == 0.0) {
                                 Strategy shortStrategy = technicalIndicatorService.vwapShortStrategy(
                                         barSeries,
                                         webSettings.getVwapLength(),
                                         c.getShortOffset(),
                                         data.getAveragePrice().doubleValue());
                                 boolean shortSatisfied = shortStrategy.getEntryRule()
                                         .isSatisfied(barSeries.getEndIndex());
                                 if(shortSatisfied
                                         && (data.getPrice().multiply(data.getOrigQty())).compareTo(lickValue) > 0
                                         && true ) {
                                     String liquidationMessage = String.format("[LIQUIDATION SATISFIED] symbol: %s, price: %s, side: %s, markprice: %s",
                                             data.getSymbol(),
                                             data.getPrice().multiply(data.getLastFilledAccumulatedQty()),
                                             data.getSide(),
                                             data.getAveragePrice().doubleValue());
                                     if(tgNotifLiquidation) {
                                         telegramService.send(liquidationMessage);
                                     }
                                     log.debug(liquidationMessage);
                                     tradeService.newOrder(
                                             data.getSymbol(),
                                             OrderSide.SELL,
                                             OrderType.MARKET,
                                             null,
                                             String.valueOf(qty),
                                             null,
                                             false,
                                             false);
                                     symbolRepository.addNumberOfBuys(data.getSymbol(), settings.getKey(), activeSettings);
                                 }
                             }
                             //downtrend
                             if(data.getSide().equalsIgnoreCase(OrderSide.SELL.name())
                                     && ((positionRecord.get().getInitialMargin() != 0.0 && order.get(0).getSide().equalsIgnoreCase(OrderSide.BUY.name())))
                                     || positionRecord.get().getInitialMargin() == 0.0) {
                                 Strategy longStrategy = technicalIndicatorService.vwapLongStrategy(
                                         barSeries,
                                         webSettings.getVwapLength(),
                                         c.getLongOffset(),
                                         data.getAveragePrice().doubleValue());
                                 boolean longSatisfied = longStrategy.getEntryRule()
                                         .isSatisfied(barSeries.getEndIndex());
                                 if(longSatisfied
                                         && (data.getPrice().multiply(data.getOrigQty())).compareTo(lickValue) > 0) {
                                     log.debug(String.format("[LIQUIDATION SATISFIED] symbol: %s, price: %s, side: %s, markprice: %s",
                                             data.getSymbol(),
                                             data.getPrice().multiply(data.getLastFilledAccumulatedQty()),
                                             data.getSide(),
                                             data.getAveragePrice().doubleValue()));
                                     tradeService.newOrder(
                                             data.getSymbol(),
                                             OrderSide.BUY,
                                             OrderType.MARKET,
                                             null,
                                             String.valueOf(qty),
                                             null,
                                             false,
                                             false);
                                     symbolRepository.addNumberOfBuys(data.getSymbol(), settings.getKey(), activeSettings);
                                 }
                             }
                             lickHunterScheduledTasks.writeToCoinsJson();
                         }
                     });
         }, exception -> {
             String message = String.format("Error during liquidation event: %s", exception.getMessage());
             telegramService.send(message);
             log.error(message);
         });
     }

     private BigDecimal getQty(SymbolRecord symbolRecord) {
         Settings settings = lickHunterService.getLickHunterSettings();
         UserDefinedSettings activeSettings = lickHunterService.getActiveSettings();
         Optional<PositionRecord> positionRecord = positionRepository.findBySymbolAndAccountId(symbolRecord.getSymbol(), settings.getKey());
         Optional<AccountRecord> accountRecord = accountRepository.findByAccountId(settings.getKey());
         BigDecimal percentBuy = new BigDecimal(settings.getPercentBal());
         if(positionRecord.isPresent()) {
             //existing positions
             if(positionRecord.get().getInitialMargin() != 0.0) {
                 BigDecimal percentageFromAverage = ((BigDecimal.valueOf(symbolRecord.getMarkPrice())
                         .subtract(new BigDecimal(positionRecord.get().getEntryPrice())).abs())
                         .divide(new BigDecimal(positionRecord.get().getEntryPrice()), MathContext.DECIMAL128))
                         .multiply(BigDecimal.valueOf(100));
                 if (percentageFromAverage.compareTo(activeSettings.getRangeFive().getPercentFromAverage()) > 0) {
                     percentBuy = activeSettings.getRangeSix().getPercentBuy();
                 } else if (percentageFromAverage.compareTo(activeSettings.getRangeFour().getPercentFromAverage()) > 0) {
                     percentBuy = activeSettings.getRangeFive().getPercentBuy();
                 } else if (percentageFromAverage.compareTo(activeSettings.getRangeThree().getPercentFromAverage()) > 0) {
                     percentBuy = activeSettings.getRangeFour().getPercentBuy();
                 } else if (percentageFromAverage.compareTo(activeSettings.getRangeTwo().getPercentFromAverage()) > 0) {
                     percentBuy = activeSettings.getRangeThree().getPercentBuy();
                 } else if (percentageFromAverage.compareTo(activeSettings.getRangeOne().getPercentFromAverage()) > 0) {
                     percentBuy = activeSettings.getRangeTwo().getPercentBuy();
                 } else if (percentageFromAverage.compareTo(activeSettings.getDcaStart()) > 0) {
                     percentBuy = activeSettings.getRangeOne().getPercentBuy();
                 }
                 //maxPos reached, percentBuy = 0
                 if(BigDecimal.valueOf(accountRecord.get().getTotalWalletBalance())
                    .multiply(activeSettings.getMaxPos().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN))
                         .compareTo(BigDecimal.valueOf(positionRecord.get().getInitialMargin()))  < 0 ) {
                     percentBuy = BigDecimal.ZERO;
                 }
             }
         }
         //amount = totalBal * (pbal/100) * leverage
         //qty = amount/price
         BigDecimal qty = BigDecimal.valueOf(accountRecord.get().getTotalWalletBalance() * (percentBuy.doubleValue()/100) * Double.parseDouble(settings.getLeverage()))
                 .divide(BigDecimal.valueOf(symbolRecord.getMarkPrice()), symbolRecord.getQuantityPrecision().intValue(), RoundingMode.HALF_DOWN);
         return qty;
     }

     private void publishBinanceEvent(final String message) {
        BinanceEvents binanceEvents = new BinanceEvents(this, message);
        applicationEventPublisher.publishEvent(binanceEvents);
     }
}
