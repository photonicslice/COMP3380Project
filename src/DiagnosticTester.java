import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Diagnostic tool to identify why TOP_SELLING_CATEGORIES returns no rows
 */
public class DiagnosticTester {

    public static void main(String[] args) {
        System.out.println("=== DIAGNOSTIC TEST FOR TOP_SELLING_CATEGORIES ===\n");
        DatabaseConnection.getConnection();

        // Step 1: Check table row counts
        System.out.println("STEP 1: Checking table row counts...");
        checkTableCounts();
        System.out.println();

        // Step 2: Check if PRODUCTS have category_id populated
        System.out.println("STEP 2: Checking if products have category_id...");
        checkProductCategories();
        System.out.println();

        // Step 3: Check CATEGORIES to PRODUCTS join
        System.out.println("STEP 3: Checking CATEGORIES to PRODUCTS join...");
        checkCategoriesProductsJoin();
        System.out.println();

        // Step 4: Check PRODUCTS to ORDER_ITEMS join
        System.out.println("STEP 4: Checking PRODUCTS to ORDER_ITEMS join...");
        checkProductsOrderItemsJoin();
        System.out.println();

        // Step 5: Check full join chain
        System.out.println("STEP 5: Checking full join chain...");
        checkFullJoinChain();
        System.out.println();

        // Step 6: Sample data from each table
        System.out.println("STEP 6: Sampling data from each table...");
        sampleCategories();
        sampleProducts();
        sampleOrderItems();

        DatabaseConnection.closeConnection();
    }

    private static void checkTableCounts() {
        String query =
            "SELECT 'CATEGORIES' AS table_name, COUNT(*) AS row_count FROM CATEGORIES " +
            "UNION ALL " +
            "SELECT 'PRODUCTS', COUNT(*) FROM PRODUCTS " +
            "UNION ALL " +
            "SELECT 'ORDER_ITEMS', COUNT(*) FROM ORDER_ITEMS";

        try {
            ResultSet rs = QueryExecutor.executeQuery(query);
            while (rs.next()) {
                System.out.println("  " + rs.getString("table_name") + ": " + rs.getInt("row_count") + " rows");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
    }

    private static void checkProductCategories() {
        String query =
            "SELECT " +
            "    COUNT(*) AS total_products, " +
            "    COUNT(category_id) AS products_with_category, " +
            "    COUNT(*) - COUNT(category_id) AS products_without_category " +
            "FROM PRODUCTS";

        try {
            ResultSet rs = QueryExecutor.executeQuery(query);
            if (rs.next()) {
                System.out.println("  Total products: " + rs.getInt("total_products"));
                System.out.println("  Products with category_id: " + rs.getInt("products_with_category"));
                System.out.println("  Products without category_id: " + rs.getInt("products_without_category"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
    }

    private static void checkCategoriesProductsJoin() {
        String query =
            "SELECT COUNT(*) AS join_count " +
            "FROM CATEGORIES c " +
            "JOIN PRODUCTS p ON c.category_id = p.category_id";

        try {
            ResultSet rs = QueryExecutor.executeQuery(query);
            if (rs.next()) {
                int count = rs.getInt("join_count");
                System.out.println("  Categories with products: " + count);
                if (count == 0) {
                    System.out.println("  ⚠️  WARNING: No products are linked to categories!");
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
    }

    private static void checkProductsOrderItemsJoin() {
        String query =
            "SELECT COUNT(*) AS join_count " +
            "FROM PRODUCTS p " +
            "JOIN ORDER_ITEMS oi ON p.product_id = oi.product_id";

        try {
            ResultSet rs = QueryExecutor.executeQuery(query);
            if (rs.next()) {
                int count = rs.getInt("join_count");
                System.out.println("  Products with order items: " + count);
                if (count == 0) {
                    System.out.println("  ⚠️  WARNING: No order items are linked to products!");
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
    }

    private static void checkFullJoinChain() {
        String query =
            "SELECT COUNT(*) AS join_count " +
            "FROM CATEGORIES c " +
            "JOIN PRODUCTS p ON c.category_id = p.category_id " +
            "JOIN ORDER_ITEMS oi ON p.product_id = oi.product_id";

        try {
            ResultSet rs = QueryExecutor.executeQuery(query);
            if (rs.next()) {
                int count = rs.getInt("join_count");
                System.out.println("  Full join result: " + count + " rows");
                if (count == 0) {
                    System.out.println("  ⚠️  WARNING: Full join returns 0 rows - this is why TOP_SELLING_CATEGORIES is empty!");
                } else {
                    System.out.println("  ✓ Full join works - TOP_SELLING_CATEGORIES should return data");
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
    }

    private static void sampleCategories() {
        String query = "SELECT TOP 3 category_id, category_name_english FROM CATEGORIES";

        try {
            ResultSet rs = QueryExecutor.executeQuery(query);
            System.out.println("\n  Sample CATEGORIES:");
            while (rs.next()) {
                System.out.println("    ID: " + rs.getInt("category_id") +
                                 ", Name: " + rs.getString("category_name_english"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
    }

    private static void sampleProducts() {
        String query = "SELECT TOP 3 product_id, category_id, category_name_portuguese FROM PRODUCTS";

        try {
            ResultSet rs = QueryExecutor.executeQuery(query);
            System.out.println("\n  Sample PRODUCTS:");
            while (rs.next()) {
                Object catId = rs.getObject("category_id");
                System.out.println("    Product: " + rs.getString("product_id") +
                                 ", category_id: " + (catId != null ? catId : "NULL") +
                                 ", category_name_portuguese: " + rs.getString("category_name_portuguese"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
    }

    private static void sampleOrderItems() {
        String query = "SELECT TOP 3 order_id, product_id, price FROM ORDER_ITEMS";

        try {
            ResultSet rs = QueryExecutor.executeQuery(query);
            System.out.println("\n  Sample ORDER_ITEMS:");
            while (rs.next()) {
                System.out.println("    Order: " + rs.getString("order_id") +
                                 ", Product: " + rs.getString("product_id") +
                                 ", Price: " + rs.getBigDecimal("price"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
    }
}
