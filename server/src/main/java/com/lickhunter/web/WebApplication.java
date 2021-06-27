package com.lickhunter.web;

import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.enums.IncomeType;
import com.lickhunter.web.controllers.ApplicationController;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.LickHunterService;
import com.lickhunter.web.services.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
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

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@PostConstruct
	public void initProcess() throws Exception {
		//TODO Major bug for websockets. Always disconnects. Data loss
//		applicationController.subscribeUserData();
		applicationController.subscribeCandleStickData();
		applicationController.subscribeMarkPrice();

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
		marketService.getCandleStickData(CandlestickInterval.WEEKLY, 200);
		marketService.getCandleStickData(CandlestickInterval.FIFTEEN_MINUTES, 20);
		lickHunterService.startWebsocket();
		lickHunterService.startProfit();
	}
}
