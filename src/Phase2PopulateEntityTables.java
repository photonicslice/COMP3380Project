import java.io.IOException;
import java.sql.SQLException;

/**
 * Phase 2: Populate Entity Tables
 * Loads: CUSTOMERS, SELLERS, PRODUCTS
 *
 * These are core business entities that depend on reference tables (Categories).
 * Must run after Phase 1.
 */
public class Phase2PopulateEntityTables {

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("PHASE 2: POPULATING ENTITY TABLES");
        System.out.println("=====================================");
        System.out.println("Tables: CUSTOMERS, SELLERS, PRODUCTS");
        System.out.println();

        long startTime = System.currentTimeMillis();

        try {
            loadCustomers();
            loadSellers();
            loadProducts();

            long endTime = System.currentTimeMillis();
            double seconds = (endTime - startTime) / 1000.0;

            System.out.println();
            System.out.println("=====================================");
            System.out.println("[SUCCESS] Phase 2 completed in " +
                             String.format("%.2f", seconds) + " seconds");
            System.out.println("=====================================");
            System.out.println();
            System.out.println("Next step: Run Phase3PopulateOrders");

        } catch (Exception e) {
            System.out.println();
            System.out.println("=====================================");
            System.out.println("[ERROR] Phase 2 failed!");
            System.out.println("=====================================");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void loadCustomers() throws SQLException, IOException {
        System.out.print("Loading CUSTOMERS... ");
        String file = CSVDataLoader.DATA_DIR + "olist_customers_dataset.csv";

        String insertSQL = "INSERT INTO CUSTOMERS (customer_id, customer_unique_id, " +
                          "customer_zip_code_prefix, customer_city, customer_state) " +
                          "VALUES (?, ?, ?, ?, ?)";

        int count = CSVDataLoader.loadCSV(file, insertSQL, 5);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadSellers() throws SQLException, IOException {
        System.out.print("Loading SELLERS... ");
        String file = CSVDataLoader.DATA_DIR + "olist_sellers_dataset.csv";

        String insertSQL = "INSERT INTO SELLERS (seller_id, seller_zip_code_prefix, " +
                          "seller_city, seller_state) VALUES (?, ?, ?, ?)";

        int count = CSVDataLoader.loadCSV(file, insertSQL, 4);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadProducts() throws SQLException, IOException {
        System.out.print("Loading PRODUCTS... ");
        String file = CSVDataLoader.DATA_DIR + "olist_products_dataset.csv";

        String insertSQL = "INSERT INTO PRODUCTS (product_id, category_name_portuguese, " +
                          "product_name_length, product_description_length, product_photos_qty, " +
                          "product_weight_g, product_length_cm, product_height_cm, product_width_cm) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int count = CSVDataLoader.loadCSV(file, insertSQL, 9);
        System.out.println("Done (" + count + " records)");
    }
}
