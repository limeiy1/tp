package seedu.sgsafe.domain.casefiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.sgsafe.utils.exceptions.CaseAlreadyClosedException;
import seedu.sgsafe.utils.exceptions.CaseCannotBeEditedException;
import seedu.sgsafe.utils.exceptions.CaseAlreadyOpenException;
import seedu.sgsafe.utils.exceptions.CaseNotFoundException;
import seedu.sgsafe.utils.exceptions.InvalidEditFlagException;

/**
 * Manages the collection of {@link Case} objects in the SGSafe system.
 * Provides functionality to store, retrieve, and display case records.
 */
public class CaseManager {

    private static final Logger logger = Logger.getLogger(CaseManager.class.getName());
    // Format string for generating case IDs
    private static final String CASE_ID_FORMAT = "%06x";

    /**
     * The central list of case records maintained by the application.
     * Each {@link Case} represents a single incident or report.
     */
    private static ArrayList<Case> caseList = new ArrayList<>();

    public static int getCaseListSize() {
        return caseList.size();
    }

    public static ArrayList<Case> getCaseList() {
        return caseList;
    }

    /**
     * Adds a new case to the case list.
     *
     * @param newCase the {@link Case} object to be added
     */
    public static void addCase(Case newCase) {
        assert newCase != null : "newCase should not be null";
        caseList.add(newCase);
    }

    //@@author shennontay
    /**
     * Finds and returns a {@link Case} object from the case list using its unique ID.
     *
     * @param id the hexadecimal ID of the case to find
     * @return the Case with the matching ID, or null if not found
     */
    public static Case getCaseById(String id) {
        logger.log(Level.FINE, "Fetching case by ID: " + id);
        String lookupId = id.toLowerCase();

        Case result = caseList.stream()
                .filter(c -> c.getId().equals(lookupId))
                .filter(c -> !c.isDeleted())
                .findFirst()
                .orElse(null);

        if (result == null) {
            logger.log(Level.FINE, "No case found: " + id);
        }
        return result;
    }
    //@@author

    /**
     * Closes an existing case in the case list.
     *
     * @param caseId the case to be closed
     */
    public static String closeCase(String caseId) throws CaseNotFoundException {
        Case caseToClose = getCaseById(caseId);
        if (caseToClose == null) {
            throw new CaseNotFoundException(caseId);
        }
        if (!caseToClose.isOpen()) {
            throw new CaseAlreadyClosedException(caseId);
        }
        caseToClose.setClosed();
        assert !caseToClose.isOpen() : "Case should be closed";
        return caseToClose.getDisplayLine();
    }

    /**
     * Reopens a closed case in the case list.
     *
     * @param caseId the case to be opened
     */
    public static String openCase(String caseId) throws CaseNotFoundException {
        Case caseToOpen = getCaseById(caseId);
        if (caseToOpen == null) {
            throw new CaseNotFoundException(caseId);
        }
        if (caseToOpen.isOpen()) {
            throw new CaseAlreadyOpenException(caseId);
        }
        caseToOpen.setOpen();
        assert caseToOpen.isOpen() : "Case should be open";
        return caseToOpen.getDisplayLine();
    }

    /**
     * Updates an existing {@link Case} with new field values.
     * <p>
     * Finds the case by its {@code caseId} using {@link #getCaseById(String)} and applies
     * the updates from {@code newFlagValues} via {@link Case#update(Map)}.
     *
     * @param caseId the 6-character hexadecimal case ID
     * @param newFlagValues map of field names to new values
     * @return the updated case’s display line
     * @throws CaseNotFoundException   if no case with the given ID exists
     * @throws InvalidEditFlagException  if any flags in {@code newFlagValues} are invalid
     * @throws CaseCannotBeEditedException if the case is closed and cannot be edited
     */
    public static String editCase(String caseId, Map<String, Object> newFlagValues)
            throws CaseNotFoundException, InvalidEditFlagException, CaseCannotBeEditedException {

        Case caseToEdit = getEditableCase(caseId);
        validateEditFlags(caseToEdit, newFlagValues);

        caseToEdit.update(newFlagValues);
        return caseToEdit.getDisplayLine();
    }

    /* ----------- editCase() Helpers ----------- */
    //@@author limeiy1
    /**
     * Retrieves a case by its ID and ensures it is editable (i.e., exists and is open).
     *
     * @param caseId the ID of the case to retrieve
     * @return the editable Case object
     * @throws CaseNotFoundException        if no case with the given ID exists
     * @throws CaseCannotBeEditedException  if the case is closed and cannot be edited
     */
    public static Case getEditableCase(String caseId) throws CaseNotFoundException, CaseCannotBeEditedException {
        Case caseToEdit = getCaseById(caseId);

        if (caseToEdit == null) {
            throw new CaseNotFoundException(caseId);
        }

        if (!caseToEdit.isOpen()) {
            logger.log(Level.WARNING, "Attempt to edit closed case: " + caseId);
            throw new CaseCannotBeEditedException(caseId);
        }

        return caseToEdit;
    }
    //@@ author

    private static void validateEditFlags(Case caseToEdit, Map<String, Object> newFlagValues) {
        List<String> invalidFlags = getInvalidEditFlags(caseToEdit, newFlagValues);

        if (!invalidFlags.isEmpty()) {
            throw new InvalidEditFlagException(invalidFlags, caseToEdit.getId());
        }
    }

    /**
     * Checks which flags in {@code newFlagValues} are invalid for the given {@link Case}.
     * <p>
     * Only the flag names (map keys) are validated; values are ignored.
     *
     * @param targetCase    the case whose valid flags are used for validation
     * @param newFlagValues a map of flag names and their corresponding values
     * @return list of invalid flag names; empty if all are valid
     */
    private static List<String> getInvalidEditFlags(Case targetCase, Map<String, Object> newFlagValues) {
        assert targetCase != null : "Target case must not be null";
        assert newFlagValues != null : "Input flag map must not be null";

        List<String> validFlags = targetCase.getValidEditFlags();
        List<String> invalidFlags = new ArrayList<>();

        for (String flag : newFlagValues.keySet()) {
            if (!validFlags.contains(flag)) {
                invalidFlags.add(flag);
            }
        }

        return invalidFlags;
    }

    /**
     * Deletes an existing case in the case list using its id.
     * Throws an {@link CaseNotFoundException} if the case does not exist or has been deleted.
     *
     * @param caseId the id of the case to be deleted.
     * @return the deleted case's display line.
     */
    public static String deleteCase(String caseId) throws CaseNotFoundException {
        Case caseToDelete = getCaseById(caseId);
        if (caseToDelete == null) {
            throw new CaseNotFoundException(caseId);
        }
        caseToDelete.setDeleted(true);
        return caseToDelete.getDisplayLine();
    }

    public static ArrayList<Case> findCasesByKeyword(String keyword) {
        ArrayList<Case> casesFound = new ArrayList<>();
        for (Case c : caseList) {
            if (c.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                casesFound.add(c);
            }
        }
        return casesFound;
    }
      
    /**
     * Reads and returns the display representation of a case.
     * Throws an {@link CaseNotFoundException} if the case does not exist or has been deleted.
     *
     * @param caseId the id of the case to be read.
     * @return the case's display representation as a String array.
     */
    public static String[] readCase(String caseId) throws CaseNotFoundException {
        Case caseToRead = getCaseById(caseId);
        if (caseToRead == null) {
            throw new CaseNotFoundException(caseId);
        }
        return caseToRead.getReadCaseDisplay();
    }

    // @@author xelisce

    /**
     * Generates a unique 6-character hexadecimal ID for a new case.
     * <p>
     * The ID is derived from the current size of {@code caseList}, formatted as a
     * zero-padded lowercase hexadecimal string. This ensures compact, readable,
     * and collision-free identifiers as long as cases are not removed or reordered.
     * <p>
     * Example outputs:
     * <ul>
     *   <li>{@code 000000} — first case</li>
     *   <li>{@code 00000a} — tenth case</li>
     *   <li>{@code 0000ff} — 256th case</li>
     * </ul>
     *
     * @return a 6-character hexadecimal string representing the new case ID
     */
    public static String generateHexId() {
        int raw = getCaseListSize();
        return String.format(CASE_ID_FORMAT, raw); // zero-padded 6-digit hex
    }

}
