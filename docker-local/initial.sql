CREATE TABLE user_account (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              external_id CHAR(36) NOT NULL,
                              email VARCHAR(255) NOT NULL UNIQUE,
                              name VARCHAR(255),
                              created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              modify_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              version BIGINT NOT NULL,
                              user_role VARCHAR(255),
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

INSERT INTO user_account (external_id, email, name, version, user_role, password)
VALUES
    (UUID(), 'admin@financialapp.com', 'admin', 1, 'ADMIN', '$2a$12$BSTIdBUX.cpPiEZGU8bWOe1OJ1gUNB.fBB91naE55icapsDMd7n12'),
    (UUID(), 'demo@financialapp.com', 'demo', 1, 'USER', '$2a$12$kL1hhmgQAroSRhL5IjZVweL.hlTVoWub3Kp4UABesOZbUf8k.FmgS'),
    (UUID(), 'user@financialapp.com', 'user', 1, 'USER', '$2a$10$iL63eKi2Caq10WQ7iG6N3uP.zpO0e0axnKDn1DW9/osfqBtCxp2yK');

INSERT INTO bank_account (external_id, user_id, account_version, account_name, account_number, account_balance)
VALUES
    (UUID(), (SELECT id FROM user_account WHERE email = 'admin@financialapp.com'), 1, 'Admin Account', '1234567890', 1000.00),
    (UUID(), (SELECT id FROM user_account WHERE email = 'demo@financialapp.com'), 1, 'Demo Bank Account', '0987654321', 500.00),
    (UUID(), (SELECT id FROM user_account WHERE email = 'user@financialapp.com'), 1, 'User Bank Account', '0987654331', 500.00);

INSERT INTO expense (external_id, version, created_on, modify_on, expense_item, expense_category, expense_type, expense, user_id)
VALUES
    (UUID(), 1, NOW(), NOW(), 'Home Expense', 'HOME', 'EXPENSE', 15.50, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (UUID(), 1, NOW(), NOW(), 'Work Expense', 'WORK', 'EXPENSE', 2.80, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (UUID(), 1, NOW(), NOW(), 'Other Expense', 'OTHER', 'EXPENSE', 10.00, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (UUID(), 1, NOW(), NOW(), 'Sports Equipment', 'SPORT', 'EXPENSE', 50.00, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (UUID(), 1, NOW(), NOW(), 'Food', 'FOOD', 'EXPENSE', 20.00, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (UUID(), 1, NOW(), NOW(), 'Income Other', 'OTHER', 'INCOME', 20.00, (SELECT id FROM user_account WHERE email = 'user@financialapp.com'));

GRANT ALL PRIVILEGES ON financial.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
