package com.tailormade.tailor.fabric.client;

import com.tailormade.tailor.client.ClientRendererSetup;
import com.tailormade.tailor.client.model.MannequinModel;
import com.tailormade.tailor.client.renderer.MannequinRenderer;
import com.tailormade.tailor.registries.ModBlockEntities;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public class ClientRenderEvents {
    public static void register() {
        EntityRendererRegistry.register(ModBlockEntities.MANNEQUIN, MannequinRenderer::new);
        EntityModelLayerRegistry.register(MannequinModel.LAYER_LOCATION, MannequinModel::createBodyLayer);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerRenderer playerRenderer) {
                ClientRendererSetup.applyPlayerRendererLayers(playerRenderer);
            }
        });
    }
}