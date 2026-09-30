package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author admin
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/healthfirst_db?useSSL=false&allowPublicKeyRetrieval=true"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "L3f@MySQL!1#"; // MYSQL PASSWORD
    
    public static Connection getConnection() throws SQLException { 
    try { 
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
    System.err.println("MySQL JDBC Driver not found!"); 
    e.printStackTrace(); 
    }
    
    return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
    
    
    

