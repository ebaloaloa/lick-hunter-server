package com.lickhunter.web.services;

import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.UserDefinedSettings;

public interface LickHunterService {
    void startWebsocket();
    void stopWebsocket();
    void restart();
    UserDefinedSettings getActiveSettings();
    Settings getLickHunterSettings();
}
