package com.lickhunter.web.services;

import com.binance.client.model.enums.IncomeType;
import com.binance.client.model.trade.AccountInformation;
import com.binance.client.model.trade.Income;

import java.util.List;

public interface AccountService {

    AccountInformation getAccountInformation() throws Exception;
    Boolean isOpenOrderIsolationActive(String key, Double isolationPercentage) throws Exception;
    Boolean isMaxOpenActive(String key, Long maxOpen);
    List<Income> getIncomeHistory(String symbol, IncomeType incomeType, Long startTime, Long endTime, Integer limit) throws Exception;

}
