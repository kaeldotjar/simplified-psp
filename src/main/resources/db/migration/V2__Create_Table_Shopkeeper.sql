CREATE TABLE IF NOT EXISTS shopkeeper (
    id binary(16) NOT NULL,
    full_name varchar(200) NOT NULL,
    cnpj varchar(18) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    balance numeric(19,2) NOT NULL,
    CONSTRAINT shopkeeper_pk PRIMARY KEY (id),
    CONSTRAINT shopkeeper_cnpj_un UNIQUE (cnpj),
    CONSTRAINT shopkeeper_email_un UNIQUE (email)
);