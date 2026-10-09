CREATE TABLE customers (
                           id_customer UUID PRIMARY KEY,
                           full_name VARCHAR(150) NOT NULL,
                           cpf VARCHAR(11) NOT NULL UNIQUE,
                           email VARCHAR(254) NOT NULL UNIQUE,
                           birth_date DATE NOT NULL,
                           customer_status VARCHAR(20) NOT NULL
                               CHECK (customer_status IN ('ACTIVE', 'INACTIVE', 'BLOCKED')),
                           created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT chk_customer_cpf_format
                               CHECK (cpf ~ '^[0-9]{11}$'),

    CONSTRAINT chk_customer_birth_date
        CHECK (birth_date <= CURRENT_DATE)
);

CREATE TABLE accounts (
                          id_account UUID PRIMARY KEY,
                          id_customer UUID NOT NULL,
                          account_number VARCHAR(20) NOT NULL UNIQUE,
                          account_status VARCHAR(20) NOT NULL
                              CHECK (account_status IN ('ACTIVE', 'BLOCKED', 'CLOSED')),
                          created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          closed_at TIMESTAMP WITH TIME ZONE,
                          updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_account_customer
                              FOREIGN KEY (id_customer)
                                  REFERENCES customers (id_customer),

                          CONSTRAINT chk_account_closed_at
                              CHECK (
                                  (account_status = 'CLOSED' AND closed_at IS NOT NULL)
                                      OR
                                  (account_status <> 'CLOSED' AND closed_at IS NULL)
                                  )
);

CREATE TABLE ledger_accounts (
                                 id_ledger_account UUID PRIMARY KEY,
                                 code VARCHAR(20) NOT NULL UNIQUE,
                                 name VARCHAR(100) NOT NULL,
                                 type VARCHAR(20) NOT NULL
                                     CHECK (type IN ('ASSET', 'LIABILITY', 'EQUITY', 'REVENUE', 'EXPENSE')),
                                 normal_balance VARCHAR(10) NOT NULL
                                     CHECK (normal_balance IN ('DEBIT', 'CREDIT')),
                                 ledger_account_status VARCHAR(20) NOT NULL
                                     CHECK (ledger_account_status IN ('ACTIVE', 'INACTIVE')),
                                 created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ledger_transactions (
                                     id_ledger_transaction UUID PRIMARY KEY,
                                     transaction_type VARCHAR(20) NOT NULL
                                         CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')),
                                     ledger_transaction_posted BOOLEAN NOT NULL DEFAULT FALSE,
                                     description VARCHAR(255) NOT NULL,
                                     reference VARCHAR(100),
                                     created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     posted_at TIMESTAMP WITH TIME ZONE,

                                     CONSTRAINT chk_ledger_transaction_posted_at
                                         CHECK (
                                             (ledger_transaction_posted = TRUE AND posted_at IS NOT NULL)
                                                 OR
                                             (ledger_transaction_posted = FALSE AND posted_at IS NULL)
                                             )
);

CREATE TABLE ledger_entries (
                                id_ledger_entry UUID PRIMARY KEY,
                                id_ledger_transaction UUID NOT NULL,
                                id_ledger_account UUID NOT NULL,
                                id_account UUID,
                                direction VARCHAR(10) NOT NULL
                                    CHECK (direction IN ('DEBIT', 'CREDIT')),
                                amount NUMERIC(19, 2) NOT NULL
                                    CHECK (amount > 0),
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_entry_transaction
                                    FOREIGN KEY (id_ledger_transaction)
                                        REFERENCES ledger_transactions (id_ledger_transaction),

                                CONSTRAINT fk_entry_ledger_account
                                    FOREIGN KEY (id_ledger_account)
                                        REFERENCES ledger_accounts (id_ledger_account),

                                CONSTRAINT fk_entry_account
                                    FOREIGN KEY (id_account)
                                        REFERENCES accounts (id_account)
);

-- Índices para consultas e relacionamentos
CREATE INDEX idx_accounts_customer
    ON accounts (id_customer);

CREATE INDEX idx_entries_transaction
    ON ledger_entries (id_ledger_transaction);

CREATE INDEX idx_entries_ledger_account
    ON ledger_entries (id_ledger_account);

CREATE INDEX idx_entries_account
    ON ledger_entries (id_account);
