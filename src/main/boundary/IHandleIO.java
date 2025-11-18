package src.main.boundary;

import java.util.List;
import java.util.Scanner;

/**
 * Interface for anything I/O related, specifically for the console
 */

public interface IHandleIO {
    static Scanner consoleScanner = new Scanner(System.in);

    /**
     * Displays a menu on the console based on the provided options.
     * Lists each option and provides an 'x' option for exit.
     * @param optionsToShow An array of menu options to display.
     */

    default public void displayMenu(String[] optionsToShow){
        System.out.println(new String(new char[20]).replace("\0", "-"));
        
        for(int i = 0; i < optionsToShow.length; i++){
            System.out.printf("%d : %s \n", i+1, optionsToShow[i]);
        }
        System.out.println("x : Back / Exit");
        System.out.println(new String(new char[20]).replace("\0", "-"));
    }

    /**
     * Prompts the user to input a choice based on menu options.
     * Accepts numeric input within the specified range or 'x' to exit.
     * @param maxRange The maximum numerical choice available.
     * @return The user’s choice as an integer, or -1 if 'x' is selected.
     */
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
    /**
     * Prompts the user to enter multiple input strings for the specified fields.
     * Displays instructions for user entry and collects responses for each field.
     * @param fields Array of field names requiring user input.
     * @return An array containing the user's inputs for each field.
     */
    default public String[] getStringArrayInput(String[] fields){
        consoleScanner.nextLine();
        int noFieldsExpected = fields.length;
        if (noFieldsExpected < 1) return null;
        System.out.println("Please key in " + noFieldsExpected + " entries.");
        String[] userInput = new String[noFieldsExpected];
        for(int i = 0; i < noFieldsExpected; i ++){
            System.out.printf("%s: ", fields[i]);
            userInput[i] = consoleScanner.nextLine().trim();
        }
        return userInput;
    }
    /**
     * Prompts the user for a single string input with a given instruction.
     * @param instruction Text to display before receiving user input.
     * @return The user's input as a trimmed string.
     */
    default public String getStringInput(String instruction){
        consoleScanner.nextLine();
        System.out.println(instruction);
        return consoleScanner.nextLine().trim();
    }
    /**
     * Displays a message to the console.
     * @param msg The message to be shown.
     */
    default public void showMessage(String msg){
        System.out.println(msg);
    }
    /**
     * Displays a list of repository data lines to the console.
     * If the data is empty or null, informs the user that the repository is empty.
     * @param data A list of strings representing the repository data.
     * @return The number of data items displayed, or 0 if there is no data.
     */
    default public int showRepoData(List<String> data){
        if (data == null || data.isEmpty()) {
            System.out.println("No data in the repo!");
            return 0;
        }
        for(String line : data){
            System.out.println(line);
        }
        return data.size();
    }

}
