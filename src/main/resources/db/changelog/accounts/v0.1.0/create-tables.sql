--liquibase formatted sql

--changeset Danila Rudenko:2_1
CREATE TABLE IF NOT EXISTS account
(
    id       uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email     VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    );

--changeset Danila Rudenko:2_2
CREATE TABLE IF NOT EXISTS authority
(
    id        BIGSERIAL PRIMARY KEY,
    authority VARCHAR(255) NOT NULL
    );

--changeset Danila Rudenko:2_3
CREATE TABLE IF NOT EXISTS account_authority
(
    account_id   uuid,
    authority_id BIGINT,
    PRIMARY KEY (account_id, authority_id)
    );

--changeset Danila Rudenko:2_4
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM pg_constraint WHERE conname = 'fk_authority' AND conrelid = 'account_authority'::regclass;
ALTER TABLE account_authority
    ADD CONSTRAINT fk_account
        FOREIGN KEY (account_id)
            REFERENCES account (id);

--changeset Danila Rudenko:2_5
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM pg_constraint WHERE conname = 'fk_authority' AND conrelid = 'account_authority'::regclass;
ALTER TABLE account_authority
    ADD CONSTRAINT fk_authority
        FOREIGN KEY (authority_id)
            REFERENCES authority (id);
