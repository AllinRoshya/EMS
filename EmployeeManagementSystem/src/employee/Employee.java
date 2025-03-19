package employee;

import java.io.*;
import java.util.ArrayList;

class Employee {
	int id;
	static int nextId = 0;
	static int nextManagerId = 1;
	String name;
	String role;
	String password;
	int managerId;
	String email;
	String phone;
	double salary;
	 int teamId;


	Employee(String name, String role,String role1,String password,int managerId, String email, String phone, double salary,int teamId) {
		this(nextId++, name, role1,password, managerId++, email, phone, salary, teamId);
	}

	Employee(int id, String name, String role,String password, int managerId, String email, String phone, double salary,
			int teamId) {
		this.id = id;
		this.name = name;
		this.role = role;
		this.password=password;
		this.managerId = managerId;
		this.email = email;
		this.phone = phone;
		this.salary = salary;
		this.teamId=teamId;
		if (id >= nextId) {
			nextId = id + 1;
		}
	}

	public static int getNextManagerId() {
		return nextManagerId++;
	}



	static void setNextId(int id) {
		nextId = id;
	}

	int getId() {
		return id;
	}

	String getName() {
		return name;
	}
	int getTeamId() {
		return teamId;
	}

	String getRole() {
		return role;
	}

	int getManagerId() {
		return managerId;
	}

	String getEmail() {
		return email;
	}

	String getPhone() {
		return phone;
	}

  

	double getSalary() {
		return salary;
	}

	void displayInfo() {
		System.out.println("\n ID: " + id + "\n Name: " + name + "\n Role: " + role + "\n Email: " + email
				+ "\n Phone: " + phone + "\n Salary: " + salary+"\n Team Id: "+teamId);
	}

	String getPassword() {
		return password;
	}
}
