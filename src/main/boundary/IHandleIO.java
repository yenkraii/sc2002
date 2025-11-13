package src.main.boundary;

import java.util.Scanner;

public interface IHandleIO {
    static Scanner consoleScanner = new Scanner(System.in);

    default public void displayMenu(String[] optionsToShow){
        System.out.println(new String(new char[20]).replace("\0", "-"));
        
        for(int i = 0; i < optionsToShow.length; i++){
            System.out.printf("%d : %s \n", i+1, optionsToShow[i]);
        }
        System.out.println("x : Exit System");
        System.out.println(new String(new char[20]).replace("\0", "-"));
    }

    default public int getMenuInput(int maxRange){
        int result = -1; String errorMsg = ""; String exitOption = "";
        while(true){
            System.out.println("Please enter your choice: ");
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
            System.out.println(errorMsg);
            consoleScanner.nextLine();
        }
    }
}
