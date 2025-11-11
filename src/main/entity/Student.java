package src.main.entity;

import java.util.Objects;

public class Student extends UserAccount {

    private int yearOfStudy;
    private String major;

    public Student(String userId, String name, String password, int yearOfStudy, String major) {
        super(userId, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;

    public String getMajor() {
        return major;

    



    
    
}
