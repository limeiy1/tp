package seedu.sgsafe.utils.command;

import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.utils.exceptions.CaseNotFoundException;
import seedu.sgsafe.utils.ui.Display;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a command to reopen a closed case in the SGSafe system.
 * This command stores the case ID to identify which case to update
 * <p>
 * When executed, this command marks the specified case as open using the
 * {@link CaseManager}, and then displays a confirmation message containing
 * the case's details via the {@link Display} class.
 */
public class OpenCommand extends Command {
    private static final Logger logger = Logger.getLogger(OpenCommand.class.getName());

    private final String caseId;

    public OpenCommand(String caseId) {
        this.commandType = CommandType.OPEN;
        this.caseId = caseId;
        logger.log(Level.INFO, "OpenCommand created");
    }

    @Override
    public void execute() {
        try {
            String displayLine = CaseManager.openCase(caseId);
            Display.printMessage("Case reopened:", displayLine);
        } catch (CaseNotFoundException e) {
            Display.printMessage(e.getErrorMessage());
        }
    }
}
