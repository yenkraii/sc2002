package src.main.control;

import src.main.control.entity.*;
import src.main.control.repository.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class DataLoader {
    private UserRepository userRepository;
    private OpportunityRepository opportunityRepository;
    private ApplicationRepository applicationRepository;
    
    // Default password for all users loaded from CSV
    private static final String DEFAULT_PASSWORD = "password";
    
    public DataLoader(UserRepository userRepo, 
                     OpportunityRepository oppRepo,
                     ApplicationRepository appRepo) {
        this.userRepository = userRepo;
        this.opportunityRepository = oppRepo;
        this.applicationRepository = appRepo;
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
    
    private void loadStudents(String filename) {
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
    
    private void loadCareerStaff(String filename) {
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
    
    private void loadCompanyReps(String filename) {
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
     * Create sample company representatives for testing
     * Only called if CSV file is empty or not provided
     */
    private void createSampleCompanyReps() {
        System.out.println("Creating 3 sample company representatives...");
        
        CompanyRep[] reps = {
            new CompanyRep("hr@techcorp.com", "Sarah Williams", DEFAULT_PASSWORD, 
                          "TechCorp International", "Human Resources", "HR Manager"),
            new CompanyRep("recruit@innovate.sg", "Michael Chen", DEFAULT_PASSWORD,
                          "Innovate Solutions Pte Ltd", "Recruitment", "Talent Acquisition Lead"),
            new CompanyRep("careers@datatech.com.sg", "Jennifer Tan", DEFAULT_PASSWORD,
                          "DataTech Analytics", "Career Development", "Recruitment Specialist")
        };
        
        for (CompanyRep rep : reps) {
            rep.setStatus(AccountStatus.APPROVED); // Pre-approve for testing
            userRepository.save(rep);
            System.out.println("Created: " + rep.getUserID() + " - " + rep.getName());
        }
    }
    
    /**
     * Create sample internship opportunities for testing
     * Allows students to test the system immediately
     */
    private void loadSampleOpportunities() {
        System.out.println("\nCreating sample internship opportunities for testing...");
        
        LocalDate today = LocalDate.now();
        int count = 0;
        
        // Check if we have any company reps
        CompanyRep rep1 = (CompanyRep) userRepository.findByUserID("hr@techcorp.com");
        if (rep1 != null && rep1.getStatus() == AccountStatus.APPROVED) {
            // Software Engineering Internship - CSC, INTERMEDIATE
            InternshipOpportunity opp1 = new InternshipOpportunity(
                "Software Engineering Intern",
                "Work on cutting-edge web applications using React, Node.js, and cloud technologies. " +
                "You'll be part of our development team building scalable enterprise solutions.",
                InternshipLevel.INTERMEDIATE,
                "CSC",
                today.minusDays(10),
                today.plusMonths(2),
                rep1.getCompanyName(),
                rep1.getUserID(),
                5
            );
            opp1.setStatus(InternshipStatus.APPROVED);
            opp1.setVisibility(true);
            opportunityRepository.save(opp1);
            count++;
            
            // Data Science Internship - CSC, ADVANCED
            InternshipOpportunity opp2 = new InternshipOpportunity(
                "Data Science & AI Intern",
                "Analyze large datasets, build predictive models using machine learning, and develop " +
                "AI-powered features for our products. Experience with Python, TensorFlow preferred.",
                InternshipLevel.ADVANCED,
                "CSC",
                today.minusDays(5),
                today.plusMonths(1),
                rep1.getCompanyName(),
                rep1.getUserID(),
                3
            );
            opp2.setStatus(InternshipStatus.APPROVED);
            opp2.setVisibility(true);
            opportunityRepository.save(opp2);
            count++;
        }
        
        CompanyRep rep2 = (CompanyRep) userRepository.findByUserID("recruit@innovate.sg");
        if (rep2 != null && rep2.getStatus() == AccountStatus.APPROVED) {
            // Electronics Engineering - EEE, INTERMEDIATE
            InternshipOpportunity opp3 = new InternshipOpportunity(
                "Electronics Engineering Intern",
                "Design and test embedded systems for IoT devices. Work with microcontrollers, sensors, " +
                "and wireless communication protocols in our hardware lab.",
                InternshipLevel.INTERMEDIATE,
                "EEE",
                today.minusDays(15),
                today.plusMonths(3),
                rep2.getCompanyName(),
                rep2.getUserID(),
                4
            );
            opp3.setStatus(InternshipStatus.APPROVED);
            opp3.setVisibility(true);
            opportunityRepository.save(opp3);
            count++;
            
            // Junior Electronics Intern - EEE, BASIC
            InternshipOpportunity opp4 = new InternshipOpportunity(
                "Junior Electronics Intern",
                "Learn the basics of circuit design, PCB layout, and testing. Perfect for students " +
                "starting their journey in electronics engineering. Training provided.",
                InternshipLevel.BASIC,
                "EEE",
                today,
                today.plusMonths(2),
                rep2.getCompanyName(),
                rep2.getUserID(),
                6
            );
            opp4.setStatus(InternshipStatus.APPROVED);
            opp4.setVisibility(true);
            opportunityRepository.save(opp4);
            count++;
        }
        
        CompanyRep rep3 = (CompanyRep) userRepository.findByUserID("careers@datatech.com.sg");
        if (rep3 != null && rep3.getStatus() == AccountStatus.APPROVED) {
            // Mechanical Design Intern - MAE, INTERMEDIATE
            InternshipOpportunity opp5 = new InternshipOpportunity(
                "Mechanical Design Intern",
                "CAD modeling using SolidWorks, prototype development using 3D printing, and participation " +
                "in design reviews. Work on real products from concept to production.",
                InternshipLevel.INTERMEDIATE,
                "MAE",
                today.minusDays(7),
                today.plusMonths(2),
                rep3.getCompanyName(),
                rep3.getUserID(),
                3
            );
            opp5.setStatus(InternshipStatus.APPROVED);
            opp5.setVisibility(true);
            opportunityRepository.save(opp5);
            count++;
        }
        
        if (count > 0) {
            System.out.println("Created " + count + " sample internship opportunities");
        } else {
            System.out.println("No approved company reps found, skipping sample opportunities");
        }
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
