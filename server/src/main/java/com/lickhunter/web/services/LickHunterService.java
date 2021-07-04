package com.lickhunter.web.services;

import com.lickhunter.web.configs.UserDefinedSettings;

public interface LickHunterService {
    void startProfit();
    void stopProfit();
    void startWebsocket();
    void stopWebsocket();
    void restart();
    UserDefinedSettings getActiveSettings();
}
