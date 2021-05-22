package com.lickhunter.web.services;

import com.lickhunter.web.models.sentiments.SentimentsAsset;
import com.lickhunter.web.to.SentimentsTO;

public interface SentimentsService {
    SentimentsAsset getSentiments(SentimentsTO sentimentsTO);
}
