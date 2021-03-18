package com.lickhunter.web.scheduler;

import com.binance.client.model.market.PriceChangeTicker;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.configs.WebSettings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.entities.public_.tables.records.PositionRecord;
import com.lickhunter.web.models.Coins;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.services.AccountService;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class LickHunterScheduledTasks {

    private final MarketService marketService;
    private final AccountService accountService;
    private final FileService fileService;
    private final PositionRepository positionRepository;

    //TODO refactor this after dependency from varPairs is removed
    @Scheduled(fixedDelay = 1000 * 3)
    @Synchronized
    public void writeToCoinsJson() throws Exception {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        TickerQueryTO tickerQueryTO = (TickerQueryTO) fileService.readFromFile("./", ApplicationConstants.TICKER_QUERY.getValue(), TickerQueryTO.class);
        WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
        List<Coins> coinsList = new ArrayList<>();
        if(accountService.isMaxOpenActive(settings.getKey(), Long.valueOf(webSettings.getMaxOpen()))
                || accountService.isOpenOrderIsolationActive(settings.getKey(), Double.valueOf(webSettings.getOpenOrderIsolationPercentage()))) {
            List<PositionRecord> positionRecords = positionRepository.findActivePositionsByAccountId(settings.getKey());
            positionRecords
                    .forEach(p -> {
                        Coins coins = new Coins();
                        coins.setSymbol(p.getSymbol().replace("USDT",""));
                        coins.setLickvalue(webSettings.getLickValue().toString());
                        coins.setLongoffset(webSettings.getLongOffset().toString());
                        coins.setShortoffset(webSettings.getShortOffset().toString());
                        coinsList.add(coins);
                    });
        } else {
            List<PriceChangeTicker> priceChangeTickers = marketService.getTickerByQuery(tickerQueryTO);
            priceChangeTickers
                    .stream()
                    .sorted(Comparator.comparing(PriceChangeTicker::getSymbol))
                    .forEach(priceChangeTicker -> {
                        Coins coins = new Coins();
                        coins.setSymbol(priceChangeTicker.getSymbol().replace("USDT",""));
                        coins.setLickvalue(webSettings.getLickValue().toString());
                        coins.setLongoffset(webSettings.getLongOffset().toString());
                        coins.setShortoffset(webSettings.getShortOffset().toString());
                        coinsList.add(coins);
                    });
        }
        fileService.writeToFile("./", ApplicationConstants.COINS.getValue(), coinsList);
    }
}
