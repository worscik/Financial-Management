CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS app;

SET search_path TO app, public;

CREATE TABLE app.user_account (
                              id BIGSERIAL PRIMARY KEY,
                              external_id UUID NOT NULL,
                              email VARCHAR(255) NOT NULL UNIQUE,
                              name VARCHAR(255),
                              created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              modify_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              version BIGINT NOT NULL,
                              user_role VARCHAR(255),
                              password VARCHAR(255)
);

CREATE TABLE app.bank_account (
                              id BIGSERIAL PRIMARY KEY,
                              external_id UUID NOT NULL,
                              user_id BIGINT NOT NULL,
                              created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              modify_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              account_version BIGINT NOT NULL,
                              account_name VARCHAR(255) NOT NULL,
                              account_number UUID NOT NULL,
                              account_balance DECIMAL(19, 4),
                              currency VARCHAR(255),
                              FOREIGN KEY (user_id) REFERENCES user_account(id)
);

CREATE TABLE app.expense (
                         id BIGSERIAL PRIMARY KEY,
                         external_id UUID NOT NULL,
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
    (gen_random_uuid(), 'admin@financialapp.com', 'admin', 1, 'ADMIN', '$2a$12$YsXuaGHGUoXSdwsMtzenY.p2A5bB.chYlBYcdCQ..QWXlKOOOMqHG'),
    (gen_random_uuid(), 'demo@financialapp.com', 'demo', 1, 'USER', '$2a$12$kIB0Vsuy9Gqm/jz.LJdZPu4Nzh9Gn33CsvQ.D510wnH9.Zg4uHNzq'),
    (gen_random_uuid(), 'user@financialapp.com', 'user', 1, 'USER', '$2a$12$8tokTl6alcZURDFAWSqZ4et6eSL6jy33RLpEH8y470sN7CGWYNg/');

INSERT INTO bank_account (external_id, user_id, account_version, account_name, account_number, account_balance, currency)
VALUES
    (gen_random_uuid(), (SELECT id FROM user_account WHERE email = 'admin@financialapp.com'), 1, 'Admin Account', '9e1f957c-3239-43a4-bd36-550e2ebc03ac'::uuid, 1000.00, 'USD'),
    (gen_random_uuid(), (SELECT id FROM user_account WHERE email = 'demo@financialapp.com'), 1, 'Demo Bank Account', 'ea8ff5cf-6b4e-4e1f-8629-a6806da85f55'::uuid, 500.00, 'PLN'),
    (gen_random_uuid(), (SELECT id FROM user_account WHERE email = 'user@financialapp.com'), 1, 'User Bank Account', '14d67eea-a317-4b6e-88e7-bc5da229f280'::uuid, 500.00, 'USD');

INSERT INTO expense (external_id, version, created_on, modify_on, expense_item, expense_category, expense_type, expense, user_id)
VALUES
    (gen_random_uuid(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Home Expense', 'HOME', 'EXPENSE', 15.50, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (gen_random_uuid(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Work Expense', 'WORK', 'EXPENSE', 2.80, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (gen_random_uuid(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Other Expense', 'OTHER', 'EXPENSE', 10.00, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (gen_random_uuid(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Sports Equipment', 'SPORT', 'EXPENSE', 50.00, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (gen_random_uuid(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Food', 'FOOD', 'EXPENSE', 20.00, (SELECT id FROM user_account WHERE email = 'user@financialapp.com')),
    (gen_random_uuid(), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Income Other', 'OTHER', 'INCOME', 20.00, (SELECT id FROM user_account WHERE email = 'user@financialapp.com'));
