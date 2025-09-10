-- Create Gold Database
CREATE DATABASE IF NOT EXISTS gold_final;
USE gold_final;

-- ========================================
-- DIMENSION: CUSTOMERS
-- ========================================
DROP TABLE IF EXISTS dim_customers;

CREATE TABLE dim_customers AS
SELECT DISTINCT
    c.customer_id,
    c.first_name,
    c.last_name,
    c.gender,
    c.marital_status,
    c.address,
    c.email,
    c.phone,
    c.created_at
FROM silver_final.cust_info c
WHERE c.customer_id IS NOT NULL;

-- ========================================
-- DIMENSION: PRODUCTS
-- ========================================
DROP TABLE IF EXISTS dim_products;

CREATE TABLE dim_products AS
SELECT DISTINCT
    p.product_id,
    p.product_name,
    p.product_description,
    p.product_price,
    p.product_stock,
    p.catalog_id
FROM silver_final.product p
WHERE p.product_id IS NOT NULL;

-- ========================================
-- DIMENSION: CATALOGS
-- ========================================
DROP TABLE IF EXISTS dim_catalogs;

CREATE TABLE dim_catalogs AS
SELECT DISTINCT
    cat.catalog_id,
    cat.catalog_name,
    cat.catalog_description
FROM silver_final.catalog cat
WHERE cat.catalog_id IS NOT NULL;

-- ========================================
-- DIMENSION: ORDERS
-- ========================================
DROP TABLE IF EXISTS dim_orders;

CREATE TABLE dim_orders AS
SELECT DISTINCT
    o.order_id,
    o.order_date,
    o.order_customer_id AS customer_id,  -- FK to dim_customers
    o.order_status
FROM silver_final.orders o
WHERE o.order_id IS NOT NULL;




-- ========================================
-- FACT TABLE: SALES
-- ========================================
USE gold_final;

DROP TABLE IF EXISTS fact_sales;

CREATE TABLE fact_sales AS
SELECT
    o.order_id,
    o.order_date,
    o.order_status,
    o.customer_id,              -- FK → dim_customers.customer_id
    p.product_id,               -- FK → dim_products.product_id
    p.catalog_id,               -- FK → dim_catalogs.catalog_id
    c.catalog_name,
    c.catalog_description,
    1 AS qty,                   -- Assuming quantity is 1 for each order item (update if you have actual qty)
    p.product_price AS price,
    (1 * p.product_price) AS amount
FROM silver_final.orders o
JOIN silver_final.product p 
    ON p.catalog_id = p.catalog_id  -- To link product to catalog
JOIN silver_final.catalog c 
    ON p.catalog_id = c.catalog_id
WHERE o.customer_id IS NOT NULL
  AND p.product_id IS NOT NULL
  AND c.catalog_id IS NOT NULL;

-- ========================================
-- VALIDATION QUERIES
-- ========================================
-- Row Count
SELECT COUNT(*) AS row_count 
FROM gold_final.fact_sales;

-- Preview Sample Records
SELECT * FROM gold_final.fact_sales 
LIMIT 20;

