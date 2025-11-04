package seedu.sgsafe.utils.exceptions;

public class DoubleLengthExceededException extends InvalidCommandException {
    private static final String ERROR_MESSAGE = "You have exceeded the maximum double value for flag: ";
    private static final String USAGE = "Maximum value allowed is 1,000,000,000,000.00";

    public DoubleLengthExceededException(String flag) {
        super(ERROR_MESSAGE + flag, USAGE);
    }
}
