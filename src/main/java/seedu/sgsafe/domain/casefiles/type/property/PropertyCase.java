package seedu.sgsafe.domain.casefiles.type.property;

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
 * such as {@link ArsonCase} and {@link VandalismCase}.
 */
public abstract class PropertyCase extends Case {
    /** The location where the property-related case occurred. */
    private String location;

    /** The estimated monetary damage caused by the incident. */
    private Double monetaryDamage;

    public PropertyCase(String id, String title, LocalDate date, String info, String victim, String officer) {
        super(id, title, date, info, victim, officer);
        this.type = CaseType.PROPERTY;
    }

    public String getLocation() {
        return location;
    }

    public Double getMonetaryDamage() {
        return monetaryDamage;
    }

    //@@author shennontay
    @Override
    public String[] getReadCaseDisplay() {
        List<String> displayList = getBaseDisplayLines();

        String formattedMonetaryDamage = (monetaryDamage == null) ? "" : String.format("%.2f", monetaryDamage);

        CaseFormatter.addWrappedFieldForRead(displayList, "Location", this.location);
        CaseFormatter.addWrappedFieldForRead(displayList, "Monetary Damage", formattedMonetaryDamage);
        CaseFormatter.addWrappedFieldForRead(displayList, "Info", getInfo());

        return displayList.toArray(new String[0]);
    }

    @Override
    public List<String> getValidEditFlags() {
        return List.of("title", "date", "info", "victim", "officer",
                "location", "monetary-damage");
    }

    @Override
    public void update(Map<String, Object> newValues) {
        super.update(newValues);
        if (newValues.containsKey("location")) {
            this.location = (String) newValues.get("location");
        }
        if (newValues.containsKey("monetary-damage") && newValues.get("monetary-damage") != null) {
            this.monetaryDamage = (Double) newValues.get("monetary-damage");
        }
    }

    //@@author Michael
    @Override
    public List<String> getAdditionalFields() {
        List<String> additionalFields = super.getAdditionalFields();
        additionalFields.add("location");
        additionalFields.add("monetary-damage");
        return additionalFields;
    }

    @Override
    public String toSaveString() {
        return super.toSaveString()
                + "|location:" + (this.location == null ? "" : this.location)
                + "|monetary-damage:" + (this.monetaryDamage == null ? "" : this.monetaryDamage);
    }
}
