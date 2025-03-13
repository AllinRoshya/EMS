package employee;

import java.util.ArrayList;

class Team {
	 int teamId;
	 String teamName;
	 int managerId;
	 ArrayList<Integer> employeeIds;

	 Team(String teamName) {
		 super();
		this.teamName = teamName;
		this.managerId = -1;
		this.employeeIds = new ArrayList<>();
	}

	Team() {
//        this.teams = new ArrayList<>();
		this.employeeIds = new ArrayList<>();
	}

	 Team(int teamId, String teamName, int managerId) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.managerId = managerId;
		this.employeeIds = new ArrayList<>();
	}

	int getTeamId() {
		return teamId;
	}

	String getTeamName() {
		return teamName;
	}

	void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	int getManagerId() {
		return managerId;
	}

	void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	ArrayList<Integer> getEmployeeIds() {
		return employeeIds;
	}

	void addEmployee(int employeeId) {
		this.employeeIds.add(employeeId);
	}

	void viewTeamMembers() {
		System.out.println("\n==== Team Details ====");
		System.out.println("Team: " + teamName);
		System.out.println("Manager ID: " + (managerId != -1 ? managerId : "No Manager Assigned"));
		if (employeeIds.isEmpty()) {
			System.out.println("No employees assigned to this team.");
		} else {
			System.out.println("Team Members: " + employeeIds);
		}
	}
}
