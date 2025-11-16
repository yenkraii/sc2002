package src.main.entity;

// abstract base class for all user accounts in system
// demonstrates encapsulation & abstraction principles

public abstract class UserAccount {
    protected String userID;
    protected String name;
    protected String password;
    protected AccountStatus status;

    // constructor for UserAccount
    // param userID: unique identifier for user
    // param name: full name of user
    // param password: user's password
    public UserAccount(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        this.password = password;
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


    // login validation - template method pattern
    // param inputPassword: password to validate
    // return true if login successful
    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword) && this.status == AccountStatus.APPROVED;
    }

    // log out operation
    public void logout() {
        System.out.println(name + " logged out successfully.");
    }

    // change password functionality
    // param oldPassword: current password
    // param newPassword: new password to set
    // return true if password changed successfully
    public boolean changePassword(String newPass) {
        if (this.password.equals(newPass)) {
            return false;    
        }
        this.password = newPass;
        return true;
        
    }

    // abstract method for getting user role (polymorphism)
    // return String representation of user role
    public abstract String getRole();

}
