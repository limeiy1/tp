package seedu.sgsafe.utils.exceptions;

public class InvalidIntegerException extends InvalidCommandException {
    private static final String STARTING_MESSAGE = "The integer input for flag '";
    private static final String ENDING_MESSAGE = "' failed to parse.";
    private static final String TIP = "Make sure the input is 0 or a positive integer and not out of range.";

    /**
     * Constructs an {@code InvalidIntegerException} with a default error message
     * stating the correct input type
     */
    public InvalidIntegerException(String flag) {
        super(STARTING_MESSAGE + flag + ENDING_MESSAGE, TIP);
    }
}
