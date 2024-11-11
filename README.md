# Financial-Management

Financial Management is an application that helps users manage their finances, track expenses, and handle account information. Below are the details for available login credentials, key endpoints, and how to interact with each component of the application.

---

## Available Logins

Use the following credentials to access the application:

- **Admin Login:**
    - **Email:** admin@financialapp.com
    - **Password:** admin

- **Demo Login:**
    - **Email:** demo@financialapp.com
    - **Password:** demo

---

## Endpoints

### Authentication
Authenticate users to generate a JWT token.

- **Endpoint:** `POST http://localhost:8080/login`
- **Description:** Send login credentials to receive a JWT token for authorized access.

### Account Management
Endpoints related to user account operations.

- **Register:**
    - **Endpoint:** `POST http://localhost:8080/register`
    - **Description:** Register a new user account.

- **Update Account:**
    - **Endpoint:** `PUT http://localhost:8080`
    - **Description:** Update account information.

- **Get Account by Email:**
    - **Endpoint:** `GET http://localhost:8080/email`
    - **Description:** Retrieve account details using the user's email.

- **Get Account by ID:**
    - **Endpoint:** `GET http://localhost:8080/id/{int_id}`
    - **Description:** Retrieve account details using the account's integer ID.

- **Delete Account:**
    - **Endpoint:** `DELETE http://localhost:8080/{externalId}`
    - **Description:** Delete an account by its external ID.

### Expense Management
Endpoints to manage and view expenses.

- **Add Expense:**
    - **Endpoint:** `POST http://localhost:8080/`
    - **Description:** Add a new expense entry.

- **Update Expense:**
    - **Endpoint:** `PUT http://localhost:8080/`
    - **Description:** Update an existing expense entry.

- **List Expenses:**
    - **Endpoint:** `GET http://localhost:8080/list`
    - **Description:** Retrieve a list of all expenses.

- **Get Expense by External ID:**
    - **Endpoint:** `GET http://localhost:8080/externalId/{externalId}`
    - **Description:** Retrieve a specific expense using its external ID.

- **Get Expense Categories:**
    - **Endpoint:** `GET http://localhost:8080/categories`
    - **Description:** Retrieve all available expense categories.

### Bank Account Management
Endpoints for managing bank accounts and retrieving account balance information.

- **Add Bank Account:**
    - **Endpoint:** `POST http://localhost:8080/`
    - **Description:** Add a new bank account.

- **Update Bank Account:**
    - **Endpoint:** `PUT http://localhost:8080/`
    - **Description:** Update an existing bank account.

- **Bank Account Operations (TODO):**
    - **Description:** Placeholder for additional bank account features in future releases.

- **Get Bank Balance:**
    - **Endpoint:** `GET http://localhost:8080/bankBalance`
    - **Description:** Retrieve the current balance of the bank account.

---

## JSON Web Token (JWT) Authentication
The application uses JWT for secure API access. Once authenticated, include the JWT token in the `Authorization` header of all requests to protected endpoints as follows:

```http
Authorization: Bearer <token>
