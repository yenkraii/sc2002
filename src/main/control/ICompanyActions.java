package src.main.control;

import java.util.List;

/**
 * Interface defining actions available to company representatives.
 * Extends the general menu actions interface.
 */
public interface ICompanyActions extends IMenuActions {
    /**
     * Creates a new internship opportunity using provided input data.
     * @param usrInput Array of input strings containing opportunity details.
     * @return Status message indicating success or failure.
     */
    public String createOpp(String[] usrInput);

    /**
     * Edits an existing internship opportunity using provided input data.
     * @param usrInput Array of input strings containing updated opportunity details.
     * @return Status message indicating success or failure.
     */
    public String editOpp(String[] usrInput);

    /**
     * Deletes an internship opportunity by its ID.
     * @param oppID The ID of the opportunity to delete.
     * @return Status message indicating success or failure.
     */
    public String deleteOpp(String oppID);

    /**
     * Toggles the visibility status of an internship opportunity.
     * @param oppID The ID of the opportunity to toggle.
     * @return Status message indicating success or failure.
     */
    public String toggleVisibility(String oppID);

    /**
     * Processes the approval or rejection of an internship opportunity.
     * @param internID The ID of the internship opportunity.
     * @param decision The decision string, e.g., "accept" or "reject".
     * @return Status message indicating success or failure.
     */
    public String processOpp(String internID, String decision);

    /**
     * Processes a student's application to an internship opportunity.
     * @param internID The ID of the internship opportunity.
     * @param studentID The ID of the student applying.
     * @param decision The decision string for the application.
     * @return Status message indicating success or failure.
     */
    public String processApp(String internID, String studentID, String decision);

    /**
     * Views all applications for a specific internship opportunity.
     * @param internID The ID of the internship opportunity.
     * @return List of strings representing applications.
     */
    public List<String> viewApp(String internID);
}
