package src.main.control;

// Controller for application operations 
public class ApplicationController {
    private ApplicationRepository applicationRepository;
    private OpportunityRepository opportunityRepository;
    private WithdrawalRequestRepository withdrawalRequestRepository;
    private static final int MAX_APPLICATIONS_PER_STUDENT = 3;

    public ApplicationController(ApplicationRepository appRepo, OpportunityRepository oppRepo, WithdrawalRequestRepository withdrawalRepo) {
        this.applicationRepository = appRepo;
        this.opportunityRepository = oppRepo;
        this.withdrawalRequestRepository = withdrawalRepo;
    }

    // Apply for internship
    public boolean applyForInternship(Student student, String internshipID){
        // Check if student already accepted a placement 
        if (student.hasAcceptedPlacement()){
            return false;
        }

        // Check max applications 
        int activeApps = applicationRepository.countActiveApplications(student.getUserID());
        if (activeApps >= MAX_APPLICATIONS_PER_STUDENT){
            return false;
        }

        internshipOpportunity opp = opportunityRepository.findByID(internshipID);
        if (opp == null || !opp.isAcceptingApplications(){
            return false;
        }

        // Check eligibility 
        if (!opp.isEligibleStudent(student)){
            return false;
        }

        // Check if already applied 
        List <InternshipApplication> existingApps = applicationRepository.findByStudent(student.getUserID());
        boolean alreadyApplied = existingApps.stream().anyMatch(app -> app.getInternship().getInternshipID().equals(internshipID));
        if (already applied){
            return false;
        }

        internshipApplication app = new InternshipApplication(student, opp);
        applicationRepository.save(app);
        return true;
    }

    // View applications for students (including non visible internships)
    public List<InternshipApplication> viewStudentApplications(String studentID){
        return applicationRepository.findByStudent(studentID);
    }

    // View applications for internship 
    public List <InternshipApplication> viewInternshipApplications(String internshipID){
        return applicationRepository.findByInternship(internshipID);
    }

    // Approve application (company rep)
    public boolean approveApplication(InternshipApplication application){
        if (application.getStatus()!=ApplicationStatus.PENDING){
            return false;
        }

        // Check if slots available 
        InternshipOpportunity opp = application.getInternship();
        if (opp.getAvailableSlots() <=0){
            return false;
        }

        application.setStatus(ApplicationStatus.SUCCESSFUL);
        applicationRepository.save(application);
        return true;
    }

    // Reject application (company rep)
    public boolean rejectApplication(InternshipApplication application){
        if (application.getStatus()!= ApplicationStatus.PENDING){
            return false;
        }

        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
        applicationRepository.save(application);
        return true;
    }

    // Accept placement (student)
    public boolean acceptPlacement(String studentID, InternshipApplication application) {
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
        applicationRepository.save(application);
        
        // Increment confirmed slots
        InternshipOpportunity opp = application.getInternship();
        opp.incrementSlotsConfirmed();
        opportunityRepository.save(opp);
        
        // Mark student as having accepted placement
        student.setHasAcceptedPlacement(true);
        
        // Withdraw all other applications
        List<InternshipApplication> allApps = 
            applicationRepository.findByStudent(studentID);
        for (InternshipApplication app : allApps) {
            if (app != application && 
                (app.getStatus() == ApplicationStatus.PENDING || 
                 app.getStatus() == ApplicationStatus.SUCCESSFUL)) {
                app.withdraw();
                applicationRepository.save(app);
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
        withdrawalRequestRepository.save(request);
        return true;
    }
    
    // Approve withdrawal request (center staff)
    public boolean approveWithdrawalRequest(WithdrawalRequest request) {
        if (request.getStatus() != WithdrawalStatus.PENDING) {
            return false;
        }
        
        // Approve request
        request.setStatus(WithdrawalStatus.APPROVED);
        withdrawalRequestRepository.save(request);
        
        // Withdraw the application
        InternshipApplication app = request.getApplication();
        app.withdraw();
        applicationRepository.save(app);
        
        // If was confirmed placement, decrement slots and update student status
        if (app.isPlacementConfirmed()) {
            InternshipOpportunity opp = app.getInternship();
            opp.decrementSlotsConfirmed();
            opportunityRepository.save(opp);
            
            Student student = request.getStudent();
            student.setHasAcceptedPlacement(false);
        }
        
        return true;
    }
    
    // Reject withdrawal request (center Staff)
    public boolean rejectWithdrawalRequest(WithdrawalRequest request) {
        if (request.getStatus() != WithdrawalStatus.PENDING) {
            return false;
        }
        
        request.setStatus(WithdrawalStatus.REJECTED);
        withdrawalRequestRepository.save(request);
        return true;
    }
    
    // Get pending withdrawal requests
    public List<WithdrawalRequest> getPendingWithdrawalRequests() {
        return withdrawalRequestRepository.findPendingRequests();
    }
}
