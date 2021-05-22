package com.lickhunter.web.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:application.properties", ignoreResourceNotFound = true)
})
@Getter
public class ApplicationConfig {
    @Value("${server.port}")
    private String serverPort;

    @Value("${api.liquidation}")
    private String liquidation;

    @Value("${sentiments.api}")
    private String sentimentsApi;

    @Value("${sentiments.social-volume-percentage}")
    private Double socialVolumePercentage;

    @Value("${sentiments.twitter-volume-percentage}")
    private Double twitterVolumePercentage;

    @Value("${sentiments.pause-bot-enable:false}")
    private Boolean pauseBotEnable;

    @Value("${sentiments.change-settings-enable:false}")
    private Boolean changeSettingsEnable;

    @Value("${sentiments.change-settings-volatility}")
    private Double changeSettingsVolatility;

    @Value("${sentiments.pause-bot}")
    private Integer pauseBotHours;

    @Value("${sentiments.enable:false}")
    private Boolean sentimentsEnable;

    @Value("${sentiments.discord-alerts}")
    private String sentimentsDiscordAlertsWebhook;

    @Value("${sentiments.discord-enable:false}")
    private Boolean sentimentsDiscordEnable;

    @Value("${sentiments.change-settings-enable}")
    private Boolean sentimentsChangeSettingsEnable;
}
