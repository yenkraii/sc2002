package src.main.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import src.main.entity.*;

// Controller for application operations 
public class ApplicationController {
    private Map<String,ArrayList<InternshipApplication>> appRepo;
    private Map<String,InternshipOpportunity> oppRepo;
    private ArrayList<WithdrawalRequest> wdrRepo;
    private static final int MAX_APPLICATIONS_PER_STUDENT = 3;

    public ApplicationController(DataRepository mainRepo) {
        appRepo = mainRepo.appRepo;
        oppRepo = mainRepo.oppRepo;
        wdrRepo = mainRepo.wdrRepo;
    }

    // Apply for internship
    public boolean applyForInternship(Student student, String internshipID){
        // Check if student already accepted a placement 
        if (student.hasAcceptedPlacement()){
            return false;
        }

        // Check max applications 
        ArrayList<InternshipApplication>  pre_existing =  appRepo.get(student.getUserID());
        if (pre_existing.size() >= MAX_APPLICATIONS_PER_STUDENT){
            return false;
        }

        InternshipOpportunity opp = oppRepo.get(internshipID);
        if (opp == null || !opp.isAcceptingApplications()){
            return false;
        }

        // Check eligibility 
        if (!opp.isEligibleStudent(student)){
            return false;
        }

        // Check if already applied 
        List <InternshipApplication> existingApps = appRepo.get(student.getUserID());
        boolean alreadyApplied = existingApps.stream().anyMatch(app -> app.getInternship().getInternshipID().equals(internshipID));
        if (alreadyApplied){
            return false;
        }

        InternshipApplication app = new InternshipApplication(student, opp);
        pre_existing.add(app);
        appRepo.put(student.getUserID(),pre_existing);
        return true;
    }

    // View applications for students (including non visible internships)
    public List<InternshipApplication> viewStudentApplications(String studentID){
        return appRepo.get(studentID);
    }

    // View applications for internship 
    public List <InternshipApplication> viewInternshipApplications(String internshipID){
        List<InternshipApplication> all = appRepo.values().stream().flatMap(ArrayList :: stream).collect(Collectors.toList());
        return all.stream().filter(app -> app.getInternship().getInternshipID() == internshipID).collect(Collectors.toList());
    }

    // Approve application (company rep)
    public boolean approveApplication(InternshipApplication application){
        Student student = application.getApplicant();
        ArrayList<InternshipApplication>  pre_existing =  appRepo.get(student.getUserID());
        pre_existing.remove(application);
        
        if (application.getStatus()!=ApplicationStatus.PENDING){
            return false;
        }

        // Check if slots available 
        InternshipOpportunity opp = application.getInternship();
        if (opp.getAvailableSlots() <=0){
            return false;
        }

        application.setStatus(ApplicationStatus.SUCCESSFUL);
        
        pre_existing.add(application);
        appRepo.put(student.getUserID(), pre_existing);
        return true;
    }

    // Reject application (company rep)
    public boolean rejectApplication(InternshipApplication application){
        Student student = application.getApplicant();
        ArrayList<InternshipApplication>  pre_existing =  appRepo.get(student.getUserID());
        pre_existing.remove(application);

        if (application.getStatus()!= ApplicationStatus.PENDING){
            return false;
        }

        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
        pre_existing.add(application);
        appRepo.put(student.getUserID(), pre_existing);
        return true;
    }

    // Accept placement (student)
    public boolean acceptPlacement(String studentID, InternshipApplication application) {
        ArrayList<InternshipApplication>  pre_existing =  appRepo.get(studentID);
        pre_existing.remove(application);
        
        if (!application.getApplicant().getUserID().equals(studentID)) {
            return false;
        }

        if (application.getStatus() != ApplicationStatus.SUCCESSFUL) {
            return false;
        }

        // Check if student already accepted a placement
        Student student = application.getApplicant();
        if (student.hasAcceptedPlacement()) {
            return false;
        }

        // Mark placement as confirmed
        application.setPlacementConfirmed(true);
        pre_existing.add(application);
        appRepo.put(student.getUserID(), pre_existing);
        
        // Increment confirmed slots
        InternshipOpportunity opp = application.getInternship();
        opp.incrementSlotsConfirmed();
        oppRepo.put(opp.getInternshipID(), opp);
        
        // Mark student as having accepted placement
        student.setHasAcceptedPlacement(true);
        
        // Withdraw all other applications
        List<InternshipApplication> allApps = 
            appRepo.get(studentID);
        for (InternshipApplication app : allApps) {
            if (app != application && 
                (app.getStatus() == ApplicationStatus.PENDING || 
                 app.getStatus() == ApplicationStatus.SUCCESSFUL)) {
                app.withdraw();
            }
        }
        
        return true;
    }
    
    // Request withdrawal (requires staff approval)
    public boolean requestWithdrawal(String studentID, InternshipApplication application, String reason) {
        if (!application.getApplicant().getUserID().equals(studentID)) {
            return false;
        }
        
        // Create withdrawal request
        WithdrawalRequest request = new WithdrawalRequest(application, application.getApplicant(), reason);
        wdrRepo.add(request);
        return true;
    }
    
    // Approve withdrawal request (center staff)
    public boolean approveWithdrawalRequest(WithdrawalRequest request) {
        
        wdrRepo.remove(request);

        if (request.getStatus() != WithdrawalStatus.PENDING) {
            return false;
        }
        
        // Approve request
        request.setStatus(WithdrawalStatus.APPROVED);
        
        wdrRepo.add(request);
        
        // Withdraw the application
        InternshipApplication app = request.getApplication();
        app.withdraw();
        //applicationRepository.save(app);
        
        // If was confirmed placement, decrement slots and update student status
        if (app.isPlacementConfirmed()) {
            InternshipOpportunity opp = app.getInternship();
            opp.decrementSlotsConfirmed();
            oppRepo.put(opp.getInternshipID(), opp);
            
            Student student = request.getStudent();
            student.setHasAcceptedPlacement(false);
        }
        
        return true;
    }
    
    // Reject withdrawal request (center Staff)
    public boolean rejectWithdrawalRequest(WithdrawalRequest request) {
        wdrRepo.remove(request);
        if (request.getStatus() != WithdrawalStatus.PENDING) {
            return false;
        }
        
        request.setStatus(WithdrawalStatus.REJECTED);
        wdrRepo.add(request);
        return true;
    }
    
    // Get pending withdrawal requests
    public List<WithdrawalRequest> getPendingWithdrawalRequests() {
        return wdrRepo.stream().filter(req -> req.getStatus() == WithdrawalStatus.PENDING).collect(Collectors.toList());
    }
}
