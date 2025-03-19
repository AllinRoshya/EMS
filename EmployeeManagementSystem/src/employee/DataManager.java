package employee;

import java.util.ArrayList;
import java.io.*;
class DataManager {
    ArrayList<LeaveRequest> leaveRequests = new ArrayList<>();
   

    static boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    static boolean isValidPhone(String phone) {
        return phone.matches("^[0-9]{10}$");
    }


}
