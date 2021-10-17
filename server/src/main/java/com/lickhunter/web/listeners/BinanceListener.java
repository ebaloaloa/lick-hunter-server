package com.lickhunter.web.listeners;

import com.lickhunter.web.configs.MessageConfig;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.UserDefinedSettings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.constants.UserDataEventConstants;
import com.lickhunter.web.entities.tables.records.AccountRecord;
import com.lickhunter.web.events.BinanceEvents;
import com.lickhunter.web.models.webhook.DiscordWebhook;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinanceListener implements ApplicationListener<BinanceEvents> {

    private final LickHunterScheduledTasks lickHunterScheduledTasks;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final FileService fileService;
    @Qualifier("discordNotification")
    @Autowired
    private NotificationService notificationService;
    private final MessageConfig message;
    @Qualifier("telegramNotification")
    @Autowired
    private NotificationService<String> telegramService;
    @Value("${telegram.notification.isolationActive}")
    private Boolean tgNotifIsolationActive;
    @Value("${telegram.notification.maxPosActive}")
    private Boolean tgNotifMaxPosActive;

    @SneakyThrows
    @Override
    public void onApplicationEvent(BinanceEvents event) {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
        UserDefinedSettings activeSettings = webSettings.getUserDefinedSettings().get(webSettings.getActive());
        Optional<AccountRecord> accountRecord = accountRepository.findByAccountId(settings.getKey());
        switch(UserDataEventConstants.valueOf(event.getMessage())) {
            case ACCOUNT_UPDATE:
                if(accountRecord.isPresent()){
                    if(accountService.isOpenOrderIsolationActive(settings.getKey(),
                            activeSettings.getMarginPercentNotification().doubleValue())) {
                        DiscordWebhook webhook = new DiscordWebhook();
                        webhook.setWebhook(settings.getDiscordwebhook());
                        webhook.setContent(String.format(message.getMarginThreshold(),
                                activeSettings.getMarginPercentNotification().toString(),
                                accountRecord.get().getTotalInitialMargin(),
                                accountRecord.get().getTotalMarginBalance()));
                        notificationService.send(webhook);
                        telegramService.send(String.format(message.getMarginThreshold(),
                                activeSettings.getMarginPercentNotification().toString(),
                                accountRecord.get().getTotalInitialMargin(),
                                accountRecord.get().getTotalMarginBalance()));
                    }
                    if(tgNotifIsolationActive && accountService.isOpenOrderIsolationActive(settings.getKey(), activeSettings.getOpenOrderIsolationPercentage())) {
                        telegramService.send(String.format("Order Isolation reached %s percent", activeSettings.getOpenOrderIsolationPercentage()));
                    }
                    if(tgNotifMaxPosActive && accountService.isMaxOpenActive(settings.getKey(), activeSettings.getMaxOpen().longValue())) {
                        telegramService.send(String.format("Maximum allowed position of %s active", activeSettings.getMaxOpen()));
                    }
                }
                break;
            case MARGIN_CALL:
            case ORDER_TRADE_UPDATE:
            case ACCOUNT_CONFIG_UPDATE:
            default:
                //do nothing
                break;
        }
    }
}
