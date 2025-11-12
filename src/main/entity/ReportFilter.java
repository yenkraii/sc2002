package src.main.entity;

// Filter criteria for generating reports 
public class ReportFilter {
  private InternshipStatus status;
  private String major; 
  private InternshipLevel level;
  private String companyName;
  private LocalDate closingDate;

  // Default constructor 
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

  // Clear all filters
  public void clearAll(){
    this.status = null;
    this.major = null;
    this.level = null;
    this.companyName = null;
    this.closingDate = null;
  }

  // Check if opportunity matches filter criteria 
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
