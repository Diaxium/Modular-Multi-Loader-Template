package com.example.modular_multi_loader_template.platform;

import com.example.modular_multi_loader_template.platform.services.IPlatformHelper;
import com.example.modular_multi_loader_template.platform.types.*;

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
     * @return a {@link PlatformInfo} instance encapsulating details about the Forge platform.
     */
    @Override
    public PlatformInfo getInfo() {
        var versionInfo = FMLLoader.versionInfo(); // Cache version info for reuse

        return new PlatformInfo(
                Platform.Forge,
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
     * Converts a Forge {@link Dist} (distribution) to the corresponding {@link Environment}.
     *
     * @param dist the distribution type provided by Forge.
     * @return {@link Environment#Client} if the {@code dist} is {@link Dist#CLIENT}; otherwise {@link Environment#Server}.
     */
    private Environment getEnvironment(Dist dist) {
        return dist == Dist.CLIENT ? Environment.Client : Environment.Server;
    }

    /**
     * Retrieves a list of all running mods as a list of {@link ModInfo} objects.
     * <p>
     * This method transforms each mod entry from the Forge mod list into a {@link ModInfo}
     * instance. It extracts properties like mod ID, display name, version, description, and
     * additional metadata such as authors, credits, and licenses.
     * </p>
     *
     * @return a list of {@link ModInfo} objects representing each loaded mod.
     */
    private List<ModInfo> fetchRunningMods() {
        Collection<String> authors = new ArrayList<>();
        Collection<String> credits = new ArrayList<>();
        Collection<String> license = new ArrayList<>();

        return ModList.get().getMods().stream().map(modContainer -> {
            Map<String, Object> properties = modContainer.getModProperties();

            authors.add((String) properties.getOrDefault("authors", ""));
            credits.add((String) properties.getOrDefault("credits", ""));
            license.add(modContainer.getOwningFile().getLicense());

            return new ModInfo(
                    modContainer.getModId(),
                    modContainer.getDisplayName(),
                    Platform.Forge,
                    modContainer.getVersion().toString(),
                    modContainer.getDescription(),
                    Environment.Both,
                    authors,
                    credits,
                    license
            );
        }).collect(Collectors.toList());
    }

    /**
     * Determines the operating system on which the game is running.
     *
     * @return an {@link OperatingSystem} enum value representing the current operating system.
     */
    private OperatingSystem getOperatingSystem() {
        return switch (Util.getPlatform()) {
            case WINDOWS -> OperatingSystem.Windows;
            case LINUX   -> OperatingSystem.Linux;
            case OSX     -> OperatingSystem.Macos;
            case SOLARIS -> OperatingSystem.Solaris;
            case UNKNOWN -> OperatingSystem.Unknown;
        };
    }
}
