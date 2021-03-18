package com.lickhunter.web.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "notification")
@PropertySource("classpath:message.properties")
@With
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class MessageProperties {
    private String marginThreshold;
}
