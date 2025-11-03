package seedu.sgsafe.utils.command;

import seedu.sgsafe.utils.ui.Display;

import java.util.logging.Logger;

public class ByeCommand extends Command {

    private static final Logger logger = Logger.getLogger(ByeCommand.class.getName());

    public ByeCommand() {
        this.commandType = CommandType.BYE;
    }

    @Override
    public void execute() {
        logger.info("Executing ByeCommand: Exiting application.");
        Display.printGoodbyeMessage();
        System.exit(0);
    }
}
