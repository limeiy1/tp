package seedu.sgsafe.utils.command;

import seedu.sgsafe.domain.casefiles.Case;
import seedu.sgsafe.domain.casefiles.CaseFormatter;
import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.utils.ui.Display;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a command to find cases based on a keyword and listing mode.
 */
public class FindCommand extends Command {
    /** The keyword to search for in case records. */
    private final String keyword;

    /** The mode that determines which cases to include in the listing. */
    private final CaseListingMode listingMode;

    /**
     * Constructs a FindCommand with the specified keyword and listing mode.
     *
     * @param keyword     The keyword to search for in case records.
     * @param listingMode The mode that determines which cases to include in the listing.
     */
    public FindCommand(String keyword, CaseListingMode listingMode) {
        this.keyword = keyword;
        this.listingMode = listingMode;
    }

    /**
     * Generates a list of display lines for all cases in the provided list.
     *
     * @param caseList The list of cases to generate display lines for.
     * @return A list of strings representing the display lines of the cases.
     */
    private List<String> getAllCaseDisplayLines(ArrayList<Case> caseList) {
        List<String> lines = new ArrayList<>();
        for (Case c : caseList) {
            lines.add(c.getDisplayLine());
        }
        return lines;
    }

    /**
     * Generates the header message for the case list table.
     *
     * @return A formatted string representing the table header.
     */
    private String generateListTableHeaderMessage() {
        return String.format(CaseFormatter.SUMMARY_FORMAT_STRING, "STATUS", "CATEGORY", "ID", "DATE", "TITLE");
    }

    /**
     * Generates an array of case descriptions based on the provided case list.
     *
     * @param caseList The list of cases to generate descriptions for.
     * @return An array of strings representing the case descriptions.
     */
    private String[] getCaseDescriptions(ArrayList<Case> caseList) {
        int count = caseList.size();
        List<String> outputLines = new ArrayList<>();

        if (count == 0) {
            outputLines.add("No cases found matching the keyword.");
            return outputLines.toArray(new String[0]);
        }

        outputLines.add("A total of " + count + " case(s) found matching the keyword: \"" + keyword + "\"");

        outputLines.add("To view more details of a case, use the command: read CASE_ID");

        outputLines.add(generateListTableHeaderMessage());

        List<String> formattedCaseLines = getAllCaseDisplayLines(caseList);
        outputLines.addAll(formattedCaseLines);

        return outputLines.toArray(new String[0]);
    }

    /**
     * Filters the provided list of cases based on their visibility and the listing mode.
     *
     * @param caseList The list of cases to filter.
     * @return A filtered list of cases.
     */
    private ArrayList<Case> filterCases(ArrayList<Case> caseList) {
        ArrayList<Case> filteredCases = new ArrayList<>();
        for (Case c : caseList) {
            if (isCaseVisible(c)) {
                filteredCases.add(c);
            }
        }
        return filteredCases;
    }

    /**
     * Determines whether a case is visible based on its status and the listing mode.
     *
     * @param caseToCheck The case to check for visibility.
     * @return True if the case is visible, false otherwise.
     */
    private Boolean isCaseVisible(Case caseToCheck) {
        if (caseToCheck.isDeleted()) {
            return false;
        }
        return switch (this.listingMode) {
        case OPEN_ONLY -> caseToCheck.isOpen();
        case CLOSED_ONLY -> !caseToCheck.isOpen();
        default -> true;
        };
    }

    /**
     * Executes the FindCommand by searching for cases matching the keyword,
     * filtering them based on the listing mode, and displaying the results.
     */
    @Override
    public void execute() {
        ArrayList<Case> caseList = CaseManager.findCasesByKeyword(keyword);
        ArrayList<Case> filteredCaseList = filterCases(caseList);
        String[] caseDescriptions = getCaseDescriptions(filteredCaseList);
        Display.printMessage(caseDescriptions);
    }
}
