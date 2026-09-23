package com.tailormade.tailor.neoforge.config;

import com.tailormade.tailor.config.TailormadeServerConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    public static final ModConfigSpec SPEC;

    static {
        NeoForgeConfigBuilder builder = new NeoForgeConfigBuilder();
        TailormadeServerConfig.define(builder);
        SPEC = builder.delegate.build();
    }
}