package com.example.modular_multi_loader_template.common.core;

import com.example.modular_multi_loader_template.api.platform.Services;
import com.example.modular_multi_loader_template.common.shared.Constants;

public class MainEntrypoint {
    public static void initialize()    {
        Constants.LOG.info("Common Loaded!");

        // Add any necessary initialization code here.
        Constants.LOG.info(Services.PLATFORM.getInfo().toString());
    }
}