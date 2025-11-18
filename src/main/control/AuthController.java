package src.main.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import src.main.entity.*;


public class AuthController {
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
    public String create(String[] infoToMap) {
        String key = infoToMap[0];

        if (userRepo.containsKey(key)) {
            return "User already exists."; // user already exists
        }

        CompanyRep newRep = new CompanyRep(key, infoToMap[1], infoToMap[2], infoToMap[3], infoToMap[4], infoToMap[5], infoToMap[6]);
        userRepo.put(key, newRep);
        pwdRepo.put(key, newRep.getPassword());
        return "Company Rep account created. Please wait for approval before logging in.";
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
    public String update(String id, String[] changeInfo) {

        UserAccount toChange = userRepo.get(id);
        String newPassword = changeInfo[0];

        if (toChange.changePassword(newPassword)) {
            userRepo.put(id, toChange);
            pwdRepo.put(id, newPassword);
            return "Password changed successfully.";
        }
        return "Error in changing password! Please try again";
    }


    public String delete(String id){
        if (userRepo.containsKey(id)){
            userRepo.remove(id);
            return "Account deleted.";
        }
        return "Error in account deletion.";
    }

    // log in user to system
    public String login(String userID, String password) {
        UserAccount user = userRepo.get(userID);
        if (user == null) {
            return "User does not exist.";
        }

        if (user.login(password)) {
            this.currentUser = user;
            return "Successfully Logged in.";
        }
        return "Wrong Password!";
    }

    // log out current user
    public void logout() {
        if (this.currentUser != null) {
            this.currentUser = null;
        }
    }

    public List<UserAccount> viewPendingReps(){
        return userRepo.values()
                .stream()
                .filter(acc -> "COMPANY_REP".equals(acc.getRole()))
                .filter(cr -> ((CompanyRep) cr).getStatus() == AccountStatus.PENDING_APPROVAL)
                .collect(Collectors.toList());
    }

    public String approveRep(String repID){
        UserAccount acc = userRepo.get(repID);
        if (acc == null || !"COMPANY_REP".equals(acc.getRole())) return "Not a company rep";
        CompanyRep rep = (CompanyRep) acc;

        if (rep.getStatus() != AccountStatus.PENDING_APPROVAL)
            return "Cannot edit this account!";

        rep.setStatus(AccountStatus.APPROVED);
        return "Approved";
    }

    public String rejectRep(String repID){
        UserAccount acc = userRepo.get(repID);
        if (acc == null || !"COMPANY_REP".equals(acc.getRole())) return "Not a company rep";
        CompanyRep rep = (CompanyRep) acc;

        if (rep.getStatus() != AccountStatus.PENDING_APPROVAL)
            return "Cannot edit this account!";

        rep.setStatus(AccountStatus.REJECTED);
        return "Rejected";
    }


}
