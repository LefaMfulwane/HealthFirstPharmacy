package dao;

import db.DatabaseConnection;
import model.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class ReportDAO {

    // Expiry Report: Fetch medicines expiring within 1 month
    public List<Medicine> getExpiringMedicines() {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE expiry_date <= DATE_ADD(CURDATE(), INTERVAL 1 MONTH) ORDER BY expiry_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Medicine(
                        rs.getInt("medicine_id"),
                        rs.getString("name"),
                        rs.getString("company"),
                        rs.getString("medicine_type"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getInt("reorder_level"),
                        rs.getDate("expiry_date"),
                        rs.getInt("supplier_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Sales Report: Summary of transactions
    public List<Object[]> getSalesReport() {
        List<Object[]> report = new ArrayList<>();
        String sql = "SELECT s.sale_id, s.sale_date, u.full_name, s.total_amount " +
                "FROM sales s LEFT JOIN users u ON s.user_id = u.user_id " +
                "ORDER BY s.sale_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                report.add(new Object[]{
                    rs.getInt("sale_id"),
                    rs.getTimestamp("sale_date"),
                    rs.getString("full_name") != null ? rs.getString("full_name") : "N/A",
                    String.format("%.2f", rs.getDouble("total_amount"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }
}




/**
package dao;

import db.DatabaseConnection; 
import model.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


 *
 * @author admin

public class ReportDAO {
    
    // Expiry Report: Fetch medicines expiring within 1 month
    public List <Medicine> getExpiringMedicines(){
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE expiry_date &lt;= DATE_ADD(CURDATE(), INTERVAL 1 MONTH) ORDER BY expiry_date ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Medicine(
                        rs.getInt("medicine_id"),
                        rs.getString("name")
                        , rs.getString("company"),
                        rs.getString("medicine_type")
                        , rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getInt("reorder_level"),
                        rs.getDate("expiry_date"),
                        rs.getInt("supplier_id")
                )); 
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return list;
    }
    // Sales Report: Summary of transactions
    public List<Object[]> getsalesReport(){
        List<Object[]>report = new ArrayList<>();
        String sql = "SELECT s.sale_id, s.sale_date, u.full_name, s.total_amount " +
                "FROM sales s LEFT JOIN users u ON s.user_id = u.user_id " +
                "ORDER BY s.sale_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery(sql)) { 
            while (rs.next()) {
                report.add(new Object[]{ 
                    rs.getInt("sale_id"),
                    rs.getTimestamp("sale_date"),
                    rs.getString("full_name") != null ? rs.getString("full_name") : "N/A",
                String.format("%.2f", rs.getDouble("total_amount"))
            }); 
        } 
    } catch (SQLException e) {
        e.printStackTrace();
    } 
        return report; 
    }
}
        
 */
        
        
        
  
   
    
    
    
    
    
