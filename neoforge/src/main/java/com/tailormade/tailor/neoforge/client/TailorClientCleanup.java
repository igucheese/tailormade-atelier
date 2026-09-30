package com.tailormade.tailor.neoforge.client;

import com.tailormade.tailor.client.renderer.TailorTextureCompositor;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

import static com.tailormade.tailor.Tailormade.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class TailorClientCleanup {

    @SubscribeEvent
    public static void onEntityLeave(EntityLeaveLevelEvent event) {
        if (!event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof LivingEntity e) {
            TailorTextureCompositor.invalidate(e.getUUID());
        }
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        TailorTextureCompositor.clearAll();
    }
}