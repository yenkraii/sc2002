package src.main.control;

import src.main.control.controller.AuthController;
import src.main.control.boundary.ConsoleUI;
import src.main.control.repository;
import src.main.control.util.DataLoader;

public class IPMSApplication {
  public static void main(String[] args){
    try {
      System.out.println("Intializing Internship Placement Management System...:);

      // Initialize repositories 
      InMemoryUserRepository userRepo = new InMemoryUserRepository();
      InMemoryOpportunityRepository oppRepo = new InMemoryOpportunityRepository();
      InMemoryApplicationRepository appRepo = new InMemoryApplicationRepository();
      InMemoryWithdrawalRequestRepository withdrawalRepo = new InMemoryWithdrawalRequestRepository();

      // Load initial data from files
      DataLoader dataLoader = new DataLoader(userRepo, oppRepo, appRepo);
      dataLoader.loadInitialData();

      // Initialize controllers
      AuthController authController = new AuthController(userRepo);

      // Start the application 
      ConsoleUI consoleUI = new ConsoleUI(authController, userRepo, oppRepo, appRepo, withdrawalRepo);
      consoleUI.displayMenu();

      System.out.println("\n Thank you for using IPMS!");
    } catch (Exception e){
      System.err.println("Fatal error starting application: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
