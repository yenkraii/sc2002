package src.main.boundary;

import src.main.control.*;
import src.main.entity.Student;
import src.main.entity.UserAccount; 

// should only be interacting with control classes
// boundary class of ConsoleUI cannot directly interact with entity classes

public class ConsoleUI implements IHandleIO{

    //private static AuthController authController;

    //ConsoleUI(AuthController ac){
    //    authController = ac;
    //}
    
    public static void main(String[] args) {
        int chosenOpt = -1;

        UserAccount a = new Student(null, null, null, chosenOpt, null);

        do{
            // menu traversal
            //if(!(authController.getCurrentUser()  instanceof UserAccount)){
                // perform login
            //} else {
                // switch statement
                switch (a.getRole()) {
                    case "STUDENT": studentMenu(a); break;
                    case "CENTER_STAFF": staffMenu(a); break;
                    case "COMPANY_REP":companyMenu(a); break;
                    default: // error 
                        System.out.println("Error faced!");
                        break;
                }
            //}






        } while (chosenOpt != -1);
        consoleScanner.close();
        System.out.println("InternSys exited!");
    }
    
    public static void studentMenu(UserAccount account){
        System.out.println("student");
    }

    public static void companyMenu(UserAccount account){
        System.out.println("company");
    }
    
    public static void staffMenu(UserAccount account){
        System.out.println("staff");
    }

}
