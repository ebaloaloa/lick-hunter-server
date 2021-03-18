package com.lickhunter.web.services.impl;

import com.alibaba.fastjson.JSON;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.models.webhook.DiscordWebhook;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service("discordNotification")
@Slf4j
@RequiredArgsConstructor
public class DiscordServiceImpl implements NotificationService<DiscordWebhook> {

    private final FileService fileService;

    public void send(DiscordWebhook webhook) throws Exception {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "PostmanRuntime/7.26.8");
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(webhook), headers);
        if(Objects.nonNull(settings.getDiscordwebhook())) {
            restTemplate.exchange(settings.getDiscordwebhook(), HttpMethod.POST, entity, String.class);
        }
    }
}
