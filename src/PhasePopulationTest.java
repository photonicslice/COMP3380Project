import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Test file for Phase-based population
 * Runs all 4 phases in sequence and validates the results
 */
public class PhasePopulationTest {

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║    PHASE-BASED DATABASE POPULATION TEST SUITE         ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        long totalStartTime = System.currentTimeMillis();
        boolean allPhasesSuccessful = true;

        try {
            // Test database connection first
            System.out.println("Testing database connection...");
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null || conn.isClosed()) {
                System.out.println("[ERROR] Database connection failed!");
                System.exit(1);
            }
            System.out.println("[OK] Database connection successful\n");

            // Clear existing data
            System.out.println("Clearing existing data...");
            CSVDataLoader.clearAllData();
            System.out.println();

            // Show initial state
            System.out.println("Initial database state:");
            showTableCounts();
            System.out.println();

            // Run Phase 1
            System.out.println("\n" + "=".repeat(60));
            System.out.println("EXECUTING PHASE 1");
            System.out.println("=".repeat(60));
            Phase1PopulateReferenceTables.main(new String[]{});
            System.out.println("\nPhase 1 Statistics:");
            showPhaseCounts(1);

            // Run Phase 2
            System.out.println("\n" + "=".repeat(60));
            System.out.println("EXECUTING PHASE 2");
            System.out.println("=".repeat(60));
            Phase2PopulateEntityTables.main(new String[]{});
            System.out.println("\nPhase 2 Statistics:");
            showPhaseCounts(2);

            // Run Phase 3
            System.out.println("\n" + "=".repeat(60));
            System.out.println("EXECUTING PHASE 3");
            System.out.println("=".repeat(60));
            Phase3PopulateOrders.main(new String[]{});
            System.out.println("\nPhase 3 Statistics:");
            showPhaseCounts(3);

            // Run Phase 4
            System.out.println("\n" + "=".repeat(60));
            System.out.println("EXECUTING PHASE 4");
            System.out.println("=".repeat(60));
            Phase4PopulateRelationships.main(new String[]{});
            System.out.println("\nPhase 4 Statistics:");
            showPhaseCounts(4);

            // Final statistics
            long totalEndTime = System.currentTimeMillis();
            double totalSeconds = (totalEndTime - totalStartTime) / 1000.0;

            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║              FINAL DATABASE STATISTICS                 ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            showTableCounts();

            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║                    TEST SUMMARY                        ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println("[SUCCESS] All 4 phases completed successfully!");
            System.out.println("Total execution time: " + String.format("%.2f", totalSeconds) + " seconds");
            System.out.println();

            // Validate data integrity
            validateDataIntegrity();

        } catch (Exception e) {
            allPhasesSuccessful = false;
            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║                   TEST FAILED!                         ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Show row counts for all tables
     */
    private static void showTableCounts() {
        String[] tables = {
            "STATES", "GEOLOCATION", "CATEGORIES", "CUSTOMERS",
            "SELLERS", "PRODUCTS", "ORDERS", "ORDER_ITEMS",
            "ORDER_PAYMENTS", "ORDER_REVIEWS"
        };

        Connection conn = DatabaseConnection.getConnection();
        System.out.println("\n┌─────────────────────────┬────────────────┐");
        System.out.println("│ Table Name              │ Row Count      │");
        System.out.println("├─────────────────────────┼────────────────┤");

        try (Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                String sql = "SELECT COUNT(*) as count FROM " + table;
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.printf("│ %-23s │ %,14d │%n", table, count);
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Error getting table counts: " + e.getMessage());
        }

        System.out.println("└─────────────────────────┴────────────────┘");
    }

    /**
     * Show counts for tables loaded in a specific phase
     */
    private static void showPhaseCounts(int phase) {
        String[] tables;
        switch (phase) {
            case 1:
                tables = new String[]{"STATES", "CATEGORIES", "GEOLOCATION"};
                break;
            case 2:
                tables = new String[]{"CUSTOMERS", "SELLERS", "PRODUCTS"};
                break;
            case 3:
                tables = new String[]{"ORDERS"};
                break;
            case 4:
                tables = new String[]{"ORDER_ITEMS", "ORDER_PAYMENTS", "ORDER_REVIEWS"};
                break;
            default:
                return;
        }

        Connection conn = DatabaseConnection.getConnection();
        try (Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                String sql = "SELECT COUNT(*) as count FROM " + table;
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.printf("  - %-23s: %,d rows%n", table, count);
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Error getting phase counts: " + e.getMessage());
        }
    }

    /**
     * Validate basic data integrity
     */
    private static void validateDataIntegrity() {
        System.out.println("\nValidating data integrity...");
        Connection conn = DatabaseConnection.getConnection();
        boolean passed = true;

        try (Statement stmt = conn.createStatement()) {
            // Check 1: All orders should have a valid customer
            String sql1 = "SELECT COUNT(*) as count FROM ORDERS o " +
                         "WHERE NOT EXISTS (SELECT 1 FROM CUSTOMERS c WHERE c.customer_id = o.customer_id)";
            ResultSet rs1 = stmt.executeQuery(sql1);
            if (rs1.next() && rs1.getInt("count") == 0) {
                System.out.println("[PASS] All orders have valid customers");
            } else {
                System.out.println("[FAIL] Some orders have invalid customer references");
                passed = false;
            }
            rs1.close();

            // Check 2: All order items should have valid orders
            String sql2 = "SELECT COUNT(*) as count FROM ORDER_ITEMS oi " +
                         "WHERE NOT EXISTS (SELECT 1 FROM ORDERS o WHERE o.order_id = oi.order_id)";
            ResultSet rs2 = stmt.executeQuery(sql2);
            if (rs2.next() && rs2.getInt("count") == 0) {
                System.out.println("[PASS] All order items have valid orders");
            } else {
                System.out.println("[FAIL] Some order items have invalid order references");
                passed = false;
            }
            rs2.close();

            // Check 3: All order items should have valid products
            String sql3 = "SELECT COUNT(*) as count FROM ORDER_ITEMS oi " +
                         "WHERE NOT EXISTS (SELECT 1 FROM PRODUCTS p WHERE p.product_id = oi.product_id)";
            ResultSet rs3 = stmt.executeQuery(sql3);
            if (rs3.next() && rs3.getInt("count") == 0) {
                System.out.println("[PASS] All order items have valid products");
            } else {
                System.out.println("[FAIL] Some order items have invalid product references");
                passed = false;
            }
            rs3.close();

            // Check 4: All order items should have valid sellers
            String sql4 = "SELECT COUNT(*) as count FROM ORDER_ITEMS oi " +
                         "WHERE NOT EXISTS (SELECT 1 FROM SELLERS s WHERE s.seller_id = oi.seller_id)";
            ResultSet rs4 = stmt.executeQuery(sql4);
            if (rs4.next() && rs4.getInt("count") == 0) {
                System.out.println("[PASS] All order items have valid sellers");
            } else {
                System.out.println("[FAIL] Some order items have invalid seller references");
                passed = false;
            }
            rs4.close();

            if (passed) {
                System.out.println("\n[SUCCESS] All data integrity checks passed!");
            } else {
                System.out.println("\n[WARNING] Some data integrity checks failed!");
            }

        } catch (SQLException e) {
            System.out.println("Error validating data: " + e.getMessage());
        }
    }
}
