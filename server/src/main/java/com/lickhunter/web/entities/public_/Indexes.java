/*
 * This file is generated by jOOQ.
 */
package com.lickhunter.web.entities.public_;


import com.lickhunter.web.entities.public_.tables.*;
import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;

import javax.annotation.Generated;


/**
 * A class modelling indexes of tables of the <code>PUBLIC</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index PRIMARY_KEY_E = Indexes0.PRIMARY_KEY_E;
    public static final Index FK_ASSET_ACCOUNT_ID_INDEX_3 = Indexes0.FK_ASSET_ACCOUNT_ID_INDEX_3;
    public static final Index PRIMARY_KEY_3 = Indexes0.PRIMARY_KEY_3;
    public static final Index IDX_CANDLESTICK_SYMBOL = Indexes0.IDX_CANDLESTICK_SYMBOL;
    public static final Index PRIMARY_KEY_F = Indexes0.PRIMARY_KEY_F;
    public static final Index PRIMARY_KEY_3D = Indexes0.PRIMARY_KEY_3D;
    public static final Index IDX_INCOME_HISTORY_INCOME_TYPE = Indexes0.IDX_INCOME_HISTORY_INCOME_TYPE;
    public static final Index IDX_INCOME_HISTORY_SYMBOL = Indexes0.IDX_INCOME_HISTORY_SYMBOL;
    public static final Index IDX_INCOME_HISTORY_TRX_ID = Indexes0.IDX_INCOME_HISTORY_TRX_ID;
    public static final Index PRIMARY_KEY_6 = Indexes0.PRIMARY_KEY_6;
    public static final Index FK_POSITION_ACCOUNT_ID_INDEX_5 = Indexes0.FK_POSITION_ACCOUNT_ID_INDEX_5;
    public static final Index PRIMARY_KEY_5 = Indexes0.PRIMARY_KEY_5;
    public static final Index PRIMARY_KEY_9 = Indexes0.PRIMARY_KEY_9;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index PRIMARY_KEY_E = Internal.createIndex("PRIMARY_KEY_E", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.ID }, true);
        public static Index FK_ASSET_ACCOUNT_ID_INDEX_3 = Internal.createIndex("FK_ASSET_ACCOUNT_ID_INDEX_3", Asset.ASSET, new OrderField[] { Asset.ASSET.ACCOUNT_ID }, false);
        public static Index PRIMARY_KEY_3 = Internal.createIndex("PRIMARY_KEY_3", Asset.ASSET, new OrderField[] { Asset.ASSET.ASSET_ }, true);
        public static Index IDX_CANDLESTICK_SYMBOL = Internal.createIndex("IDX_CANDLESTICK_SYMBOL", Candlestick.CANDLESTICK, new OrderField[] { Candlestick.CANDLESTICK.SYMBOL }, false);
        public static Index PRIMARY_KEY_F = Internal.createIndex("PRIMARY_KEY_F", Candlestick.CANDLESTICK, new OrderField[] { Candlestick.CANDLESTICK.ID }, true);
        public static Index PRIMARY_KEY_3D = Internal.createIndex("PRIMARY_KEY_3D", Coins.COINS, new OrderField[] { Coins.COINS.SYMBOL }, true);
        public static Index IDX_INCOME_HISTORY_INCOME_TYPE = Internal.createIndex("IDX_INCOME_HISTORY_INCOME_TYPE", IncomeHistory.INCOME_HISTORY, new OrderField[] { IncomeHistory.INCOME_HISTORY.INCOME_TYPE }, false);
        public static Index IDX_INCOME_HISTORY_SYMBOL = Internal.createIndex("IDX_INCOME_HISTORY_SYMBOL", IncomeHistory.INCOME_HISTORY, new OrderField[] { IncomeHistory.INCOME_HISTORY.SYMBOL }, false);
        public static Index IDX_INCOME_HISTORY_TRX_ID = Internal.createIndex("IDX_INCOME_HISTORY_TRX_ID", IncomeHistory.INCOME_HISTORY, new OrderField[] { IncomeHistory.INCOME_HISTORY.TRX_ID }, false);
        public static Index PRIMARY_KEY_6 = Internal.createIndex("PRIMARY_KEY_6", IncomeHistory.INCOME_HISTORY, new OrderField[] { IncomeHistory.INCOME_HISTORY.ID }, true);
        public static Index FK_POSITION_ACCOUNT_ID_INDEX_5 = Internal.createIndex("FK_POSITION_ACCOUNT_ID_INDEX_5", Position.POSITION, new OrderField[] { Position.POSITION.ACCOUNT_ID }, false);
        public static Index PRIMARY_KEY_5 = Internal.createIndex("PRIMARY_KEY_5", Position.POSITION, new OrderField[] { Position.POSITION.SYMBOL }, true);
        public static Index PRIMARY_KEY_9 = Internal.createIndex("PRIMARY_KEY_9", Symbol.SYMBOL, new OrderField[] { Symbol.SYMBOL.SYMBOL_ }, true);
    }
}
