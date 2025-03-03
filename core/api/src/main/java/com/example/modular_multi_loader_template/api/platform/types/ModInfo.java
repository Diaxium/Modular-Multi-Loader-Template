package com.example.modular_multi_loader_template.api.platform.types;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents information about a mod.
 *
 * @param id          The unique identifier of the mod.
 * @param name        The display name of the mod.
 * @param type        The type of the mod.
 * @param version     The version of the mod.
 * @param description A brief description of the mod.
 * @param environment The execution environment for the mod.
 * @param authors     The authors of the mod.
 * @param contributors The contributors of the mod.
 * @param license     The licenses under which the mod is distributed.
 */
public record ModInfo(
        String id,
        String name,
        Platform type,
        String version,
        String description,
        Environment environment,
        Collection<String> authors,
        Collection<String> contributors,
        Collection<String> license
) {
    /**
     * Constructor with validation to ensure required fields are not null or empty.
     */
    public ModInfo {
        Objects.requireNonNull(id, "Mod ID cannot be null");
        Objects.requireNonNull(name, "Mod name cannot be null");
        Objects.requireNonNull(type, "Mod type cannot be null");
        Objects.requireNonNull(version, "Mod version cannot be null");
        Objects.requireNonNull(environment, "Mod environment cannot be null");
        Objects.requireNonNull(authors, "Mod authors cannot be null");
        Objects.requireNonNull(contributors, "Mod contributors cannot be null");
        Objects.requireNonNull(license, "Mod license cannot be null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("Mod ID cannot be blank");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Mod name cannot be blank");
        }
        if (version.isBlank()) {
            throw new IllegalArgumentException("Mod version cannot be blank");
        }
    }

    /**
     * Returns a formatted string representation of the mod.
     *
     * @return A formatted string containing mod details.
     */
    @Override
    public String toString() {
        return String.format("ModInfo{id='%s', name='%s', type='%s', version='%s', description='%s', environment='%s', authors=%s, contributors=%s, license=%s}",
                id, name, type, version, description, environment, authors, contributors, license);
    }
}
