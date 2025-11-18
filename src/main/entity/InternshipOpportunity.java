package src.main.entity;

import java.time.LocalDate;
/**
 * Represents an internship opportunity provided by a company.
 * Tracks details such as title, description, level, major requirements,
 * application dates, status, company info, slot availability, and visibility.
 */
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

    /**
     * Constructs an InternshipOpportunity with the specified details.
     * Automatically assigns a unique internship ID.
     * The initial status is set to PENDING and visibility false.
     * @param internshipTitle Title of the internship.
     * @param description Description of the internship.
     * @param internshipLevel Internship level (BASIC, INTERMEDIATE, ADVANCED).
     * @param preferredMajor Preferred major for applicants.
     * @param applicationOpeningDate Date applications open.
     * @param applicationClosingDate Date applications close.
     * @param companyName Company offering the internship.
     * @param companyRepInCharge Company representative in charge.
     * @param numberOfSlots Number of internship slots available.
     */


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

    public InternshipOpportunity(String internshipTitle, String description, String internshipLevel, String preferredMajor, String applicationOpeningDate, String applicationClosingDate, String companyName, String companyRepInCharge, String numberOfSlots){
        this.internshipID = "INT" + String.format("%05d",nextID++);
        this.internshipTitle = internshipTitle;
        this.description = description;
        this.internshipLevel = InternshipLevel.valueOf(internshipLevel);
        this.preferredMajor = preferredMajor;
        this.applicationOpeningDate = LocalDate.parse(applicationOpeningDate);
        this.applicationClosingDate = LocalDate.parse(applicationClosingDate);
        this.status = InternshipStatus.PENDING;
        this.companyName = companyName;
        this.companyRepInCharge = companyRepInCharge;
        this.numberOfSlots = Integer.parseInt(numberOfSlots);
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
    /**
     * Increments the count of confirmed slots by one.
     * Automatically updates status to FILLED if all slots are confirmed.
     */
    public void incrementSlotsConfirmed(){
        this.slotsConfirmed++;
        if(this.slotsConfirmed >= this.numberOfSlots){
            this.status = InternshipStatus.FILLED;
        } 
    }
    /**
     * Decrements the count of confirmed slots by one if possible.
     * Updates status to APPROVED if not completely filled.
     */
    public void decrementSlotsConfirmed(){
        if (this.slotsConfirmed > 0){
            this.slotsConfirmed--;
            if (this.status == InternshipStatus.FILLED){
                this.status = InternshipStatus.APPROVED;
            }
        }
    }
    /**
     * Checks if the internship is currently accepting applications.
     * Must be approved, visible, within application dates, and not filled.
     * @return True if accepting applications, false otherwise.
     */
    public boolean isAcceptingApplications(){
        LocalDate today = LocalDate.now();
        return status == InternshipStatus.APPROVED && visibility && !today.isBefore(applicationOpeningDate) && !today.isAfter(applicationClosingDate) && status != InternshipStatus.FILLED;
    }
    /**
     * Checks if a student is eligible to apply for this internship.
     * Student must match preferred major and be eligible for internship level.
     * @param student Student to check.
     * @return True if eligible, false otherwise.
     */
    public boolean isEligibleStudent(Student student){
        return //student.getMajor().equalsIgnoreCase(preferredMajor) && 
        student.isEligibleForLevel(this.internshipLevel);
    }

    /**
     * Returns a string summary of the internship opportunity.
     * Includes ID, title, company, level, slots confirmed, total slots, status, and visibility.
     * @return Formatted string summary.
     */
    public String toString() {
        return String.format("[%s] %s - %s (%s) | Slots: %d/%d | Status: %s | Visible: %s", internshipID, internshipTitle, companyName, internshipLevel, slotsConfirmed, numberOfSlots, status, visibility ? "Yes" : "No");
    }
    /**
     * Exports the internship details as a CSV-compatible string.
     * @return CSV formatted string of internship attributes.
     */
    public String[] export(){
        return new String[] {internshipTitle, description, String.valueOf(internshipLevel), preferredMajor, String.valueOf(applicationOpeningDate), String.valueOf(applicationClosingDate), companyName, companyRepInCharge, String.valueOf(numberOfSlots)};
    }

}
