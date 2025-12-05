import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Loads data from CSV files into the database tables.
 * Handles all data population and table clearing operations.
 */
public class CSVDataLoader {
    public static final String DATA_DIR = "data/";
    public static final int BATCH_SIZE = 5000; // Batch size for bulk inserts

    /**
     * Clears all data from all tables in the correct order (respecting foreign keys)
     */
    public static void clearAllData() {
        System.out.println("\nClearing all data from database...");
        Connection conn = DatabaseConnection.getConnection();

        String[] tables = {
            "ORDER_REVIEWS",
            "ORDER_PAYMENTS",
            "ORDER_ITEMS",
            "ORDERS",
            "PRODUCTS",
            "CATEGORIES",
            "SELLERS",
            "CUSTOMERS",
            "GEOLOCATION",
            "STATES"
        };

        try (Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                stmt.executeUpdate("DELETE FROM " + table);
                System.out.println("[OK] Cleared " + table);
            }
            System.out.println("\nAll data cleared successfully.");
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to clear data.");
            System.out.println("Details: " + e.getMessage());
        }
    }

    /**
     * Populates all tables from CSV files in the correct order
     */
    public static void populateAllTables() {
        System.out.println("\nPopulating database from CSV files...");
        long startTime = System.currentTimeMillis();

        try {
            // Load in correct order respecting foreign key dependencies
            loadStates();
            loadGeolocation();
            loadCategories();
            loadCustomers();
            loadSellers();
            loadProducts();
            loadOrders();
            loadOrderItems();
            loadOrderPayments();
            loadOrderReviews();

            long endTime = System.currentTimeMillis();
            double seconds = (endTime - startTime) / 1000.0;
            System.out.println("\n[SUCCESS] All data loaded successfully in " +
                             String.format("%.2f", seconds) + " seconds.");
        } catch (Exception e) {
            System.out.println("\nERROR: Failed to populate database.");
            System.out.println("Details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadStates() throws SQLException, IOException {
        System.out.print("Loading STATES... ");
        Connection conn = DatabaseConnection.getConnection();
        boolean originalAutoCommit = conn.getAutoCommit();

        // Create states manually (Brazilian states)
        String insertSQL = "INSERT INTO STATES (state_code, state_name, region) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            conn.setAutoCommit(false);

            String[][] states = {
                {"AC", "Acre", "North"},
                {"AL", "Alagoas", "Northeast"},
                {"AP", "Amapá", "North"},
                {"AM", "Amazonas", "North"},
                {"BA", "Bahia", "Northeast"},
                {"CE", "Ceará", "Northeast"},
                {"DF", "Distrito Federal", "Central-West"},
                {"ES", "Espírito Santo", "Southeast"},
                {"GO", "Goiás", "Central-West"},
                {"MA", "Maranhão", "Northeast"},
                {"MT", "Mato Grosso", "Central-West"},
                {"MS", "Mato Grosso do Sul", "Central-West"},
                {"MG", "Minas Gerais", "Southeast"},
                {"PA", "Pará", "North"},
                {"PB", "Paraíba", "Northeast"},
                {"PR", "Paraná", "South"},
                {"PE", "Pernambuco", "Northeast"},
                {"PI", "Piauí", "Northeast"},
                {"RJ", "Rio de Janeiro", "Southeast"},
                {"RN", "Rio Grande do Norte", "Northeast"},
                {"RS", "Rio Grande do Sul", "South"},
                {"RO", "Rondônia", "North"},
                {"RR", "Roraima", "North"},
                {"SC", "Santa Catarina", "South"},
                {"SP", "São Paulo", "Southeast"},
                {"SE", "Sergipe", "Northeast"},
                {"TO", "Tocantins", "North"}
            };

            for (String[] state : states) {
                pstmt.setString(1, state[0]);
                pstmt.setString(2, state[1]);
                pstmt.setString(3, state[2]);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            throw e;
        } finally {
            try {
                conn.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                System.err.println("Failed to restore auto-commit: " + e.getMessage());
            }
        }
        System.out.println("Done (27 records)");
    }

    private static void loadGeolocation() throws SQLException, IOException {
        System.out.print("Loading GEOLOCATION... ");
        String file = DATA_DIR + "olist_geolocation_dataset.csv";
        Connection conn = DatabaseConnection.getConnection();

        String insertSQL = "INSERT INTO GEOLOCATION (zip_code_prefix, geolocation_lat, " +
                          "geolocation_lng, geolocation_city, geolocation_state) " +
                          "VALUES (?, ?, ?, ?, ?)";

        int count = loadCSV(file, insertSQL, 5);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadCategories() throws SQLException, IOException {
        System.out.print("Loading CATEGORIES... ");
        String file = DATA_DIR + "product_category_name_translation.csv";
        Connection conn = DatabaseConnection.getConnection();

        String insertSQL = "INSERT INTO CATEGORIES (category_name_portuguese, category_name_english) " +
                          "VALUES (?, ?)";

        int count = loadCSV(file, insertSQL, 2);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadCustomers() throws SQLException, IOException {
        System.out.print("Loading CUSTOMERS... ");
        String file = DATA_DIR + "olist_customers_dataset.csv";
        Connection conn = DatabaseConnection.getConnection();

        String insertSQL = "INSERT INTO CUSTOMERS (customer_id, customer_unique_id, " +
                          "customer_zip_code_prefix, customer_city, customer_state) " +
                          "VALUES (?, ?, ?, ?, ?)";

        int count = loadCSV(file, insertSQL, 5);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadSellers() throws SQLException, IOException {
        System.out.print("Loading SELLERS... ");
        String file = DATA_DIR + "olist_sellers_dataset.csv";
        Connection conn = DatabaseConnection.getConnection();

        String insertSQL = "INSERT INTO SELLERS (seller_id, seller_zip_code_prefix, " +
                          "seller_city, seller_state) VALUES (?, ?, ?, ?)";

        int count = loadCSV(file, insertSQL, 4);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadProducts() throws SQLException, IOException {
        System.out.print("Loading PRODUCTS... ");
        String file = DATA_DIR + "olist_products_dataset.csv";
        Connection conn = DatabaseConnection.getConnection();

        String insertSQL = "INSERT INTO PRODUCTS (product_id, category_name_portuguese, " +
                          "product_name_length, product_description_length, product_photos_qty, " +
                          "product_weight_g, product_length_cm, product_height_cm, product_width_cm) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int count = loadCSV(file, insertSQL, 9);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadOrders() throws SQLException, IOException {
        System.out.print("Loading ORDERS... ");
        String file = DATA_DIR + "olist_orders_dataset.csv";
        Connection conn = DatabaseConnection.getConnection();

        String insertSQL = "INSERT INTO ORDERS (order_id, customer_id, order_status, " +
                          "order_purchase_timestamp, order_approved_at, order_delivered_carrier_date, " +
                          "order_delivered_customer_date, order_estimated_delivery_date) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int count = loadCSV(file, insertSQL, 8);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadOrderItems() throws SQLException, IOException {
        System.out.print("Loading ORDER_ITEMS... ");
        String file = DATA_DIR + "olist_order_items_dataset.csv";
        Connection conn = DatabaseConnection.getConnection();

        String insertSQL = "INSERT INTO ORDER_ITEMS (order_id, order_item_seq, product_id, " +
                          "seller_id, shipping_limit_date, price, freight_value) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int count = loadCSV(file, insertSQL, 7);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadOrderPayments() throws SQLException, IOException {
        System.out.print("Loading ORDER_PAYMENTS... ");
        String file = DATA_DIR + "olist_order_payments_dataset.csv";
        Connection conn = DatabaseConnection.getConnection();

        String insertSQL = "INSERT INTO ORDER_PAYMENTS (order_id, payment_sequential, " +
                          "payment_type, payment_installments, payment_value) " +
                          "VALUES (?, ?, ?, ?, ?)";

        int count = loadCSV(file, insertSQL, 5);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadOrderReviews() throws SQLException, IOException {
        System.out.print("Loading ORDER_REVIEWS... ");
        String file = DATA_DIR + "olist_order_reviews_dataset.csv";
        Connection conn = DatabaseConnection.getConnection();

        String insertSQL = "INSERT INTO ORDER_REVIEWS (review_id, order_id, review_score, " +
                          "review_comment_title, review_comment_message, review_creation_date, " +
                          "review_answer_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";

        int count = loadCSV(file, insertSQL, 7);
        System.out.println("Done (" + count + " records)");
    }

    /**
     * Generic CSV loader that reads a file and executes prepared statement for each row
     * Optimized with batch processing and transaction control
     */
    public static int loadCSV(String filename, String insertSQL, int columnCount)
            throws SQLException, IOException {
        Connection conn = DatabaseConnection.getConnection();
        int count = 0;
        long startTime = System.currentTimeMillis();
        boolean originalAutoCommit = conn.getAutoCommit();

        try (BufferedReader br = new BufferedReader(new FileReader(filename));
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            // Disable auto-commit for transaction optimization
            conn.setAutoCommit(false);

            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = parseCSVLine(line);

                if (values.length >= columnCount) {
                    for (int i = 0; i < columnCount; i++) {
                        String value = values[i].trim();
                        if (value.isEmpty() || value.equalsIgnoreCase("null")) {
                            pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
                        } else {
                            pstmt.setString(i + 1, value);
                        }
                    }

                    // Add to batch instead of executing immediately
                    pstmt.addBatch();
                    count++;

                    // Execute batch every BATCH_SIZE records
                    if (count % BATCH_SIZE == 0) {
                        pstmt.executeBatch();
                        pstmt.clearBatch();
                        conn.commit();

                        // Progress indicator
                        long elapsed = System.currentTimeMillis() - startTime;
                        double rate = count / (elapsed / 1000.0);
                        System.out.print(String.format(" %,d (%.0f/s)", count, rate));
                    }
                }
            }

            // Execute remaining batch
            if (count % BATCH_SIZE != 0) {
                pstmt.executeBatch();
                pstmt.clearBatch();
            }

            // Final commit
            conn.commit();

        } catch (SQLException | IOException e) {
            // Rollback on error
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            throw e;
        } finally {
            // Restore original auto-commit setting
            try {
                conn.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                System.err.println("Failed to restore auto-commit: " + e.getMessage());
            }
        }

        return count;
    }

    /**
     * Parses a CSV line handling quoted fields
     */
    public static String[] parseCSVLine(String line) {
        java.util.List<String> values = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString());

        return values.toArray(new String[0]);
    }
}
