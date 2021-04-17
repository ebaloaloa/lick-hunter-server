package com.lickhunter.web.properties;

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

}
