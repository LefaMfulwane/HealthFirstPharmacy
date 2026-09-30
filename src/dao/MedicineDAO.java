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
public class MedicineDAO {
    public List<Medicine> getAllMedicines() {
        List<Medicine> medicineList = new ArrayList<>();
        String sql = "SELECT * FROM medicines ORDER BY name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                medicineList.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicineList;
    }

    public List<Medicine> searchMedicines(String keyword) {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE name LIKE ? OR company LIKE ? ORDER BY name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToMedicine(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addMedicine(Medicine m) {
        String sql = "INSERT INTO medicines (name, company, medicine_type, price, quantity_in_stock, reorder_level, expiry_date, supplier_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getCompany());
            ps.setString(3, m.getMedicineType());
            ps.setDouble(4, m.getPrice());
            ps.setInt(5, m.getQuantityInStock());
            ps.setInt(6, m.getReorderLevel());
            ps.setDate(7, m.getExpiryDate());
            if (m.getSupplierId() > 0) ps.setInt(8, m.getSupplierId()); else ps.setNull(8, Types.INTEGER);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMedicine(Medicine m) {
        String sql = "UPDATE medicines SET name=?, company=?, medicine_type=?, price=?, quantity_in_stock=?, reorder_level=?, expiry_date=?, supplier_id=? WHERE medicine_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getCompany());
            ps.setString(3, m.getMedicineType());
            ps.setDouble(4, m.getPrice());
            ps.setInt(5, m.getQuantityInStock());
            ps.setInt(6, m.getReorderLevel());
            ps.setDate(7, m.getExpiryDate());
            if (m.getSupplierId() > 0) ps.setInt(8, m.getSupplierId()); else ps.setNull(8, Types.INTEGER);
            ps.setInt(9, m.getMedicineId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMedicine(int medicineId) {
        String sql = "DELETE FROM medicines WHERE medicine_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException {
        return new Medicine(
                rs.getInt("medicine_id"),
                rs.getString("name"),
                rs.getString("company"),
                rs.getString("medicine_type"),
                rs.getDouble("price"),
                rs.getInt("quantity_in_stock"),
                rs.getInt("reorder_level"),
                rs.getDate("expiry_date"),
                rs.getInt("supplier_id")
        );
    }
}
    

        
        

    




//replaced code with updated one
/**
 * 
 public List<Medicine> getAllMedicines() {
        List<Medicine> medicineList = new ArrayList<>();
        String sql = "SELECT * FROM medicines ORDER BY name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                medicineList.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicineList;
    }

    public List<Medicine> searchMedicines(String keyword) {
        List<Medicine> medicineList = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE name LIKE ? OR company LIKE ? ORDER BY name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    medicineList.add(mapResultSetToMedicine(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicineList;
    }

    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException {
        return new Medicine(
                rs.getInt("medicine_id"),
                rs.getString("name"),
                rs.getString("company"),
                rs.getString("medicine_type"),
                rs.getDouble("price"),
                rs.getInt("quantity_in_stock"),
                rs.getInt("reorder_level"),
                rs.getDate("expiry_date"),
                rs.getInt("supplier_id")
        );
    }
 */

