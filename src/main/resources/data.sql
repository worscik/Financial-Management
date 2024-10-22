CREATE TABLE IF NOT EXISTS USER_ACCOUNT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    role VARCHAR(50),
    created_on TIMESTAMP,
    modify_on TIMESTAMP,
    version LONG,
    external_id UUID,
    password VARCHAR(50)
);

INSERT INTO USER_ACCOUNT (email, name, role, created_on, modify_on, external_id, version, password)
VALUES ('demo@example.com', 'user', 'DEMO', NOW(), NOW(), 'f9969a5d-55d2-4e31-83e1-5759500a1e6d', 1, 'user');

CREATE TABLE IF NOT EXISTS BANK_ACCOUNT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    external_id UUID NOT NULL,
    user_id BIGINT NOT NULL,
    created_on TIMESTAMP,
    modify_on TIMESTAMP,
    account_version LONG,
    account_name VARCHAR(255),
    account_number VARCHAR(20),
    account_balance DECIMAL(19, 4),
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id)
);

INSERT INTO BANK_ACCOUNT (external_id, user_id, created_on, modify_on, account_version, account_name, account_number, account_balance)
VALUES ('b396f22e-1a5a-4f1b-9f7d-59456c00d9a4',
        (SELECT id FROM USER_ACCOUNT WHERE email = 'demo@example.com'),
        NOW(),
        NOW(),
        1,
        'Main Account',
        '12345678901234567890',
        1000.00);
