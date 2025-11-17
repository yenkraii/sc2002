package src.main.control;

import java.util.Objects;

import src.main.control.*;

// this class faciltates interactions between controllers

public class IPMSApplication implements IMenuActions {
    private IMainRepository systemStorage;
    private IEditLoadedData appControl;
    private IEditLoadedData oppControl;
    private IEditLoadedData usrControl;

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
      AuthController ac = (AuthController) usrControl;
      return (!Objects.isNull(ac.getCurrrentUser()));
    }

    public String accountRole(){
      if (isLoggedOn()){
        AuthController ac = (AuthController) usrControl;
        return ac.getCurrrentUser().getRole();
      }
      return "";
    }

    public boolean login(String[] usrInput){
      AuthController ac = (AuthController) usrControl;
      if(ac.login(usrInput[0], usrInput[1])) return true;
      return false;
    }

    public void logout(){
      AuthController ac = (AuthController) usrControl;
      ac.logout();
    }

    public void changePassword(String[] usrInput){
      AuthController ac = (AuthController) usrControl;
      ac.update(ac.getCurrrentUser().getUserID(), usrInput);
    }

    public void registerCompanyRep(String[] usrInput){
      AuthController ac = (AuthController) usrControl;
      ac.create(ac.getCurrrentUser(), usrInput);
      ac.update(usrInput[0], new String[] {usrInput[3]});
    }
}
