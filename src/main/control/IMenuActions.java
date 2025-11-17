package src.main.control;

import java.util.List;
import src.main.entity.*;

public interface IMenuActions {
    public void setup();
    public void shutdown();
    public boolean isLoggedOn();
    public String accountRole();
    public String login(String[] usrInput);
    public void logout();
    public void changePassword(String[] usrInput);

    public void registerCompanyRep(String[] usrInput);
    public String applyIntern(UserAccount user, String id);
    public List<InternshipOpportunity> viewOpp();
    public List<InternshipApplication> viewApp();
    public String acceptPlacement(UserAccount user);
    public String requestWithdrawal(UserAccount user);

    public String createOpp(UserAccount rep);
    public String viewOpp(CompanyRep rep);
    public String viewApp(CompanyRep rep);
    public String processApp(CompanyRep rep);
    public String editOpp(CompanyRep rep);
    public String deleteOpp(CompanyRep rep);
    public String toggleVisibility(CompanyRep rep);

    public void manageCompanyReps();
    public void manageOpp();
    public void generateReports();
    public void setFilters();

}
