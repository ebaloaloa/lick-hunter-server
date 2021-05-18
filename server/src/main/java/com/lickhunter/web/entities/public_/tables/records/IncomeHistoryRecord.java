/*
 * This file is generated by jOOQ.
 */
package com.lickhunter.web.entities.public_.tables.records;


import com.lickhunter.web.entities.public_.tables.IncomeHistory;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.math.BigDecimal;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IncomeHistoryRecord extends UpdatableRecordImpl<IncomeHistoryRecord> implements Record8<Long, Long, String, String, BigDecimal, String, Long, String> {

    private static final long serialVersionUID = -995164421;

    /**
     * Setter for <code>PUBLIC.INCOME_HISTORY.ID</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.INCOME_HISTORY.ID</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>PUBLIC.INCOME_HISTORY.TRX_ID</code>.
     */
    public void setTrxId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.INCOME_HISTORY.TRX_ID</code>.
     */
    public Long getTrxId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>PUBLIC.INCOME_HISTORY.SYMBOL</code>.
     */
    public void setSymbol(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>PUBLIC.INCOME_HISTORY.SYMBOL</code>.
     */
    public String getSymbol() {
        return (String) get(2);
    }

    /**
     * Setter for <code>PUBLIC.INCOME_HISTORY.INCOME_TYPE</code>.
     */
    public void setIncomeType(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>PUBLIC.INCOME_HISTORY.INCOME_TYPE</code>.
     */
    public String getIncomeType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>PUBLIC.INCOME_HISTORY.INCOME</code>.
     */
    public void setIncome(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>PUBLIC.INCOME_HISTORY.INCOME</code>.
     */
    public BigDecimal getIncome() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>PUBLIC.INCOME_HISTORY.ASSET</code>.
     */
    public void setAsset(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>PUBLIC.INCOME_HISTORY.ASSET</code>.
     */
    public String getAsset() {
        return (String) get(5);
    }

    /**
     * Setter for <code>PUBLIC.INCOME_HISTORY.TIME</code>.
     */
    public void setTime(Long value) {
        set(6, value);
    }

    /**
     * Getter for <code>PUBLIC.INCOME_HISTORY.TIME</code>.
     */
    public Long getTime() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>PUBLIC.INCOME_HISTORY.ACCOUNT_ID</code>.
     */
    public void setAccountId(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>PUBLIC.INCOME_HISTORY.ACCOUNT_ID</code>.
     */
    public String getAccountId() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, Long, String, String, BigDecimal, String, Long, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Long, Long, String, String, BigDecimal, String, Long, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return IncomeHistory.INCOME_HISTORY.ID;
    }

    @Override
    public Field<Long> field2() {
        return IncomeHistory.INCOME_HISTORY.TRX_ID;
    }

    @Override
    public Field<String> field3() {
        return IncomeHistory.INCOME_HISTORY.SYMBOL;
    }

    @Override
    public Field<String> field4() {
        return IncomeHistory.INCOME_HISTORY.INCOME_TYPE;
    }

    @Override
    public Field<BigDecimal> field5() {
        return IncomeHistory.INCOME_HISTORY.INCOME;
    }

    @Override
    public Field<String> field6() {
        return IncomeHistory.INCOME_HISTORY.ASSET;
    }

    @Override
    public Field<Long> field7() {
        return IncomeHistory.INCOME_HISTORY.TIME;
    }

    @Override
    public Field<String> field8() {
        return IncomeHistory.INCOME_HISTORY.ACCOUNT_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getTrxId();
    }

    @Override
    public String component3() {
        return getSymbol();
    }

    @Override
    public String component4() {
        return getIncomeType();
    }

    @Override
    public BigDecimal component5() {
        return getIncome();
    }

    @Override
    public String component6() {
        return getAsset();
    }

    @Override
    public Long component7() {
        return getTime();
    }

    @Override
    public String component8() {
        return getAccountId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getTrxId();
    }

    @Override
    public String value3() {
        return getSymbol();
    }

    @Override
    public String value4() {
        return getIncomeType();
    }

    @Override
    public BigDecimal value5() {
        return getIncome();
    }

    @Override
    public String value6() {
        return getAsset();
    }

    @Override
    public Long value7() {
        return getTime();
    }

    @Override
    public String value8() {
        return getAccountId();
    }

    @Override
    public IncomeHistoryRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public IncomeHistoryRecord value2(Long value) {
        setTrxId(value);
        return this;
    }

    @Override
    public IncomeHistoryRecord value3(String value) {
        setSymbol(value);
        return this;
    }

    @Override
    public IncomeHistoryRecord value4(String value) {
        setIncomeType(value);
        return this;
    }

    @Override
    public IncomeHistoryRecord value5(BigDecimal value) {
        setIncome(value);
        return this;
    }

    @Override
    public IncomeHistoryRecord value6(String value) {
        setAsset(value);
        return this;
    }

    @Override
    public IncomeHistoryRecord value7(Long value) {
        setTime(value);
        return this;
    }

    @Override
    public IncomeHistoryRecord value8(String value) {
        setAccountId(value);
        return this;
    }

    @Override
    public IncomeHistoryRecord values(Long value1, Long value2, String value3, String value4, BigDecimal value5, String value6, Long value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached IncomeHistoryRecord
     */
    public IncomeHistoryRecord() {
        super(IncomeHistory.INCOME_HISTORY);
    }

    /**
     * Create a detached, initialised IncomeHistoryRecord
     */
    public IncomeHistoryRecord(Long id, Long trxId, String symbol, String incomeType, BigDecimal income, String asset, Long time, String accountId) {
        super(IncomeHistory.INCOME_HISTORY);

        set(0, id);
        set(1, trxId);
        set(2, symbol);
        set(3, incomeType);
        set(4, income);
        set(5, asset);
        set(6, time);
        set(7, accountId);
    }
}
