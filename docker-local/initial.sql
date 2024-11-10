-- Tabela użytkowników
CREATE TABLE user_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    external_id CHAR(36) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modify_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL,
    role VARCHAR(255),
    password VARCHAR(255)
);

CREATE TABLE bank_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    external_id CHAR(36) NOT NULL,
    user_id BIGINT NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modify_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    account_version BIGINT NOT NULL,
    account_name VARCHAR(255),
    account_number VARCHAR(255),
    account_balance DECIMAL(19, 4),
    FOREIGN KEY (user_id) REFERENCES user_account(id)
);

CREATE TABLE expense (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    external_id CHAR(36) NOT NULL,
    version INT NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modify_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expense_category VARCHAR(255),
    expense_item VARCHAR(255),
    expense_type VARCHAR(255),
    expense DECIMAL(19, 4),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_account(id)
);

INSERT INTO user_account (external_id, email, name, version, role, password)
VALUES
(UUID(), 'admin@financialapp.com', 'admin', 1, 'ADMIN', 'admin');

INSERT INTO user_account (external_id, email, name, version, role, password)
VALUES
(UUID(), 'demo@financialapp.com', 'demo', 1, 'USER', 'demo');

INSERT INTO bank_account (external_id, user_id, account_version, account_name, account_number, account_balance)
VALUES
(UUID(), (SELECT id FROM user_account WHERE email = 'admin@financialapp.com'), 1, 'Admin Account', '1234567890', 1000.00);

INSERT INTO bank_account (external_id, user_id, account_version, account_name, account_number, account_balance)
VALUES
(UUID(), (SELECT id FROM user_account WHERE email = 'demo@financialapp.com'), 1, 'Demo Bank Account', '0987654321', 500.00);

INSERT INTO expense (external_id, version, created_on, modify_on, expense_item, expense_category, expense_type, expense, user_id)
VALUES
(UUID(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'HOME', 'Meal', 'EXPENSE', 15.50, (SELECT id FROM user_account WHERE email = 'demo@financialapp.com')),
(UUID(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'WORK', 'Bus Ticket', 'EXPENSE', 2.80, (SELECT id FROM user_account WHERE email = 'demo@financialapp.com')),
(UUID(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'OTHER', 'Date', 'EXPENSE', 10.00, (SELECT id FROM user_account WHERE email = 'demo@financialapp.com')),
(UUID(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SPORT', 'Shorts', 'EXPENSE', 50.00, (SELECT id FROM user_account WHERE email = 'demo@financialapp.com')),
(UUID(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FOOD', 'BigMac', 'EXPENSE', 20.00, (SELECT id FROM user_account WHERE email = 'demo@financialapp.com')),
(UUID(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '', '', 'INCOME', 20.00, (SELECT id FROM user_account WHERE email = 'demo@financialapp.com'));
