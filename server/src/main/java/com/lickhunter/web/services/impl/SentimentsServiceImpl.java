package com.lickhunter.web.services.impl;

import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.models.sentiments.SentimentData;
import com.lickhunter.web.services.SentimentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class SentimentsServiceImpl implements SentimentsService {

    private final ApplicationConfig applicationConfig;
    @Value("${sentiments.key}")
    private String key;

    public SentimentData getSentiments(String symbol) {
        SentimentData result = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
            restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(applicationConfig.getSentimentsApi())
                    .queryParam("data", "assets")
                    .queryParam("symbol", symbol);
            ResponseEntity<SentimentData> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, SentimentData.class);
            result = res.getBody();
        } catch (Exception e) {
            log.error(String.format("Failed to retrieve sentiments. %s", e.getMessage()));
        }
        return result;
    }
}
