package com.tailormade.tailor.fabric.config;

import com.tailormade.tailor.config.TailormadeServerConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricConfigInit {
    private FabricConfigInit() {}

    public static void init() {
        FabricConfigBuilder builder = new FabricConfigBuilder();
        TailormadeServerConfig.define(builder);

        Path file = FabricLoader.getInstance().getConfigDir().resolve("tailormade-server.json");
        builder.load(file);
    }
}
