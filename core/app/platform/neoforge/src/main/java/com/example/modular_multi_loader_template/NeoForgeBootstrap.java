package com.example.modular_multi_loader_template;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.PROJECT_ID)
public class NeoForgeBootstrap {
    public NeoForgeBootstrap(IEventBus modBus) {
        Constants.LOG.info("NeoForge Loaded!");

        // Initialization code
        Common.initialize();
    }
}