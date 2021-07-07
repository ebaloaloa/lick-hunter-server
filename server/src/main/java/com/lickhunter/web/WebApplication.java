package com.lickhunter.web;

import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.enums.IncomeType;
import com.lickhunter.web.controllers.ApplicationController;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.*;
import com.lickhunter.web.websockets.BinanceSubscription;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class WebApplication {

	@Autowired
	private AccountService accountService;

	@Autowired
	private MarketService marketService;

	@Autowired
	private LickHunterScheduledTasks lickHunterScheduledTasks;

	@Autowired
	private ApplicationController applicationController;

	@Autowired
	private LickHunterService lickHunterService;

	@Autowired
	private WatchService watchService;

	@Autowired
	private BinanceSubscription binanceSubscription;

	@Autowired
	private TechnicalIndicatorService technicalIndicatorService;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@PostConstruct
	public void initProcess() throws Exception {
		applicationController.subscribeCandleStickData();
		applicationController.subscribeMarkPrice();
		applicationController.subscribeUserData();

		/**
		 * BinanceScheduledTasks
		 */
		accountService.getAccountInformation();
		marketService.getExchangeInformation();
		marketService.get24hrTickerPriceChange();
		marketService.getLiquidations();
		Arrays.stream(IncomeType.values())
				.forEach(incomeType -> {
					try {
						accountService.getIncomeHistory(null,
								incomeType,
								null,
								null,
								1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
		lickHunterScheduledTasks.checkSentiments();
		marketService.getCandleStickData(CandlestickInterval.WEEKLY, 200);
		marketService.getCandleStickData(CandlestickInterval.FIFTEEN_MINUTES, 20);
		marketService.getCandleStickData(CandlestickInterval.HOURLY, 20);
		binanceSubscription.subscribeLiquidation();
	}

	@EventListener(ApplicationReadyEvent.class)
	@SneakyThrows
	public void startWatcher() {
		watchService.fileWatcher();
	}
}
