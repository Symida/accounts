--liquibase formatted sql

--changeset Danila Rudenko:2_1
CREATE TABLE IF NOT EXISTS account
(
    id       uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email    VARCHAR(255) NOT NULL UNIQUE,
    role    VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);