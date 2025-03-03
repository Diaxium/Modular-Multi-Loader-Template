package com.example.modular_multi_loader_template.neoforge.core;

import com.example.modular_multi_loader_template.common.shared.Constants;
import com.example.modular_multi_loader_template.common.core.MainEntrypoint;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.PROJECT_ID)
public class NeoforgeEntryPoint {
    public NeoforgeEntryPoint(IEventBus modBus) {
        Constants.LOG.info("NeoForge Loaded!");

        // Initialization code
        MainEntrypoint.initialize();
    }
}