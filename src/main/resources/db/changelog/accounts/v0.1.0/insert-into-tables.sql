--liquibase formatted sql

--changeset Danila Rudenko:2_6
--preconditions onFail:CONTINUE
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'account';
INSERT INTO account (mail, username, password)
VALUES ('admin@mail.ru','admin', 'admin')
    ON CONFLICT DO NOTHING;

--changeset Danila Rudenko:2_7
--preconditions onFail:CONTINUE
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'authority';
INSERT INTO authority (authority)
VALUES ('ADMIN'),
       ('USER')
    ON CONFLICT DO NOTHING;