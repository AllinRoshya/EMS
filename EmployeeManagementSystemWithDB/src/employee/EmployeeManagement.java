package employee;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class EmployeeManagement {
	static final Logger logger = Logger.getLogger(EmployeeManagement.class);
    static Scanner input = new Scanner(System.in);
   static Team teams=new Team();
	static TeamDAO teamDAO=new TeamDAO();
   static EmployeeDAO employeeDAO=new EmployeeDAO();
    public static void main(String[] args) throws SQLException {
    	PropertyConfigurator.configure("/home/allin-zstk382/eclipse-workspace/EmployeeManagementSystem/src/LogFolder/log4j.properties");  
    	System.out.println("\n--------------Employee Management System--------------");
        ArrayList<Employee> employees = employeeDAO.loadEmployees();
        DataManager dataManager = new DataManager();
        
        while (true) {
            try {
                System.out.println("\n1. Login");
                System.out.println("2. Exit");
                System.out.print("\nEnter your choice: ");
                int choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                case 1:
                    login(dataManager, employees);
                    break;
                case 2:
                    logger.info("Exiting system...");
                    return;

                default:
                    logger.warn("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                input.nextLine();
            }
        }
    }


	static void login(DataManager dataManager, ArrayList<Employee> employees) { 
            System.out.print("\nEnter Username: ");
            String username = input.nextLine().trim();
            System.out.print("Enter Password: ");
            String password = input.nextLine().trim();

            if (username.equalsIgnoreCase("a") && password.equals("a123")) {
                Admin admin = new Admin(1, "a");
                adminMenu(admin, employees, dataManager,teams); 
                return;
            }

            System.out.print("Enter Role (manager/employee): ");
            String role = input.nextLine().trim().toLowerCase();

            Employee authenticatedUser = DatabaseConnection.authenticate(username, password);
            
            System.out.println(authenticatedUser);

            if (authenticatedUser == null) {
                DatabaseConnection.insertLoginDetails(username, password, role);
            }
            switch (role) {
                case "manager":
                    Manager manager = new Manager(authenticatedUser.getId(), authenticatedUser.getName());
                    managerMenu(manager, dataManager);  
                    break;

                case "employee":
				Employee fullEmployee = null;
				try {
					fullEmployee = EmployeeDAO.findEmployee(authenticatedUser.getName());
				} catch (SQLException e) {
					e.printStackTrace();
				}
                       if (fullEmployee != null) {
                           employeeMenu(fullEmployee, dataManager);  
                       } else {
                           System.out.println("Error retrieving employee details.");
                       }
                       break;
                default:
                    logger.warn("Invalid role.");
            }
        } 
	 static ArrayList<Employee> loadEmployeesint() {
	        ArrayList<Employee> employees = new ArrayList<>();
	        try {
	            employees = employeeDAO.loadEmployees(); 
	            
	        } catch (SQLException e) {
	            logger.error("Error loading employees from DB: " + e.getMessage(),e);
	        }
	        return employees;
	    }

	    static void saveEmployee(Employee emp) {
	        try {
	            EmployeeDAO.saveEmployee(emp);
	        } catch (SQLException e) {
	            logger.error("Error saving employee to DB: " + e.getMessage());
	        }
	    }

	    static Employee findEmployee(String username){
	        try {
	            return EmployeeDAO.findEmployee(username);  
	        } catch (SQLException e) {
	            logger.error("Error finding employee in DB: " + e.getMessage());
	        }
	        return null;
	    }

	static void adminMenu(Admin admin, ArrayList<Employee> employees, DataManager dataManager, Team teams) {
		while (true) {
			try {
				System.out.println("\n------Admin Menu------");
				System.out.println("1. Add Employee");
				System.out.println("2. View All Employees");
				System.out.println("3. Update Employee");
				System.out.println("4. Delete Employee");
				System.out.println("5. Add Manager");
				System.out.println("6. View Hierarchy");
				System.out.println("7. Manage Teams");
				System.out.println("8. Logout");
				System.out.print("\nEnter your choice: ");
				int choice = input.nextInt();
				input.nextLine();

				switch (choice) {
				case 1:
				    System.out.print("Enter Name: ");
				    String name = input.nextLine();
				    
				    System.out.print("Enter Role: ");
				    String role = input.nextLine();
				    
				    System.out.print("Enter Password: ");
				    String password = input.nextLine();
				    
				    int managerId;
				    if (role.equalsIgnoreCase("Manager")) {
				        managerId = Employee.getNextManagerId(); 
				    } else {
				        System.out.print("Enter Manager ID: ");
				        managerId = input.nextInt();
				        input.nextLine();
				    }
				    
				    System.out.print("Enter Email: ");
				    String email = input.nextLine();
				    if (!DataManager.isValidEmail(email)) {
				        logger.warn("Invalid email format. Please try again.");
				        break;
				    }
				    
				    System.out.print("Enter Phone: ");
				    String phone = input.nextLine();
				    if (!DataManager.isValidPhone(phone)) {
				        logger.warn("Invalid phone number. It should be 10 digits.");
				        break;
				    }
				    
				    System.out.print("Enter Salary: ");
				    double salary = input.nextDouble();
				    input.nextLine();
				    
				    System.out.print("Enter Team Id: ");
				    int teamId = input.nextInt();
				    input.nextLine();
				    
				    Employee newEmployee = new Employee(name, role, role, password, managerId, email, phone, salary, teamId);
				    EmployeeDAO.saveEmployee(newEmployee);
				    System.out.println("Employee added successfully.");
				    break;

				case 2:
					employeeDAO.viewAllEmployees(employees);
					break;

				case 3:
					System.out.print("Enter Employee ID to update: ");
				    int updateId = input.nextInt();
				    input.nextLine(); 

				    System.out.print("Enter New Role: ");
				    String newRole = input.nextLine();

				    System.out.print("Enter New Email: ");
				    String newEmail = input.nextLine();
				    if (!DataManager.isValidEmail(newEmail)) {
				        System.out.println("Invalid email format. Please try again.");
				        break;
				    }

				    System.out.print("Enter New Phone Number: ");
				    String newPhoneNo = input.nextLine();
				    if (!DataManager.isValidPhone(newPhoneNo)) {
				        System.out.println("Invalid phone number. It should be 10 digits.");
				        break;
				    }

				    System.out.print("Enter New Salary: ");
				    double newSalary = input.nextDouble();
					employeeDAO.updateEmployee(updateId, newRole, newEmail, newPhoneNo, newSalary);
                    break;

				case 4:
				    System.out.print("Enter Employee ID to delete: ");
				    int deleteId = input.nextInt();
				    input.nextLine();
				    
				    boolean deleted = employeeDAO.deleteEmployee(deleteId);
				    if (deleted) {
				        System.out.println("Employee deleted successfully.");
				    } else {
				        System.out.println("Employee not found or could not be deleted.");
				    }
				    break;

				case 5:
				    System.out.print("Enter Manager Id: ");
				    int managerId1 = input.nextInt();
				    input.nextLine(); 
				    
				    System.out.print("Enter Manager Name: ");
				    String managerName = input.nextLine();
				    
				    System.out.print("Enter Role: ");
				    String managerRole = input.nextLine();
				    
				    System.out.print("Enter Manager Email: ");
				    String managerEmail = input.nextLine();
				    if (!DataManager.isValidEmail(managerEmail)) {
				        System.out.println("Invalid email format. Please try again.");
				        break;
				    }

				    System.out.print("Enter Manager Phone: ");
				    String managerPhone = input.nextLine();
				    if (!DataManager.isValidPhone(managerPhone)) {
				        System.out.println("Invalid phone number. It should be 10 digits.");
				        break;
				    }

				    System.out.print("Enter Manager Salary: ");
				    double managerSalary = input.nextDouble();
				    input.nextLine(); 
				    
				    System.out.print("Enter Team Id: ");
				    int teamId1 = input.nextInt();
				    input.nextLine();

				    Employee newManager = new Employee(managerId1, managerName, managerRole, "manager123", 0, managerEmail, managerPhone, managerSalary, teamId1);
				    EmployeeDAO.saveEmployee(newManager);
				    
				    System.out.println("Manager added successfully.");
				    break;

				case 6:
				    System.out.print("Enter Manager ID to view hierarchy: ");
				    int hierManId = input.nextInt();
				    input.nextLine(); 
				    employeeDAO.viewHierarchy(hierManId);
				    break;

				case 7:
				    manageTeams(teams);
				    break;

				case 8:
				 return;

				default:
					logger.warn("Invalid choice.");
				}
			} catch (Exception e) {
				logger.error("Error in Admin Menu: " + e.getMessage());
				input.nextLine();
			}
		}
	}

	static void manageTeams(Team teams) {
		while (true) {
			try {
				System.out.println("\n------Manage Teams------");
				System.out.println("\n1. Add Team");
				System.out.println("2. View All Teams");
				System.out.println("3. View team members");
				System.out.println("4. Logout");
				System.out.print("\nEnter your choice: ");
				int choice = input.nextInt();
				input.nextLine();

				switch (choice) {
				case 1:
					System.out.print("Enter Team Name: ");
					String teamName = input.nextLine();
					System.out.println("Enter Manager Id: ");
					int managerId=input.nextInt();
					if (teamName.isEmpty()) {
						System.out.println("Team name cannot be empty.");
					} else {
						 boolean success = teamDAO.addTeam(teamName,managerId); 
					        if (success) {
					            System.out.println("Team added successfully!");
					        } else {
					            System.out.println("Failed to add team.");
					        }
					    }
					    break;

				case 2:
					System.out.println("\nList of Teams:");
					teamDAO.viewAllTeams();
					break;

				case 3:
					System.out.print("Enter Team Name: ");
					String viewTeamName = input.nextLine().trim();

					Team viewTeam = teamDAO.findTeam(viewTeamName);
					if (viewTeam == null) {
						System.out.println("Team not found.");
					} else {
						viewTeam.viewTeamMembers();
					}
					break;
				case 4:
					return;

				default:
					System.out.println("Invalid choice.");
				}
			} catch (Exception e) {
				System.out.println("Error in manage teams: " + e.getMessage());
			}
		}
	}

	static void managerMenu(Manager manager, DataManager dataManager) {
	    while (true) {
	        try {
	            System.out.println("\n------Manager Menu------");
	            System.out.println("1. View Employees");
	            System.out.println("2. Approve/Reject Leave");
	            System.out.println("3. Logout");
	            System.out.print("\nEnter your choice: ");
	            int choice = input.nextInt();
	            input.nextLine();

	            switch (choice) {
	            case 1:
	                System.out.println("\nList of Employees:");
	                for (Employee emp : loadEmployeesint()) { 
	                    emp.displayInfo();
	                }
	                break;

	            case 2:
	                System.out.print("Enter Employee ID for leave approval: ");
	                int empId = input.nextInt();
	                input.nextLine();
	                System.out.print("Approve leave? (true/false): ");
	                boolean approval = input.nextBoolean();
	                input.nextLine();
	                LeaveRequestDAO.approveLeave(dataManager, empId, approval);
	                break;

	            case 3:
	                return;

	            default:
	                System.out.println("Invalid choice.");
	            }
	        } catch (Exception e) {
	            System.out.println("An unexpected error occurred: " + e.getMessage());
	        }
	    }
	}
	
	static void employeeMenu(Employee employee, DataManager dataManager) {
		while (true) {
			try {
				System.out.println("\n------Employee Menu------");
				System.out.println("1. View Profile");
				System.out.println("2. Apply for Leave (reason)");
				System.out.println("3. Check Leave Status");
				System.out.println("4. Exit");
				System.out.print("\nEnter your choice: ");
				int choice = input.nextInt();
				input.nextLine();

				switch (choice) {
				case 1:
					System.out.println("Employee ID: " + employee.getId());
					Employee fullEmployee = EmployeeDAO.findEmployee(employee.getName());
                    if (fullEmployee != null) {
                        fullEmployee.displayInfo();  
                    } else {
                        System.out.println("Employee not found. Please try again.");
                    }
                    break;

				case 2:
					System.out.println("Employee ID: " + employee.getId());
					System.out.print("Enter Leave Details: ");
					String leaveDetails = input.nextLine();
					System.out.println("Applying leave for Employee ID: " + employee.getId());
					LeaveRequestDAO.applyLeave(employee.getId(), leaveDetails);
					break;

				case 3:
					System.out.println("Checking leave status...");
					LeaveRequestDAO.checkLeaveStatus(employee.getId());
					break;

				case 4:
					return;

				default:
					System.out.println("Invalid choice.");
				}
			} catch (Exception e) {
				System.out.println("An unexpected error occurred: " + e.getMessage());
			}
		}
	}

}
