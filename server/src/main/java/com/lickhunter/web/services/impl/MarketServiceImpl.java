package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.binance.client.constant.BinanceApiConstants;
import com.binance.client.exception.BinanceApiException;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import com.binance.client.model.market.PriceChangeTicker;
import com.jcabi.aspects.Async;
import com.jcabi.aspects.Cacheable;
import com.jcabi.aspects.RetryOnFailure;
import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.configs.Settings;
import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.entities.tables.records.CandlestickRecord;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
import com.lickhunter.web.models.liquidation.Liquidations;
import com.lickhunter.web.models.market.ExchangeInformation;
import com.lickhunter.web.repositories.CandlestickRepository;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.services.FileService;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.services.TechnicalIndicatorService;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Market Data Facade Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final FileService<Settings, ?> fileService;
    private final CandlestickRepository candlestickRepository;
    private final SymbolRepository symbolRepository;
    private final ApplicationConfig applicationConfig;
    private final TechnicalIndicatorService technicalIndicatorService;

    /**
     * {@inheritDoc}
     */
    public List<SymbolRecord> getTickerByQuery(TickerQueryTO query) {
        List<SymbolRecord> result;
        if(Objects.nonNull(query.getSymbol())) {
            return symbolRepository.findBySymbols(query.getSymbol());
        }
        List<SymbolRecord> symbolRecords = symbolRepository.findAll().stream()
                .filter(s -> s.getSymbol().matches("^.*USDT$"))
                .filter(s -> Objects.nonNull(s.getMarkPrice()))
                .filter(s -> Objects.nonNull(s.getVolatility()))
                .filter(s -> Objects.nonNull(s.getOnboardDate()))
                .collect(Collectors.toList());

        //get symbols by trading age
        List<SymbolRecord> symbols = symbolRecords.stream()
                .filter(s -> ChronoUnit.DAYS.between(Instant.ofEpochMilli(s.getOnboardDate()).atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()) > query.getMinimumTradingAge())
                .collect(Collectors.toList());

        result = symbols.stream()
                //Exclude
                .filter(Objects.nonNull(query.getExclude()) ?
                        t -> query.getExclude().stream().noneMatch(e -> t.getSymbol().contains(e)) :
                        t -> true)
                //Price Change Percentage
                .filter(Objects.nonNull(query.getMaxPriceChangePercent()) ?
                        t-> BigDecimal.valueOf(t.getPriceChangePercent()).abs().compareTo(query.getMaxPriceChangePercent()) < 0 :
                        t -> true)
                //Volume Limit
                .filter(Objects.nonNull(query.getVolumeLowerLimit()) && Objects.nonNull(query.getVolumeUpperLimit()) ?
                        t -> t.getQuoteVolume().compareTo(query.getVolumeLowerLimit().doubleValue()) > 0 &&
                            t.getQuoteVolume().compareTo(query.getVolumeUpperLimit().doubleValue()) < 0
                        : t -> true)
                //Bollinger Bands Strategy
                .filter(Objects.nonNull(query.getBbStrategy()) && query.getBbStrategy() ?
                        t -> {
                            Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(t.getSymbol());
                            if(symbolRecord.isPresent()) {
                                BarSeries barSeries = technicalIndicatorService.getBarSeries(symbolRecord.get().getSymbol(), CandlestickInterval.of(query.getBbTimeframe()));
                                Strategy strategy = technicalIndicatorService.bollingerBandsStrategy(barSeries, symbolRecord.get().getMarkPrice(), query.getBbBarCount());
                                return strategy.getEntryRule().isSatisfied(barSeries.getEndIndex());
                            }
                            return true;
                        } :
                        t -> true)
                //CCI Correction Strategy
                .filter(Objects.nonNull(query.getCciStrategy()) && query.getCciStrategy() ?
                        t -> {
                            Optional<SymbolRecord> symbolRecord = symbolRepository.findBySymbol(t.getSymbol());
                            if(symbolRecord.isPresent()) {
                                BarSeries barSeries = technicalIndicatorService.getBarSeries(symbolRecord.get().getSymbol(), CandlestickInterval.of(query.getCciTimeframe()));
                                Strategy strategy = technicalIndicatorService.cciCorrectionStrategy(barSeries, query.getCciBarCount());
                                return strategy.getEntryRule().isSatisfied(barSeries.getEndIndex());
                            }
                            return true;
                        } :
                        t -> true)
                .collect(Collectors.toList());

        //all time high
        if(Objects.nonNull(query.getPercentageFromAllTimeHigh())) {
            result = result.stream()
                    .filter(isNearThreshHoldAllTimeHigh(query.getPercentageFromAllTimeHigh(), symbolRecords)
                            .negate())
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void getExchangeInformation() {
        log.info("Retrieving Exchange Information");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ExchangeInformation> result = restTemplate.getForEntity(BinanceApiConstants.API_BASE_URL + "/fapi/v1/exchangeInfo", ExchangeInformation.class);
        Objects.requireNonNull(result.getBody()).getSymbols().forEach(symbolRepository::insertOrUpdate);
    }

    public void get24hrTickerPriceChange() throws Exception {
        log.info("Retrieving 24h Ticker Price Change");
        Settings settings = fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        List<PriceChangeTicker> priceChangeTickers = syncRequestClient.get24hrTickerPriceChange(null);
        priceChangeTickers.forEach(symbolRepository::insertOrUpdate);
    }

    /**
     * {@inheritDoc}
     */
    @RetryOnFailure(attempts = 5, delay = 5, unit = TimeUnit.SECONDS)
    public void getCandleStickData(CandlestickInterval interval, int limit) throws Exception {
        log.info("Retrieving candlesticks data");
        Settings settings = fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        symbolRepository.findAll()
                .stream()
                .filter(symbolRecord -> symbolRecord.getSymbol().matches("^.*USDT$"))
                .forEach(s -> {
                    List<CandlestickRecord> candlestickRecords = candlestickRepository.getCandleStickBySymbol(s.getSymbol());
                    List<Candlestick> candleStickList;
                    try {
                        candleStickList = syncRequestClient.getCandlestick(s.getSymbol(), interval, null, null, limit);
                        candleStickList.stream()
                                .filter(c -> candlestickRecords.stream()
                                        .noneMatch(cr -> c.getOpenTime().compareTo((cr.getOpenTime())) == 0))
                                .forEach(c -> candlestickRepository.insert(s.getSymbol(), c, interval));
                    } catch (BinanceApiException e) {
                        log.warn(String.format("Failed to retrieve candlestick data for symbol: %s", s.getSymbol()));
                    }
                });
        log.info("Successfully retrieved Candlestick data");
    }

    /**
     * {@inheritDoc}
     */
    @Cacheable(lifetime = 1, unit = TimeUnit.MINUTES)
    public List<SymbolRecord> getMarkPriceData() throws Exception {
        log.info("Retrieving Mark Price data");
        Settings settings = fileService.readFromFile("./", ApplicationConstants.SETTINGS.getValue(), Settings.class);
        SyncRequestClient syncRequestClient = SyncRequestClient.create(settings.getKey(), settings.getSecret());
        syncRequestClient.getMarkPrice("")
                .forEach(symbolRepository::insertOrUpdate);
        log.info("Successfully retrieved Mark Price data");
        return symbolRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Async
    public void getLiquidations() {
        log.info("Retrieving liquidation data.");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "PostmanRuntime/7.26.8");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Liquidations> liquidations = restTemplate.exchange(applicationConfig.getLiquidation(), HttpMethod.GET, entity, Liquidations.class);
        Objects.requireNonNull(liquidations.getBody()).getData()
                .forEach(d -> symbolRepository.insertOrUpdate(d));
        log.info("Successfully retrieved liquidation data.");
    }

    private Predicate<SymbolRecord> isNearThreshHoldAllTimeHigh(Long threshHold, List<SymbolRecord> markPrices) {
        return priceChangeTicker -> {
            Optional<Double> markPrice = markPrices.stream()
                    .filter(m -> m.getSymbol().contains(priceChangeTicker.getSymbol()))
                    .map(SymbolRecord::getMarkPrice)
                    .filter(Objects::nonNull)
                    .findAny();
            if(markPrice.isPresent()) {
                List<CandlestickRecord> candlestickRecords = candlestickRepository.getCandleStickBySymbol(priceChangeTicker.getSymbol());
                OptionalDouble max = candlestickRecords.stream()
                        .filter(Objects::nonNull)
                        .mapToDouble(CandlestickRecord::getHigh)
                        .max();
                if(max.isPresent()) {
                    BigDecimal percentageChange = ((BigDecimal.valueOf(markPrice.get()).subtract(BigDecimal.valueOf(max.getAsDouble())).abs()).divide(BigDecimal.valueOf(max.getAsDouble()), 2, RoundingMode.HALF_DOWN)).multiply(BigDecimal.valueOf(100));
                    return percentageChange.compareTo(BigDecimal.valueOf(threshHold)) < 0;
                }
            }
            return false;
        };
    }
}
