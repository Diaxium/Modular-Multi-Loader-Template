package com.example.modular_multi_loader_template.functions.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * PersistentLogger supports a builder–style API for creating a custom logger with a rolling file appender.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * public static final Logger LOG = new PersistentLogger()
 *         .setLoggerName("MyModule")
 *         .setLogDirectory("MyApp")
 *         .setAppVersion("1.0.0-beta.1")
 *         .setJavaCompatibility("22.0 (supports compatibility 17-22)", "Default", "Max", "Up to Java 23 (class file version 67.0)")
 *         .setDescription("Simple Description")
 *         .useSeparateLogFile(true)
 *         .setLoggingLevels(Level.INFO, Level.WARN, Level.ERROR, Level.DEBUG)
 *         .setCustomLayoutPattern("[%d{HH:mm:ss.SSS}] [%level]: %msg%n%xEx")
 *         .attachConsoleLogging(true)
 *         .build();
 * }</pre>
 *
 * <p>The logger is configured with a custom header (if using separate log files), rollover settings,
 * and optional console appenders. Additional customizations (like overriding the header or triggering policy)
 * are also available.</p>
 */
public class PersistentLogger {

    // Basic configuration fields (for clarity)
    private String loggerName;
    private String logDirectory;

    // Application details and file retention settings
    private String description = "";
    private int maxBackupFiles = 5;
    private boolean useSeparateLogFile = true;

    // Version and Java compatibility details
    private String appVersion;
    private String javaVersion;
    private String defaultCompatibilityLevel;
    private String maxEffectiveCompatibilityLevel;
    private String asmSupportDetails;

    // Logging levels (the effective level is the lowest among the provided levels)
    private Level effectiveLevel = Level.INFO;
    private Level[] loggingLevels;

    // Optional customizations
    private String customLayoutPattern; // Allows overriding the default log message pattern
    private String customHeader;        // Optional header override
    private boolean attachConsole = true; // Enable or disable console logging

    // Custom policy override (if desired)
    private TriggeringPolicy customTriggeringPolicy;

    /**
     * Default constructor.
     */
    public PersistentLogger() {
        // No configuration is performed in the constructor.
    }

    // ----------------------- Builder Methods -----------------------

    public PersistentLogger setLoggerName(String name) {
        this.loggerName = name;
        return this;
    }

    public PersistentLogger setLogDirectory(String directory) {
        this.logDirectory = directory;
        return this;
    }

    public PersistentLogger setAppVersion(String version) {
        this.appVersion = version;
        return this;
    }

    public PersistentLogger setJavaCompatibility(String javaVersion,
                                                 String defaultCompatibility,
                                                 String maxEffectiveCompat,
                                                 String asmSupport) {
        this.javaVersion = javaVersion;
        this.defaultCompatibilityLevel = defaultCompatibility;
        this.maxEffectiveCompatibilityLevel = maxEffectiveCompat;
        this.asmSupportDetails = asmSupport;
        return this;
    }

    public PersistentLogger setDescription(String description) {
        this.description = description;
        return this;
    }

    public PersistentLogger useSeparateLogFile(boolean separate) {
        this.useSeparateLogFile = separate;
        return this;
    }

    public PersistentLogger setMaxBackupFiles(int maxFiles) {
        this.maxBackupFiles = maxFiles;
        return this;
    }

    public PersistentLogger setLoggingLevels(Level... levels) {
        if (levels != null && levels.length > 0) {
            this.loggingLevels = levels;
            this.effectiveLevel = Arrays.stream(levels)
                    .min(Comparator.comparingInt(Level::intLevel))
                    .orElse(Level.INFO);
        }

        return this;
    }

    public PersistentLogger setCustomLayoutPattern(String pattern) {
        this.customLayoutPattern = pattern;
        return this;
    }

    public PersistentLogger setCustomHeader(String header) {
        this.customHeader = header;
        return this;
    }

    public PersistentLogger attachConsoleLogging(boolean attach) {
        this.attachConsole = attach;
        return this;
    }

    public PersistentLogger setCustomTriggeringPolicy(TriggeringPolicy policy) {
        this.customTriggeringPolicy = policy;
        return this;
    }

    // ----------------------- Build Method -----------------------

    public Logger build() {
        if (loggerName == null || logDirectory == null) {
            throw new IllegalStateException("Logger name and log directory must be provided.");
        }
        if (appVersion == null || javaVersion == null ||
                defaultCompatibilityLevel == null || maxEffectiveCompatibilityLevel == null ||
                asmSupportDetails == null) {
            throw new IllegalStateException("App version and Java compatibility details must be set.");
        }
        if (loggingLevels == null || loggingLevels.length == 0) {
            effectiveLevel = Level.INFO;
        }

        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = new LoggerConfig(loggerName, effectiveLevel, false);

        if (useSeparateLogFile) {
            String header = (customHeader != null) ? customHeader :
                    buildHeader(loggerName, logDirectory, description, appVersion, javaVersion,
                            defaultCompatibilityLevel, maxEffectiveCompatibilityLevel, asmSupportDetails);
            PatternLayout layout = createPatternLayout(config, header);
            RollingRandomAccessFileAppender fileAppender = createFileAppender(config, logDirectory, loggerName, maxBackupFiles, layout);
            fileAppender.start();
            loggerConfig.addAppender(fileAppender, null, null);
        } else {
            // Shared file configuration WITHOUT backup functionality.
            String pattern = (customLayoutPattern != null) ? customLayoutPattern :
                    "[%d{ddMMMyyyy HH:mm:ss.SSS}] [%t/%level] [%c]: %msg%n%xEx";
            PatternLayout layout = PatternLayout.newBuilder()
                    .withConfiguration(config)
                    .withCharset(StandardCharsets.UTF_8)
                    .withPattern(pattern)
                    .build();
            String sharedDir = "logs";
            String sharedFile = "latest.log";
            String backupFilePattern = "%d{yyyy-MM-dd_HH-mm-ss}.log.gz";
            RollingRandomAccessFileAppender fileAppender = RollingRandomAccessFileAppender.newBuilder()
                    .withAppend(true)
                    .withFileName(sharedDir + "/" + sharedFile)
                    .withFilePattern(sharedDir + "/" + backupFilePattern)
                    .withPolicy(new SharedFileTriggeringPolicy())
                    .withStrategy(DefaultRolloverStrategy.newBuilder()
                            .withMax(String.valueOf(this.maxBackupFiles))
                            .withConfig(config)
                            .build())
                    .setName("LatestFileAppender")
                    .setLayout(layout)
                    .setConfiguration(config)
                    .build();
            fileAppender.start();
            loggerConfig.addAppender(fileAppender, null, null);
        }

        if (attachConsole) {
            attachConsoleAppenders(loggerConfig, config);
        }

        config.addLogger(loggerName, loggerConfig);
        ctx.updateLoggers();

        return LogManager.getLogger(loggerName);
    }

    // ----------------------- Internal Utility Methods -----------------------

    private String buildHeader(String moduleName, String directory, String description, String version,
                               String javaVersion, String defaultCompat, String maxEffectiveCompat, String asmSupport) {
        String title = directory + " " + moduleName;
        return HeaderBuilder.newBuilder()
                .setTotalWidth(150)
                .setTitle(title)
                .setDescription("Module Description:\n" + (description != null ? description : ""))
                .addDetail("Version", version)
                .addDetail("Java Version", javaVersion)
                .addDetail("Default Compatibility", defaultCompat)
                .addDetail("Max Effective Compatibility", maxEffectiveCompat)
                .addDetail("ASM Support Details", asmSupport)
                .build();
    }

    private PatternLayout createPatternLayout(Configuration config, String header) {
        String pattern = (customLayoutPattern != null) ? customLayoutPattern : "[%d{HH:mm:ss.SSS}] [%level]: %msg%n%xEx";
        return PatternLayout.newBuilder()
                .withConfiguration(config)
                .withCharset(StandardCharsets.UTF_8)
                .withPattern(pattern)
                .withHeader(header)
                .build();
    }

    private RollingRandomAccessFileAppender createFileAppender(Configuration config, String directory, String moduleName, int maxBackups, PatternLayout layout) {
        String fullDir = "logs/" + directory;
        String fileName = moduleName + ".log";
        String backupPattern = moduleName + "-backup-%d{yyyy-MM-dd_HH-mm-ss}.log.gz";
        TriggeringPolicy policy = (customTriggeringPolicy != null) ? customTriggeringPolicy : new CustomTriggeringPolicy();
        return RollingRandomAccessFileAppender.newBuilder()
                .withAppend(true)
                .withFileName(fullDir + "/" + fileName)
                .withFilePattern(fullDir + "/" + backupPattern)
                .withPolicy(policy)
                .withStrategy(DefaultRolloverStrategy.newBuilder()
                        .withMax(String.valueOf(maxBackups))
                        .withConfig(config)
                        .build())
                .setName(moduleName + " FileAppender")
                .setLayout(layout)
                .setConfiguration(config)
                .build();
    }

    private void attachConsoleAppenders(LoggerConfig loggerConfig, Configuration config) {
        Optional.ofNullable(config.getAppenders().get("Console"))
                .ifPresent(appender -> loggerConfig.addAppender(appender, null, null));
        Optional.ofNullable(config.getAppenders().get("SysOut"))
                .ifPresent(appender -> loggerConfig.addAppender(appender, null, null));
        Optional.ofNullable(config.getAppenders().get("ServerGuiConsole"))
                .ifPresent(appender -> loggerConfig.addAppender(appender, null, null));
    }

    // ----------------------- Custom Triggering Policies -----------------------

    private static class CustomTriggeringPolicy implements TriggeringPolicy {
        private boolean firstEvent = true;

        @Override
        public void initialize(RollingFileManager manager) {
            // No initialization needed.
        }

        @Override
        public boolean isTriggeringEvent(LogEvent event) {
            if (firstEvent) {
                firstEvent = false;
                return true;
            }
            return false;
        }
    }

    private static class SharedFileTriggeringPolicy implements TriggeringPolicy {
        @Override
        public void initialize(RollingFileManager manager) {
            // No initialization needed.
        }

        @Override
        public boolean isTriggeringEvent(LogEvent event) {
            return false;
        }
    }
}
