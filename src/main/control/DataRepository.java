package src.main.control;

import src.main.entity.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataRepository implements IMainRepository {
    protected Map<String,UserAccount> userRepo;
    protected Map<String,InternshipOpportunity> oppRepo;
    protected Map<String,ArrayList<InternshipApplication>> appRepo;
    protected ArrayList<WithdrawalRequest> wdrRepo;
    protected Map<String,String> pwdRepo;


    public DataRepository(){
        this.userRepo = new HashMap<String,UserAccount>();
        this.oppRepo = new HashMap<String,InternshipOpportunity>();
        this.appRepo = new HashMap<String,ArrayList<InternshipApplication>>();
        this.wdrRepo = new ArrayList<WithdrawalRequest>();
        this.pwdRepo = new HashMap<String,String>();
    }
    
    public void loadInitialData() {
        System.out.println("Loading initial data from CSV files...");
        
        // Load from CSV files
        loadUserAuth("data/password_list.csv");

        loadStudents("data/student_list.csv");
        loadCareerStaff("data/staff_list.csv");
        loadCompanyReps("data/company_representative_list.csv");

        loadInternApp("data/intern_app_list.csv");
        loadInternOpp("data/intern_opp_list.csv");
        
        // Optionally create sample opportunities for testing
        //loadSampleOpportunities();
        
        System.out.println("Data loading complete!");
    }

    public void saveFinalData(){
        saveUserAuth("data/password_list.csv");

        saveUsers("data/staff_list.csv", 
                "data/company_representative_list.csv",
                "data/student_list.csv");
        
        saveInternApp("data/intern_app_list.csv");
        saveInternOpp("data/intern_opp_list.csv");
    }

    
    public void loadStudents(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            reader.readLine(); // automatically skip first header row
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] inputInfo = processLine(line);
                userRepo.put(inputInfo[0],new Student(inputInfo[0], 
                                                inputInfo[1], 
                                                pwdRepo.get(inputInfo[0]),
                                                inputInfo[2],
                                                Integer.parseInt(inputInfo[3]),
                                                inputInfo[4]));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            System.err.println("Please check the file path and try again");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    public void loadCareerStaff(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            reader.readLine(); // automatically skip first header row
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) continue;
                
                String[] inputInfo = processLine(line);
                userRepo.put(inputInfo[0],new CenterStaff(inputInfo[0], 
                                                inputInfo[1], 
                                                pwdRepo.get(inputInfo[0]), 
                                                inputInfo[2],
                                                inputInfo[3],
                                                inputInfo[4]));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    public void loadCompanyReps(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return;
            }   
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            reader.readLine(); // automatically skip first header row
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] inputInfo = processLine(line);
                userRepo.put(inputInfo[0],new CompanyRep(inputInfo[0], 
                                                inputInfo[1], 
                                                pwdRepo.get(inputInfo[0]), 
                                                inputInfo[2], 
                                                inputInfo[3], 
                                                inputInfo[4],
                                                inputInfo[5],
                                                inputInfo[6]));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public void loadUserAuth(String filename){
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            reader.readLine(); // automatically skip first header row
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) continue;
                
                String[] inputInfo = processLine(line);
                pwdRepo.put(inputInfo[0], inputInfo[1]);

            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
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

    public void saveUserAuth(String filename){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))){
            bw.write("userId,password\n");
            for(String userId : userRepo.keySet()){
                bw.write(userId + "," + pwdRepo.get(userId)+"\n");
            }
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void saveUsers(String staffFile, String companyFile, String studentFile){
        try{
            BufferedWriter staffBW = new BufferedWriter(new FileWriter(staffFile));
            BufferedWriter companyBW = new BufferedWriter(new FileWriter(companyFile));
            BufferedWriter studentBW = new BufferedWriter(new FileWriter(studentFile));

            staffBW.write("StaffID,Name,Role,Department,Email\n");
            companyBW.write("CompanyRepID,Name,CompanyName,Department,Position,Email,Status\n");
            studentBW.write("StudentID,Name,Major,Year,Email\n");
            
            for(UserAccount user : userRepo.values()){
                switch(user.getRole()){
                    case "STUDENT": 
                        studentBW.write(String.join(",",user.export()) + "\n"); break;
                    case "CENTER_STAFF": 
                        staffBW.write(String.join(",",user.export()) + "\n"); break;
                    case "COMPANY_REP":
                        companyBW.write(String.join(",",user.export()) + "\n"); break;
                    default: break;
                }
            }

            staffBW.close();companyBW.close();studentBW.close();

        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void saveInternOpp(String filename){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))){
            bw.write("title,description,level,major,openingDate,closingDate,companyName,repId,slots\n");
            for(InternshipOpportunity opp : oppRepo.values()){
                bw.write(String.join(",",opp.export()) + "\n");
            }
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void saveInternApp(String filename){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))){
            bw.write("studentId,oppId\n");
            for(ArrayList<InternshipApplication> appLs : appRepo.values()){
                for(InternshipApplication app: appLs)
                    bw.write(String.join(",",app.export()) + "\n");
            }
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void loadInternApp(String filename){
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return;
            }   
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            reader.readLine(); // automatically skip first header row
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] inputInfo = processLine(line);
                Student applicant = (Student)userRepo.get(inputInfo[0]);
                InternshipApplication app = new InternshipApplication(applicant, oppRepo.get(inputInfo[1]));

                if(appRepo.containsKey(inputInfo[0])){
                    ArrayList<InternshipApplication> temp = appRepo.get(inputInfo[0]);
                    temp.add(app);
                    appRepo.put(applicant.getUserID(), temp);
                }else{
                    ArrayList<InternshipApplication> temp = new ArrayList<>();
                    temp.add(app);
                    appRepo.put(applicant.getUserID(), temp);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public void loadInternOpp(String filename){
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return;
            }   
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            reader.readLine(); // automatically skip first header row
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] inputInfo = processLine(line);
                InternshipOpportunity opp = new InternshipOpportunity(inputInfo[0], 
                                                    inputInfo[1], 
                                                    inputInfo[2], 
                                                    inputInfo[3], 
                                                    inputInfo[4],
                                                    inputInfo[5],
                                                    inputInfo[6],
                                                    inputInfo[7],
                                                    inputInfo[8]);
                oppRepo.put(opp.getInternshipID(), opp);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
