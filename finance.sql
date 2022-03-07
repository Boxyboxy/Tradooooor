DROP DATABASE IF EXISTS finance;

CREATE DATABASE finance;

USE finance;

CREATE TABLE users (
  email varchar(64) NOT NULL,
  cash decimal(19, 2) DEFAULT 10000.00,
  PRIMARY KEY (email)
);

CREATE TABLE orders (
  order_id int NOT NULL AUTO_INCREMENT,
  email varchar(64) NOT NULL,
  symbol varchar(64) NOT NULL,
  shares int NOT NULL,
  price decimal(19, 2) NOT NULL,
  timestamp datetime,
  PRIMARY KEY(order_id),
  CONSTRAINT fk_email FOREIGN KEY(email) REFERENCES users(email)
);