package src.main.control;

import java.util.List;

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
    public String processApp(String internID, String studentID, String decision);
    public String acceptPlacement(String internID);
    public String requestWithdrawal(String internID, String reason);

    public String createOpp(String[] usrInput);
    public String editOpp(String[] usrInput);
    public String deleteOpp(String oppID);
    public String toggleVisibility(String oppID);
    public String processOpp(String internID, String decision);

    public void setFilters(String[] input);
    public String processWdr(int index, String decision);
    public List<String> viewWdr();
    public List<String> viewPendingReps();
    public String processRep(String repID, String decision);

}
