package seedu.sgsafe.utils.exceptions;

public class InvalidSavedFieldsException extends InvalidSaveStringException {
    private static final String MESSAGE = "The following saved case is missing some mandatory fields: ";
    private static final String TIP = "These fields must be present and must have a value:";
    private static final String REQUIRED_FIELDS =
            "category, title, date, info, is-deleted, is-open, created-at, updated-at";

    public InvalidSavedFieldsException(String saveString) {
        super(MESSAGE, saveString, TIP, REQUIRED_FIELDS);
    }
}
