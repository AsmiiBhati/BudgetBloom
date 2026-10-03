# BudgetBloom 🌱 

**BudgetBloom** is a Java-based personal finance management backend designed to help users track income and expenses, organize transactions by category, manage monthly budgets, and work toward savings goals.

The project is being built as a RESTful backend using **Spring Boot, Spring Data JPA, Hibernate, and PostgreSQL**.

---

## 🚀 Tech Stack

* **Java 21**
* **Spring Boot 3.5.16**
* **Spring Web**
* **Spring Data JPA**
* **Hibernate**
* **PostgreSQL 18.4**
* **Maven**
* **Lombok**
* **Jakarta Bean Validation**
* **Spring Security Crypto (BCrypt)**
* **Spring Boot DevTools**

---

## 🏗️ Architecture

BudgetBloom follows a layered backend architecture:

```text
Controller
    ↓
 Service
    ↓
Repository
    ↓
 Database
```

### Layers

* **Controller** — exposes REST API endpoints and handles HTTP requests/responses.
* **Service** — contains business logic and validation rules.
* **Repository** — handles database access through Spring Data JPA.
* **Entity** — represents the database tables and their relationships.
* **DTO** — separates API request/response models from persistence entities.

---

## 🗄️ Database Design

BudgetBloom uses **PostgreSQL** as its primary database.

The current schema contains **5 tables** and one PostgreSQL enum.

### Tables

#### `users`

Stores user account information.

| Column          | Description                |
| --------------- | -------------------------- |
| `id`            | UUID primary key           |
| `name`          | User's name                |
| `email`         | Unique email address       |
| `password_hash` | BCrypt-hashed password     |
| `created_at`    | Account creation timestamp |
| `updated_at`    | Last update timestamp      |

#### `categories`

Stores user-specific transaction categories.

| Column       | Description        |
| ------------ | ------------------ |
| `id`         | UUID primary key   |
| `user_id`    | Reference to user  |
| `name`       | Category name      |
| `type`       | Income or expense  |
| `created_at` | Creation timestamp |

A user cannot have duplicate category names.

#### `transactions`

Stores individual income and expense records.

| Column             | Description           |
| ------------------ | --------------------- |
| `id`               | UUID primary key      |
| `user_id`          | Reference to user     |
| `category_id`      | Reference to category |
| `amount`           | Transaction amount    |
| `type`             | Income or expense     |
| `description`      | Optional description  |
| `transaction_date` | Date of transaction   |
| `created_at`       | Creation timestamp    |
| `updated_at`       | Last update timestamp |

#### `budgets`

Stores monthly category-based budgets.

| Column        | Description                   |
| ------------- | ----------------------------- |
| `id`          | UUID primary key              |
| `user_id`     | Reference to user             |
| `category_id` | Reference to category         |
| `amount`      | Budget amount                 |
| `month_start` | First day of the budget month |
| `created_at`  | Creation timestamp            |
| `updated_at`  | Last update timestamp         |

A user can have only one budget for a particular category and month.

#### `savings_goals`

Stores user savings targets.

| Column           | Description            |
| ---------------- | ---------------------- |
| `id`             | UUID primary key       |
| `user_id`        | Reference to user      |
| `name`           | Goal name              |
| `target_amount`  | Desired savings amount |
| `current_amount` | Amount currently saved |
| `target_date`    | Optional target date   |
| `created_at`     | Creation timestamp     |
| `updated_at`     | Last update timestamp  |

---

## 🔖 Transaction Types

BudgetBloom uses a PostgreSQL enum:

```text
INCOME
EXPENSE
```

This keeps transaction and category types constrained to valid values.

---

## 🔐 Security

Password handling uses **BCrypt hashing** through Spring Security Crypto.

Passwords are never intended to be stored as plain text.

Authentication and authorization features such as **JWT-based authentication** are planned for a later stage.

---

## 📊 Planned Features

The backend is being developed incrementally.

Planned functionality includes:

* User registration and login
* BCrypt password hashing
* Category management
* Income and expense tracking
* Transaction filtering
* Monthly budget management
* Budget variance calculations
* Savings goal management
* Spending analysis by category
* Financial summary/aggregation APIs
* JWT authentication and authorization
* Global exception handling
* Request validation
* API documentation
* Automated tests

## ⚙️ Running Locally

### Prerequisites

Make sure the following are installed:

* Java 21
* Maven
* PostgreSQL 18+
* IntelliJ IDEA or another Java IDE

### 1. Clone the repository

```bash
git clone https://github.com/AsmiiBhati/BudgetBloom.git
cd BudgetBloom
```

### 2. Configure the database

Create a PostgreSQL database named:

```text
BudgetBloom
```

Run the project's database schema SQL against this database.

### 3. Configure the database password

BudgetBloom expects the PostgreSQL password through the environment variable:

```text
DB_PASSWORD
```

The password is intentionally not hardcoded in the application configuration.

### 4. Build the project

```bash
mvn clean compile
```

### 5. Run tests

```bash
mvn test
```

### 6. Start the application

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## 🔮 Future Improvements

Potential future improvements include:

* JWT-based authentication
* Role-based authorization
* Advanced spending analytics
* Recurring transactions
* Budget alerts
* Savings progress tracking
* Monthly financial dashboards
* Swagger/OpenAPI documentation
* Comprehensive unit and integration testing
* Frontend integration

---

**Made By - Asmii Bhati**
