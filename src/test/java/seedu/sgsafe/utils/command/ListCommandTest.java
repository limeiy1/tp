package seedu.sgsafe.utils.command;

// @@author xelisce
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.sgsafe.domain.casefiles.Case;
import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.domain.casefiles.type.financial.ScamCase;
import seedu.sgsafe.domain.casefiles.type.financial.TheftCase;
import seedu.sgsafe.domain.casefiles.type.property.VandalismCase;
import seedu.sgsafe.domain.casefiles.type.violent.AssaultCase;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Unit tests for {@link ListCommand}, verifying correct filtering, formatting,
 * verbosity, and edge case handling across all {@link CaseListingMode} variants.
 */
class ListCommandTest {

    private static final int NUMBER_OF_PREAMBLE_LINES_SHARED = 1; // case count
    private static final int NUMBER_OF_PREAMBLE_LINES_VERBOSE = NUMBER_OF_PREAMBLE_LINES_SHARED + 6;
    private static final int NUMBER_OF_PREAMBLE_LINES_SUMMARY = NUMBER_OF_PREAMBLE_LINES_SHARED + 5 + 1;
    private static final int NUMBER_OF_LINES_BETWEEN_VERBOSE_MODE = 2; // Created at and updated at

    private ArrayList<Case> caseList;

    @BeforeEach
    void resetCaseList() throws Exception {
        Field caseListField = CaseManager.class.getDeclaredField("caseList");
        caseListField.setAccessible(true);
        caseList = (ArrayList<Case>) caseListField.get(null);
        caseList.clear();
    }

    @Test
    void list_withNoCases_returnsHeaderOnly() {
        ListCommand command = new ListCommand(CaseListingMode.DEFAULT, false);
        String[] output = command.getCaseDescriptions(caseList);

        assertEquals(1, output.length);
        assertEquals("You currently have no cases in total. Add some now!", output[0]);
    }

    @Test
    void list_withOneOpenCase_returnsCorrectHeaderAndLine() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        caseList.add(new TheftCase("000001", "Robbery", date, "Masked suspect", "John Doe", "Officer Tan"));

        ListCommand command = new ListCommand(CaseListingMode.OPEN_ONLY, false);
        String[] output = command.getCaseDescriptions(caseList);

        assertEquals(NUMBER_OF_PREAMBLE_LINES_SUMMARY+1, output.length);
        assertEquals("You currently have 1 case open", output[0]);
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY].contains("[Open]"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY].contains("Robbery"));
    }

    @Test
    void list_withMixedCases_filtersByStatusCorrectly() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        caseList.add(new TheftCase("000001", "Robbery", date, "Masked suspect", "John Doe", "Officer Tan"));
        caseList.add(new ScamCase("000002", "Fraud", date, "Email scam", "Jane Doe", "Officer Lim"));

        Case closedCase = new AssaultCase("000003", "Trespass", date,
                "Unauthorized entry", "Jake Doe", "Officer Ong");
        closedCase.setClosed();
        caseList.add(closedCase);

        String[] openOutput = new ListCommand(CaseListingMode.OPEN_ONLY, false).getCaseDescriptions(caseList);
        assertEquals("You currently have 2 cases open", openOutput[0]);
        assertEquals(NUMBER_OF_PREAMBLE_LINES_SUMMARY + 2, openOutput.length);
        assertTrue(openOutput[NUMBER_OF_PREAMBLE_LINES_SUMMARY].contains("Robbery"));
        assertTrue(openOutput[NUMBER_OF_PREAMBLE_LINES_SUMMARY+1].contains("Fraud"));

        String[] closedOutput = new ListCommand(CaseListingMode.CLOSED_ONLY, false).getCaseDescriptions(caseList);
        assertEquals("You currently have 1 case closed", closedOutput[0]);
        assertEquals(NUMBER_OF_PREAMBLE_LINES_SUMMARY+1, closedOutput.length);
        assertTrue(closedOutput[NUMBER_OF_PREAMBLE_LINES_SUMMARY].contains("Trespass"));
        assertTrue(closedOutput[NUMBER_OF_PREAMBLE_LINES_SUMMARY].contains("[Closed]"));
    }

    @Test
    void list_withAllMode_returnsAllCases() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        caseList.add(new TheftCase("000001", "Robbery", date, "Masked suspect", "John Doe", "Officer Tan"));
        caseList.add(new ScamCase("000002", "Fraud", date, "Email scam", "Jane Doe", "Officer Lim"));

        Case closedCase = new AssaultCase("000003", "Trespass", date,
                "Unauthorized entry", "Jake Doe", "Officer Ong");
        closedCase.setClosed();
        caseList.add(closedCase);

        String[] output = new ListCommand(CaseListingMode.DEFAULT, false).getCaseDescriptions(caseList);
        assertEquals("You currently have 3 cases in total", output[0]);
        assertEquals(NUMBER_OF_PREAMBLE_LINES_SUMMARY+3, output.length);
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY].contains("Robbery"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY+1].contains("Fraud"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY+2].contains("Trespass"));
    }

    @Test
    void list_withDefaultMode_behavesLikeAll() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        caseList.add(new TheftCase("000001", "Robbery", date, "Masked suspect", "John Doe", "Officer Tan"));
        caseList.add(new ScamCase("000002", "Fraud", date, "Email scam", "Jane Doe", "Officer Lim"));

        String[] output = new ListCommand(CaseListingMode.DEFAULT, false).getCaseDescriptions(caseList);
        assertEquals("You currently have 2 cases in total", output[0]);
        assertEquals(NUMBER_OF_PREAMBLE_LINES_SUMMARY+2, output.length);
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY].contains("Robbery"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY+1].contains("Fraud"));
    }

    @Test
    void list_verboseMode_includesDetailedInfo() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        caseList.add(new TheftCase("000001", "Robbery", date, "Masked suspect", "John Doe", "Officer Tan"));

        ListCommand command = new ListCommand(CaseListingMode.DEFAULT, true);
        String[] output = command.getCaseDescriptions(caseList);

        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_VERBOSE].startsWith("======== CASE ID 000001 ========"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_VERBOSE+1].contains("Open"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_VERBOSE+2].contains("Category"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_VERBOSE+2].contains("Theft"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_VERBOSE+3].contains("Robbery"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_VERBOSE+4].contains("01/10/2025"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_VERBOSE+5].contains("Masked suspect"));
        assertTrue(
                output[
                        NUMBER_OF_PREAMBLE_LINES_VERBOSE+5+NUMBER_OF_LINES_BETWEEN_VERBOSE_MODE+1
                        ].contains("John Doe"));
        assertTrue(
                output[
                        NUMBER_OF_PREAMBLE_LINES_VERBOSE+5+NUMBER_OF_LINES_BETWEEN_VERBOSE_MODE+2
                        ].contains("Officer Tan"));
    }

    @Test
    void list_summaryMode_omitsDetailedInfo() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        caseList.add(new TheftCase("000001", "Robbery", date, "Masked suspect", "John Doe", "Officer Tan"));

        ListCommand command = new ListCommand(CaseListingMode.DEFAULT, false);
        String[] output = command.getCaseDescriptions(caseList);

        assertEquals(NUMBER_OF_PREAMBLE_LINES_SUMMARY+1, output.length);
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY].contains("Robbery"));
    }

    @Test
    void list_verboseMixed_includesDetails() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        caseList.add(new TheftCase("000001", "Robbery", date, "Masked suspect", "John Doe", "Officer Tan"));
        caseList.add(new ScamCase("000002", "Fraud", date, "Email scam", "Jane Doe", "Officer Lim"));

        Case closedCase = new AssaultCase("000003", "Trespass", date,
                "Unauthorized entry", "Jake Doe", "Officer Ong");
        closedCase.setClosed();
        caseList.add(closedCase);

        ListCommand command = new ListCommand(CaseListingMode.DEFAULT, true);
        String[] output = command.getCaseDescriptions(caseList);

        boolean foundFraudCategory = false;
        boolean foundTheftCategory = false;

        for (String line : output) {
            if (line.contains("Category") && line.contains("Scam")) {
                foundFraudCategory = true;
            }
            if (line.contains("Category") && line.contains("Theft")) {
                foundTheftCategory = true;
            }
        }

        assertTrue(foundFraudCategory, "Expected 'Category : SCAM' to appear in verbose output");
        assertTrue(foundTheftCategory, "Expected 'Category : THEFT' to appear in verbose output");
    }

    @Test
    void list_summaryMixed_omitsDetails() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        caseList.add(new TheftCase("000001", "Robbery", date, "Masked suspect", "John Doe", "Officer Tan"));
        caseList.add(new ScamCase("000002", "Fraud", date, "Email scam", "Jane Doe", "Officer Lim"));

        Case closedCase = new AssaultCase("000003", "Trespass", date,
                "Unauthorized entry", "Jake Doe", "Officer Ong");
        closedCase.setClosed();
        caseList.add(closedCase);

        ListCommand command = new ListCommand(CaseListingMode.DEFAULT, false);
        String[] output = command.getCaseDescriptions(caseList);

        assertEquals(NUMBER_OF_PREAMBLE_LINES_SUMMARY+3, output.length);
        assertFalse(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY].contains("Masked suspect"));
        assertFalse(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY+1].contains("Email scam"));
        assertFalse(output[NUMBER_OF_PREAMBLE_LINES_SUMMARY+2].contains("Unauthorized entry"));
    }

    @Test
    void list_verboseMode_wrapsLongInfo() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        String longInfo = "X".repeat(150); // long unbroken string
        caseList.add(new ScamCase("000004", "Forgery", date, longInfo, "Alex", "Officer Lee"));

        ListCommand command = new ListCommand(CaseListingMode.DEFAULT, true);
        String[] output = command.getCaseDescriptions(caseList);

        boolean foundInfoLabel = false;
        boolean foundWrappedLine = false;

        for (String line : output) {
            if (line.startsWith("Info")) {
                foundInfoLabel = true;
            } else if (foundInfoLabel && line.startsWith(" ".repeat(12))) {
                foundWrappedLine = true;
            }
        }

        assertTrue(foundInfoLabel, "Expected 'Info' label to appear in verbose output");
        assertTrue(foundWrappedLine, "Expected long info to wrap onto a new indented line");
    }

    @Test
    void list_verboseMode_handlesMissingFieldsGracefully() {
        LocalDate date = LocalDate.of(2025, 10, 1);
        caseList.add(new VandalismCase("000005", "Vandalism", date, "Graffiti", null, null));

        ListCommand command = new ListCommand(CaseListingMode.DEFAULT, true);
        String[] output = command.getCaseDescriptions(caseList);

        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_VERBOSE+2].startsWith("Category"));
        assertTrue(output[NUMBER_OF_PREAMBLE_LINES_VERBOSE+3].contains("Vandalism"));
        assertFalse(output[
                NUMBER_OF_PREAMBLE_LINES_VERBOSE+3+NUMBER_OF_LINES_BETWEEN_VERBOSE_MODE
                ].contains("Victim  : "));
        assertFalse(output[
                NUMBER_OF_PREAMBLE_LINES_VERBOSE+3+NUMBER_OF_LINES_BETWEEN_VERBOSE_MODE+1
                ].contains("Officer : "));
    }

    @Test
    void list_verboseMode_includesCategoryField() {
        LocalDate date = LocalDate.of(2023, 10, 5);
        caseList.add(new ScamCase("000006", "Forgery", date, "Fake documents", "Sam", "Officer Teo"));

        ListCommand command = new ListCommand(CaseListingMode.DEFAULT, true);
        String[] output = command.getCaseDescriptions(caseList);

        boolean foundCategory = false;
        for (String line : output) {
            if (line.startsWith("Category") && line.contains("Scam")) {
                foundCategory = true;
            }
        }

        assertTrue(foundCategory, "Expected 'Category : SCAM' to appear in verbose output");
    }
}
