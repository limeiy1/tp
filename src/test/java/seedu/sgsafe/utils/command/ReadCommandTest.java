package seedu.sgsafe.utils.command;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.sgsafe.domain.casefiles.Case;
import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.domain.casefiles.type.traffic.SpeedingCase;


public class ReadCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void execute_validCase_displaysCaseDetails() {

        LocalDate date = LocalDate.of(2025, 10, 10);
        Case sampleCase = new SpeedingCase("000001", "Speeding", date, "Suspect armed", "Alice", "Officer Tan");
        CaseManager.addCase(sampleCase);

        ReadCommand command = new ReadCommand("000001");
        command.execute();

        String output = outContent.toString();
        assertTrue(output.contains("Speeding"));
        assertTrue(output.contains("Suspect armed"));
    }

    @Test
    void execute_caseNotFound_printsError() {
        ReadCommand command = new ReadCommand("DOESNTEXIST");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        command.execute();

        String output = out.toString().toLowerCase();
        assertTrue(output.contains("not found") || output.contains("doesntexist"));
    }


}
