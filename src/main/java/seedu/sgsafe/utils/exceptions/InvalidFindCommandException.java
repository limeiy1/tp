package seedu.sgsafe.utils.exceptions;

public class InvalidFindCommandException extends InvalidCommandException {
    private static final String ERROR_MESSAGE = "Your find command format is incorrect.";
    private static final String CORRECT_FORMAT_MESSAGE = "The correct format for a find command is";
    private static final String EXAMPLE_COMMAND =
            "find --keyword KEYWORD [--status <open|closed>]";

    public InvalidFindCommandException() {
        super(ERROR_MESSAGE, CORRECT_FORMAT_MESSAGE, EXAMPLE_COMMAND);
    }
}
