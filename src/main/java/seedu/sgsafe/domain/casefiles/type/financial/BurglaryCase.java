package seedu.sgsafe.domain.casefiles.type.financial;

import seedu.sgsafe.domain.casefiles.CaseFormatter;
import seedu.sgsafe.domain.casefiles.type.CaseCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Represents a case involving a burglary.
 */
public class BurglaryCase extends FinancialCase {
    /** The house location or address where the burglary occurred. */
    private String location;

    public BurglaryCase(String id, String title, LocalDate date, String info, String victim, String officer) {
        super(id, title, date, info, victim, officer);
        this.category = CaseCategory.BURGLARY;
        this.categoryString = "Burglary";
    }

    public String getLocation() {
        return location;
    }

    //@@author shennontay
    @Override
    public String[] getReadCaseDisplay() {
        List<String> displayList = getBaseDisplayLines();

        String formattedValue = (getFinancialValue() == null) ? "" : String.format("%.2f", getFinancialValue());
        CaseFormatter.addWrappedFieldForRead(displayList, "Financial Value", formattedValue);
        CaseFormatter.addWrappedFieldForRead(displayList, "Location", this.location);
        CaseFormatter.addWrappedFieldForRead(displayList, "Info", getInfo());

        return displayList.toArray(new String[0]);
    }

    @Override
    public List<String> getValidEditFlags() {
        return List.of("title", "date", "info", "victim", "officer",
                "financial-value", "location");
    }
    @Override
    public void update(Map<String, Object> newValues) {
        super.update(newValues);
        if (newValues.containsKey("location")) {
            this.location = (String) newValues.get("location");
        }
    }

    //@@author Michael
    @Override
    public List<String> getAdditionalFields() {
        List<String> additionalFields = super.getAdditionalFields();
        additionalFields.add("location");
        return additionalFields;
    }

    @Override
    public String toSaveString() {
        return super.toSaveString()
                + "|location:" + (this.location == null ? "" : this.location);
    }
}
