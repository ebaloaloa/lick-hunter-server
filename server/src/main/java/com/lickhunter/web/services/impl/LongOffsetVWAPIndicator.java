package com.lickhunter.web.services.impl;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.TypicalPriceIndicator;
import org.ta4j.core.indicators.helpers.VolumeIndicator;
import org.ta4j.core.indicators.volume.VWAPIndicator;
import org.ta4j.core.num.Num;

public class LongOffsetVWAPIndicator extends VWAPIndicator {

    private final int barCount;
    private final Indicator<Num> typicalPrice;
    private final Indicator<Num> volume;
    private final Num zero;
    private final Num offset;

    /**
     * Constructor.
     *  @param series   the series
     * @param barCount the time frame
     */
    public LongOffsetVWAPIndicator(BarSeries series, int barCount, double offset) {
        super(series, barCount);
        this.barCount = barCount;
        this.typicalPrice = new TypicalPriceIndicator(series);
        this.volume = new VolumeIndicator(series);
        this.zero = numOf(0);
        this.offset = numOf(offset);
    }

    @Override
    protected Num calculate(int index) {
        if (index <= 0) {
            return this.typicalPrice.getValue(index);
        }
        int startIndex = Math.max(0, index - barCount + 1);
        Num cumulativeTPV = zero;
        Num cumulativeVolume = zero;
        for (int i = startIndex; i <= index; i++) {
            Num currentVolume = volume.getValue(i);
            cumulativeTPV = cumulativeTPV.plus(typicalPrice.getValue(i).multipliedBy(currentVolume));
            cumulativeVolume = cumulativeVolume.plus(currentVolume);
        }
        Num vwap = cumulativeTPV.dividedBy(cumulativeVolume);
        return vwap.minus(vwap.multipliedBy(offset).dividedBy(numOf(100)));
    }
}
