CREATE TABLE IF NOT EXISTS account (
    id SERIAL,
    user_uid uuid NOT NULL,
    currency character varying NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS expense_category (
    id SERIAL,
    name character varying NOT NULL,
    uid uuid NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS expense (
    id SERIAL,
    amount double precision NOT NULL,
    name character varying NOT NULL,
    description character varying NOT NULL,
    account_id bigint NOT NULL,
    category_id bigint NOT NULL,
    currency character varying NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_account
        FOREIGN KEY (account_id)
            REFERENCES account(id),
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES expense_category(id)
);

CREATE TABLE IF NOT EXISTS income_category (
    id SERIAL,
    name character varying NOT NULL,
    uid uuid NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS income (
    id SERIAL,
    amount double precision NOT NULL,
    description character varying NOT NULL,
    account_id bigint NOT NULL,
    category_id bigint NOT NULL,
    currency character varying NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_account
        FOREIGN KEY (account_id)
            REFERENCES account(id),
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES income_category(id)
);