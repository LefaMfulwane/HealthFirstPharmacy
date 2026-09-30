package dao;

import db.DatabaseConnection;
import model.CartItem;

import java.sql.*;
import java.util.List;

/**
 *
 * @author admin
 */
public class SaleDAO {

    public int processSale(int userId, List<CartItem> cartItems, double totalAmount) {
        String insertSaleSQL = "INSERT INTO sales (total_amount, user_id) VALUES (?, ?)";
        String insertItemSQL = "INSERT INTO sale_items (sale_id, medicine_id, quantity_sold, price_at_sale) VALUES (?, ?, ?, ?)";
        String updateStockSQL = "UPDATE medicines SET quantity_in_stock = quantity_in_stock - ? WHERE medicine_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Insert Sales Header
            int saleId = -1;
            try (PreparedStatement psSale = conn.prepareStatement(insertSaleSQL, Statement.RETURN_GENERATED_KEYS)) {
                psSale.setDouble(1, totalAmount);
                psSale.setInt(2, userId);
                psSale.executeUpdate();

                try (ResultSet rs = psSale.getGeneratedKeys()) {
                    if (rs.next()) {
                        saleId = rs.getInt(1);
                    }
                }
            }

            if (saleId == -1) {
                conn.rollback();
                return -1;
            }

            // 2. Insert Sale Items and Deduct Inventory Stock
            try (PreparedStatement psItem = conn.prepareStatement(insertItemSQL);
                 PreparedStatement psStock = conn.prepareStatement(updateStockSQL)) {

                for (CartItem item : cartItems) {
                    // Record sale line item
                    psItem.setInt(1, saleId);
                    psItem.setInt(2, item.getMedicineId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getUnitPrice());
                    psItem.executeUpdate();

                    // Update stock level in medicines table
                    psStock.setInt(1, item.getQuantity());
                    psStock.setInt(2, item.getMedicineId());
                    psStock.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction
            return saleId;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}