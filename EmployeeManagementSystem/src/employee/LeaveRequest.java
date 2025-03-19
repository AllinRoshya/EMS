package employee;

public class LeaveRequest {
     static int requestCounter = 1; 
     int requestId;
     int empId;
     String leaveDetails;
     boolean isPending;
     boolean isApproved;

   
    public LeaveRequest(int empId, String leaveDetails) {
        this.requestId = requestCounter++;
        this.empId = empId;
        this.leaveDetails = leaveDetails;
        this.isPending = true; 
        this.isApproved = false;
    }

    // Getters
    public int getRequestId() {
        return requestId;
    }

    public int getEmpId() {
        return empId;
    }

    public String getLeaveDetails() {
        return leaveDetails;
    }

    public boolean isPending() {
        return isPending;
    }

    public boolean isApproved() {
        return isApproved;
    }

    // Set leave status (approved/rejected)
   void setLeaveStatus(boolean approval) {
        this.isPending = false;
        this.isApproved = approval;
    }

    
}
