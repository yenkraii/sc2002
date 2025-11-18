package src.main.entity;

import java.time.LocalDate;

import src.main.entity.WithdrawalStatus;

/**
 * Represents a student's request to withdraw from an internship placement.
 * Withdrawal requires approval from centre staff.
 */
public class WithdrawalRequest {
    private InternshipApplication application;
    private Student student;
    private LocalDate requestDate;
    private String reason;
    private boolean isAfterConfirmation;
    private WithdrawalStatus status;
    /**
     * Constructs a WithdrawalRequest for a specified internship application and student.
     * Initially set to PENDING status with reason and current request date.
     * Tracks if request is after placement confirmation.
     * @param application The internship application being withdrawn from.
     * @param student The student making the withdrawal request.
     * @param reason Explanation for withdrawal.
     */
    public WithdrawalRequest(InternshipApplication application, Student student, String reason) {
        this.application = application;
        this.student = student;
        this.status = WithdrawalStatus.PENDING;
        this.requestDate = LocalDate.now();
        this.reason = reason;
        this.isAfterConfirmation = application.isPlacementConfirmed();
    }

    // getters & setters
    public InternshipApplication getApplication() {
        return application;
    }

    public Student getStudent() {
        return student;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public String getReason() {
        return reason;
    }

    public boolean isAfterConfirmation() {
        return isAfterConfirmation;
    }

    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    // override
    public String toString() {
        String timing = isAfterConfirmation ? "After Confirmation" : "Before Confirmation";
        return String.format("Withdrawal Request: %s | %s | Status: %s | %s", student.getName(), application.getInternship().getInternshipTitle(), status, timing);
    }

}
