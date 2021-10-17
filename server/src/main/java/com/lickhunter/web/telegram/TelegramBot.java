package com.lickhunter.web.telegram;

import com.binance.client.model.trade.AccountInformation;
import com.lickhunter.web.configs.MessageConfig;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.entities.tables.records.TelegramUsersRecord;
import com.lickhunter.web.repositories.TelegramRepository;
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
    private final FileService fileService;
    private final AccountService accountService;
    private final LickHunterService lickHunterService;
    private final LickHunterScheduledTasks lickHunterScheduledTasks;
    private final TelegramRepository telegramRepository;

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
                AccountInformation accountInformation = accountService.getAccountInformation();
                message.setText(String.format(messageProperties.getBalance(),
                        accountInformation.getTotalWalletBalance().setScale(2, RoundingMode.HALF_UP),
                        accountInformation.getTotalUnrealizedProfit().setScale(2, RoundingMode.HALF_UP),
                        accountInformation.getTotalMaintMargin()
                                .divide(accountInformation.getTotalMarginBalance(), 2)
                                .multiply(BigDecimal.valueOf(100))
                                .setScale(2, RoundingMode.HALF_UP),
                        accountInformation.getPositions().stream()
                                .filter(position -> position.getInitialMargin().compareTo(BigDecimal.ZERO) != 0)
                                .count(),
                        accountService.getDailyPnl().setScale(2, RoundingMode.HALF_UP),
                        accountService.getDailyPnl()
                                .divide(accountInformation.getTotalWalletBalance(), 2)
                                .multiply(BigDecimal.valueOf(100))
                                .setScale(2, RoundingMode.HALF_UP)));
            }
            if (update.getMessage().getText().equals(Commands.STATUS)) {
                Boolean isBotPaused = lickHunterScheduledTasks.getIsBotPaused();
                message.setText(String.format("Status: %s\nSettings: %s\n",
                        isBotPaused ? "Paused" : "Running",
                        lickHunterService.getWebSettings().getActive()));
            }
            if (update.getMessage().getText().equals(Commands.PAUSE_BOT)) {
                lickHunterScheduledTasks.pauseOnClose();
                message.setText("LickHunter will pause after all positions are closed.");
            }
            if (update.getMessage().getText().equals(Commands.RESUME_BOT)) {
                lickHunterScheduledTasks.resumeBot();
                message.setText("LickHunter will resume trading.");
            }
            if (update.getMessage().getText().contains(Commands.SETTINGS)) {
                message.setText("Settings not found.");
                WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
                webSettings.getUserDefinedSettings().forEach((s, userDefinedSettings) -> {
                    if(s.equals(update.getMessage().getText().split(" ")[1])) {
                        webSettings.setActive(s);
                        webSettings.setDefaultSettings(s);
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
            telegramRepository.insertOrUpdate(update.getMessage().getFrom().getUserName(), chat_id);
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                log.error(String.format("Failed sending telegram response: %s", e.getMessage()));
            }
        }
    }

    public void sendNotification(String notification) {
        Stream.of(userName).forEach(s -> {
            Optional<TelegramUsersRecord> telegramUsersRecord = telegramRepository.findByUsername(s);
            if(telegramUsersRecord.isPresent()) {
                SendMessage message = new SendMessage();
                message.setText(notification);
                message.setChatId(telegramUsersRecord.get().getChatId().toString());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.error(String.format("Failed sending notification: %s", e.getMessage()));
                }
            }
        });
    }
}
