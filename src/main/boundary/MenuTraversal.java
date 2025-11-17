package src.main.boundary;

import src.main.control.IPMSApplication;
import src.main.control.IMenuActions;


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
                displayMenu(new String[] {"Login", "Register Company Rep"});
                chosenOpt = getMenuInput(2);
                switch(chosenOpt){
                    case -1: break;
                    case 2 : 
                        mainSystem.registerCompanyRep(getStringArrayInput(
                            new String[] {"UserID", "Name", "Password", "Email", "Company Name", "Department", "Position"})); 
                        break;
                    case 1 :     
                        System.out.println(mainSystem.login(getStringArrayInput(new String[] {"UserID", "Password"})));
                        break;
                }

                if (chosenOpt == -1) break;

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
        final String[] StudentChoices = new String[] 
            {"View Intern Listing", 
            "Apply for Intern",
            "View Applications", 
            "Accept Placement",
            "Request Withdrawal",
            "Set Filters",
            "Change Password"};
        
        displayMenu(StudentChoices);
        switch (getMenuInput(StudentChoices.length)) {
            case 1: // view listing
                showRepoData(mainSystem.viewOpp());
                break;
            case 2: // apply for intern
            showMessage(
                mainSystem.applyIntern(
                    getStringInput("Enter InternID:"))
                );
                break;
            case 3: // view applications
                showRepoData(
                    mainSystem.viewApp(getStringInput("Enter InternID:"))
                );
                break;
            case 4: // accept placement
                showMessage(
                    mainSystem.acceptPlacement(
                        getStringInput("Enter InternID:")
                        ));
                break;
            case 5: // request withdrawal
                showMessage(
                    mainSystem.requestWithdrawal(
                        getStringInput("Enter InternID:"), 
                        getStringInput("State reason behind withdrawal:"))
                );
                break;
            case 6: // set filters
                mainSystem.setFilters(
                    getStringArrayInput(
                        new String[] {
                            "Major",
                            "Company Name",
                            "Level (BASIC, INTERMEDIATE, ADVANCED)",
                            "Status (PENDING, FILLED)"
                        }
                    )
                );
                break;
            case 7: // change password
                mainSystem.changePassword(getStringArrayInput(new String[] {"New Password"}));
                return -1;
            case -1: // log out
                return -1;
        }
        return 0;
    }

    public int companyMenu(){
        final String[] lvl1CompanyChoices = new String[] 
            {"Create Opportunity", 
            "View Opportunities", 
            "Edit Opportunity", 
            "Delete Opportunity", 
            "Toggle Visibility", 
            "View Applications", 
            "Approve/Reject Application", 
            "Change Password"};
        
        displayMenu(lvl1CompanyChoices);
        switch (getMenuInput(lvl1CompanyChoices.length)) {
            case 1: // create opp
                mainSystem.createOpp( 
                    getStringArrayInput(new String[]{
                        "Internship Title",
                        "Description",
                        "Level (BASIC, INTERMEDIATE, ADVANCED)",
                        "Preferred Major",
                        "Opening date",
                        "Closing Date",
                        "Number of Slots"
                    })
                );
                break;
            case 2: // view opps
                showRepoData(mainSystem.viewOpp());
                break;
            case 3: // edit opp
                mainSystem.editOpp( 
                    getStringArrayInput(new String[]{
                        "Intern Opp ID",
                        "Internship Title",
                        "Description",
                        "Level (BASIC, INTERMEDIATE, ADVANCED)",
                        "Preferred Major",
                        "Opening date",
                        "Closing Date",
                        "Number of Slots"
                    })
                );
                break;
            case 4: // delete opp
                showMessage(
                    mainSystem.deleteOpp(getStringInput("Enter InternID: "))
                );
                break;
            case 5: // toggle visibility
                showMessage(
                    mainSystem.toggleVisibility(getStringInput("Enter InternID: "))
                );
                break;
            case 6: // view applications
                showRepoData(
                    mainSystem.viewApp(getStringInput("Enter InternID:"))
                );
                break;
            case 7: // approve/reject app
                    showMessage(
                        mainSystem.processApp(
                            getStringInput("Enter InternID:"),
                            getStringInput("Enter Student ID:"),
                            getStringInput("Approve or reject?:")
                        )
                    );
                break;
            case 8:
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
            {"Approve/Reject Company Representatives", 
            "Approve/Reject Internship Opportunities", 
            "Generate Reports", 
            "Delete Opportunity", 
            "View All Opportunities", 
            "Set Report Filters", 
            "Approve/Reject Withdrawal", 
            "Change Password"};
        
        displayMenu(lvl1StaffChoices);
        switch (getMenuInput(lvl1StaffChoices.length)) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4: // delete opp
                showMessage(
                    mainSystem.deleteOpp(getStringInput("Enter InternID: "))
                );
                break;
            case 5: // view opp
                showRepoData(mainSystem.viewOpp());
                break;
            case 6:// set filters
                mainSystem.setFilters(
                    getStringArrayInput(
                        new String[] {
                            "Major",
                            "Company Name",
                            "Level (BASIC, INTERMEDIATE, ADVANCED)",
                            "Status (PENDING, FILLED, APPROVED, REJECTED)"
                        }
                    )
                );
                break;
            case 7: // approve / reject
                break;
            case 8:
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
