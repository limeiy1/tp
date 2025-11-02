package seedu.sgsafe.utils.ui;

import seedu.sgsafe.utils.command.AddCommand;
import seedu.sgsafe.utils.command.ByeCommand;
import seedu.sgsafe.utils.command.CaseListingMode;
import seedu.sgsafe.utils.command.CloseCommand;
import seedu.sgsafe.utils.command.Command;
import seedu.sgsafe.utils.command.FindCommand;
import seedu.sgsafe.utils.command.HelpCommand;
import seedu.sgsafe.utils.command.ListCommand;
import seedu.sgsafe.utils.command.EditCommand;
import seedu.sgsafe.utils.command.EditPromptCommand;
import seedu.sgsafe.utils.command.DeleteCommand;
import seedu.sgsafe.utils.command.ReadCommand;

import seedu.sgsafe.utils.command.OpenCommand;
import seedu.sgsafe.utils.command.SettingCommand;
import seedu.sgsafe.utils.command.SettingType;
import seedu.sgsafe.utils.exceptions.DuplicateFlagException;
import seedu.sgsafe.utils.exceptions.EmptyCommandException;
import seedu.sgsafe.utils.exceptions.IncorrectFlagException;
import seedu.sgsafe.utils.exceptions.InputLengthExceededException;
import seedu.sgsafe.utils.exceptions.InvalidByeCommandException;
import seedu.sgsafe.utils.exceptions.InvalidCaseIdException;
import seedu.sgsafe.utils.exceptions.InvalidCloseCommandException;
import seedu.sgsafe.utils.exceptions.InvalidDateInputException;
import seedu.sgsafe.utils.exceptions.InvalidEditCommandException;
import seedu.sgsafe.utils.exceptions.InvalidFindCommandException;
import seedu.sgsafe.utils.exceptions.InvalidFormatStringException;
import seedu.sgsafe.utils.exceptions.InvalidHelpCommandException;
import seedu.sgsafe.utils.exceptions.InvalidIntegerException;
import seedu.sgsafe.utils.exceptions.InvalidListCommandException;
import seedu.sgsafe.utils.exceptions.InvalidAddCommandException;
import seedu.sgsafe.utils.exceptions.InvalidOpenCommandException;
import seedu.sgsafe.utils.exceptions.InvalidReadCommandException;
import seedu.sgsafe.utils.exceptions.InvalidSettingCommandException;
import seedu.sgsafe.utils.exceptions.UnknownCommandException;
import seedu.sgsafe.utils.exceptions.InvalidDeleteCommandException;
import  seedu.sgsafe.utils.exceptions.InvalidCharacterException;
import seedu.sgsafe.utils.settings.Settings;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Responsible for interpreting raw user input and converting it into structured {@link Command} objects.
 * Acts as the first step in the command execution pipeline by identifying the command type and validating arguments.
 */
public class Parser {

    // Logger for logging parsing activities and errors
    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    // Regular expression used to split input into flags and their values. It retains the delimiter.
    private static final String FLAG_SEPARATOR_REGEX = "\\s+(?=--)";

    // Prefix used to identify flags in the input
    private static final String FLAG_PREFIX = "--";

    // Validator instance for input validation
    private static final Validator validator = new Validator();

    // Maximum allowed length for any input value
    private static final int MAX_INPUT_LENGTH = 5000;

    // Placeholder for escaped flag sequences
    private static final String ESCAPED_FLAG_PLACEHOLDER = "<<<ESCAPED_DOUBLE_DASH>>>";

    /**
     * Parses raw user input into a {@link Command} object.
     * <p>
     * This method trims the input, extracts the command keyword, and delegates to specialized parsers
     * based on the keyword. If the input is empty or unrecognized, an appropriate exception is thrown.
     *
     * @param userInput the full input string entered by the user
     * @return a {@link Command} representing the parsed action
     * @throws EmptyCommandException       if the input is empty or contains only whitespace
     * @throws UnknownCommandException     if the command keyword is not recognized
     * @throws InvalidListCommandException if the {@code list} command contains unexpected arguments
     */
    public static Command parseInput(String userInput) {
        String cleanedUserInput = cleanUserInput(userInput);
        String keyword = getKeywordFromUserInput(cleanedUserInput).toLowerCase();
        String remainder = getRemainderFromUserInput(cleanedUserInput);
        if(remainder.contains("|")) {
            throw new InvalidCharacterException();
        }

        return switch (keyword) {
        case "list" -> parseListCommand(remainder);
        case "add" -> parseAddCommand(remainder);
        case "edit" -> parseEditCommand(remainder);
        case "close" -> parseCloseCommand(remainder);
        case "open" -> parseOpenCommand(remainder);
        case "delete" -> parseDeleteCommand(remainder);
        case "bye" -> parseByeCommand(remainder);
        case "help" -> parseHelpCommand(remainder);
        case "setting" -> parseSettingCommand(remainder);
        case "read" -> parseReadCommand(remainder);
        case "find" -> parseFindCommand(remainder);
        default -> throw new UnknownCommandException(userInput, keyword);
        };
    }

    /**
     * Cleans the raw user input by trimming whitespace and validating non-emptiness.
     *
     * @param userInput the raw input string from the user
     * @return a trimmed, non-empty input string
     * @throws EmptyCommandException if the input is empty after trimming
     */
    private static String cleanUserInput(String userInput) {
        userInput = userInput.strip();
        if (userInput.isEmpty()) {
            throw new EmptyCommandException();
        }
        return userInput;
    }

    /**
     * Extracts the command keyword from the user input.
     * <p>
     * The keyword is assumed to be the first word before any space.
     *
     * @param userInput the full input string
     * @return the command keyword (e.g., "add", "list", "edit")
     */
    private static String getKeywordFromUserInput(String userInput) {
        int spaceIndex = userInput.indexOf(" ");
        if (spaceIndex != -1) {
            return userInput.substring(0, spaceIndex).trim();
        } else {
            return userInput;
        }
    }

    /**
     * Extracts the remainder of the input after the command keyword.
     * <p>
     * Used to pass arguments to command-specific parsers.
     *
     * @param userInput the full input string
     * @return the remainder of the input after the keyword, or an empty string if none
     */
    private static String getRemainderFromUserInput(String userInput) {
        int spaceIndex = userInput.indexOf(" ");
        if (spaceIndex != -1 && userInput.length() > spaceIndex + 1) {
            return userInput.substring(spaceIndex + 1).trim();
        } else {
            return "";
        }
    }

    //@@ author xelisce
    /**
     * Parses the {@code list} command and validates its optional {@code --status} and {@code --mode} flags.
     * <p>
     * Supported formats include:
     * <ul>
     *   <li>{@code list} — Lists cases using the default mode and non-verbose output</li>
     *   <li>{@code list --status open} — Lists only open cases</li>
     *   <li>{@code list --status closed} — Lists only closed cases</li>
     *   <li>{@code list --status all} — Lists all cases</li>
     *   <li>{@code list --mode verbose} — Enables verbose output</li>
     *   <li>{@code list --status open --mode summary} — Lists open cases with summary output</li>
     * </ul>
     * If {@code --status} is present, its value must be one of {@code open}, {@code closed}, or {@code all}.
     * If {@code --mode} is present, its value must be either {@code verbose} or {@code summary}.
     * Any invalid flag or value will result in a {@link IncorrectFlagException}.
     *
     * @param remainder the portion of the input following the {@code list} keyword
     * @return a {@link ListCommand} with the appropriate {@link CaseListingMode} and verbosity setting
     * @throws IncorrectFlagException if the input contains invalid flags or unsupported values
     */
    private static Command parseListCommand(String remainder) {
        if (remainder.isEmpty()) {
            return new ListCommand(CaseListingMode.DEFAULT, false);
        }

        Map<String, String> flagValues = extractFlagValues(remainder);
        List<String> validFlags = List.of("status", "mode");

        if (!validator.haveValidFlags(flagValues, validFlags)) {
            throw new InvalidListCommandException();
        }

        CaseListingMode listingMode = parseListStatus(flagValues.get("status"));
        boolean isVerbose = parseListMode(flagValues.get("mode"));

        return new ListCommand(listingMode, isVerbose);
    }

    /**
     * Parses the {@code --status} flag value and maps it to a {@link CaseListingMode}.
     * <p>
     * Valid values are:
     * <ul>
     *   <li>{@code open} — Maps to {@link CaseListingMode#OPEN_ONLY}</li>
     *   <li>{@code closed} — Maps to {@link CaseListingMode#CLOSED_ONLY}</li>
     *   <li>{@code all} — Maps to {@link CaseListingMode#ALL}</li>
     * </ul>
     * If the value is {@code null} or empty, {@link CaseListingMode#DEFAULT} is returned.
     * Any other value will result in a {@link IncorrectFlagException}.
     *
     * @param status the value of the {@code --status} flag
     * @return the corresponding {@link CaseListingMode}
     * @throws IncorrectFlagException if the status value is invalid
     */
    private static CaseListingMode parseListStatus(String status) {
        if (status == null || status.isEmpty()) {
            return CaseListingMode.DEFAULT;
        }

        return switch (status.toLowerCase()) {
        case "open" -> CaseListingMode.OPEN_ONLY;
        case "closed" -> CaseListingMode.CLOSED_ONLY;
        case "all" -> CaseListingMode.ALL;
        default -> throw new InvalidListCommandException();
        };
    }

    /**
     * Parses the {@code --mode} flag value and determines verbosity.
     * <p>
     * Valid values are:
     * <ul>
     *   <li>{@code verbose} — Enables verbose output</li>
     *   <li>{@code summary} — Enables summary (non-verbose) output</li>
     * </ul>
     * If the value is {@code null} or empty, summary mode is assumed by default.
     * Any other value will result in a {@link IncorrectFlagException}.
     *
     * @param mode the value of the {@code --mode} flag
     * @return {@code true} if verbose mode is enabled, {@code false} otherwise
     * @throws IncorrectFlagException if the mode value is invalid
     */
    private static boolean parseListMode(String mode) {
        if (mode == null || mode.isEmpty()) {
            return false; // default to non-verbose
        }

        return switch (mode.toLowerCase()) {
        case "verbose" -> true;
        case "summary" -> false;
        default -> throw new InvalidListCommandException();
        };
    }

    //@@ author

    /**
     * Parses the {@code add} command and validates its arguments.
     * <p>
     * This method extracts flags and their values from the input, ensuring that required fields
     * (category, title, date, and info) are present.
     *
     * @param remainder the portion of the input following the {@code add} keyword
     * @return a valid {@link AddCommand} if arguments are invalid
     */
    private static Command parseAddCommand(String remainder) {
        // List of required flags for the add command
        List<String> requiredFlags = List.of("category", "title", "date", "info");

        // List of valid flags to be taken as input from the user
        List<String> VALID_FLAGS = List.of("category", "title", "date", "info", "victim", "officer");

        LocalDate date;

        if (validator.inputIsEmpty(remainder)) {
            throw new InvalidAddCommandException();
        }

        Map<String, String> flagValues = extractFlagValues(remainder);

        if (!validator.haveAllRequiredFlags(flagValues, requiredFlags) ||
                !validator.haveValidFlags(flagValues, VALID_FLAGS)) {
            throw new InvalidAddCommandException();
        }

        try {
            date = DateFormatter.parseDate(flagValues.get("date"), Settings.getInputDateFormat());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Invalid date format detected");
            throw new InvalidDateInputException();
        }

        return new AddCommand(flagValues.get("category"), flagValues.get("title"), date,
                flagValues.get("info"), flagValues.get("victim"), flagValues.get("officer"));
    }

    /**
     * Parses the {@code close} command and validates its argument.
     * <p>
     * This method expects a string representing the caseId to close.
     * If the input is empty, an {@link InvalidCloseCommandException}
     * will be thrown.
     * If the caseId format is wrong, an {@link InvalidCaseIdException}
     * will be thrown.
     *
     * @param remainder the portion of the input following the {@code close} keyword
     * @return a valid {@link CloseCommand} if the argument is a valid caseId
     * @throws InvalidCloseCommandException if the argument is missing
     * @throws InvalidCaseIdException       if the caseId format is wrong
     */
    private static Command parseCloseCommand(String remainder) {
        if (validator.inputIsEmpty(remainder)) {
            throw new InvalidCloseCommandException();
        }
        if (!validator.isValidCaseId(remainder)) {
            throw new InvalidCaseIdException();
        }
        return new CloseCommand(remainder);
    }

    /**
     * Parses the {@code close} command and validates its argument.
     * <p>
     * This method expects a string representing the caseId to open.
     * If the input is empty, an {@link InvalidCloseCommandException}
     * will be thrown.
     * If the caseId format is wrong, an {@link InvalidCaseIdException}
     * will be thrown.
     *
     * @param remainder the portion of the input following the {@code open} keyword
     * @return a valid {@link OpenCommand} if the argument is a valid caseId
     * @throws InvalidOpenCommandException if the argument is missing
     * @throws InvalidCaseIdException      if the caseId format is wrong
     */
    private static Command parseOpenCommand(String remainder) {
        if (validator.inputIsEmpty(remainder)) {
            throw new InvalidOpenCommandException();
        }
        if (!validator.isValidCaseId(remainder)) {
            throw new InvalidCaseIdException();
        }
        return new OpenCommand(remainder);
    }

    /**
     * Extracts flags and their corresponding values from the input string.
     * <p>
     * The input is split based on the defined flag separator regex, and each part is processed
     * to isolate the flag name and its value. The results are stored in a map.
     * \-- is used as an escape character for -- to use -- in body text.
     *
     * @param input the portion of the input containing flags and their values
     * @return a map of flag names with their corresponding values
     * @throws DuplicateFlagException if a flag appears more than once in the input
     * @throws IncorrectFlagException if a flag is malformed or missing its value
     */
    private static Map<String, String> extractFlagValues(String input) {

        // Replace \-- with a placeholder
        String escapedInput = input.replace("\\--", ESCAPED_FLAG_PLACEHOLDER);

        String[] parts = escapedInput.split(FLAG_SEPARATOR_REGEX);
        Map<String, String> flagValues = new HashMap<>();

        for (String part : parts) {

            // First, the prefix -- is removed.
            String trimmedPart = part.replaceFirst(FLAG_PREFIX, "").trim();
            if (trimmedPart.isEmpty()) {
                logger.log(Level.WARNING, "Incorrect flag usage detected");
                throw new IncorrectFlagException();
            }

            int spaceIndex = trimmedPart.indexOf(" ");
            if (spaceIndex == -1) {
                logger.log(Level.WARNING, "Incorrect flag usage detected");
                throw new IncorrectFlagException();
            }

            // Then we separate the flag from its value.
            String flag = trimmedPart.substring(0, spaceIndex).trim();
            String value = trimmedPart.substring(spaceIndex + 1).trim();

            // Replace the placeholder back with --
            value = value.replace(ESCAPED_FLAG_PLACEHOLDER, "--");

            if(value.length() > MAX_INPUT_LENGTH){
                logger.log(Level.WARNING, "Input exceeds character limit");
                throw new InputLengthExceededException();
            }

            if (flagValues.containsKey(flag)) {
                logger.log(Level.WARNING, "Duplicated flags detected");
                throw new DuplicateFlagException();
            }

            // Finally, we store the flag and its value in the map.
            flagValues.put(flag, value);
        }
        return flagValues;
    }

    /**
     * Parses the 'edit' command input.
     * <p>
     * Supports two modes:
     * <ol>
     *   <li>{@code edit <caseId>} - Shows valid flags for the case</li>
     *   <li>{@code edit <caseId> --flag value} - Directly edits the case</li>
     * </ol>
     */
    private static Command parseEditCommand(String remainder) {
        if (remainder.isEmpty()) {
            throw new InvalidEditCommandException();
        }

        int firstSpaceIndex = remainder.indexOf(" ");

        // Case 1: Only case ID is provided (e.g. "edit 000000")
        if (firstSpaceIndex == -1) {
            if (!validator.isValidCaseId(remainder)) {
                throw new InvalidCaseIdException();
            }
            return new EditPromptCommand(remainder);
        }

        // Case 2: Flags provided together with case ID (e.g. "edit 000000 --location 123 Street")
        String caseId = remainder.substring(0, firstSpaceIndex);
        if (!validator.isValidCaseId(caseId)) {
            throw new InvalidCaseIdException();
        }

        String replacements = remainder.substring(firstSpaceIndex + 1).trim();

        // Check if replacements start with --
        if (replacements.startsWith("--")) {
            Map<String, String> flagValues = extractFlagValues(replacements);
            Map<String, Object> typedFlagValues = convertFlagValueTypes(flagValues);
            return new EditCommand(caseId, typedFlagValues);
        } else {
            logger.log(Level.WARNING, "Incorrect flag usage detected");
            throw new IncorrectFlagException();
        }
    }

    /**
     * Converts raw flag values from strings to their appropriate types based on flag names.
     * @param rawValues map of flag names and their string values as input by the user
     * @return map of flag names and their values converted to appropriate types
     * @throws InvalidDateInputException if a date value cannot be parsed using the system input date format
     * @throws InvalidIntegerException if a numerical flag value is non-numeric or negative
     */
    public static Map<String, Object> convertFlagValueTypes(Map<String, String> rawValues) {
        logger.fine("Starting flag value type conversion.");

        Map<String, Object> typedValues = new HashMap<>();
        LocalDate parsedDate;

        for (Map.Entry<String, String> entry : rawValues.entrySet()) {
            String flag = entry.getKey();
            String value = entry.getValue();

            // Convert based on flag type (e.g. date for "date", integer for "no-of-victims")
            switch (flag) {
            case "date":
                try {
                    parsedDate = DateFormatter.parseDate(rawValues.get("date"), Settings.getInputDateFormat());
                    typedValues.put(flag, parsedDate);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Failed to parse date value '" + value + "' for flag '" + flag + "'.");
                    throw new InvalidDateInputException();
                }
                break;

            case "exceeded-speed",
                 "number-of-victims",
                 "speed-limit",
                 "monetary-damage",
                 "financial-value",
                 "number-of-casualties":
                try {
                    Integer intValue = Integer.parseInt(value);
                    if (intValue < 0) {
                        logger.log(Level.WARNING,"Value for flag '" + flag + "' is negative: " + intValue);
                        throw new InvalidIntegerException(flag);
                    }
                    typedValues.put(flag, intValue);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "Failed to parse integer from non-numeric string '" + value
                            + "' for flag '" + flag + "'.");
                    throw new InvalidIntegerException(flag);
                }
                break;
            default:
                // All other flags remain as String
                typedValues.put(flag, value);
            }
        }

        logger.fine("Finished flag value type conversion.");
        return typedValues;
    }


    /**
     * Parses the 'delete' command input, validates its format, and constructs an DeleteCommand object.
     * Throws an InvalidDeleteCommandException if the input is missing or incorrectly formatted.
     */
    private static Command parseDeleteCommand(String remainder) {
        if (!validator.isValidCaseId(remainder)) {
            throw new InvalidDeleteCommandException();
        }
        return new DeleteCommand(remainder);
    }

    private static Command parseSettingCommand(String remainder) {
        List<String> requiredFlags = List.of("type", "value");
        List<String> validFlags = List.of("type", "value");

        if (validator.inputIsEmpty(remainder)) {
            throw new InvalidSettingCommandException(false);
        }

        Map<String, String> flagValues = extractFlagValues(remainder);

        if (!validator.haveAllRequiredFlags(flagValues, requiredFlags) ||
                !validator.haveValidFlags(flagValues, validFlags)) {
            throw new InvalidSettingCommandException(false);
        }

        SettingType settingType = parseSettingType(flagValues.get("type"));
        if(!validator.isValidDateTimeString(flagValues.get("value"))) {
            throw new InvalidFormatStringException();
        }

        return new SettingCommand(settingType, flagValues.get("value"));
    }

    private static SettingType parseSettingType(String typeString) {
        try {
            return SettingType.valueOf(typeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSettingCommandException(true);
        }
    }

    /**
     * Parses the 'read' command input, validates its format, and constructs a ReadCommand object.
     * Throws an InvalidReadCommandException if the input is missing or incorrectly formatted.
     */

    private static Command parseReadCommand(String remainder) {
        if (!validator.isValidCaseId(remainder)) {
            throw new InvalidReadCommandException();
        }
        return new ReadCommand(remainder);
    }

    private static Command parseByeCommand(String remainder) {
        if (!remainder.isEmpty()) {
            throw new InvalidByeCommandException();
        }
        return new ByeCommand();
    }

    private static Command parseHelpCommand(String remainder) {
        if (!remainder.isEmpty()) {
            throw new InvalidHelpCommandException();
        }
        return new HelpCommand();
    }

    //@@ author zhengjie2002
    /**
     * Parses the {@code find} command and validates its arguments.
     * <p>
     * This method extracts the {@code --keyword} flag from the input and validates that it is present.
     * The keyword is used to search for cases with matching titles or descriptions.
     * <p>
     * Supported format:
     * <ul>
     *   <li>{@code find --keyword <search_term>} — Searches for cases containing the specified keyword</li>
     * </ul>
     * The {@code --keyword} flag is required. If it is missing or if any invalid flags are present,
     * an {@link InvalidFindCommandException} will be thrown.
     *
     * @param remainder the portion of the input following the {@code find} keyword
     * @return a {@link FindCommand} configured with the specified search keyword
     * @throws InvalidFindCommandException if the input is empty, missing the required {@code --keyword} flag,
     *                                     or contains invalid flags
     */
    private static Command parseFindCommand(String remainder) {
        // List of required flags for the find command
        List<String> requiredFlags = List.of("keyword");

        //  List of valid flags to be taken as input from the user
        List<String> validFlags = List.of("keyword");


        if (validator.inputIsEmpty(remainder)) {
            throw new InvalidFindCommandException();
        }

        Map<String, String> flagValues = extractFlagValues(remainder);

        if (!validator.haveAllRequiredFlags(flagValues, requiredFlags) ||
                !validator.haveValidFlags(flagValues, validFlags)) {
            throw new InvalidFindCommandException();
        }

        return new FindCommand(flagValues.get("keyword"));
    }

    //@@ author
}
