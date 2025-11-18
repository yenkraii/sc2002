package src.main.control;

import src.main.entity.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// controller for internship opportunity operations
public class OpportunityController{

    private Map<String,InternshipOpportunity> oppRepo;
    private Map<String,ArrayList<InternshipApplication>> appRepo;

    private static final int MAX_OPPORTUNITIES_PER_REP = 5;
    private static final int MAX_SLOTS = 10;

    // constructor
    public OpportunityController(DataRepository mainRepo) {
        oppRepo = mainRepo.oppRepo;
        appRepo = mainRepo.appRepo;
    }
    /**
     * Creates a new internship opportunity for a company representative.
     * Checks limits, validates details, and adds the opportunity.
     * @param companyRep The company representative creating the opportunity.
     * @param title Title of the internship.
     * @param description Description of the internship.
     * @param level Level of the internship.
     * @param major Preferred major for applicants.
     * @param openDate Application opening date.
     * @param closeDate Application closing date.
     * @param slots Number of available slots.
     * @return Status message for creation result.
     */
    public String createOpportunity(CompanyRep companyRep,
                                                   String title, String description,
                                                   InternshipLevel level, String major,
                                                   LocalDate openDate, LocalDate closeDate,
                                                   int slots) {
        // Check if rep has reached max opportunities
        List<InternshipOpportunity> existingOpps = 
            filterByCompanyRep(companyRep.getUserID());
        
        if (existingOpps.size() >= MAX_OPPORTUNITIES_PER_REP) {
            return "Limit of Opportunities reached.";
        }
        
        // Validate slots
        if (slots < 1 || slots > MAX_SLOTS) {
            return "Invalid slot details.";
        }
        
        // Validate dates
        if (closeDate.isBefore(openDate)) {
            return "Invalid closing date.";
        }
        
        InternshipOpportunity opp = new InternshipOpportunity(
            title, description, level, major, openDate, closeDate,
            companyRep.getCompanyName(), companyRep.getUserID(), slots
        );
        
        oppRepo.put(opp.getInternshipID(), opp);
        return "Sucessful Creation.";
    }


    /**
     * Updates a non-approved internship opportunity.
     * Only pending opportunities can be edited.
     * @param oppID Opportunity ID.
     * @param repID Company representative ID.
     * @param title New title.
     * @param description New description.
     * @param level New level.
     * @param major New major.
     * @param openDate New opening date.
     * @param closeDate New closing date.
     * @param slots New slot count.
     * @return Status message for update result.
     */
    public String updateOpportunity(String oppID, String repID,
                                    String title, String description,
                                    InternshipLevel level, String major,
                                    LocalDate openDate, LocalDate closeDate,
                                    int slots) {
        InternshipOpportunity opp = oppRepo.get(oppID);
        
        if (opp == null || !opp.getCompanyRepInCharge().equals(repID)) {
            return "No access to this opportunity.";
        }
        
        // Can't edit approved opportunities
        if (opp.getStatus() != InternshipStatus.PENDING) {
            return "Cannot edit approved opportunities";
        }
        
        // Validate
        if (slots < 1 || slots > MAX_SLOTS || closeDate.isBefore(openDate)) {
            return "Info invalid for opportunity.";
        }
        
        opp.setInternshipTitle(title);
        opp.setDescription(description);
        opp.setInternshipLevel(level);
        opp.setPreferredMajor(major);
        opp.setApplicationOpeningDate(openDate);
        opp.setApplicationClosingDate(closeDate);
        opp.setNumberOfSlots(slots);
        
        oppRepo.put(opp.getInternshipID(), opp);
        return "Successful update.";
    }


    /**
     * Deletes an internship opportunity.
     * Only possible if created by the rep and if no applications exist.
     * @param oppID Opportunity ID.
     * @param repID Company representative ID.
     * @return Status message for deletion result.
     */
    public String deleteOpportunity(String oppID, String repID) {
        InternshipOpportunity opp = oppRepo.get(oppID);
        
        if (opp == null || !opp.getCompanyRepInCharge().equals(repID)) {
            return "No access to this opportunity";
        }
        
        // Can't delete if has applications
        if (getNumberOfApplicants(opp) > 1) {
            return "Cannot delete since there are active applications!";
        }
        
        oppRepo.remove(oppID);
        return "Deletion sucessful";
    }


    /**
     * Toggles the visibility status of an internship opportunity.
     * @param oppID Opportunity ID.
     * @param repID Company representative ID.
     * @param visible True for visible, false for hidden.
     * @return Status message for toggle action.
     */
    public String toggleVisibility(String oppID, String repID, boolean visible) {
        InternshipOpportunity opp = oppRepo.get(oppID);
        
        if (opp == null || !opp.getCompanyRepInCharge().equals(repID)) {
            return "Cannot edit!";
        }
        
        opp.setVisibility(visible);
        oppRepo.put(opp.getInternshipID(), opp);
        return "Vissiblity updated.";
    }


    /**
     * Approves a pending opportunity as centre staff.
     * @param actor The staff member approving.
     * @param oppID Opportunity ID.
     * @return Status message for approval result.
     */
    public String approveOpportunity(UserAccount actor,String oppID) {
        if (!(actor instanceof CenterStaff)) return "Not authorised to edit!";
        InternshipOpportunity opp = oppRepo.get(oppID);
        
        if (opp == null || opp.getStatus() != InternshipStatus.PENDING) {
            return "Cannot edit.";
        }
        
        opp.setStatus(InternshipStatus.APPROVED);
        opp.setVisibility(true); // Auto-enable visibility on approval
        oppRepo.put(opp.getInternshipID(), opp);
        return "Approved opportunity";
    }


    /**
     * Rejects a pending opportunity as centre staff.
     * @param actor The staff member rejecting.
     * @param oppID Opportunity ID.
     * @return Status message for rejection result.
     */
    public String rejectOpportunity(UserAccount actor, String oppID) {
        if (!(actor instanceof CenterStaff)) return "Not authorised.";
        InternshipOpportunity opp = oppRepo.get(oppID);
        
        if (opp == null || opp.getStatus() != InternshipStatus.PENDING) {
            return "Cannot edit";
        }
        
        opp.setStatus(InternshipStatus.REJECTED);
        oppRepo.put(opp.getInternshipID(), opp);
        return "Rejected opportunity.";
    }

    /**
     * Returns a list of visible opportunities eligible for a student.
     * Applies filtering if provided.
     * @param student The requesting student.
     * @param filter Optional filter conditions.
     * @return List of eligible internship opportunities.
     */
    public List<InternshipOpportunity> getVisibleOpportunities(Student student, ReportFilter filter) {
        List<InternshipOpportunity> allOpps = filter != null ? 
            filterUsingReportFilter(filter) :
            oppRepo.values().stream().collect(Collectors.toList());
        
        return allOpps.stream()
            .filter(InternshipOpportunity::isVisible)
            .filter(opp -> opp.isEligibleStudent(student))
            .filter(InternshipOpportunity::isAcceptingApplications)
            .sorted(Comparator.comparing(InternshipOpportunity::getInternshipTitle))
            .collect(Collectors.toList());
    }
    /**
     * Filters opportunities by company representative.
     * @param repID Company representative ID.
     * @return List of opportunities belonging to the rep.
     */
    public List<InternshipOpportunity> filterByCompanyRep(String repID){
        return oppRepo.values()
                    .stream()
                    .filter(opp -> opp.getCompanyRepInCharge().equals(repID))
                    .collect(Collectors.toList());
    }
    /**
     * Filters opportunities using the report filter.
     * @param reportFilter Filter object to apply.
     * @return List of matching internship opportunities.
     */
    public List<InternshipOpportunity> filterUsingReportFilter(ReportFilter reportFilter){
        return oppRepo.values()
                    .stream()
                    .filter(opp -> reportFilter.matches(opp))
                    .collect(Collectors.toList());
    }
    /**
     * Returns the number of applicants for a given opportunity.
     * @param oppToCheck Internship opportunity to check.
     * @return Number of applications for the opportunity.
     */
    public long getNumberOfApplicants(InternshipOpportunity oppToCheck){
        List<InternshipApplication> all = appRepo.values().stream().flatMap(ArrayList :: stream).collect(Collectors.toList());
        return all.stream().filter(app -> app.getInternship().equals(oppToCheck)).count();
    }
    /**
     * Retrieves the specified internship opportunity by ID.
     * @param id ID of the opportunity.
     * @return InternshipOpportunity object if found.
     */
    public InternshipOpportunity getOpp(String id){
        return oppRepo.get(id);
    }

}





