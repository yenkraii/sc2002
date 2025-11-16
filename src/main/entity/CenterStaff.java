package src.main.entity;

public class CenterStaff extends UserAccount {
    private String staffDepartment; 
    private String role;

    public CenterStaff(String userID, String name, String password, String email, String staffDepartment, String role){
        super(userID, name, password, email);
        this.staffDepartment = staffDepartment;
        this.role = role;
    }

    public String getStaffDepartment(){
        return staffDepartment;
    }

    public void setStaffDepartment(String staffDepartment){
        this.staffDepartment = staffDepartment;
    }

    @Override
    public String getRole(){
        return "CENTER_STAFF";
    }
    public String[] export(){
        return new String[] {userID, name, role, staffDepartment, email};
    }
}
