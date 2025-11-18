package src.main.control;

import java.util.List;

/**
 * Interface defining basic menu actions applicable to all user roles.
 */
public interface IMenuActions {
    /**
     * Sets up necessary resources or data for the menu actions.
     */
    public void setup();

    /**
     * Performs shutdown tasks such as saving data.
     */
    public void shutdown();

    /**
     * Checks if a user is currently logged on.
     * @return True if logged on, false otherwise.
     */
    public boolean isLoggedOn();

    /**
     * Gets the account role of the current user.
     * @return Role string of the account.
     */
    public String accountRole();

    /**
     * Logs in a user with the provided credentials.
     * @param usrInput Array containing user ID and password.
     * @return Status message indicating success or failure.
     */
    public String login(String[] usrInput);

    /**
     * Logs out the current user.
     */
    public void logout();

    /**
     * Changes the password of a logged-in user.
     * @param usrInput Array containing old and new passwords.
     */
    public void changePassword(String[] usrInput);

    /**
     * Registers a new company representative.
     * @param usrInput Array containing registration information.
     * @return Status message indicating success or failure.
     */
    public String registerCompanyRep(String[] usrInput);

    /**
     * Sets filters for data viewing or reporting.
     * @param input Array containing filter criteria.
     */
    public void setFilters(String[] input);

    /**
     * Views available internship opportunities.
     * @return List of strings representing opportunities.
     */
    public List<String> viewOpp();
}
