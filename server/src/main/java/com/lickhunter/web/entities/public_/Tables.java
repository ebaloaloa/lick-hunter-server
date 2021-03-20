/*
 * This file is generated by jOOQ.
 */
package com.lickhunter.web.entities.public_;


import com.lickhunter.web.entities.public_.tables.Account;
import com.lickhunter.web.entities.public_.tables.Asset;
import com.lickhunter.web.entities.public_.tables.Candlestick;
import com.lickhunter.web.entities.public_.tables.Coins;
import com.lickhunter.web.entities.public_.tables.IncomeHistory;
import com.lickhunter.web.entities.public_.tables.Position;
import com.lickhunter.web.entities.public_.tables.Symbol;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in PUBLIC
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>PUBLIC.ACCOUNT</code>.
     */
    public static final Account ACCOUNT = Account.ACCOUNT;

    /**
     * The table <code>PUBLIC.ASSET</code>.
     */
    public static final Asset ASSET = Asset.ASSET;

    /**
     * The table <code>PUBLIC.CANDLESTICK</code>.
     */
    public static final Candlestick CANDLESTICK = Candlestick.CANDLESTICK;

    /**
     * The table <code>PUBLIC.COINS</code>.
     */
    public static final Coins COINS = Coins.COINS;

    /**
     * The table <code>PUBLIC.INCOME_HISTORY</code>.
     */
    public static final IncomeHistory INCOME_HISTORY = IncomeHistory.INCOME_HISTORY;

    /**
     * The table <code>PUBLIC.POSITION</code>.
     */
    public static final Position POSITION = Position.POSITION;

    /**
     * The table <code>PUBLIC.SYMBOL</code>.
     */
    public static final Symbol SYMBOL = Symbol.SYMBOL;
}