CREATE TABLE IF NOT EXISTS users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS wallets
(
    id                    SERIAL PRIMARY KEY,
    user_id               INTEGER        NOT NULL,
    wallet_name           VARCHAR(255),
    cryptocurrency        VARCHAR(255)   NOT NULL,
    public_address        VARCHAR(255)   NOT NULL UNIQUE,
    encrypted_seed        TEXT,
    encrypted_wallet_data TEXT,
    available_balance     NUMERIC(20, 8) NOT NULL  DEFAULT 0,
    unconfirmed_balance   NUMERIC(20, 8) NOT NULL  DEFAULT 0,
    created_at            TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at            TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions
(
    id               SERIAL PRIMARY KEY,
    wallet_id        INTEGER        NOT NULL,
    tx_hash          VARCHAR(255)   NOT NULL UNIQUE,
    amount           NUMERIC(20, 8) NOT NULL,
    fee              NUMERIC(20, 8) NOT NULL,
    transaction_type VARCHAR(50)    NOT NULL,
    status           VARCHAR(50)    NOT NULL,
    confirmations    INTEGER                  DEFAULT 0,
    block_hash       VARCHAR(255),
    block_height     INTEGER,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT fk_transaction_wallet FOREIGN KEY (wallet_id)
        REFERENCES wallets (id)
        ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS user_sessions
(
    id               SERIAL PRIMARY KEY,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id          INTEGER  NOT NULL,
    token            VARCHAR(255),
    token_issue_at   TIMESTAMP,
    token_expire_at  TIMESTAMP,
    token_revoked_at TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_session_user_id ON user_sessions (user_id);