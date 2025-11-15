package src.main.control;

import src.main.entity.*;
import src.repo.ApplicationRepository;
import src.repo.OpportunityRepository;
import src.repo.UserRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// controller for internship opportunity operations
public class OpportunityController {
    private OpportunityRepository opportunityRepository;
    private UserRepository userRepository;
    private ApplicationRepository applicationRepository;
    private static final int MAX_OPPORTUNITIES_PER_REP = 5;
    private static final int MAX_SLOTS = 10;

    // constructor
    // param oppRepo: opportunity repository
    // param userRepo: user repository
    // param appRepo: application repository
    public OpportunityController(OpportunityRepository oppRepo, UserRepository userRepo, ApplicationRepository appRepo) {
        opportunityRepository = oppRepo;
        userRepository = userRepo;
        applicationRepository = appRepo;
    }

    // create a new internship opportunity
    // param companyRep: company representative creating the opportunity
    // param title: internship title
    // param description: description of internship
    // param level: internship level
    // param major: preferred major
    // param openDate: opening date
    // param closeDate: closing date
    // param slots: number of slots
    // return created opportunity or null if failed
    public InternshipOpportuntity createOpportunity(CompanyRep companyRep, String title, String description, InternshipLevel level, String major, LocalDate openDate, LocalDate closeDate, int slots) {
        // check if rep has reached max opportunities
        List<InternshipOpportunity> existingOpps = opportunityRepository.findByCompanyRep(companyRep.getUserID());

        if (existingOpps.size() >= MAX_OPPORTUNITIES_PER_REP) {
            return null;
        }

        // validate slots
        if (slots < 1 || slots > MAX_SLOTS) {
            return null;
        }

        // validate dates
        if (closeDate.isBefore(openDate)) {
            return null;
        }

        InternshipOpportunity opp = new InternshipOpportunity(title, description, level, major, openDate, closeDate, companyRep.getCompanyName(), companyRep.getUserID(), slots);

        opportunityRepository.save(opp);
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
    public boolean updateOpportunity(String oppID, String repID, String title, String description, InternshipLevel level, String major, LocalDate openDate, LocalDate closeDate, int slots) {
        InternshipOpportunity opp = opportunityRepository.findByID(oppID);

        if (opp == null || !opp.getCompanyRepInCharge().equals(repID)) {
            return false;
        }

        // cannot edit approved opportunities
        if (opp.getStatus() != InternshipStatus.PENDING) {
            return false;
        }

        // validate
        if (slots < 1 || slots > MAX_SLOTS || closeDate.isBefore(openDate)) {
            return false;
        }

        opp.setInternshipTitle(title);
        opp.setDescription(description);
        opp.setInternshipLevel(level);
        opp.setPreferredMajor(major);
        opp.setApplicationOpeningDate(openDate);
        opp.setApplicationClosingDate(closeDate);
        opp.setNumberOfSlots(slots);
        opportunityRepository.save(opp);
        return true;
    }

    // delete internship opportunity
    // param oppID: opportunity ID
    // param: repID: company rep ID
    // return true if successful
    public boolean deleteOpportunity(String oppID, String repID) {
        InternshipOpportunity opp = opportunityRepository.findByID(oppID);

        if (opp == null || !opp.getCompanyRepInCharge().equals(repID)) {
            return false;
        }

        // cannot delete if have applications
        List<InternshipApplication> apps = applicationRepository.findByInternship(oppID);
        if (!apps.isEmpty()) {
            return false;
        }

        opportunityRepository.delete(oppID);
        return true;
    }

    // approve opportunity (center staff only)
    // param oppID: opportunity ID
    // return true if successful
    public boolean approveOpportunity(String oppID) {
        InternshipOpportunity opp = opportunityRepository.findByID(oppID);

        if (opp == null || opp.getStatus() != InternshipStatus.PENDING) {
            return false;
        }

        opp.setStatus(InternshipStatus.APPROVED);
        opp.setVisibility(true); // auto-enable visibility on approval
        opportunityRepository.save(opp);
        return true;
    }

    // reject opportunity (center staff only)
    // param oppID: opportunity ID
    // return true if successful
    public boolean rejectOpportunity(String oppID) {
        InternshipOpportunity opp = opportunityRepository.findByID(oppID);

        if (opp == null || opp.getStatus() != InternshipStatus.PENDING) {
            return false;
        }

        opp.setStatus(InternshipStatus.REJECTED);
        opportunityRepository.save(opp);
        return true;
    }

    // get visible opportunities for student
    // param student: student requesting opportunities
    // param filter: optional filter
    // return list of eligible opportunities
    public List<InternshipOpportunity> getVisibleOpportunities(Student student, ReportFilter filter) {
        List<InternshipOpportunity> allOpps = filter != null ?
                opportunityRepository.findByFilter(filter) :
                opportunityRepository.findAll();

        return allOpps.stream()
                .filter(InternshipOpportunity::isVisible)
                .filter(opp -> opp.isEligibleStudent(student))
                .filter(InternshipOpportunity::isAcceptingApplications)
                .sorted(Comparator.comparing(InternshipOpportunity::getInternshipTitle))
                .collect(Collectors.toList());
    }

    // get all opportunities for company rep
    // param repID: company rep ID
    // return list of opportunities
    public List<InternshipOpportunity> getOpportunitiesByRep(String repID) {
        return opportunityRepository.findByCompanyRep(repID);
    }

    // generate report with filter
    // param filter: report filter
    // return list of filtered opportunities
    public List<InternshipOpportunity> generateReport(ReportFilter filter) {
        return opportunityRepository.findByFilter(filter);
    }
}





