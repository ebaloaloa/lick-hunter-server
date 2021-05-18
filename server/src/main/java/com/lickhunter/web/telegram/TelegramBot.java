package com.lickhunter.web.telegram;

import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.entities.public_.tables.records.AccountRecord;
import com.lickhunter.web.entities.public_.tables.records.PositionRecord;
import com.lickhunter.web.properties.MessageProperties;
import com.lickhunter.web.repositories.AccountRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
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

    private final MessageProperties messageProperties;
    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final FileService fileService;
    private final AccountService accountService;

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
            if (update.getMessage().getText().equals("/start")) {
                message.setText(messageProperties.getTelegramCommands());
            }
            if (update.getMessage().getText().equals("/balance")) {
                Optional<AccountRecord> accountRecord = accountRepository.findByAccountId(settings.getKey());
                List<PositionRecord> positionRecords = positionRepository.findByAccountId(settings.getKey());
                if(accountRecord.isPresent()) {
                    message.setText(String.format(messageProperties.getBalance(),
                            BigDecimal.valueOf(accountRecord.get().getTotalWalletBalance()).setScale(2, RoundingMode.HALF_UP),
                            BigDecimal.valueOf(accountRecord.get().getTotalUnrealizedProfit()).setScale(2, RoundingMode.HALF_UP),
                            BigDecimal.valueOf(accountRecord.get().getTotalMaintenanceMargin())
                                    .divide(BigDecimal.valueOf(accountRecord.get().getTotalMarginBalance()), 2)
                                    .multiply(BigDecimal.valueOf(100))
                                    .setScale(2, RoundingMode.HALF_UP),
                            positionRecords.stream()
                                    .filter(position -> position.getInitialMargin().compareTo(0.0) != 0)
                                    .count(),
                            accountService.getDailyPnl().setScale(2, RoundingMode.HALF_UP),
                            accountService.getDailyPnl()
                                    .divide(BigDecimal.valueOf(accountRecord.get().getTotalWalletBalance()), 2)
                                    .multiply(BigDecimal.valueOf(100))
                                    .setScale(2, RoundingMode.HALF_UP)));
                }
            }
            if (update.getMessage().getText().equals("/startprofit")) {
                Runtime.getRuntime().exec("Start Profit.cmd");
                message.setText(messageProperties.getStartProfit());
            }
            if (update.getMessage().getText().equals("/stopprofit")) {
                Runtime.getRuntime().exec("Stop Profit.cmd");
                message.setText(messageProperties.getStopProfit());
            }
            if (update.getMessage().getText().equals("/startwebsocket")) {
                Runtime.getRuntime().exec("Start Websocket.cmd");
                message.setText(messageProperties.getStartWebsocket());
            }
            if (update.getMessage().getText().equals("/stopwebsocket")) {
                Runtime.getRuntime().exec("Stop Websocket.cmd");
                message.setText(messageProperties.getStopWebsocket());
            }
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
