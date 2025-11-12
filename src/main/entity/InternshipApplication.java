package src.main.entity;

import java.time.LocalDate;

// represents student's application for an internship
public class InternshipApplication{
    private ApplicationStatus status;
    private Student applicant;
    private InternshipOpportunity internship;
    private LocalDate applicationDate;
    private boolean placementConfirmed; // track if student accepted placement

    // constructor for InternshipApplication
    // param applicant: student applying
    // param internship: internship being applied to
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

    public void setPlacementConfirmed(boolean confirmed) {
        this.placementConfirmed = confirmed;
        if (confirmed) {
            this.status = ApplicationStatus.CONFIRMED;
        }
    }

    // withdraw application
    public void withdraw() {
        this.status = ApplicationStatus.WITHDRAWN;
    }

    // override
    public String toString() {
        String confirmText = placementConfirmed ? " [PLACEMENT CONFIRMED]" : "";
        return String.format("Application: %s for %s | Status: %s%s", applicant.getName(), internship.getInternshipTitle(), status, confirmText);
    }
}