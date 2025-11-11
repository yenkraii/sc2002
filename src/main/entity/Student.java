package src.main.entity;

public class Student extends UserAccount {
    private int yearOfStudy;
    private String major;
    private boolean hasAcceptedPlacement;

    public Student(String UserID, String name, String password, int yearOfStudy, String major) {
        super(userID, name, password);  // userID might need to be changed to protected
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.hasAcceptedPlacement = false;
    }

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

    public String getRole() {
        return "STUDENT";
    }

    public boolean isEligibleForLevel(InternshipLevel level) {
        if (yearOfStudy <= 2) {
            return level == InternshipLevel.BASIC;
        }
        return true;
    }
}
