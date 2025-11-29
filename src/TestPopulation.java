/**
 * Quick test to populate the database
 */
public class TestPopulation {
    public static void main(String[] args) {
        System.out.println("=== Testing Database Population ===\n");

        try {
            // Clear existing data first
            System.out.println("Step 1: Clearing existing data...");
            CSVDataLoader.clearAllData();

            System.out.println("\nStep 2: Populating all tables...");
            CSVDataLoader.populateAllTables();

            System.out.println("\n=== TEST COMPLETE ===");

        } catch (Exception e) {
            System.out.println("\nTEST FAILED!");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
