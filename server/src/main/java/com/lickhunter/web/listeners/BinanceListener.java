package com.lickhunter.web.listeners;

import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.constants.UserDataEventConstants;
import com.lickhunter.web.entities.public_.tables.records.AccountRecord;
import com.lickhunter.web.events.BinanceEvents;
import com.lickhunter.web.models.webhook.DiscordWebhook;
import com.lickhunter.web.properties.MessageProperties;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final NotificationService notificationService;
    private final MessageProperties message;

    @SneakyThrows
    @Override
    public void onApplicationEvent(BinanceEvents event) {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
        Optional<AccountRecord> accountRecord = accountRepository.findByAccountId(settings.getKey());
        switch(UserDataEventConstants.valueOf(event.getMessage())) {
            case ACCOUNT_UPDATE:
//                lickHunterScheduledTasks.writeToCoinsJson();
                if(accountRecord.isPresent()
                    && accountService.isOpenOrderIsolationActive(settings.getKey(),
                        webSettings.getMarginPercentNotification().doubleValue())) {
                    DiscordWebhook webhook = new DiscordWebhook();
                    webhook.setContent(String.format(message.getMarginThreshold(),
                            webSettings.getMarginPercentNotification().toString(),
                            accountRecord.get().getTotalInitialMargin(),
                            accountRecord.get().getTotalMarginBalance()));
                    notificationService.send(webhook);
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
