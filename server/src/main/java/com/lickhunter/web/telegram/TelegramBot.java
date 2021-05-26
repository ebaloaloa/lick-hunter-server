package com.lickhunter.web.telegram;

import com.lickhunter.web.configs.MessageConfig;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.entities.tables.records.AccountRecord;
import com.lickhunter.web.entities.tables.records.PositionRecord;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.LickHunterService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(prefix = "telegram", name ="enable", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.token}")
    private String token;

    @Value("${telegram.bot}")
    private String botUserName;

    @Value("${telegram.username}")
    private String[] userName;

    private final MessageConfig messageProperties;
    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final FileService fileService;
    private final AccountService accountService;
    private final LickHunterService lickHunterService;

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText() &&
                Stream.of(userName)
                        .anyMatch(u -> u.equals(update.getMessage().getFrom().getUserName()))) {
            // Set variables
            SendMessage message = new SendMessage(); // Create a message object object
            Long chat_id = update.getMessage().getChatId();
            message.setChatId(chat_id.toString());
            if (update.getMessage().getText().equals(Commands.startCommand)) {
                message.setText(messageProperties.getTelegramCommands());
            }
            if (update.getMessage().getText().equals("/balance")) {
                Optional<AccountRecord> accountRecord = accountRepository.findByAccountId(settings.getKey());
                List<PositionRecord> positionRecords = positionRepository.findByAccountId(settings.getKey());
                accountRecord.ifPresent(record -> message.setText(String.format(messageProperties.getBalance(),
                        BigDecimal.valueOf(record.getTotalWalletBalance()).setScale(2, RoundingMode.HALF_UP),
                        BigDecimal.valueOf(record.getTotalUnrealizedProfit()).setScale(2, RoundingMode.HALF_UP),
                        BigDecimal.valueOf(record.getTotalMaintenanceMargin())
                                .divide(BigDecimal.valueOf(record.getTotalMarginBalance()), 2)
                                .multiply(BigDecimal.valueOf(100))
                                .setScale(2, RoundingMode.HALF_UP),
                        positionRecords.stream()
                                .filter(position -> position.getInitialMargin().compareTo(0.0) != 0)
                                .count(),
                        accountService.getDailyPnl().setScale(2, RoundingMode.HALF_UP),
                        accountService.getDailyPnl()
                                .divide(BigDecimal.valueOf(record.getTotalWalletBalance()), 2)
                                .multiply(BigDecimal.valueOf(100))
                                .setScale(2, RoundingMode.HALF_UP))));
            }
            if (update.getMessage().getText().equals(Commands.START_PROFIT)) {
                lickHunterService.startProfit();
                message.setText(messageProperties.getStartProfit());
            }
            if (update.getMessage().getText().equals(Commands.STOP_PROFIT)) {
                lickHunterService.stopProfit();
                message.setText(messageProperties.getStopProfit());
            }
            if (update.getMessage().getText().equals(Commands.START_WEBSOCKET)) {
                lickHunterService.startWebsocket();
                message.setText(messageProperties.getStartWebsocket());
            }
            if (update.getMessage().getText().equals(Commands.STOP_WEBSOCKET)) {
                lickHunterService.stopWebsocket();
                message.setText(messageProperties.getStopWebsocket());
            }
            if (update.getMessage().getText().equals(Commands.ENABLE_RESTART)) {
                LickHunterScheduledTasks.restartEnabled.set(true);
                message.setText("LickHunter scheduled restart enabled.");
            }
            if (update.getMessage().getText().equals(Commands.DISABLE_RESTART)) {
                LickHunterScheduledTasks.restartEnabled.set(false);
                message.setText("LickHunter scheduled restart disabled.");
            }
            if (update.getMessage().getText().contains(Commands.SETTINGS)) {
                message.setText("Settings not found.");
                WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
                webSettings.getUserDefinedSettings().forEach((s, userDefinedSettings) -> {
                    if(s.equals(update.getMessage().getText().split(" ")[1])) {
                        webSettings.setActive(s);
                        try {
                            fileService.writeToFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), webSettings);
                            message.setText("Successfully changed settings to " + s);
                        } catch (Exception e) {
                            message.setText("Error encountered writing new settings.");
                            log.error("Error writing settings. " + e.getMessage());
                        }
                    }
                });
            }
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
