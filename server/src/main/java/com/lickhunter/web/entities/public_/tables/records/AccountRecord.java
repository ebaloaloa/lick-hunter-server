/*
 * This file is generated by jOOQ.
 */
package com.lickhunter.web.entities.public_.tables.records;


import com.lickhunter.web.entities.public_.tables.Account;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;


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
public class AccountRecord extends UpdatableRecordImpl<AccountRecord> implements Record13<String, Boolean, Boolean, Double, Double, Double, Double, Double, Double, Double, Double, Double, Long> {

    private static final long serialVersionUID = 1727270122;

    /**
     * Setter for <code>PUBLIC.ACCOUNT.ID</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.ID</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.CAN_TRADE</code>.
     */
    public void setCanTrade(Boolean value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.CAN_TRADE</code>.
     */
    public Boolean getCanTrade() {
        return (Boolean) get(1);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.CAN_WITHDRAW</code>.
     */
    public void setCanWithdraw(Boolean value) {
        set(2, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.CAN_WITHDRAW</code>.
     */
    public Boolean getCanWithdraw() {
        return (Boolean) get(2);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.FEE_TIER</code>.
     */
    public void setFeeTier(Double value) {
        set(3, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.FEE_TIER</code>.
     */
    public Double getFeeTier() {
        return (Double) get(3);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.MAX_WITHDRAW_AMOUNT</code>.
     */
    public void setMaxWithdrawAmount(Double value) {
        set(4, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.MAX_WITHDRAW_AMOUNT</code>.
     */
    public Double getMaxWithdrawAmount() {
        return (Double) get(4);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.TOTAL_INITIAL_MARGIN</code>.
     */
    public void setTotalInitialMargin(Double value) {
        set(5, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.TOTAL_INITIAL_MARGIN</code>.
     */
    public Double getTotalInitialMargin() {
        return (Double) get(5);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.TOTAL_MAINTENANCE_MARGIN</code>.
     */
    public void setTotalMaintenanceMargin(Double value) {
        set(6, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.TOTAL_MAINTENANCE_MARGIN</code>.
     */
    public Double getTotalMaintenanceMargin() {
        return (Double) get(6);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.TOTAL_MARGIN_BALANCE</code>.
     */
    public void setTotalMarginBalance(Double value) {
        set(7, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.TOTAL_MARGIN_BALANCE</code>.
     */
    public Double getTotalMarginBalance() {
        return (Double) get(7);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.TOTAL_OPEN_ORDER_INITIAL_MARGIN</code>.
     */
    public void setTotalOpenOrderInitialMargin(Double value) {
        set(8, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.TOTAL_OPEN_ORDER_INITIAL_MARGIN</code>.
     */
    public Double getTotalOpenOrderInitialMargin() {
        return (Double) get(8);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.TOTAL_POSITION_INITIAL_MARGIN</code>.
     */
    public void setTotalPositionInitialMargin(Double value) {
        set(9, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.TOTAL_POSITION_INITIAL_MARGIN</code>.
     */
    public Double getTotalPositionInitialMargin() {
        return (Double) get(9);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.TOTAL_UNREALIZED_PROFIT</code>.
     */
    public void setTotalUnrealizedProfit(Double value) {
        set(10, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.TOTAL_UNREALIZED_PROFIT</code>.
     */
    public Double getTotalUnrealizedProfit() {
        return (Double) get(10);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.TOTAL_WALLET_BALANCE</code>.
     */
    public void setTotalWalletBalance(Double value) {
        set(11, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.TOTAL_WALLET_BALANCE</code>.
     */
    public Double getTotalWalletBalance() {
        return (Double) get(11);
    }

    /**
     * Setter for <code>PUBLIC.ACCOUNT.UPDATE_TIME</code>.
     */
    public void setUpdateTime(Long value) {
        set(12, value);
    }

    /**
     * Getter for <code>PUBLIC.ACCOUNT.UPDATE_TIME</code>.
     */
    public Long getUpdateTime() {
        return (Long) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row13<String, Boolean, Boolean, Double, Double, Double, Double, Double, Double, Double, Double, Double, Long> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    @Override
    public Row13<String, Boolean, Boolean, Double, Double, Double, Double, Double, Double, Double, Double, Double, Long> valuesRow() {
        return (Row13) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Account.ACCOUNT.ID;
    }

    @Override
    public Field<Boolean> field2() {
        return Account.ACCOUNT.CAN_TRADE;
    }

    @Override
    public Field<Boolean> field3() {
        return Account.ACCOUNT.CAN_WITHDRAW;
    }

    @Override
    public Field<Double> field4() {
        return Account.ACCOUNT.FEE_TIER;
    }

    @Override
    public Field<Double> field5() {
        return Account.ACCOUNT.MAX_WITHDRAW_AMOUNT;
    }

    @Override
    public Field<Double> field6() {
        return Account.ACCOUNT.TOTAL_INITIAL_MARGIN;
    }

    @Override
    public Field<Double> field7() {
        return Account.ACCOUNT.TOTAL_MAINTENANCE_MARGIN;
    }

    @Override
    public Field<Double> field8() {
        return Account.ACCOUNT.TOTAL_MARGIN_BALANCE;
    }

    @Override
    public Field<Double> field9() {
        return Account.ACCOUNT.TOTAL_OPEN_ORDER_INITIAL_MARGIN;
    }

    @Override
    public Field<Double> field10() {
        return Account.ACCOUNT.TOTAL_POSITION_INITIAL_MARGIN;
    }

    @Override
    public Field<Double> field11() {
        return Account.ACCOUNT.TOTAL_UNREALIZED_PROFIT;
    }

    @Override
    public Field<Double> field12() {
        return Account.ACCOUNT.TOTAL_WALLET_BALANCE;
    }

    @Override
    public Field<Long> field13() {
        return Account.ACCOUNT.UPDATE_TIME;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public Boolean component2() {
        return getCanTrade();
    }

    @Override
    public Boolean component3() {
        return getCanWithdraw();
    }

    @Override
    public Double component4() {
        return getFeeTier();
    }

    @Override
    public Double component5() {
        return getMaxWithdrawAmount();
    }

    @Override
    public Double component6() {
        return getTotalInitialMargin();
    }

    @Override
    public Double component7() {
        return getTotalMaintenanceMargin();
    }

    @Override
    public Double component8() {
        return getTotalMarginBalance();
    }

    @Override
    public Double component9() {
        return getTotalOpenOrderInitialMargin();
    }

    @Override
    public Double component10() {
        return getTotalPositionInitialMargin();
    }

    @Override
    public Double component11() {
        return getTotalUnrealizedProfit();
    }

    @Override
    public Double component12() {
        return getTotalWalletBalance();
    }

    @Override
    public Long component13() {
        return getUpdateTime();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public Boolean value2() {
        return getCanTrade();
    }

    @Override
    public Boolean value3() {
        return getCanWithdraw();
    }

    @Override
    public Double value4() {
        return getFeeTier();
    }

    @Override
    public Double value5() {
        return getMaxWithdrawAmount();
    }

    @Override
    public Double value6() {
        return getTotalInitialMargin();
    }

    @Override
    public Double value7() {
        return getTotalMaintenanceMargin();
    }

    @Override
    public Double value8() {
        return getTotalMarginBalance();
    }

    @Override
    public Double value9() {
        return getTotalOpenOrderInitialMargin();
    }

    @Override
    public Double value10() {
        return getTotalPositionInitialMargin();
    }

    @Override
    public Double value11() {
        return getTotalUnrealizedProfit();
    }

    @Override
    public Double value12() {
        return getTotalWalletBalance();
    }

    @Override
    public Long value13() {
        return getUpdateTime();
    }

    @Override
    public AccountRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public AccountRecord value2(Boolean value) {
        setCanTrade(value);
        return this;
    }

    @Override
    public AccountRecord value3(Boolean value) {
        setCanWithdraw(value);
        return this;
    }

    @Override
    public AccountRecord value4(Double value) {
        setFeeTier(value);
        return this;
    }

    @Override
    public AccountRecord value5(Double value) {
        setMaxWithdrawAmount(value);
        return this;
    }

    @Override
    public AccountRecord value6(Double value) {
        setTotalInitialMargin(value);
        return this;
    }

    @Override
    public AccountRecord value7(Double value) {
        setTotalMaintenanceMargin(value);
        return this;
    }

    @Override
    public AccountRecord value8(Double value) {
        setTotalMarginBalance(value);
        return this;
    }

    @Override
    public AccountRecord value9(Double value) {
        setTotalOpenOrderInitialMargin(value);
        return this;
    }

    @Override
    public AccountRecord value10(Double value) {
        setTotalPositionInitialMargin(value);
        return this;
    }

    @Override
    public AccountRecord value11(Double value) {
        setTotalUnrealizedProfit(value);
        return this;
    }

    @Override
    public AccountRecord value12(Double value) {
        setTotalWalletBalance(value);
        return this;
    }

    @Override
    public AccountRecord value13(Long value) {
        setUpdateTime(value);
        return this;
    }

    @Override
    public AccountRecord values(String value1, Boolean value2, Boolean value3, Double value4, Double value5, Double value6, Double value7, Double value8, Double value9, Double value10, Double value11, Double value12, Long value13) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AccountRecord
     */
    public AccountRecord() {
        super(Account.ACCOUNT);
    }

    /**
     * Create a detached, initialised AccountRecord
     */
    public AccountRecord(String id, Boolean canTrade, Boolean canWithdraw, Double feeTier, Double maxWithdrawAmount, Double totalInitialMargin, Double totalMaintenanceMargin, Double totalMarginBalance, Double totalOpenOrderInitialMargin, Double totalPositionInitialMargin, Double totalUnrealizedProfit, Double totalWalletBalance, Long updateTime) {
        super(Account.ACCOUNT);

        set(0, id);
        set(1, canTrade);
        set(2, canWithdraw);
        set(3, feeTier);
        set(4, maxWithdrawAmount);
        set(5, totalInitialMargin);
        set(6, totalMaintenanceMargin);
        set(7, totalMarginBalance);
        set(8, totalOpenOrderInitialMargin);
        set(9, totalPositionInitialMargin);
        set(10, totalUnrealizedProfit);
        set(11, totalWalletBalance);
        set(12, updateTime);
    }
}