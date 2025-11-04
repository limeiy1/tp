package seedu.sgsafe.utils.command;

/**
 * Specifies the mode for listing case files in the SGSafe system.
 * Determines whether the command should list open cases, closed cases, or all cases.
 */
public enum CaseListingMode {
    /** Defaults to listing all cases when command has no input arguments*/
    DEFAULT,

    /** List only open cases. */
    OPEN_ONLY,

    /** List only closed cases. */
    CLOSED_ONLY
}
