package seedu.sgsafe.domain.casefiles;

import seedu.sgsafe.domain.casefiles.type.CaseType;
import seedu.sgsafe.domain.casefiles.type.CaseCategory;
import seedu.sgsafe.utils.settings.Settings;
import seedu.sgsafe.utils.storage.Storage;
import seedu.sgsafe.utils.ui.DateFormatter;

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Represents a case file in the SGSafe system.
 * Each case contains metadata such as title, date, victim, officer, and status.
 */
public abstract class Case {

    /** The type of case. */
    protected CaseType type;

    /** The category of the case. */
    protected CaseCategory category;

    /** The category name to be printed. */
    protected String categoryString;

    /** The title or summary of the case. */
    private final String id;

    /** The title or summary of the case. */
    private String title;

    /** The date the case was recorded or occurred. */
    private LocalDate date;

    /** Additional information or notes about the case. */
    private String info;

    /** The name of the victim involved in the case. */
    private String victim;

    /** The name of the officer assigned to the case. */
    private String officer;

    /** Indicates whether the case is currently open. */
    private boolean isOpen;

    /** Indicates whether a case has been deleted. */
    private boolean isDeleted;

    /** Metadata timestamp for auditing of when the case is created. */
    private LocalDateTime createdAt;

    /** Metadata timestamp for auditing of when the case is updated. */
    private LocalDateTime updatedAt;

    /**
     * Constructs a {@code Case} object with the specified details.
     * The case is initialized as closed by default.
     *
     * @param id      the case ID in hex form
     * @param title   the title or summary of the case
     * @param date    the date the case was recorded or occurred
     * @param info    additional information or notes about the case
     * @param victim  the name of the victim involved
     * @param officer the name of the officer assigned
     */
    public Case(String id, String title, LocalDate date, String info, String victim, String officer) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.info = info;
        this.victim = victim;
        this.officer = officer;
        this.isOpen = true;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Retrieves the title or summary of the case.
     *
     * @return the title of the case
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the date the case was recorded or occurred.
     *
     * @return the date of the case
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Retrieves additional information about the case.
     *
     * @return the additional information about the case
     */
    public String getInfo() {
        return info;
    }

    /**
     * Retrieves the type of the case.
     *
     * @return the type of the case.
     */
    public CaseType getType() {
        return type;
    }

    /**
     * Retrieves the category of the case.
     *
     * @return the category of the case.
     */
    public CaseCategory getCategory() {
        return category;
    }

    /**
     * Retrieves the category of the case in the print format.
     *
     * @return the categoryString of the case.
     */
    public String getCategoryString() {
        return categoryString;
    }

    /**
     * Retrieves the name of the victim involved in the case.
     *
     * @return the name of the victim, or null if not specified
     */
    public String getVictim() {
        return victim;
    }

    /**
     * Retrieves the name of the officer assigned to the case.
     *
     * @return the name of the officer, or null if not specified
     */
    public String getOfficer() {
        return officer;
    }

    /**
     * Retrieves the unique ID of the case.
     *
     * @return the case ID
     */
    public String getId() {
        return this.id;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the createdAt timestamp.
     *
     * @param createdAt the {@link LocalDateTime} that createdAt should be set to.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Sets the updatedAt timestamp.
     *
     * @param updatedAt the {@link LocalDateTime} that updatedAt should be set to.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Initializes the metadata fields of this object using data loaded from a saved source.
     * <p>
     * This method sets the object's open and deleted status, as well as its creation
     * and last update timestamps.
     * </p>
     *
     * @param isOpen     {@code true} if the object is currently open; {@code false} otherwise.
     * @param isDeleted  {@code true} if the object has been marked as deleted; {@code false} otherwise.
     * @param createdAt  the timestamp representing when the object was originally created.
     * @param updatedAt  the timestamp representing the last time the object was updated.
     */
    public void initialiseMetadataFromSave(boolean isOpen, boolean isDeleted,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.isOpen = isOpen;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public List<String> getAdditionalFields() {
        return new ArrayList<>();
    }

    //@@author xelisce

    /**
     * Builds a one‑line summary representation of this case.
     * <p>
     * Includes status, category, ID, date, and title in a fixed‑width format.
     *
     * @return a formatted summary line for this case
     */
    public String getDisplayLine() {
        String dateString = DateFormatter.formatDate(date, Settings.getOutputDateFormat());
        return CaseFormatter.formatCaseSummaryLine(isOpen, categoryString, id, dateString, title);
    }

    /**
     * Builds a verbose, multi‑line representation of this case.
     * <p>
     * Starts with a case ID header, followed by wrapped fields such as
     * status, category, title, date, info, timestamps, victim, and officer.
     *
     * @return an array of formatted lines for verbose display
     */
    public String[] getMultiLineVerboseDisplay() {
        List<String> lines = new ArrayList<>();
        lines.add(CaseFormatter.formatCaseIDHeader(id));

        String dateString = DateFormatter.formatDate(date, Settings.getOutputDateFormat());
        String statusString = CaseFormatter.convertStatusToString(isOpen);
        String createdAtString = DateFormatter.formatDateTime(createdAt,
                Settings.getDateTimeFormat());
        String updatedAtString = DateFormatter.formatDateTime(updatedAt,
                Settings.getDateTimeFormat());

        CaseFormatter.addWrappedFieldForVerbose(lines, "Status", statusString);
        CaseFormatter.addWrappedFieldForVerbose(lines, "Category", categoryString);
        CaseFormatter.addWrappedFieldForVerbose(lines, "Title", title);
        CaseFormatter.addWrappedFieldForVerbose(lines, "Date", dateString);
        CaseFormatter.addWrappedFieldForVerbose(lines, "Info", info);
        CaseFormatter.addWrappedFieldForVerbose(lines, "Created at", createdAtString);
        CaseFormatter.addWrappedFieldForVerbose(lines, "Updated at", updatedAtString);
        CaseFormatter.addWrappedFieldForVerbose(lines, "Victim", victim);
        CaseFormatter.addWrappedFieldForVerbose(lines, "Officer", officer);

        return lines.toArray(new String[0]);
    }
    //@@ author

    public void setClosed() {
        this.isOpen = false;
        updatedAt = LocalDateTime.now();
    }

    public void setOpen() {
        this.isOpen = true;
        updatedAt = LocalDateTime.now();
    }

    /**
     * Returns whether this case is currently open.
     *
     * @return {@code true} if the case is open; {@code false} otherwise
     */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
     * Returns the list of valid flags that can be used to edit this case type.
     * The default implementation returns common flags shared by all case types.
     * Subclasses with additional fields should override this method.
     *
     * @return list of valid flag names for editing
     */
    public List<String> getValidEditFlags() {
        // Default flags for all case types
        return List.of("title", "date", "info", "victim", "officer");
    }

    /**
     * Updates the editable fields of {@code Case} instance using the provided map of new values.
     * <p>
     * Each key in {@code newValues} corresponds to a valid editable field (e.g. {@code title}, {@code date},
     * {@code info}, {@code victim}, {@code officer}). Only fields present in the map are updated; all
     * others remain unchanged.
     *
     * @param newValues a map containing field names and their new values
     */
    public void update(Map<String, Object> newValues) {
        if (newValues.containsKey("title")) {
            this.title = (String) newValues.get("title");
        }
        if (newValues.containsKey("date")) {
            this.date = (LocalDate) newValues.get("date");
        }
        if (newValues.containsKey("info")) {
            this.info = (String) newValues.get("info");
        }
        if (newValues.containsKey("victim")) {
            this.victim = (String) newValues.get("victim");
        }
        if (newValues.containsKey("officer")) {
            this.officer = (String) newValues.get("officer");
        }
        this.updatedAt = LocalDateTime.now();
    }

    //@@author shennontay
    /**
     * Builds the common display lines shared by all case types.
     * <p>
     * Excludes the {@code Info} field so that subclasses can insert
     * their own fields before the Info line if needed.
     *
     * @return a list of formatted display lines for the base fields
     */
    protected List<String> getBaseDisplayLines() {
        List<String> lines = new ArrayList<>();

        String dateString = DateFormatter.formatDate(date, Settings.getOutputDateFormat());
        String createdAtString = DateFormatter.formatDateTime(createdAt,
                Settings.getDateTimeFormat());
        String updatedAtString = DateFormatter.formatDateTime(updatedAt,
                Settings.getDateTimeFormat());
        String statusString = CaseFormatter.convertStatusToString(isOpen);

        CaseFormatter.addWrappedFieldForRead(lines, "Title", title);
        CaseFormatter.addWrappedFieldForRead(lines,"Case ID", id);
        CaseFormatter.addWrappedFieldForRead(lines,"Status", statusString);
        CaseFormatter.addWrappedFieldForRead(lines,"Category", categoryString);
        CaseFormatter.addWrappedFieldForRead(lines,"Date", dateString);
        CaseFormatter.addWrappedFieldForRead(lines,"Victim", getVictim());
        CaseFormatter.addWrappedFieldForRead(lines,"Officer", getOfficer());
        CaseFormatter.addWrappedFieldForRead(lines,"Created at", createdAtString);
        CaseFormatter.addWrappedFieldForRead(lines,"Updated at", updatedAtString);

        return lines;
    }

    /**
     * Default implementation of the read‑case display for categories
     * without additional fields.
     * <p>
     * Subclasses may override this method to insert extra details
     * before the {@code Info} line.
     *
     * @return an array of formatted display lines for this case
     */
    public String[] getReadCaseDisplay() {
        List<String> displayList = getBaseDisplayLines();
        CaseFormatter.addWrappedFieldForRead(displayList, "Info :", getInfo());

        return displayList.toArray(new String[0]);
    }
    //@@author

    /**
     * Converts this object's data fields into a single comma-separated string suitable for saving.
     * <p>
     * If any string field (e.g. {@code title}, {@code date}, etc.) is {@code null}, it will be replaced
     * with an empty string in the output. The {@code isDeleted} field is represented as {@code "1"} if true
     * and {@code "0"} if false.
     * </p>
     *
     * @return a formatted string containing all of this object's field values
     */
    public String toSaveString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Storage.getSaveDatePattern());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Storage.getSaveDateTimePattern());
        return "category:" + this.category.toString()
                + "|title:" + (this.title == null ? "" : this.title)
                + "|date:" + (this.date == null ? "" : this.date.format(dateFormatter))
                + "|info:" + (this.info == null ? "" : this.info)
                + "|victim:" + (this.victim == null ? "" : this.victim)
                + "|officer:" + (this.officer == null ? "" : this.officer)
                + "|is-deleted:" + (this.isDeleted ? "1" : "0")
                + "|is-open:" + (this.isOpen ? "1" : "0")
                + "|created-at:" + (this.createdAt == null ? "" : this.createdAt.format(dateTimeFormatter))
                + "|updated-at:" + (this.updatedAt == null ? "" : this.updatedAt.format(dateTimeFormatter));
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
