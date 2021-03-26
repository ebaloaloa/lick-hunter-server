package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.ResponseResult;
import com.binance.client.model.trade.Leverage;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.constants.TradeConstants;
import com.lickhunter.web.entities.public_.tables.records.PositionRecord;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final FileService fileService;
    private final PositionRepository positionRepository;
    private final AccountService accountService;

    public ResponseResult marginType(String symbol, String marginType) throws Exception {
        ResponseResult result = new ResponseResult();
        try {
            Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
            SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
            result = syncRequestClient.changeMarginType(symbol, marginType);
        } catch (Exception e) {
            log.error(String.format("Error when changing margin type for symbol: %s marginType: %s | msg: %s", symbol, marginType, e.getMessage()));
        }
        return result;
    }

    public void changeAllMarginType() throws Exception {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
        List<PositionRecord> positionRecordList = positionRepository.findByAccountId(settings.getKey());
        AtomicReference<Boolean> changed = new AtomicReference<>(false);
        positionRecordList.stream()
            .filter(positionRecord -> positionRecord.getSymbol().matches("^.*USDT$"))
                .forEach(p -> {
                    if(p.getInitialMargin() == 0.0 &&
                            ((webSettings.getMarginType().equalsIgnoreCase(TradeConstants.ISOLATED.getValue()) && !p.getIsolated())
                                || (webSettings.getMarginType().equalsIgnoreCase(TradeConstants.CROSSED.getValue()) && p.getIsolated()))) {
                        try {
                            this.marginType(p.getSymbol(), webSettings.getMarginType().toUpperCase());
                            log.info(String.format("Successfully changed margin type of %s to %s", p.getSymbol(), webSettings.getMarginType().toUpperCase()));
                            changed.set(true);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                });
        if(changed.get()) {
            accountService.getAccountInformation();
        }
    }


    public Leverage changeInitialLeverage(String symbol, int leverage) throws Exception {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        return syncRequestClient.changeInitialLeverage(symbol, leverage);
    }
}