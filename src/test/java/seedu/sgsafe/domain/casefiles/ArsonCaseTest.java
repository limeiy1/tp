package seedu.sgsafe.domain.casefiles;

import org.junit.jupiter.api.Test;
import seedu.sgsafe.domain.casefiles.type.property.ArsonCase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArsonCaseTest {

    @Test
    void constructor_initializesFieldsCorrectly() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        ArsonCase c = new ArsonCase("A001F1", "Warehouse Fire", date,
                "Deliberate fire incident", "David", "Officer Ong");

        assertEquals("A001F1", c.getId());
        assertEquals("Warehouse Fire", c.getTitle());
        assertEquals("David", c.getVictim());
        assertEquals("Officer Ong", c.getOfficer());
        assertEquals("Arson", c.getCategoryString());
        assertNull(c.getLocation());
        assertNull(c.getMonetaryDamage());
    }

    @Test
    void getValidEditFlags_returnsExpectedFlags() {
        ArsonCase c = new ArsonCase("A003", "Warehouse Fire", LocalDate.now(),
                "Deliberate ignition", "David", "Officer Ong");

        List<String> expected = List.of("title", "date", "info", "victim", "officer",
                "location", "monetary-damage");
        assertEquals(expected, c.getValidEditFlags());
    }

    @Test
    void toSaveString_formatsMonetaryDamageWithTwoDecimalPlaces() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        ArsonCase c = new ArsonCase("A001", "Factory Fire", date,
                "Electrical fault suspected", "John", "Officer Lim");

        Map<String, Object> updates = new HashMap<>();
        updates.put("monetary-damage", 2000.0);
        c.update(updates);

        String saveString = c.toSaveString();
        assertTrue(saveString.contains("|monetary-damage:2000.0"));

        ArsonCase c2 = new ArsonCase("A002", "House Fire", date,
                "Suspected arson", "Jane", "Officer Tan");
        String saveString2 = c2.toSaveString();
        assertTrue(saveString2.contains("|monetary-damage:"));
    }

}
