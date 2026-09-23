package com.tailormade.tailor.neoforge.commands;

import com.tailormade.tailor.commands.TailormadeCommandRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static com.tailormade.tailor.Tailormade.MODID;

@EventBusSubscriber(modid = MODID)
public class TailormadeCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        TailormadeCommandRegistry.registerCommands(event.getDispatcher());
    }
}