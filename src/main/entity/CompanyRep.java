package src.main.entity;

public class CompanyRep extends UserAccount {
    private String companyName;
    private String department;
    private String position;

    public CompanyRep(String userID, String name, String password, String companyName, String department, String position){
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department; 
        this.position = position; 
        this.status = AccountStatus.PENDING_APPROVAL; 
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
}
