package com.tailormade.tailor.fabric.client;

import com.tailormade.tailor.client.ClientRendererSetup;
import com.tailormade.tailor.client.model.MannequinModel;
import com.tailormade.tailor.client.renderer.MannequinRenderer;
import com.tailormade.tailor.registries.ModBlockEntities;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class ClientRenderEvents {
    private static Set<ResourceLocation> MOB_TARGETS = new HashSet<>();

    public static void register() {
        EntityRendererRegistry.register(ModBlockEntities.MANNEQUIN, MannequinRenderer::new);
        EntityModelLayerRegistry.register(MannequinModel.LAYER_LOCATION, MannequinModel::createBodyLayer);

        // MCA Reborn MOD の村人にも服を着せたい
        for (String mobId : ClientRendererSetup.MOD_LIVING_ENTITY_IDS) {
            MOB_TARGETS.add(ResourceLocation.fromNamespaceAndPath("mca", mobId));
        }
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerRenderer playerRenderer) {
                ClientRendererSetup.applyPlayerRendererLayers(playerRenderer);
            }
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            if (MOB_TARGETS.contains(id)) {
                ClientRendererSetup.applyMobLayers(entityRenderer);
            }
        });

        TailorClientCleanup.register();
    }
}