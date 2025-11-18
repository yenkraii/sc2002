package src.main.control;

abstract interface IMainRepository {

    /**
     * Loads all initial data required by the system.
     */
    void loadInitialData();

    /**
     * Loads career centre staff data from the specified file.
     * @param filename Name of the file containing staff data.
     */
    void loadCareerStaff(String filename);

    /**
     * Loads company representatives' data from the specified file.
     * @param filename Name of the file containing company reps data.
     */
    void loadCompanyReps(String filename);

    /**
     * Loads student account data from the specified file.
     * @param filename Name of the file containing students data.
     */
    void loadStudents(String filename);

    /**
     * Loads user authentication data from the specified file.
     * @param filename Name of the file containing authentication data.
     */
    void loadUserAuth(String filename);

    /**
     * Loads internship opportunity data from the specified file.
     * @param filename Name of the file containing internship opportunities.
     */
    void loadInternOpp(String filename);

    /**
     * Loads internship application data from the specified file.
     * @param filename Name of the file containing internship applications.
     */
    void loadInternApp(String filename);

    /**
     * Processes a line of input for conversion to repository data.
     * @param line Line of text to process.
     * @return Outcome string after processing.
     */
    String processLine(String line);

    /**
     * Saves all data and performs any required finalization procedures.
     */
    void saveFinalData();

    /**
     * Saves all user data to the specified staff, company, and student files.
     * @param staffFile File path for staff data.
     * @param companyFile File path for company reps data.
     * @param studentFile File path for students data.
     */
    void saveUsers(String staffFile, String companyFile, String studentFile);

    /**
     * Saves user authentication data to the specified file.
     * @param filename Name of the file to save authentication data.
     */
    void saveUserAuth(String filename);

    /**
     * Saves internship opportunity data to the specified file.
     * @param filename Name of the file to save opportunity data.
     */
    void saveInternOpp(String filename);

    /**
     * Saves internship application data to the specified file.
     * @param filename Name of the file to save application data.
     */
    void saveInternApp(String filename);
}
