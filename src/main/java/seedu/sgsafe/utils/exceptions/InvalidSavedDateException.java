package seedu.sgsafe.utils.exceptions;

public class InvalidSavedDateException extends InvalidSaveStringException {
    private static final String MESSAGE = "The following saved case has the wrong format for dates: ";
    private static final String EXTRA1 = "The date field must have the format (dd/MM/yyyy).";
    private static final String EXTRA2 = "The created-at and modified-at fields " +
            "must have the format (dd/MM/yyyy HH:mm:ss).";

    public InvalidSavedDateException(String saveString) {
        super(MESSAGE, saveString, EXTRA1, EXTRA2);
    }
}
