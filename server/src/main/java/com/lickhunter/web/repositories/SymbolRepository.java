package com.lickhunter.web.repositories;

import com.binance.client.model.market.MarkPrice;
import com.binance.client.model.market.PriceChangeTicker;
import com.binance.client.model.user.OrderUpdate;
import com.lickhunter.web.configs.UserDefinedSettings;
import com.lickhunter.web.entities.tables.records.PositionRecord;
import com.lickhunter.web.entities.tables.records.SymbolRecord;
import com.lickhunter.web.models.liquidation.Datum;
import com.lickhunter.web.models.market.Symbol;
import com.lickhunter.web.models.sentiments.SentimentsAsset;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;

import static com.lickhunter.web.entities.tables.Symbol.SYMBOL;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
public class SymbolRepository {

    private final DSLContext dsl;
    private final PositionRepository positionRepository;

    public void insertOrUpdate(MarkPrice markPrice) {
        Optional<SymbolRecord> symbolRecord = this.findBySymbol(markPrice.getSymbol());
        if(symbolRecord.isPresent()) {
            this.update(markPrice);
        } else {
            this.insert(markPrice);
        }
    }

    public void insertOrUpdate(Symbol symbol) {
        Optional<SymbolRecord> symbolRecord = this.findBySymbol(symbol.getSymbol());
        if(symbolRecord.isPresent()) {
            this.update(symbol);
        } else {
            this.insert(symbol);
        }
    }

    public void insertOrUpdate(Datum datum) {
        Optional<SymbolRecord> symbolRecord = this.findBySymbol(datum.getSymbol().concat("USDT"));
        if(symbolRecord.isPresent()) {
            this.update(datum);
        } else {
            this.insert(datum);
        }
    }

    public void insertOrUpdate(PriceChangeTicker priceChangeTicker) {
        Optional<SymbolRecord> symbolRecord = this.findBySymbol(priceChangeTicker.getSymbol());
        if(symbolRecord.isPresent()) {
            this.update(priceChangeTicker);
        } else {
            this.insert(priceChangeTicker);
        }
    }

    public void insert(MarkPrice markPrice) {
        dsl.insertInto(SYMBOL)
                .set(SYMBOL.SYMBOL_, markPrice.getSymbol())
                .set(SYMBOL.MARK_PRICE, markPrice.getMarkPrice().doubleValue())
                .set(SYMBOL.LAST_FUNDING_RATE, markPrice.getLastFundingRate().doubleValue())
                .set(SYMBOL.NEXT_FUNDING_TIME, markPrice.getNextFundingTime().doubleValue())
                .set(SYMBOL.TIME, markPrice.getTime())
                .execute();
    }

    public void insert(Datum datum) {
        dsl.insertInto(SYMBOL)
                .set(SYMBOL.SYMBOL_, datum.getSymbol().concat("USDT"))
                .set(SYMBOL.LICK_MEDIAN, Double.valueOf(datum.getMedianUsdt()))
                .set(SYMBOL.LICK_AVERAGE, Double.valueOf(datum.getAverageUsdt()))
                .execute();
    }

    public void insert(Symbol symbol) {
        dsl.insertInto(SYMBOL)
                .set(SYMBOL.SYMBOL_, symbol.getSymbol())
                .set(SYMBOL.ONBOARD_DATE, symbol.getOnboardDate().longValue())
                .set(SYMBOL.PRICE_PRECISION, symbol.getPricePrecision().longValue())
                .set(SYMBOL.TICK_SIZE, Double.parseDouble(symbol.getFilters().get(0).getTickSize()))
                .execute();
    }

    public void insert(PriceChangeTicker priceChangeTicker) {
        dsl.insertInto(SYMBOL)
                .set(SYMBOL.SYMBOL_, priceChangeTicker.getSymbol())
                .set(SYMBOL.PRICE_CHANGE_PERCENT, priceChangeTicker.getPriceChangePercent().doubleValue())
                .set(SYMBOL.QUOTE_VOLUME, priceChangeTicker.getQuoteVolume().doubleValue())
                .execute();
    }

    public void update(MarkPrice markPrice) {
        dsl.update(SYMBOL)
                .set(SYMBOL.MARK_PRICE, markPrice.getMarkPrice().doubleValue())
                .set(SYMBOL.LAST_FUNDING_RATE, markPrice.getLastFundingRate().doubleValue())
                .set(SYMBOL.NEXT_FUNDING_TIME, markPrice.getNextFundingTime().doubleValue())
                .set(SYMBOL.TIME, markPrice.getTime())
                .where(SYMBOL.SYMBOL_.eq(markPrice.getSymbol()))
                .execute();
    }

    public void update(Datum datum) {
        dsl.update(SYMBOL)
                .set(SYMBOL.LICK_MEDIAN, Double.valueOf(datum.getMedianUsdt()))
                .set(SYMBOL.LICK_AVERAGE, Double.valueOf(datum.getAverageUsdt()))
                .where(SYMBOL.SYMBOL_.eq(datum.getSymbol().concat("USDT")))
                .execute();
    }

    public void update(Symbol symbol) {
        dsl.update(SYMBOL)
                .set(SYMBOL.ONBOARD_DATE, symbol.getOnboardDate().longValue())
                .set(SYMBOL.PRICE_PRECISION, symbol.getPricePrecision().longValue())
                .set(SYMBOL.TICK_SIZE, Double.parseDouble(symbol.getFilters().get(0).getTickSize()))
                .where(SYMBOL.SYMBOL_.eq(symbol.getSymbol()))
                .execute();
    }

    public void update(SentimentsAsset sentimentsAsset) {
        dsl.update(SYMBOL)
                .set(SYMBOL.VOLATILITY, sentimentsAsset.getData().get(0).getVolatility())
                .where(SYMBOL.SYMBOL_.eq(sentimentsAsset.getData().get(0).getSymbol().concat("USDT")))
                .execute();
    }

    public void update(PriceChangeTicker priceChangeTicker) {
        dsl.update(SYMBOL)
                .set(SYMBOL.PRICE_CHANGE_PERCENT, priceChangeTicker.getPriceChangePercent().doubleValue())
                .set(SYMBOL.QUOTE_VOLUME, priceChangeTicker.getQuoteVolume().doubleValue())
                .where(SYMBOL.SYMBOL_.eq(priceChangeTicker.getSymbol()))
                .execute();
    }

    public List<SymbolRecord> findAll() {
        return dsl.selectFrom(SYMBOL).fetch();
    }

    public Optional<SymbolRecord> findBySymbol(String symbol) {
        return dsl.selectFrom(SYMBOL)
                .where(SYMBOL.SYMBOL_.eq(symbol))
                .fetchOptional();
    }

    public List<SymbolRecord> findBySymbols(List<String> symbol) {
        return dsl.selectFrom(SYMBOL)
                .where(SYMBOL.SYMBOL_.in(symbol))
                .fetch();
    }

    public Long updateNumberOfBuys(OrderUpdate orderUpdate, String accountId, UserDefinedSettings userDefinedSettings) {
        Optional<SymbolRecord> symbolRecord = this.findBySymbol(orderUpdate.getSymbol());
        Optional<PositionRecord> positionRecord = positionRepository.findBySymbolAndAccountId(orderUpdate.getSymbol(), accountId);
        if(symbolRecord.isPresent() && positionRecord.isPresent()) {
            //reset number of buys
            if(orderUpdate.getIsReduceOnly()) {
                dsl.update(SYMBOL)
                        .set(SYMBOL.FIRST_BUY, 0L)
                        .set(SYMBOL.SECOND_BUY, 0L)
                        .set(SYMBOL.THIRD_BUY, 0L)
                        .set(SYMBOL.FOURTH_BUY, 0L)
                        .set(SYMBOL.FIFTH_BUY, 0L)
                        .set(SYMBOL.SIXTH_BUY, 0L)
                        .where(SYMBOL.SYMBOL_.eq(orderUpdate.getSymbol()))
                        .execute();
            } else {
                BigDecimal percentageFromAverage = ((BigDecimal.valueOf(symbolRecord.get().getMarkPrice())
                        .subtract(new BigDecimal(positionRecord.get().getEntryPrice())).abs()).divide(new BigDecimal(positionRecord.get().getEntryPrice()), MathContext.DECIMAL128)).multiply(BigDecimal.valueOf(100));
                if(percentageFromAverage.compareTo(userDefinedSettings.getRangeFour().getPercentFromAverage()) > 0) {
                    dsl.update(SYMBOL)
                            .set(SYMBOL.FIFTH_BUY, SYMBOL.FIFTH_BUY.plus(1))
                            .where(SYMBOL.SYMBOL_.eq(orderUpdate.getSymbol()))
                            .execute();
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeThree().getPercentFromAverage()) > 0) {
                    dsl.update(SYMBOL)
                            .set(SYMBOL.FOURTH_BUY, SYMBOL.FOURTH_BUY.plus(1))
                            .where(SYMBOL.SYMBOL_.eq(orderUpdate.getSymbol()))
                            .execute();
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeTwo().getPercentFromAverage()) > 0) {
                    dsl.update(SYMBOL)
                            .set(SYMBOL.THIRD_BUY, SYMBOL.THIRD_BUY.plus(1))
                            .where(SYMBOL.SYMBOL_.eq(orderUpdate.getSymbol()))
                            .execute();
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getRangeOne().getPercentFromAverage()) > 0) {
                    dsl.update(SYMBOL)
                            .set(SYMBOL.SECOND_BUY, SYMBOL.SECOND_BUY.plus(1))
                            .where(SYMBOL.SYMBOL_.eq(orderUpdate.getSymbol()))
                            .execute();
                } else if(percentageFromAverage.compareTo(userDefinedSettings.getDcaStart()) > 0) {
                    dsl.update(SYMBOL)
                            .set(SYMBOL.FIRST_BUY, SYMBOL.FIRST_BUY.plus(1))
                            .where(SYMBOL.SYMBOL_.eq(orderUpdate.getSymbol()))
                            .execute();
                }
            }
            symbolRecord = this.findBySymbol(orderUpdate.getSymbol());
            return symbolRecord.get().getFirstBuy()
                    + symbolRecord.get().getSecondBuy()
                    + symbolRecord.get().getThirdBuy()
                    + symbolRecord.get().getFourthBuy()
                    + symbolRecord.get().getFifthBuy();
        }
        return 0L;
    }
}
