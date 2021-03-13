package com.lickhunter.web;

import com.binance.client.model.enums.CandlestickInterval;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.websockets.BinanceSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class WebApplication {

	@Autowired
	private BinanceSubscription binanceSubscription;

	@Autowired
	private MarketService marketService;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initProcess() throws ServiceException {
		binanceSubscription.subscribeCandleStickData();
		marketService.getCandleStickData(CandlestickInterval.FIFTEEN_MINUTES, 100);
	}

}
