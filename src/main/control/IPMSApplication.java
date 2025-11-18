package src.main.control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import src.main.control.*;
import src.main.entity.*;

// this class faciltates interactions between controllers

public class IPMSApplication implements IMenuActions, ICompanyActions, IStaffActions, IStudentActions {
    private IMainRepository systemStorage;
    private ApplicationController appControl;
    private OpportunityController oppControl;
    private AuthController usrControl;

    private ReportFilter reportFilter;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public IPMSApplication(){
      systemStorage = new DataRepository();
      appControl = new ApplicationController((DataRepository)systemStorage);
      oppControl = new OpportunityController((DataRepository)systemStorage);
      usrControl = new AuthController((DataRepository)systemStorage);
      reportFilter = new ReportFilter();
    }

    public void setup(){
      systemStorage.loadInitialData();
    }

    public void shutdown(){
      systemStorage.saveFinalData();
    }

    public boolean isLoggedOn(){
      return (!Objects.isNull(usrControl.getCurrrentUser()));
    }

    public String accountRole(){
      if (isLoggedOn()){
        return usrControl.getCurrrentUser().getRole();
      }
      return "";
    }

    public String login(String[] usrInput){
      return usrControl.login(usrInput[0], usrInput[1]);
    }

    public void logout(){
      usrControl.logout();
    }

    public void changePassword(String[] usrInput){
      usrControl.update(usrControl.getCurrrentUser().getUserID(), usrInput);
    }

    public String registerCompanyRep(String[] usrInput){
      return usrControl.create(usrInput);
    }

    public String applyIntern(String id){
      return appControl.applyForInternship((Student) usrControl.getCurrrentUser(), id);
    }

    public String acceptPlacement(String internID){
        String currentUserID = usrControl.getCurrrentUser().getUserID();

        // Find THIS student's application for this internship
        InternshipApplication myApp = appControl.findApp(currentUserID, internID);

        if (myApp == null) {
            return "You have no application for this internship!";
        }

        return appControl.acceptPlacement(currentUserID, myApp);
    }

    public String requestWithdrawal(String internID, String reason){
      return appControl.requestWithdrawal(
          usrControl.getCurrrentUser().getUserID(),  
          appControl.viewInternshipApplications(internID).get(0), 
          reason);
    }

    public String createOpp(String[] usrInput){
      return oppControl.createOpportunity(
        (CompanyRep) usrControl.getCurrrentUser(), 
        usrInput[0], 
        usrInput[1], 
        InternshipLevel.valueOf(usrInput[2]), 
        usrInput[3], 
        LocalDate.parse(usrInput[4], formatter), 
        LocalDate.parse(usrInput[5], formatter), 
        Integer.parseInt(usrInput[6])
        );
    }

    public String deleteOpp(String oppID){
      switch (usrControl.getCurrrentUser().getRole()) {
        case "COMPANY_REP":
          return oppControl.deleteOpportunity(oppID, usrControl.getCurrrentUser().getUserID());

        case "CENTER_STAFF":
          return oppControl.deleteOpportunity(oppID, oppControl.getOpp(oppID).getCompanyRepInCharge());

        default:
          return "Unauthorised action.";
      }
    }

    public List<String> viewApp(String internID){
      return appControl.viewInternshipApplications(internID)
              .stream().map(app -> app.toString()).collect(Collectors.toList());
    }

    public List<String> viewOpp(){
      List<InternshipOpportunity> lsOpp;
      switch (usrControl.getCurrrentUser().getRole()) {
        case "COMPANY_REP":
          lsOpp = oppControl.filterByCompanyRep(usrControl.getCurrrentUser().getUserID());
          break;
        case "CENTER_STAFF":
          lsOpp = oppControl.filterUsingReportFilter(reportFilter);
          break;
        case "STUDENT":
          lsOpp = oppControl.getVisibleOpportunities((Student)usrControl.getCurrrentUser(), reportFilter);
          break;
        default:
          return null;
      }
      return lsOpp.stream().map(app -> app.toString()).collect(Collectors.toList());
    }

    public void setFilters(String[] input){
      reportFilter.setMajor(input[0]);
      reportFilter.setCompanyName(input[1]);
      reportFilter.setLevel(InternshipLevel.valueOf(input[2]));
      reportFilter.setStatus(InternshipStatus.valueOf(input[3]));
    }

    public String toggleVisibility(String oppID){
      InternshipOpportunity opp = oppControl.getOpp(oppID);
      switch (usrControl.getCurrrentUser().getRole()) {
        case "COMPANY_REP":
          return oppControl.toggleVisibility(oppID, usrControl.getCurrrentUser().getUserID(), !opp.isVisible());
        case "CENTER_STAFF":
          return oppControl.toggleVisibility(oppID, opp.getCompanyRepInCharge(), !opp.isVisible());  
        default:
          return "Unauthorised action.";
      }
    }

    public String processApp(String internID, String studentID, String decision){
      if(decision.contains("a"))
        return appControl.approveApplication(appControl.findApp(studentID, internID));
      else
          return appControl.rejectApplication(appControl.findApp(studentID, internID));
    }

    public String editOpp(String[] usrInput){
      return oppControl.updateOpportunity(
        usrInput[0], 
        usrControl.getCurrrentUser().getUserID(), 
        usrInput[1], 
        usrInput[2], 
        InternshipLevel.valueOf(usrInput[3]), 
        usrInput[4],
        LocalDate.parse(usrInput[5], formatter),
        LocalDate.parse(usrInput[6], formatter),
        Integer.parseInt(usrInput[7])
        );
    }

    public String processOpp(String internID, String decision){
      if(decision.contains("a"))
        return oppControl.approveOpportunity(usrControl.getCurrrentUser(), internID);
      else
          return oppControl.rejectOpportunity(usrControl.getCurrrentUser(), internID);
    }

    public String processWdr(int index, String decision){
        if(index < 0) return "Invalid entry!";

        List<WithdrawalRequest> allWdr = appControl.getPendingWithdrawalRequests();

        if(index >= allWdr.size()) {
            return "Invalid withdrawal request number!";
        }

        if(decision.contains("a"))
            return appControl.approveWithdrawalRequest(allWdr.get(index));
        else
            return appControl.rejectWithdrawalRequest(allWdr.get(index));
    }

    public List<String> viewWdr(){
      return appControl.getPendingWithdrawalRequests().stream().map(wdr -> wdr.toString()).collect(Collectors.toList());
    }

    public List<String> viewPendingReps(){
      return usrControl.viewPendingReps().stream().map(acc -> acc.toString()).collect(Collectors.toList());
    }

    public String processRep(String repID, String decision){
      if(decision.contains("a"))
        return usrControl.approveRep(repID);
      else
          return usrControl.rejectRep(repID);
    }

  }
