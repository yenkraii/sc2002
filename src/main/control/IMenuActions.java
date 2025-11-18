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
    public String registerCompanyRep(String[] usrInput);
    public void setFilters(String[] input);
    public List<String> viewOpp();

}
