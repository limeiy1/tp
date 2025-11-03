package seedu.sgsafe.domain.casefiles.type.traffic;

import seedu.sgsafe.domain.casefiles.CaseFormatter;
import seedu.sgsafe.domain.casefiles.type.CaseCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Represents a case involving a traffic accident.
 */
public class AccidentCase extends TrafficCase{
    /** The number of people killed or injured by the traffic accident. */
    private Integer numberOfCasualties;

    public AccidentCase(String id, String title, LocalDate date, String info, String victim, String officer) {
        super(id, title, date, info, victim, officer);
        this.category = CaseCategory.ACCIDENT;
        this.categoryString = "Traffic accident";
    }

    public Integer getNumberOfCasualties() {
        return numberOfCasualties;
    }

    //@@author shennontay
    @Override
    public String[] getReadCaseDisplay() {
        List<String> displayList = getBaseDisplayLines();

        CaseFormatter.addWrappedFieldForRead(displayList, "Vehicle Type", this.getVehicleType());
        CaseFormatter.addWrappedFieldForRead(displayList, "Vehicle Plate", this.getVehiclePlate());
        CaseFormatter.addWrappedFieldForRead(displayList, "Road Name", this.getRoadName());
        CaseFormatter.addWrappedFieldForRead(displayList,
                "Number of Casualties", String.valueOf(this.numberOfCasualties));

        CaseFormatter.addWrappedFieldForRead(displayList, "Info", getInfo());

        return displayList.toArray(new String[0]);
    }

    @Override
    public List<String> getValidEditFlags() {
        return List.of("title", "date", "info", "victim", "officer",
                "vehicle-type", "vehicle-plate", "road-name",
                "number-of-casualties");
    }

    @Override
    public void update(Map<String, Object> newValues) {
        super.update(newValues);
        if (newValues.containsKey("number-of-casualties") && newValues.get("number-of-casualties") != null) {
            this.numberOfCasualties= (Integer) newValues.get("number-of-casualties");
        }
    }

    //@@author Michael
    @Override
    public List<String> getAdditionalFields() {
        List<String> additionalFields = super.getAdditionalFields();
        additionalFields.add("number-of-casualties");
        return additionalFields;
    }

    @Override
    public String toSaveString() {
        return super.toSaveString()
                + "|number-of-casualties:" + (this.numberOfCasualties == null ? "" : this.numberOfCasualties);
    }
}
