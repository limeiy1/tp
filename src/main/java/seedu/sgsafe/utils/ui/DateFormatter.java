package seedu.sgsafe.utils.ui;
// @@author zhengjie2002

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Utility class for parsing and formatting dates.
 * Provides methods to convert between LocalDate objects and their string representations
 * based on a specified date format.
 */
public class DateFormatter {

    /**
     * Parses a date string into a LocalDate object using the specified date format.
     *
     * @param dateString The date string to be parsed.
     * @param dateFormat The format of the date string, following the DateTimeFormatter pattern.
     * @return A LocalDate object representing the parsed date.
     * @throws DateTimeParseException if the date string cannot be parsed with the given format.
     */
    public static LocalDate parseDate(String dateString, String dateFormat) throws DateTimeParseException {
        // Convert yyyy to uuuu for strict parsing
        String strictDateFormat = dateFormat.replace("yyyy", "uuuu");
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(strictDateFormat).withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Formats a LocalDate object into a string representation using the specified date format.
     *
     * @param date       The LocalDate object to be formatted.
     * @param dateFormat The format to use for the date string, following the DateTimeFormatter pattern.
     * @return A string representation of the formatted date.
     */
    public static String formatDate(LocalDate date, String dateFormat) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return date.format(formatter);
    }

    /**
     * Formats a LocalDateTime object into a string representation using the specified date-time format.
     *
     * @param dateTime       The LocalDateTime object to be formatted.
     * @param dateTimeFormat The format to use for the date-time string, following the DateTimeFormatter pattern.
     * @return A string representation of the formatted date-time.
     */
    public static String formatDateTime(LocalDateTime dateTime, String dateTimeFormat) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        return dateTime.format(formatter);
    }
}
