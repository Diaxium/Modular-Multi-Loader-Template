package com.example.modular_multi_loader_template.platform;

import com.example.modular_multi_loader_template.platform.services.IPlatformHelper;
import com.example.modular_multi_loader_template.platform.types.*;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModEnvironment;
import net.fabricmc.loader.api.metadata.Person;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.Util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link IPlatformHelper} for the Fabric mod loader.
 * <p>
 * This class provides platform information such as the game version, mod details,
 * environment type, and operating system for a Fabric-based environment.
 * </p>
 */
public class FabricPlatform implements IPlatformHelper {

    /**
     * Retrieves platform information specific to Fabric.
     *
     * @return a {@link PlatformInfo} object containing details about the platform,
     *         including the Fabric version, game version, game directory,
     *         development environment flag, mod list supplier, and operating system.
     */
    @Override
    public PlatformInfo getInfo() {
        final FabricLoader fabricLoader = FabricLoader.getInstance();
        return new PlatformInfo(
                Platform.Fabric,
                FabricLoaderImpl.VERSION,
                FabricLoaderImpl.INSTANCE.getGameProvider().getRawGameVersion(),
                fabricLoader.getGameDir(),
                fabricLoader.isDevelopmentEnvironment(),
                getEnvironment(fabricLoader.getEnvironmentType()),
                this::fetchRunningMods,
                getOperatingSystem()
        );
    }

    /**
     * Converts an {@link EnvType} from the Fabric API to the corresponding {@link Environment}.
     *
     * @param envType the environment type provided by Fabric
     * @return {@link Environment#Client} if the {@code envType} is CLIENT; otherwise {@link Environment#Server}
     */
    private Environment getEnvironment(EnvType envType) {
        return envType == EnvType.CLIENT ? Environment.Client : Environment.Server;
    }

    /**
     * Fetches a list of all running mods and converts them to {@link ModInfo} objects.
     *
     * @return a list of {@link ModInfo} objects representing each loaded mod.
     */
    private List<ModInfo> fetchRunningMods() {
        final FabricLoader fabricLoader = FabricLoader.getInstance();
        return fabricLoader.getAllMods().stream()
                .map(modContainer -> {
                    final var metadata = modContainer.getMetadata();
                    return new ModInfo(
                            metadata.getId(),
                            metadata.getName(),
                            metadata.getType().equals("builtin") ? Platform.Builtin : Platform.Fabric,
                            metadata.getVersion().getFriendlyString(),
                            metadata.getDescription(),
                            getModEnvironment(metadata.getEnvironment()),
                            extractNames(metadata.getAuthors()),
                            extractNames(metadata.getContributors()),
                            metadata.getLicense()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Maps a Fabric {@link ModEnvironment} to the corresponding {@link Environment}.
     *
     * @param modEnvironment the mod environment as provided by Fabric
     * @return {@link Environment#Client} for CLIENT, {@link Environment#Server} for SERVER,
     *         and {@link Environment#Both} for any other value.
     */
    private Environment getModEnvironment(ModEnvironment modEnvironment) {
        return switch (modEnvironment) {
            case CLIENT -> Environment.Client;
            case SERVER -> Environment.Server;
            default -> Environment.Both;
        };
    }

    /**
     * Determines the operating system on which the game is running.
     *
     * @return an {@link OperatingSystem} enum value representing the current operating system.
     */
    private OperatingSystem getOperatingSystem() {
        return switch (Util.getPlatform()) {
            case WINDOWS -> OperatingSystem.Windows;
            case LINUX -> OperatingSystem.Linux;
            case OSX -> OperatingSystem.Macos;
            case SOLARIS -> OperatingSystem.Solaris;
            case UNKNOWN -> OperatingSystem.Unknown;
        };
    }

    /**
     * Extracts the names from a collection of {@link Person} objects.
     *
     * @param people a collection of {@link Person} objects (e.g., mod authors or contributors)
     * @return a list of names extracted from the provided collection.
     */
    private List<String> extractNames(Collection<Person> people) {
        return people.stream()
                .map(Person::getName)
                .collect(Collectors.toList());
    }
}
