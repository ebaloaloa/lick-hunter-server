package com.lickhunter.web;

import com.binance.client.model.enums.CandlestickInterval;
import com.lickhunter.web.controllers.ApplicationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class WebApplication {

	@Autowired
	private ApplicationController applicationController;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initProcess() throws Exception {
		applicationController.subscribeCandleStickData();
		applicationController.subscribeMarkPrice();
		//TODO add properties to enable/disable candlestickDate on application startup
		applicationController.getCandleStickData(CandlestickInterval.HOURLY, 100);
	}

}
