INSERT INTO
  users (email)
VALUES
  ('eugenengboxiang@gmail.com');

INSERT INTO
  orders (email, symbol, shares, price, timestamp)
VALUES
  (
    'eugenengboxiang@gmail.com',
    'mrna',
    '20',
    '150.00',
    '2022-02-20 01:05:05'
  );

UPDATE
  users
SET
  cash = 10000.00 -(20 * 150.00)
WHERE
  email = 'eugenengboxiang@gmail.com';

SELECT
  cash
FROM
  users
WHERE
  email = 'eugenengboxiang@gmail.com';

SELECT
  *
FROM
  orders
WHERE
  email = 'eugenengboxiang@gmail.com';

-- table with total quantity of shares
SELECT
  symbol,
  SUM(shares)
FROM
  orders
WHERE
  email = "eugenengboxiang@gmail.com"
  AND symbol = "MRNA";

-- table with total_cost and average_cost_per_share
SELECT
  symbol,
  SUM(shares) AS quantity,
  SUM(shares * price) AS total_cost,
  (SUM(shares * price) / SUM(shares)) AS average_cost_per_share
FROM
  orders
WHERE
  email = "eugenengboxiang@gmail.com"
GROUP BY
  symbol;