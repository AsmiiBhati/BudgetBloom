CREATE TYPE transaction_type AS ENUM ('INCOME','EXPENSE');

CREATE TABLE users (
                       id            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                       name          varchar(100) NOT NULL,
                       email         varchar(255) NOT NULL UNIQUE,
                       password_hash varchar(255) NOT NULL,
                       created_at    timestamp NOT NULL DEFAULT now(),
                       updated_at    timestamp NOT NULL DEFAULT now()
);

CREATE TABLE categories (
                            id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                            user_id    uuid NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            name       varchar(100) NOT NULL,
                            type       transaction_type NOT NULL,
                            created_at timestamp NOT NULL DEFAULT now(),
                            UNIQUE (user_id, name)
);

CREATE TABLE transactions (
                              id               uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                              user_id          uuid NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                              category_id      uuid NOT NULL REFERENCES categories(id),
                              amount           numeric(15,2) NOT NULL CHECK (amount > 0),
                              type             transaction_type NOT NULL,
                              description      varchar(255),
                              transaction_date date NOT NULL,
                              created_at       timestamp NOT NULL DEFAULT now(),
                              updated_at       timestamp NOT NULL DEFAULT now()
);

-- One budget per user + expense category + month (month_start is always the 1st of the month)
CREATE TABLE budgets (
                         id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                         user_id     uuid NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                         category_id uuid NOT NULL REFERENCES categories(id),
                         amount      numeric(15,2) NOT NULL CHECK (amount > 0),
                         month_start date NOT NULL CHECK (EXTRACT(DAY FROM month_start) = 1),
                         created_at  timestamp NOT NULL DEFAULT now(),
                         updated_at  timestamp NOT NULL DEFAULT now(),
                         UNIQUE (user_id, category_id, month_start)
);

CREATE TABLE savings_goals (
                               id             uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                               user_id        uuid NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               name           varchar(100) NOT NULL,
                               target_amount  numeric(15,2) NOT NULL CHECK (target_amount > 0),
                               current_amount numeric(15,2) NOT NULL DEFAULT 0 CHECK (current_amount >= 0),
                               target_date    date,
                               created_at     timestamp NOT NULL DEFAULT now(),
                               updated_at     timestamp NOT NULL DEFAULT now()
);

CREATE INDEX idx_transactions_user_date ON transactions (user_id, transaction_date);
CREATE INDEX idx_transactions_category  ON transactions (category_id);
CREATE INDEX idx_budgets_user_month     ON budgets (user_id, month_start);
CREATE INDEX idx_goals_user             ON savings_goals (user_id);