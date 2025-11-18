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

    // ✔ Constructor for NEW opportunities (auto-generate ID)
    public InternshipOpportunity(String internshipTitle, String description,
                                 InternshipLevel internshipLevel, String preferredMajor,
                                 LocalDate applicationOpeningDate, LocalDate applicationClosingDate,
                                 String companyName, String companyRepInCharge, int numberOfSlots) {

        this.internshipID = "INT" + String.format("%05d", nextID++);
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

    // ✔ Constructor for LOADING from CSV (keeps ID & all fields)
    public InternshipOpportunity(String internshipID, String internshipTitle, String description,
                                 InternshipLevel internshipLevel, String preferredMajor,
                                 LocalDate applicationOpeningDate, LocalDate applicationClosingDate,
                                 String companyName, String companyRepInCharge, int numberOfSlots,
                                 int slotsConfirmed, boolean visibility, InternshipStatus status) {

        this.internshipID = internshipID;
        this.internshipTitle = internshipTitle;
        this.description = description;
        this.internshipLevel = internshipLevel;
        this.preferredMajor = preferredMajor;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.companyName = companyName;
        this.companyRepInCharge = companyRepInCharge;
        this.numberOfSlots = numberOfSlots;
        this.slotsConfirmed = slotsConfirmed;
        this.visibility = visibility;
        this.status = status;

        // ✔ Ensure auto-generated IDs never collide
        int num = Integer.parseInt(internshipID.substring(3));
        if (num >= nextID) nextID = num + 1;
    }

    public String getInternshipID() { return internshipID; }
    public String getInternshipTitle() { return internshipTitle; }
    public String getDescription() { return description; }
    public InternshipLevel getInternshipLevel() { return internshipLevel; }
    public String getPreferredMajor() { return preferredMajor; }
    public LocalDate getApplicationOpeningDate() { return applicationOpeningDate; }
    public LocalDate getApplicationClosingDate() { return applicationClosingDate; }
    public InternshipStatus getStatus() { return status; }
    public String getCompanyName() { return companyName; }
    public String getCompanyRepInCharge() { return companyRepInCharge; }
    public int getNumberOfSlots() { return numberOfSlots; }
    public int getSlotsConfirmed() { return slotsConfirmed; }
    public boolean isVisible() { return visibility; }

    public int getAvailableSlots() { return numberOfSlots - slotsConfirmed; }

    public void setInternshipTitle(String t){ this.internshipTitle = t; }
    public void setDescription(String d){ this.description = d; }
    public void setInternshipLevel(InternshipLevel l){ this.internshipLevel = l; }
    public void setPreferredMajor(String m){ this.preferredMajor = m; }
    public void setApplicationOpeningDate(LocalDate d){ this.applicationOpeningDate = d; }
    public void setApplicationClosingDate(LocalDate d){ this.applicationClosingDate = d; }
    public void setStatus(InternshipStatus s){ this.status = s; }
    public void setNumberOfSlots(int s){ this.numberOfSlots = s; }
    public void setVisibility(boolean v){ this.visibility = v; }

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
        return status == InternshipStatus.APPROVED &&
               visibility &&
               !today.isBefore(applicationOpeningDate) &&
               !today.isAfter(applicationClosingDate) &&
               status != InternshipStatus.FILLED;
    }

    public boolean isEligibleStudent(Student student){
        return student.isEligibleForLevel(this.internshipLevel);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s) | Slots: %d/%d | Status: %s | Visible: %s",
                internshipID, internshipTitle, companyName, internshipLevel,
                slotsConfirmed, numberOfSlots, status, visibility ? "Yes" : "No");
    }

    // ✔ FULL CSV EXPORT with ID included
    public String[] export(){
        return new String[]{
            internshipID,
            internshipTitle,
            description,
            internshipLevel.toString(),
            preferredMajor,
            applicationOpeningDate.toString(),
            applicationClosingDate.toString(),
            companyName,
            companyRepInCharge,
            String.valueOf(numberOfSlots),
            String.valueOf(slotsConfirmed),
            String.valueOf(visibility),
            status.toString()
        };
    }
}
