package com.example.modular_multi_loader_template.platform.types;

import com.example.modular_multi_loader_template.api.platform.types.Platform;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PlatformTest {

    @Test
    void testEnumValues() {
        Platform[] platforms = Platform.values();
        assertEquals(6, platforms.length);
        assertTrue(Arrays.asList(platforms).contains(Platform.Fabric));
        assertTrue(Arrays.asList(platforms).contains(Platform.Forge));
        assertTrue(Arrays.asList(platforms).contains(Platform.Neoforge));
        assertTrue(Arrays.asList(platforms).contains(Platform.Quilt));
        assertTrue(Arrays.asList(platforms).contains(Platform.Vanilla));
        assertTrue(Arrays.asList(platforms).contains(Platform.Builtin));
    }
}