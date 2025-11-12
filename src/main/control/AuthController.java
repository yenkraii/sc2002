package src.main.control;

import src.main.entity.CompanyRep;
import src.main.entity.UserAccount;
import src.repo.UserRepository;

// controller for authentication operations
// demonstrates SRP (handles only authentication)
public class AuthController {
    private UserRepository userRepository;
    private UserAccount currentUser;

    // constructor for AuthController
    // param userRepository: user repository instance
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public boolean registerCompanyRep(String userID, String name, String password, String companyName, String department, String position) {
        if (userRepository.findByUserID(userID) != null) {
            return false; // user already exists
        }

        CompanyRep newRep = new CompanyRep(userID, name, password, companyName, department, position);
        userRepository.save(newRep);
        return true;
    }

    // log in user to system
    // param userID: user ID
    // param password: password
    // return UserAccount if successful, null otherwise
    public UserAccount login(String userID, String password) {
        UserAccount user = userRepository.findByUserID(userID);
        if (user == null) {
            return null;
        }

        if (user.login(password)) {
            this.currentUser = user;
            return user;
        }

        return null;
    }

    // log out current user
    public void logout() {
        if (currentUser != null) {
            currentUser.logout();
            currentUser = null;
        }
    }

    // change password for user
    // param UserAccount: user account
    // param oldPassword: old password
    // param newPassword: new password
    // return true if successful
    public boolean changePassword(UserAccount userAccount, String oldPassword, String newPassword) {
        if (userAccount.changePassword(oldPassword, newPassword)) {
            userRepository.save(userAccount);
            return true;
        }
        return false;
    }

    // get current logged in user
    // return current user account
    public UserAccount getCurrentUser() {
        return currentUser;
    }

}
