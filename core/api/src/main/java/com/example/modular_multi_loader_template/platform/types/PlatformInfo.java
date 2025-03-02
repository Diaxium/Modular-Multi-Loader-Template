package com.example.modular_multi_loader_template.platform.types;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents information about the current platform and environment.
 *
 * @param type                     The type of platform (e.g., "Fabric", "Forge", "NeoForge", "Quilt", "Vanilla").
 * @param version                  The version of the platform.
 * @param gameVersion              The version of the game.
 * @param gameDirectory            The directory where the game is installed.
 * @param isDevelopment            Indicates whether the platform is running in a development environment (e.g., for testing/debugging).
 * @param environment              The execution environment (e.g., CLIENT, SERVER).
 * @param runningModsSupplier      Supplier for information about currently running mods.
 * @param operatingSystem          The operating system the platform is running on.
 */
public record PlatformInfo(
        Platform type,
        String version,
        String gameVersion,
        Path gameDirectory,
        boolean isDevelopment,
        Environment environment,
        Supplier<List<ModInfo>> runningModsSupplier,  // Use Supplier for dynamic mod retrieval
        OperatingSystem operatingSystem
) {
    public PlatformInfo {
        Objects.requireNonNull(type, "Platform type cannot be null");
        Objects.requireNonNull(version, "Platform version cannot be null");
        Objects.requireNonNull(gameVersion, "Game version cannot be null");
        Objects.requireNonNull(gameDirectory, "Game directory cannot be null");
        Objects.requireNonNull(environment, "Environment cannot be null");
        Objects.requireNonNull(runningModsSupplier, "Running mods supplier cannot be null");
        Objects.requireNonNull(operatingSystem, "Operating system cannot be null");

        if (version.isBlank()) {
            throw new IllegalArgumentException("Platform version cannot be blank");
        }
        if (gameVersion.isBlank()) {
            throw new IllegalArgumentException("Game version cannot be blank");
        }
        if (!gameDirectory.toFile().exists()) {
            throw new IllegalArgumentException("Game directory does not exist: " + gameDirectory);
        }
    }

    /**
     * Gets the configuration directory inside the game directory.
     *
     * @return The path to the config directory.
     */
    public Path getConfigDirectory() {
        return this.gameDirectory.resolve("config");
    }

    /**
     * Gets the saves directory inside the game directory.
     *
     * @return The path to the saves' directory.
     */
    public Path getSavesDirectory() {
        return this.gameDirectory.resolve("saves");
    }

    /**
     * Gets a specific save directory inside the saves' directory.
     *
     * @param saveName The name of the save.
     * @return The path to the specified save directory.
     */
    public Path getSaveDirectory(String saveName) {
        return this.getSavesDirectory().resolve(saveName);
    }

    /**
     * Returns the dynamically fetched running mods.
     */
    public List<ModInfo> getRunningMods() {
        return runningModsSupplier.get();  // Call the supplier to get an updated list
    }

    /**
     * Finds a mod by its mod ID.
     *
     * @param modId The mod ID to search for.
     * @return An Optional containing the ModInfo if found, otherwise empty.
     */
    public Optional<ModInfo> findModInfo(String modId) {
        return this.getRunningMods().stream()
                .filter(modInfo -> modInfo.id().equalsIgnoreCase(modId))
                .findFirst();
    }

    /**
     * Checks if the platform is running on Windows.
     *
     * @return true if running on Windows, false otherwise.
     */
    public boolean isWindows() {
        return operatingSystem == OperatingSystem.Windows;
    }

    /**
     * Checks if the platform is running on macOS.
     *
     * @return true if running on macOS, false otherwise.
     */
    public boolean isMacOs() {
        return operatingSystem == OperatingSystem.Macos;
    }

    /**
     * Checks if the platform is running on Linux.
     *
     * @return true if running on Linux, false otherwise.
     */
    public boolean isLinux() {
        return operatingSystem == OperatingSystem.Linux;
    }

    /**
     * Returns a formatted string representation of the platform information.
     *
     * @return A formatted string containing platform details.
     */
    @Override
    public String toString() {
        return String.format("""
                PlatformInfo {
                    type='%s',
                    version='%s',
                    gameVersion='%s',
                    gameDirectory='%s',
                    isDevelopment=%b,
                    environment='%s',
                    runningMods=%s,
                    operatingSystem='%s'
                }""",
                type, version, gameVersion, gameDirectory, isDevelopment, environment,
                getRunningMods().isEmpty() ? "No Mods Loaded" : getRunningMods(), operatingSystem);
    }
}
