package src.main.entity;

/**
 * Status of a student's internship application.
 * Tracks progress and outcome of internship applications.
 */
public enum ApplicationStatus {
    /** Application is awaiting decision. */
    PENDING,
    /** Application was successful. */
    SUCCESSFUL,
    /** Application was unsuccessful. */
    UNSUCCESSFUL,
    /** Application was withdrawn by the student. */
    WITHDRAWN,
    /** Application is confirmed for placement. */
    CONFIRMED // for placement confirmation tracking
}
