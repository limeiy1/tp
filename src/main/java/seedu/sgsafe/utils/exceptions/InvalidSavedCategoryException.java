package seedu.sgsafe.utils.exceptions;

public class InvalidSavedCategoryException extends InvalidSaveStringException {
    private static final String MESSAGE = "The following save string has an invalid category:";
    private static final String EXTRA = "The valid categories are described in the user guide and must be in UPPERCASE";

    public InvalidSavedCategoryException(String saveString) {
        super(MESSAGE, saveString, EXTRA);
    }
}
