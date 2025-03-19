package employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DatabaseConnection {
	static  Connection connect = null;
    static Statement stat = null;
    static PreparedStatement stmt = null;
    static ResultSet resultSet = null;
    
    static Employee authenticate(String username, String password) {

        String query = "SELECT * FROM Role WHERE username = ? AND password = ?";

        try {
            connect= DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
            stmt = connect.prepareStatement(query);

            stmt.setString(1, username);
            stmt.setString(2, password);
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String role = resultSet.getString("role");

                return new Employee(userId, username, role,"", 0, "", "", 0.0, 0);
            }

        } catch (SQLException e) {
            System.out.println("Authentication error: " + e.getMessage());
        } 

        return null;
    }
    static void insertLoginDetails(String username, String password, String role) {
        String query = "INSERT INTO Role (username, password, role) VALUES (?, ?, ?)";
        System.out.println(username + password+role);
        
        try {
         
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");

            System.out.println("Connected to database successfully.");

            stmt = connect.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int rowsInserted = stmt.executeUpdate();
            
            if (rowsInserted > 0) {
                System.out.println("✅ Login details inserted successfully.");
            } else {
                System.out.println("❌ No rows inserted.");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error inserting login details: " + e.getMessage());
        } 
    }
}
