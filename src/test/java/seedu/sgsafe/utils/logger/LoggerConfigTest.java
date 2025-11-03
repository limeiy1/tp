package seedu.sgsafe.utils.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.logging.Logger;

class LoggerConfigTest {

    private static final String LOG_FILE_NAME = "./sgsafe.log";

    @BeforeEach
    void setUp() {
        // Ensure the log file is deleted before each test
        File logFile = new File(LOG_FILE_NAME);
        if (logFile.exists()) {
            logFile.delete();
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up the log file after each test
        File logFile = new File(LOG_FILE_NAME);
        if (logFile.exists()) {
            logFile.delete();
        }
    }

    @Test
    void configureLogger_properLoggerConfiguration_configuresSuccessfully() {
        assertDoesNotThrow(() -> LoggerConfig.configureLogger(),
                "Logger configuration should complete without throwing exceptions");

        Logger rootLogger = Logger.getLogger("");
        assertTrue(rootLogger.getHandlers().length > 0,
                "Logger should have handlers configured");
    }

    @Test
    void configureLogger_loggerConfiguredBefore_doesNotConfigureAgain() {
        LoggerConfig.configureLogger();
        long initialLastModified = new File(LOG_FILE_NAME).lastModified();

        LoggerConfig.configureLogger();
        long lastModifiedAfterSecondCall = new File(LOG_FILE_NAME).lastModified();

        assertEquals(initialLastModified, lastModifiedAfterSecondCall,
                "Logger should not be reconfigured if already configured");
    }
}
