--liquibase formatted sql

--changeset Danila Rudenko:2_2
--preconditions onFail:CONTINUE
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'account';
INSERT INTO account (email, role, username, password)
VALUES ('admin@mail.ru', 'ADMIN', 'admin', '$2a$12$Yyg2IoVVVhbh5/DedtVBdeVXNktcfVrFQGvRvOd3O0AVHUELCV76m')
    ON CONFLICT DO NOTHING;