/**
 * Brazilian E-Commerce Database Analyzer
 * COMP 3380 - Database Project
 *
 * Main application class that launches the menu-driven interface for
 * querying and analyzing the Brazilian E-Commerce database.
 *
 * @author M Tausif Tajwar Bhuiyan, Luke Rodriguez, Dinh Nhat Nguyen
 * @version Stage 6 - November 2025
 */
public class BrazilianECommerceAnalyzer {

    public static void main(String[] args) {
        System.out.println("\n===============================================================================");
        System.out.println("                BRAZILIAN E-COMMERCE DATABASE ANALYZER");
        System.out.println("                      COMP 3380 - Database Project");
        System.out.println("===============================================================================\n");

        System.out.println("Initializing application...");

        try {
            // Test database connection
            DatabaseConnection.getConnection();
            System.out.println("Application ready.\n");

            // Launch menu interface
            MenuInterface menu = new MenuInterface();
            menu.showMainMenu();

        } catch (Exception e) {
            System.out.println("\nFATAL ERROR: Application failed to start.");
            System.out.println("Details: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
