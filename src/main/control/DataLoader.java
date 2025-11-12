package src.main.control;

import src.main.control.entity.*;
import src.main.control.repository.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

// Utility class for loading initial data from files
public class DataLoader {
  private UserRepository userRepository;
  private OpportunityRepository opportunityRepository;
  private ApplicationRepository applicationRepository;

  public DataLoader(UserRepository userRepo, OpportunityRepository oppRepo, ApplicationRepository appRepo) {
    this.userRepository = userRepo;
    this.opportunityRepository = oppRepo;
    this.applicationRepository = appRepo;
  }

  // Load intial data from files
  public void loadInitialData() {
    System.out.println("Loading initial data...");

    // Load students 
    loadStudents("data/students.txt");

    // Load center staff
    loadCareerStaff("data/staff.txt");

    // Load sample company reps (for testing)
    loadSampleCompanyReps();
        
    // Load sample opportunities (for testing)
    loadSampleOpportunities();
        
    System.out.println("Data loading complete!");
  }

  // Load students from file
  private void loadStudents(String filename) {
    try {
      File file = new File(filename);
      if (!file.exists()) {
        System.out.println("Student file not found. Creating sample data...");
        createSampleStudents();
        return;
      }

      Scanner scanner = new Scanner(file);
      int count = 0;
            
      // Skip header if exists
      if (scanner.hasNextLine()) {
        String header = scanner.nextLine();
        if (!header.startsWith("U")) {
          // Was a header, continue
        } else {
          // Process this line
          processStudentLine(header);
          count++;
        }
      }
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) continue;
        processStudentLine(line);
        count++;
      }
      
      scanner.close();
      System.out.println("✅ Loaded " + count + " students");
    
    } catch (FileNotFoundException e) {
      System.out.println("Error loading students: " + e.getMessage());
            createSampleStudents();
        }
    }

      

    






  
}
