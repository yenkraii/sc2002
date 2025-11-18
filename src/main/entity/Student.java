package src.main.entity;

/**
 * Represents a student user in the internship system.
 * Extends UserAccount and contains student-specific properties.
 */
public class Student extends UserAccount{
    private int yearOfStudy;
    private String major;
    private boolean hasAcceptedPlacement;

    /**
     * Constructs a Student with specified details.
     * @param userID Student ID.
     * @param name Student’s name.
     * @param password Student’s password.
     * @param email Student’s email.
     * @param yearOfStudy Year of study (1-4).
     * @param major Student’s major.
     */
    public Student(String UserID, String name, String password, String email, int yearOfStudy, String major) {
        super(UserID, name, password, email);  // userID might need to be changed to protected
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

    /**
     * Checks if the student is eligible for a given internship level.
     * @param level Internship level to check.
     * @return True if eligible, otherwise false.
     */
    public boolean isEligibleForLevel(InternshipLevel level) {
        if (yearOfStudy <= 2) {
            return level == InternshipLevel.BASIC;
        }
        return true; // year 3+ can apply for any level
    }

    public String[] export(){
        return new String[] {userID, name, major, String.valueOf(yearOfStudy), email};
    }

}
