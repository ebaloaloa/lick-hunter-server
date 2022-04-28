package com.lickhunter.web.scheduler;

import com.lickhunter.web.configs.*;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.entities.tables.records.PositionRecord;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
import com.lickhunter.web.models.Coins;
import com.lickhunter.web.models.sentiments.Datum;
import com.lickhunter.web.models.sentiments.SentimentData;
import com.lickhunter.web.models.webhook.DiscordWebhook;
import com.lickhunter.web.repositories.PositionRepository;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.services.*;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class LickHunterScheduledTasks {

    private final MarketService marketService;
    private final AccountService accountService;
    private final FileService fileService;
    private final PositionRepository positionRepository;
    private final LickHunterService lickHunterService;
    private final SentimentsService sentimentsService;
    private final ApplicationConfig applicationConfig;
    private final MessageConfig messageConfig;
    private final SymbolRepository symbolRepository;
    @Qualifier("discordNotification")
    @Autowired
    private NotificationService<DiscordWebhook> notificationService;
    private AtomicBoolean isBotPaused = new AtomicBoolean(false);
    private AtomicBoolean pauseOnCloseActive = new AtomicBoolean(false);
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future;

    @SneakyThrows
    @Scheduled(fixedRateString = "${scheduler.write-coins}")
    @Retryable( value = UndeclaredThrowableException.class,
            maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 100))
    public void writeToCoinsJson() {
        Settings settings = lickHunterService.getLickHunterSettings();
        TickerQueryTO tickerQueryTO = lickHunterService.getQuery();
        UserDefinedSettings activeSettings = lickHunterService.getActiveSettings();
        List<SymbolRecord> symbolRecords = marketService.getTickerByQuery(tickerQueryTO);
        List<PositionRecord> activePositions = positionRepository.findActivePositionsByAccountId(settings.getKey());
        List<SymbolRecord> allSymbols = symbolRepository.findAll();
        allSymbols.forEach(symbolRecord -> symbolRepository.updateCoinValue(this.coinValue(symbolRecord, activeSettings)));
        if(pauseOnCloseActive.get() && activePositions.isEmpty() && !isBotPaused.get()) {
            pauseBot();
        }
        if(accountService.isMaxOpenActive(settings.getKey(), Long.valueOf(activeSettings.getMaxOpen()))
                || accountService.isOpenOrderIsolationActive(settings.getKey(), activeSettings.getOpenOrderIsolationPercentage())
                || pauseOnCloseActive.get()) {
            activePositions = activePositions.stream()
                    .filter(this.allowDca(activeSettings))
                    .collect(Collectors.toList());
            activePositions
                    .forEach(p -> symbolRepository.updateCanTrade(p.getSymbol(), Boolean.TRUE));
            List<PositionRecord> finalActivePositions = activePositions;
            allSymbols.stream()
                    .filter(symbolRecord -> finalActivePositions.stream().noneMatch(positionRecord -> symbolRecord.getSymbol().equalsIgnoreCase(positionRecord.getSymbol())))
                    .forEach(symbolRecord -> symbolRepository.updateCanTrade(symbolRecord.getSymbol(), Boolean.FALSE));
        } else {
            symbolRecords = symbolRecords
                    .stream()
                    .filter(this.allowDca(activeSettings, settings.getKey()))
                    .sorted(Comparator.comparing(SymbolRecord::getSymbol))
                    .collect(Collectors.toList());
            symbolRecords.forEach(s -> symbolRepository.updateCanTrade(s.getSymbol(), Boolean.TRUE));
            List<SymbolRecord> finalSymbolRecords = symbolRecords;
            allSymbols.stream()
                    .filter(symbolRecord -> finalSymbolRecords.stream().noneMatch(positionRecord -> symbolRecord.getSymbol().equalsIgnoreCase(positionRecord.getSymbol())))
                    .forEach(symbolRecord -> symbolRepository.updateCanTrade(symbolRecord.getSymbol(), Boolean.FALSE));
        }
    }

    @Scheduled(fixedRateString = "${scheduler.exclude-coins}")
    @SneakyThrows
    public void excludeCoins() {
        TickerQueryTO tickerQueryTO = (TickerQueryTO) fileService.readFromFile("./", ApplicationConstants.TICKER_QUERY.getValue(), TickerQueryTO.class);
        List<SymbolRecord> symbolRecords = marketService.getTickerByQuery(tickerQueryTO);
        //Auto Exclude
        if (Objects.nonNull(tickerQueryTO.getAutoExclude()) && tickerQueryTO.getAutoExclude()) {
            symbolRecords.forEach(priceChangeTicker -> {
                if(BigDecimal.valueOf(priceChangeTicker.getPriceChangePercent()).abs().compareTo(tickerQueryTO.getAutoExcludePercentage()) > 0) {
                    tickerQueryTO.getExclude().add(priceChangeTicker.getSymbol().replace("USDT",""));
                    try {
                        fileService.writeToFile("./", ApplicationConstants.TICKER_QUERY.getValue(), tickerQueryTO);
                    } catch (Exception exception) {
                        log.error("Error encountered during excluding of coins " + ApplicationConstants.TICKER_QUERY.getValue());
                    }
                }
            });
        }
    }

    @Scheduled(cron = "${scheduler.sentiments:-}")
    @Async
    public void checkSentiments() {
        if(applicationConfig.getSentimentsEnable()) {
            log.info("Checking sentiments information.");
            symbolRepository.findAll().forEach(symbolRecord -> {
                SentimentData sentimentData = sentimentsService.getSentiments(symbolRecord.getSymbol().toUpperCase().replace("USDT",""));
                if(sentimentData.getData().size() > 0) {
                    sentimentData.getData()
                            .forEach(datum -> {
                                symbolRepository.update(datum);
                                if(datum.getSymbol().equalsIgnoreCase("btc")) {
                                    this.changeSettings(datum);
                                }
                            });
                }
            });
            this.writeToCoinsJson();
        }
    }

    public void pauseOnClose() {
        if(applicationConfig.getPauseBotEnable()) {
            isBotPaused.set(false);
            pauseOnCloseActive.set(true);
            log.info("Bot will pause after all positions are closed.");
        }
    }

    public void resumeBot() {
        isBotPaused.set(false);
        pauseOnCloseActive.set(false);
        log.info("Bot is now resumed.");
    }

    public Boolean getIsBotPaused() {
        return this.isBotPaused.get();
    }

    private void pauseBot() {
        if(applicationConfig.getPauseBotEnable()) {
            if (isBotPaused.get()) {
                future.cancel(true);
            }
            isBotPaused.set(true);
            log.info(String.format("Bot is now paused. It will resume after %s hours", applicationConfig.getPauseBotHours()));
            future = executorService.schedule(this::resumeBot, applicationConfig.getPauseBotHours().longValue(), TimeUnit.HOURS);
        }
    }

    private void changeSettings(Datum datum) {
        if(applicationConfig.getSentimentsChangeSettingsEnable()) {
            WebSettings webSettings = (WebSettings) fileService.readFromFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), WebSettings.class);
            if(datum.getVolatility()
                    .compareTo(applicationConfig.getChangeSettingsVolatility())  >= 0) {
                webSettings.setActive(webSettings.getSafe());
                log.info("Changed to safe settings: " + webSettings.getSafe());
            } else {
                webSettings.setActive(webSettings.getDefaultSettings());
                log.info("Changed to default settings: " + webSettings.getDefaultSettings());
            }
            fileService.writeToFile("./", ApplicationConstants.WEB_SETTINGS.getValue(), webSettings);
        }
    }

    private void sendSentimentsDiscordNotification(DiscordWebhook webhook) throws Exception {
        if(applicationConfig.getSentimentsDiscordEnable()) {
            notificationService.send(webhook);
        }
    }

    private Coins coinValue(SymbolRecord symbolRecord, UserDefinedSettings activeSettings) {
        Coins coins = new Coins();
        coins.setSymbol(symbolRecord.getSymbol());
        if(activeSettings.getAutoOffset() && Objects.nonNull(symbolRecord.getVolatility())) {
            if(symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityOne()) < 0
                    && symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityTwo()) < 0
                    && symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityThree()) < 0) {
                coins.setLongoffset(activeSettings.getLongOffset().toString());
                coins.setShortoffset(activeSettings.getShortOffset().toString());
            }
            if(symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityOne()) > 0
                    && symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityTwo()) < 0
                    && symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityThree()) < 0) {
                coins.setLongoffset(activeSettings.getOffsetOne().toString());
                coins.setShortoffset(activeSettings.getOffsetOne().toString());
            }
            if(symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityOne()) > 0
                    && symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityTwo()) > 0
                    && symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityThree()) < 0) {
                coins.setLongoffset(activeSettings.getOffsetTwo().toString());
                coins.setShortoffset(activeSettings.getOffsetTwo().toString());
            }
            if(symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityOne()) > 0
                    && symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityTwo()) > 0
                    && symbolRecord.getVolatility().compareTo(activeSettings.getOffsetVolatilityThree()) > 0) {
                coins.setLongoffset(activeSettings.getOffsetThree().toString());
                coins.setShortoffset(activeSettings.getOffsetThree().toString());
            }
        } else {
            coins.setLongoffset(activeSettings.getLongOffset().toString());
            coins.setShortoffset(activeSettings.getShortOffset().toString());
        }
        return coins;
    }

    private Predicate<PositionRecord> allowDca(UserDefinedSettings userDefinedSettings) {
        return positionRecord -> {
            Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(positionRecord.getSymbol());
            if(symbolRecord.isPresent()) {
                BigDecimal percentageFromAverage = ((BigDecimal.valueOf(symbolRecord.get().getMarkPrice())
                        .subtract(new BigDecimal(positionRecord.getEntryPrice())).abs()).divide(new BigDecimal(positionRecord.getEntryPrice()), MathContext.DECIMAL128)).multiply(BigDecimal.valueOf(100));
                if(percentageFromAverage.compareTo(userDefinedSettings.getRangeFive().getPercentFromAverage()) > 0) {
                    return true;
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeFour().getPercentFromAverage()) > 0) {
                    return symbolRecord.get().getFifthBuy() < Long.parseLong(userDefinedSettings.getRangeFive().getNumberOfBuys());
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeThree().getPercentFromAverage()) > 0) {
                    return symbolRecord.get().getFourthBuy() < Long.parseLong(userDefinedSettings.getRangeFour().getNumberOfBuys());
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeTwo().getPercentFromAverage()) > 0) {
                    return symbolRecord.get().getThirdBuy() < Long.parseLong(userDefinedSettings.getRangeThree().getNumberOfBuys());
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeOne().getPercentFromAverage()) > 0) {
                    return symbolRecord.get().getSecondBuy() < Long.parseLong(userDefinedSettings.getRangeTwo().getNumberOfBuys());
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getDcaStart()) > 0) {
                    return symbolRecord.get().getFirstBuy() < Long.parseLong(userDefinedSettings.getRangeOne().getNumberOfBuys());
                } else {
                    return false;
                }
            }
            return true;
        };
    }

    private Predicate<SymbolRecord> allowDca(UserDefinedSettings userDefinedSettings, String accountId) {
        return symbolRecord -> {
            Optional<PositionRecord> positionRecord = positionRepository.findBySymbolAndAccountId(symbolRecord.getSymbol(), accountId);
            if(positionRecord.isPresent() && positionRecord.get().getInitialMargin() > 0.0 ) {
                BigDecimal percentageFromAverage = ((BigDecimal.valueOf(symbolRecord.getMarkPrice())
                        .subtract(new BigDecimal(positionRecord.get().getEntryPrice())).abs()).divide(new BigDecimal(positionRecord.get().getEntryPrice()), MathContext.DECIMAL128)).multiply(BigDecimal.valueOf(100));
                if(percentageFromAverage.compareTo(userDefinedSettings.getRangeFive().getPercentFromAverage()) > 0) {
                    return true;
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeFour().getPercentFromAverage()) > 0) {
                    return symbolRecord.getFifthBuy() < Long.parseLong(userDefinedSettings.getRangeFive().getNumberOfBuys());
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeThree().getPercentFromAverage()) > 0) {
                    return symbolRecord.getFourthBuy() < Long.parseLong(userDefinedSettings.getRangeFour().getNumberOfBuys());
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeTwo().getPercentFromAverage()) > 0) {
                    return symbolRecord.getThirdBuy() < Long.parseLong(userDefinedSettings.getRangeThree().getNumberOfBuys());
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeOne().getPercentFromAverage()) > 0) {
                    return symbolRecord.getSecondBuy() < Long.parseLong(userDefinedSettings.getRangeTwo().getNumberOfBuys());
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getDcaStart()) > 0) {
                    return symbolRecord.getFirstBuy() < Long.parseLong(userDefinedSettings.getRangeOne().getNumberOfBuys());
                } else {
                    return false;
                }
            }
            return true;
        };
    }
}
