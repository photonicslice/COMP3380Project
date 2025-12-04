import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests all queries to identify which ones return no rows
 */
public class QueryTester {

    private static class QueryInfo {
        String name;
        String query;
        Object[] params;

        QueryInfo(String name, String query, Object... params) {
            this.name = name;
            this.query = query;
            this.params = params;
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing all queries to find empty results...\n");
        DatabaseConnection.getConnection();

        List<QueryInfo> queries = new ArrayList<>();

        // Market and Sales Analysis
        queries.add(new QueryInfo("HIGH_VALUE_CUSTOMER_STATES", QueryManager.HIGH_VALUE_CUSTOMER_STATES));
        queries.add(new QueryInfo("TOP_SELLING_CATEGORIES", QueryManager.TOP_SELLING_CATEGORIES));
        queries.add(new QueryInfo("STATES_WITH_CUSTOMERS_NO_ORDERS", QueryManager.STATES_WITH_CUSTOMERS_NO_ORDERS));
        queries.add(new QueryInfo("HIGHEST_SALES_GEOLOCATION", QueryManager.HIGHEST_SALES_GEOLOCATION));

        // Seller Performance
        queries.add(new QueryInfo("SELLER_SUCCESS_RATE", QueryManager.SELLER_SUCCESS_RATE));
        queries.add(new QueryInfo("UNUSED_PRODUCT_CATALOG", QueryManager.UNUSED_PRODUCT_CATALOG));
        queries.add(new QueryInfo("STATES_WITH_CUSTOMERS_NO_SELLERS", QueryManager.STATES_WITH_CUSTOMERS_NO_SELLERS));
        queries.add(new QueryInfo("SINGLE_PRODUCT_SELLERS", QueryManager.SINGLE_PRODUCT_SELLERS));

        // Order and Review Quality
        queries.add(new QueryInfo("ORDER_REVIEW_RATE", QueryManager.ORDER_REVIEW_RATE));
        queries.add(new QueryInfo("WORST_RATED_CATEGORY", QueryManager.WORST_RATED_CATEGORY));
        queries.add(new QueryInfo("ORDERS_PAID_IN_FULL", QueryManager.ORDERS_PAID_IN_FULL));
        queries.add(new QueryInfo("REVIEW_SCORE_EXTREMES", QueryManager.REVIEW_SCORE_EXTREMES));

        // Payment and Transaction Analysis
        queries.add(new QueryInfo("MOST_COMMON_PAYMENT_TYPE_BY_STATE", QueryManager.MOST_COMMON_PAYMENT_TYPE_BY_STATE));
        queries.add(new QueryInfo("AVG_INSTALLMENTS_BY_PAYMENT_TYPE", QueryManager.AVG_INSTALLMENTS_BY_PAYMENT_TYPE));

        // Customer Behavior
        queries.add(new QueryInfo("REPEAT_PURCHASE_CUSTOMERS", QueryManager.REPEAT_PURCHASE_CUSTOMERS));
        queries.add(new QueryInfo("AVG_TIME_BETWEEN_ORDERS", QueryManager.AVG_TIME_BETWEEN_ORDERS));

        // Additional Analysis
        queries.add(new QueryInfo("DELIVERY_PERFORMANCE_BY_STATE", QueryManager.DELIVERY_PERFORMANCE_BY_STATE));
        queries.add(new QueryInfo("CATEGORY_PERFORMANCE_BY_QUARTER", QueryManager.CATEGORY_PERFORMANCE_BY_QUARTER));

        // Parameterized queries with sample data
        queries.add(new QueryInfo("REVENUE_BY_STATE_AND_YEAR", QueryManager.REVENUE_BY_STATE_AND_YEAR, "SP", "2017%"));
        queries.add(new QueryInfo("SELLER_INVENTORY_CHECK", QueryManager.SELLER_INVENTORY_CHECK, "100%"));

        List<String> emptyQueries = new ArrayList<>();
        int totalQueries = queries.size();
        int queriesWithResults = 0;

        for (QueryInfo qi : queries) {
            try {
                System.out.print("Testing " + qi.name + "... ");
                ResultSet rs = QueryExecutor.executeQuery(qi.query, qi.params);

                boolean hasRows = rs.next();

                if (hasRows) {
                    // Count rows
                    int rowCount = 1;
                    while (rs.next()) {
                        rowCount++;
                    }
                    System.out.println("OK (" + rowCount + " rows)");
                    queriesWithResults++;
                } else {
                    System.out.println("EMPTY - NO ROWS RETURNED");
                    emptyQueries.add(qi.name);
                }

                rs.close();
            } catch (SQLException e) {
                System.out.println("ERROR: " + e.getMessage());
                emptyQueries.add(qi.name + " (ERROR)");
            }
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println("Total queries tested: " + totalQueries);
        System.out.println("Queries with results: " + queriesWithResults);
        System.out.println("Queries with no results: " + emptyQueries.size());

        if (!emptyQueries.isEmpty()) {
            System.out.println("\nQueries returning NO ROWS:");
            for (String queryName : emptyQueries) {
                System.out.println("  - " + queryName);
            }
        } else {
            System.out.println("\nAll queries returned results!");
        }

        DatabaseConnection.closeConnection();
    }
}
