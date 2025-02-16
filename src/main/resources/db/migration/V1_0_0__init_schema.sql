CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE wallets
(
    id             SERIAL PRIMARY KEY,
    user_id        INTEGER      NOT NULL,
    wallet_name    VARCHAR(255),
    public_address VARCHAR(255) NOT NULL UNIQUE,
    encrypted_seed TEXT,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at     TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE TABLE transactions
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

CREATE TABLE balances
(
    wallet_id           INTEGER PRIMARY KEY,
    available_balance   NUMERIC(20, 8) NOT NULL  DEFAULT 0,
    unconfirmed_balance NUMERIC(20, 8) NOT NULL  DEFAULT 0,
    updated_at          TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT fk_balance_wallet FOREIGN KEY (wallet_id)
        REFERENCES wallets (id)
        ON DELETE CASCADE
);
