package com.lickhunter.web.services.impl;

import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.models.sentiments.SentimentsAsset;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.services.SentimentsService;
import com.lickhunter.web.to.SentimentsTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SentimentsServiceImpl implements SentimentsService {

    private final ApplicationConfig applicationConfig;
    private final SymbolRepository symbolRepository;
    @Value("${sentiments.key}")
    private String key;

    public SentimentsAsset getSentiments(SentimentsTO sentimentsTO) {
        SentimentsAsset result = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("user-agent", "PostmanRuntime/7.26.8");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(applicationConfig.getSentimentsApi())
                    .queryParam("data", sentimentsTO.getEndpoint())
                    .queryParam("key", key)
                    .queryParam("symbol", sentimentsTO.getSymbol())
                    .queryParam("data_points", sentimentsTO.getDataPoints())
                    .queryParam("change", sentimentsTO.getChange())
                    .queryParam("interval", sentimentsTO.getInterval());

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);

            ResponseEntity<SentimentsAsset> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET , entity, SentimentsAsset.class);
            symbolRepository.update(res.getBody());
            result = res.getBody();
        } catch (Exception e) {
            log.warn(String.format("Failed to receive %s sentiments: %s", sentimentsTO.getSymbol(), e.getMessage()));
        }
        return result;
    }
}
