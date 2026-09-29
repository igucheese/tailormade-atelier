package com.tailormade.tailor.fabric;

import com.tailormade.tailor.Tailormade;
import com.tailormade.tailor.fabric.config.FabricConfigInit;
import com.tailormade.tailor.fabric.registries.ModEntityAttributes;
import net.fabricmc.api.ModInitializer;

public class TailormadeFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricConfigInit.init();
        Tailormade.init();
        ModEntityAttributes.init();
    }
}