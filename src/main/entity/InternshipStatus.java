package src.main.entity;

/**
 * Status of an internship opportunity for a student.
 */
public enum InternshipStatus {
    /** Opportunity is awaiting approval. */
    PENDING,
    /** Opportunity has been approved and is available. */
    APPROVED,
    /** Opportunity has been rejected and is unavailable. */
    REJECTED,
    /** Internship opportunity is filled. */
    FILLED
}
