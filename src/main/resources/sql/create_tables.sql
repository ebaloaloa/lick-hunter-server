DROP TABLE IF EXISTS candlestick, symbol, account, asset, position;

CREATE TABLE candlestick (
    ID                     INTEGER      NOT NULL IDENTITY(1,1),
    SYMBOL                 VARCHAR(20)  NOT NULL,
    OPEN_TIME              NUMBER(13)   NOT NULL,
    OPEN                   DOUBLE       NOT NULL,
    HIGH                   DOUBLE       NOT NULL,
    LOW                    DOUBLE       NOT NULL,
    CLOSE                  DOUBLE       NOT NULL,
    VOLUME                 DOUBLE       NOT NULL,
    CLOSE_TIME             NUMBER(13)   NOT NULL,
    QUOTE_ASSET_VOLUME     DOUBLE       NOT NULL,
    NUMBER_OF_TRADES       INTEGER      NOT NULL,
    TAKER_BUY_BASE_VOLUME  DOUBLE       NOT NULL,
    TAKER_BUY_QUOTE_VOLUME DOUBLE       NOT NULL,

    CONSTRAINT pk_candlestick      PRIMARY KEY (id)
);

CREATE TABLE symbol (
    SYMBOL            VARCHAR(20) NOT NULL,
    MARK_PRICE        DOUBLE       NULL,
    LAST_FUNDING_RATE DOUBLE       NULL,
    NEXT_FUNDING_TIME DOUBLE       NULL,
    TIME              NUMBER(13)   NULL,
    ENABLED           BOOLEAN      NULL,

    CONSTRAINT pk_symbol          PRIMARY KEY (symbol)
);

CREATE TABLE account (
    ID                              VARCHAR(255) NOT NULL,
    CAN_TRADE                       BOOLEAN      NOT NULL,
    CAN_WITHDRAW                    BOOLEAN      NOT NULL,
    FEE_TIER                        DOUBLE       NOT NULL,
    MAX_WITHDRAW_AMOUNT             DOUBLE       NOT NULL,
    TOTAL_INITIAL_MARGIN            DOUBLE       NOT NULL,
    TOTAL_MAINTENANCE_MARGIN        DOUBLE       NOT NULL,
    TOTAL_MARGIN_BALANCE            DOUBLE       NOT NULL,
    TOTAL_OPEN_ORDER_INITIAL_MARGIN DOUBLE       NOT NULL,
    TOTAL_POSITION_INITIAL_MARGIN   DOUBLE       NOT NULL,
    TOTAL_UNREALIZED_PROFIT         DOUBLE       NOT NULL,
    TOTAL_WALLET_BALANCE            DOUBLE       NOT NULL,
    UPDATE_TIME                     NUMBER(13)   NOT NULL,

    CONSTRAINT pk_account      PRIMARY KEY (id)
);

CREATE TABLE asset (
    ASSET                     VARCHAR(20) NOT NULL,
    INITIAL_MARGIN            DOUBLE       NOT NULL,
    MAINT_MARGIN              DOUBLE       NOT NULL,
    MARGIN_BALANCE            DOUBLE       NOT NULL,
    MAX_WITHDRAW_AMOUNT       DOUBLE       NOT NULL,
    OPEN_ORDER_INITIAL_MARGIN DOUBLE       NOT NULL,
    POSITION_INITIAL_MARGIN   DOUBLE       NOT NULL,
    UNREALIZED_PROFIT         DOUBLE       NOT NULL,
    ACCOUNT_ID                VARCHAR(255) NOT NULL,
    CONSTRAINT pk_asset       PRIMARY KEY (ASSET),
    CONSTRAINT fk_asset_account_id  FOREIGN KEY (ACCOUNT_ID)    REFERENCES account(id),
);

CREATE TABLE position (
    ISOLATED                  BOOLEAN      NOT NULL,
    LEVERAGE                  DOUBLE       NOT NULL,
    INITIAL_MARGIN            DOUBLE       NOT NULL,
    MAINT_MARGIN              DOUBLE       NOT NULL,
    OPEN_ORDER_INITIAL_MARGIN DOUBLE       NOT NULL,
    POSITION_INITIAL_MARGIN   DOUBLE       NOT NULL,
    SYMBOL                    VARCHAR(20)  NOT NULL,
    UNREALIZED_PROFIT         DOUBLE       NOT NULL,
    ENTRY_PRICE               VARCHAR(255) NOT NULL,
    MAX_NOTIONAL              VARCHAR(255) NOT NULL,
    POSITION_SIDE             VARCHAR(255) NOT NULL,
    ACCOUNT_ID                VARCHAR(255) NOT NULL,

    CONSTRAINT pk_position    PRIMARY KEY (SYMBOL),
    CONSTRAINT fk_position_account_id  FOREIGN KEY (ACCOUNT_ID)    REFERENCES account(id),
);

--INDEX
CREATE INDEX idx_symbol
ON candlestick (symbol);
