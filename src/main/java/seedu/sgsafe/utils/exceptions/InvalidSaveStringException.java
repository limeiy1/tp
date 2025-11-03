package seedu.sgsafe.utils.exceptions;

public class InvalidSaveStringException extends RuntimeException {
    private final String[] errorMessages ;

    /**
     * Constructs an InvalidCommandException with the specified error messages.
     *
     * @param errorMessages A variable-length array of error messages describing the reason for the exception.
     *                     Each message provides additional context about the invalid command.
     */

    protected InvalidSaveStringException(String... errorMessages) {
        this.errorMessages = errorMessages;
    }

    /**
     * Retrieves the error messages associated with this exception.
     *
     * @return An array of error messages.
     */
    public String[] getErrorMessage() {
        return errorMessages;
    }
}
