package com.lickhunter.web.services;

import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.UserDefinedSettings;
import com.lickhunter.web.to.TickerQueryTO;

public interface LickHunterService {
    void startWebsocket();
    void stopWebsocket();
    void restart();
    TickerQueryTO getQuery();
    UserDefinedSettings getActiveSettings();
    Settings getLickHunterSettings();
}
