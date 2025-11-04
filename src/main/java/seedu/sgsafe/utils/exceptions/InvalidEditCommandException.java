package seedu.sgsafe.utils.exceptions;

public class InvalidEditCommandException extends InvalidCommandException {
    private static final String ERROR_MESSAGE = "The case ID is missing or the format is incorrect.";
    private static final String TIP = "Case ID should be exactly 6 characters of 0-9 or A-F.";
    private static final String EXAMPLE = "For example, try: \"edit 000000\" or \"edit 000000 --title new title \"";

    public InvalidEditCommandException() {
        super(ERROR_MESSAGE, TIP, EXAMPLE);
    }
}
