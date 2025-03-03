package com.example.modular_multi_loader_template.forge.platform;

import com.example.modular_multi_loader_template.api.platform.services.IPlatformHelper;
import com.example.modular_multi_loader_template.api.platform.types.PlatformInfo;

import net.minecraft.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link IPlatformHelper} for the Forge mod loader.
 * <p>
 * This class provides platform-specific information including the loader version,
 * game version, game directory, development environment flag, mod list supplier,
 * and operating system details for a Forge-based environment.
 * </p>
 */
public class ForgePlatform implements IPlatformHelper {

    /**
     * Retrieves the current platform information for the Forge environment.
     * <p>
     * This method gathers data such as the Forge loader version, Minecraft version,
     * game directory, whether the environment is in development mode, the environment
     * type (client or server), the list of running mods, and the operating system.
     * </p>
     *
     * @return a {@link com.example.modular_multi_loader_template.api.platform.types.PlatformInfo} instance encapsulating details about the Forge platform.
     */
    @Override
    public com.example.modular_multi_loader_template.api.platform.types.PlatformInfo getInfo() {
        var versionInfo = FMLLoader.versionInfo(); // Cache version info for reuse

        return new PlatformInfo(
                com.example.modular_multi_loader_template.api.platform.types.Platform.Forge,
                versionInfo.forgeVersion(),
                versionInfo.mcVersion(),
                FMLLoader.getGamePath(),
                !FMLLoader.isProduction(),
                getEnvironment(FMLLoader.getDist()),
                this::fetchRunningMods,
                getOperatingSystem()
        );
    }

    /**
     * Converts a Forge {@link Dist} (distribution) to the corresponding {@link com.example.modular_multi_loader_template.api.platform.types.Environment}.
     *
     * @param dist the distribution type provided by Forge.
     * @return {@link com.example.modular_multi_loader_template.api.platform.types.Environment#Client} if the {@code dist} is {@link Dist#CLIENT}; otherwise {@link com.example.modular_multi_loader_template.api.platform.types.Environment#Server}.
     */
    private com.example.modular_multi_loader_template.api.platform.types.Environment getEnvironment(Dist dist) {
        return dist == Dist.CLIENT ? com.example.modular_multi_loader_template.api.platform.types.Environment.Client : com.example.modular_multi_loader_template.api.platform.types.Environment.Server;
    }

    /**
     * Retrieves a list of all running mods as a list of {@link com.example.modular_multi_loader_template.api.platform.types.ModInfo} objects.
     * <p>
     * This method transforms each mod entry from the Forge mod list into a {@link com.example.modular_multi_loader_template.api.platform.types.ModInfo}
     * instance. It extracts properties like mod ID, display name, version, description, and
     * additional metadata such as authors, credits, and licenses.
     * </p>
     *
     * @return a list of {@link com.example.modular_multi_loader_template.api.platform.types.ModInfo} objects representing each loaded mod.
     */
    private List<com.example.modular_multi_loader_template.api.platform.types.ModInfo> fetchRunningMods() {
        Collection<String> authors = new ArrayList<>();
        Collection<String> credits = new ArrayList<>();
        Collection<String> license = new ArrayList<>();

        return ModList.get().getMods().stream().map(modContainer -> {
            Map<String, Object> properties = modContainer.getModProperties();

            authors.add((String) properties.getOrDefault("authors", ""));
            credits.add((String) properties.getOrDefault("credits", ""));
            license.add(modContainer.getOwningFile().getLicense());

            return new com.example.modular_multi_loader_template.api.platform.types.ModInfo(
                    modContainer.getModId(),
                    modContainer.getDisplayName(),
                    com.example.modular_multi_loader_template.api.platform.types.Platform.Forge,
                    modContainer.getVersion().toString(),
                    modContainer.getDescription(),
                    com.example.modular_multi_loader_template.api.platform.types.Environment.Both,
                    authors,
                    credits,
                    license
            );
        }).collect(Collectors.toList());
    }

    /**
     * Determines the operating system on which the game is running.
     *
     * @return an {@link com.example.modular_multi_loader_template.api.platform.types.OperatingSystem} enum value representing the current operating system.
     */
    private com.example.modular_multi_loader_template.api.platform.types.OperatingSystem getOperatingSystem() {
        return switch (Util.getPlatform()) {
            case WINDOWS -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Windows;
            case LINUX   -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Linux;
            case OSX     -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Macos;
            case SOLARIS -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Solaris;
            case UNKNOWN -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Unknown;
        };
    }
}
