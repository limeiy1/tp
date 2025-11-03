package seedu.sgsafe.domain.casefiles.type.financial;

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
 * such as {@link BurglaryCase}, {@link ScamCase} and {@link TheftCase}.
 */
public abstract class FinancialCase extends Case {
    /** The estimated financial value lost by the victim. */
    private Double financialValue;

    public FinancialCase(String id, String title, LocalDate date, String info, String victim, String officer) {
        super(id, title, date, info, victim, officer);
        this.type = CaseType.FINANCIAL;
    }

    public Double getFinancialValue() {
        return financialValue;
    }

    //@@author shennontay
    @Override
    public String[] getReadCaseDisplay() {
        List<String> displayList = getBaseDisplayLines();

        String formattedValue = (financialValue == null) ? "" : String.format("%.2f", financialValue);
        CaseFormatter.addWrappedFieldForRead(displayList, "Financial Value", formattedValue);
        CaseFormatter.addWrappedFieldForRead(displayList, "Info", getInfo());

        return displayList.toArray(new String[0]);
    }

    @Override
    public List<String> getValidEditFlags() {
        return List.of("title", "date", "info", "victim", "officer",
                "financial-value");
    }

    @Override
    public void update(Map<String, Object> newValues) {
        super.update(newValues);
        if (newValues.containsKey("financial-value") && newValues.get("financial-value") != null) {
            this.financialValue = (Double) newValues.get("financial-value");
        }
    }

    //@@author Michael
    @Override
    public List<String> getAdditionalFields() {
        List<String> additionalFields = super.getAdditionalFields();
        additionalFields.add("financial-value");
        return additionalFields;
    }

    @Override
    public String toSaveString() {
        return super.toSaveString()
                + "|financial-value:"
                + (this.financialValue == null ? "" : String.format("%.2f", this.financialValue));
    }
}
