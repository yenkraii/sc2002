package src.main.entity;

import java.time.LocalDate;

// represents student's request to withdraw from an internship
// requires center staff approval
public class WithdrawalRequest {
    private InternshipApplication application;
    private Student student;
    private LocalDate requestDate;
    private String reason;
    private boolean isAfterConfirmation;

    // constructor for WithdrawalRequest
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
