package src.main.entity;
/**
 * Represents a career centre staff user account.
 * Extends UserAccount and maintains staff-specific information.
 */
public class CenterStaff extends UserAccount {
    private String staffDepartment; 
    private String role;
    /**
     * Constructs a CenterStaff account with department and role information.
     * @param userID Staff user ID.
     * @param name Staff member name.
     * @param password Account password.
     * @param email Staff member email.
     * @param staffDepartment Department of staff member.
     * @param role Role/position of staff member.
     */
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
