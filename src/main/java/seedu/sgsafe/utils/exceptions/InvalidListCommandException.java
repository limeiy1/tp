package seedu.sgsafe.utils.exceptions;

/**
 * Represents an exception thrown when a list command is malformed or contains invalid arguments.
 * <p>
 * This exception provides a standard error message along with guidance on the correct format.
 */
public class InvalidListCommandException extends InvalidCommandException {

    private static final String ERROR_MESSAGE = "Your list command format is incorrect.";
    private static final String CORRECT_FORMAT_MESSAGE = "The correct format for a list command is:";
    private static final String CORRECT_COMMAND_FORMAT = "list [--status <open|closed>] [--mode verbose]";
    private static final String EXAMPLE_COMMAND = "Example: list --status closed --mode verbose";
    private static final String TIP = "The items in [ ] are optional.";

    /**
     * Constructs a {@code ListCommandException} with a predefined error message and usage example.
     */
    public InvalidListCommandException() {
        super(ERROR_MESSAGE, CORRECT_FORMAT_MESSAGE, CORRECT_COMMAND_FORMAT, EXAMPLE_COMMAND, TIP);
    }
}
