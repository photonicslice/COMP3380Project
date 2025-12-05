import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for displaying database statistics and table information
 */
public class DatabaseStatistics {

    /**
     * Display comprehensive statistics for all tables in the database
     */
    public static void showDatabaseStatistics() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              BRAZILIAN E-COMMERCE DATABASE STATISTICS          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        // Get counts for all tables
        Map<String, Integer> tableCounts = getTableCounts();

        if (tableCounts.isEmpty()) {
            System.out.println("No data found or unable to retrieve statistics.");
            return;
        }

        // Calculate total rows
        int totalRows = tableCounts.values().stream().mapToInt(Integer::intValue).sum();

        // Display by category
        displayReferenceTableStats(tableCounts);
        displayEntityTableStats(tableCounts);
        displayTransactionTableStats(tableCounts);
        displayRelationshipTableStats(tableCounts);

        // Display summary
        System.out.println("\n┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                        SUMMARY                                 │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ Total Tables:  %-10d                                    │%n", tableCounts.size());
        System.out.printf("│ Total Records: %-,10d                                  │%n", totalRows);
        System.out.println("└────────────────────────────────────────────────────────────────┘\n");

        // Show data completeness
        showDataCompleteness(tableCounts);
    }

    /**
     * Get row counts for all tables
     */
    private static Map<String, Integer> getTableCounts() {
        Map<String, Integer> counts = new LinkedHashMap<>();

        String[] tables = {
            // Reference tables
            "STATES", "GEOLOCATION", "CATEGORIES",
            // Entity tables
            "CUSTOMERS", "SELLERS", "PRODUCTS",
            // Transaction tables
            "ORDERS",
            // Relationship tables
            "ORDER_ITEMS", "ORDER_PAYMENTS", "ORDER_REVIEWS"
        };

        Connection conn = DatabaseConnection.getConnection();

        try (Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                try {
                    String sql = "SELECT COUNT(*) as count FROM " + table;
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        counts.put(table, rs.getInt("count"));
                    }
                    rs.close();
                } catch (SQLException e) {
                    counts.put(table, 0);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving table counts: " + e.getMessage());
        }

        return counts;
    }

    /**
     * Display reference table statistics
     */
    private static void displayReferenceTableStats(Map<String, Integer> counts) {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│  PHASE 1: REFERENCE TABLES                                     │");
        System.out.println("├─────────────────────────────────┬──────────────────────────────┤");
        System.out.printf("│ %-31s │ %,26d │%n", "STATES", counts.getOrDefault("STATES", 0));
        System.out.printf("│ %-31s │ %,26d │%n", "CATEGORIES", counts.getOrDefault("CATEGORIES", 0));
        System.out.printf("│ %-31s │ %,26d │%n", "GEOLOCATION", counts.getOrDefault("GEOLOCATION", 0));
        System.out.println("└─────────────────────────────────┴──────────────────────────────┘\n");
    }

    /**
     * Display entity table statistics
     */
    private static void displayEntityTableStats(Map<String, Integer> counts) {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│  PHASE 2: ENTITY TABLES                                        │");
        System.out.println("├─────────────────────────────────┬──────────────────────────────┤");
        System.out.printf("│ %-31s │ %,26d │%n", "CUSTOMERS", counts.getOrDefault("CUSTOMERS", 0));
        System.out.printf("│ %-31s │ %,26d │%n", "SELLERS", counts.getOrDefault("SELLERS", 0));
        System.out.printf("│ %-31s │ %,26d │%n", "PRODUCTS", counts.getOrDefault("PRODUCTS", 0));
        System.out.println("└─────────────────────────────────┴──────────────────────────────┘\n");
    }

    /**
     * Display transaction table statistics
     */
    private static void displayTransactionTableStats(Map<String, Integer> counts) {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│  PHASE 3: TRANSACTION TABLES                                   │");
        System.out.println("├─────────────────────────────────┬──────────────────────────────┤");
        System.out.printf("│ %-31s │ %,26d │%n", "ORDERS", counts.getOrDefault("ORDERS", 0));
        System.out.println("└─────────────────────────────────┴──────────────────────────────┘\n");
    }

    /**
     * Display relationship table statistics
     */
    private static void displayRelationshipTableStats(Map<String, Integer> counts) {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│  PHASE 4: RELATIONSHIP TABLES                                  │");
        System.out.println("├─────────────────────────────────┬──────────────────────────────┤");
        System.out.printf("│ %-31s │ %,26d │%n", "ORDER_ITEMS", counts.getOrDefault("ORDER_ITEMS", 0));
        System.out.printf("│ %-31s │ %,26d │%n", "ORDER_PAYMENTS", counts.getOrDefault("ORDER_PAYMENTS", 0));
        System.out.printf("│ %-31s │ %,26d │%n", "ORDER_REVIEWS", counts.getOrDefault("ORDER_REVIEWS", 0));
        System.out.println("└─────────────────────────────────┴──────────────────────────────┘\n");
    }

    /**
     * Show data completeness analysis
     */
    private static void showDataCompleteness(Map<String, Integer> counts) {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│  DATA COMPLETENESS                                             │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");

        boolean isEmpty = counts.values().stream().allMatch(count -> count == 0);
        boolean isPartiallyLoaded = counts.values().stream().anyMatch(count -> count > 0) &&
                                    counts.values().stream().anyMatch(count -> count == 0);
        boolean isFullyLoaded = counts.values().stream().allMatch(count -> count > 0);

        if (isEmpty) {
            System.out.println("│ Status: DATABASE IS EMPTY                                      │");
            System.out.println("│ Action: Run Phase 1-4 population scripts                       │");
        } else if (isPartiallyLoaded) {
            System.out.println("│ Status: PARTIALLY LOADED                                       │");
            System.out.println("│ Action: Some tables are empty. Complete remaining phases.      │");

            // Show which phases are incomplete
            if (counts.getOrDefault("STATES", 0) == 0 ||
                counts.getOrDefault("CATEGORIES", 0) == 0 ||
                counts.getOrDefault("GEOLOCATION", 0) == 0) {
                System.out.println("│   - Phase 1 (Reference Tables) incomplete                      │");
            }
            if (counts.getOrDefault("CUSTOMERS", 0) == 0 ||
                counts.getOrDefault("SELLERS", 0) == 0 ||
                counts.getOrDefault("PRODUCTS", 0) == 0) {
                System.out.println("│   - Phase 2 (Entity Tables) incomplete                         │");
            }
            if (counts.getOrDefault("ORDERS", 0) == 0) {
                System.out.println("│   - Phase 3 (Transaction Tables) incomplete                    │");
            }
            if (counts.getOrDefault("ORDER_ITEMS", 0) == 0 ||
                counts.getOrDefault("ORDER_PAYMENTS", 0) == 0 ||
                counts.getOrDefault("ORDER_REVIEWS", 0) == 0) {
                System.out.println("│   - Phase 4 (Relationship Tables) incomplete                   │");
            }
        } else if (isFullyLoaded) {
            System.out.println("│ Status: FULLY LOADED                                           │");
            System.out.println("│ All tables contain data. Database is ready for queries.        │");
        }

        System.out.println("└────────────────────────────────────────────────────────────────┘");
    }

    /**
     * Quick row count display (compact format)
     */
    public static void showQuickStats() {
        Map<String, Integer> counts = getTableCounts();

        System.out.println("\n╔═══════════════════════════╦═══════════════╗");
        System.out.println("║ Table                     ║ Row Count     ║");
        System.out.println("╠═══════════════════════════╬═══════════════╣");

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            System.out.printf("║ %-25s ║ %,13d ║%n", entry.getKey(), entry.getValue());
        }

        System.out.println("╚═══════════════════════════╩═══════════════╝\n");
    }
}
