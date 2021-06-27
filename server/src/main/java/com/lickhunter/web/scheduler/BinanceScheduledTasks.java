package com.lickhunter.web.scheduler;

import com.binance.client.constant.BinanceApiConstants;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.enums.IncomeType;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.models.market.ExchangeInformation;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.services.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinanceScheduledTasks {

    private final AccountService accountService;
    private final MarketService marketService;
    private final TradeService tradeService;

    @Scheduled(fixedRateString = "${scheduler.account-information}")
    public void getAccountInformation() throws Exception {
        accountService.getAccountInformation();
    }

    @Scheduled(fixedRateString = "${scheduler.margin}")
    public void changeMarginType() throws Exception {
        tradeService.changeAllMarginType();
    }

    @Scheduled(fixedRateString = "${scheduler.leverage}")
    public void changeLeverage() throws Exception {
        tradeService.changeAllLeverage();
    }

    @Scheduled(fixedRateString = "${scheduler.liquidation}")
    public void getLiquidationData() throws Exception {
        marketService.getLiquidations();
    }

    @Scheduled(fixedRateString = "${scheduler.exchange-information}")
    public void getExchangeInformation() throws ServiceException {
        marketService.getExchangeInformation();
    }

    @Scheduled(fixedRateString = "${scheduler.ticker-price-change}")
    public void get24hrTickerPriceChange() throws Exception {
        marketService.get24hrTickerPriceChange();
    }

    @Scheduled(fixedRateString = "${scheduler.income-history}")
    public void getIncomeHistory() throws Exception {
        Arrays.stream(IncomeType.values())
                .forEach(incomeType -> {
                    try {
                        accountService.getIncomeHistory(null,
                                incomeType,
                                null,
                                null,
                                50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
