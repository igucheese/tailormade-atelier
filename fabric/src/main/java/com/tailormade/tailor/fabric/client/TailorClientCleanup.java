package com.tailormade.tailor.fabric.client;

import com.tailormade.tailor.client.renderer.TailorTextureCompositor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.world.entity.LivingEntity;

public class TailorClientCleanup {
    private TailorClientCleanup() {}

    public static void register() {
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, level) -> {
            if (entity instanceof LivingEntity e) {
                TailorTextureCompositor.invalidate(e.getUUID());
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register(
                (handler, client) -> TailorTextureCompositor.clearAll()
        );
    }
}
