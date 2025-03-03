package com.example.modular_multi_loader_template.forge.core;

import com.example.modular_multi_loader_template.common.shared.Constants;
import com.example.modular_multi_loader_template.common.core.MainEntrypoint;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.PROJECT_ID)
public class ForgeEntryPoint {
    public ForgeEntryPoint(FMLJavaModLoadingContext context)    {
        // Initialization code
        MainEntrypoint.initialize();
    }
}