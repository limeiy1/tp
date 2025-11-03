package seedu.sgsafe.domain.casefiles;

import org.junit.jupiter.api.Test;
import seedu.sgsafe.domain.casefiles.type.financial.TheftCase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TheftCaseTest {

    @Test
    void constructor_initializesFieldsCorrectly() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        TheftCase c = new TheftCase("F002T", "Wallet Theft", date,
                "Wallet stolen at MRT", "Alex", "Officer Tan");

        assertEquals("F002T", c.getId());
        assertEquals("Wallet Theft", c.getTitle());
        assertEquals("Theft", c.getCategoryString());
        assertNull(c.getFinancialValue());
        assertNull(c.getStolenObject());
    }

    @Test
    void getValidEditFlags_returnsExpectedList() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        TheftCase c = new TheftCase("F002T", "Case", date, "Info", "V", "O");

        List<String> expected = List.of(
                "title", "date", "info", "victim", "officer",
                "financial-value", "stolen-object"
        );
        assertEquals(expected, c.getValidEditFlags());
    }

    @Test
    void update_updatesFinancialValueAndStolenObject_andBaseFields() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        TheftCase c = new TheftCase("F002T", "Old Title", date,
                "Old info", "Victim", "Officer");

        Map<String, Object> updates = new HashMap<>();
        updates.put("financial-value", 1500.00);
        updates.put("stolen-object", "Laptop");
        updates.put("info", "Updated info");
        updates.put("title", "Updated title");
        updates.put("officer", "Officer Lee");

        c.update(updates);

        assertEquals(1500.00, c.getFinancialValue());
        assertEquals("Laptop", c.getStolenObject());
        assertEquals("Updated info", c.getInfo());
        assertEquals("Updated title", c.getTitle());
        assertEquals("Officer Lee", c.getOfficer());
    }

    @Test
    void update_handlesNullFinancialValueGracefully() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        TheftCase c = new TheftCase("F002T", "Case", date, "Info", "V", "O");

        Map<String, Object> first = new HashMap<>();
        first.put("financial-value", 200.00);
        c.update(first);
        assertEquals(200.00, c.getFinancialValue());

        Map<String, Object> second = new HashMap<>();
        second.put("financial-value", null);
        second.put("stolen-object", "Phone");
        c.update(second);
        assertEquals(200.00, c.getFinancialValue());
        assertEquals("Phone", c.getStolenObject());
    }

    @Test
    void getReadCaseDisplay_includesAllRelevantFields() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        TheftCase c = new TheftCase("F002T", "Wallet Theft", date,
                "Wallet stolen", "Victim", "Officer Tan");

        Map<String, Object> updates = new HashMap<>();
        updates.put("financial-value", 1000.00);
        updates.put("stolen-object", "Wallet");
        c.update(updates);

        String[] lines = c.getReadCaseDisplay();
        assertTrue(contains(lines, "Financial Value", "1000.00"));
        assertTrue(contains(lines, "Stolen Object", "Wallet"));
        assertTrue(contains(lines, "Info", "Wallet stolen"));
    }

    @Test
    void getAdditionalFields_appendsStolenObjectAfterFinancialValue() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        TheftCase c = new TheftCase("F002T", "Title", date, "Info", "V", "O");

        List<String> fields = c.getAdditionalFields();
        assertTrue(fields.contains("financial-value"));
        assertTrue(fields.contains("stolen-object"));
        assertEquals("stolen-object", fields.get(fields.size() - 1));
    }

    @Test
    void toSaveString_includesKeysWithEmptyAndPopulatedValues() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        TheftCase c = new TheftCase("F002T", "Title", date, "Info", "V", "O");

        String emptySave = c.toSaveString();
        assertTrue(emptySave.contains("|financial-value:"));
        assertTrue(emptySave.contains("|stolen-object:"));

        Map<String, Object> updates = new HashMap<>();
        updates.put("financial-value", 2500.00);
        updates.put("stolen-object", "Watch");
        c.update(updates);

        String populated = c.toSaveString();
        assertTrue(populated.contains("|financial-value:2500.00"));
        assertTrue(populated.contains("|stolen-object:Watch"));
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
