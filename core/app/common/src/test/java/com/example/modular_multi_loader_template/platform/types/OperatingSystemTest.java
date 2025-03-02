package com.example.modular_multi_loader_template.platform.types;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OperatingSystemTest {

    @Test
    void testEnumValues() {
        OperatingSystem[] osValues = OperatingSystem.values();

        assertTrue(Arrays.asList(osValues).contains(OperatingSystem.Windows));
        assertTrue(Arrays.asList(osValues).contains(OperatingSystem.Linux));
        assertTrue(Arrays.asList(osValues).contains(OperatingSystem.Macos));
        assertTrue(Arrays.asList(osValues).contains(OperatingSystem.Solaris));
        assertTrue(Arrays.asList(osValues).contains(OperatingSystem.Unknown));
    }
}