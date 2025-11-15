package src.main.entity;

public class CenterStaff extends UserAccount {
    private String staffDepartment; 

    public CenterStaff(String userID, String name, String password, String staffDepartment){
        super(userID, name, password);
        this.staffDepartment = staffDepartment;
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
}
