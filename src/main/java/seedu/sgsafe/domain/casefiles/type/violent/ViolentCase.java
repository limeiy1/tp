package seedu.sgsafe.domain.casefiles.type.violent;

import seedu.sgsafe.domain.casefiles.Case;
import seedu.sgsafe.domain.casefiles.CaseFormatter;
import seedu.sgsafe.domain.casefiles.type.CaseType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Represents a case under the Financial type.
 * <p>
 * This class serves as an abstract base for financial-related cases
 * such as {@link AssaultCase}, {@link MurderCase} and {@link RobberyCase}.
 */
public abstract class ViolentCase extends Case {
    /** The weapon used by the suspect or culprit. */
    private String weapon;

    /** The number of victims affected. */
    private Integer numberOfVictims;

    public ViolentCase(String id, String title, LocalDate date, String info, String victim, String officer) {
        super(id, title, date, info, victim, officer);
        this.type = CaseType.VIOLENT;
    }

    //@@author shennontay
    public String getWeapon() {
        return weapon;
    }

    public Integer getNumberOfVictims() {
        return numberOfVictims;
    }

    @Override
    public String[] getReadCaseDisplay() {
        List<String> displayList = getBaseDisplayLines();

        CaseFormatter.addWrappedFieldForRead(displayList, "Weapon", this.weapon);
        CaseFormatter.addWrappedFieldForRead(
                displayList, "Number of Victims", CaseFormatter.safeString(this.numberOfVictims));

        CaseFormatter.addWrappedFieldForRead(displayList, "Info", getInfo());

        return displayList.toArray(new String[0]);
    }

    @Override
    public List<String> getValidEditFlags() {
        return List.of("title", "date", "info", "victim", "officer", "weapon", "number-of-victims");
    }

    @Override
    public void update(Map<String, Object> newValues) {
        super.update(newValues);
        if (newValues.containsKey("weapon")) {
            this.weapon = (String) newValues.get("weapon");
        }
        if (newValues.containsKey("number-of-victims") && newValues.get("number-of-victims") != null) {
            this.numberOfVictims = (Integer) newValues.get("number-of-victims");
        }
    }

    //@@author Michael
    @Override
    public List<String> getAdditionalFields() {
        List<String> additionalFields = super.getAdditionalFields();
        additionalFields.add("weapon");
        additionalFields.add("number-of-victims");
        return additionalFields;
    }

    @Override
    public String toSaveString() {
        return super.toSaveString()
                + "|number-of-victims:" + (this.numberOfVictims  == null ? "" : this.numberOfVictims)
                + "|weapon:" + (this.weapon == null ? "" : this.weapon);
    }
}
