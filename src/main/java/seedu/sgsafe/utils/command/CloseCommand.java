package seedu.sgsafe.utils.command;

import seedu.sgsafe.domain.casefiles.CaseManager;
import seedu.sgsafe.utils.exceptions.CaseNotFoundException;
import seedu.sgsafe.utils.ui.Display;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a command to close an existing case in the SGSafe system.
 * This command stores the case ID to identify which case to update
 * <p>
 * When executed, this command marks the specified case as closed using the
 * {@link CaseManager}, and then displays a confirmation message containing
 * the case's details via the {@link Display} class.
 */
public class CloseCommand extends Command {
    private static final Logger logger = Logger.getLogger(CloseCommand.class.getName());

    private final String caseId;

    public CloseCommand(String caseId) {
        assert caseId != null;
        this.commandType = CommandType.CLOSE;
        this.caseId = caseId;
        logger.log(Level.INFO, "CloseCommand created");
    }

    @Override
    public void execute() {
        try {
            String displayLine = CaseManager.closeCase(caseId);
            Display.printMessage("Case closed:", displayLine);
        } catch (CaseNotFoundException e) {
            Display.printMessage(e.getErrorMessage());
        }
    }
}
