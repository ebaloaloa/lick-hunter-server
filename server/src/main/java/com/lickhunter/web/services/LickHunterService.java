package com.lickhunter.web.services;

import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.UserDefinedSettings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.to.TickerQueryTO;

public interface LickHunterService {
    TickerQueryTO getQuery();
    WebSettings getWebSettings();
    UserDefinedSettings getActiveSettings();
    Settings getLickHunterSettings();
}
