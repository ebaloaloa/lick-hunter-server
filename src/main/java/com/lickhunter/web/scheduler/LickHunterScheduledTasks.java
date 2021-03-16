package com.lickhunter.web.scheduler;

import com.binance.client.model.market.PriceChangeTicker;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.models.varpairs.VarPairs;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.*;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LickHunterScheduledTasks {

    private final MarketService marketService;
    private final FileService fileService;

    public void writeToVarPairs() throws Exception {
        VarPairs varPairs = (VarPairs) fileService.readFromFile("./", ApplicationConstants.VAR_PAIRS.getValue(), VarPairs.class);
        TickerQueryTO tickerQueryTO = (TickerQueryTO) fileService.readFromFile("./", "query.json", TickerQueryTO.class);
        List<PriceChangeTicker> priceChangeTickerList = marketService.getTickerByQuery(tickerQueryTO);
        varPairs.getCoins().forEach(
                c -> priceChangeTickerList.forEach(t -> c.setVarStaticList(
                        c.getSymbol().contains(t.getSymbol().replace("USDT", ""))))
        );
        fileService.writeToFile("./","varPairs.json", varPairs);
    }

}
