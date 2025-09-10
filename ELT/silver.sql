CREATE DATABASE IF NOT EXISTS silver_final;
USE silver_final;



DROP TABLE IF EXISTS orders;

CREATE TABLE orders AS
WITH stg AS (
    SELECT
        CAST(order_id AS CHAR) AS order_id,
        -- Normalize and validate order_date
        CASE 
            WHEN TRIM(order_date) REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2}.*$'
            THEN STR_TO_DATE(LEFT(order_date, 19), '%Y-%m-%d %H:%i:%s')
            ELSE NULL
        END AS order_date,
        CAST(order_customer_id AS CHAR) AS order_customer_id,
        UPPER(TRIM(order_status)) AS order_status_raw
    FROM ordersdb.orders
),

norm AS (
    SELECT
        order_id,
        order_date,
        order_customer_id,
        -- Map statuses to clean, standardized values
        CASE 
            WHEN order_status_raw = 'COMPLETE' THEN 'Complete'
            WHEN order_status_raw = 'CLOSED' THEN 'Closed'
            WHEN order_status_raw = 'PENDING_PAYMENT' THEN 'Pending Payment'
            WHEN order_status_raw = 'PENDING' THEN 'Pending'
            WHEN order_status_raw = 'CANCELED' THEN 'Canceled'
            WHEN order_status_raw = 'PROCESSING' THEN 'Processing'
            WHEN order_status_raw = 'ON_HOLD' THEN 'On Hold'
            ELSE 'Unknown'
        END AS order_status
    FROM stg
),

latest AS (
    SELECT 
        n.*,
        ROW_NUMBER() OVER (PARTITION BY n.order_id ORDER BY n.order_date DESC) AS rn
    FROM norm n
)

SELECT 
    order_id,
    order_date,
    order_customer_id,
    order_status
FROM latest
WHERE rn = 1
  AND order_id IS NOT NULL;



DROP TABLE IF EXISTS customers;

CREATE TABLE customers AS
WITH stg AS (
    SELECT
        CAST(customer_id AS CHAR) AS customer_id,
        TRIM(address) AS address,
        LOWER(TRIM(email)) AS email, -- emails normalized to lowercase
        TRIM(first_name) AS first_name,
        TRIM(last_name) AS last_name,
        -- Clean phone numbers, remove non-digit characters
        REGEXP_REPLACE(TRIM(phone), '[^0-9]', '') AS phone_raw
    FROM ordersdb.customers
),

norm AS (
    SELECT
        customer_id,
        address,
        email,
        CONCAT(
            UPPER(SUBSTRING(first_name, 1, 1)),
            LOWER(SUBSTRING(first_name, 2))
        ) AS first_name, -- Proper case
        CONCAT(
            UPPER(SUBSTRING(last_name, 1, 1)),
            LOWER(SUBSTRING(last_name, 2))
        ) AS last_name, -- Proper case
        -- Format phone as ###-###-####
        CASE 
            WHEN LENGTH(phone_raw) = 10 THEN 
                CONCAT(
                    SUBSTRING(phone_raw, 1, 3), '-',
                    SUBSTRING(phone_raw, 4, 3), '-',
                    SUBSTRING(phone_raw, 7, 4)
                )
            ELSE phone_raw
        END AS phone
    FROM stg
),

latest AS (
    SELECT 
        n.*,
        ROW_NUMBER() OVER (PARTITION BY n.customer_id ORDER BY n.email DESC) AS rn
    FROM norm n
)

SELECT 
    customer_id,
    address,
    email,
    first_name,
    last_name,
    phone
FROM latest
WHERE rn = 1
  AND customer_id IS NOT NULL;



DROP TABLE IF EXISTS silver_final.catalog;

CREATE TABLE catalog AS
WITH stg AS (
    SELECT
        CAST(catalog_id AS CHAR) AS catalog_id,
        TRIM(catalog_description) AS catalog_description,
        TRIM(catalog_name) AS catalog_name
    FROM ordersdb.catalogs
),

norm AS (
    SELECT
        catalog_id,
        -- Proper case for description
        CONCAT(
            UPPER(SUBSTRING(catalog_description, 1, 1)),
            LOWER(SUBSTRING(catalog_description, 2))
        ) AS catalog_description,
        -- Proper case for catalog name
        CONCAT(
            UPPER(SUBSTRING(catalog_name, 1, 1)),
            LOWER(SUBSTRING(catalog_name, 2))
        ) AS catalog_name
    FROM stg
),

latest AS (
    SELECT 
        n.*,
        ROW_NUMBER() OVER (PARTITION BY n.catalog_id ORDER BY n.catalog_id DESC) AS rn
    FROM norm n
)

SELECT 
    catalog_id,
    catalog_description,
    catalog_name
FROM latest
WHERE rn = 1
  AND catalog_id IS NOT NULL;


DROP TABLE IF EXISTS product;

CREATE TABLE product AS
WITH stg AS (
    SELECT
        CAST(product_id AS CHAR) AS product_id,
        TRIM(product_description) AS product_description,
        TRIM(product_name) AS product_name,
        CAST(product_price AS DECIMAL(10,2)) AS product_price,
        CAST(product_stock AS DECIMAL) AS product_stock,
        CAST(catalog_id AS CHAR) AS catalog_id
    FROM ordersdb.products
),

norm AS (
    SELECT
        product_id,
        -- Proper case for description
        CONCAT(
            UPPER(SUBSTRING(product_description, 1, 1)),
            LOWER(SUBSTRING(product_description, 2))
        ) AS product_description,
        
        -- Proper case for product name
        CONCAT(
            UPPER(SUBSTRING(product_name, 1, 1)),
            LOWER(SUBSTRING(product_name, 2))
        ) AS product_name,

        product_price,
        CASE 
            WHEN product_stock < 0 THEN 0 -- Ensure no negative stock
            ELSE product_stock
        END AS product_stock,
        
        catalog_id
    FROM stg
),

latest AS (
    SELECT 
        n.*,
        ROW_NUMBER() OVER (PARTITION BY n.product_id ORDER BY n.product_id DESC) AS rn
    FROM norm n
)

SELECT 
    product_id,
    product_description,
    product_name,
    product_price,
    product_stock,
    catalog_id
FROM latest
WHERE rn = 1
  AND product_id IS NOT NULL;
