# Quick Start Guide - Brazilian E-Commerce Database Analyzer

## Initial Setup (First Time Only)

### Step 1: Create the Database Schema

Connect to your SQL Server database and run the schema creation script:

```bash
# Using sqlcmd
sqlcmd -S uranium.cs.umanitoba.ca -d cs3380 -U YOUR_USERNAME -P YOUR_PASSWORD -i schema.sql
```

Or use any SQL Server client (SQL Server Management Studio, Azure Data Studio, etc.) to execute the `schema.sql` file.

### Step 2: Configure Database Credentials

Make sure the `auth.cfg` file exists in the `DemoJavaProjectRelease/` directory with your credentials:

```
username=YOUR_USERNAME
password=YOUR_PASSWORD
```

If the file doesn't exist, create it with the above content.

### Step 3: Compile the Application

```bash
make compile
```

Or manually:

```bash
javac -d bin -cp ".:DemoJavaProjectRelease/mssql-jdbc-11.2.0.jre11.jar" src/*.java
```

## Running the Application

### Quick Run

```bash
make run
```

Or manually:

```bash
java -cp "bin:DemoJavaProjectRelease/mssql-jdbc-11.2.0.jre11.jar" BrazilianECommerceAnalyzer
```

## First Time Usage

When you run the application for the first time, the database will be empty. Follow these steps:

1. **Run the application**
   ```bash
   make run
   ```

2. **Select Database Management** - Choose option `6` from the main menu

3. **Populate Database** - Choose option `2` to load data from CSV files
   - This will take 2-5 minutes depending on your connection
   - You'll see progress indicators as each table is loaded

4. **Return to Main Menu** - Press `3` to go back

5. **Start Running Queries!** - Now you can explore any of the analytical queries

## Example Usage Session

```
===============================================================================
                BRAZILIAN E-COMMERCE DATABASE ANALYZER
                      COMP 3380 - Database Project
===============================================================================

Main Menu:
----------
1. Market and Sales Analysis
2. Seller Performance Analysis
3. Customer Behavior Analysis
4. Order and Review Quality Analysis
5. Payment and Transaction Analysis
6. Database Management
7. Help / Instructions
8. Exit Application

Enter your choice (1-8): 1

===============================================================================
Main Menu > 1. Market and Sales Analysis
===============================================================================

Market and Sales Analysis - Available Queries:
-----------------------------------------------
1. High-Value Customer States
   -> Shows states with highest average order values

2. Top Selling Categories by Revenue
   -> Identifies product categories generating most revenue

3. States with Customers but No Orders
   -> Finds untapped markets with registered users

4. Highest Sales Geolocation
   -> Identifies zip code with most customer orders

5. Back to Main Menu

Enter your choice (1-5): 2

Processing query... Please wait.
[Results displayed in formatted ASCII table]
Query executed in 1.47 seconds.

Press Enter to continue...
```

## Common Operations

### View Top Selling Categories
1. Main Menu → `1` (Market Analysis)
2. Select `2` (Top Selling Categories)

### Check Seller Performance
1. Main Menu → `2` (Seller Performance)
2. Select `1` (Seller Success Rate)

### Analyze Customer Behavior
1. Main Menu → `3` (Customer Behavior)
2. Select `1` (Repeat Purchase Customers)

### Clear and Reload Database
1. Main Menu → `6` (Database Management)
2. Select `1` (Clear All Data)
3. Type `DELETE ALL DATA` to confirm
4. Select `2` (Repopulate Database)

## Tips

- **Pagination:** Press Enter to view next page of results, or type 'q' to return to menu
- **Help:** Select option `7` from main menu for detailed instructions
- **Safe Exit:** Always use option `8` to exit cleanly

## Troubleshooting

**Error: Could not find config file**
- Make sure `auth.cfg` exists in the project root or `DemoJavaProjectRelease/` directory

**Error: Failed to connect to database**
- Check your username and password in `auth.cfg`
- Verify network connection to uranium.cs.umanitoba.ca
- Ensure VPN is connected if required

**Error: CSV file not found**
- Make sure all CSV files are in the `data/` directory
- File names are case-sensitive

**Query returns no results**
- Database may be empty - use Database Management to populate
- Some queries only return results when specific conditions are met

## Query Categories Overview

| Category | Queries | Purpose |
|----------|---------|---------|
| **Market Analysis** | 4 queries | Geographic trends, sales patterns, untapped markets |
| **Seller Performance** | 4 queries | Ratings, inventory analysis, fulfillment gaps |
| **Customer Behavior** | 2 queries | Loyalty metrics, repeat purchases, lifetime value |
| **Order Quality** | 4 queries | Review rates, sentiment analysis, delivery performance |
| **Payment Analysis** | 2 queries | Payment methods, installment behavior by region |

## Data Volumes

After populating the database, you'll have approximately:

- 27 States
- 1,000,163 Geolocation records
- 99,441 Customers
- 3,095 Sellers
- 32,951 Products
- 71 Product Categories
- 99,441 Orders
- 112,650 Order Items
- 103,886 Order Payments
- 99,224 Order Reviews

**Total: ~1.5 million records**

## Support

For issues or questions:
- Refer to the full README.md for detailed documentation
- Check the Stage 5 and Stage 6 PDF documents for query specifications
- Review the SQL schema in schema.sql for database structure

---

**Ready to Start?**

```bash
make run
```

Happy analyzing!
