package src.main.entity;

/**
 * Represents a company representative account.
 * Extends UserAccount and includes company details.
 */

public class CompanyRep extends UserAccount {
    private String companyName;
    private String department;
    private String position;
    /**
     * Constructs a CompanyRep with specified details.
     * @param userID Unique user ID.
     * @param name Representative’s name.
     * @param password Account password.
     * @param email Representative’s email.
     * @param companyName Name of company.
     * @param department Representative’s department.
     * @param position Representative’s position.
     */
    public CompanyRep(String userID, String name, String password, String email, String companyName, String department, String position){
        super(userID, name, password, email);
        this.companyName = companyName;
        this.department = department; 
        this.position = position; 
        this.status = AccountStatus.PENDING_APPROVAL; 
    }
    /**
     * Constructs a CompanyRep with full details including status.
     * @param userID Unique user ID.
     * @param name Representative’s name.
     * @param password Account password.
     * @param email Representative’s email.
     * @param companyName Name of company.
     * @param department Representative’s department.
     * @param position Representative’s position.
     * @param status Account status string.
     */
    public CompanyRep(String userID, String name, String password, String email, String companyName, String department, String position, String status){
        super(userID, name, password, email);
        this.companyName = companyName;
        this.department = department; 
        this.position = position; 
        this.status = AccountStatus.valueOf(status); 
    }

    public String getCompanyName(){
        return companyName;
    }

    public String getDepartment(){
        return department;
    }

    public String getPosition(){
        return position;
    }

    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public void setPosition(String position){
        this.position = position;
    }

    public String getRole(){
        return "COMPANY_REP";
    }    

    public String[] export(){
        return new String[] {userID, name, companyName, department,position, email, String.valueOf(status)};
    }

}
