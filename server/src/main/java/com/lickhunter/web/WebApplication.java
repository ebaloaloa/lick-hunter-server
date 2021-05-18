package com.lickhunter.web;

import com.binance.client.model.enums.CandlestickInterval;
import com.lickhunter.web.controllers.ApplicationController;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

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

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initProcess() throws Exception {
		//TODO Major bug for websockets. Always disconnects. Data loss
//		applicationController.subscribeUserData();
//		applicationController.subscribeCandleStickData();
//		applicationController.subscribeMarkPrice();

		/**
		 * BinanceScheduledTasks
		 */
		marketService.getLiquidations();
		marketService.getMarkPriceData();
		accountService.getAccountInformation();
		accountService.getIncomeHistory(null,
				null,
				LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
						.atZone(ZoneId.systemDefault())
						.toEpochSecond(),
				LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
						.atZone(ZoneId.systemDefault())
						.toEpochSecond(),
				null);
		marketService.getCandleStickData(CandlestickInterval.DAILY, 500);
	}
}
