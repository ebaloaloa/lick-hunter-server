package com.lickhunter.web.configs;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ConfigurationProperties(prefix = "notification")
@PropertySources({
        @PropertySource(value = "classpath:message.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:message.properties", ignoreResourceNotFound = true)
})
@With
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class MessageConfig {
    private String marginThreshold;
    private String balance;
    private String startProfit;
    private String stopProfit;
    private String startWebsocket;
    private String stopWebsocket;
    private String telegramCommands;
    private String socialVolumeAlerts;
    private String twitterVolumeAlerts;
}
