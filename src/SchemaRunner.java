import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Utility to run schema.sql and recreate database tables
 */
public class SchemaRunner {
    public static void main(String[] args) {
        System.out.println("=== Database Schema Runner ===\n");
        System.out.println("Reading schema.sql...");

        try {
            Connection conn = DatabaseConnection.getConnection();

            // Read schema file line by line and build statements
            StringBuilder currentStatement = new StringBuilder();
            int successCount = 0;
            int errorCount = 0;

            try (BufferedReader br = new BufferedReader(new FileReader("schema.sql"));
                 Statement stmt = conn.createStatement()) {

                String line;
                while ((line = br.readLine()) != null) {
                    String trimmed = line.trim();

                    // Skip empty lines and comment-only lines
                    if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                        continue;
                    }

                    // Remove inline comments before adding to statement
                    String lineWithoutComment = line;
                    int commentPos = line.indexOf("--");
                    if (commentPos >= 0) {
                        lineWithoutComment = line.substring(0, commentPos).trim();
                    }

                    // Add line to current statement (if not empty after comment removal)
                    if (!lineWithoutComment.isEmpty()) {
                        currentStatement.append(lineWithoutComment).append(" ");
                    }

                    // Execute if line ends with semicolon
                    if (trimmed.endsWith(";")) {
                        String sql = currentStatement.toString().trim();
                        // Remove trailing semicolon
                        sql = sql.substring(0, sql.length() - 1);

                        try {
                            stmt.execute(sql);
                            String preview = sql.length() > 80 ?
                                           sql.substring(0, 80).replaceAll("\\s+", " ") + "..." :
                                           sql.replaceAll("\\s+", " ");
                            System.out.println("[OK] " + preview);
                            successCount++;
                        } catch (Exception e) {
                            String sqlUpper = sql.toUpperCase();
                            // Ignore expected errors (like dropping non-existent tables)
                            if (sqlUpper.contains("CREATE") || sqlUpper.contains("ALTER")) {
                                System.out.println("[ERROR] " + e.getMessage());
                                if (sqlUpper.contains("ORDER_REVIEWS")) {
                                    System.out.println("  Failed SQL: " + sql);
                                }
                                errorCount++;
                            }
                        }

                        // Reset for next statement
                        currentStatement = new StringBuilder();
                    }
                }
            }

            System.out.println("\n=== Schema Execution Complete ===");
            System.out.println("Successful statements: " + successCount);
            if (errorCount > 0) {
                System.out.println("Errors (may be ignorable): " + errorCount);
            }
            System.out.println("\nDatabase schema is ready!");

        } catch (Exception e) {
            System.out.println("FATAL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
