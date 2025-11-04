package seedu.sgsafe.utils.command;

import seedu.sgsafe.utils.ui.Display;

/**
 * Displays manual for using all available commands.
 * <p>
 * The {@code HelpCommand} prints a summary of every command keyword, its syntax,
 * and example usages to assist the user in navigating SGSafe.
 */
public class HelpCommand extends Command {
    private static final String HELP_TEXT =
                    "SGSAFE HELP MENU\n" +
                    "\tHere is a list of supported commands and their formats. Use this as a quick " +
                    "reference to add, manage, and view cases.\n" +
                    "\tNote: All input fields shown in [brackets] below are optional.\n" +
                    "\t______________________________________________________________________________________" +
                    "____________________________________________\n" +
                    "\n" +
                    "\tADD — Create a new case\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tAdds a new case record to the system. Each case must belong to one of the predefined\n" +
                    "\t\tcategories and include basic information such as title, date, and information.\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\tadd --category CATEGORY --title TITLE --date DATE --info INFO [--victim VICTIM]" +
                    " [--officer OFFICER]\n" +
                    "\n" +
                    "\tExamples:\n" +
                    "\t\tadd --category Theft --title Theft case --date 15/01/2022 --info Stolen wallet " +
                    "--victim John Doe --officer Officer Smith\n" +
                    "\t\tadd --category Burglary --info Burglary at 123 Main St --date 15/01/2022 --title " +
                    "Burglary case\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• CATEGORY must be one of the following:\n" +
                    "\t\t  Burglary, Scam, Theft, Arson, Property, Vandalism, Rape, Voyeurism,\n" +
                    "\t\t  Accident, Speeding, Assault, Murder, Others\n" +
                    "\t\t• Date format defaults to dd/MM/yyyy. You can change it using the 'setting' command.\n" +
                    "\t\t• A maximum of 5000 characters is allowed for all fields.\n" +
                    "\t___________________________________________________________________________________________" +
                    "_______________________________________\n" +
                    "\n" +
                    "\tLIST — View existing cases\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tDisplays all cases currently stored in the system, with optional filters for status " +
                    "and output detail.\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\tlist [--status open|closed] [--mode verbose]\n" +
                    "\n" +
                    "\tExamples:\n" +
                    "\t\tlist\n" +
                    "\t\tlist --status closed\n" +
                    "\t\tlist --status open --mode verbose\n" +
                    "\n" +
                    "\tFlags:\n" +
                    "\t\t--status value    Filters cases by their status.\n" +
                    "\t\t\topen   → Show only open cases\n" +
                    "\t\t\tclosed → Show only closed cases\n" +
                    "\n" +
                    "\t\t--mode value      Controls the output detail level.\n" +
                    "\t\t\tverbose → Detailed multi-line output\n" +
                    "\n" +
                    "\tDisplayed Fields (Default mode):\n" +
                    "\t\t• Status (Open/Closed)\n" +
                    "\t\t• Category\n" +
                    "\t\t• Case ID\n" +
                    "\t\t• Date\n" +
                    "\t\t• Title\n" +
                    "\n" +
                    "\tDisplayed Fields (Verbose mode):\n" +
                    "\t\t• Case ID\n" +
                    "\t\t• Status (Open/Closed)\n" +
                    "\t\t• Category\n" +
                    "\t\t• Title\n" +
                    "\t\t• Date\n" +
                    "\t\t• Info\n" +
                    "\t\t• Created at\n" +
                    "\t\t• Updated at\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• Use the read command to view more fields\n" +
                    "\t________________________________________________________________________________________" +
                    "__________________________________________\n" +
                    "\n" +
                    "\tREAD — Display full details of a specific case\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tShows all available details for a specific case, including both the common base fields and" +
                    " any additional fields\n" +
                    "\t\tunique to the case category.\n" +
                    "\t\tThis provides the most complete view of a case record.\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\tread CASEID\n" +
                    "\n" +
                    "\tExamples:\n" +
                    "\t\tread 00000b\n" +
                    "\n" +
                    "\tDisplayed Fields:\n" +
                    "\t\t• Title\n" +
                    "\t\t• Case ID\n" +
                    "\t\t• Status (Open/Closed)\n" +
                    "\t\t• Category\n" +
                    "\t\t• Date\n" +
                    "\t\t• Victim\n" +
                    "\t\t• Officer\n" +
                    "\t\t• Created at\n" +
                    "\t\t• Updated at\n" +
                    "\t\t• Additional category-specific fields (e.g., Weapon, Road name, Vehicle type, etc.)\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• The case ID must be exactly 6 hexadecimal digits (e.g., 000001, 00beef).\n" +
                    "\t\t• Use 'list' to check the case ID of the case to be read.\n" +
                    "\t___________________________________________________________________________________________" +
                    "_______________________________________\n" +
                    "\n" +
                    "\tEDIT — Modify an existing case\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tEdits the details of an existing case by specifying its case ID and one or more fields to " +
                    "update.\n" +
                    "\t\tIf no flags are provided, the valid editable fields for that case type will be shown.\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\tedit CASEID\n" +
                    "\t\tedit CASEID --flag VALUE [--flag VALUE ...]\n" +
                    "\n" +
                    "\tExamples:\n" +
                    "\t\tedit 000001\n" +
                    "\t\t\tDisplays a list of editable fields for the case.\n" +
                    "\t\tedit 000001 --title Updated Title --date 10/02/2024\n" +
                    "\t\t\tUpdates the title and date of case 000001.\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• All flags must correspond to valid editable fields for that case type.\n" +
                    "\t\t• Invalid flags will cancel the update and display an error message.\n" +
                    "\t\t• The case ID must be exactly 6 hexadecimal digits (e.g., 000001, 00beef).\n" +
                    "\t\t• Use 'list' to check the case ID of the case to be edited.\n" +
                    "\t\t• Date format defaults to dd/MM/yyyy. You can change it using the 'setting' command.\n" +
                    "\t____________________________________________________________________________________________" +
                    "______________________________________\n" +
                    "\n" +
                    "\tCLOSE — Mark a case as closed\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tMarks a case as closed. Closed cases remain in the system but are shown as [Closed].\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\tclose CASEID\n" +
                    "\n" +
                    "\tExamples:\n" +
                    "\t\tclose 000003\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• The case ID must be exactly 6 hexadecimal digits (e.g., 000001, 00beef).\n" +
                    "\t\t• Use 'list' to check the case ID of the case to be closed.\n" +
                    "\t___________________________________________________________________________________" +
                    "_______________________________________________\n" +
                    "\n" +
                    "\tOPEN — Reopen a closed case\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tReopens a previously closed case, changing its status back to [Open].\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\topen CASEID\n" +
                    "\n" +
                    "\tExamples:\n" +
                    "\t\topen 000003\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• The case ID must be exactly 6 hexadecimal digits (e.g., 000001, 00beef).\n" +
                    "\t\t• Use 'list' to check the case ID of the case to be opened.\n" +
                    "\t___________________________________________________________________________________________" +
                    "_______________________________________\n" +
                    "\n" +
                    "\tDELETE — Delete a case\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tPermanently deletes the specified case by its case ID. This action cannot be undone.\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\tdelete CASEID\n" +
                    "\n" +
                    "\tExamples:\n" +
                    "\t\tdelete 00012a\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• The case ID must be exactly 6 hexadecimal digits (e.g., 000001, 00beef).\n" +
                    "\t\t• Use 'list' to check the case ID of the case to be deleted.\n" +
                    "\t____________________________________________________________________________________" +
                    "______________________________________________\n" +
                    "\n" +
                    "\tSETTING — Configure program settings\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tAllows customisation of input and output date formats for the program.\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\tsetting --type TYPE --value VALUE\n" +
                    "\n" +
                    "\tExamples:\n" +
                    "\t\tsetting --type dateinput --value dd-MM-yyyy\n" +
                    "\t\tsetting --type dateoutput --value dd/MM/yyyy\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• TYPE must be either 'dateinput' or 'dateoutput' or 'timestampoutput'.\n" +
                    "\t\t• VALUE must be a valid Java DateTimeFormatter pattern.\n" +
                    "\t\t• Default format is dd/MM/yyyy.\n" +
                    "\t_______________________________________________________________________________________" +
                    "___________________________________________\n" +
                    "\n" +
                    "\tFIND — Filter cases by keywords \n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tDisplays all cases whose titles match the keyword(s) provided.\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\tfind --keyword KEYWORD [--status <open|closed>] \n" +
                    "\n" +
                    "\tExamples:\n" +
                    "\t\tfind --keyword homicide \n" +
                    "\t\tfind --keyword murder at home --status open\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• KEYWORD is a string literal that will be matched. However, it is case-insensitive.\n" +
                    "\t\t• This search is not case-sensitive and finds all cases where the title contains " +
                            "the KEYWORD string.\n" +
                    "\t_______________________________________________________________________________________" +
                    "___________________________________________\n" +
                    "\n" +
                    "\tHELP — Display this help menu\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tDisplays a list of all available commands with usage information and examples.\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\thelp\n" +
                    "\t_________________________________________________________________________________________" +
                    "_________________________________________\n" +
                    "\n" +
                    "\tBYE — Exit the program\n" +
                    "\n" +
                    "\tDescription:\n" +
                    "\t\tSafely terminates the program. All unsaved data will be written before exit.\n" +
                    "\n" +
                    "\tUsage:\n" +
                    "\t\tbye\n" +
                    "\n" +
                    "\tNotes:\n" +
                    "\t\t• After calling bye, no further input is accepted, additional input " +
                            "invalidates the command.\n" +
                    "\t___________________________________________________________________________________________" +
                    "_______________________________________\n" +
                    "\tEnd of help.";



    public HelpCommand() {
        this.commandType = CommandType.HELP;
    }

    @Override
    public void execute() {
        Display.printMessage(HELP_TEXT);
    }
}
