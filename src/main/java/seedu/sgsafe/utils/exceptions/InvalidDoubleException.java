package seedu.sgsafe.utils.exceptions;

public class InvalidDoubleException extends InvalidCommandException {
    private static final String STARTING_MESSAGE = "The input for flag '";
    private static final String ENDING_MESSAGE = "' failed to parse.";
    private static final String TIP = "Make sure the input is 0 or a positive number.";


    /**
     * Constructs an {@code InvalidDoubleException} with a default error message
     * stating the correct input type
     */
    public InvalidDoubleException(String flag) {
        super(STARTING_MESSAGE + flag + ENDING_MESSAGE, TIP);
    }
}
