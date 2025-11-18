package src.main.control;

import java.util.List;

public interface IStaffActions extends IMenuActions{
    public String processWdr(int index, String decision);
    public List<String> viewWdr();
    public List<String> viewPendingReps();
    public String processRep(String repID, String decision);
    public String deleteOpp(String oppID);
    public String processOpp(String internID, String decision);   
}
