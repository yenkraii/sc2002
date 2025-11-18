package src.main.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public String applyForInternship(Student student, String internshipID){
        // Check if student already accepted a placement 
        if (student.hasAcceptedPlacement()){
            return "Student already has an accepted internship";
        }

        // Check max applications 
        ArrayList<InternshipApplication> pre_existing =  appRepo.get(student.getUserID());
        if (pre_existing == null) {
            pre_existing = new ArrayList<>();
        }
        if (pre_existing.size() >= MAX_APPLICATIONS_PER_STUDENT){
            return "Max applications reached.";
        }

        InternshipOpportunity opp = oppRepo.get(internshipID);
        if (opp == null || !opp.isAcceptingApplications()){
            return "Opportunity not found/accepting applications.";
        }

        // Check eligibility 
        if (!opp.isEligibleStudent(student)){
            return "Student is not eligible.";
        }

        // Check if already applied 
        // Use pre_existing for everything (already null-checked above)
        boolean alreadyApplied = pre_existing.stream()
                .anyMatch(app -> app.getInternship().getInternshipID().equals(internshipID));
        if (alreadyApplied){
            return "Applied already!";
        }

        InternshipApplication app = new InternshipApplication(student, opp);
        pre_existing.add(app);
        appRepo.put(student.getUserID(),pre_existing);
        return "Application successful.";
    }

    // View applications for internship 
    public List<InternshipApplication> viewInternshipApplications(String internshipID){
        List<InternshipApplication> all = appRepo.values().stream().flatMap(ArrayList :: stream).toList();
        return all.stream().filter(app -> Objects.equals(app.getInternship().getInternshipID(), internshipID)).collect(Collectors.toList());
    }

    public InternshipApplication findApp(String userID, String internID){
        return appRepo.values().stream()
                .flatMap(ArrayList::stream)
                .filter(app -> app.getInternship() != null
                        && Objects.equals(app.getApplicant().getUserID(), userID)
                        && Objects.equals(app.getInternship().getInternshipID(), internID))
                .findFirst()
                .orElse(null);
    }



    // Approve application (company rep)
    public String approveApplication(InternshipApplication application){
        // Do ALL validation FIRST
        if (application.getStatus() != ApplicationStatus.PENDING){
            return "Application is not available";
        }

        InternshipOpportunity opp = application.getInternship();
        if (opp.getAvailableSlots() <= 0){
            return "Insufficient slots for opportunity";
        }

        // ONLY modify after all checks pass
        application.setStatus(ApplicationStatus.SUCCESSFUL);

        // No need to remove/add - just update the status
        // The object in the ArrayList is the same reference!
        return "Application approved.";
    }

    // Reject application (company rep)
    public String rejectApplication(InternshipApplication application){
        Student student = application.getApplicant();
        ArrayList<InternshipApplication>  pre_existing =  appRepo.get(student.getUserID());
        pre_existing.remove(application);

        if (application.getStatus()!= ApplicationStatus.PENDING){
            return "Cannot reject withdrawn/accepted application!";
        }

        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
        pre_existing.add(application);
        appRepo.put(student.getUserID(), pre_existing);
        return "Rejected successfully";
    }

    // Accept placement (student)
    public String acceptPlacement(String studentID, InternshipApplication application) {
        ArrayList<InternshipApplication>  pre_existing =  appRepo.get(studentID);
        pre_existing.remove(application);
        
        if (!application.getApplicant().getUserID().equals(studentID)) {
            return "Not Student";
        }

        if (application.getStatus() != ApplicationStatus.SUCCESSFUL) {
            return "Application not Successfully";
        }

        // Check if student already accepted a placement
        Student student = application.getApplicant();
        if (student.hasAcceptedPlacement()) {
            return "Already accepted";
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
        
        return "Accepted Opportunity!";
    }
    
    // Request withdrawal (requires staff approval)
    public String requestWithdrawal(String studentID, InternshipApplication application, String reason) {
        if (!application.getApplicant().getUserID().equals(studentID)) {
            return "Unable to withdraw";
        }
        
        // Create withdrawal request
        WithdrawalRequest request = new WithdrawalRequest(application, application.getApplicant(), reason);
        wdrRepo.add(request);
        return "Submitted Withdrawal Request";
    }
    
    // Approve withdrawal request (center staff)
    public String approveWithdrawalRequest(WithdrawalRequest request) {
        
        wdrRepo.remove(request);

        if (request.getStatus() != WithdrawalStatus.PENDING) {
            return "Cannot edit Withdrawal Request";
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
        
        return "Withdrawal Request approved.";
    }
    
    // Reject withdrawal request (center Staff)
    public String rejectWithdrawalRequest(WithdrawalRequest request) {
        wdrRepo.remove(request);
        if (request.getStatus() != WithdrawalStatus.PENDING) {
            return  "Cannot edit Withdrawal Request";
        }
        
        request.setStatus(WithdrawalStatus.REJECTED);
        wdrRepo.add(request);
        return  "Withdrawal Request rejected.";
    }
    
    // Get pending withdrawal requests
    public List<WithdrawalRequest> getPendingWithdrawalRequests() {
        return wdrRepo.stream().filter(req -> req.getStatus() == WithdrawalStatus.PENDING).collect(Collectors.toList());
    }
}
