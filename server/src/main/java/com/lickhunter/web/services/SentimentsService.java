package com.lickhunter.web.services;


import com.lickhunter.web.models.sentiments.SentimentData;

public interface SentimentsService {
    SentimentData getSentiments(String symbol);
}
