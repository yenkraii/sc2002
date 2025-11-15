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

    public DataLoader(UserRepository userRepo, 
                     OpportunityRepository oppRepo,
                     ApplicationRepository appRepo) {
        this.userRepository = userRepo;
        this.opportunityRepository = oppRepo;
        this.applicationRepository = appRepo;
    }

    public void loadInitialData() {
        System.out.println("Loading initial data from CSV files...");

        // Replace with uploaded file paths
        loadStudents("/mnt/data/sample_student_list.csv");
        loadCareerStaff("/mnt/data/sample_staff_list.csv");
        loadCompanyReps("/mnt/data/sample_company_representative_list.csv");

        loadSampleOpportunities();

        System.out.println("Data loading complete!");
    }

    private void loadStudents(String filename) {
        System.out.println("\n📖 Loading students from: " + filename);
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File not found: " + filename);
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

                if (lineNumber == 1 && (line.toLowerCase().contains("student") || line.toLowerCase().contains("name"))) {
                    System.out.println("Detected header row, skipping...");
                    continue;
                }

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
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void processStudentLine(String line, int lineNumber) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");
        if (parts.length < 5) throw new IllegalArgumentException("Insufficient fields");

        String userID = parts[0];
        String name = parts[1];
        String password = parts[2];
        int yearOfStudy = Integer.parseInt(parts[3]);
        String major = parts[4];

        Student student = new Student(userID, name, password, yearOfStudy, major);
        userRepository.save(student);
    }

    private void loadCareerStaff(String filename) {
        System.out.println("\n📖 Loading career staff from: " + filename);
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File not found: " + filename);
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

                if (lineNumber == 1 && line.toLowerCase().contains("staff")) {
                    System.out.println("   Detected header row, skipping...");
                    continue;
                }

                try {
                    processStaffLine(line, lineNumber);
                    count++;
                } catch (Exception e) {
                    System.err.println("Error on line " + lineNumber + ": " + e.getMessage());
                }
            }
            reader.close();
            System.out.println("Successfully loaded " + count + " staff members");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void processStaffLine(String line, int lineNumber) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");
        CareerStaff staff = new CareerStaff(parts[0], parts[1], parts[2], parts[3]);
        userRepository.save(staff);
    }

    private void loadCompanyReps(String filename) {
        System.out.println("\n📖 Loading company representatives from: " + filename);
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File not found: " + filename);
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

                if (lineNumber == 1 && line.toLowerCase().contains("company")) {
                    System.out.println("   Detected header row, skipping...");
                    continue;
                }

                try {
                    processCompanyRepLine(line, lineNumber);
                    count++;
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
            reader.close();
            System.out.println("Loaded " + count + " company reps");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void processCompanyRepLine(String line, int lineNumber) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");

        CompanyRep rep = new CompanyRep(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        rep.setStatus(AccountStatus.APPROVED);
        userRepository.save(rep);
    }

    private void createSampleCompanyReps() {
        CompanyRep rep = new CompanyRep("sample@example.com", "Sample Rep", "password", "SampleCorp", "HR", "Manager");
        rep.setStatus(AccountStatus.APPROVED);
        userRepository.save(rep);
    }

    private void loadSampleOpportunities() {
        // unchanged
    }
}
