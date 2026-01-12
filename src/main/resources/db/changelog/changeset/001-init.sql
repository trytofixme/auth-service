--liquibase formatted sql

--changeset jwt-auth:001
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE tokens (
    id BIGSERIAL PRIMARY KEY,
    access_token TEXT NOT NULL,
    refresh_token TEXT NOT NULL,
    is_logged_out BOOLEAN DEFAULT FALSE NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

--rollback DROP TABLE users; DROP TABLE tokens;