package src.main.boundary;

import java.util.List;
import java.util.Scanner;

public interface IHandleIO {
    static Scanner consoleScanner = new Scanner(System.in);

    default public void displayMenu(String[] optionsToShow){
        System.out.println(new String(new char[20]).replace("\0", "-"));
        
        for(int i = 0; i < optionsToShow.length; i++){
            System.out.printf("%d : %s \n", i+1, optionsToShow[i]);
        }
        System.out.println("x : Back / Exit");
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

    default public String getStringInput(String instruction){
        consoleScanner.nextLine();
        System.out.println(instruction);
        return consoleScanner.nextLine().trim();
    }

    default public void showMessage(String msg){
        System.out.println(msg);
    }

    default public int showRepoData(List<String> data){
        if (data.isEmpty() || data == null) {
            System.out.println("No data in the repo!");
            return 0;
        }
        for(String line : data){
            System.out.println(line);
        }
        return data.size();
    }

}
