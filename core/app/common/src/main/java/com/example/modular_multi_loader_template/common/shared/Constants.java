package com.example.modular_multi_loader_template.common.shared;

import com.example.modular_multi_loader_template.utils.functions.logging.PersistentLogger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.ClassVisitor;

/**
 * Contains project-wide constants and configurations.
 *
 * <p>This class centralizes key project settings, including metadata and logger configuration.
 * It defines constants for versioning, identification, and a descriptive overview of the project.
 * The logger configuration is set up to provide detailed, consistent logging across the application.
 *
 * <h3>Project Metadata</h3>
 * <ul>
 *   <li>{@link #PROJECT_VERSION} - The current version of the project.</li>
 *   <li>{@link #PROJECT_ID} - A unique identifier for the project.</li>
 *   <li>{@link #PROJECT_NAME} - The human-readable name of the project.</li>
 *   <li>{@link #PROJECT_DESCRIPTION} - A brief description of the project’s purpose.</li>
 * </ul>
 *
 * <h3>Logger Configuration</h3>
 * <p>The {@link #LOG} logger is built using a fluent API from {@code PersistentLogger} with the following features:
 * <ul>
 *   <li>Custom logger name and version details.</li>
 *   <li>Logs are stored in a directory named after the project.</li>
 *   <li>No separate log file is used, but a maximum of 5 backup files is maintained.</li>
 *   <li>Logging is set to {@link Level#DEBUG} to capture detailed information for development and troubleshooting.</li>
 *   <li>A custom layout pattern that includes a timestamp, logger name, log level, message, and exception details.</li>
 *   <li>Console logging is enabled for real-time output.</li>
 *   <li>Java compatibility details are provided based on the ASM library version, indicating support for Java versions 18 to 22,
 *       with an extended note for Java 23 (class file version 67.0).</li>
 * </ul>
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 *     Logger log = Constants.LOG;
 *     log.info("Application started.");
 * </pre>
 *
 * <p>This class should be used as the single source of truth for application-wide constants and logging behavior.
 */
public class Constants {
    /** The current version of the project. */
    public static final String PROJECT_VERSION = "1.0.0-BETA.1";

    /** A unique identifier for the project. */
    public static final String PROJECT_ID = "modular_multi_loader_template";

    /** The human-readable name of the project. */
    public static final String PROJECT_NAME = "Modular-Multi-Loader-Template";

    /** A brief description of the project's purpose. */
    public static final String PROJECT_DESCRIPTION = "A modular multi-loader template for development.";

    /**
     * The version of the ASM library used for bytecode manipulation.
     * <p>
     * This value is dynamically obtained from the package of the {@link ClassVisitor} class.
     */
    private static final String ASM_VERSION = ClassVisitor.class.getPackage().getImplementationVersion();

    /**
     * A persistent logger configured for application-wide use.
     * <p>
     * The logger is created using a fluent configuration API provided by {@link PersistentLogger}. Its settings include:
     * <ul>
     *   <li>Setting the logger name to match the project name and including the project version.</li>
     *   <li>Using a log directory named after the project.</li>
     *   <li>Disabling separate log files while maintaining a maximum of 5 backup files.</li>
     *   <li>Setting the logging level to {@link Level#DEBUG} for detailed output.</li>
     *   <li>Using a custom layout pattern for log messages:
     *       <code>[%d{ddMMMyyyy HH:mm:ss.SSS}][%logger/%level]: %msg%n%xEx</code>, which includes date, logger, level, message, and exceptions.</li>
     *   <li>Enabling console logging for real-time feedback.</li>
     *   <li>Providing Java compatibility information derived from the ASM version:
     *       <ul>
     *         <li>First parameter: The ASM version with its supported Java compatibility range.</li>
     *         <li>Second parameter: The minimum supported Java version ("18").</li>
     *         <li>Third parameter: The maximum supported Java version within the compatibility target ("22").</li>
     *         <li>Fourth parameter: A note on extended compatibility ("Up to Java 23 (class file version 67.0)").</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * <p>This logger can be used throughout the project to ensure consistent logging behavior.
     */
    public static final Logger LOG = new PersistentLogger()
            .setLoggerName(PROJECT_NAME)
            .setAppVersion(PROJECT_VERSION)
            .setDescription(PROJECT_DESCRIPTION)
            .setLogDirectory(PROJECT_NAME)
            .useSeparateLogFile(false)
            .setMaxBackupFiles(5)
            .setLoggingLevels(Level.DEBUG)
            .setCustomLayoutPattern("[%d{ddMMMyyyy HH:mm:ss.SSS}][%logger/%level]: %msg%n%xEx")
            .attachConsoleLogging(true)
            .setJavaCompatibility(
                    // First parameter: ASM version with its supported Java compatibility range
                    String.format("%s (supports compatibility 18-22)", ASM_VERSION),
                    // Second parameter: minimum supported Java version
                    "18",
                    // Third parameter: maximum supported Java version within the compatibility target
                    "22",
                    // Fourth parameter: note on extended compatibility (e.g., class file version details)
                    "Up to Java 23 (class file version 67.0)")
            .build();
}
