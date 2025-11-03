package seedu.sgsafe.utils.command;

import seedu.sgsafe.domain.casefiles.Case;
import seedu.sgsafe.domain.casefiles.CaseFormatter;
import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.utils.ui.Display;

import java.util.ArrayList;
import java.util.List;

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.commandType = CommandType.FIND;
        this.keyword = keyword;
    }

    private List<String> getAllCaseDisplayLines (ArrayList<Case> caseList) {
        List<String> lines = new ArrayList<>();
        for (Case c : caseList) {
            lines.add(c.getDisplayLine());
        }
        return lines;
    }

    private String generateListTableHeaderMessage() {
        return String.format(CaseFormatter.SUMMARY_FORMAT_STRING, "STATUS", "CATEGORY", "ID", "DATE", "TITLE");
    }


    private String[] getCaseDescriptions(ArrayList<Case> caseList) {
        int count = caseList.size();
        List<String> outputLines = new ArrayList<>();

        if(count == 0) {
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

    @Override
    public void execute() {
        ArrayList<Case> caseList = CaseManager.findCasesByKeyword(keyword);
        String[] caseDescriptions = getCaseDescriptions(caseList);
        Display.printMessage(caseDescriptions);
    }
}
