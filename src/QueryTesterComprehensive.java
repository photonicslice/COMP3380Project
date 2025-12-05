import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive query tester for all queries in QueryManager
 * Tests each query and reports:
 * - Whether it executes without errors
 * - How many rows it returns
 * - Sample of first row data
 */
public class QueryTesterComprehensive {

    private static class QueryTest {
        String name;
        String sql;
        boolean isParameterized;
        String[] paramValues;

        QueryTest(String name, String sql) {
            this.name = name;
            this.sql = sql;
            this.isParameterized = false;
        }

        QueryTest(String name, String sql, String... params) {
            this.name = name;
            this.sql = sql;
            this.isParameterized = true;
            this.paramValues = params;
        }
    }

    public static void main(String[] args) {
        System.out.println("\n╔═════════════════════════════════════════════════════════════════╗");
        System.out.println("║        COMPREHENSIVE QUERY TESTING SUITE                       ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════╝\n");

        List<QueryTest> tests = new ArrayList<>();

        // Market and Sales Analysis
        tests.add(new QueryTest("High Value Customer States", QueryManager.HIGH_VALUE_CUSTOMER_STATES));
        tests.add(new QueryTest("Top Selling Categories", QueryManager.TOP_SELLING_CATEGORIES));
        tests.add(new QueryTest("States with Customers but No Orders", QueryManager.STATES_WITH_CUSTOMERS_NO_ORDERS));
        tests.add(new QueryTest("Highest Sales Geolocation", QueryManager.HIGHEST_SALES_GEOLOCATION));

        // Seller Performance
        tests.add(new QueryTest("Seller Success Rate", QueryManager.SELLER_SUCCESS_RATE));
        tests.add(new QueryTest("Unused Product Catalog", QueryManager.UNUSED_PRODUCT_CATALOG));
        tests.add(new QueryTest("States with Customers but No Sellers", QueryManager.STATES_WITH_CUSTOMERS_NO_SELLERS));
        tests.add(new QueryTest("Single Product Sellers Count", QueryManager.SINGLE_PRODUCT_SELLERS));

        // Customer Behavior
        tests.add(new QueryTest("Repeat Purchase Customers", QueryManager.REPEAT_PURCHASE_CUSTOMERS));
        tests.add(new QueryTest("Average Time Between Orders", QueryManager.AVG_TIME_BETWEEN_ORDERS));

        // Order and Review Quality
        tests.add(new QueryTest("Order Review Rate", QueryManager.ORDER_REVIEW_RATE));
        tests.add(new QueryTest("Worst Rated Category", QueryManager.WORST_RATED_CATEGORY));
        tests.add(new QueryTest("Orders Paid in Full", QueryManager.ORDERS_PAID_IN_FULL));
        tests.add(new QueryTest("Review Score Extremes", QueryManager.REVIEW_SCORE_EXTREMES));

        // Payment Analysis
        tests.add(new QueryTest("Most Common Payment Type by State", QueryManager.MOST_COMMON_PAYMENT_TYPE_BY_STATE));
        tests.add(new QueryTest("Avg Installments by Payment Type", QueryManager.AVG_INSTALLMENTS_BY_PAYMENT_TYPE));

        // Additional Analysis
        tests.add(new QueryTest("Delivery Performance by State", QueryManager.DELIVERY_PERFORMANCE_BY_STATE));
        tests.add(new QueryTest("Category Performance by Quarter", QueryManager.CATEGORY_PERFORMANCE_BY_QUARTER));

        // Parameterized queries - test with sample values
        tests.add(new QueryTest("Revenue by State and Year (SP, 2017)", QueryManager.REVENUE_BY_STATE_AND_YEAR, "SP", "2017"));
        tests.add(new QueryTest("Sellers by Category (furniture)", QueryManager.SELLERS_BY_CATEGORY, "%furniture%"));

        int passed = 0;
        int failed = 0;
        int noResults = 0;

        for (QueryTest test : tests) {
            System.out.println("┌─────────────────────────────────────────────────────────────────┐");
            System.out.printf("│ Testing: %-56s│%n", test.name);
            System.out.println("└─────────────────────────────────────────────────────────────────┘");

            try {
                int rowCount = executeQueryTest(test);

                if (rowCount > 0) {
                    System.out.printf("✓ PASS - Returned %,d rows%n", rowCount);
                    passed++;
                } else {
                    System.out.println("⚠ WARNING - Query executed but returned 0 rows");
                    noResults++;
                }

            } catch (SQLException e) {
                System.out.println("✗ FAIL - SQL Error: " + e.getMessage());
                failed++;
            } catch (Exception e) {
                System.out.println("✗ FAIL - Error: " + e.getMessage());
                failed++;
            }

            System.out.println();
        }

        // Summary
        System.out.println("╔═════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        TEST SUMMARY                             ║");
        System.out.println("╠═════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Queries Tested: %-42d║%n", tests.size());
        System.out.printf("║ ✓ Passed (with results): %-37d║%n", passed);
        System.out.printf("║ ⚠ No Results: %-49d║%n", noResults);
        System.out.printf("║ ✗ Failed (errors): %-44d║%n", failed);
        System.out.println("╚═════════════════════════════════════════════════════════════════╝");

        if (failed > 0) {
            System.out.println("\n❌ Some queries failed! Review the errors above.");
        } else if (noResults > 0) {
            System.out.println("\n⚠️  Some queries returned no results. They may need adjustment.");
        } else {
            System.out.println("\n✅ All queries passed successfully!");
        }
    }

    private static int executeQueryTest(QueryTest test) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        int rowCount = 0;

        if (test.isParameterized) {
            try (PreparedStatement pstmt = conn.prepareStatement(test.sql)) {
                // Set parameters
                for (int i = 0; i < test.paramValues.length; i++) {
                    pstmt.setString(i + 1, test.paramValues[i]);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Show first row as sample
                    if (rs.next()) {
                        rowCount++;
                        System.out.print("  Sample: ");
                        for (int i = 1; i <= Math.min(3, columnCount); i++) {
                            Object value = rs.getObject(i);
                            System.out.print(metaData.getColumnName(i) + "=" + value);
                            if (i < Math.min(3, columnCount)) System.out.print(", ");
                        }
                        if (columnCount > 3) System.out.print("...");
                        System.out.println();

                        // Count remaining rows
                        while (rs.next()) {
                            rowCount++;
                        }
                    }
                }
            }
        } else {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(test.sql)) {

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Show first row as sample
                if (rs.next()) {
                    rowCount++;
                    System.out.print("  Sample: ");
                    for (int i = 1; i <= Math.min(3, columnCount); i++) {
                        Object value = rs.getObject(i);
                        System.out.print(metaData.getColumnName(i) + "=" + value);
                        if (i < Math.min(3, columnCount)) System.out.print(", ");
                    }
                    if (columnCount > 3) System.out.print("...");
                    System.out.println();

                    // Count remaining rows
                    while (rs.next()) {
                        rowCount++;
                    }
                }
            }
        }

        return rowCount;
    }
}
