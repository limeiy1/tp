package seedu.sgsafe.domain.casefiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for formatting case information for display.
 * <p>
 * Provides constants and helper methods to format summary lines,
 * verbose listings, and wrapped fields with consistent alignment.
 */
public class CaseFormatter {

    /** Max characters per display line (used for truncation/wrapping). Counted from after the tab in display */
    static final int MAX_DISPLAY_WIDTH_CHARACTERS = 100;

    //@@author shennontay
    /** Fixed width for field labels in read command. */
    static final String MAX_LABEL_STRING_WIDTH_FOR_READ_COMMAND = "%-17s"; // longest label is "Created by"
    static final String PREFIX_FORMAT_STRING_FOR_READ_COMMAND =
            MAX_LABEL_STRING_WIDTH_FOR_READ_COMMAND + " : ";
    //@@author

    //@@author xelisce
    /** Fixed width for field labels in verbose command. */
    static final String MAX_LABEL_STRING_WIDTH_FOR_VERBOSE = "%-10s"; // longest label is "Created by"
    static final String PREFIX_FORMAT_STRING_FOR_VERBOSE =
            MAX_LABEL_STRING_WIDTH_FOR_VERBOSE + " : ";

    /** Format specifiers for summary fields. */
    static final int MAX_VERBOSE_LINES_PER_FIELD = 5;

    /** Format specifiers for summary fields. */
    static final String MAX_STATUS_STRING_WIDTH   = "%-8s";   // [Closed] is the longest
    static final String MAX_CATEGORY_STRING_WIDTH = "%-16s";  // Traffic accident is the longest
    static final String MAX_ID_STRING_WIDTH       = "%-6s";   // 6-char hex ID
    static final String MAX_DATE_STRING_WIDTH     = "%-10s";  // dd/MM/yyyy
    static final String MAX_TITLE_STRING_WIDTH    = "%s";     // do not truncate title here
    static final int MAX_TITLE_WIDTH              = 40;       // title is truncated separately using this with ellipses

    /** Composite format string for summary lines. */
    public static final String SUMMARY_FORMAT_STRING =
            MAX_STATUS_STRING_WIDTH + " "
                    + MAX_CATEGORY_STRING_WIDTH + " "
                    + MAX_ID_STRING_WIDTH + " "
                    + MAX_DATE_STRING_WIDTH + " "
                    + MAX_TITLE_STRING_WIDTH;
    //@@author

    /**
     * Converts a case status flag into a display string.
     *
     * @param isOpen true if case is open, false if closed
     * @return "Open" or "Closed"
     */
    public static String convertStatusToString(boolean isOpen) {
        return isOpen ? "Open" : "Closed";
    }

    /**
     * Formats a single summary line for a case.
     *
     * @param status   case status flag
     * @param category case category
     * @param id       case ID
     * @param date     case date string
     * @param title    case title
     * @return formatted summary line
     */
    public static String formatCaseSummaryLine(
            boolean status,
            String category,
            String id,
            String date,
            String title) {
        String statusString = "[" + convertStatusToString(status) + "]";
        String titleString = truncateWithEllipses(title, MAX_TITLE_WIDTH);
        return String.format(SUMMARY_FORMAT_STRING,
                statusString, category, id, date, titleString);
    }

    /**
     * Builds a header line for verbose display.
     * Example: {@code "======== CASE ID 000000 ========"}
     *
     * @param id case ID
     * @return formatted header string
     */
    public static String formatCaseIDHeader(String id) {
        return "======== CASE ID " + id + " ========";
    }

    /**
     * Truncates a string to a maximum length, appending "..." if exceeded.
     *
     * @param input          string to truncate
     * @param maximumLength  maximum allowed length
     * @return truncated string with ellipses if needed
     */
    public static String truncateWithEllipses(String input, int maximumLength) {
        if (input == null) {
            return "";
        }
        if (maximumLength <= 0) {
            return "...";
        }
        return input.length() <= maximumLength
                ? input
                : input.substring(0, maximumLength) + "...";
    }

    //@@author shennontay
    /** Adds a wrapped field for the read command output. */
    public static void addWrappedFieldForRead(List<String> lines, String label, String value) {
        if (lines == null || label == null) {
            return;
        }

        if (value == null ||value.isEmpty()) {
            lines.add(formatPrefix(label, false));
            return;
        }

        List<String> fieldLines = wrapField(false, label, value, MAX_DISPLAY_WIDTH_CHARACTERS);
        lines.addAll(fieldLines);
    }
    //@@author

    //@@author xelisce
    /** Adds a wrapped field for the verbose command output. */
    public static void addWrappedFieldForVerbose(List<String> lines, String label, String value) {
        if (lines == null || label == null) {
            return;
        }
        if (value != null && !value.isEmpty()) {
            List<String> fieldLines = wrapField(true, label, value, MAX_DISPLAY_WIDTH_CHARACTERS);
            lines.addAll(fieldLines);
        }
    }
    //@@author

    //@@author shennontay
    /**
     * Wraps a field value into multiple lines with a label prefix.
     *
     * @param verbose true for verbose mode, false for read mode
     * @param label   field label
     * @param value   field value
     * @param width   maximum line width
     * @return list of wrapped lines
     */
    public static List<String> wrapField(boolean verbose, String label, String value, int width) {
        String prefix = formatPrefix(label, verbose);
        int available = width - prefix.length();

        List<String> wrapped = wrapWords(prefix, value, available);

        //@@author xelisce
        // Limit the number of lines per field for verbose printing
        if (verbose && wrapped.size() > MAX_VERBOSE_LINES_PER_FIELD) {
            List<String> limited = new ArrayList<>(wrapped.subList(0, MAX_VERBOSE_LINES_PER_FIELD));
            String last = limited.get(MAX_VERBOSE_LINES_PER_FIELD - 1);
            limited.set(MAX_VERBOSE_LINES_PER_FIELD - 1, last + "...");
            return limited;
        }

        return wrapped;
    }

    //@@author shennontay
    /** Formats a label prefix depending on mode. */
    protected static String formatPrefix(String label, boolean verbose) {
        if (verbose) {
            return String.format(PREFIX_FORMAT_STRING_FOR_VERBOSE, label);
        } else {
            return String.format(PREFIX_FORMAT_STRING_FOR_READ_COMMAND, label);
        }
    }
    //@@author

    /**
     * Immutable record representing the state of a line being wrapped.
     *
     * @param line          current line buffer
     * @param currentLength number of characters used in the line
     */
    protected record LineState(StringBuilder line, int currentLength) {
    }

    /**
     * Splits text into wrapped lines based on available width.
     *
     * @param prefix    label prefix
     * @param value     text to wrap
     * @param available max characters per line (excluding prefix)
     * @return list of wrapped lines
     */
    private static List<String> wrapWords(String prefix, String value, int available) {
        List<String> lines = new ArrayList<>();
        String[] words = value.split(" ");
        StringBuilder line = new StringBuilder(prefix);
        int currentLength = 0;

        for (String word : words) {
            word = handleLongWord(word, available, prefix, lines, line);
            LineState state = tryAddWord(word, line, currentLength, available, prefix, lines);
            line = state.line;
            currentLength = state.currentLength;
        }

        lines.add(line.toString());
        return lines;
    }

    //@@author xelisce
    /**
     * Splits words longer than the available width into chunks.
     *
     * @param word      word to process
     * @param available max characters per line
     * @param prefix    label prefix for indentation
     * @param lines     list of lines to append to
     * @param line      current line buffer
     * @return remaining portion of the word
     */
    protected static String handleLongWord(String word, int available, String prefix,
                                           List<String> lines, StringBuilder line) {
        while (word.length() > available) {
            int chunkLength = available - 1; // leave one character's space for dash
            String chunk = word.substring(0, chunkLength) + "-";
            lines.add(line.toString() + chunk);
            word = word.substring(chunkLength);
            line.setLength(0);
            line.append(" ".repeat(prefix.length())); // indent
        }
        return word;
    }
    //@@author

    //@@author shennontay
    /**
     * Attempts to add a word to the current line, wrapping if necessary.
     *
     * @param word          word to add
     * @param line          current line buffer
     * @param currentLength number of characters already in the line
     * @param available     max characters per line
     * @param prefix        label prefix for indentation
     * @param lines         list of lines to append to
     * @return updated line state
     */
    protected static LineState tryAddWord(String word, StringBuilder line, int currentLength,
                                          int available, String prefix, List<String> lines) {
        // If word doesn't fit, flush current line
        if (currentLength + word.length() > available) {
            lines.add(line.toString());
            line = new StringBuilder(" ".repeat(prefix.length()));
            currentLength = 0;
        }

        // Add space if not first word
        if (currentLength > 0) {
            line.append(" ");
            currentLength++;
        }

        line.append(word);
        currentLength += word.length();

        return new LineState(line, currentLength);
    }

    public static String safeString(Integer val) {
        return val == null ? "" : val.toString();
    }
}
