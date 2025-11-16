package src.main.control;

abstract interface IMainRepository {
    
    public void loadInitialData();

    public void loadCareerStaff(String filename);
    public void loadCompanyReps(String filename);
    public void loadStudents(String filename);
    public void loadUserAuth(String filename);
    public void loadInternOpp(String filename);
    public void loadInternApp(String filename);

    public String[] processLine(String line);

    public void saveFinalData();

    public void saveUsers(String staffFile, String companyFile, String studentFile);
    public void saveUserAuth(String filename);
    public void saveInternOpp(String filename);
    public void saveInternApp(String filename);

}
