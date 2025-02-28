package com.example.modular_multi_loader_template;


import net.fabricmc.api.ModInitializer;

public class FabricBootstrap implements ModInitializer {
    @Override
    public void onInitialize()    {
        // Initialization code
        Common.initialize();
    }
}