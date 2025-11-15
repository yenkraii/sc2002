package src.main.control;

import src.main.control.*;
import src.main.entity.*;
import src.repo.*;
import java.time.*;

public interface ILoadSampleData {

    static IRepository userRepository = null;
    static IRepository opportunityRepository = null;
    static IRepository applicationRepository = null;
    final String DEFAULT_PASSWORD = "password";
    
    /**
     * Create sample company representatives for testing
     * Only called if CSV file is empty or not provided
     */
    default public void createSampleCompanyReps() {
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
            userRepository.save((UserAccount)rep);
            System.out.println("Created: " + rep.getUserID() + " - " + rep.getName());
        }
    }
    
    /**
     * Create sample internship opportunities for testing
     * Allows students to test the system immediately
     */
    default public void loadSampleOpportunities() {
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
}
