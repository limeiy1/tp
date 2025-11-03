package seedu.sgsafe.utils.exceptions;

public class InvalidIntegerException extends InvalidCommandException {
    private static final String STARTING_MESSAGE = "The input for flag '";
    private static final String ENDING_MESSAGE = "' has to be 0 or a positive integer.";

    /**
     * Constructs an {@code InvalidIntegerException} with a default error message
     * stating the correct input type
     */
    public InvalidIntegerException(String flag) {
        super(STARTING_MESSAGE + flag + ENDING_MESSAGE);
    }
}
