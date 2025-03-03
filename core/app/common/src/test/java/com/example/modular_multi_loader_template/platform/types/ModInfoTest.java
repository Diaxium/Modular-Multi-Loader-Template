package com.example.modular_multi_loader_template.platform.types;

import com.example.modular_multi_loader_template.api.platform.types.Environment;
import com.example.modular_multi_loader_template.api.platform.types.ModInfo;
import com.example.modular_multi_loader_template.api.platform.types.Platform;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ModInfoTest {

    @Test
    void testValidModInfo() {
        List<String> authors = List.of("Author1");
        List<String> contributors = List.of("Contributor1");
        List<String> licenses = List.of("License1");

        ModInfo modInfo = new ModInfo(
                "mod1",
                "Test Mod",
                Platform.Fabric,
                "1.0",
                "A test mod",
                Environment.Client,
                authors,
                contributors,
                licenses
        );

        assertEquals("mod1", modInfo.id());
        assertEquals("Test Mod", modInfo.name());
        assertEquals(Platform.Fabric, modInfo.type());
        assertEquals("1.0", modInfo.version());
        assertEquals("A test mod", modInfo.description());
        assertEquals(Environment.Client, modInfo.environment());
        assertEquals(authors, modInfo.authors());
    }

    @Test
    void testNullValues() {
        List<String> authors = List.of("Author1");
        List<String> contributors = List.of("Contributor1");
        List<String> licenses = List.of("License1");

        Exception exception = assertThrows(NullPointerException.class, () -> {
            new ModInfo(null, "Test Mod", Platform.Fabric, "1.0", "A test mod",
                    Environment.Client, authors, contributors, licenses);
        });
        assertTrue(exception.getMessage().contains("Mod ID cannot be null"));
    }

    @Test
    void testBlankValues() {
        List<String> authors = List.of("Author1");
        List<String> contributors = List.of("Contributor1");
        List<String> licenses = List.of("License1");

        Exception ex1 = assertThrows(IllegalArgumentException.class, () -> {
            new ModInfo("   ", "Test Mod", Platform.Fabric, "1.0", "A test mod",
                    Environment.Client, authors, contributors, licenses);
        });
        assertTrue(ex1.getMessage().contains("Mod ID cannot be blank"));

        Exception ex2 = assertThrows(IllegalArgumentException.class, () -> {
            new ModInfo("mod1", "  ", Platform.Fabric, "1.0", "A test mod",
                    Environment.Client, authors, contributors, licenses);
        });
        assertTrue(ex2.getMessage().contains("Mod name cannot be blank"));

        Exception ex3 = assertThrows(IllegalArgumentException.class, () -> {
            new ModInfo("mod1", "Test Mod", Platform.Fabric, "   ", "A test mod",
                    Environment.Client, authors, contributors, licenses);
        });
        assertTrue(ex3.getMessage().contains("Mod version cannot be blank"));
    }

    @Test
    void testToString() {
        List<String> authors = List.of("Author1");
        List<String> contributors = List.of("Contributor1");
        List<String> licenses = List.of("License1");

        ModInfo modInfo = new ModInfo(
                "mod1",
                "Test Mod",
                Platform.Fabric,
                "1.0",
                "A test mod",
                Environment.Client,
                authors,
                contributors,
                licenses
        );

        String output = modInfo.toString();
        assertTrue(output.contains("mod1"));
        assertTrue(output.contains("Test Mod"));
        assertTrue(output.contains("Fabric"));
    }
}