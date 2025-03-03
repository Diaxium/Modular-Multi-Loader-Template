package com.example.modular_multi_loader_template.platform.types;

import com.example.modular_multi_loader_template.api.platform.types.Environment;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentTest {

    @Test
    void testEnumValues() {
        Environment[] envValues = Environment.values();
        assertTrue(Arrays.asList(envValues).contains(Environment.Client));
        assertTrue(Arrays.asList(envValues).contains(Environment.Server));
        assertTrue(Arrays.asList(envValues).contains(Environment.Both));
    }
}