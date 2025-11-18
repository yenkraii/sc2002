package src.main.entity;

/**
 * Abstract base class for all user accounts in the system.
 * Demonstrates encapsulation and abstraction principles.
 */
public abstract class UserAccount {
    protected String userID;
    protected String name;
    protected String password;
    protected AccountStatus status;
    protected String email;
    /**
     * Constructs a UserAccount with specified user details.
     * @param userID Unique identifier for user.
     * @param name Full name of user.
     * @param password User’s password.
     * @param email Email address of user.
     */
    public UserAccount(String userID, String name, String password, String email) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.email = email;
        this.status = AccountStatus.APPROVED;
    }

    // getters & setters (encapsulation)
    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }


    /**
     * Performs login validation for this account.
     * @param inputPassword Password for validation.
     * @return True if login is successful and account approved, else false.
     */
    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword) && this.status == AccountStatus.APPROVED;
    }

    /**
     * Changes the account password.
     * @param newPass The new password to set.
     * @return True if password changed successfully, false otherwise.
     */
    public boolean changePassword(String newPass) {
        if (this.password.equals(newPass)) {
            return false;    
        }
        this.password = newPass;
        return true;
        
    }
    /**
     * Abstract method for exporting account details.
     * @return String for export (e.g., CSV format).
     */
    public abstract String[] export();

    /**
     * Abstract method for retrieving user role.
     * @return String representing the user role.
     */
    public abstract String getRole();

    @Override
    public String toString(){
        return userID;
    }

}
