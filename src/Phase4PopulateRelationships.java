import java.io.IOException;
import java.sql.SQLException;

/**
 * Phase 4: Populate Relationship Tables
 * Loads: ORDER_ITEMS, ORDER_PAYMENTS, ORDER_REVIEWS
 *
 * These tables link orders with products, sellers, payments, and reviews.
 * Must run after Phase 3.
 */
public class Phase4PopulateRelationships {

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("PHASE 4: POPULATING RELATIONSHIP TABLES");
        System.out.println("=====================================");
        System.out.println("Tables: ORDER_ITEMS, ORDER_PAYMENTS, ORDER_REVIEWS");
        System.out.println();

        long startTime = System.currentTimeMillis();

        try {
            loadOrderItems();
            loadOrderPayments();
            loadOrderReviews();

            long endTime = System.currentTimeMillis();
            double seconds = (endTime - startTime) / 1000.0;

            System.out.println();
            System.out.println("=====================================");
            System.out.println("[SUCCESS] Phase 4 completed in " +
                             String.format("%.2f", seconds) + " seconds");
            System.out.println("=====================================");
            System.out.println();
            System.out.println("All phases complete! Database is fully populated.");

        } catch (Exception e) {
            System.out.println();
            System.out.println("=====================================");
            System.out.println("[ERROR] Phase 4 failed!");
            System.out.println("=====================================");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void loadOrderItems() throws SQLException, IOException {
        System.out.print("Loading ORDER_ITEMS... ");
        String file = CSVDataLoader.DATA_DIR + "olist_order_items_dataset.csv";

        String insertSQL = "INSERT INTO ORDER_ITEMS (order_id, order_item_seq, product_id, " +
                          "seller_id, shipping_limit_date, price, freight_value) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int count = CSVDataLoader.loadCSV(file, insertSQL, 7);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadOrderPayments() throws SQLException, IOException {
        System.out.print("Loading ORDER_PAYMENTS... ");
        String file = CSVDataLoader.DATA_DIR + "olist_order_payments_dataset.csv";

        String insertSQL = "INSERT INTO ORDER_PAYMENTS (order_id, payment_sequential, " +
                          "payment_type, payment_installments, payment_value) " +
                          "VALUES (?, ?, ?, ?, ?)";

        int count = CSVDataLoader.loadCSV(file, insertSQL, 5);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadOrderReviews() throws SQLException, IOException {
        System.out.print("Loading ORDER_REVIEWS... ");
        String file = CSVDataLoader.DATA_DIR + "olist_order_reviews_dataset.csv";

        String insertSQL = "INSERT INTO ORDER_REVIEWS (review_id, order_id, review_score, " +
                          "review_comment_title, review_comment_message, review_creation_date, " +
                          "review_answer_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";

        int count = CSVDataLoader.loadCSV(file, insertSQL, 7);
        System.out.println("Done (" + count + " records)");
    }
}
