import java.util.Scanner;

/**
 * Manages the command-line menu interface for the Brazilian E-Commerce Database Analyzer.
 * Provides numbered menu navigation matching Stage 6 requirements.
 */
public class MenuInterface {
    private Scanner scanner;

    public MenuInterface() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu and handles user selection
     */
    public void showMainMenu() {
        while (true) {
            printHeader();
            System.out.println("Main Menu:");
            System.out.println("----------");
            System.out.println("1. Market and Sales Analysis");
            System.out.println("2. Seller Performance Analysis");
            System.out.println("3. Customer Behavior Analysis");
            System.out.println("4. Order and Review Quality Analysis");
            System.out.println("5. Payment and Transaction Analysis");
            System.out.println("6. Additional Analysis");
            System.out.println("7. Database Management");
            System.out.println("8. Help / Instructions");
            System.out.println("9. Exit Application");
            System.out.println();
            System.out.print("Enter your choice (1-9): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showMarketAnalysisMenu();
                    break;
                case "2":
                    showSellerPerformanceMenu();
                    break;
                case "3":
                    showCustomerBehaviorMenu();
                    break;
                case "4":
                    showOrderQualityMenu();
                    break;
                case "5":
                    showPaymentAnalysisMenu();
                    break;
                case "6":
                    showAdditionalAnalysisMenu();
                    break;
                case "7":
                    showDatabaseManagementMenu();
                    break;
                case "8":
                    showHelp();
                    break;
                case "9":
                    System.out.println("\nThank you for using the Brazilian E-Commerce Database Analyzer!");
                    DatabaseConnection.closeConnection();
                    return;
                default:
                    System.out.println("\nInvalid choice. Please enter a number between 1 and 9.");
                    pause();
            }
        }
    }

    private void showMarketAnalysisMenu() {
        printSeparator();
        System.out.println("Main Menu > 1. Market and Sales Analysis");
        printSeparator();
        System.out.println("\nMarket and Sales Analysis - Available Queries:");
        System.out.println("-----------------------------------------------");
        System.out.println("1. High-Value Customer States");
        System.out.println("   -> Shows states with highest average order values");
        System.out.println();
        System.out.println("2. Top Selling Categories by Revenue");
        System.out.println("   -> Identifies product categories generating most revenue");
        System.out.println();
        System.out.println("3. States with Customers but No Orders");
        System.out.println("   -> Finds untapped markets with registered users");
        System.out.println();
        System.out.println("4. Highest Sales Geolocation");
        System.out.println("   -> Identifies zip code with most customer orders");
        System.out.println();
        System.out.println("5. Back to Main Menu");
        System.out.println();
        System.out.print("Enter your choice (1-5): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                QueryExecutor.executeAndDisplay(QueryManager.HIGH_VALUE_CUSTOMER_STATES,
                                               "High-Value Customer States");
                pause();
                break;
            case "2":
                QueryExecutor.executeAndDisplay(QueryManager.TOP_SELLING_CATEGORIES,
                                               "Top Selling Categories by Revenue");
                pause();
                break;
            case "3":
                QueryExecutor.executeAndDisplay(QueryManager.STATES_WITH_CUSTOMERS_NO_ORDERS,
                                               "States with Customers but No Orders");
                pause();
                break;
            case "4":
                QueryExecutor.executeAndDisplay(QueryManager.HIGHEST_SALES_GEOLOCATION,
                                               "Highest Sales Geolocation");
                pause();
                break;
            case "5":
                return;
            default:
                System.out.println("\nInvalid choice.");
                pause();
        }
    }

    private void showSellerPerformanceMenu() {
        printSeparator();
        System.out.println("Main Menu > 2. Seller Performance Analysis");
        printSeparator();
        System.out.println("\nSeller Performance Analysis - Available Queries:");
        System.out.println("-------------------------------------------------");
        System.out.println("1. Seller Success Rate by Review Score");
        System.out.println("   -> Shows top-performing sellers with highest review scores");
        System.out.println();
        System.out.println("2. Unused Product Catalog");
        System.out.println("   -> Lists products that have never been ordered");
        System.out.println();
        System.out.println("3. States with Customers but No Sellers");
        System.out.println("   -> Identifies fulfillment gaps");
        System.out.println();
        System.out.println("4. Single-Product Sellers Count");
        System.out.println("   -> Counts sellers offering only one product");
        System.out.println();
        System.out.println("5. Back to Main Menu");
        System.out.println();
        System.out.print("Enter your choice (1-5): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                QueryExecutor.executeAndDisplay(QueryManager.SELLER_SUCCESS_RATE,
                                               "Seller Success Rate by Review Score");
                pause();
                break;
            case "2":
                QueryExecutor.executeAndDisplay(QueryManager.UNUSED_PRODUCT_CATALOG,
                                               "Unused Product Catalog");
                pause();
                break;
            case "3":
                QueryExecutor.executeAndDisplay(QueryManager.STATES_WITH_CUSTOMERS_NO_SELLERS,
                                               "States with Customers but No Sellers");
                pause();
                break;
            case "4":
                QueryExecutor.executeAndDisplay(QueryManager.SINGLE_PRODUCT_SELLERS,
                                               "Single-Product Sellers Count");
                pause();
                break;
            case "5":
                return;
            default:
                System.out.println("\nInvalid choice.");
                pause();
        }
    }

    private void showCustomerBehaviorMenu() {
        printSeparator();
        System.out.println("Main Menu > 3. Customer Behavior Analysis");
        printSeparator();
        System.out.println("\nCustomer Behavior Analysis - Available Queries:");
        System.out.println("------------------------------------------------");
        System.out.println("1. Repeat Purchase Customers");
        System.out.println("   -> Identifies customers who placed multiple orders");
        System.out.println();
        System.out.println("2. Average Time Between Orders");
        System.out.println("   -> Shows customer return frequency metrics");
        System.out.println();
        System.out.println("3. Back to Main Menu");
        System.out.println();
        System.out.print("Enter your choice (1-3): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                QueryExecutor.executeAndDisplay(QueryManager.REPEAT_PURCHASE_CUSTOMERS,
                                               "Repeat Purchase Customers");
                pause();
                break;
            case "2":
                QueryExecutor.executeAndDisplay(QueryManager.AVG_TIME_BETWEEN_ORDERS,
                                               "Average Time Between Orders");
                pause();
                break;
            case "3":
                return;
            default:
                System.out.println("\nInvalid choice.");
                pause();
        }
    }

    private void showOrderQualityMenu() {
        printSeparator();
        System.out.println("Main Menu > 4. Order and Review Quality Analysis");
        printSeparator();
        System.out.println("\nOrder and Review Quality Analysis - Available Queries:");
        System.out.println("-------------------------------------------------------");
        System.out.println("1. Order Review Rate");
        System.out.println("   -> Percentage of orders that receive reviews");
        System.out.println();
        System.out.println("2. Worst Rated Product Category");
        System.out.println("   -> Category with lowest average review score");
        System.out.println();
        System.out.println("3. Orders Paid in Full (Single Payment)");
        System.out.println("   -> Count of orders with exactly one payment");
        System.out.println();
        System.out.println("4. Review Score Extremes (1-Star vs. 5-Star)");
        System.out.println("   -> Distribution of extreme ratings");
        System.out.println();
        System.out.println("5. Back to Main Menu");
        System.out.println();
        System.out.print("Enter your choice (1-5): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                QueryExecutor.executeAndDisplay(QueryManager.ORDER_REVIEW_RATE,
                                               "Order Review Rate");
                pause();
                break;
            case "2":
                QueryExecutor.executeAndDisplay(QueryManager.WORST_RATED_CATEGORY,
                                               "Worst Rated Product Category");
                pause();
                break;
            case "3":
                QueryExecutor.executeAndDisplay(QueryManager.ORDERS_PAID_IN_FULL,
                                               "Orders Paid in Full");
                pause();
                break;
            case "4":
                QueryExecutor.executeAndDisplay(QueryManager.REVIEW_SCORE_EXTREMES,
                                               "Review Score Extremes");
                pause();
                break;
            case "5":
                return;
            default:
                System.out.println("\nInvalid choice.");
                pause();
        }
    }

    private void showPaymentAnalysisMenu() {
        printSeparator();
        System.out.println("Main Menu > 5. Payment and Transaction Analysis");
        printSeparator();
        System.out.println("\nPayment and Transaction Analysis - Available Queries:");
        System.out.println("------------------------------------------------------");
        System.out.println("1. Most Common Payment Type by State");
        System.out.println("   -> Dominant payment method per state");
        System.out.println();
        System.out.println("2. Average Number of Installments per Payment Type");
        System.out.println("   -> Financing behavior by payment method");
        System.out.println();
        System.out.println("3. Back to Main Menu");
        System.out.println();
        System.out.print("Enter your choice (1-3): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                QueryExecutor.executeAndDisplay(QueryManager.MOST_COMMON_PAYMENT_TYPE_BY_STATE,
                                               "Most Common Payment Type by State");
                pause();
                break;
            case "2":
                QueryExecutor.executeAndDisplay(QueryManager.AVG_INSTALLMENTS_BY_PAYMENT_TYPE,
                                               "Average Installments per Payment Type");
                pause();
                break;
            case "3":
                return;
            default:
                System.out.println("\nInvalid choice.");
                pause();
        }
    }

    private void showAdditionalAnalysisMenu() {
        printSeparator();
        System.out.println("Main Menu > 6. Additional Analysis");
        printSeparator();
        System.out.println("\nAdditional Analysis - Available Queries:");
        System.out.println("-----------------------------------------");
        System.out.println("1. Delivery Performance by State");
        System.out.println("   -> Analyzes delivery times, delays, and late delivery rates per state");
        System.out.println();
        System.out.println("2. Category Performance by Quarter");
        System.out.println("   -> Shows product category revenue trends by year and quarter");
        System.out.println();
        System.out.println("3. Revenue by State and Year (Parameterized)");
        System.out.println("   -> Total orders and revenue for a specific state and year");
        System.out.println();
        System.out.println("4. Seller Inventory Check (Parameterized)");
        System.out.println("   -> Find sellers by product description length");
        System.out.println();
        System.out.println("5. Back to Main Menu");
        System.out.println();
        System.out.print("Enter your choice (1-5): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                QueryExecutor.executeAndDisplay(QueryManager.DELIVERY_PERFORMANCE_BY_STATE,
                                               "Delivery Performance by State");
                pause();
                break;
            case "2":
                QueryExecutor.executeAndDisplay(QueryManager.CATEGORY_PERFORMANCE_BY_QUARTER,
                                               "Category Performance by Quarter");
                pause();
                break;
            case "3":
                executeRevenueByStateAndYear();
                pause();
                break;
            case "4":
                executeSellerInventoryCheck();
                pause();
                break;
            case "5":
                return;
            default:
                System.out.println("\nInvalid choice.");
                pause();
        }
    }

    private void showDatabaseManagementMenu() {
        printSeparator();
        System.out.println("Main Menu > 7. Database Management");
        printSeparator();
        System.out.println("\nDatabase Management Options:");
        System.out.println("-----------------------------");
        System.out.println("1. Clear All Data (WARNING: Destructive Operation)");
        System.out.println("   -> Deletes all records from all tables");
        System.out.println();
        System.out.println("2. Repopulate Database from CSV Files");
        System.out.println("   -> Loads data from pre-validated CSV files");
        System.out.println();
        System.out.println("3. Back to Main Menu");
        System.out.println();
        System.out.print("Enter your choice (1-3): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                clearDatabaseWithConfirmation();
                pause();
                break;
            case "2":
                CSVDataLoader.populateAllTables();
                pause();
                break;
            case "3":
                return;
            default:
                System.out.println("\nInvalid choice.");
                pause();
        }
    }

    private void executeRevenueByStateAndYear() {
        printSeparator();
        System.out.println("Revenue by State and Year - Parameterized Query");
        printSeparator();
        System.out.println("\nThis query shows total orders and revenue for a specific state and year.");
        System.out.println();

        System.out.print("Enter state code (e.g., SP, RJ, MG): ");
        String stateCode = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter year (e.g., 2017, 2018): ");
        String year = scanner.nextLine().trim();

        // Create year pattern for LIKE query (e.g., "2017%")
        String yearPattern = year + "%";

        System.out.println("\nExecuting query for state: " + stateCode + ", year: " + year);
        QueryExecutor.executeAndDisplay(QueryManager.REVENUE_BY_STATE_AND_YEAR,
                                       "Revenue by State and Year",
                                       stateCode, yearPattern);
    }

    private void executeSellerInventoryCheck() {
        printSeparator();
        System.out.println("Seller Inventory Check - Parameterized Query");
        printSeparator();
        System.out.println("\nThis query finds sellers with products matching a description length pattern.");
        System.out.println();

        System.out.print("Enter product description length pattern (e.g., 100, 50%, %500): ");
        String descLengthPattern = scanner.nextLine().trim();

        System.out.println("\nExecuting query for description length pattern: " + descLengthPattern);
        QueryExecutor.executeAndDisplay(QueryManager.SELLER_INVENTORY_CHECK,
                                       "Seller Inventory Check",
                                       descLengthPattern);
    }

    private void clearDatabaseWithConfirmation() {
        printSeparator();
        System.out.println("WARNING: CLEAR ALL DATA");
        printSeparator();
        System.out.println("\nYou are about to DELETE ALL DATA from all tables.");
        System.out.println("This operation CANNOT be undone without repopulating from CSV files.");
        System.out.println();
        System.out.print("Type 'DELETE ALL DATA' to confirm (case-sensitive): ");

        String confirmation = scanner.nextLine();

        if (confirmation.equals("DELETE ALL DATA")) {
            System.out.println("\nConfirmation received. Deleting data...");
            CSVDataLoader.clearAllData();
        } else {
            System.out.println("\nOperation cancelled. No data was deleted.");
        }
    }

    private void showHelp() {
        printSeparator();
        System.out.println("HELP - INSTRUCTIONS FOR USE");
        printSeparator();
        System.out.println("\nOVERVIEW:");
        System.out.println("This interface provides secure access to the Brazilian E-Commerce database");
        System.out.println("for analytical queries. Navigate using numbered menus and follow prompts for");
        System.out.println("parameterized queries.");
        System.out.println();
        System.out.println("USAGE:");
        System.out.println("- Select a category from the main menu (1-6)");
        System.out.println("- Choose a specific query from the submenu");
        System.out.println("- For parameterized queries, enter requested information when prompted");
        System.out.println("- Results display in formatted tables (max 25 rows per page)");
        System.out.println("- Press Enter to view next page, or 'q' to return to menu");
        System.out.println();
        System.out.println("SECURITY:");
        System.out.println("- All queries use prepared statements (SQL injection safe)");
        System.out.println("- No direct SQL input accepted");
        System.out.println("- Database credentials are secured in auth.cfg");
        System.out.println();
        System.out.println("DATABASE MANAGEMENT:");
        System.out.println("- Option 7 provides data deletion and repopulation");
        System.out.println("- Deletion requires confirmation (prevents accidental data loss)");
        System.out.println("- Repopulation loads from pre-validated CSV files");
        System.out.println();
        System.out.println("QUERY CATEGORIES:");
        System.out.println("1. Market Analysis - Geographic trends, category performance, sales patterns");
        System.out.println("2. Seller Performance - Ratings, inventory, fulfillment gaps");
        System.out.println("3. Customer Behavior - Repeat purchases, loyalty metrics, lifetime value");
        System.out.println("4. Order Quality - Review rates, delivery performance, sentiment analysis");
        System.out.println("5. Payment Analysis - Payment methods, installment behavior, state preferences");
        System.out.println("6. Additional Analysis - Delivery metrics, quarterly trends, parameterized queries");
        System.out.println("7. Database Management - Clear data, repopulate, verify integrity");
        System.out.println();
        System.out.println("RESULT INTERPRETATION:");
        System.out.println("- Column headers describe each data field");
        System.out.println("- Results sorted by relevance (e.g., highest revenue first)");
        System.out.println("- Null values displayed as \"N/A\"");
        System.out.println("- Decimal values rounded to 2 places for readability");
        printSeparator();
        pause();
    }

    private void printHeader() {
        printSeparator();
        System.out.println("BRAZILIAN E-COMMERCE DATABASE ANALYZER");
        System.out.println("COMP 3380 - Database Project");
        printSeparator();
        System.out.println();
    }

    private void printSeparator() {
        for (int i = 0; i < 80; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    private void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
