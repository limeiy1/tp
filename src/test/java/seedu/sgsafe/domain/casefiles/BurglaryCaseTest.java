package seedu.sgsafe.domain.casefiles;

import org.junit.jupiter.api.Test;
import seedu.sgsafe.domain.casefiles.type.financial.BurglaryCase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BurglaryCaseTest {

    @Test
    void constructor_initializesFieldsCorrectly() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        BurglaryCase c = new BurglaryCase("F001B", "Home Burglary", date,
                "Cash and jewelry stolen", "Jane Doe", "Officer Tan");

        assertEquals("F001B", c.getId());
        assertEquals("Home Burglary", c.getTitle());
        assertEquals("Burglary", c.getCategoryString());
        assertNull(c.getFinancialValue());
        assertNull(c.getLocation());
    }

    @Test
    void getValidEditFlags_returnsExpectedList() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        BurglaryCase c = new BurglaryCase("F001B", "Case", date, "Info", "V", "O");

        List<String> expected = List.of(
                "title", "date", "info", "victim", "officer",
                "financial-value", "location"
        );
        assertEquals(expected, c.getValidEditFlags());
    }

    @Test
    void getReadCaseDisplay_includesAllRelevantFields() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        BurglaryCase c = new BurglaryCase("F001B", "Home Burglary", date,
                "Items stolen", "Victim", "Officer Tan");

        Map<String, Object> updates = new HashMap<>();
        updates.put("financial-value", 20000.00);
        updates.put("location", "123 Bukit Timah Road");
        c.update(updates);

        String[] lines = c.getReadCaseDisplay();
        assertTrue(contains(lines, "Financial Value", "20000.00"));
        assertTrue(contains(lines, "Location", "123 Bukit Timah Road"));
        assertTrue(contains(lines, "Info", "Items stolen"));
    }

    @Test
    void getAdditionalFields_appendsLocationToFinancialCaseFields() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        BurglaryCase c = new BurglaryCase("F001B", "Title", date, "Info", "V", "O");

        List<String> fields = c.getAdditionalFields();
        assertTrue(fields.contains("financial-value"));
        assertTrue(fields.contains("location"));
        assertEquals("location", fields.get(fields.size() - 1));
    }

    @Test
    void toSaveString_includesAllKeysWithAndWithoutValues() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        BurglaryCase c = new BurglaryCase("F001B", "Title", date, "Info", "V", "O");

        String emptySave = c.toSaveString();
        assertTrue(emptySave.contains("|financial-value:"));
        assertTrue(emptySave.contains("|location:"));

        Map<String, Object> updates = new HashMap<>();
        updates.put("financial-value", 50000.00);
        updates.put("location", "Pasir Ris");
        c.update(updates);

        String populated = c.toSaveString();
        assertTrue(populated.contains("|financial-value:50000.00"));
        assertTrue(populated.contains("|location:Pasir Ris"));
    }


    private boolean contains(String[] lines, String label, String value) {
        for (String l : lines) {
            if (l.contains(label) && l.contains(value)) {
                return true;
            }
        }
        return false;
    }
}
