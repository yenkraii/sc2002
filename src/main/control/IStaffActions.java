package src.main.control;

import java.util.List;

/**
 * Interface defining actions available to centre staff users.
 * Extends the general menu actions interface.
 */
public interface IStaffActions extends IMenuActions {
    /**
     * Processes a withdrawal request decision.
     * @param index Index of the withdrawal request.
     * @param decision Decision string, e.g., "accept" or "reject".
     * @return Status message indicating success or failure.
     */
    public String processWdr(int index, String decision);

    /**
     * Views all withdrawal requests pending action.
     * @return List of strings representing pending withdrawal requests.
     */
    public List<String> viewWdr();

    /**
     * Views all company representatives pending approval.
     * @return List of strings representing pending company representatives.
     */
    public List<String> viewPendingReps();

    /**
     * Processes decision on a company representative's account.
     * @param repID The ID of the representative.
     * @param decision Decision string, e.g., "accept" or "reject".
     * @return Status message indicating success or failure.
     */
    public String processRep(String repID, String decision);

    /**
     * Deletes an internship opportunity.
     * @param oppID The ID of the opportunity to delete.
     * @return Status message indicating success or failure.
     */
    public String deleteOpp(String oppID);

    /**
     * Processes approval or rejection of an internship opportunity.
     * @param internID The internship opportunity ID.
     * @param decision Decision string, e.g., "accept" or "reject".
     * @return Status message indicating success or failure.
     */
    public String processOpp(String internID, String decision);
}
