package src.main.control;

import java.util.List;

public interface IStudentActions extends IMenuActions {
    public String applyIntern(String id);
    public List<String> viewApp(String internID);
    public String acceptPlacement(String internID);
    public String requestWithdrawal(String internID, String reason);
}
