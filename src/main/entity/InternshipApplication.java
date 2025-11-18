package src.main.entity;

import java.time.LocalDate;

/**
 * Represents a student's application for an internship.
 * Tracks application status, applicant, applied internship, submission date, and placement status.
 */
public class InternshipApplication{
    private ApplicationStatus status;
    private Student applicant;
    private InternshipOpportunity internship;
    private LocalDate applicationDate;
    private boolean placementConfirmed; // track if student accepted placement

    /**
     * Constructs a new internship application for a student and opportunity.
     * Application starts as PENDING.
     * @param applicant Student submitting application.
     * @param internship Internship being applied for.
     */
    public InternshipApplication(Student applicant, InternshipOpportunity internship) {
        this.status = ApplicationStatus.PENDING;
        this.applicant = applicant;
        this.internship = internship;
        this.applicationDate = LocalDate.now();
        this.placementConfirmed = false;
    }

    // getters & setters
    public ApplicationStatus getStatus() {
        return status;
    }

    public Student getApplicant() {
        return applicant;
    }

    public InternshipOpportunity getInternship() {
        return internship;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public boolean isPlacementConfirmed() {
        return placementConfirmed;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    /**
     * Sets if the student's placement is confirmed.
     * Also updates the status to CONFIRMED if true.
     * @param confirmed True if confirmed.
     */
    public void setPlacementConfirmed(boolean confirmed) {
        this.placementConfirmed = confirmed;
        if (confirmed) {
            this.status = ApplicationStatus.CONFIRMED;
        }
    }

    /**
     * Withdraws the application, setting its status to WITHDRAWN.
     */
    public void withdraw() {
        this.status = ApplicationStatus.WITHDRAWN;
    }

    // override
    public String toString() {
        String confirmText = placementConfirmed ? " [PLACEMENT CONFIRMED]" : "";
        return String.format("Application: %s for %s | Status: %s%s", applicant.getName(), internship.getInternshipTitle(), status, confirmText);
    }

    public String[] export(){
        return new String[] {applicant.userID, internship.getInternshipID()};
    }
}