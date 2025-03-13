package employee;

import java.sql.*;
import java.util.ArrayList;

public class EmployeeDAO {
	static Connection connect = null;
	static Statement stat = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;
	static ArrayList<Employee> employees = new ArrayList<>();

	ArrayList<Employee> loadEmployees() throws SQLException {
		employees.clear();
		String query = "SELECT emp_id, name, email, phone, role, password,salary, manager_id, team_id FROM Employee";
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
		try {
			pstmt = connect.prepareStatement(query);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				employees.add(new Employee(rs.getInt("emp_id"), rs.getString("name"), rs.getString("role"),
						rs.getString("password"), rs.getInt("manager_id"), rs.getString("email"), rs.getString("phone"),
						rs.getDouble("salary"), rs.getInt("team_id")));
			}
		} catch (SQLException e) {
			System.out.println("Error loading employees: " + e.getMessage());
		}
		return employees;
	}

	static void saveEmployee(Employee emp) throws SQLException {
		String query = "INSERT INTO Employee (name, email, phone, role, password, salary, manager_id, team_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");

		try {
			pstmt = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, emp.getName());
			pstmt.setString(2, emp.getEmail());
			pstmt.setString(3, emp.getPhone());
			pstmt.setString(4, emp.getRole());
			pstmt.setString(5, SecurityUtils.hashPassword(emp.getPassword()));
			pstmt.setDouble(6, emp.getSalary());
			pstmt.setInt(7, emp.getManagerId());
			pstmt.setInt(8, emp.getTeamId());

			pstmt.executeUpdate();
			System.out.println("Employee saved successfully.");
		} catch (SQLException e) {
			System.out.println("Error saving employee: " + e.getMessage());
		}
	}

	static Employee findEmployee(String name) throws SQLException {
		String query = "SELECT * FROM Employee WHERE name = ?";
		Employee employee = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root",
					"03Sep2006");
			pstmt = connect.prepareStatement(query);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				employee = new Employee(rs.getInt("emp_id"), rs.getString("name"), rs.getString("role"),
						rs.getString("password"), rs.getInt("manager_id"), rs.getString("email"), rs.getString("phone"),
						rs.getDouble("salary"), rs.getInt("team_id"));
				 System.out.println("Logged in Employee ID: " + employee.getId());
			}
		} catch (SQLException e) {
			System.out.println("Error during login: " + e.getMessage());
		}
		return employee;
	}

	void viewAllEmployees(ArrayList<Employee> employees) throws SQLException {
		employees = loadEmployees();
		System.out.println(
				"\n================================================== Employee List ==================================================");
		System.out.printf("%-5s %-20s %-15s %-10s %-25s %-15s %-10s %-8s\n", "ID", "Name", "Role", "ManagerID", "Email",
				"Phone", "Salary", "TeamID");
		System.out.println(
				"-------------------------------------------------------------------------------------------------------------------");

		if (employees.isEmpty()) {
			System.out.println("No employees found.");
		} else {
			for (Employee emp : employees) {
				System.out.printf("%-5d %-20s %-15s %-10s %-25s %-15s %-10.2f %-8s\n", emp.getId(), emp.getName(),
						emp.getRole(), (emp.getManagerId() == 0 ? "N/A" : emp.getManagerId()), emp.getEmail(),
						(emp.getPhone() == null ? "N/A" : emp.getPhone()), emp.getSalary(),
						(emp.getTeamId() == 0 ? "N/A" : emp.getTeamId()));
			}
		}

		System.out.println(
				"===================================================================================================================\n");
	}

	void updateEmployee(int id, String newRole, String newEmail, String newPhone, double newSalary)
			throws SQLException {
		String query1 = "UPDATE Employee SET role = ?, email = ?, phone = ?, salary = ? WHERE emp_id = ?";
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
		try {
			pstmt = connect.prepareStatement(query1);
			pstmt.setString(1, newRole);
			pstmt.setString(2, newEmail);
			pstmt.setString(3, newPhone);
			pstmt.setDouble(4, newSalary);
			pstmt.setInt(5, id);

			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Employee updated successfully.");
			} else {
				System.out.println("Employee not found.");
			}

		} catch (SQLException e) {
			System.out.println("Error updating employee: " + e.getMessage());
		}
	}
	boolean deleteEmployee(int id) {
	    String query = "DELETE FROM Employee WHERE emp_id = ?";

	    try  {
	    	pstmt = connect.prepareStatement(query);
	        pstmt.setInt(1, id);
	        int affectedRows = pstmt.executeUpdate();

	        if (affectedRows > 0) {
	            System.out.println("Employee deleted successfully.");
	            return true;
	        } else {
	            System.out.println("Error: Employee with ID " + id + " not found.");
	            return false;
	        }
	    } catch (SQLException e) {
	        System.out.println("Error deleting employee: " + e.getMessage());
	        return false;
	    }
	}
	public void viewHierarchy(int managerId) {
	    String query = "SELECT emp_id, name, role, email, phone, salary FROM Employee WHERE manager_id = ?";

	    System.out.println("\n============================================== Hierarchy View ===============================================");
	    System.out.println("Manager ID: " + managerId);
	    System.out.printf("%-5s %-20s %-15s %-25s %-15s %-10s\n", 
	                      "ID", "Name", "Role", "Email", "Phone", "Salary");
	    System.out.println("--------------------------------------------------------------------------------------------------------------");

	    try {
	    	pstmt = connect.prepareStatement(query);
	        pstmt.setInt(1, managerId);
	        ResultSet rs = pstmt.executeQuery();

	        boolean hasEmployees = false;
	        while (rs.next()) {
	            hasEmployees = true;
	            System.out.printf("%-5d %-20s %-15s %-25s %-15s %-10.2f\n", 
	                              rs.getInt("emp_id"), rs.getString("name"), rs.getString("role"),
	                              rs.getString("email"), rs.getString("phone"), rs.getDouble("salary"));
	        }

	        if (!hasEmployees) {
	            System.out.println("No employees report to this manager.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error retrieving hierarchy: " + e.getMessage());
	    }

	    System.out.println("==============================================================================================================\n");
	}

}
