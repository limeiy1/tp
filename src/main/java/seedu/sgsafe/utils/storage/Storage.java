package seedu.sgsafe.utils.storage;

import seedu.sgsafe.domain.casefiles.Case;
import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.domain.casefiles.type.CaseCategory;
import seedu.sgsafe.domain.casefiles.type.OthersCase;
import seedu.sgsafe.domain.casefiles.type.financial.BurglaryCase;
import seedu.sgsafe.domain.casefiles.type.financial.ScamCase;
import seedu.sgsafe.domain.casefiles.type.financial.TheftCase;
import seedu.sgsafe.domain.casefiles.type.property.ArsonCase;
import seedu.sgsafe.domain.casefiles.type.property.VandalismCase;
import seedu.sgsafe.domain.casefiles.type.sexual.RapeCase;
import seedu.sgsafe.domain.casefiles.type.sexual.VoyeurismCase;
import seedu.sgsafe.domain.casefiles.type.traffic.AccidentCase;
import seedu.sgsafe.domain.casefiles.type.traffic.SpeedingCase;
import seedu.sgsafe.domain.casefiles.type.violent.AssaultCase;
import seedu.sgsafe.domain.casefiles.type.violent.MurderCase;
import seedu.sgsafe.domain.casefiles.type.violent.RobberyCase;
import seedu.sgsafe.utils.settings.Settings;
import seedu.sgsafe.utils.ui.Display;
import seedu.sgsafe.utils.ui.Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;

/**
 * The {@code Storage} class handles the persistence (saving and loading)
 * of {@link Case} objects from and to a text file.
 * <p>
 * Each case is serialized into a string format that uses key-value pairs separated by
 * a pipe character ("|"), with each key and value separated by a colon (":").
 * <p>
 * This class also provides date and datetime patterns used for consistent formatting.
 */
public class Storage {
    /** The date pattern used for saving and parsing case dates (e.g., 29/10/2025). */
    private static final String SAVE_DATE_PATTERN = "dd/MM/yyyy";

    /** The date-time pattern used for saving and parsing timestamps (e.g., 29/10/2025 13:45:22). */
    private static final String SAVE_DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

    private static final String SETTING_PREFIX = "settings:";

    /** The filename where cases are stored. */
    private final String filename;

    /**
     * Constructs a {@code Storage} object with the specified filename.
     *
     * @param filename the name of the file used to save and load case data
     */
    public Storage(String filename) {
        this.filename = filename;
    }

    /**
     * Returns the save date pattern.
     *
     * @return the pattern used for saving and parsing dates
     */
    public static String getSaveDatePattern() {
        return SAVE_DATE_PATTERN;
    }

    /**
     * Returns the save date-time pattern.
     *
     * @return the pattern used for saving and parsing date-times
     */
    public static String getSaveDateTimePattern() {
        return SAVE_DATETIME_PATTERN;
    }

    /**
     * Parses a serialized save string into a map of field names and their corresponding values.
     * Fields are separated by {@code |}, and key-value pairs are separated by {@code :}.
     *
     * @param saveString the serialized string representation of a case
     * @return a map containing field names as keys and their string values
     */
    private Map<String, String> getFields(String saveString) {
        Map<String, String> fields = new HashMap<>();
        for (String field : saveString.split("\\|")) {
            String[] splitField = field.split(":", 2);
            String key = splitField[0];
            String value = (splitField[1].isEmpty()) ? null : splitField[1];
            if (value != null) {
                fields.put(key, value);
            }
        }
        return fields;
    }

    /**
     * Converts a serialized save string into a corresponding {@link Case} object.
     * <p>
     * The method uses the {@code category} field to determine which subclass of {@link Case}
     * to instantiate (e.g., {@link TheftCase}, {@link MurderCase}, etc.).
     *
     * @param line a line of text representing a serialized case
     * @return a {@code Case} object reconstructed from the save string
     */
    public Case getCaseFromSaveString(String line) {
        Map<String, String> fields = getFields(line);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(getSaveDatePattern());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(getSaveDateTimePattern());

        // Parse base attributes
        String id = fields.get("id");
        String title = fields.get("title");
        LocalDate date = LocalDate.parse(fields.get("date"), dateFormatter);
        String info = fields.get("info");
        String victim = fields.get("victim");
        String officer = fields.get("officer");
        boolean isDeleted = fields.get("is-deleted").equals("1");
        boolean isOpen = fields.get("is-open").equals("1");
        String category = fields.get("category");
        LocalDateTime createdAt = LocalDateTime.parse(fields.get("created-at"), dateTimeFormatter);
        LocalDateTime updatedAt = LocalDateTime.parse(fields.get("updated-at"), dateTimeFormatter);

        // Instantiate the appropriate subclass of Case
        Case newCase = switch (CaseCategory.valueOf(category)) {
        case BURGLARY -> new BurglaryCase(id, title, date, info, victim, officer);
        case SCAM -> new ScamCase(id, title, date, info, victim, officer);
        case THEFT -> new TheftCase(id, title, date, info, victim, officer);
        case ARSON -> new ArsonCase(id, title, date, info, victim, officer);
        case VANDALISM -> new VandalismCase(id, title, date, info, victim, officer);
        case RAPE -> new RapeCase(id, title, date, info, victim, officer);
        case VOYEURISM -> new VoyeurismCase(id, title, date, info, victim, officer);
        case ACCIDENT -> new AccidentCase(id, title, date, info, victim, officer);
        case SPEEDING -> new SpeedingCase(id, title, date, info, victim, officer);
        case ASSAULT -> new AssaultCase(id, title, date, info, victim, officer);
        case MURDER -> new MurderCase(id, title, date, info, victim, officer);
        case ROBBERY -> new RobberyCase(id, title, date, info, victim, officer);
        case OTHERS -> new OthersCase(id, title, date, info, victim, officer);
        };

        // Only retain additional fields that are specific to the subclass
        List<String> additionalFields = newCase.getAdditionalFields();
        fields.keySet().retainAll(additionalFields);

        // Convert field values to appropriate data types
        Map<String, Object> typedFields = Parser.convertFlagValueTypes(fields);
        newCase.update(typedFields);

        // Set timestamps and status flags
        newCase.initialiseMetadataFromSave(isOpen, isDeleted, createdAt, updatedAt);

        return newCase;
    }

    private void loadSettings(String settingString) throws IllegalArgumentException {
        String[] settings = settingString.substring(SETTING_PREFIX.length()).split("\\|");
        if (settings.length != 3) {
            throw new IllegalArgumentException();
        }

        Settings.setInputDateFormat(settings[0].strip());
        Settings.setOutputDateFormat(settings[1].strip());
        Settings.setDateTimeFormat(settings[2].strip());
    }

    /**
     * Loads all cases from the file into the {@link CaseManager}.
     * <p>
     * Each non-empty line in the file represents a serialized {@link Case} object.
     * If the file does not exist, this method does nothing.
     */
    public void loadCaseManager() {
        File file = new File(this.filename);
        if (file.exists()) {
            try (Scanner s = new Scanner(file)) {
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    if (line.startsWith(SETTING_PREFIX)) {
                        ArrayList<String> settingResult = new ArrayList<>();
                        settingResult.add("Loading settings from save...");

                        try {
                            loadSettings(line);
                        } catch (IllegalArgumentException e) {
                            settingResult.add("Invalid settings format. " +
                                    "Some of them could not be loaded from the save file.");
                        }

                        settingResult.add("Date input format was set to: " + Settings.getInputDateFormat());
                        settingResult.add("Date output format was set to: " + Settings.getOutputDateFormat());
                        settingResult.add("Timestamp output format was set to: " + Settings.getDateTimeFormat());
                        Display.printMessage(settingResult.toArray(new String[0]));
                    }
                    else if (!line.trim().isEmpty()) {
                        Case newCase = getCaseFromSaveString(line);
                        CaseManager.addCase(newCase);
                    }
                }
            } catch (IOException e) {
                System.out.println("Something went wrong while loading from the save file: " + e.getMessage());
            }
        }
    }

    /**
     * Saves all cases currently managed by {@link CaseManager} to the file.
     * <p>
     * Each case is written to the file in its serialized string format,
     * with one line per case.
     */
    public void saveToFile() {
        ArrayList<Case> cases = CaseManager.getCaseList();
        try (FileWriter fw = new FileWriter(this.filename)) {
            fw.append(SETTING_PREFIX);
            fw.append(Settings.getInputDateFormat()).append("|");
            fw.append(Settings.getOutputDateFormat()).append("|");
            fw.append(Settings.getDateTimeFormat()).append(System.lineSeparator());
            for (Case c : cases) {
                fw.append(c.toSaveString());
                fw.append(System.lineSeparator());
            }
            fw.flush();
        } catch (IOException e) {
            System.out.println("Something went wrong while saving: " + e.getMessage());
        }
    }
}
