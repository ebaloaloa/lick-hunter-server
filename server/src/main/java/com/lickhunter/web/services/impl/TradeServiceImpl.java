package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.ResponseResult;
import com.binance.client.model.enums.*;
import com.binance.client.model.trade.Leverage;
import com.binance.client.model.trade.Order;
import com.binance.client.model.user.OrderUpdate;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.UserDefinedSettings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.constants.TradeConstants;
import com.lickhunter.web.entities.tables.records.PositionRecord;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.repositories.SymbolRepository;
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

    public ResponseResult marginType(String symbol, String marginType) {
        ResponseResult result = new ResponseResult();
        try {
            Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
            SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
            result = syncRequestClient.changeMarginType(symbol, marginType);
        } catch (Exception e) {
            log.error(String.format("Error when changing margin type for symbol: %s marginType: %s | msg: %s", symbol, marginType, e.getMessage()));
        }
        return result;
    }

    public void changeAllMarginType() {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
        UserDefinedSettings activeSettings = webSettings.getUserDefinedSettings().get(webSettings.getActive());
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
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
        UserDefinedSettings activeSettings = webSettings.getUserDefinedSettings().get(webSettings.getActive());
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
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        return syncRequestClient.changeInitialLeverage(symbol, leverage);
    }

    @SneakyThrows
    public void takeProfitLimitOrders(OrderUpdate orderUpdate, String accountId) {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        TickerQueryTO tickerQueryTO = (TickerQueryTO) fileService.readFromFile("./", ApplicationConstants.TICKER_QUERY.getValue(), TickerQueryTO.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        Optional<PositionRecord> positionRecord = positionRepository.findBySymbolAndAccountId(orderUpdate.getSymbol(), accountId);
        Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(orderUpdate.getSymbol());
        if(tickerQueryTO.getExclude().stream().noneMatch(s -> s.concat("USDT").equalsIgnoreCase(orderUpdate.getSymbol()))
            &&
                ((orderUpdate.getType().equalsIgnoreCase(OrderType.MARKET.name())
                && orderUpdate.getExecutionType().equalsIgnoreCase(TransactType.TRADE.name())
                && orderUpdate.getOrderStatus().equalsIgnoreCase(OrderState.FILLED.name())
                && !orderUpdate.getIsReduceOnly())
            || (orderUpdate.getType().equalsIgnoreCase(OrderType.LIMIT.name())
                && orderUpdate.getOrderStatus().equalsIgnoreCase(OrderState.CANCELED.name())
                && orderUpdate.getIsReduceOnly())
                && syncRequestClient.getOpenOrders(orderUpdate.getSymbol()).isEmpty()
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
