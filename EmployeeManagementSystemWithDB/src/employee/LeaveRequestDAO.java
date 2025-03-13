package employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LeaveRequestDAO {
	
	static Connection connect = null;
	static Statement stat = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;
	
	 static void applyLeave(int empId, String leaveDetails) {
	    String query = "INSERT INTO Leave_Request (emp_id, date, reason, status) VALUES (?, CURDATE(), ?, 'Pending')";
	    
	    try {
	        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
	        pstmt = connect.prepareStatement(query);
	        pstmt.setInt(1, empId);
System.out.println("Employee id who applied leave: "+empId);
	        pstmt.setString(2, leaveDetails);
	        
	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Leave request submitted successfully.");
	        } else {
	            System.out.println("Failed to submit leave request.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error applying leave: " + e.getMessage());
	    }
	}
	 static void approveLeave(DataManager dataManager, int empId, boolean approval) {
	        String query = "UPDATE Leave_Request SET approval = ?,status = ? WHERE emp_id = ? AND status = 'Pending' AND approval IS NULL LIMIT 1";

	        try {
	            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
	            pstmt = connect.prepareStatement(query);
	            pstmt.setBoolean(1, approval);  
	            pstmt.setString(2, approval ? "Approved" : "Rejected");
	            pstmt.setInt(3, empId);

	            int rowsUpdated = pstmt.executeUpdate();
	            if (rowsUpdated > 0) {
	                System.out.println("Leave request updated successfully.");
	            } else {
	                System.out.println("No pending leave request found for the given employee.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error updating leave request: " + e.getMessage());
	        }
	    }

	public static void checkLeaveStatus(int empId) {
	    String query = "SELECT l_id, date, reason, status FROM Leave_Request WHERE emp_id = ?";
	    
	    try {
	        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
	        pstmt = connect.prepareStatement(query);
	        pstmt.setInt(1, empId);
	        rs = pstmt.executeQuery();

	        System.out.println("\n----- Leave Status -----");
	        System.out.printf("%-5s %-15s %-20s %-10s\n", "LID", "Date", "Reason", "Status");
	        System.out.println("--------------------------------------------");
	        
	        boolean hasLeaves = false;
	        while (rs.next()) {
	            hasLeaves = true;
	            System.out.printf("%-5d %-15s %-20s %-10s\n",
	                    rs.getInt("l_id"), rs.getDate("date"), rs.getString("reason"), rs.getString("status"));
	        }

	        if (!hasLeaves) {
	            System.out.println("No leave records found.");
	        }
	        System.out.println("--------------------------------------------");
	        
	    } catch (SQLException e) {
	        System.out.println("Error fetching leave status: " + e.getMessage());
	    }
	}

}
