package src.main.control;

import java.util.List;

public interface ICompanyActions extends IMenuActions{
    public String createOpp(String[] usrInput);
    public String editOpp(String[] usrInput);
    public String deleteOpp(String oppID);
    public String toggleVisibility(String oppID);
    public String processOpp(String internID, String decision);   
    public String processApp(String internID, String studentID, String decision);
    public List<String> viewApp(String internID);
}
