package com.lickhunter.web.services.impl;

import com.lickhunter.web.configs.MessageConfig;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.UserDefinedSettings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.models.Coins;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.LickHunterService;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LickHunterServiceImpl implements LickHunterService {

    private final MessageConfig messageConfig;
    private final FileService fileService;

    public TickerQueryTO getQuery() {
        TickerQueryTO tickerQueryTO = (TickerQueryTO) fileService.readFromFile("./", ApplicationConstants.TICKER_QUERY.getValue(), TickerQueryTO.class);
        return tickerQueryTO;
    }

    @Override
    public WebSettings getWebSettings() {
        WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
        return webSettings;
    }

    public UserDefinedSettings getActiveSettings() {
        WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
        return webSettings.getUserDefinedSettings().get(webSettings.getActive());
    }

    public Settings getLickHunterSettings() {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        return settings;
    }

    public List<Coins> getCoins() {
        Coins[] coins = (Coins[]) fileService.readFromFile("./", ApplicationConstants.COINS.getValue(), Coins[].class);
        return Arrays.asList(coins);
    }
}
