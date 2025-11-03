package seedu.sgsafe.utils.command;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.sgsafe.domain.casefiles.Case;
import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.domain.casefiles.type.violent.RobberyCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;


public class EditPromptCommandTest {

    private ByteArrayOutputStream out;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        CaseManager.getCaseList().clear();

        out = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(out));
    }

    private String getOutput() {
        System.setOut(originalOut);
        return out.toString();
    }

    @Test
    void execute_caseExistsAndOpen_displaysValidFlags() {
        LocalDate date = LocalDate.of(2025, 10, 10);
        Case openCase = new RobberyCase("000001", "Robbery", date, "Suspect masked", "Alice", "Officer Tan");
        CaseManager.addCase(openCase);

        EditPromptCommand cmd = new EditPromptCommand("000001");
        cmd.execute();

        String output = getOutput().toLowerCase();

        assertTrue(output.contains("case found"));
        assertTrue(output.contains("fields that can be edited"));
        assertTrue(output.contains("usage: edit 000001"));
        assertTrue(output.contains("--"));
    }

    @Test
    void execute_caseNotFound_printsNotFoundMessage() {
        EditPromptCommand cmd = new EditPromptCommand("999");

        cmd.execute();
        String output = getOutput().toLowerCase();

        assertTrue(output.contains("no case found"));
    }

    @Test
    void execute_caseFoundButClosed_printsCannotBeEditedMessage() {

        LocalDate date = LocalDate.of(2025, 10, 10);
        Case closedCase = new RobberyCase("000001", "Robbery", date, "Suspect masked", "Bob", "Officer Tan");
        CaseManager.addCase(closedCase);
        CaseManager.closeCase("000001");

        EditPromptCommand cmd = new EditPromptCommand("000001");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        cmd.execute();

        System.setOut(originalOut);

        String output = outContent.toString();
        assertTrue(output.contains("has been closed"),
                "Expected error message when editing a closed case.");
    }
}
