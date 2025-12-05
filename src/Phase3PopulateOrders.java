import java.io.IOException;
import java.sql.SQLException;

/**
 * Phase 3: Populate Transaction Tables
 * Loads: ORDERS
 *
 * Order transactions that depend on customers.
 * Must run after Phase 2.
 */
public class Phase3PopulateOrders {

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("PHASE 3: POPULATING TRANSACTION TABLES");
        System.out.println("=====================================");
        System.out.println("Tables: ORDERS");
        System.out.println();

        long startTime = System.currentTimeMillis();

        try {
            loadOrders();

            long endTime = System.currentTimeMillis();
            double seconds = (endTime - startTime) / 1000.0;

            System.out.println();
            System.out.println("=====================================");
            System.out.println("[SUCCESS] Phase 3 completed in " +
                             String.format("%.2f", seconds) + " seconds");
            System.out.println("=====================================");
            System.out.println();
            System.out.println("Next step: Run Phase4PopulateRelationships");

        } catch (Exception e) {
            System.out.println();
            System.out.println("=====================================");
            System.out.println("[ERROR] Phase 3 failed!");
            System.out.println("=====================================");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void loadOrders() throws SQLException, IOException {
        System.out.print("Loading ORDERS... ");
        String file = CSVDataLoader.DATA_DIR + "olist_orders_dataset.csv";

        String insertSQL = "INSERT INTO ORDERS (order_id, customer_id, order_status, " +
                          "order_purchase_timestamp, order_approved_at, order_delivered_carrier_date, " +
                          "order_delivered_customer_date, order_estimated_delivery_date) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int count = CSVDataLoader.loadCSV(file, insertSQL, 8);
        System.out.println("Done (" + count + " records)");
    }
}
