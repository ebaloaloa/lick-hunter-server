DROP TABLE IF EXISTS candlestick;
DROP TABLE IF EXISTS symbol;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS asset;
DROP TABLE IF EXISTS position;
DROP TABLE IF EXISTS coins;
DROP TABLE IF EXISTS income_history;

CREATE TABLE IF NOT EXISTS candlestick (
    ID                     INTEGER      NOT NULL,
    SYMBOL                 VARCHAR(20)  NOT NULL,
    OPEN_TIME              BIGINT       NOT NULL,
    OPEN                   DOUBLE       NOT NULL,
    HIGH                   DOUBLE       NOT NULL,
    LOW                    DOUBLE       NOT NULL,
    CLOSE                  DOUBLE       NOT NULL,
    VOLUME                 DOUBLE       NOT NULL,
    CLOSE_TIME             BIGINT       NOT NULL,
    QUOTE_ASSET_VOLUME     DOUBLE       NOT NULL,
    NUMBER_OF_TRADES       INTEGER      NOT NULL,
    TAKER_BUY_BASE_VOLUME  DOUBLE       NOT NULL,
    TAKER_BUY_QUOTE_VOLUME DOUBLE       NOT NULL,
    TIMEFRAME              VARCHAR(20)  NULL,

    CONSTRAINT pk_candlestick      PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS symbol (
    SYMBOL               VARCHAR(20)  NOT NULL,
    MARK_PRICE           DOUBLE       NULL,
    LAST_FUNDING_RATE    DOUBLE       NULL,
    NEXT_FUNDING_TIME    DOUBLE       NULL,
    TIME                 BIGINT       NULL,
    ONBOARD_DATE         BIGINT       NULL,
    PRICE_CHANGE_PERCENT DOUBLE       NULL,
    QUOTE_VOLUME         DOUBLE       NULL,
    PRICE_PRECISION      BIGINT       NULL,
    TICK_SIZE            DOUBLE       NULL,
    VOLATILITY           DOUBLE       NULL,
    LICK_MEDIAN          DOUBLE       NULL,
    LICK_AVERAGE         DOUBLE       NULL,
    FIRST_BUY            BIGINT       NULL        DEFAULT 0,
    SECOND_BUY           BIGINT       NULL        DEFAULT 0,
    THIRD_BUY            BIGINT       NULL        DEFAULT 0,
    FOURTH_BUY           BIGINT       NULL        DEFAULT 0,
    FIFTH_BUY            BIGINT       NULL        DEFAULT 0,
    SIXTH_BUY            BIGINT       NULL        DEFAULT 0,

    CONSTRAINT pk_symbol           PRIMARY KEY (symbol)
);

CREATE TABLE IF NOT EXISTS account (
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
    UPDATE_TIME                     BIGINT       NOT NULL,

    CONSTRAINT pk_account      PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS asset (
    ASSET                     VARCHAR(20)  NOT NULL,
    INITIAL_MARGIN            DOUBLE       NOT NULL,
    MAINT_MARGIN              DOUBLE       NOT NULL,
    MARGIN_BALANCE            DOUBLE       NOT NULL,
    MAX_WITHDRAW_AMOUNT       DOUBLE       NOT NULL,
    OPEN_ORDER_INITIAL_MARGIN DOUBLE       NOT NULL,
    POSITION_INITIAL_MARGIN   DOUBLE       NOT NULL,
    UNREALIZED_PROFIT         DOUBLE       NOT NULL,
    ACCOUNT_ID                VARCHAR(255) NOT NULL,

    CONSTRAINT pk_asset       PRIMARY KEY (ASSET),
    CONSTRAINT fk_asset_account_id  FOREIGN KEY (ACCOUNT_ID)    REFERENCES account(id)
);

CREATE TABLE IF NOT EXISTS position (
    ISOLATED                  BOOLEAN      NULL,
    LEVERAGE                  DOUBLE       NULL,
    INITIAL_MARGIN            DOUBLE       NOT NULL,
    MAINT_MARGIN              DOUBLE       NULL,
    OPEN_ORDER_INITIAL_MARGIN DOUBLE       NULL,
    POSITION_INITIAL_MARGIN   DOUBLE       NOT NULL,
    SYMBOL                    VARCHAR(20)  NOT NULL,
    UNREALIZED_PROFIT         DOUBLE       NOT NULL,
    ENTRY_PRICE               VARCHAR(255) NOT NULL,
    MAX_NOTIONAL              VARCHAR(255) NULL,
    POSITION_SIDE             VARCHAR(255) NULL,
    ORDER_ID                  BIGINT       NULL,
    ORDER_SIDE                VARCHAR(255) NULL,
    ACCOUNT_ID                VARCHAR(255) NOT NULL,

    CONSTRAINT pk_position             PRIMARY KEY (SYMBOL),
    CONSTRAINT fk_position_account_id  FOREIGN KEY (ACCOUNT_ID)    REFERENCES account(id)
);

CREATE TABLE IF NOT EXISTS income_history (
    ID          INTEGER           NOT NULL,
    SYMBOL      VARCHAR(20)   NULL,
    INCOME_TYPE VARCHAR(20)   NULL,
    INCOME      DOUBLE        NULL,
    ASSET       VARCHAR(20)   NULL,
    TIME        BIGINT        NULL,
    ACCOUNT_ID  VARCHAR(255)  NOT NULL,

    CONSTRAINT pk_income_history PRIMARY KEY (ID)
);

CREATE INDEX idx_candlestick_symbol
ON candlestick (symbol);
CREATE INDEX idx_candlestick_timeframe
ON candlestick (timeframe);
CREATE INDEX idx_income_history_symbol
ON income_history (symbol);
CREATE INDEX idx_income_history_income_type
ON income_history (income_type);

