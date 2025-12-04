import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Quick tester for specific queries
 */
public class QuickQueryTester {

    public static void main(String[] args) {
        System.out.println("Quick testing queries for empty results...\n");
        DatabaseConnection.getConnection();

        // Test specific queries quickly
        testQuery("TOP_SELLING_CATEGORIES", QueryManager.TOP_SELLING_CATEGORIES);
        testQuery("STATES_WITH_CUSTOMERS_NO_ORDERS", QueryManager.STATES_WITH_CUSTOMERS_NO_ORDERS);
        testQuery("UNUSED_PRODUCT_CATALOG", QueryManager.UNUSED_PRODUCT_CATALOG);
        testQuery("STATES_WITH_CUSTOMERS_NO_SELLERS", QueryManager.STATES_WITH_CUSTOMERS_NO_SELLERS);
        testQuery("REPEAT_PURCHASE_CUSTOMERS", QueryManager.REPEAT_PURCHASE_CUSTOMERS);
        testQuery("AVG_TIME_BETWEEN_ORDERS", QueryManager.AVG_TIME_BETWEEN_ORDERS);

        DatabaseConnection.closeConnection();
    }

    private static void testQuery(String name, String query) {
        try {
            System.out.print("Testing " + name + "... ");
            ResultSet rs = QueryExecutor.executeQuery(query);

            if (rs.next()) {
                System.out.println("HAS ROWS");
            } else {
                System.out.println("EMPTY");
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
