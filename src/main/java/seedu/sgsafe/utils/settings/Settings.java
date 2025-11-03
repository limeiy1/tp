package seedu.sgsafe.utils.settings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * The `Settings` class provides utility methods to manage input and output date formats.
 * It allows setting and retrieving date formats while ensuring the formats are valid.
 */
public class Settings {

    // Default date format used for input and output
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    // Default date-time format used for date and time representation
    private static final String DEAFULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    // Logger for logging purposes
    private static final Logger logger = Logger.getLogger(Settings.class.getName());

    private static String inputDateFormat = DEFAULT_DATE_FORMAT;

    private static String outputDateFormat = DEFAULT_DATE_FORMAT;

    private static String dateTimeFormat = DEAFULT_DATE_TIME_FORMAT;

    public static String getInputDateFormat() {
        return inputDateFormat;
    }

    public static String getOutputDateFormat() {
        return outputDateFormat;
    }

    public static String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * Sets the output date format.
     * Validates the provided format by attempting to create a `DateTimeFormatter`.
     *
     * @param format the new output date format as a `String`.
     */
    public static void setOutputDateFormat(String format) {
        // Validate format by attempting to create a DateTimeFormatter and parse. If it fails it will throw an exception
        validateDateFormat(format);
        outputDateFormat = format;
    }

    /**
     * Sets the input date format.
     * Validates the provided format by attempting to create a `DateTimeFormatter`.
     *
     * @param format the new input date format as a `String`.
     */
    public static void setInputDateFormat(String format) {
        // Validate format by attempting to create a DateTimeFormatter and parse. If it fails it will throw an exception
        validateDateFormat(format);
        inputDateFormat = format;
    }

    /**
     * Sets the date-time format.
     * Validates the provided format by attempting to create a `DateTimeFormatter`.
     *
     * @param format the new date-time format as a `String`.
     */
    public static void setDateTimeFormat(String format) {
        // Validate format by attempting to create a DateTimeFormatter and parse. If it fails it will throw an exception
        validateDateTimeFormat(format);
        dateTimeFormat = format;
    }

    /**
     * Validates the given date format string.
     * It checks if the format is a valid pattern for {@code DateTimeFormatter}.
     *
     * @param format The date format string to validate.
     * @throws IllegalArgumentException if the format is null, empty, or an invalid pattern.
     */
    private static void validateDateFormat(String format) {
        if (format == null || format.isEmpty()) {
            logger.warning("Date time format is null or empty.");
            throw new IllegalArgumentException();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        String formattedDate = testDate.format(formatter);
        LocalDate.parse(formattedDate, formatter);
    }

    /**
     * Validates the given date-time format string.
     * It checks if the format is a valid pattern for {@code DateTimeFormatter}.
     *
     * @param format The date time format string to validate.
     * @throws IllegalArgumentException if the format is null, empty, or an invalid pattern.
     */
    private static void validateDateTimeFormat(String format) {
        if (format == null || format.isEmpty()) {
            logger.warning("Date time format is null or empty.");
            throw new IllegalArgumentException();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime testDateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        String formattedDateTime = testDateTime.format(formatter);
        LocalDateTime.parse(formattedDateTime, formatter);
    }
}
