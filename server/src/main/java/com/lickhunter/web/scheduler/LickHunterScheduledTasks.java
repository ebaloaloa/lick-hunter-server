package com.lickhunter.web.scheduler;

import com.binance.client.model.market.PriceChangeTicker;
import com.lickhunter.web.configs.*;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.entities.lickhunterdb.tables.records.CoinsRecord;
import com.lickhunter.web.entities.lickhunterdb.tables.records.PositionRecord;
import com.lickhunter.web.models.Coins;
import com.lickhunter.web.models.sentiments.SentimentsAsset;
import com.lickhunter.web.models.sentiments.TimeSeries;
import com.lickhunter.web.models.webhook.DiscordWebhook;
import com.lickhunter.web.repositories.CoinsRepository;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.services.*;
import com.lickhunter.web.to.SentimentsTO;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class LickHunterScheduledTasks {

    private final MarketService marketService;
    private final AccountService accountService;
    private final FileService fileService;
    private final PositionRepository positionRepository;
    private final CoinsRepository coinsRepository;
    private final LickHunterService lickHunterService;
    private final SentimentsService sentimentsService;
    private final ApplicationConfig applicationConfig;
    private final MessageConfig messageConfig;
    @Qualifier("discordNotification")
    private final NotificationService<DiscordWebhook> notificationService;
    public static AtomicBoolean restartEnabled = new AtomicBoolean(true);
    private AtomicBoolean isBotPaused = new AtomicBoolean(false);
    private AtomicBoolean pauseOnCloseActive = new AtomicBoolean(false);
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Future<?> future;

    @Scheduled(fixedRateString = "${scheduler.write-coins}")
    @Synchronized
    public void writeToCoinsJson() throws Exception {
        Settings settings = (Settings) fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        TickerQueryTO tickerQueryTO = (TickerQueryTO) fileService.readFromFile("./", ApplicationConstants.TICKER_QUERY.getValue(), TickerQueryTO.class);
        WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
        UserDefinedSettings activeSettings = webSettings.getUserDefinedSettings().get(webSettings.getActive());
        List<Coins> coinsList = new ArrayList<>();
        List<PriceChangeTicker> priceChangeTickers = marketService.getTickerByQuery(tickerQueryTO);
        List<PositionRecord> positionRecords = positionRepository.findActivePositionsByAccountId(settings.getKey());
        if(pauseOnCloseActive.get() && positionRecords.isEmpty()) {
            pauseBot();
        }
        if(accountService.isMaxOpenActive(settings.getKey(), Long.valueOf(activeSettings.getMaxOpen()))
                || accountService.isOpenOrderIsolationActive(settings.getKey(), activeSettings.getOpenOrderIsolationPercentage())
                || pauseOnCloseActive.get()) {
            positionRecords
                    .forEach(p -> {
                        Coins coins = new Coins();
                        coins.setSymbol(p.getSymbol().replace("USDT",""));
                        Optional<CoinsRecord> coinsRecord = coinsRepository.findBySymbol(coins.getSymbol());
                        if(Objects.nonNull(activeSettings.getAutoLickValue()) &&
                                activeSettings.getAutoLickValue() &&
                                coinsRecord.isPresent() &&
                                Objects.nonNull(coinsRecord.get().getLickValue())) {
                            coins.setLickvalue(coinsRecord.get().getLickValue().toString());
                        } else {
                            coins.setLickvalue(activeSettings.getLickValue().toString());
                        }
                        coins.setLongoffset(activeSettings.getLongOffset().toString());
                        coins.setShortoffset(activeSettings.getShortOffset().toString());
                        coinsList.add(coins);
                    });
        } else {
            priceChangeTickers
                    .stream()
                    .sorted(Comparator.comparing(PriceChangeTicker::getSymbol))
                    .forEach(priceChangeTicker -> {
                        Coins coins = new Coins();
                        coins.setSymbol(priceChangeTicker.getSymbol().replace("USDT",""));
                        Optional<CoinsRecord> coinsRecord = coinsRepository.findBySymbol(coins.getSymbol());
                        if(Objects.nonNull(activeSettings.getAutoLickValue()) &&
                                activeSettings.getAutoLickValue() &&
                                coinsRecord.isPresent() &&
                                Objects.nonNull(coinsRecord.get().getLickValue())) {
                            coins.setLickvalue(coinsRecord.get().getLickValue().toString());
                        } else {
                            coins.setLickvalue(activeSettings.getLickValue().toString());
                        }
                        coins.setLongoffset(activeSettings.getLongOffset().toString());
                        coins.setShortoffset(activeSettings.getShortOffset().toString());
                        coinsList.add(coins);
                    });
        }
        //Auto Exclude
        if (Objects.nonNull(tickerQueryTO.getAutoExclude()) && tickerQueryTO.getAutoExclude()) {
            priceChangeTickers.forEach(priceChangeTicker -> {
                if(priceChangeTicker.getPriceChangePercent().abs().compareTo(tickerQueryTO.getAutoExcludePercentage()) > 0) {
                    tickerQueryTO.getExclude().add(priceChangeTicker.getSymbol().replace("USDT",""));
                }
            });
        }
        fileService.writeToFile("./", ApplicationConstants.COINS.getValue(), coinsList);
        fileService.writeToFile("./", ApplicationConstants.TICKER_QUERY.getValue(), tickerQueryTO);
    }

    @Scheduled(cron = "${scheduler.lickhunter:-}")
    @Synchronized
    public void restartLickHunter() {
        if(restartEnabled.get()) {
            lickHunterService.restart();
        }
    }

    @Scheduled(cron = "${scheduler.sentiments:-}")
    @Synchronized
    public void checkSentiments() throws Exception {
        if(applicationConfig.getSentimentsEnable()) {
            log.info("Checking sentiments information.");
            SentimentsTO assets = new SentimentsTO()
                    .withEndpoint("assets")
                    .withChange("1h")
                    .withInterval("hour")
                    .withDataPoints(1)
                    .withSymbol("BTC");
            SentimentsAsset sentimentsAsset = sentimentsService.getSentiments(assets);
            if(!sentimentsAsset.getData().isEmpty()) {
                socialVolumeAlert(sentimentsAsset);
                twitterVolumeAlert(sentimentsAsset);
                changeSettings(sentimentsAsset);
            }
        }
    }

    private void socialVolumeAlert(SentimentsAsset sentimentsAsset) throws Exception {
        TimeSeries current = sentimentsAsset.getData().get(0).getTimeSeries().get(0);
        TimeSeries previous = sentimentsAsset.getData().get(0).getTimeSeries().get(1);
        if(current.getSocialVolume().compareTo(previous.getSocialVolume()) > 0) {
            BigDecimal socialVolumeChange = BigDecimal.valueOf(((current.getSocialVolume().doubleValue() -
                    previous.getSocialVolume().doubleValue()) /
                    previous.getSocialVolume().doubleValue()) *
                    100D)
                    .setScale(2, RoundingMode.HALF_UP);
            if(socialVolumeChange.compareTo(BigDecimal.valueOf(applicationConfig.getSocialVolumePercentage())) > 0) {
                DiscordWebhook webhook = new DiscordWebhook();
                webhook.setWebhook(applicationConfig.getSentimentsDiscordAlertsWebhook());
                webhook.setContent(String.format(messageConfig.getSocialVolumeAlerts(),
                        socialVolumeChange,
                        previous.getSocialVolume(),
                        current.getSocialVolume(),
                        "Bitcoin (BTC)",
                        applicationConfig.getSocialVolumePercentage()));
                sendSentimentsDiscordNotification(webhook);
                pauseOnCloseActive.set(true);
            }
        }
    }

    private void twitterVolumeAlert(SentimentsAsset sentimentsAsset) throws Exception {
        TimeSeries current = sentimentsAsset.getData().get(0).getTimeSeries().get(0);
        TimeSeries previous = sentimentsAsset.getData().get(0).getTimeSeries().get(1);
        if(current.getTweets().compareTo(previous.getTweets()) > 0) {
            BigDecimal tweetChange = BigDecimal.valueOf(((current.getTweets().doubleValue() -
                    previous.getTweets().doubleValue()) /
                    previous.getTweets().doubleValue()) *
                    100D)
                    .setScale(2, RoundingMode.HALF_UP);
            if(tweetChange.compareTo(BigDecimal.valueOf(applicationConfig.getTwitterVolumePercentage())) > 0) {
                DiscordWebhook webhook = new DiscordWebhook();
                webhook.setWebhook(applicationConfig.getSentimentsDiscordAlertsWebhook());
                webhook.setContent(String.format(messageConfig.getTwitterVolumeAlerts(),
                        tweetChange,
                        previous.getTweets(),
                        current.getTweets(),
                        "Bitcoin (BTC)",
                        applicationConfig.getTwitterVolumePercentage()));
                sendSentimentsDiscordNotification(webhook);
                pauseOnCloseActive.set(true);
            }
        }
    }

    private void pauseBot() {
        if(applicationConfig.getPauseBotEnable()) {
            if (isBotPaused.get()) {
                future.cancel(true);
            }
            restartEnabled.set(false);
            lickHunterService.stopProfit();
            lickHunterService.stopWebsocket();
            isBotPaused.set(true);
            log.info(String.format("Bot paused. It will resume after %s hours", applicationConfig.getPauseBotHours()));
            future = executorService.schedule(() -> {
                restartEnabled.set(true);
                lickHunterService.startProfit();
                lickHunterService.startWebsocket();
                isBotPaused.set(false);
                pauseOnCloseActive.set(false);
                log.info("Bot is now resumed.");
            }, applicationConfig.getPauseBotHours(), TimeUnit.HOURS);
        }
    }

    private void changeSettings(SentimentsAsset sentimentsAsset) throws Exception {
        if(applicationConfig.getSentimentsChangeSettingsEnable()) {
            WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
            if(sentimentsAsset.getData().get(0).getVolatility()
                    .compareTo(applicationConfig.getChangeSettingsVolatility())  >= 0) {
                webSettings.setActive(webSettings.getSafe());
            } else {
                webSettings.setActive(webSettings.getDefaultSettings());
            }
            fileService.writeToFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), webSettings);
        }
    }

    private void sendSentimentsDiscordNotification(DiscordWebhook webhook) throws Exception {
        if(applicationConfig.getSentimentsDiscordEnable()) {
            notificationService.send(webhook);
        }
    }
}
