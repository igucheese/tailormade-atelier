package com.tailormade.tailor.neoforge;

import com.tailormade.tailor.Tailormade;
import com.tailormade.tailor.neoforge.client.ClientRenderEvents;
import com.tailormade.tailor.neoforge.config.ServerConfig;
import com.tailormade.tailor.registries.ModNetworking;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Tailormade.MODID)
public class TailormadeNeoForge {
    public TailormadeNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        Tailormade.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientRenderEvents.register(modEventBus, NeoForge.EVENT_BUS);
            ModNetworking.registerClientReceivers();
        }
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }
}
