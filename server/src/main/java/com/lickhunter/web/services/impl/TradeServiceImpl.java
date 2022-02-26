package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.binance.client.exception.BinanceApiException;
import com.binance.client.model.ResponseResult;
import com.binance.client.model.enums.*;
import com.binance.client.model.trade.Leverage;
import com.binance.client.model.trade.Order;
import com.binance.client.model.user.OrderUpdate;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.UserDefinedSettings;
import com.lickhunter.web.constants.TradeConstants;
import com.lickhunter.web.entities.tables.records.AccountRecord;
import com.lickhunter.web.entities.tables.records.PositionRecord;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("telegramNotification")
    @Autowired
    private NotificationService<String> telegramService;

    public ResponseResult marginType(String symbol, String marginType) {
        ResponseResult result = new ResponseResult();
        try {
            Settings settings = lickHunterService.getLickHunterSettings();
            SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
            result = syncRequestClient.changeMarginType(symbol, marginType);
        } catch (Exception e) {
            String message = String.format("Error when changing margin type for symbol: %s marginType: %s. Exception: %s", symbol, marginType, e.getMessage());
            telegramService.send(message);
            log.error(message);
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
                            String message = String.format("Failed changing margin for %s. Exception: %s", p.getSymbol(), e.getMessage());
                            telegramService.send(message);
                            log.error(message);
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
                        String message = String.format("Failed changing leverage for %s. Exception: %s", p.getSymbol(), e.getMessage());
                        telegramService.send(message);
                        log.error(message);
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
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        List<PositionRecord> activePositions = positionRepository.findActivePositionsByAccountId(settings.getKey());
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
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        Optional<PositionRecord> positionRecord = positionRepository.findBySymbolAndAccountId(orderUpdate.getSymbol(), settings.getKey());
        Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(orderUpdate.getSymbol());
        if((orderUpdate.getType().equalsIgnoreCase(OrderType.MARKET.name())
                    && orderUpdate.getExecutionType().equalsIgnoreCase(TransactType.TRADE.name())
                    && orderUpdate.getOrderStatus().equalsIgnoreCase(OrderState.FILLED.name())
                    && !orderUpdate.getIsReduceOnly())
                && (positionRecord.isPresent() && symbolRecord.isPresent())) {
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
                Objects.nonNull(reduceOnly) ? String.valueOf(reduceOnly) : null,
                null,
                null,
                null,
                NewOrderRespType.RESULT,
                String.valueOf(closePosition));
    }

    @SneakyThrows
    public void closeAllPositions() {
        Settings settings = lickHunterService.getLickHunterSettings();
        List<PositionRecord> positionRecords = positionRepository.findActivePositionsByAccountId(settings.getKey());
        positionRecords.forEach(positionRecord -> {
            Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(positionRecord.getSymbol());
            if(symbolRecord.isPresent()) {
                this.closePosition(symbolRecord.get());
            }
        });
    }

    @SneakyThrows
    public void closePosition(SymbolRecord symbolRecord)  {
        Settings settings = lickHunterService.getLickHunterSettings();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        List<Order> orders = syncRequestClient.getAllOrders(
                symbolRecord.getSymbol(),
                null,
                null,
                null,
                null)
                .stream()
                .filter(o -> o.getStatus().equalsIgnoreCase(OrderState.FILLED.name())
                        && o.getType().equalsIgnoreCase(OrderType.MARKET.name()))
                .sorted(Comparator.comparing(Order::getUpdateTime).reversed())
                .collect(Collectors.toList());

        if(orders.isEmpty()) {
            String msg = String.format("Orders for symbol %s is empty", symbolRecord.getSymbol());
            log.error(msg);
            throw new Exception(msg);
        }
        BigDecimal qty = orders.stream()
                .map(Order::getExecutedQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        try {
            this.newOrder(
                    symbolRecord.getSymbol(),
                    orders.get(0).getSide().equalsIgnoreCase(OrderSide.SELL.name()) ? OrderSide.BUY : OrderSide.SELL,
                    OrderType.MARKET,
                    null,
                    String.valueOf(qty),
                    null,
                    true,
                    false);
        } catch (BinanceApiException e) {
            String msg = String.format("Failed closing position for symbol %s: %s", symbolRecord.getSymbol(), e.getMessage());
            log.error(msg);
            telegramService.send(msg);
        }
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
                telegramService.send("Stop Loss triggered. Closed all positions.");
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
