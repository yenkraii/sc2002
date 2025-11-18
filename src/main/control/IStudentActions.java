package src.main.control;

import java.util.List;

/**
 * Interface defining actions available to student users.
 * Extends the general menu actions interface.
 */
public interface IStudentActions extends IMenuActions {
    /**
     * Applies for an internship opportunity by ID.
     * @param id The internship opportunity ID.
     * @return Status message indicating success or failure.
     */
    public String applyIntern(String id);

    /**
     * Views applications for a specific internship opportunity.
     * @param internID The internship opportunity ID.
     * @return List of strings representing applications.
     */
    public List<String> viewApp(String internID);

    /**
     * Accepts a placement for an internship opportunity.
     * @param internID The internship opportunity ID.
     * @return Status message indicating success or failure.
     */
    public String acceptPlacement(String internID);

    /**
     * Requests withdrawal from an accepted internship placement.
     * @param internID The internship opportunity ID.
     * @param reason Reason for the withdrawal request.
     * @return Status message indicating success or failure.
     */
    public String requestWithdrawal(String internID, String reason);
}
