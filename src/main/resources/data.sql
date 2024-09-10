CREATE TABLE IF NOT EXISTS USER_ACCOUNT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    role VARCHAR(50),
    created_on TIMESTAMP,
    modify_on TIMESTAMP,
    version LONG,
    external_id UUID
);

INSERT INTO USER_ACCOUNT (email, name, role, created_on, modify_on, external_id, version)
VALUES ('demo@example.com', 'Demo User', 'DEMO', NOW(), NOW(), 'f9969a5d-55d2-4e31-83e1-5759500a1e6d', 1);

