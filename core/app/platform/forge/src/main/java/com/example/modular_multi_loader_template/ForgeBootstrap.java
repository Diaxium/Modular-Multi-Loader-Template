package com.example.modular_multi_loader_template;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.PROJECT_ID)
public class ForgeBootstrap {
    public void ForgeBootstrap(FMLJavaModLoadingContext context)    {
        // Initialization code
        Common.initialize();
    }
}