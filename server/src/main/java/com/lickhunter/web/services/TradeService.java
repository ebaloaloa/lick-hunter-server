package com.lickhunter.web.services;

import com.binance.client.model.ResponseResult;
import com.binance.client.model.trade.Leverage;

public interface TradeService {
    ResponseResult marginType(String symbol, String marginType) throws Exception;
    void changeAllMarginType() throws Exception;
    void changeAllLeverage() throws Exception;
    Leverage changeInitialLeverage(String symbol, int leverage) throws Exception;
}
