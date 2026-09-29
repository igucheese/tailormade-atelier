package com.tailormade.tailor.config;

import java.util.function.Supplier;

public class TailormadeServerConfig {
    private TailormadeServerConfig() {}

    public static ConfigValue<Float> DYE_COST_MULTIPLIER;

    public static void define(ConfigBuilder builder) {
        builder.push("Tailormade Server Config");

        // 染色時に必要なインク量の係数
        DYE_COST_MULTIPLIER = builder.defineFloat("dyeCostMultiplier", "Multiplier for the amount of dye required for tailoring. The default value is 1.0f; setting it to 0.0f makes it free.", 1.0f);

        builder.pop();
    }
}
