package src.main.control;

import src.main.entity.*;

import java.io.*;
import java.util.ArrayList;

public class DataRepository implements IMainRepository {
    protected ArrayList<UserAccount> userRepository;
    protected ArrayList<InternshipOpportunity> opportunityRepository;
    protected ArrayList<InternshipApplication> applicationRepository;
    protected ArrayList<WithdrawalRequest> withdrawalRepository;
    
    // Default password for all users loaded from CSV
    private static final String DEFAULT_PASSWORD = "password";

    public DataRepository(){
        this.userRepository = new ArrayList<UserAccount>();
        this.opportunityRepository = new ArrayList<InternshipOpportunity>();
        this.applicationRepository = new ArrayList<InternshipApplication>();
        this.withdrawalRepository = new ArrayList<WithdrawalRequest>();
    }
    
    public void loadInitialData() {
        System.out.println("Loading initial data from CSV files...");
        
        // Load from CSV files
        loadStudents("data/sample_student_list.csv");
        loadCareerStaff("data/sample_staff_list.csv");
        loadCompanyReps("data/sample_company_representative_list.csv");
        
        // Optionally create sample opportunities for testing
        //loadSampleOpportunities();
        
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
            
            reader.readLine(); // automatically skip first header row

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) continue;
                
                String[] inputInfo = processLine(line);
                userRepository.add(new Student(inputInfo[0], 
                                                inputInfo[1], 
                                                DEFAULT_PASSWORD, 
                                                Integer.parseInt(inputInfo[2]),
                                                inputInfo[3]));
                count++;

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
            
            reader.readLine(); // automatically skip first header row

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) continue;
                
                String[] inputInfo = processLine(line);
                userRepository.add(new CenterStaff(inputInfo[0], 
                                                inputInfo[1], 
                                                DEFAULT_PASSWORD, 
                                                inputInfo[2]));
                count++;

            }
            
            reader.close();
            System.out.println("Successfully loaded " + count + " staff members");
            
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    public void loadCompanyReps(String filename) {
        System.out.println("\nLoading company representatives from: " + filename);
        
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File not found: " + filename);
                //System.out.println("Creating sample company representatives instead...");
                //createSampleCompanyReps();
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int count = 0;
            
            reader.readLine(); // automatically skip first header row

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) continue;
                
                String[] inputInfo = processLine(line);
                userRepository.add(new CompanyRep(inputInfo[0], 
                                                inputInfo[1], 
                                                DEFAULT_PASSWORD, 
                                                inputInfo[2], 
                                                inputInfo[3], 
                                                inputInfo[4]));
                count++;

            }
            
            reader.close();
            
            if (count == 0) {
                System.out.println("No company representatives found in CSV");
                //System.out.println("Creating sample company representatives instead...");
                //createSampleCompanyReps();
            } else {
                System.out.println("Successfully loaded " + count + " company representatives");
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            //createSampleCompanyReps();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public String[] processLine(String line){
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");
        }

        return parts;
    }


}
