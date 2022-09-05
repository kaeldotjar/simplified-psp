CREATE TABLE IF NOT EXISTS common_user (
    id binary(16) NOT NULL,
    full_name varchar(200) NOT NULL,
    cpf varchar(14) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    balance numeric(19,2) NOT NULL,
    CONSTRAINT common_user_pk PRIMARY KEY (id),
    CONSTRAINT common_user_cpf_un UNIQUE (cpf),
    CONSTRAINT common_user_email_un UNIQUE (email)
);