CREATE TABLE IF NOT EXISTS transaction (
    id binary(16) NOT NULL,
    payee_id binary(16) NOT NULL,
    payer_id binary(16) NOT NULL,
    value numeric(19,2) NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT transaction_pk PRIMARY KEY (id)
);