package src.main.entity;


public class UserAccount {
    private String userID;
    private String name;
    private String password;
    private AccountStatus status;

    // 
    UserAccount(String id, String userName){
        userID = id;
        name = userName;
        password = "password"; // default
        status = AccountStatus.PendingApproval;
    }

    public boolean checkPasswordMatch(String enteredPassword){
        return (password == enteredPassword) ? true : false;
    }
    
    public boolean changePassword(String toChange){
        if (checkPasswordMatch(toChange)) return false;
        password = toChange;
        return true;
    }

    public boolean changeStatus(AccountStatus newStatus){
        if(newStatus == status) return false;
        newStatus = status;
        return true;
    }

    // override instead creating new methods
    @Override
    public String toString(){
        return this.userID + ", " + this.name;
    }

    @Override
    public boolean equals(Object toCompare){
        UserAccount other = (UserAccount) toCompare;
        return (this.userID == other.userID) ? true : false;
    }
}
