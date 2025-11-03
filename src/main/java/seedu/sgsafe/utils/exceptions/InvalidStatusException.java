package seedu.sgsafe.utils.exceptions;

/**
 * Exception thrown when the status flag in a list command is invalid.
 * This class extends {@code InvalidCommandException} and provides
 * a predefined error message, correct format message, and an example
 * command to guide the user.
 */

public class InvalidStatusException extends InvalidCommandException {

    private static final String ERROR_MESSAGE = "Your list status flag is incorrect.";
    private static final String CORRECT_FORMAT_MESSAGE = "The correct format is:";
    private static final String CORRECT_COMMAND_FORMAT = "--status <open|closed>";
    private static final String EXAMPLE_COMMAND = "You may exclude this flag if you want to list all cases.";

    /**
     * Constructs an {@code InvalidStatusException} with a predefined error message,
     * correct format message, and an example command.
     */
    public InvalidStatusException() {
        super(ERROR_MESSAGE, CORRECT_FORMAT_MESSAGE, CORRECT_COMMAND_FORMAT, EXAMPLE_COMMAND);
    }
}
