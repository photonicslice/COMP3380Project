import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connections with credentials loaded from auth.cfg file.
 * Provides singleton connection to SQL Server database.
 */
public class DatabaseConnection {
    private static Connection connection = null;
    private static String username;
    private static String password;
    private static final String DATABASE_NAME = "cs3380";
    private static final String SERVER_URL = "uranium.cs.umanitoba.ca:1433";

    /**
     * Loads database credentials from auth.cfg file
     */
    private static void loadCredentials() {
        if (username != null && password != null) {
            return; // Already loaded
        }

        Properties prop = new Properties();
        String fileName = "auth.cfg";

        try {
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: Could not find config file 'auth.cfg'.");
            System.out.println("Please create auth.cfg with your database credentials.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("ERROR: Error reading config file.");
            System.exit(1);
        }

        username = prop.getProperty("username");
        password = prop.getProperty("password");

        if (username == null || password == null) {
            System.out.println("ERROR: Username or password not provided in auth.cfg.");
            System.exit(1);
        }
    }

    /**
     * Gets or creates a database connection
     * @return Active database connection
     */
    public static Connection getConnection() {
        loadCredentials();

        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException e) {
                // Connection is closed, create a new one
            }
        }

        String connectionUrl =
                "jdbc:sqlserver://" + SERVER_URL + ";"
                + "database=" + DATABASE_NAME + ";"
                + "user=" + username + ";"
                + "password=" + password + ";"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";

        try {
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("[OK] Connected to database successfully.");
            return connection;
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to connect to database.");
            System.out.println("Details: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    /**
     * Closes the database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[OK] Database connection closed.");
            } catch (SQLException e) {
                System.out.println("WARNING: Error closing database connection.");
            }
        }
    }
}
