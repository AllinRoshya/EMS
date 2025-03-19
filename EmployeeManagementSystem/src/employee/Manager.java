package employee;

import java.util.ArrayList;

class Manager extends Employee {

    Manager(int id, String name) {
        super("", name, "Manager","", 0, "", "", 0, id);
    }

    

     Employee findEmployee(ArrayList<Employee> employees, int empId) {
        for (Employee emp : employees) {
            if (emp.getId() == empId) {
                return emp;
            }
        }
        return null;
    }
}
