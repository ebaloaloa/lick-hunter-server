package com.lickhunter.web.services;

import com.binance.client.model.ResponseResult;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.trade.Leverage;
import com.binance.client.model.user.OrderUpdate;
import com.lickhunter.web.entities.tables.records.SymbolRecord;

public interface TradeService {
    ResponseResult marginType(String symbol, String marginType) throws Exception;
    void changeAllMarginType();
    void changeAllLeverage();
    Leverage changeInitialLeverage(String symbol, int leverage);
    void takeProfitLimitOrders(OrderUpdate orderUpdate);
    void newOrder(String symbol, OrderSide orderSide, OrderType orderType, TimeInForce timeInForce, String qty, String price, Boolean reduceOnly, Boolean closePosition);
    void closeAllPositions();
    void closePosition(SymbolRecord symbolRecord);
    void stopLoss();
    void createTakeProfitOrders();
}
