package com.example.modular_multi_loader_template.fabric.platform;

import com.example.modular_multi_loader_template.api.platform.services.IPlatformHelper;
import com.example.modular_multi_loader_template.api.platform.types.PlatformInfo;

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
     * @return a {@link com.example.modular_multi_loader_template.api.platform.types.PlatformInfo} object containing details about the platform,
     *         including the Fabric version, game version, game directory,
     *         development environment flag, mod list supplier, and operating system.
     */
    @Override
    public com.example.modular_multi_loader_template.api.platform.types.PlatformInfo getInfo() {
        final FabricLoader fabricLoader = FabricLoader.getInstance();
        return new PlatformInfo(
                com.example.modular_multi_loader_template.api.platform.types.Platform.Fabric,
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
     * Converts an {@link EnvType} from the Fabric API to the corresponding {@link com.example.modular_multi_loader_template.api.platform.types.Environment}.
     *
     * @param envType the environment type provided by Fabric
     * @return {@link com.example.modular_multi_loader_template.api.platform.types.Environment#Client} if the {@code envType} is CLIENT; otherwise {@link com.example.modular_multi_loader_template.api.platform.types.Environment#Server}
     */
    private com.example.modular_multi_loader_template.api.platform.types.Environment getEnvironment(EnvType envType) {
        return envType == EnvType.CLIENT ? com.example.modular_multi_loader_template.api.platform.types.Environment.Client : com.example.modular_multi_loader_template.api.platform.types.Environment.Server;
    }

    /**
     * Fetches a list of all running mods and converts them to {@link com.example.modular_multi_loader_template.api.platform.types.ModInfo} objects.
     *
     * @return a list of {@link com.example.modular_multi_loader_template.api.platform.types.ModInfo} objects representing each loaded mod.
     */
    private List<com.example.modular_multi_loader_template.api.platform.types.ModInfo> fetchRunningMods() {
        final FabricLoader fabricLoader = FabricLoader.getInstance();
        return fabricLoader.getAllMods().stream()
                .map(modContainer -> {
                    final var metadata = modContainer.getMetadata();
                    return new com.example.modular_multi_loader_template.api.platform.types.ModInfo(
                            metadata.getId(),
                            metadata.getName(),
                            metadata.getType().equals("builtin") ? com.example.modular_multi_loader_template.api.platform.types.Platform.Builtin : com.example.modular_multi_loader_template.api.platform.types.Platform.Fabric,
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
     * Maps a Fabric {@link ModEnvironment} to the corresponding {@link com.example.modular_multi_loader_template.api.platform.types.Environment}.
     *
     * @param modEnvironment the mod environment as provided by Fabric
     * @return {@link com.example.modular_multi_loader_template.api.platform.types.Environment#Client} for CLIENT, {@link com.example.modular_multi_loader_template.api.platform.types.Environment#Server} for SERVER,
     *         and {@link com.example.modular_multi_loader_template.api.platform.types.Environment#Both} for any other value.
     */
    private com.example.modular_multi_loader_template.api.platform.types.Environment getModEnvironment(ModEnvironment modEnvironment) {
        return switch (modEnvironment) {
            case CLIENT -> com.example.modular_multi_loader_template.api.platform.types.Environment.Client;
            case SERVER -> com.example.modular_multi_loader_template.api.platform.types.Environment.Server;
            default -> com.example.modular_multi_loader_template.api.platform.types.Environment.Both;
        };
    }

    /**
     * Determines the operating system on which the game is running.
     *
     * @return an {@link com.example.modular_multi_loader_template.api.platform.types.OperatingSystem} enum value representing the current operating system.
     */
    private com.example.modular_multi_loader_template.api.platform.types.OperatingSystem getOperatingSystem() {
        return switch (Util.getPlatform()) {
            case WINDOWS -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Windows;
            case LINUX -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Linux;
            case OSX -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Macos;
            case SOLARIS -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Solaris;
            case UNKNOWN -> com.example.modular_multi_loader_template.api.platform.types.OperatingSystem.Unknown;
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
