package com.lickhunter.web.services.impl;

import com.binance.client.SyncRequestClient;
import com.binance.client.constant.BinanceApiConstants;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import com.binance.client.model.market.PriceChangeTicker;
import com.jcabi.aspects.Cacheable;
import com.jcabi.aspects.RetryOnFailure;
import com.lickhunter.web.configs.ApplicationConfig;
import com.lickhunter.web.entities.public_.tables.records.CandlestickRecord;
import com.lickhunter.web.entities.public_.tables.records.SymbolRecord;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.models.market.ExchangeInformation;
import com.lickhunter.web.models.market.Symbol;
import com.lickhunter.web.repositories.CandlestickRepository;
import com.lickhunter.web.repositories.SymbolRepository;
import com.lickhunter.web.services.MarketService;
import com.lickhunter.web.to.TickerQueryTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class MarketServiceImpl implements MarketService {

    private final ApplicationConfig config;
    private final CandlestickRepository candlestickRepository;
    private final SymbolRepository symbolRepository;

    /**
     * {@inheritDoc}
     */
    public List<PriceChangeTicker> getTickerByQuery(TickerQueryTO query) throws ServiceException {
        log.debug(String.format("Retrieving Symbols using input: %s", query));
        SyncRequestClient syncRequestClient = SyncRequestClient.create(config.getKey(), config.getSecret());
        List<PriceChangeTicker> result;
        if(Objects.nonNull(query.getSymbol())) {
            return syncRequestClient.get24hrTickerPriceChange(query.getSymbol());
        }
        ExchangeInformation exchangeInformation = this.getExchangeInformation();

        //get symbols by trading age
        List<Symbol> symbols = exchangeInformation.getSymbols().stream()
                .filter(s -> ChronoUnit.DAYS.between(Instant.ofEpochMilli(s.getOnboardDate().longValue()).atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()) > query.getMinimumTradingAge())
                .collect(Collectors.toList());

        result = syncRequestClient.get24hrTickerPriceChange(null).stream()
                .filter(t -> symbols.stream().anyMatch(s -> s.getSymbol().contains(t.getSymbol())))
                .filter(Objects.nonNull(query.getMaxPriceChangePercent()) ?
                        t-> t.getPriceChangePercent().abs().compareTo(query.getMaxPriceChangePercent()) < 0 :
                        t -> true)
                .filter(Objects.nonNull(query.getVolumeLowerLimit()) && Objects.nonNull(query.getVolumeUpperLimit()) ?
                        t -> t.getQuoteVolume().compareTo(query.getVolumeLowerLimit()) > 0 &&
                            t.getQuoteVolume().compareTo(query.getVolumeLowerLimit()) < 0
                        : t -> true)
                .collect(Collectors.toList());

        //TODO save mark price into database using websockets. Update existing
        //all time high
        List<SymbolRecord> markPrices = symbolRepository.findAll();
        if(Objects.nonNull(query.getPercentageFromAllTimeHigh())) {
            result = result.stream()
                    .filter(isNearThreshHoldAllTimeHigh(query.getPercentageFromAllTimeHigh(), markPrices)
                            .negate())
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Cacheable(lifetime = 5)
    public ExchangeInformation getExchangeInformation() throws ServiceException {
        log.debug("Retrieving Exchange Information");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ExchangeInformation> result = restTemplate.getForEntity(BinanceApiConstants.API_BASE_URL + "/fapi/v1/exchangeInfo", ExchangeInformation.class);
        return result.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @RetryOnFailure(attempts = 5, delay = 5, unit = TimeUnit.SECONDS)
    public void getCandleStickData(CandlestickInterval interval, int limit) throws ServiceException {
        log.info("Retrieving candlesticks data");
        SyncRequestClient syncRequestClient = SyncRequestClient.create(config.getKey(), config.getSecret());
        this.getExchangeInformation().getSymbols()
                .forEach(s -> {
                    List<CandlestickRecord> candlestickRecords = candlestickRepository.getCandleStickBySymbol(s.getSymbol());
                    List<Candlestick> candleStickList = syncRequestClient.getCandlestick(s.getSymbol(), interval, null, null, limit);
                    candleStickList.stream()
                            .filter(c -> candlestickRecords.stream()
                                    .noneMatch(cr -> c.getOpenTime().compareTo((cr.getOpenTime())) == 0))
                            .forEach(c -> candlestickRepository.insert(s.getSymbol(), c));
                });
        log.info("Successfully retrieved Candlestick data");
    }

    /**
     * {@inheritDoc}
     */
    @Scheduled(fixedDelay = 1000 * 30)
    public List<SymbolRecord> getMarkPriceData() {
        log.info("Retrieving Mark Price data");
        SyncRequestClient syncRequestClient = SyncRequestClient.create(config.getKey(), config.getSecret());
        syncRequestClient.getMarkPrice("")
                .forEach(symbolRepository::insert);
        log.info("Successfully retrieved Mark Price data");
        return symbolRepository.findAll();

    }

    private Predicate<PriceChangeTicker> isNearThreshHoldAllTimeHigh(Long threshHold, List<SymbolRecord> markPrices) {
        return priceChangeTicker -> {
            Optional<Double> markPrice = markPrices.stream()
                    .filter(m -> m.getSymbol().contains(priceChangeTicker.getSymbol()))
                    .map(SymbolRecord::getMarkPrice)
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
