package src.main.boundary;

import java.util.Scanner;
import src.main.control.*; 
// should only be interacting with control classes
// boundary class of ConsoleUI cannot directly interact with entity classes
import src.main.entity.UserAccount;

public class ConsoleUI {
    
    private static String[] loginOptions = {"Login", "Logout"};
    
    public static void main(String[] args) {
        int chosenOpt = -1;
        UserAccount currUser = null;

        do{
            if(currUser == null){ // no user login
                displayMenu(loginOptions);
                chosenOpt = getInput();
            }


        } while (chosenOpt != -1);
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

    public static int getInput(){
        // TODO: VALIDATE USER INPUT
        final Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    public static void showMessage(String msg){
        System.out.println(msg);
    }

}
