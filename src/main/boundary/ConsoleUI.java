package src.main.boundary;

import java.util.Scanner;
import src.main.control.*; 
// should only be interacting with control classes
// boundary class of ConsoleUI cannot directly interact with entity classes
import src.main.entity.UserAccount;

public class ConsoleUI {
    
    private static String[] loginOptions = {"Login", "Logout"};
    private static Scanner consoleScanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        int chosenOpt = -1;
        UserAccount currUser = null;

        do{
            if(currUser == null){ // no user login
                // create separate login method
                chosenOpt = getMenuInput(4);
                continue;
            }
            // user is logged in

            


        } while (chosenOpt != -1);
        consoleScanner.close();
        showMessage("InternSys exited!");
    }
    
    public static void displayMenu(String[] optionsToShow){
        showMessage(new String(new char[20]).replace("\0", "-"));
        
        for(int i = 0; i < optionsToShow.length; i++){
            System.out.printf("%d : %s \n", i+1, optionsToShow[i]);
        }
        showMessage("x : Exit System");
        showMessage(new String(new char[20]).replace("\0", "-"));
    }

    public static int getMenuInput(int maxRange){
        int result = -1; String errorMsg = ""; String exitOption = "";
        while(true){
            showMessage("Please enter your choice: ");
            if(consoleScanner.hasNextInt()){
                result = consoleScanner.nextInt();
                if ((result > 0) && (result <= maxRange)){
                    return result;
                }
                errorMsg = "Choice not listed! Please enter an option in the menu shown. ";
            } else { // user entered a string; need to check if its the "x" option
                exitOption = consoleScanner.next();
                if(exitOption.equals("x")) return -1;
                errorMsg = "Please either enter a number from 1 to " + maxRange + " or x to exit.";
            }
            showMessage(errorMsg);
            consoleScanner.nextLine();
        }
    }

    public static void showMessage(String msg){
        System.out.println(msg);
    }

}
