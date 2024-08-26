--liquibase formatted sql

--changeset Danila Rudenko:2_6
--preconditions onFail:CONTINUE
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'account';
INSERT INTO account (id, email, username, password)
VALUES ('5b399164-ba1f-4b48-b4f7-463cd1d5ce37', 'admin@mail.ru','admin', '$2a$12$Yyg2IoVVVhbh5/DedtVBdeVXNktcfVrFQGvRvOd3O0AVHUELCV76m')
    ON CONFLICT DO NOTHING;

--changeset Danila Rudenko:2_7
--preconditions onFail:CONTINUE
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'authority';
INSERT INTO authority (name)
VALUES ('ADMIN'),
       ('USER')
    ON CONFLICT DO NOTHING;

--changeset Danila Rudenko:2_8
--preconditions onFail:CONTINUE
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'account_authority';
INSERT INTO account_authority (account_id, authority_id)
VALUES ((SELECT id
         FROM account
         WHERE username = 'admin'),
        (SELECT id
         FROM authority
         WHERE name = 'ADMIN'))
    ON CONFLICT DO NOTHING;