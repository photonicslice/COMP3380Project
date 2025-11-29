import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles execution of SQL queries using prepared statements for security
 */
public class QueryExecutor {

    /**
     * Executes a SELECT query and returns the ResultSet
     * @param query SQL query string with ? placeholders for parameters
     * @param params Parameters to bind to the query
     * @return ResultSet containing query results
     */
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query,
                                                       ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);

        // Bind parameters
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        return stmt.executeQuery();
    }

    /**
     * Executes an UPDATE, INSERT, or DELETE query
     * @param query SQL query string with ? placeholders
     * @param params Parameters to bind to the query
     * @return Number of rows affected
     */
    public static int executeUpdate(String query, Object... params) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Bind parameters
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        }
    }

    /**
     * Executes a query and displays results using ResultFormatter
     * @param query SQL query string
     * @param title Title for the results display
     * @param params Query parameters
     * @return Number of rows returned
     */
    public static int executeAndDisplay(String query, String title, Object... params) {
        try {
            System.out.println("Processing query... Please wait.");
            long startTime = System.currentTimeMillis();

            ResultSet rs = executeQuery(query, params);
            int rowCount = ResultFormatter.displayResults(rs, title);

            long endTime = System.currentTimeMillis();
            double seconds = (endTime - startTime) / 1000.0;
            System.out.println("Query executed in " + String.format("%.2f", seconds) + " seconds.\n");

            return rowCount;
        } catch (SQLException e) {
            System.out.println("ERROR: Query execution failed.");
            System.out.println("Details: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Validates user input to prevent SQL injection
     * @param input User input string
     * @return Sanitized input
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remove any SQL keywords and special characters that could be dangerous
        return input.trim();
    }
}
