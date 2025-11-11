package src.main.entity;

import java.time.LocalDate;

public class InternshipApplication{
    private ApplicationStatus status;
    private Student applicant;
    private InternshipOpportunity internship;
    private LocalDate applicationDate;
    private boolean placementConfirmed;

    public InternshipApplication(Student applicant, InternshipOpportunity internship) {
        this.status = ApplicationStatus.PENDING;
        this.applicant = applicant;
        this.internship = internship;
        this.applicationDate = LocalDate.now();
        this.placementConfirmed = false;
    }

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

    public void withdraw() {
        this.status = ApplicationStatus.WITHDRAWN;
    }
}