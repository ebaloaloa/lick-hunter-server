package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.ResponseResult;
import com.binance.client.model.enums.*;
import com.binance.client.model.trade.Leverage;
import com.binance.client.model.trade.Order;
import com.binance.client.model.user.OrderUpdate;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.UserDefinedSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.constants.TradeConstants;
import com.lickhunter.web.entities.tables.records.AccountRecord;
import com.lickhunter.web.entities.tables.records.PositionRecord;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.LickHunterService;
import com.lickhunter.web.services.TradeService;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final FileService fileService;
    private final PositionRepository positionRepository;
    private final AccountService accountService;
    private final SymbolRepository symbolRepository;
    private final LickHunterService lickHunterService;
    private final AccountRepository accountRepository;
    private final LickHunterScheduledTasks lickHunterScheduledTasks;

    public ResponseResult marginType(String symbol, String marginType) {
        ResponseResult result = new ResponseResult();
        try {
            Settings settings = lickHunterService.getLickHunterSettings();
            SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
            result = syncRequestClient.changeMarginType(symbol, marginType);
        } catch (Exception e) {
            log.error(String.format("Error when changing margin type for symbol: %s marginType: %s | msg: %s", symbol, marginType, e.getMessage()));
        }
        return result;
    }

    public void changeAllMarginType() {
        Settings settings = lickHunterService.getLickHunterSettings();
        UserDefinedSettings activeSettings = lickHunterService.getActiveSettings();
        List<PositionRecord> positionRecordList = positionRepository.findByAccountId(settings.getKey());
        AtomicReference<Boolean> changed = new AtomicReference<>(false);
        positionRecordList.stream()
            .filter(positionRecord -> positionRecord.getSymbol().matches("^.*USDT$"))
                .filter(positionRecord -> positionRecord.getInitialMargin() == 0.0)
                .forEach(p -> {
                    if((activeSettings.getMarginType().equalsIgnoreCase(TradeConstants.ISOLATED.getValue()) && !p.getIsolated())
                                || (activeSettings.getMarginType().equalsIgnoreCase(TradeConstants.CROSSED.getValue()) && p.getIsolated())) {
                        try {
                            this.marginType(p.getSymbol(), activeSettings.getMarginType().toUpperCase());
                            log.info(String.format("Successfully changed margin type of %s to %s", p.getSymbol(), activeSettings.getMarginType().toUpperCase()));
                            changed.set(true);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                });
        if(changed.get()) {
            accountService.getAccountInformation();
        }
    }

    public void changeAllLeverage() {
        Settings settings = lickHunterService.getLickHunterSettings();
        UserDefinedSettings activeSettings = lickHunterService.getActiveSettings();
        List<PositionRecord> positionRecordList = positionRepository.findByAccountId(settings.getKey());
        AtomicReference<Boolean> changed = new AtomicReference<>(false);
        positionRecordList.stream()
                .filter(positionRecord -> positionRecord.getSymbol().matches("^.*USDT$"))
                .filter(positionRecord -> positionRecord.getLeverage() != activeSettings.getLeverage().doubleValue())
                .forEach(p -> {
                    try {
                        this.changeInitialLeverage(p.getSymbol(), activeSettings.getLeverage());
                        log.info(String.format("Successfully changed leverage of %s to %s", p.getSymbol(), activeSettings.getLeverage()));
                        changed.set(true);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
        if(changed.get()) {
            accountService.getAccountInformation();
        }
    }

    public Leverage changeInitialLeverage(String symbol, int leverage) {
        Settings settings = lickHunterService.getLickHunterSettings();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        return syncRequestClient.changeInitialLeverage(symbol, leverage);
    }

    public void createTakeProfitOrders() {
        Settings settings = lickHunterService.getLickHunterSettings();
        TickerQueryTO tickerQueryTO = (TickerQueryTO) fileService.readFromFile("./", ApplicationConstants.TICKER_QUERY.getValue(), TickerQueryTO.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        List<PositionRecord> activePositions = positionRepository.findActivePositionsByAccountId(settings.getKey())
                .stream()
                .filter(positionRecord -> tickerQueryTO.getExclude().stream().noneMatch(s -> s.concat("USDT").equalsIgnoreCase(positionRecord.getSymbol())))
                .collect(Collectors.toList());
        activePositions.forEach(positionRecord -> {
            Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(positionRecord.getSymbol());
            if(syncRequestClient.getOpenOrders(positionRecord.getSymbol()).isEmpty() && symbolRecord.isPresent()) {
                List<Order> order = syncRequestClient.getAllOrders(
                        positionRecord.getSymbol(),
                        Objects.isNull(positionRecord.getOrderId()) ? null : positionRecord.getOrderId(),
                        null,
                        null,
                        null)
                        .stream()
                        .filter(o -> o.getStatus().equalsIgnoreCase(OrderState.FILLED.name())
                                && o.getType().equalsIgnoreCase(OrderType.MARKET.name()))
                        .sorted(Comparator.comparing(Order::getUpdateTime).reversed())
                        .collect(Collectors.toList());
                BigDecimal qty = order.stream()
                        .map(Order::getExecutedQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                Integer scale = BigDecimal.valueOf(symbolRecord.get().getTickSize()).stripTrailingZeros().scale();
                Double diff = BigDecimal.valueOf(Double.parseDouble(positionRecord.getEntryPrice()) * this.getPercentTakeProfit(symbolRecord.get()).doubleValue() / 100)
                        .setScale(scale, RoundingMode.HALF_DOWN).doubleValue();
                if(order.get(0).getSide().equalsIgnoreCase(OrderSide.BUY.name())) {
                    syncRequestClient.postOrder(
                            positionRecord.getSymbol(),
                            OrderSide.SELL,
                            PositionSide.BOTH,
                            OrderType.LIMIT,
                            TimeInForce.GTC,
                            String.valueOf(qty),
                            String.valueOf(BigDecimal.valueOf(Double.parseDouble(positionRecord.getEntryPrice()) + diff).setScale(scale, RoundingMode.HALF_DOWN)),
                            "true",
                            null,
                            null,
                            null,
                            NewOrderRespType.RESULT,
                            "false");
                }
                if(order.get(0).getSide().equalsIgnoreCase(OrderSide.SELL.name())) {
                    syncRequestClient.postOrder(
                            positionRecord.getSymbol(),
                            OrderSide.BUY,
                            PositionSide.BOTH,
                            OrderType.LIMIT,
                            TimeInForce.GTC,
                            String.valueOf(qty),
                            String.valueOf(BigDecimal.valueOf(Double.parseDouble(positionRecord.getEntryPrice()) - diff).setScale(scale, RoundingMode.HALF_DOWN)),
                            "true",
                            null,
                            null,
                            null,
                            NewOrderRespType.RESULT,
                            "false");
                }
            }
        });
    }

    @SneakyThrows
    public void takeProfitLimitOrders(OrderUpdate orderUpdate) {
        Settings settings = lickHunterService.getLickHunterSettings();
        TickerQueryTO tickerQueryTO = (TickerQueryTO) fileService.readFromFile("./", ApplicationConstants.TICKER_QUERY.getValue(), TickerQueryTO.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        Optional<PositionRecord> positionRecord = positionRepository.findBySymbolAndAccountId(orderUpdate.getSymbol(), settings.getKey());
        Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(orderUpdate.getSymbol());
        if(tickerQueryTO.getExclude().stream().noneMatch(s -> s.concat("USDT").equalsIgnoreCase(orderUpdate.getSymbol()))
            &&
                ((orderUpdate.getType().equalsIgnoreCase(OrderType.MARKET.name())
                    && orderUpdate.getExecutionType().equalsIgnoreCase(TransactType.TRADE.name())
                    && orderUpdate.getOrderStatus().equalsIgnoreCase(OrderState.FILLED.name())
                    && !orderUpdate.getIsReduceOnly())
//                && syncRequestClient.getOpenOrders(orderUpdate.getSymbol()).isEmpty()
                && (positionRecord.isPresent() && symbolRecord.isPresent()))) {
            //cancel open order
            syncRequestClient.cancelAllOpenOrder(orderUpdate.getSymbol());
            List<Order> order = syncRequestClient.getAllOrders(
                        orderUpdate.getSymbol(),
                        Objects.isNull(positionRecord.get().getOrderId()) ? null : positionRecord.get().getOrderId(),
                        null,
                        null,
                        null)
                    .stream()
                    .filter(o -> o.getStatus().equalsIgnoreCase(OrderState.FILLED.name())
                        && o.getType().equalsIgnoreCase(OrderType.MARKET.name()))
                    .sorted(Comparator.comparing(Order::getUpdateTime).reversed())
                    .collect(Collectors.toList());
            BigDecimal qty = order.stream()
                    .map(Order::getExecutedQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Integer scale = BigDecimal.valueOf(symbolRecord.get().getTickSize()).stripTrailingZeros().scale();
            Double diff = BigDecimal.valueOf(Double.parseDouble(positionRecord.get().getEntryPrice()) * this.getPercentTakeProfit(symbolRecord.get()).doubleValue() / 100)
                    .setScale(scale, RoundingMode.HALF_DOWN).doubleValue();
            if(orderUpdate.getSide().equalsIgnoreCase(OrderSide.BUY.name()) && orderUpdate.getType().equalsIgnoreCase(OrderType.MARKET.name())) {
                syncRequestClient.postOrder(
                        orderUpdate.getSymbol(),
                        OrderSide.SELL,
                        PositionSide.BOTH,
                        OrderType.LIMIT,
                        TimeInForce.GTC,
                        String.valueOf(qty),
                        String.valueOf(BigDecimal.valueOf(Double.parseDouble(positionRecord.get().getEntryPrice()) + diff).setScale(scale, RoundingMode.HALF_DOWN)),
                        "true",
                        null,
                        null,
                        null,
                        NewOrderRespType.RESULT,
                        "false");
            }
            if(orderUpdate.getSide().equalsIgnoreCase(OrderSide.SELL.name()) && orderUpdate.getType().equalsIgnoreCase(OrderType.MARKET.name())) {
                syncRequestClient.postOrder(
                        orderUpdate.getSymbol(),
                        OrderSide.BUY,
                        PositionSide.BOTH,
                        OrderType.LIMIT,
                        TimeInForce.GTC,
                        String.valueOf(qty),
                        String.valueOf(BigDecimal.valueOf(Double.parseDouble(positionRecord.get().getEntryPrice()) - diff).setScale(scale, RoundingMode.HALF_DOWN)),
                        "true",
                        null,
                        null,
                        null,
                        NewOrderRespType.RESULT,
                        "false");
            }
        }
    }

    @Override
    public void newOrder(String symbol, OrderSide orderSide, OrderType orderType, TimeInForce timeInForce, String qty, String price, Boolean reduceOnly, Boolean closePosition) {
        Settings settings = lickHunterService.getLickHunterSettings();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        syncRequestClient.postOrder(
                symbol,
                orderSide,
                PositionSide.BOTH,
                orderType,
                timeInForce,
                qty,
                price,
                String.valueOf(reduceOnly),
                null,
                null,
                null,
                NewOrderRespType.RESULT,
                String.valueOf(closePosition));
    }

    @SneakyThrows
    public void closeAllPositions() {
        Settings settings = lickHunterService.getLickHunterSettings();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        List<PositionRecord> positionRecords = positionRepository.findActivePositionsByAccountId(settings.getKey());
        positionRecords.forEach(positionRecord -> {
            Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(positionRecord.getSymbol());
            if(symbolRecord.isPresent()) {
                List<Order> orders = syncRequestClient.getAllOrders(
                        positionRecord.getSymbol(),
                        null,
                        null,
                        null,
                        null)
                        .stream()
                        .filter(o -> o.getStatus().equalsIgnoreCase(OrderState.FILLED.name())
                                && o.getType().equalsIgnoreCase(OrderType.MARKET.name()))
                        .sorted(Comparator.comparing(Order::getUpdateTime).reversed())
                        .collect(Collectors.toList());
                BigDecimal qty = orders.stream()
                        .map(Order::getExecutedQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                syncRequestClient.postOrder(
//                        positionRecord.getSymbol(),
//                        orders.get(0).getSide().equalsIgnoreCase(OrderSide.BUY.name()) ? OrderSide.SELL : OrderSide.BUY,
//                        PositionSide.BOTH,
//                        OrderType.MARKET,
//                        null,
//                        String.valueOf(orders.get(0).getExecutedQty()),
//                        null,
//                        null,
//                        null,
//                        null,
//                        null,
//                        NewOrderRespType.RESULT,
//                        null
//                );
            }
        });
    }

    @SneakyThrows
    public void stopLoss() {
        Settings settings = lickHunterService.getLickHunterSettings();
        accountService.getAccountInformation();
        Optional<AccountRecord> accountRecord = accountRepository.findByAccountId(settings.getKey());
        if(accountRecord.isPresent()) {
            if(accountRecord.get().getTotalUnrealizedProfit().compareTo(BigDecimal.ZERO.doubleValue()) < 0
                && BigDecimal.valueOf(accountRecord.get().getTotalWalletBalance())
                    .multiply(new BigDecimal(settings.getStoploss()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN))
                    .compareTo(BigDecimal.valueOf(accountRecord.get().getTotalUnrealizedProfit()).abs()) < 0) {
                lickHunterScheduledTasks.pauseOnClose();
                this.closeAllPositions();
            }
        }
    }

    private BigDecimal getPercentTakeProfit(SymbolRecord symbolRecord) {
        UserDefinedSettings activeSettings = lickHunterService.getActiveSettings();
        if(symbolRecord.getSixthBuy() != 0) {
            return activeSettings.getRangeSix().getPercentTakeProfit();
        } else if(symbolRecord.getFifthBuy() != 0) {
            return activeSettings.getRangeFive().getPercentTakeProfit();
        } else if(symbolRecord.getFourthBuy() != 0) {
            return activeSettings.getRangeFour().getPercentTakeProfit();
        } else if(symbolRecord.getThirdBuy() != 0) {
            return activeSettings.getRangeThree().getPercentTakeProfit();
        } else if(symbolRecord.getSecondBuy() != 0) {
            return activeSettings.getRangeTwo().getPercentTakeProfit();
        } else if(symbolRecord.getFirstBuy() != 0) {
            return activeSettings.getRangeOne().getPercentTakeProfit();
        }
        return new BigDecimal(lickHunterService.getLickHunterSettings().getTakeprofit());
    }
}
