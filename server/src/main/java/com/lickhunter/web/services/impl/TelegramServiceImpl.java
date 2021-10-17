package com.lickhunter.web.services.impl;

import com.lickhunter.web.services.NotificationService;
import com.lickhunter.web.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("telegramNotification")
@Slf4j
@RequiredArgsConstructor
public class TelegramServiceImpl implements NotificationService<String> {

    private final TelegramBot telegramBot;

    @Override
    public void send(String message) {
        telegramBot.sendNotification(message);
    }
}
