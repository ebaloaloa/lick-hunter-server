package com.lickhunter.web.services;

import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.PriceChangeTicker;
import com.lickhunter.web.entities.public_.tables.records.SymbolRecord;
import com.lickhunter.web.exceptions.ServiceException;
import com.lickhunter.web.models.market.ExchangeInformation;
import com.lickhunter.web.to.TickerQueryTO;

import java.util.List;

public interface MarketService {
    /**
     * Retrieves price change statistics by specifying query.
     * If symbol is set, tickers for that specific symbol will be returned.
     * Specify maxPriceChangePercentage to return records below that limit
     * Set volumeUpperLimit and volumeLowerLimit to return list symbols within that volume range.
     *
     * @param query symbol, maxPriceChangePercent, volumeUpperLimit, volumeLowerLimit
     * @return List of PriceChangeTicker
     * @throws ServiceException
     */
    List<PriceChangeTicker> getTickerByQuery(TickerQueryTO query) throws ServiceException, Exception;

    /**
     * Current exchange trading rules and symbol information
     * @return ExchangeInformation
     * @throws ServiceException
     */
    ExchangeInformation getExchangeInformation() throws ServiceException;

    /**
     * Manually retrieve candlestick data
     * @param interval
     *    ONE_MINUTE("1m"),
     *     THREE_MINUTES("3m"),
     *     FIVE_MINUTES("5m"),
     *     FIFTEEN_MINUTES("15m"),
     *     HALF_HOURLY("30m"),
     *     HOURLY("1h"),
     *     TWO_HOURLY("2h"),
     *     FOUR_HOURLY("4h"),
     *     SIX_HOURLY("6h"),
     *     EIGHT_HOURLY("8h"),
     *     TWELVE_HOURLY("12h"),
     *     DAILY("1d"),
     *     THREE_DAILY("3d"),
     *     WEEKLY("1w"),
     *     MONTHLY("1M");
     * @param limit identifies the number of bars to be retrieved
     * @throws ServiceException
     */
    void getCandleStickData(CandlestickInterval interval, int limit) throws Exception;

    /**
     * Retrieves Mark Price Data
     * @throws ServiceException
     */
    List<SymbolRecord> getMarkPriceData() throws Exception;

    /**
     * Retrieves Liquidation Data
     * @throws Exception
     */
    void getLiquidations() throws Exception;
}
