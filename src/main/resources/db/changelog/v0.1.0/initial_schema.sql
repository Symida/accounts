CREATE TABLE users (
    id uuid PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY,
    authority VARCHAR(255) NOT NULL
);

CREATE TABLE users_roles (
    user_id uuid REFERENCES users(id),
    role_id BIGINT REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);
