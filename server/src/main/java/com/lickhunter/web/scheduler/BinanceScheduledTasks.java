package com.lickhunter.web.scheduler;

import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.enums.IncomeType;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.services.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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

    @Scheduled(fixedRateString = "${scheduler.mark-price}")
    public void getMarkPriceData() throws Exception {
        marketService.getMarkPriceData();
    }

    @Scheduled(fixedRateString = "${scheduler.candlestick}")
    public void getCandleStickData() throws Exception {
        marketService.getCandleStickData(CandlestickInterval.HOURLY, 5);
    }

    @Scheduled(fixedRateString = "${scheduler.liquidation}")
    public void getLiquidationData() throws Exception {
        marketService.getLiquidations();
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
