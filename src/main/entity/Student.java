package src.main.entity;


// Student entity class
// extends UserAccount to inherit common properties
public class Student extends UserAccount{
    private int yearOfStudy;
    private String major;
    private boolean hasAcceptedPlacement;

    // constructor for Student
    // param userID: student ID
    // param name: student name
    // param password: student password
    // param yearOfStudy: year of study (1-4)
    // param major: student major
    public Student(String UserID, String name, String password, int yearOfStudy, String major) {
        super(UserID, name, password);  // userID might need to be changed to protected
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.hasAcceptedPlacement = false;
    }

    // getters & setters
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public boolean hasAcceptedPlacement() {
        return hasAcceptedPlacement;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setHasAcceptedPlacement(boolean hasAccepted) {
        this.hasAcceptedPlacement = hasAccepted;
    }

    // override
    public String getRole() {
        return "STUDENT";
    }

    // check if student is eligible for internship level
    // param level: internship level to check
    // return true if eligible
    public boolean isEligibleForLevel(InternshipLevel level) {
        if (yearOfStudy <= 2) {
            return level == InternshipLevel.BASIC;
        }
        return true; // year 3+ can apply for any level
    }
}
