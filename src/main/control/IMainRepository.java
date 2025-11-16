package src.main.control;

abstract interface IMainRepository {
    
    public void loadInitialData();

    public void loadCareerStaff(String filename);

    public void loadCompanyReps(String filename);

    public void loadStudents(String filename);

    public void loadUserAuth(String filename);

    public String[] processLine(String line);

}
