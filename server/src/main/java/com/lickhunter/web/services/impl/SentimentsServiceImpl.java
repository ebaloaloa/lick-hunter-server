package com.lickhunter.web.services.impl;

import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.models.sentiments.SentimentData;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.services.LickHunterService;
import com.lickhunter.web.services.NotificationService;
import com.lickhunter.web.services.SentimentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SentimentsServiceImpl implements SentimentsService {

    private final ApplicationConfig applicationConfig;
    private final LickHunterService lickHunterService;
    @Value("${sentiments.key}")
    private String key;

    public SentimentData getSentiments() {
        SentimentData result = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
            restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("user-agent", lickHunterService.getWebSettings().getUserAgent());
            HttpEntity<String> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(applicationConfig.getSentimentsApi())
                    .queryParam("data", "market")
                    .queryParam("type", "fast")
                    .queryParam("key", key);
            ResponseEntity<SentimentData> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, SentimentData.class);
            result = res.getBody();
        } catch (Exception e) {
            log.error(String.format("Failed to retrieve sentiments. %s", e.getMessage()));
        }
        return result;
    }
}
