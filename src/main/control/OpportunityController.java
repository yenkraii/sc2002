package src.main.control;

import src.main.entity.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// controller for internship opportunity operations
public class OpportunityController implements IEditLoadedData{

    private DataRepository dataRepo;

    private static final int MAX_OPPORTUNITIES_PER_REP = 5;
    private static final int MAX_SLOTS = 10;

    // constructor
    public OpportunityController(DataRepository mainRepo) {
        dataRepo = mainRepo;
    }

    public boolean create(UserAccount actor, String[] infoToMap){
        int slots = Integer.parseInt(infoToMap[4]);
        if(slots< 1 || slots > MAX_SLOTS){
            return false;
        }

        InternshipOpportunity opp = new InternshipOpportunity(null, null, null, null, null, null, null, null, slots);
        
        dataRepo.oppRepo.put(null, opp);
        
        return true;
    };
    public Object read(String id){
        return dataRepo.oppRepo.get(id);
    };
    public boolean update(String idToUpdate, String[] infoToMap){
        if (!dataRepo.oppRepo.containsKey(idToUpdate)){
            return false;
        }
        InternshipOpportunity toUpdate = dataRepo.oppRepo.get(idToUpdate);

        return true;

    };
    public boolean delete(String id){
        if (dataRepo.oppRepo.containsKey(id)){
            dataRepo.oppRepo.remove(id);
            return true;
        }
        return false;
    };
}





