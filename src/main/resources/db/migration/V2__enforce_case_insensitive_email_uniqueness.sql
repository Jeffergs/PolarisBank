ALTER TABLE customers
DROP CONSTRAINT customers_email_key;

CREATE UNIQUE INDEX uk_customers_email_lower
    ON customers (LOWER(email));