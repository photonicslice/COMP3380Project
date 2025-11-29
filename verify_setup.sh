#!/bin/bash

# Setup Verification Script for Brazilian E-Commerce Database Analyzer
# Checks that all required components are in place

echo "=========================================================================="
echo "Brazilian E-Commerce Database Analyzer - Setup Verification"
echo "=========================================================================="
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

SUCCESS=0
WARNINGS=0
ERRORS=0

# Function to check file existence
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}[OK]${NC} $2"
        ((SUCCESS++))
        return 0
    else
        echo -e "${RED}[MISSING]${NC} $2"
        echo "     Expected location: $1"
        ((ERRORS++))
        return 1
    fi
}

# Function to check directory existence
check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}[OK]${NC} $2"
        ((SUCCESS++))
        return 0
    else
        echo -e "${RED}[MISSING]${NC} $2"
        echo "     Expected location: $1"
        ((ERRORS++))
        return 1
    fi
}

# Check Java installation
echo "Checking Java Installation..."
echo "------------------------------"
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo -e "${GREEN}[OK]${NC} Java is installed (version $JAVA_VERSION)"
    ((SUCCESS++))
else
    echo -e "${RED}[ERROR]${NC} Java is not installed or not in PATH"
    echo "     Please install JDK 11 or higher"
    ((ERRORS++))
fi

if command -v javac &> /dev/null; then
    echo -e "${GREEN}[OK]${NC} Java compiler (javac) is available"
    ((SUCCESS++))
else
    echo -e "${RED}[ERROR]${NC} Java compiler (javac) is not installed"
    echo "     Please install JDK (not just JRE)"
    ((ERRORS++))
fi
echo ""

# Check source files
echo "Checking Source Files..."
echo "------------------------"
check_file "src/BrazilianECommerceAnalyzer.java" "Main application class"
check_file "src/DatabaseConnection.java" "Database connection class"
check_file "src/QueryManager.java" "Query manager class"
check_file "src/QueryExecutor.java" "Query executor class"
check_file "src/ResultFormatter.java" "Result formatter class"
check_file "src/MenuInterface.java" "Menu interface class"
check_file "src/CSVDataLoader.java" "CSV data loader class"
echo ""

# Check JDBC driver
echo "Checking JDBC Driver..."
echo "-----------------------"
if [ -f "DemoJavaProjectRelease/mssql-jdbc-11.2.0.jre11.jar" ]; then
    echo -e "${GREEN}[OK]${NC} SQL Server JDBC driver found"
    ((SUCCESS++))
elif [ -f "DemoJavaProjectRelease/mssql-jdbc-11.2.0.jre18.jar" ]; then
    echo -e "${YELLOW}[WARNING]${NC} Found JRE18 driver (JRE11 driver preferred)"
    ((WARNINGS++))
else
    echo -e "${RED}[ERROR]${NC} SQL Server JDBC driver not found"
    echo "     Expected: DemoJavaProjectRelease/mssql-jdbc-11.2.0.jre11.jar"
    ((ERRORS++))
fi
echo ""

# Check auth.cfg
echo "Checking Database Configuration..."
echo "----------------------------------"
if [ -f "auth.cfg" ]; then
    echo -e "${GREEN}[OK]${NC} auth.cfg found in current directory"
    ((SUCCESS++))
elif [ -f "DemoJavaProjectRelease/auth.cfg" ]; then
    echo -e "${GREEN}[OK]${NC} auth.cfg found in DemoJavaProjectRelease/"
    ((SUCCESS++))
else
    echo -e "${RED}[ERROR]${NC} auth.cfg not found"
    echo "     Please create auth.cfg with your database credentials:"
    echo "     username=YOUR_USERNAME"
    echo "     password=YOUR_PASSWORD"
    ((ERRORS++))
fi
echo ""

# Check data files
echo "Checking CSV Data Files..."
echo "--------------------------"
check_dir "data" "Data directory"
check_file "data/olist_customers_dataset.csv" "Customers dataset"
check_file "data/olist_orders_dataset.csv" "Orders dataset"
check_file "data/olist_order_items_dataset.csv" "Order items dataset"
check_file "data/olist_order_payments_dataset.csv" "Order payments dataset"
check_file "data/olist_order_reviews_dataset.csv" "Order reviews dataset"
check_file "data/olist_products_dataset.csv" "Products dataset"
check_file "data/olist_sellers_dataset.csv" "Sellers dataset"
check_file "data/olist_geolocation_dataset.csv" "Geolocation dataset"
check_file "data/product_category_name_translation.csv" "Category translation"
echo ""

# Check schema file
echo "Checking Database Schema..."
echo "---------------------------"
check_file "schema.sql" "Database schema file"
echo ""

# Check build files
echo "Checking Build Files..."
echo "-----------------------"
check_file "Makefile" "Makefile for compilation"
check_file "README.md" "README documentation"
check_file "QUICKSTART.md" "Quick start guide"
echo ""

# Check if project is compiled
echo "Checking Compilation Status..."
echo "------------------------------"
if [ -d "bin" ] && [ "$(ls -A bin)" ]; then
    echo -e "${GREEN}[OK]${NC} Project is compiled (bin directory exists with files)"
    ((SUCCESS++))
else
    echo -e "${YELLOW}[INFO]${NC} Project not yet compiled"
    echo "     Run 'make compile' to compile the project"
    ((WARNINGS++))
fi
echo ""

# Summary
echo "=========================================================================="
echo "Verification Summary"
echo "=========================================================================="
echo -e "${GREEN}Successful checks: $SUCCESS${NC}"
if [ $WARNINGS -gt 0 ]; then
    echo -e "${YELLOW}Warnings: $WARNINGS${NC}"
fi
if [ $ERRORS -gt 0 ]; then
    echo -e "${RED}Errors: $ERRORS${NC}"
fi
echo ""

if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}Setup verification complete! You're ready to compile and run.${NC}"
    echo ""
    echo "Next steps:"
    echo "  1. Ensure database schema is created: Run schema.sql on the server"
    echo "  2. Compile the project: make compile"
    echo "  3. Run the application: make run"
    echo "  4. Populate the database using Database Management menu (Option 6)"
else
    echo -e "${RED}Setup verification found errors. Please fix them before proceeding.${NC}"
    exit 1
fi

echo "=========================================================================="
