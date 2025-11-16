package src.main.control;

import java.util.Map;

import src.main.entity.*;

// controller for authentication operations
// demonstrates SRP (handles only authentication)
public class AuthController implements IEditLoadedData{
    private Map<String,UserAccount> userRepo;
    private Map<String,String> pwdRepo;
    private UserAccount currentUser;

    // constructor for AuthController
    // param userRepository: user repository instance
    public AuthController(DataRepository dataRepository) {
        this.userRepo = dataRepository.userRepo;
        this.pwdRepo = dataRepository.pwdRepo;
        this.currentUser = null;
    }

    // register a new company representative
    // param userID: company email
    // param name: rep name
    // param password: rep password
    // param companyName: company name
    // param department: rep department
    // param position: rep position in company
    // return true if registration successful
    public boolean create(UserAccount actor, String[] infoToMap) {
        String key = infoToMap[0];
        if (userRepo.containsKey(key) || actor instanceof Student) {
            return false; // user already exists
        }

        CompanyRep newRep = new CompanyRep(key, infoToMap[1], infoToMap[2], infoToMap[3], infoToMap[4], infoToMap[5]);
        userRepo.put(key, newRep);
        return true;
    }

    // get current logged in user
    // return current user account
    public UserAccount getCurrrentUser(){
        return this.currentUser;
    }

    public UserAccount read(String id) {
        return userRepo.get(id);
    }

    // change password for user
    // param UserAccount: user account
    // param oldPassword: old password
    // param newPassword: new password
    // return true if successful
    public boolean update(String id, String[] changeInfo) {

        UserAccount toChange = userRepo.get(id);
        String newPassword = changeInfo[0];

        if (toChange.changePassword(newPassword)) {
            userRepo.replace(id, toChange);
            pwdRepo.replace(id, newPassword);
            return true;
        }
        return false;
    }


    public boolean delete(String id){
        if (userRepo.containsKey(id)){
            userRepo.remove(id);
            return true;
        }
        return false;
    }

    // log in user to system
    public boolean login(String userID, String password) {
        UserAccount user = userRepo.get(userID);
        if (user == null) {
            return false;
        }

        if (user.login(password)) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    // log out current user
    public void logout() {
        if (this.currentUser != null) {
            this.currentUser.logout();
            this.currentUser = null;
        }
    }

}
