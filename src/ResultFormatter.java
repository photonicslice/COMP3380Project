import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Formats SQL query results as ASCII tables with proper alignment
 */
public class ResultFormatter {
    private static final int MAX_COLUMN_WIDTH = 25;
    private static final int ROWS_PER_PAGE = 25;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
    private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#,###");

    /**
     * Displays query results in a formatted ASCII table with pagination
     * @param rs ResultSet from query execution
     * @param queryTitle Title to display above the table
     * @return Number of rows displayed
     */
    public static int displayResults(ResultSet rs, String queryTitle) throws SQLException {
        if (rs == null) {
            System.out.println("No results to display.");
            return 0;
        }

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Get column names and determine widths
        String[] columnNames = new String[columnCount];
        int[] columnWidths = new int[columnCount];

        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = metaData.getColumnLabel(i + 1);
            columnWidths[i] = Math.min(columnNames[i].length(), MAX_COLUMN_WIDTH);
        }

        // Read all data into memory to determine column widths and enable pagination
        List<String[]> rows = new ArrayList<>();
        while (rs.next()) {
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                Object value = rs.getObject(i + 1);
                String strValue;

                if (value == null) {
                    strValue = "N/A";
                } else if (value instanceof Double || value instanceof Float) {
                    strValue = DECIMAL_FORMAT.format(value);
                } else if (value instanceof Integer || value instanceof Long) {
                    strValue = INTEGER_FORMAT.format(value);
                } else {
                    strValue = value.toString();
                }

                row[i] = strValue;
                columnWidths[i] = Math.max(columnWidths[i], Math.min(strValue.length(), MAX_COLUMN_WIDTH));
            }
            rows.add(row);
        }

        if (rows.isEmpty()) {
            printSeparator();
            System.out.println("Query Results: " + queryTitle);
            printSeparator();
            System.out.println("No rows returned.");
            printSeparator();
            return 0;
        }

        // Display results with pagination
        int totalRows = rows.size();
        int currentRow = 0;

        while (currentRow < totalRows) {
            // Print header
            printSeparator();
            System.out.println("Query Results: " + queryTitle);
            printSeparator();

            int endRow = Math.min(currentRow + ROWS_PER_PAGE, totalRows);
            System.out.println("Showing rows " + (currentRow + 1) + "-" + endRow + " of " + totalRows + " total results");
            System.out.println();

            // Print column headers
            printRowSeparator(columnWidths);
            System.out.print("|");
            for (int i = 0; i < columnCount; i++) {
                System.out.print(" " + padString(columnNames[i], columnWidths[i]) + " |");
            }
            System.out.println();
            printRowSeparator(columnWidths);

            // Print rows for this page
            for (int r = currentRow; r < endRow; r++) {
                String[] row = rows.get(r);
                System.out.print("|");
                for (int i = 0; i < columnCount; i++) {
                    String value = row[i];
                    if (value.length() > MAX_COLUMN_WIDTH) {
                        value = value.substring(0, MAX_COLUMN_WIDTH - 3) + "...";
                    }
                    System.out.print(" " + padString(value, columnWidths[i]) + " |");
                }
                System.out.println();
            }
            printRowSeparator(columnWidths);

            currentRow = endRow;

            // Pagination prompt
            if (currentRow < totalRows) {
                System.out.println("\n[Enter] Next page (" + (currentRow + 1) + "-" +
                                 Math.min(currentRow + ROWS_PER_PAGE, totalRows) + ") | [Q] Return to menu");
                System.out.print("> ");
                try {
                    String input = new java.util.Scanner(System.in).nextLine().trim();
                    if (input.equalsIgnoreCase("q") || input.equalsIgnoreCase("quit")) {
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }

        System.out.println("\nQuery returned " + totalRows + " row(s).");
        return totalRows;
    }

    /**
     * Pads a string to the specified width
     */
    private static String padString(String str, int width) {
        if (str.length() >= width) {
            return str.substring(0, width);
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < width) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * Prints a row separator line based on column widths
     */
    private static void printRowSeparator(int[] columnWidths) {
        System.out.print("+");
        for (int width : columnWidths) {
            for (int i = 0; i < width + 2; i++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();
    }

    /**
     * Prints a standard separator line
     */
    private static void printSeparator() {
        for (int i = 0; i < 80; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    /**
     * Displays a simple message count result
     */
    public static void displayCount(int count, String message) {
        printSeparator();
        System.out.println(message + ": " + INTEGER_FORMAT.format(count));
        printSeparator();
    }
}
