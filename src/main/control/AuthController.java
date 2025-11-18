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
    /**
     * Instantiates AuthController with provided data repository.
     * @param dataRepository The repository containing user and authentication data.
     */
    public AuthController(DataRepository dataRepository) {
        this.userRepo = dataRepository.userRepo;
        this.pwdRepo = dataRepository.pwdRepo;
        this.currentUser = null;
    }

    /**
     * Registers a new company representative account.
     * @param infoToMap String[] user input that needs to be mapped
     * @return String status message for registration outcome.
     */
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

    /**
     * Retrieves the currently logged-in user account.
     * @return The current UserAccount if logged in, null otherwise.
     */
    public UserAccount getCurrrentUser(){
        return this.currentUser;
    }
    /**
     * Reads user account information by ID.
     * @param id User account ID.
     * @return The UserAccount instance if found.
     */
    public UserAccount read(String id) {
        return userRepo.get(id);
    }

    /**
     * Changes the password for the specified user account.
     * @param id User id to update (usually logged in account)
     * @param changeInfo new password entered by user
     * @return Status message indicating success or reason for failure.
     */
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

    /**
     * Deletes a user account by ID.
     * @param id ID of the account to delete.
     * @return Status message on account deletion result.
     */
    public String delete(String id){
        if (userRepo.containsKey(id)){
            userRepo.remove(id);
            return "Account deleted.";
        }
        return "Error in account deletion.";
    }

    /**
     * Logs a user into the system.
     * @param userID The user account ID.
     * @param password The password for authentication.
     * @return Status message indicating success or reason for failure.
     */
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

    /**
     * Logs out the current user.
     */
    public void logout() {
        if (this.currentUser != null) {
            this.currentUser = null;
        }
    }
    /**
     * Views pending company representative accounts for approval review.
     * @return List of UserAccount instances pending approval.
     */
    public List<UserAccount> viewPendingReps(){
        return userRepo.values()
                .stream()
                .filter(acc -> "COMPANY_REP".equals(acc.getRole()))
                .filter(cr -> ((CompanyRep) cr).getStatus() == AccountStatus.PENDING_APPROVAL)
                .collect(Collectors.toList());
    }
    /**
     * Approves a company representative account.
     * @param repID Account ID of the representative to approve.
     * @return Status message on approval result.
     */
    public String approveRep(String repID){
        UserAccount acc = userRepo.get(repID);
        if (acc == null || !"COMPANY_REP".equals(acc.getRole())) return "Not a company rep";
        CompanyRep rep = (CompanyRep) acc;

        if (rep.getStatus() != AccountStatus.PENDING_APPROVAL)
            return "Cannot edit this account!";

        rep.setStatus(AccountStatus.APPROVED);
        return "Approved";
    }
    /**
     * Rejects a company representative account.
     * @param repID Account ID of the representative to reject.
     * @return Status message on rejection result.
     */
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
