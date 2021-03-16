package com.lickhunter.web;

import com.lickhunter.web.controllers.ApplicationController;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class WebApplication {

	@Autowired
	private ApplicationController applicationController;

	@Autowired
	private LickHunterScheduledTasks lickHunterScheduledTasks;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initProcess() throws Exception {
		applicationController.subscribeCandleStickData();
		applicationController.subscribeMarkPrice();
		applicationController.subscribeUserData();
		//TODO create configuration to allow execution of below api requests on application startup
		applicationController.getAccountInformation();
		applicationController.getMarkPriceData();
		applicationController.getCandleStickData("HOURLY", "100");
	}
}
