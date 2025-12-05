import java.sql.*;

public class DataDiagnostic {
    public static void main(String[] args) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();

        System.out.println("=== DATA DIAGNOSTIC ===\n");

        // Check if PRODUCTS.category_id is populated
        ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) as total, COUNT(category_id) as with_cat FROM PRODUCTS");
        if (rs1.next()) {
            System.out.println("PRODUCTS: Total=" + rs1.getInt("total") + ", With category_id=" + rs1.getInt("with_cat"));
        }
        rs1.close();

        // Check customers with multiple orders
        ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) as count FROM (SELECT customer_id FROM ORDERS GROUP BY customer_id HAVING COUNT(*) > 1) t");
        if (rs2.next()) {
            System.out.println("Customers with multiple orders: " + rs2.getInt("count"));
        }
        rs2.close();

        // Check order status
        ResultSet rs3 = stmt.executeQuery("SELECT order_status, COUNT(*) as count FROM ORDERS GROUP BY order_status");
        System.out.println("\nOrder statuses:");
        while (rs3.next()) {
            System.out.println("  " + rs3.getString("order_status") + ": " + rs3.getInt("count"));
        }
        rs3.close();

        // Check products without orders
        ResultSet rs4 = stmt.executeQuery("SELECT COUNT(*) as count FROM PRODUCTS p WHERE NOT EXISTS (SELECT 1 FROM ORDER_ITEMS oi WHERE oi.product_id = p.product_id)");
        if (rs4.next()) {
            System.out.println("\nProducts never ordered: " + rs4.getInt("count"));
        }
        rs4.close();

        // Check category join
        ResultSet rs5 = stmt.executeQuery("SELECT COUNT(*) as count FROM PRODUCTS p JOIN CATEGORIES c ON p.category_id = c.category_id");
        if (rs5.next()) {
            System.out.println("Products with category join: " + rs5.getInt("count"));
        }
        rs5.close();

        stmt.close();
    }
}
