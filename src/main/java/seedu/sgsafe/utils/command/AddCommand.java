package seedu.sgsafe.utils.command;

import seedu.sgsafe.domain.casefiles.CaseFormatter;
import seedu.sgsafe.domain.casefiles.type.financial.BurglaryCase;
import seedu.sgsafe.domain.casefiles.type.financial.ScamCase;
import seedu.sgsafe.domain.casefiles.type.property.ArsonCase;
import seedu.sgsafe.domain.casefiles.type.property.VandalismCase;
import seedu.sgsafe.domain.casefiles.type.sexual.RapeCase;
import seedu.sgsafe.domain.casefiles.type.sexual.VoyeurismCase;
import seedu.sgsafe.domain.casefiles.type.traffic.AccidentCase;
import seedu.sgsafe.domain.casefiles.type.traffic.SpeedingCase;
import seedu.sgsafe.domain.casefiles.type.violent.AssaultCase;
import seedu.sgsafe.domain.casefiles.type.violent.MurderCase;
import seedu.sgsafe.domain.casefiles.type.OthersCase;
import seedu.sgsafe.domain.casefiles.type.financial.TheftCase;
import seedu.sgsafe.domain.casefiles.type.violent.RobberyCase;
import seedu.sgsafe.utils.exceptions.InvalidCategoryException;
import seedu.sgsafe.domain.casefiles.Case;
import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.utils.ui.Display;

import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents a command to add a new case.
 * This command contains details about the case, including its title, date, information,
 * and optionally the victim and officer involved.
 */
public class AddCommand extends Command {

    private static final Logger logger = Logger.getLogger(AddCommand.class.getName());

    // Category of the case
    private final String caseCategory;

    // Title of the case
    private final String caseTitle;

    // Date of the case
    private final LocalDate caseDate;

    // Additional information about the case
    private final String caseInfo;

    // Name of the victim involved in the case (optional)
    private final String caseVictim;

    // Name of the officer handling the case (optional)
    private final String caseOfficer;

    /**
     * Constructs an AddCommand with the specified case details.
     *
     * @param caseCategory The category of the case. Cannot be null.
     * @param caseTitle   The title of the case. Cannot be null.
     * @param caseDate    The date of the case. Cannot be null.
     * @param caseInfo    Additional information about the case. Cannot be null.
     * @param caseVictim  The name of the victim involved in the case. Can be null.
     * @param caseOfficer The name of the officer handling the case. Can be null.
     */
    public AddCommand(String caseCategory, String caseTitle, LocalDate caseDate,
                      String caseInfo, String caseVictim, String caseOfficer) {
        this.commandType = CommandType.ADD;
        this.caseCategory = caseCategory.toLowerCase();
        this.caseTitle = caseTitle;
        this.caseDate = caseDate;
        this.caseInfo = caseInfo;
        this.caseVictim = caseVictim;
        this.caseOfficer = caseOfficer;
        logger.log(Level.INFO, "AddCommand created");
    }

    public String getCaseCategory() {
        return caseCategory;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public LocalDate getCaseDate() {
        return caseDate;
    }

    public String getCaseInfo() {
        return caseInfo;
    }

    public String getCaseVictim() {
        return caseVictim;
    }

    public String getCaseOfficer() {
        return caseOfficer;
    }

    // @@author zhengjie2002

    /**
     * Executes the AddCommand, creating a new case based on the provided details.
     * Each case type is instantiated according to the specified category.
     * The case is added to the CaseManager, and a confirmation message is displayed.
     *
     * @throws InvalidCategoryException if the case category is invalid.
     */
    @Override
    public void execute() {
        String id = CaseManager.generateHexId();
        Case newCase;

        switch (caseCategory) {
        case "scam" -> newCase = new ScamCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "theft" -> newCase = new TheftCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "burglary" -> newCase = new BurglaryCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "arson" -> newCase = new ArsonCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "vandalism" -> newCase = new VandalismCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "rape" -> newCase = new RapeCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "voyeurism" -> newCase = new VoyeurismCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "speeding" -> newCase = new SpeedingCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "accident" -> newCase = new AccidentCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "assault" -> newCase = new AssaultCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "murder" -> newCase = new MurderCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "robbery" -> newCase = new RobberyCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        case "others" -> newCase = new OthersCase(id, caseTitle, caseDate, caseInfo, caseVictim, caseOfficer);
        default -> throw new InvalidCategoryException();
        }

        CaseManager.addCase(newCase);
        Display.printMessage("New case added:", generateListTableHeaderMessage(), newCase.getDisplayLine());
    }

    private String generateListTableHeaderMessage() {
        return String.format(CaseFormatter.SUMMARY_FORMAT_STRING, "STATUS", "CATEGORY", "ID", "DATE", "TITLE");
    }

}
