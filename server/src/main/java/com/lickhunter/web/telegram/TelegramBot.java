package com.lickhunter.web.telegram;

import com.binance.client.model.trade.AccountInformation;
import com.lickhunter.web.properties.MessageProperties;
import com.lickhunter.web.services.AccountService;
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
    private String userName;

    private final AccountService accountService;
    private final MessageProperties messageProperties;

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
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText() && userName.equals(update.getMessage().getFrom().getUserName())) {
            // Set variables
            SendMessage message = new SendMessage(); // Create a message object object
            Long chat_id = update.getMessage().getChatId();
            message.setChatId(chat_id.toString());
            if (update.getMessage().getText().equals("/start")) {
                message.setText(messageProperties.getTelegramCommands());
            }
            if (update.getMessage().getText().equals("/balance")) {
                AccountInformation accountInformation = accountService.getAccountInformation();
                message.setText(String.format(messageProperties.getBalance(),
                        accountInformation.getTotalWalletBalance().setScale(2, RoundingMode.HALF_UP),
                        accountInformation.getTotalUnrealizedProfit().setScale(2, RoundingMode.HALF_UP),
                        accountInformation.getTotalMaintMargin()
                                .divide(accountInformation.getTotalMarginBalance(), 2)
                                .multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP),
                        accountInformation.getPositions().stream()
                                .filter(position -> position.getInitialMargin().compareTo(new BigDecimal(0.0)) != 0)
                                .count()));
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
