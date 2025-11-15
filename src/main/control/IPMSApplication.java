package src.main.control;

import src.main.control.*;

// this class faciltates interactions between controllers

public class IPMSApplication {
    private IMainRepository systemStorage;

    public void run(){
      systemStorage.loadInitialData();;
    }






    // public static void main(String[] args){
    //   try {
    //     System.out.println("Intializing Internship Placement Management System...:");

    //     // Initialize repositories 
    //     InMemoryUserRepository userRepo = new InMemoryUserRepository();
    //     InMemoryOpportunityRepository oppRepo = new InMemoryOpportunityRepository();
    //     InMemoryApplicationRepository appRepo = new InMemoryApplicationRepository();
    //     InMemoryWithdrawalRequestRepository withdrawalRepo = new InMemoryWithdrawalRequestRepository();

    //     // Load initial data from files
    //     DataLoader dataLoader = new DataLoader(userRepo, oppRepo, appRepo);
    //     dataLoader.loadInitialData();

    //     // Initialize controllers
    //     AuthController authController = new AuthController(userRepo);

    //     // Start the application 
    //     src.main.boundary.ConsoleUI consoleUI = new ConsoleUI(authController, userRepo, oppRepo, appRepo, withdrawalRepo);
    //     consoleUI.start();

    //     System.out.println("\n Thank you for using IPMS!");
    //   } catch (Exception e){
    //     System.err.println("Fatal error starting application: " + e.getMessage());
    //     e.printStackTrace();
    //   }
    // }
}
