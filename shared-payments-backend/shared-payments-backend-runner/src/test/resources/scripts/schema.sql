-- schema.sql

-- Make sure we're using our `shared_payments` database
\c shared_payments;

-- We can create our group table
CREATE TABLE IF NOT EXISTS group_table (
  id BIGINT PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

-- We can create our user table
CREATE TABLE IF NOT EXISTS user_table (
  id BIGINT PRIMARY KEY,
  name VARCHAR NOT NULL,
  group_id BIGINT REFERENCES group_table(id)
);

-- We can create our payment table
CREATE TABLE IF NOT EXISTS payment (
  id BIGINT PRIMARY KEY,
  description TEXT NOT NULL,
  amount BIGINT NOT NULL,
  currency VARCHAR NOT NULL,
  payment_date BIGINT NOT NULL,
  user_id BIGINT REFERENCES user_table(id)
);

-- We can insert sequences
CREATE SEQUENCE group_sequence START 301;
CREATE SEQUENCE user_sequence START 501;
CREATE SEQUENCE payment_sequence START 301;


-- We can insert groups
INSERT INTO group_table(id, name)	VALUES (1, 'Users');
INSERT INTO group_table(id, name)	VALUES (2, 'Users who loves Micronaut');

-- We can insert users
INSERT INTO user_table(id, name, group_id)	VALUES (100, 'David Romero', 1);
INSERT INTO user_table(id, name, group_id)	VALUES (200, 'User 2', 1);
INSERT INTO user_table(id, name, group_id)	VALUES (300, 'User 3', 1);
INSERT INTO user_table(id, name, group_id)	VALUES (400, 'User 4', 2);

-- We can insert payments
INSERT INTO payment(id, description, amount, currency, payment_date, user_id)	VALUES (100, 'Burger King', 200000, '€', 1600341441000, 100);
INSERT INTO payment(id, description, amount, currency, payment_date, user_id)	VALUES (200, 'Supermarket', 456100, '€', 1600341486000, 200);

