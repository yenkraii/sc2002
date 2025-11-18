package src.main.entity;

// Filter criteria for generating reports 

import java.time.LocalDate;
/**
 * Represents filter criteria for generating internship opportunity reports.
 * Stores combinations of status, major, level, company, and closing date.
 */
public class ReportFilter {
  private InternshipStatus status;
  private String major; 
  private InternshipLevel level;
  private String companyName;
  private LocalDate closingDate;

  /**
  * Creates a new, empty filter (no criteria set).
  */
  public ReportFilter(){
  }

  public InternshipStatus getStatus(){
    return status;
  }

  public String getMajor(){
    return major;
  }

  public InternshipLevel getLevel(){
    return level;
  }

  public String getCompanyName(){
    return companyName;
  }

  public LocalDate getClosingDate(){
    return closingDate;
  }

  public void setStatus(InternshipStatus status){
    this.status = status;
  }

  public void setMajor(String major){
    this.major = major;
  }

  public void setLevel(InternshipLevel level){
    this.level = level;
  }

  public void setCompanyName(String companyName){
    this.companyName = companyName;
  }

  public void setClosingDate(LocalDate closingDate){
    this.closingDate = closingDate;
  }

  /**
  * Clears all filter criteria.
  * All fields reset to null.
  */
  public void clearAll(){
    this.status = null;
    this.major = null;
    this.level = null;
    this.companyName = null;
    this.closingDate = null;
  }

  /**
   * Checks whether an internship opportunity matches the filter settings.
   * @param opp InternshipOpportunity to test.
   * @return True if matching, otherwise false.
   */
  public boolean matches(InternshipOpportunity opp){
    if (status!= null && opp.getStatus()!= status)
      return false;
    if (major!= null && !opp.getPreferredMajor().equalsIgnoreCase(major))
      return false;
    if (level!= null && opp.getInternshipLevel()!= level)
      return false;
    if (companyName!= null && !opp.getCompanyName().equals(companyName))
      return false;
    if (closingDate!= null && !opp.getApplicationClosingDate().equals(closingDate))
      return false;
    return true;
  }
}
