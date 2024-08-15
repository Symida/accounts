CREATE TABLE account (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE authority (
    id BIGSERIAL PRIMARY KEY,
    authority VARCHAR(255) NOT NULL
);

CREATE TABLE account_authority (
    account_id uuid REFERENCES account(id),
    authority_id BIGINT REFERENCES authority(id),
    PRIMARY KEY (account_id, authority_id)
);

INSERT INTO account (username, password) VALUES ('admin', 'admin');
INSERT INTO authority (authority) VALUES ('ADMIN'), ('USER');

