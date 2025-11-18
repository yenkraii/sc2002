package src.main.entity;

/**
 * Indicates the current status of a user account in the system.
 */
public enum AccountStatus {
    /** Account is pending approval by staff. */
    PENDINGAPPROVAL,
    /** Account has been approved for use. */
    APPROVED,
    /** Account registration has been rejected. */
    REJECTED,
    /** Account has been suspended due to violations or inactivity. */
    SUSPENDED
}
