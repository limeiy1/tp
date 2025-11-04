package seedu.sgsafe.utils.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.sgsafe.domain.casefiles.Case;
import seedu.sgsafe.domain.casefiles.CaseFormatter;
import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.utils.ui.Display;

/**
 * Represents a command that lists case files in the SGSafe system.
 * <p>
 * This command supports multiple listing modes to filter cases:
 * {@link CaseListingMode#OPEN_ONLY}, {@link CaseListingMode#CLOSED_ONLY},
 * and {@link CaseListingMode#DEFAULT}.
 * It also supports verbose mode to display detailed case information.
 */
public class ListCommand extends Command {

    /** The mode that determines which cases to include in the listing. */
    private final CaseListingMode listingMode;

    /** Whether to display cases in verbose (multi-line) format. */
    private final boolean isVerbose;

    /**
     * Constructs a {@code ListCommand} with the specified listing mode and verbosity.
     *
     * @param listingMode the mode used to filter cases for display
     * @param isVerbose   whether to display cases in verbose format
     */
    public ListCommand(CaseListingMode listingMode, boolean isVerbose) {
        this.commandType = CommandType.LIST;
        this.listingMode = listingMode;
        this.isVerbose = isVerbose;
    }

    /**
     * Returns the listing mode associated with this command.
     *
     * @return the {@link CaseListingMode} used for filtering
     */
    public CaseListingMode getListingMode() {
        return this.listingMode;
    }

    /**
     * Executes the command by retrieving and displaying the filtered list of cases.
     * <p>
     * The case list is obtained from {@link CaseManager#getCaseList()},
     * formatted using {@link #getCaseDescriptions(ArrayList)}, and printed via {@link Display#printMessage(String[])}.
     */
    @Override
    public void execute() {
        ArrayList<Case> caseList = CaseManager.getCaseList();
        String[] caseDescriptions = getCaseDescriptions(caseList);
        Display.printMessage(caseDescriptions);
    }

    /**
     * Generates a formatted array of case descriptions based on the current {@link CaseListingMode}
     * and verbosity setting.
     * <p>
     * The output includes:
     * <ul>
     *   <li>A summary line indicating the number of matching cases</li>
     *   <li>A table header line (only in non-verbose mode)</li>
     *   <li>Formatted case entries:
     *     <ul>
     *       <li>In summary mode: one line per case via {@link Case#getDisplayLine()}</li>
     *       <li>In verbose mode: multiple lines per case via {@link Case#getMultiLineVerboseDisplay()}</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * @param caseList the full list of cases to filter and format
     * @return an array of formatted strings representing the filtered and formatted case descriptions
     */
    String[] getCaseDescriptions(ArrayList<Case> caseList) {
        ArrayList<Case> matchingCases = filterCasesByMode(caseList);
        int count = matchingCases.size();
        List<String> outputLines = new ArrayList<>();

        outputLines.add(generateCaseCountMessage(count));
        if (!isVerbose && !matchingCases.isEmpty()) {
            // Add explanatory message as separate lines with a divider
            outputLines.add("---");
            outputLines.add("Note: Only very basic case details are shown here.");
            outputLines.add("For more in depth information about the case (e.g. Info, Victim, Officer)");
            outputLines.add("run: list --mode verbose");
            outputLines.add("---");
            outputLines.add(generateListTableHeaderMessage());
        } else if (isVerbose && !matchingCases.isEmpty()) {
            // Add explanatory message as separate lines with a divider
            outputLines.add("---");
            outputLines.add("Note: Only basic case details (e.g. Title) are shown here and is truncated if too long.");
            outputLines.add("For full case information (e.g. case-specific details like murder weapon),");
            outputLines.add("use the read command");
            outputLines.add("To use the read command, run: read <caseID>");
            outputLines.add("---");
        }
        List<String> formattedCaseLines = formatCases(matchingCases);
        outputLines.addAll(formattedCaseLines);

        return outputLines.toArray(new String[0]);
    }

    /**
     * Generates a header line for the case listing table in summary mode.
     * <p>
     * This header aligns with the format produced by {@link Case#getDisplayLine()},
     * showing column labels for status, ID, date, and title.
     *
     * @return a formatted header string for the case list table
     */
    private String generateListTableHeaderMessage() {
        return String.format(CaseFormatter.SUMMARY_FORMAT_STRING, "STATUS", "CATEGORY", "ID", "DATE", "TITLE");
    }

    /**
     * Formats a list of cases based on the current verbosity setting.
     * <p>
     * In verbose mode, each case is rendered using {@link Case#getMultiLineVerboseDisplay()},
     * prefixed with a divider line. In summary mode, each case is rendered using {@link Case#getDisplayLine()}.
     *
     * @param cases the list of cases to format
     * @return a list of formatted strings representing each case
     */
    private List<String> formatCases(List<Case> cases) {
        List<String> lines = new ArrayList<>();

        for (Case currentCase : cases) {
            if (this.isVerbose) {
                String[] currentLines = currentCase.getMultiLineVerboseDisplay();
                lines.addAll(Arrays.asList(currentLines));
            } else {
                String currentLine = currentCase.getDisplayLine();
                lines.add(currentLine);
            }
        }

        return lines;
    }

    /**
     * Filters the provided list of cases based on the current {@link CaseListingMode}.
     *
     * @param caseList the full list of cases to filter
     * @return a new list containing only the cases that match the filter criteria
     */
    private ArrayList<Case> filterCasesByMode(ArrayList<Case> caseList) {
        return new ArrayList<>(
                caseList.stream()
                        .filter(this::isCaseVisible)
                        .toList()
        );
    }

    /**
     * Determines whether a given case should be included in the listing based on the current mode.
     *
     * @param caseRecord the case to evaluate
     * @return {@code true} if the case matches the filter criteria; {@code false} otherwise
     */
    private boolean isCaseVisible(Case caseRecord) {
        if (caseRecord.isDeleted()) {
            return false;
        }
        return switch (this.listingMode) {
        case OPEN_ONLY -> caseRecord.isOpen();
        case CLOSED_ONLY -> !caseRecord.isOpen();
        case DEFAULT -> true;
        };
    }

    /**
     * Generates a summary header message based on the number of matching cases and the current {@link CaseListingMode}.
     * <p>
     * The message format varies depending on the count and mode:
     * <ul>
     *   <li>If {@code caseCount == 0}: {@code "You currently have no cases [status]. Add some now!"}</li>
     *   <li>If {@code caseCount == 1}: {@code "You currently have 1 case [status]"}</li>
     *   <li>If {@code caseCount > 1}: {@code "You currently have n cases [status]"}</li>
     * </ul>
     * The status label reflects the listing mode:
     * <ul>
     *   <li>{@code OPEN_ONLY} → "open"</li>
     *   <li>{@code CLOSED_ONLY} → "closed"</li>
     *   <li>{@code DEFAULT} → "in total"</li>
     * </ul>
     *
     * @param caseCount the number of cases matching the current listing mode
     * @return a formatted summary message describing the case count and status
     */
    private String generateCaseCountMessage(int caseCount) {
        String statusLabel = switch (this.listingMode) {
        case OPEN_ONLY -> "open";
        case CLOSED_ONLY -> "closed";
        case DEFAULT -> "in total";
        };

        if (caseCount == 0) {
            return "You currently have no cases " + statusLabel + ". Add some now!";
        } else if (caseCount == 1) {
            return "You currently have 1 case " + statusLabel;
        } else {
            return "You currently have " + caseCount + " cases " + statusLabel;
        }
    }
}
