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
    public String applyIntern(String id);
    public List<String> viewOpp();
    public List<String> viewApp(String internID);
    public String acceptPlacement(String internID);
    public String requestWithdrawal(String internID, String reason);

    public String createOpp(String[] usrInput);
    //public String processApp(CompanyRep rep);
    //public String editOpp(CompanyRep rep);
    public String deleteOpp(String oppID);
    public String toggleVisibility(String oppID);

    //public void manageCompanyReps();
    //public void manageOpp();
    //public void generateReports();
    public void setFilters(String[] input);

}
