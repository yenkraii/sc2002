package src.main.control;

public interface IMenuActions {
    public void setup();
    public boolean isLoggedOn();
    public String accountRole();
    public boolean login(String[] usrInput);
    public void logout();
    public void changePassword(String[] usrInput);
}
