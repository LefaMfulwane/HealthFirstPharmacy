package dao;

import db.DatabaseConnection;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author admin
 */
public class USerDAO {
    
    public User authenticate(String username, String password) {
        String sql = "SELECT user_id, username, role, full_name FROM users WHERE username = ? AND password = ?"; 
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username); 
            ps.setString(2, password); 
            
            try (ResultSet rs = ps.executeQuery()) { 
                if (rs.next())
                { return new User(
                        rs.getInt("user_id"), 
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("full_name")
                    );
                } 
            } 
        } catch (SQLException e) {
            e.printStackTrace(); 
        } 
        return null;
    }
    
    public List<User> getAllUsers(){
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, username, role, full_name FROM users ORDER BY user_id ASC";
    
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new User( 
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("full_name")
                )); 
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } 
    public boolean addUser(String username, String password, String role, String fullName) {
        String sql = "INSERT INTO users (username, password, role, full_name) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) { 
            
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.setString(4, fullName);
            
            return ps.executeUpdate()> 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
    } 
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0; 
        } catch (SQLException e) { 
            e.printStackTrace();
            return false;
        }
    } 
}
    
    
    
    
   

