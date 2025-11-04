package seedu.sgsafe.utils.exceptions;

public class InvalidNumberException extends InvalidCommandException {
    private static final String ERROR_MESSAGE = "Error parsing numerical input for flag: ";

    public InvalidNumberException(String flag) {
        super(ERROR_MESSAGE + flag);
    }
}
