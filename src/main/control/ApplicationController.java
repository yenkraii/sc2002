package src.main.control;

import java.util.List;

import src.main.entity.*;

// Controller for application operations 
public class ApplicationController implements IEditLoadedData {
    private DataRepository dataRepo;
    private static final int MAX_APPLICATIONS_PER_STUDENT = 3;

    public ApplicationController(DataRepository mainRepo) {
        dataRepo = mainRepo;
    }

    public boolean create(UserAccount student, String[] infoLs){
        if (!(student instanceof Student)) return false;
        Student actor = (Student) student;
        InternshipOpportunity opp = dataRepo.oppRepo.get(infoLs[0]);
        if ((opp == null) || (!opp.isAcceptingApplications()) || (!opp.isEligibleStudent(actor))){
            return false;
        }

        //TODO: REVISIT THE INTERNAPP CONNECTION

        InternshipApplication internApp = new InternshipApplication(actor, opp);
        dataRepo.appRepo.put("", internApp);
        return true;

    }
    
    public Object read(String id){
        return dataRepo.appRepo.get(id);
    }
    public boolean update(String idToUpdate, String[] infoToMap){
        
        //TODO: REVISIT THE INTERNAPP CONNECTION
        
        return false;
    }
    
    
    
    public boolean delete(String id){
        if (dataRepo.appRepo.containsKey(id)){
            dataRepo.appRepo.remove(id);
            return true;
        }
        return false;
    }
}
