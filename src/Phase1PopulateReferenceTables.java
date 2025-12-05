import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Phase 1: Populate Reference Tables
 * Loads: STATES, CATEGORIES, GEOLOCATION
 *
 * These are foundational lookup tables that have no foreign key dependencies.
 */
public class Phase1PopulateReferenceTables {

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("PHASE 1: POPULATING REFERENCE TABLES");
        System.out.println("=====================================");
        System.out.println("Tables: STATES, CATEGORIES, GEOLOCATION");
        System.out.println();

        long startTime = System.currentTimeMillis();

        try {
            loadStates();
            loadCategories();
            loadGeolocation();

            long endTime = System.currentTimeMillis();
            double seconds = (endTime - startTime) / 1000.0;

            System.out.println();
            System.out.println("=====================================");
            System.out.println("[SUCCESS] Phase 1 completed in " +
                             String.format("%.2f", seconds) + " seconds");
            System.out.println("=====================================");
            System.out.println();
            System.out.println("Next step: Run Phase2PopulateEntityTables");

        } catch (Exception e) {
            System.out.println();
            System.out.println("=====================================");
            System.out.println("[ERROR] Phase 1 failed!");
            System.out.println("=====================================");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
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

    private static void loadCategories() throws SQLException, IOException {
        System.out.print("Loading CATEGORIES... ");
        String file = CSVDataLoader.DATA_DIR + "product_category_name_translation.csv";

        String insertSQL = "INSERT INTO CATEGORIES (category_name_portuguese, category_name_english) " +
                          "VALUES (?, ?)";

        int count = CSVDataLoader.loadCSV(file, insertSQL, 2);
        System.out.println("Done (" + count + " records)");
    }

    private static void loadGeolocation() throws SQLException, IOException {
        System.out.print("Loading GEOLOCATION... ");
        String file = CSVDataLoader.DATA_DIR + "olist_geolocation_dataset.csv";

        String insertSQL = "INSERT INTO GEOLOCATION (zip_code_prefix, geolocation_lat, " +
                          "geolocation_lng, geolocation_city, geolocation_state) " +
                          "VALUES (?, ?, ?, ?, ?)";

        int count = CSVDataLoader.loadCSV(file, insertSQL, 5);
        System.out.println("Done (" + count + " records)");
    }
}
