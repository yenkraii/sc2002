package src.main.control;

import src.main.entity.*;
import src.repo.*;
import src.repo.in_memory.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class DataLoader implements IMainRepository, ILoadSampleData{
    protected IRepository userRepository;
    protected IRepository opportunityRepository;
    protected IRepository applicationRepository;
    
    // Default password for all users loaded from CSV
    private static final String DEFAULT_PASSWORD = "password";
    
    // public DataLoader(UserRepository userRepo, 
    //                  OpportunityRepository oppRepo,
    //                  ApplicationRepository appRepo) {
    //     this.userRepository = userRepo;
    //     this.opportunityRepository = oppRepo;
    //     this.applicationRepository = appRepo;
    // }


    public DataLoader(){
        this.userRepository = new InMemoryUserRepository();
        this.opportunityRepository = new InMemoryOpportunityRepository();
        this.applicationRepository = new InMemoryApplicationRepository();
    }



    
    public void loadInitialData() {
        System.out.println("Loading initial data from CSV files...");
        
        // Load from CSV files
        loadStudents("data/sample_student_list.csv");
        loadCareerStaff("data/sample_staff_list.csv");
        loadCompanyReps("data/sample_company_representative_list.csv");
        
        // Optionally create sample opportunities for testing
        loadSampleOpportunities();
        
        System.out.println("Data loading complete!");
    }
    
    public void loadStudents(String filename) {
        System.out.println("\nLoading students from: " + filename);
        
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File not found: " + filename);
                System.out.println("Please ensure the CSV file is in the 'data' folder");
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int count = 0;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }
                
                // Skip header row
                if (lineNumber == 1) {
                    System.out.println("Detected header row, skipping...");
                    continue;
                }
                
                // Process student data
                try {
                    processStudentLine(line, lineNumber);
                    count++;
                } catch (Exception e) {
                    System.err.println("Error on line " + lineNumber + ": " + e.getMessage());
                    System.err.println("Line content: " + line);
                }
            }
            
            reader.close();
            System.out.println("Successfully loaded " + count + " students");
            
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            System.err.println("Please check the file path and try again");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private void processStudentLine(String line, int lineNumber) {
        // Split by comma, but handle quoted fields if present
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        
        // Clean each field (remove quotes, trim whitespace)
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");
        }
        
        // Validate minimum required fields
        if (parts.length < 5) {
            throw new IllegalArgumentException(
                "Insufficient fields. Expected: StudentID,Name,Major,Year,Email. Got " + parts.length + " fields"
            );
        }
        
        // Extract fields (matching your CSV: StudentID,Name,Major,Year,Email)
        String studentID = parts[0].trim();
        String name = parts[1].trim();
        String major = parts[2].trim();
        String yearStr = parts[3].trim();
        String email = parts[4].trim();
        
        // Validate Student ID format
        if (!studentID.matches("U\\d{7}[A-Z]")) {
            throw new IllegalArgumentException(
                "Invalid Student ID format: " + studentID + ". Expected format: U1234567A"
            );
        }
        
        // Parse year of study
        int yearOfStudy;
        try {
            yearOfStudy = Integer.parseInt(yearStr);
            if (yearOfStudy < 1 || yearOfStudy > 4) {
                throw new IllegalArgumentException(
                    "Year of study must be 1-4. Got: " + yearOfStudy
                );
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid year of study: " + yearStr + ". Must be a number 1-4"
            );
        }
        
        // Create and save student (using studentID as userID, default password)
        Student student = new Student(studentID, name, DEFAULT_PASSWORD, yearOfStudy, major);
        userRepository.save(student);
        
            System.out.println("   [CHECK] Loaded: " + studentID + " - " + name + " (Year " + yearOfStudy + ", " + major + ")");
    }
    
    public void loadCareerStaff(String filename) {
        System.out.println("\nLoading career staff from: " + filename);
        
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File not found: " + filename);
                System.out.println("Please ensure the CSV file is in the 'data' folder");
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int count = 0;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }
                
                // Skip header row
                if (lineNumber == 1) {
                    System.out.println("Detected header row, skipping...");
                    continue;
                }
                
                // Process staff data
                try {
                    processStaffLine(line, lineNumber);
                    count++;
                } catch (Exception e) {
                    System.err.println("Error on line " + lineNumber + ": " + e.getMessage());
                    System.err.println("Line content: " + line);
                }
            }
            
            reader.close();
            System.out.println("Successfully loaded " + count + " staff members");
            
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private void processStaffLine(String line, int lineNumber) {
        // Split and clean
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");
        }
        
        // Validate fields
        if (parts.length < 5) {
            throw new IllegalArgumentException(
                "Insufficient fields. Expected: StaffID,Name,Role,Department,Email. Got " + parts.length + " fields"
            );
        }
        
        // Extract fields (matching your CSV: StaffID,Name,Role,Department,Email)
        String staffID = parts[0].trim();
        String name = parts[1].trim();
        String role = parts[2].trim();
        String department = parts[3].trim();
        String email = parts[4].trim();
        
        // Create and save staff (using staffID as userID, default password)
        CareerStaff staff = new CareerStaff(staffID, name, DEFAULT_PASSWORD, department);
        userRepository.save(staff);
        
        System.out.println("Loaded: " + staffID + " - " + name + " (" + department + ")");
    }
    
    public void loadCompanyReps(String filename) {
        System.out.println("\nLoading company representatives from: " + filename);
        
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File not found: " + filename);
                System.out.println("Creating sample company representatives instead...");
                createSampleCompanyReps();
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int count = 0;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                if (line.isEmpty()) continue;
                
                // Skip header row
                if (lineNumber == 1) {
                    System.out.println("Detected header row, skipping...");
                    continue;
                }
                
                try {
                    processCompanyRepLine(line, lineNumber);
                    count++;
                } catch (Exception e) {
                    System.err.println("Error on line " + lineNumber + ": " + e.getMessage());
                    System.err.println("Line content: " + line);
                }
            }
            
            reader.close();
            
            if (count == 0) {
                System.out.println("No company representatives found in CSV");
                System.out.println("Creating sample company representatives instead...");
                createSampleCompanyReps();
            } else {
                System.out.println("Successfully loaded " + count + " company representatives");
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            createSampleCompanyReps();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private void processCompanyRepLine(String line, int lineNumber) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");
        }
        
        if (parts.length < 7) {
            throw new IllegalArgumentException(
                "Insufficient fields. Expected: CompanyRepID,Name,CompanyName,Department,Position,Email,Status. Got " + parts.length
            );
        }
        
        // Extract fields (matching your CSV)
        String companyRepID = parts[0].trim();
        String name = parts[1].trim();
        String companyName = parts[2].trim();
        String department = parts[3].trim();
        String position = parts[4].trim();
        String email = parts[5].trim();
        String statusStr = parts[6].trim();
        
        // Validate email format
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must be valid: " + email);
        }
        
        // Use email as userID (more standard for company reps)
        CompanyRep rep = new CompanyRep(email, name, DEFAULT_PASSWORD, companyName, department, position);
        
        // Set status based on CSV (default to APPROVED if empty or invalid)
        if (statusStr.equalsIgnoreCase("APPROVED")) {
            rep.setStatus(AccountStatus.APPROVED);
        } else if (statusStr.equalsIgnoreCase("PENDING")) {
            rep.setStatus(AccountStatus.PENDING);
        } else {
            rep.setStatus(AccountStatus.APPROVED); // Default for testing
        }
        
        userRepository.save(rep);
        
        System.out.println("Loaded: " + email + " - " + name + " (" + companyName + ")");
    }
    
    
    /**
     * Display loading summary
     * Shows statistics of loaded data
     */
    public void displayLoadingSummary() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DATA LOADING SUMMARY");
        System.out.println("=".repeat(50));
        
        // Count each type
        int studentCount = userRepository.findAllStudents().size();
        int staffCount = userRepository.findAllStaff().size();
        int companyRepCount = userRepository.findPendingCompanyReps().size() + 
                              userRepository.findAll().stream()
                                  .filter(u -> u instanceof CompanyRep && u.getStatus() == AccountStatus.APPROVED)
                                  .count();
        int opportunityCount = opportunityRepository.findAll().size();
        
        System.out.println("Students loaded:           " + studentCount);
        System.out.println("Staff members loaded:      " + staffCount);
        System.out.println("Company reps loaded:       " + companyRepCount);
        System.out.println("Opportunities created:     " + opportunityCount);
        System.out.println("\nDefault password for all users: " + DEFAULT_PASSWORD);
        System.out.println("=".repeat(50));
        System.out.println("System ready! You can now login.\n");
    }
}
