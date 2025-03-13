package employee;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

class Admin extends Employee {

	    Admin(int id, String name) {
	        super(id, name, "Admin","", 0, "", "",0,0);
	    }

	    void addEmployee(ArrayList<Employee> employees, String name, String role, int managerId, String email, String phone,double salary,int teamId) {
	       Employee newEmployee= new Employee( teamId, name, role, phone, managerId, email, phone,salary,teamId);
	       employees.add(newEmployee);
	        System.out.println("Employee added successfully. Employee ID: " +newEmployee .getId());
	    
	    }
	    void addManager(ArrayList<Employee> employees, String name, String role, String email, String phone, double salary,int teamId) {
	        Employee newManager = new Employee(teamId, name, role, phone, managerId, email, phone, salary,teamId); 
	        employees.add(newManager);
	        System.out.println("Manager added successfully. Manager ID: " + newManager.getId());
	    }
	
}
