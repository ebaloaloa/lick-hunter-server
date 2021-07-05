package com.lickhunter.web.services;

import com.binance.client.model.ResponseResult;
import com.binance.client.model.trade.Leverage;
import com.binance.client.model.user.OrderUpdate;

public interface TradeService {
    ResponseResult marginType(String symbol, String marginType);
    void changeAllMarginType();
    void changeAllLeverage();
    Leverage changeInitialLeverage(String symbol, int leverage);
    void takeProfitLimitOrders(OrderUpdate orderUpdate, String accountId);
}
