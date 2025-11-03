package seedu.sgsafe.domain.casefiles;

import org.junit.jupiter.api.Test;
import seedu.sgsafe.domain.casefiles.type.property.VandalismCase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VandalismCaseTest {

    @Test
    void constructor_initializesFieldsCorrectly() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        VandalismCase c = new VandalismCase("V001Z3", "Graffiti on Wall", date,
                "Spray paint found on building wall", "Liam", "Officer Chen");

        assertEquals("V001Z3", c.getId());
        assertEquals("Graffiti on Wall", c.getTitle());
        assertEquals("Liam", c.getVictim());
        assertEquals("Officer Chen", c.getOfficer());
        assertEquals("Vandalism", c.getCategoryString());
        assertNull(c.getLocation(), "Location should initially be null");
        assertNull(c.getMonetaryDamage(), "Monetary damage should initially be null");
    }

    @Test
    void update_updatesLocationMonetaryDamageFields() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        VandalismCase c = new VandalismCase("V001Z3", "Graffiti on Wall", date,
                "Spray paint found on building wall", "Liam", "Officer Chen");

        Map<String, Object> updates = new HashMap<>();
        updates.put("location", "Downtown Block 5");
        updates.put("monetary-damage", 1200.00);

        c.update(updates);

        assertEquals("Downtown Block 5", c.getLocation());
        assertEquals(1200.00, c.getMonetaryDamage());
    }

    @Test
    void getReadCaseDisplay_includesLocationMonetaryDamageFields() {
        LocalDate date = LocalDate.of(2025, 10, 14);
        VandalismCase c = new VandalismCase("V001Z3", "Graffiti on Wall", date,
                "Spray paint found on building wall", "Liam", "Officer Chen");

        Map<String, Object> updates = new HashMap<>();
        updates.put("location", "Downtown Block 5");
        updates.put("monetary-damage", 1200.00);
        c.update(updates);

        String[] lines = c.getReadCaseDisplay();
        assertTrue(containsLine(lines, "Location", "Downtown Block 5"));
        assertTrue(containsLine(lines, "Monetary Damage", "1200.00"));
    }

    private boolean containsLine(String[] lines, String label, String value) {
        for (String line : lines) {
            if (line.contains(label) && line.contains(value)) {
                return true;
            }
        }
        return false;
    }
}
