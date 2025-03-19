package employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

	static Connection connect = null;
	static Statement stat = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;

     boolean addTeam(String teamName,int managerId) {
        String query = "INSERT INTO Team (name,manager_id) VALUES (?,?)";
        
        try {
        	connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
        	pstmt = connect.prepareStatement(query);
            pstmt.setString(1, teamName);
            if (managerId == -1) { 
                pstmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(2, managerId);
            }
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error adding team: " + e.getMessage());
            return false;
        }
    }

    public List<Team> viewAllTeams() {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM Team";
        try {
        	connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
        	pstmt = connect.prepareStatement(query);
        	
        	rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("t_id");
                String name = rs.getString("name");
                int managerId = rs.getInt("manager_id");
                teams.add(new Team(id,name,managerId));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching teams: " + e.getMessage());
        }
        return teams;
        
    }
    public boolean assignEmployeeToTeam(int teamId, int employeeId) {
        String query = "INSERT INTO team_members (t_id, emp_id) VALUES (?, ?)";
        try  {
        	connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
        	 pstmt = connect.prepareStatement(query);
            pstmt.setInt(1, teamId);
            pstmt.setInt(2, employeeId);
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error assigning employee to team: " + e.getMessage());
            return false;
        }
    }
    public void viewTeamMembers(int teamId) {
        String query = "SELECT t.t_id, t.name, e.emp_id, e.name, e.role, e.email, e.phone, e.salary " +
                       "FROM Team t " +
                       "LEFT JOIN Employee e ON t.t_id = e.team_id " +
                       "WHERE t.t_id = ?";

        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
            pstmt = connect.prepareStatement(query);
            pstmt.setInt(1, teamId);
            rs = pstmt.executeQuery();

            System.out.println("\n================================================= Team Members ===============================================");
            System.out.printf("%-5s %-15s %-5s %-20s %-10s %-25s %-15s %-10s\n\n",
                    "TID", "Team Name", "EID", "Employee Name", "Role", "Email", "Phone", "Salary");
            System.out.println("----------------------------------------------------------------------------------------------------------------");

            boolean hasMembers = false;
            while (rs.next()) {
                hasMembers = true;
                System.out.printf("%-5d %-15s %-5d %-20s %-10s %-25s %-15s %-10.2f\n",
                        rs.getInt("t_id"), rs.getString("name"), rs.getInt("emp_id"),
                        rs.getString("name"), rs.getString("role"), rs.getString("email"),
                        rs.getString("phone"), rs.getDouble("salary"));
            }
            
            if (!hasMembers) {
                System.out.println("No employees found in this team.");
            }
            System.out.println("===============================================================================================================");
            
        } catch (SQLException e) {
            System.out.println("Error fetching team members: " + e.getMessage());
        }
    }

    
    public Team findTeam(String teamName) {
        String query = "SELECT * FROM Team WHERE name = ?";
        try  {
        	connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeManagement", "root", "03Sep2006");
        	 pstmt = connect.prepareStatement(query);
            pstmt.setString(1, teamName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("t_id");
                String name = rs.getString("name");
                int managerId = rs.getInt("manager_id");

                return new Team(id, name, managerId);
            }
        } catch (SQLException e) {
            System.out.println("Error finding team: " + e.getMessage());
        }
        return null;
    }

}

