package src.main.entity;

import java.time.LocalDate;

public class InternshipOpportunity {
    private static int nextID = 1;

    private String internshipID;
    private String internshipTitle;
    private String description;
    private InternshipLevel internshipLevel;
    private String preferredMajor;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private InternshipStatus status;
    private String companyName;
    private String companyRepInCharge;
    private int numberOfSlots;
    private int slotsConfirmed;
    private boolean visibility;

    public InternshipOpportunity(String internshipTitle, String description, InternshipLevel internshipLevel, String preferredMajor, LocalDate applicationOpeningDate, LocalDate applicationClosingDate, String companyName, String companyRepInCharge, int numberOfSlots){
        this.internshipID = "INT" + String.format("%05d",nextID++);
        this.internshipTitle = internshipTitle;
        this.description = description;
        this.internshipLevel = internshipLevel;
        this.preferredMajor = preferredMajor;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.status = InternshipStatus.PENDING;
        this.companyName = companyName;
        this.companyRepInCharge = companyRepInCharge;
        this.numberOfSlots = numberOfSlots;
        this.slotsConfirmed = 0;
        this.visibility = false;
    }

    public String getInternshipID(){
        return internshipID;
    }

    public String getInternshipTitle(){
        return internshipTitle;
    }

    public String getDescription(){
        return description;
    }

    public InternshipLevel getInternshipLevel(){
        return internshipLevel;
    }

    public String getPreferredMajor(){
        return preferredMajor;
    }

    public LocalDate getApplicationOpeningDate(){
        return applicationOpeningDate;
    }
    
    public LocalDate getApplicationClosingDate(){
        return applicationClosingDate;
    }

    public InternshipStatus getStatus(){
        return status;
    }
    
    public String getCompanyName(){
        return companyName;
    }
    
    public String getCompanyRepInCharge(){
        return companyRepInCharge;
    }
    
    public int getNumberOfSlots(){
        return numberOfSlots;
    }

    public int getSlotsConfirmed(){
        return slotsConfirmed;
    }

    public boolean isVisible(){
        return visibility;
    }

    public int getAvailableSlots(){
        return numberOfSlots - slotsConfirmed;
    }

    public void setInternshipTitle(String internshipTitle){
        this.internshipTitle = internshipTitle;
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public void setInternshipLevel(InternshipLevel internshipLevel) { 
        this.internshipLevel = internshipLevel; 
    }
    
    public void setPreferredMajor(String preferredMajor) { 
        this.preferredMajor = preferredMajor; 
    }
    public void setApplicationOpeningDate(LocalDate date) { 
        this.applicationOpeningDate = date; 
    }
    
    public void setApplicationClosingDate(LocalDate date) { 
        this.applicationClosingDate = date; 
    }
    
    public void setStatus(InternshipStatus status) { 
        this.status = status; 
    }
    
    public void setNumberOfSlots(int numberOfSlots) { 
        this.numberOfSlots = numberOfSlots; 
    }
    
    public void setVisibility(boolean visibility) { 
        this.visibility = visibility; 
    }

    public void incrementSlotsConfirmed(){
        this.slotsConfirmed++;
        if(this.slotsConfirmed >= this.numberOfSlots){
            this.status = InternshipStatus.FILLED;
        } 
    }

    public void decrementSlotsConfirmed(){
        if (this.slotsConfirmed > 0){
            this.slotsConfirmed--;
            if (this.status == InternshipStatus.FILLED){
                this.status = InternshipStatus.APPROVED;
            }
        }
    }

    public boolean isAcceptingApplications(){
        LocalDate today = LocalDate.now();
        return status == InternshipStatus.APPROVED && visibility && !today.isBefore(applicationOpeningDate) && !today.isAfter(applicationClosingDate) && status != InternshipStatus.FILLED;
    }

    public boolean isEligibleStudent(Student student){
        return student.getMajor().equalsIgnoreCase(preferredMajor) && student.isEligibleForLevel(this.internshipLevel);
    }
    
    public String toString() {
        return String.format("[%s] %s - %s (%s) | Slots: %d/%d | Status: %s | Visible: %s", internshipID, internshipTitle, companyName, internshipLevel, slotsConfirmed, numberOfSlots, status, visibility ? "Yes" : "No");
    } 
}
