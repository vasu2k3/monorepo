from pyspark.sql import SparkSession
from pyspark.sql.functions import col, lit

# ======================================
# 1. Initialize Spark Session
# ======================================
spark = SparkSession.builder \
    .appName("Orders ETL - Gold Layer") \
    .config("spark.sql.warehouse.dir", "/user/hive/warehouse") \
    .enableHiveSupport() \
    .getOrCreate()

# ======================================
# 2. Load Silver Layer Tables
# ======================================
silver_db = "silver_final"

orders_df = spark.table(f"{silver_db}.orders")
customers_df = spark.table(f"{silver_db}.customers")
products_df = spark.table(f"{silver_db}.product")
catalog_df = spark.table(f"{silver_db}.catalog")

# ======================================
# 3. Create Gold Layer - Dimensions
# ======================================
gold_db = "gold_final"

# --- Customers Dimension ---
dim_customers_df = customers_df.select(
    col("customer_id"),
    col("first_name"),
    col("last_name"),
    col("gender"),
    col("marital_status"),
    col("address"),
    col("email"),
    col("phone"),
    col("created_at")
).distinct()

dim_customers_df.write.mode("overwrite").saveAsTable(f"{gold_db}.dim_customers")

# --- Products Dimension ---
dim_products_df = products_df.select(
    col("product_id"),
    col("product_name"),
    col("product_description"),
    col("product_price"),
    col("product_stock"),
    col("catalog_id")
).distinct()

dim_products_df.write.mode("overwrite").saveAsTable(f"{gold_db}.dim_products")

# --- Catalogs Dimension ---
dim_catalogs_df = catalog_df.select(
    col("catalog_id"),
    col("catalog_name"),
    col("catalog_description")
).distinct()

dim_catalogs_df.write.mode("overwrite").saveAsTable(f"{gold_db}.dim_catalogs")

# --- Orders Dimension ---
dim_orders_df = orders_df.select(
    col("order_id"),
    col("order_date"),
    col("order_customer_id").alias("customer_id"),
    col("order_status")
).distinct()

dim_orders_df.write.mode("overwrite").saveAsTable(f"{gold_db}.dim_orders")

# ======================================
# 4. Create Gold Layer - Fact Table
# ======================================
fact_sales_df = (
    orders_df.alias("o")
    .join(products_df.alias("p"), col("p.catalog_id") == col("p.catalog_id"), "inner")
    .join(catalog_df.alias("c"), col("p.catalog_id") == col("c.catalog_id"), "inner")
    .select(
        col("o.order_id"),
        col("o.order_date"),
        col("o.order_status"),
        col("o.order_customer_id").alias("customer_id"),
        col("p.product_id"),
        col("p.catalog_id"),
        col("c.catalog_name"),
        col("c.catalog_description"),
        lit(1).alias("qty"),  # default quantity
        col("p.product_price").alias("price"),
        (lit(1) * col("p.product_price")).alias("amount")
    )
    .where(
        col("o.order_customer_id").isNotNull() &
        col("p.product_id").isNotNull() &
        col("c.catalog_id").isNotNull()
    )
)

fact_sales_df.write.mode("overwrite").saveAsTable(f"{gold_db}.fact_sales")

# ======================================
# 5. Validation Queries
# ======================================

# Row Count
row_count = spark.sql(f"SELECT COUNT(*) AS row_count FROM {gold_db}.fact_sales")
row_count.show()

# Sample Records
sample_data = spark.sql(f"SELECT * FROM {gold_db}.fact_sales LIMIT 20")
sample_data.show(truncate=False)
