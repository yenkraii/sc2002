package src.main.control;

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

    public String applyIntern(UserAccount user, String id){
      return appControl.applyForInternship((Student) user, id);
    }

    

  }
