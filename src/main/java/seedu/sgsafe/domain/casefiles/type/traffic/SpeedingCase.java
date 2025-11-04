package seedu.sgsafe.domain.casefiles.type.traffic;

import java.util.List;
import java.util.Map;

import seedu.sgsafe.domain.casefiles.type.CaseCategory;
import seedu.sgsafe.domain.casefiles.CaseFormatter;

import java.time.LocalDate;

/**
 * Represents a case involving speeding offences.
 */
public class SpeedingCase extends TrafficCase {
    /** The speed limit of the road */
    private Integer speedLimit;

    /** The actual speed the driver was driving at. */
    private Integer exceededSpeed;

    public SpeedingCase(String id, String title, LocalDate date, String info, String victim, String officer) {
        super(id, title, date, info, victim, officer);
        this.category = CaseCategory.SPEEDING;
        this.categoryString = "Speeding";
    }

    public Integer getSpeedLimit() {
        return speedLimit;
    }

    public Integer getExceededSpeed() {
        return exceededSpeed;
    }

    //@@author shennontay
    @Override
    public String[] getReadCaseDisplay() {
        List<String> displayList = getBaseDisplayLines();

        CaseFormatter.addWrappedFieldForRead(displayList, "Vehicle Type", this.getVehicleType());
        CaseFormatter.addWrappedFieldForRead(displayList, "Vehicle Plate", this.getVehiclePlate());
        CaseFormatter.addWrappedFieldForRead(displayList, "Road Name", this.getRoadName());
        CaseFormatter.addWrappedFieldForRead(displayList, "Speed Limit", CaseFormatter.safeString(this.speedLimit));
        CaseFormatter.addWrappedFieldForRead(displayList, "Exceeded Speed",
                CaseFormatter.safeString(this.exceededSpeed));

        CaseFormatter.addWrappedFieldForRead(displayList, "Info", getInfo());

        return displayList.toArray(new String[0]);
    }
    //@@author

    @Override
    public List<String> getValidEditFlags() {
        return List.of("title", "date", "info", "victim", "officer",
                "vehicle-type", "vehicle-plate", "road-name",
                "speed-limit", "exceeded-speed");
    }

    @Override
    public void update(Map<String, Object> newValues) {
        super.update(newValues);
        if (newValues.containsKey("speed-limit") && newValues.get("speed-limit") != null) {
            this.speedLimit = (Integer) newValues.get("speed-limit");
        }
        if (newValues.containsKey("exceeded-speed") && newValues.get("exceeded-speed") != null) {
            this.exceededSpeed = (Integer) newValues.get("exceeded-speed");
        }
    }

    @Override
    public List<String> getAdditionalFields() {
        List<String> additionalFields = super.getAdditionalFields();
        additionalFields.add("speed-limit");
        additionalFields.add("exceeded-speed");
        return additionalFields;
    }

    @Override
    public String toSaveString() {
        return super.toSaveString()
                + "|speed-limit:" + (this.speedLimit == null ? "" : this.speedLimit)
                + "|exceeded-speed:" + (this.exceededSpeed == null ? "" : this.exceededSpeed);
    }
}
