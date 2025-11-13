package src.main.boundary;

import src.main.control.*;

// should only be interacting with control classes
// boundary class of ConsoleUI cannot directly interact with entity classes

public class ConsoleUI implements IHandleIO{

    private static AuthController authController;

    ConsoleUI(AuthController ac){
        authController = ac;
    }
    
    public void start(String[] args) {
        int chosenOpt = -1;
        do{
            // menu traversal
            if((authController.getCurrentUser()  == null)){
                // perform login
            } else {
                // switch statement
                switch (authController.getCurrentUser().getRole()) {
                    case "STUDENT": 
                        studentMenu(); break;
                    case "CENTER_STAFF": 
                        staffMenu(); break;
                    case "COMPANY_REP":
                        companyMenu(); break;
                    default: // error 
                        System.out.println("Error faced!");
                        break;
                }
            }

        } while (chosenOpt != -1);
        consoleScanner.close();
        System.out.println("InternSys exited!");
    }
    
    public void studentMenu(){
        final String[] lvl1StudentChoices = new String[] 
            {"View Intern Listing", "View Applications", "Export All", "Logoff"};
        
        final String[] lvl2StudentChoices = new String[] 
            {"Select", "Back"};
        
        displayMenu(lvl1StudentChoices);
        switch (getMenuInput(lvl1StudentChoices.length)) {
            case 1:
                // view intern listing
                break;
            case 2:
                // view apps
                break;
            case 3:
                // export all
                break;
            case -1:
                // log out
                break;
        }
    }

    public void companyMenu(){
        final String[] lvl1CompanyChoices = new String[] 
            {"View Intern Posting", "View Applications", "Export All", "Logoff"};
        
        final String[] lvl2CompanyChoices = new String[] 
            {"Select", "Back"};
        
        displayMenu(lvl1CompanyChoices);
        switch (getMenuInput(lvl1CompanyChoices.length)) {
            case 1:
                // view intern listing
                break;
            case 2:
                // view apps
                break;
            case 3:
                // export all
                break;
            case -1:
                // log out
                break;
        }
    }
    
    public void staffMenu(){
        final String[] lvl1StaffChoices = new String[] 
            {"View Opportunities", "View Accounts", "Export All", "Logoff"};
        
        final String[] lvl2StaffChoices = new String[] 
            {"Select", "Back"};
        
        displayMenu(lvl1StaffChoices);
        switch (getMenuInput(lvl1StaffChoices.length)) {
            case 1:
                // view Opportunities
                break;
            case 2:
                // view accounts
                break;
            case 3:
                // export all
                break;
            case -1:
                // log out
                break;
        }
    }
}
