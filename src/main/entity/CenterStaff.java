package src.main.entity;

import enums.AccountStatus;
import enums.InternshipStatus;
import enums.ApplicationStatus; 

public class CenterStaff extends UserAccount implements StaffActions{
    private String staffDepartment; 

    public CenterStaff(String userID, String name, String password, String staffDepartment){
        super(userID, name, password, AccountStatus.Active);
        this.staffDepartment = staffDepartment;
    }

    public String getStaffDepartment(){
        return staffDepartment;
    }

    public void setStaffDepartment(String staffDepartment){
        this.staffDepartment = staffDepartment;
    }

    public boolean approveCompanyRep(CompanyRep rep){
        if (rep.getStatus() == AccountStatus.PendingApproval){
            rep.setStatus(AccountStatus.Active);
            System.out.println("Approved Company Representative: " + rep.getName());
            return true;
        } else {
            System.out.println("Company Representative " + rep.getName() + "is not pending approval.");
            return false;
        } 
    }

    public boolean rejectCompanyRep(Company rep){
        if (rep.getStatus() == AccountStatus.PendingApproval){
            rep.setStatus(AccountStatus.Suspended);
            System.out.println("Rejected Company Representative: " + rep.getName());
            return true;
        } else {
            System.out.println("Company Representative " + rep.getName() + "is not pending approval.";
            return false;
        }
    }

    public boolean approveInternshipOpp(InternshipOpportunity opp){
        if (opp.getStatus() == InternshipStatus.PendingApproval){
            opp.setStatus(InternshipStatus.Approved);
            opp.setVisibiliy(true);
            System.out.println("Internship Opportunity Approved: " + opp.getTitle());
            return true;
        } else {
            System.out.println("Internship " + opp.getTitle() + "is not pending approval.");
            return false;
        }
    }

    public boolean rejectInternshipOpp(InternshipOpportunity opp) {
        if (opp.getStatus() == InternshipStatus.PendingApproval){
            opp.setStatus(InternshipStatus.Rejected);
            opp.setVisibility(false);
            System.out.println("Internship Opportunity Rejected: " + opp.getTitle());
            return true;
        } else {
            System.out.println("Internship " + opp.getTitle() + "is not pending approval.");
            return false;
        }
    }

    public boolean approveWithdrawalReq(InternApplication app){
        if (app.getStatus() != ApplicationStatus.Withdraw){
            app.getStatus(ApplicationStatus.Withdrawn);
            System.out.println("Withdrawal request approved for " + app.getStudent().getName());
            return true;
        } else {
            System.out.println("Application already withdrawn.");
            return false;
        }
    }

    public boolean rejectedWithdrawalReq(InternApplication app){
        System.out.println("Withdrawal request rejected for " + app.getStudent().getName());
        return false;
    }

    public String toString(){
        return "CenterStaff{" + "Name='" + getName() + '\'' + ", Department='" + staffDepartment + '\'' + ", Status=" + getStatus() + '}';
    }
    
}
