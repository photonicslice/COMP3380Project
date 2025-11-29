-- Brazilian E-Commerce Database Schema
-- COMP 3380 - Database Project
-- Creates all tables based on the ER diagram in Stage 6

-- Drop existing tables (in reverse order of dependencies)
IF OBJECT_ID('ORDER_REVIEWS', 'U') IS NOT NULL DROP TABLE ORDER_REVIEWS;
IF OBJECT_ID('ORDER_PAYMENTS', 'U') IS NOT NULL DROP TABLE ORDER_PAYMENTS;
IF OBJECT_ID('ORDER_ITEMS', 'U') IS NOT NULL DROP TABLE ORDER_ITEMS;
IF OBJECT_ID('ORDERS', 'U') IS NOT NULL DROP TABLE ORDERS;
IF OBJECT_ID('PRODUCTS', 'U') IS NOT NULL DROP TABLE PRODUCTS;
IF OBJECT_ID('CATEGORIES', 'U') IS NOT NULL DROP TABLE CATEGORIES;
IF OBJECT_ID('SELLERS', 'U') IS NOT NULL DROP TABLE SELLERS;
IF OBJECT_ID('CUSTOMERS', 'U') IS NOT NULL DROP TABLE CUSTOMERS;
IF OBJECT_ID('GEOLOCATION', 'U') IS NOT NULL DROP TABLE GEOLOCATION;
IF OBJECT_ID('STATES', 'U') IS NOT NULL DROP TABLE STATES;

-- Create STATES table
CREATE TABLE STATES (
    state_code VARCHAR(2) PRIMARY KEY,
    state_name VARCHAR(50) NOT NULL,
    region VARCHAR(20)
);

-- Create GEOLOCATION table
CREATE TABLE GEOLOCATION (
    geolocation_id INT IDENTITY(1,1) PRIMARY KEY,
    zip_code_prefix VARCHAR(5) NOT NULL,
    geolocation_lat DECIMAL(10, 8),
    geolocation_lng DECIMAL(11, 8),
    geolocation_city VARCHAR(100),
    geolocation_state VARCHAR(2)
);

-- Create CUSTOMERS table
CREATE TABLE CUSTOMERS (
    customer_id VARCHAR(50) PRIMARY KEY,
    customer_unique_id VARCHAR(50),
    customer_zip_code_prefix VARCHAR(5),
    customer_city VARCHAR(100),
    customer_state VARCHAR(2)
);

-- Create SELLERS table
CREATE TABLE SELLERS (
    seller_id VARCHAR(50) PRIMARY KEY,
    seller_zip_code_prefix VARCHAR(5),
    seller_city VARCHAR(100),
    seller_state VARCHAR(2)
);

-- Create CATEGORIES table
CREATE TABLE CATEGORIES (
    category_id INT IDENTITY(1,1) PRIMARY KEY,
    category_name_portuguese VARCHAR(100),
    category_name_english VARCHAR(100)
);

-- Create PRODUCTS table
CREATE TABLE PRODUCTS (
    product_id VARCHAR(50) PRIMARY KEY,
    category_name_portuguese VARCHAR(100),
    product_name_length INT,
    product_description_length INT,
    product_photos_qty INT,
    product_weight_g INT,
    product_length_cm INT,
    product_height_cm INT,
    product_width_cm INT,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id)
);

-- Create ORDERS table
CREATE TABLE ORDERS (
    order_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50),
    order_status VARCHAR(20),
    order_purchase_timestamp DATETIME,
    order_approved_at DATETIME,
    order_delivered_carrier_date DATETIME,
    order_delivered_customer_date DATETIME,
    order_estimated_delivery_date DATETIME,
    FOREIGN KEY (customer_id) REFERENCES CUSTOMERS(customer_id)
);

-- Create ORDER_ITEMS table (weak entity - composite key)
CREATE TABLE ORDER_ITEMS (
    order_id VARCHAR(50),
    order_item_seq INT,
    product_id VARCHAR(50),
    seller_id VARCHAR(50),
    shipping_limit_date DATETIME,
    price DECIMAL(10, 2),
    freight_value DECIMAL(10, 2),
    PRIMARY KEY (order_id, order_item_seq),
    FOREIGN KEY (order_id) REFERENCES ORDERS(order_id),
    FOREIGN KEY (product_id) REFERENCES PRODUCTS(product_id),
    FOREIGN KEY (seller_id) REFERENCES SELLERS(seller_id)
);

-- Create ORDER_PAYMENTS table (weak entity - composite key)
CREATE TABLE ORDER_PAYMENTS (
    order_id VARCHAR(50),
    payment_sequential INT,
    payment_type VARCHAR(30),
    payment_installments INT,
    payment_value DECIMAL(10, 2),
    PRIMARY KEY (order_id, payment_sequential),
    FOREIGN KEY (order_id) REFERENCES ORDERS(order_id)
);

-- Create ORDER_REVIEWS table
CREATE TABLE ORDER_REVIEWS (
    review_record_id INT IDENTITY(1,1) PRIMARY KEY,  -- Auto-increment ID as PK
    review_id VARCHAR(50),  -- Removed PRIMARY KEY constraint (has duplicates in source data)
    order_id VARCHAR(50),
    review_score INT,
    review_comment_title VARCHAR(100),
    review_comment_message VARCHAR(MAX),
    review_creation_date DATETIME,
    review_answer_timestamp DATETIME,
    FOREIGN KEY (order_id) REFERENCES ORDERS(order_id)
);

-- Create indexes for performance
CREATE INDEX idx_customer_zip ON CUSTOMERS(customer_zip_code_prefix);
CREATE INDEX idx_seller_zip ON SELLERS(seller_zip_code_prefix);
CREATE INDEX idx_geo_zip ON GEOLOCATION(zip_code_prefix);
CREATE INDEX idx_geo_state ON GEOLOCATION(geolocation_state);
CREATE INDEX idx_order_customer ON ORDERS(customer_id);
CREATE INDEX idx_order_status ON ORDERS(order_status);
CREATE INDEX idx_order_items_product ON ORDER_ITEMS(product_id);
CREATE INDEX idx_order_items_seller ON ORDER_ITEMS(seller_id);
CREATE INDEX idx_product_category ON PRODUCTS(category_name_portuguese);
CREATE INDEX idx_review_order ON ORDER_REVIEWS(order_id);
CREATE INDEX idx_review_score ON ORDER_REVIEWS(review_score);

PRINT 'Database schema created successfully.';
