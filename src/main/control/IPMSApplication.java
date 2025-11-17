package src.main.control;

import java.time.LocalDate;
import java.util.Objects;

import src.main.control.*;
import src.main.entity.*;

// this class faciltates interactions between controllers

public class IPMSApplication implements IMenuActions {
    private IMainRepository systemStorage;
    private ApplicationController appControl;
    private OpportunityController oppControl;
    private AuthController usrControl;

    public IPMSApplication(){
      systemStorage = new DataRepository();
      appControl = new ApplicationController((DataRepository)systemStorage);
      oppControl = new OpportunityController((DataRepository)systemStorage);
      usrControl = new AuthController((DataRepository)systemStorage);
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

    public void registerCompanyRep(String[] usrInput){
      usrControl.create(usrControl.getCurrrentUser(), usrInput);
      usrControl.update(usrInput[0], new String[] {usrInput[3]});
    }

    public String applyIntern(String id){
      return appControl.applyForInternship((Student) usrControl.getCurrrentUser(), id);
    }

    public String acceptPlacement(String internID){
      return appControl.acceptPlacement(usrControl.getCurrrentUser().getUserID(), appControl.viewInternshipApplications(internID).get(0));
    }

    public String requestWithdrawal(String internID, String reason){
      return appControl.requestWithdrawal(
          usrControl.getCurrrentUser().getUserID(),  
          appControl.viewInternshipApplications(internID).get(0), 
          reason);
    }

    public InternshipOpportunity createOpp(String[] usrInput){
      return oppControl.createOpportunity(
        (CompanyRep) usrControl.getCurrrentUser(), 
        usrInput[0], 
        usrInput[1], 
        InternshipLevel.valueOf(usrInput[2]), 
        usrInput[5], 
        LocalDate.parse(usrInput[3]), 
        LocalDate.parse(usrInput[4]), 
        Integer.parseInt(usrInput[5])
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
  }
