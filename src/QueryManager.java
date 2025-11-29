/**
 * Manages all SQL queries for the Brazilian E-Commerce Database Analyzer.
 * All queries use prepared statements for security.
 * Queries are organized by category matching the menu structure.
 */
public class QueryManager {

    // ==================== MARKET AND SALES ANALYSIS ====================

    public static final String HIGH_VALUE_CUSTOMER_STATES =
            "SELECT " +
            "    g.geolocation_state AS state, " +
            "    s.state_name, " +
            "    COUNT(DISTINCT o.order_id) AS total_orders, " +
            "    AVG(oi.price + oi.freight_value) AS avg_order_value " +
            "FROM CUSTOMERS c " +
            "JOIN GEOLOCATION g ON c.customer_zip_code_prefix = g.zip_code_prefix " +
            "JOIN STATES s ON g.geolocation_state = s.state_code " +
            "JOIN ORDERS o ON c.customer_id = o.customer_id " +
            "JOIN ORDER_ITEMS oi ON o.order_id = oi.order_id " +
            "GROUP BY g.geolocation_state, s.state_name " +
            "ORDER BY avg_order_value DESC";

    public static final String TOP_SELLING_CATEGORIES =
            "SELECT " +
            "    c.category_name_english, " +
            "    COUNT(DISTINCT oi.order_id) AS orders_count, " +
            "    COUNT(DISTINCT oi.product_id) AS products_count, " +
            "    SUM(oi.price) AS total_revenue, " +
            "    AVG(oi.price) AS avg_product_price " +
            "FROM CATEGORIES c " +
            "JOIN PRODUCTS p ON c.category_id = p.category_id " +
            "JOIN ORDER_ITEMS oi ON p.product_id = oi.product_id " +
            "GROUP BY c.category_id, c.category_name_english " +
            "ORDER BY total_revenue DESC";

    public static final String STATES_WITH_CUSTOMERS_NO_ORDERS =
            "SELECT " +
            "    s.state_code, " +
            "    s.state_name, " +
            "    COUNT(DISTINCT c.customer_id) AS registered_customers " +
            "FROM STATES s " +
            "JOIN GEOLOCATION g ON s.state_code = g.geolocation_state " +
            "JOIN CUSTOMERS c ON g.zip_code_prefix = c.customer_zip_code_prefix " +
            "WHERE NOT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM ORDERS o " +
            "    WHERE o.customer_id = c.customer_id " +
            ") " +
            "GROUP BY s.state_code, s.state_name " +
            "HAVING COUNT(DISTINCT c.customer_id) > 0 " +
            "ORDER BY registered_customers DESC";

    public static final String HIGHEST_SALES_GEOLOCATION =
            "SELECT " +
            "    g.zip_code_prefix, " +
            "    g.geolocation_city, " +
            "    g.geolocation_state, " +
            "    g.geolocation_lat, " +
            "    g.geolocation_lng, " +
            "    COUNT(DISTINCT o.order_id) AS total_orders, " +
            "    COUNT(DISTINCT c.customer_id) AS unique_customers, " +
            "    SUM(oi.price + oi.freight_value) AS total_revenue " +
            "FROM GEOLOCATION g " +
            "JOIN CUSTOMERS c ON g.zip_code_prefix = c.customer_zip_code_prefix " +
            "JOIN ORDERS o ON c.customer_id = o.customer_id " +
            "JOIN ORDER_ITEMS oi ON o.order_id = oi.order_id " +
            "GROUP BY g.zip_code_prefix, g.geolocation_city, g.geolocation_state, " +
            "         g.geolocation_lat, g.geolocation_lng " +
            "ORDER BY total_orders DESC";

    // ==================== SELLER PERFORMANCE ====================

    public static final String SELLER_SUCCESS_RATE =
            "SELECT " +
            "    s.seller_id, " +
            "    g.geolocation_city AS seller_city, " +
            "    g.geolocation_state AS seller_state, " +
            "    COUNT(DISTINCT oi.order_id) AS orders_fulfilled, " +
            "    COUNT(DISTINCT r.review_id) AS reviews_received, " +
            "    AVG(r.review_score) AS avg_review_score, " +
            "    ROUND(COUNT(DISTINCT r.review_id) * 100.0 / " +
            "          COUNT(DISTINCT oi.order_id), 2) AS review_rate_percentage " +
            "FROM SELLERS s " +
            "JOIN GEOLOCATION g ON s.seller_zip_code_prefix = g.zip_code_prefix " +
            "JOIN ORDER_ITEMS oi ON s.seller_id = oi.seller_id " +
            "LEFT JOIN ORDER_REVIEWS r ON oi.order_id = r.order_id " +
            "GROUP BY s.seller_id, g.geolocation_city, g.geolocation_state " +
            "HAVING COUNT(DISTINCT r.review_id) > 0 " +
            "ORDER BY avg_review_score DESC, orders_fulfilled DESC";

    public static final String UNUSED_PRODUCT_CATALOG =
            "SELECT " +
            "    p.product_id, " +
            "    c.category_name_english AS category, " +
            "    p.product_weight_g, " +
            "    p.product_length_cm, " +
            "    p.product_height_cm, " +
            "    p.product_width_cm, " +
            "    p.product_photos_qty " +
            "FROM PRODUCTS p " +
            "JOIN CATEGORIES c ON p.category_id = c.category_id " +
            "WHERE NOT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM ORDER_ITEMS oi " +
            "    WHERE oi.product_id = p.product_id " +
            ") " +
            "ORDER BY c.category_name_english, p.product_id";

    public static final String STATES_WITH_CUSTOMERS_NO_SELLERS =
            "SELECT " +
            "    s.state_code, " +
            "    s.state_name, " +
            "    COUNT(DISTINCT c.customer_id) AS customer_count " +
            "FROM STATES s " +
            "JOIN GEOLOCATION g ON s.state_code = g.geolocation_state " +
            "JOIN CUSTOMERS c ON g.zip_code_prefix = c.customer_zip_code_prefix " +
            "WHERE NOT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM SELLERS sel " +
            "    JOIN GEOLOCATION g2 ON sel.seller_zip_code_prefix = g2.zip_code_prefix " +
            "    WHERE g2.geolocation_state = s.state_code " +
            ") " +
            "GROUP BY s.state_code, s.state_name " +
            "ORDER BY customer_count DESC";

    public static final String SINGLE_PRODUCT_SELLERS =
            "SELECT " +
            "    COUNT(*) AS single_product_seller_count " +
            "FROM ( " +
            "    SELECT " +
            "        oi.seller_id, " +
            "        COUNT(DISTINCT oi.product_id) AS unique_products " +
            "    FROM ORDER_ITEMS oi " +
            "    GROUP BY oi.seller_id " +
            "    HAVING COUNT(DISTINCT oi.product_id) = 1 " +
            ") AS single_product_sellers";

    // ==================== ORDER AND REVIEW QUALITY ====================

    public static final String ORDER_REVIEW_RATE =
            "SELECT " +
            "    COUNT(DISTINCT o.order_id) AS total_orders, " +
            "    COUNT(DISTINCT r.order_id) AS reviewed_orders, " +
            "    ROUND(COUNT(DISTINCT r.order_id) * 100.0 / " +
            "          COUNT(DISTINCT o.order_id), 2) AS review_rate_percentage " +
            "FROM ORDERS o " +
            "LEFT JOIN ORDER_REVIEWS r ON o.order_id = r.order_id";

    public static final String WORST_RATED_CATEGORY =
            "SELECT " +
            "    c.category_name_english AS category, " +
            "    COUNT(DISTINCT p.product_id) AS products_in_category, " +
            "    COUNT(DISTINCT r.review_id) AS total_reviews, " +
            "    AVG(r.review_score) AS avg_review_score, " +
            "    SUM(CASE WHEN r.review_score <= 2 THEN 1 ELSE 0 END) AS negative_reviews, " +
            "    SUM(CASE WHEN r.review_score >= 4 THEN 1 ELSE 0 END) AS positive_reviews " +
            "FROM CATEGORIES c " +
            "JOIN PRODUCTS p ON c.category_id = p.category_id " +
            "JOIN ORDER_ITEMS oi ON p.product_id = oi.product_id " +
            "JOIN ORDER_REVIEWS r ON oi.order_id = r.order_id " +
            "GROUP BY c.category_id, c.category_name_english " +
            "HAVING COUNT(DISTINCT r.review_id) >= 10 " +
            "ORDER BY avg_review_score ASC";

    public static final String ORDERS_PAID_IN_FULL =
            "SELECT " +
            "    COUNT(*) AS single_payment_orders " +
            "FROM ( " +
            "    SELECT " +
            "        order_id, " +
            "        COUNT(*) AS payment_count " +
            "    FROM ORDER_PAYMENTS " +
            "    GROUP BY order_id " +
            "    HAVING COUNT(*) = 1 " +
            ") AS single_payments";

    public static final String REVIEW_SCORE_EXTREMES =
            "SELECT " +
            "    SUM(CASE WHEN review_score = 1 THEN 1 ELSE 0 END) AS one_star_reviews, " +
            "    SUM(CASE WHEN review_score = 5 THEN 1 ELSE 0 END) AS five_star_reviews, " +
            "    ROUND(SUM(CASE WHEN review_score = 1 THEN 1 ELSE 0 END) * 100.0 / " +
            "          COUNT(*), 2) AS one_star_percentage, " +
            "    ROUND(SUM(CASE WHEN review_score = 5 THEN 1 ELSE 0 END) * 100.0 / " +
            "          COUNT(*), 2) AS five_star_percentage, " +
            "    COUNT(*) AS total_reviews " +
            "FROM ORDER_REVIEWS";

    // ==================== PAYMENT AND TRANSACTION ANALYSIS ====================

    public static final String MOST_COMMON_PAYMENT_TYPE_BY_STATE =
            "WITH state_payment_counts AS ( " +
            "    SELECT " +
            "        g.geolocation_state, " +
            "        s.state_name, " +
            "        op.payment_type, " +
            "        COUNT(*) AS payment_count " +
            "    FROM CUSTOMERS c " +
            "    JOIN GEOLOCATION g ON c.customer_zip_code_prefix = g.zip_code_prefix " +
            "    JOIN STATES s ON g.geolocation_state = s.state_code " +
            "    JOIN ORDERS o ON c.customer_id = o.customer_id " +
            "    JOIN ORDER_PAYMENTS op ON o.order_id = op.order_id " +
            "    GROUP BY g.geolocation_state, s.state_name, op.payment_type " +
            "), " +
            "ranked_payments AS ( " +
            "    SELECT " +
            "        geolocation_state, " +
            "        state_name, " +
            "        payment_type, " +
            "        payment_count, " +
            "        ROW_NUMBER() OVER (PARTITION BY geolocation_state " +
            "                          ORDER BY payment_count DESC) AS rank " +
            "    FROM state_payment_counts " +
            ") " +
            "SELECT " +
            "    geolocation_state, " +
            "    state_name, " +
            "    payment_type AS most_common_payment_type, " +
            "    payment_count " +
            "FROM ranked_payments " +
            "WHERE rank = 1 " +
            "ORDER BY geolocation_state";

    public static final String AVG_INSTALLMENTS_BY_PAYMENT_TYPE =
            "SELECT " +
            "    payment_type, " +
            "    COUNT(*) AS total_payments, " +
            "    AVG(payment_installments) AS avg_installments, " +
            "    MIN(payment_installments) AS min_installments, " +
            "    MAX(payment_installments) AS max_installments, " +
            "    ROUND(AVG(payment_value), 2) AS avg_payment_value " +
            "FROM ORDER_PAYMENTS " +
            "GROUP BY payment_type " +
            "ORDER BY avg_installments DESC";

    // ==================== CUSTOMER BEHAVIOR AND LOYALTY ====================

    public static final String REPEAT_PURCHASE_CUSTOMERS =
            "SELECT " +
            "    c.customer_id, " +
            "    c.customer_unique_id, " +
            "    g.geolocation_city, " +
            "    g.geolocation_state, " +
            "    COUNT(DISTINCT o.order_id) AS total_orders, " +
            "    MIN(o.order_purchase_timestamp) AS first_order_date, " +
            "    MAX(o.order_purchase_timestamp) AS last_order_date, " +
            "    SUM(oi.price + oi.freight_value) AS total_lifetime_value " +
            "FROM CUSTOMERS c " +
            "JOIN GEOLOCATION g ON c.customer_zip_code_prefix = g.zip_code_prefix " +
            "JOIN ORDERS o ON c.customer_id = o.customer_id " +
            "JOIN ORDER_ITEMS oi ON o.order_id = oi.order_id " +
            "GROUP BY c.customer_id, c.customer_unique_id, " +
            "         g.geolocation_city, g.geolocation_state " +
            "HAVING COUNT(DISTINCT o.order_id) > 1 " +
            "ORDER BY total_orders DESC, total_lifetime_value DESC";

    public static final String AVG_TIME_BETWEEN_ORDERS =
            "WITH customer_orders AS ( " +
            "    SELECT " +
            "        customer_id, " +
            "        order_id, " +
            "        order_purchase_timestamp, " +
            "        LAG(order_purchase_timestamp) OVER " +
            "            (PARTITION BY customer_id ORDER BY order_purchase_timestamp) " +
            "            AS previous_order_date " +
            "    FROM ORDERS " +
            "), " +
            "order_gaps AS ( " +
            "    SELECT " +
            "        customer_id, " +
            "        DATEDIFF(day, previous_order_date, order_purchase_timestamp) " +
            "            AS days_between_orders " +
            "    FROM customer_orders " +
            "    WHERE previous_order_date IS NOT NULL " +
            ") " +
            "SELECT " +
            "    COUNT(DISTINCT customer_id) AS returning_customers, " +
            "    AVG(days_between_orders) AS avg_days_between_orders, " +
            "    MIN(days_between_orders) AS min_days_between_orders, " +
            "    MAX(days_between_orders) AS max_days_between_orders " +
            "FROM order_gaps";

    // ==================== ADDITIONAL QUERIES ====================

    public static final String DELIVERY_PERFORMANCE_BY_STATE =
            "SELECT " +
            "    g.geolocation_state, " +
            "    s.state_name, " +
            "    COUNT(DISTINCT o.order_id) AS total_delivered_orders, " +
            "    AVG(DATEDIFF(day, o.order_purchase_timestamp, " +
            "                 o.order_delivered_customer_date)) AS avg_actual_delivery_days, " +
            "    AVG(DATEDIFF(day, o.order_purchase_timestamp, " +
            "                 o.order_estimated_delivery_date)) AS avg_estimated_delivery_days, " +
            "    AVG(DATEDIFF(day, o.order_estimated_delivery_date, " +
            "                 o.order_delivered_customer_date)) AS avg_delay_days, " +
            "    SUM(CASE WHEN o.order_delivered_customer_date > " +
            "                  o.order_estimated_delivery_date THEN 1 ELSE 0 END) AS late_deliveries, " +
            "    ROUND(SUM(CASE WHEN o.order_delivered_customer_date > " +
            "                       o.order_estimated_delivery_date THEN 1 ELSE 0 END) * 100.0 / " +
            "          COUNT(*), 2) AS late_delivery_percentage " +
            "FROM ORDERS o " +
            "JOIN CUSTOMERS c ON o.customer_id = c.customer_id " +
            "JOIN GEOLOCATION g ON c.customer_zip_code_prefix = g.zip_code_prefix " +
            "JOIN STATES s ON g.geolocation_state = s.state_code " +
            "WHERE o.order_delivered_customer_date IS NOT NULL " +
            "GROUP BY g.geolocation_state, s.state_name " +
            "ORDER BY avg_delay_days DESC";

    public static final String CATEGORY_PERFORMANCE_BY_QUARTER =
            "SELECT " +
            "    c.category_name_english, " +
            "    YEAR(o.order_purchase_timestamp) AS order_year, " +
            "    DATEPART(QUARTER, o.order_purchase_timestamp) AS order_quarter, " +
            "    COUNT(DISTINCT oi.order_id) AS orders, " +
            "    SUM(oi.price) AS revenue, " +
            "    AVG(oi.price) AS avg_item_price " +
            "FROM CATEGORIES c " +
            "JOIN PRODUCTS p ON c.category_id = p.category_id " +
            "JOIN ORDER_ITEMS oi ON p.product_id = oi.product_id " +
            "JOIN ORDERS o ON oi.order_id = o.order_id " +
            "WHERE o.order_status = 'delivered' " +
            "GROUP BY c.category_name_english, YEAR(o.order_purchase_timestamp), " +
            "         DATEPART(QUARTER, o.order_purchase_timestamp) " +
            "ORDER BY c.category_name_english, order_year, order_quarter";

    public static final String REVENUE_BY_STATE_AND_YEAR =
            "SELECT " +
            "    COUNT(DISTINCT T1.order_id) AS Total_Orders, " +
            "    SUM(T2.payment_value) AS Total_Revenue " +
            "FROM ORDERS T1 " +
            "JOIN ORDER_PAYMENTS T2 ON T1.order_id = T2.order_id " +
            "JOIN CUSTOMERS T3 ON T1.customer_id = T3.customer_id " +
            "WHERE T3.customer_state = ? " +
            "  AND T1.order_purchase_timestamp LIKE ? " +
            "GROUP BY T3.customer_state";

    public static final String SELLER_INVENTORY_CHECK =
            "SELECT DISTINCT " +
            "    T1.seller_id " +
            "FROM ORDER_ITEMS T1 " +
            "JOIN PRODUCTS T2 ON T1.product_id = T2.product_id " +
            "WHERE T2.product_description_length IS NOT NULL " +
            "  AND T2.product_description_length LIKE ?";
}
