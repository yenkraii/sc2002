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

    public InternshipOpportunity createOpportunity(CompanyRep companyRep,
                                                   String title, String description,
                                                   InternshipLevel level, String major,
                                                   LocalDate openDate, LocalDate closeDate,
                                                   int slots) {
        // Check if rep has reached max opportunities
        List<InternshipOpportunity> existingOpps = 
            filterByCompanyRep(companyRep.getUserID());
        
        if (existingOpps.size() >= MAX_OPPORTUNITIES_PER_REP) {
            return null;
        }
        
        // Validate slots
        if (slots < 1 || slots > MAX_SLOTS) {
            return null;
        }
        
        // Validate dates
        if (closeDate.isBefore(openDate)) {
            return null;
        }
        
        InternshipOpportunity opp = new InternshipOpportunity(
            title, description, level, major, openDate, closeDate,
            companyRep.getCompanyName(), companyRep.getUserID(), slots
        );
        
        oppRepo.put(opp.getInternshipID(), opp);
        return opp;
    }
    

    // update internship opportunity (only if not approved yet)
    // param oppID: opportunity ID
    // param repID: company rep ID
    // param title: new internship title
    // param description: new description
    // param level: new level
    // param major: new major
    // param openDate: new opening date
    // param closeDate: new closing date
    // param slots: new number of slots
    // return true if successful
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
    

    // delete internship opportunity
    // param oppID: opportunity ID
    // param: repID: company rep ID
    // return true if successful
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


    // get visible opportunities for student
    // param student: student requesting opportunities
    // param filter: optional filter
    // return list of eligible opportunitie
    public String toggleVisibility(String oppID, String repID, boolean visible) {
        InternshipOpportunity opp = oppRepo.get(oppID);
        
        if (opp == null || !opp.getCompanyRepInCharge().equals(repID)) {
            return "Cannot edit!";
        }
        
        opp.setVisibility(visible);
        oppRepo.put(opp.getInternshipID(), opp);
        return "Vissiblity updated.";
    }
    

    // approve opportunity (center staff only)
    // param oppID: opportunity ID
    // return true if successful
    public String approveOpportunity(UserAccount actor,String oppID) {
        if (!(actor instanceof CompanyRep)) return "Not authorised to edit!";
        InternshipOpportunity opp = oppRepo.get(oppID);
        
        if (opp == null || opp.getStatus() != InternshipStatus.PENDING) {
            return "Cannot edit.";
        }
        
        opp.setStatus(InternshipStatus.APPROVED);
        opp.setVisibility(true); // Auto-enable visibility on approval
        oppRepo.put(opp.getInternshipID(), opp);
        return "Approved opportunity";
    }
    

    // reject opportunity (center staff only)
    // param oppID: opportunity ID
    // return true if successful
    public String rejectOpportunity(UserAccount actor, String oppID) {
        if (!(actor instanceof CompanyRep)) return "Not authorised.";
        InternshipOpportunity opp = oppRepo.get(oppID);
        
        if (opp == null || opp.getStatus() != InternshipStatus.PENDING) {
            return "Cannot edit";
        }
        
        opp.setStatus(InternshipStatus.REJECTED);
        oppRepo.put(opp.getInternshipID(), opp);
        return "Rejected opportunity.";
    }
    
    
    // get visible opportunities for student
    // param student: student requesting opportunities
    // param filter: optional filter
    // return list of eligible opportunities
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

    public List<InternshipOpportunity> filterByCompanyRep(String repID){
        return oppRepo.values()
                    .stream()
                    .filter(opp -> opp.getCompanyRepInCharge() == repID)
                    .collect(Collectors.toList());
    }

    public List<InternshipOpportunity> filterUsingReportFilter(ReportFilter reportFilter){
        return oppRepo.values()
                    .stream()
                    .filter(opp -> reportFilter.matches(opp))
                    .collect(Collectors.toList());
    }

    public long getNumberOfApplicants(InternshipOpportunity oppToCheck){
        List<InternshipApplication> all = appRepo.values().stream().flatMap(ArrayList :: stream).collect(Collectors.toList());
        return all.stream().filter(app -> app.getInternship().equals(oppToCheck)).count();
    }

    public InternshipOpportunity getOpp(String id){
        return oppRepo.get(id);
    }

}





