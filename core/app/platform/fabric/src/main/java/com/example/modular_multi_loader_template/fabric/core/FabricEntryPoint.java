package com.example.modular_multi_loader_template.fabric.core;

import com.example.modular_multi_loader_template.common.core.MainEntrypoint;
import net.fabricmc.api.ModInitializer;

public class FabricEntryPoint implements ModInitializer {
    @Override
    public void onInitialize()    {
        // Initialization code
        MainEntrypoint.initialize();
    }
}