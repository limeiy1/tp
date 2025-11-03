package seedu.sgsafe.utils.logger;

import seedu.sgsafe.utils.ui.Display;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Utility class for configuring application-wide logging.
 */
public class LoggerConfig {
    private static final String LOG_FILE_NAME = "./sgsafe.log";
    private static final String ERROR_MESSAGE = "Failed to initialize log file: ";
    private static boolean isConfigured = false;

    /**
     * Configures the root logger to write to a file instead of console.
     * Should be called once at application startup.
     */
    public static void configureLogger() {
        if (isConfigured) {
            return;
        }

        Logger rootLogger = Logger.getLogger("");

        // Remove default console handlers
        java.util.logging.Handler[] handlers = rootLogger.getHandlers();
        for (java.util.logging.Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                rootLogger.removeHandler(handler);
            }
        }

        // Add file handler
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE_NAME, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.ALL);
            isConfigured = true;
        } catch (IOException e) {
            Display.printMessage(ERROR_MESSAGE+ e.getMessage());
        }
    }
}
