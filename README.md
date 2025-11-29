# Brazilian E-Commerce Database Analyzer

COMP 3380 - Database Project
**Group Members:** M Tausif Tajwar Bhuiyan, Luke Rodriguez, Dinh Nhat Nguyen
**Date:** November 2025

## Overview

This is a menu-driven command-line application implemented in Java that provides analysts with secure and efficient access to the Brazilian E-Commerce database. The application allows users to execute analytical queries across multiple categories including market analysis, seller performance, customer behavior, order quality, and payment analysis.

## Project Structure

```
Database_project/
├── src/                                    # Java source files
│   ├── BrazilianECommerceAnalyzer.java    # Main application class
│   ├── DatabaseConnection.java             # Database connection management
│   ├── QueryManager.java                   # All SQL queries
│   ├── QueryExecutor.java                  # Prepared statement execution
│   ├── ResultFormatter.java                # ASCII table formatting
│   ├── MenuInterface.java                  # Menu navigation
│   └── CSVDataLoader.java                  # Data loading from CSV
├── data/                                   # CSV data files
│   ├── olist_customers_dataset.csv
│   ├── olist_orders_dataset.csv
│   ├── olist_order_items_dataset.csv
│   ├── olist_order_payments_dataset.csv
│   ├── olist_order_reviews_dataset.csv
│   ├── olist_products_dataset.csv
│   ├── olist_sellers_dataset.csv
│   ├── olist_geolocation_dataset.csv
│   └── product_category_name_translation.csv
├── DemoJavaProjectRelease/                 # JDBC driver and demo
│   ├── mssql-jdbc-11.2.0.jre11.jar
│   └── auth.cfg                            # Database credentials
├── schema.sql                              # Database schema creation script
├── Makefile                                # Build automation
├── Stage 5 final.pdf                       # Query specifications
└── Stage 6.pdf                             # Interface design specification
```

## Prerequisites

1. **Java Development Kit (JDK)** - Version 11 or higher
2. **SQL Server Database** - Access to uranium.cs.umanitoba.ca
3. **Database Credentials** - Valid username and password
4. **JDBC Driver** - Microsoft SQL Server JDBC Driver (included)

## Setup Instructions

### 1. Database Setup

First, create the database schema by running the SQL script:

```bash
# Connect to SQL Server and run:
sqlcmd -S uranium.cs.umanitoba.ca -d cs3380 -U your_username -P your_password -i schema.sql
```

Alternatively, execute the schema.sql file using any SQL Server client.

### 2. Configure Database Credentials

Create or update the `auth.cfg` file in the `DemoJavaProjectRelease/` directory:

```
username=your_username
password=your_password
```

**Important:** Keep this file secure and do not commit it to version control.

### 3. Compile the Application

```bash
make compile
```

This will compile all Java source files and place the .class files in the `bin/` directory.

### 4. Populate the Database (Optional)

If the database is empty, you can populate it using the application's Database Management menu (Option 6 > Option 2).

## Running the Application

### Using Make

```bash
make run
```

### Manual Execution

```bash
java -cp "bin:DemoJavaProjectRelease/mssql-jdbc-11.2.0.jre11.jar" BrazilianECommerceAnalyzer
```

## Application Features

### Main Menu Categories

1. **Market and Sales Analysis**
   - High-Value Customer States
   - Top Selling Categories by Revenue
   - States with Customers but No Orders
   - Highest Sales Geolocation

2. **Seller Performance Analysis**
   - Seller Success Rate by Review Score
   - Unused Product Catalog
   - States with Customers but No Sellers
   - Single-Product Sellers Count

3. **Customer Behavior Analysis**
   - Repeat Purchase Customers
   - Average Time Between Orders

4. **Order and Review Quality Analysis**
   - Order Review Rate
   - Worst Rated Product Category
   - Orders Paid in Full
   - Review Score Extremes

5. **Payment and Transaction Analysis**
   - Most Common Payment Type by State
   - Average Installments per Payment Type

6. **Database Management**
   - Clear All Data
   - Repopulate from CSV Files

7. **Help / Instructions**

8. **Exit Application**

### Security Features

- **SQL Injection Prevention:** All queries use prepared statements
- **Input Validation:** User inputs are sanitized before processing
- **No Direct SQL Access:** Users cannot execute arbitrary SQL
- **Credential Protection:** Database credentials stored in separate config file

### Result Display

- Results are displayed in formatted ASCII tables
- Pagination: 25 rows per page
- Decimal formatting for monetary values
- Null values displayed as "N/A"
- Query execution time displayed

## Data Loading

The application can load data from CSV files in the `data/` directory. The loading process:

1. Creates Brazilian state records
2. Loads geolocation data (~1M records)
3. Loads product categories
4. Loads customers (~100K records)
5. Loads sellers (~3K records)
6. Loads products (~33K records)
7. Loads orders (~100K records)
8. Loads order items (~113K records)
9. Loads order payments (~104K records)
10. Loads order reviews (~99K records)

**Note:** Full database population may take several minutes depending on the server connection.

## Database Schema

The database follows the Enhanced ER Diagram specified in Stage 6:

- **STATES** - Brazilian state information
- **GEOLOCATION** - Geographic coordinates for zip codes
- **CUSTOMERS** - Customer information
- **SELLERS** - Seller information
- **CATEGORIES** - Product categories (Portuguese and English)
- **PRODUCTS** - Product catalog
- **ORDERS** - Customer orders
- **ORDER_ITEMS** - Items within orders (weak entity)
- **ORDER_PAYMENTS** - Payment transactions (weak entity)
- **ORDER_REVIEWS** - Customer reviews

## Troubleshooting

### Connection Issues

**Problem:** Cannot connect to database
**Solution:**
- Verify auth.cfg contains correct credentials
- Check network connectivity to uranium.cs.umanitoba.ca
- Ensure firewall allows connections on port 1433

### Compilation Errors

**Problem:** JDBC driver not found
**Solution:**
- Verify mssql-jdbc-11.2.0.jre11.jar is in DemoJavaProjectRelease/
- Check Makefile classpath settings

### Data Loading Failures

**Problem:** CSV file not found
**Solution:**
- Ensure all CSV files are in the data/ directory
- Verify file names match exactly (case-sensitive)

## Development

### Building

```bash
make clean    # Remove compiled files
make compile  # Compile source files
make run      # Compile and run
```

### Code Structure

- **Object-Oriented Design:** Separate classes for each responsibility
- **Prepared Statements:** All queries use parameterized SQL
- **Error Handling:** Comprehensive exception handling and user feedback
- **Modular Queries:** All SQL queries centralized in QueryManager

## Authors

- M Tausif Tajwar Bhuiyan
- Luke Rodriguez
- Dinh Nhat Nguyen

## License

This project is created for educational purposes as part of COMP 3380 at the University of Manitoba.

## Acknowledgments

- Dataset: Brazilian E-Commerce Public Dataset by Olist
- Database Server: University of Manitoba CS Department
- JDBC Driver: Microsoft SQL Server JDBC Driver
