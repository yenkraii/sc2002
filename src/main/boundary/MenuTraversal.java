package src.main.boundary;

import src.main.control.*;

import src.main.control.IPMSApplication;

// should only be interacting with control classes
// boundary class of ConsoleUI cannot directly interact with entity classes

public class MenuTraversal implements IHandleIO{

    private IMenuActions mainSystem;

    MenuTraversal(){
        mainSystem = new IPMSApplication();
    }
    
    public void start(){
        mainSystem.setup();
        int chosenOpt = -1;

        while(true){
            
            if(!(mainSystem.isLoggedOn())){
                displayMenu(new String[] {"Login"});
                chosenOpt = getMenuInput(1);
                if(chosenOpt == -1) break;
                if(!mainSystem.login(getStringArrayInput(new String[] {"UserID", "Password"}))){ 
                    // didn't log in correctly
                    System.out.println("Wrong userID or Password! Try again.");
                    continue;
                }
                
                System.out.println("\nSucessfully logged in!\n\n");

            } else {

                if(chosenOpt == -1){ // declared intention to logged off already
                    mainSystem.logout(); 
                    System.out.println("Logged out successfully!");
                    continue;
                }

                switch (mainSystem.accountRole()) {
                    case "STUDENT": 
                        chosenOpt = studentMenu(); break;
                    case "CENTER_STAFF": 
                        chosenOpt = staffMenu(); break;
                    case "COMPANY_REP":
                        chosenOpt = companyMenu(); break;
                    default: // error 
                        System.out.println("Error faced!");
                        break;
                }
            }
        }



        mainSystem.shutdown();
        consoleScanner.close();
        System.out.println("InternSys exited!");
    }
    
    public int studentMenu(){
        final String[] lvl1StudentChoices = new String[] 
            {"View Intern Listing", "View Applications", "Export All", "Change Password"};
        
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
            case 4:
                // change password
                mainSystem.changePassword(getStringArrayInput(new String[] {"New Password"}));
                return -1;
            case -1:
                // log out
                return -1;
        }
        return 0;
    }

    public int companyMenu(){
        final String[] lvl1CompanyChoices = new String[] 
            {"View Intern Posting", "View Applications", "Export All", "Change Password"};
        
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
            case 4:
                // change password
                mainSystem.changePassword(getStringArrayInput(new String[] {"New Password"}));
                return -1;
            case -1:
                // log out
                return -1;
        }
        return 0;
    }
    
    public int staffMenu(){
        final String[] lvl1StaffChoices = new String[] 
            {"View Opportunities", "View Accounts", "Export All", "Change Password"};
        
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
            case 4:
                // change password
                mainSystem.changePassword(getStringArrayInput(new String[] {"New Password"}));
                return -1;
            case -1:
                // log out
                return -1;
        }
        return 0;
    }
}
