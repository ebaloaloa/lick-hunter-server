package com.lickhunter.web.scheduler;

import com.binance.client.model.enums.IncomeType;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.services.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinanceScheduledTasks {

    private final AccountService accountService;
    private final MarketService marketService;
    private final TradeService tradeService;
    private final FileService<WebSettings, ?> fileService;
    private final LickHunterScheduledTasks lickHunterScheduledTasks;
    private final LickHunterService lickHunterService;

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
        lickHunterScheduledTasks.writeToCoinsJson();
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

    @Scheduled(cron = "${scheduler.daily-reinvestment:-}")
    @SneakyThrows
    public void transferFromFuturesToSpot() {
        WebSettings webSettings = lickHunterService.getWebSettings();
        if(webSettings.getDailyReinvestment().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new Exception("Daily Reinvestment Percentage should not exceed 100.");
        }
        BigDecimal amount = accountService.getDailyPnl()
                .multiply(BigDecimal.valueOf(100)
                        .subtract(webSettings.getDailyReinvestment())
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));
        if(amount.compareTo(BigDecimal.ZERO) > 0) {
            accountService.futuresTransfer("USDT", amount.doubleValue(), 2);
        }
    }

    @Scheduled(fixedRateString = "60000")
    @SneakyThrows
    public void pauseOnStopLoss() {
        tradeService.stopLoss();
    }

    @Scheduled(fixedRateString = "30000")
    @SneakyThrows
    public void createTakeProfitOrders() {
        tradeService.createTakeProfitOrders();
    }
}
