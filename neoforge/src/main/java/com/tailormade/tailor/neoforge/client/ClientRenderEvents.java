package com.tailormade.tailor.neoforge.client;

import com.tailormade.tailor.client.ClientRendererSetup;
import com.tailormade.tailor.client.model.MannequinModel;
import com.tailormade.tailor.client.renderer.*;
import com.tailormade.tailor.registries.ModBlockEntities;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.List;

public class ClientRenderEvents {
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientRenderEvents::onAddLayers);
        modEventBus.addListener(ClientRenderEvents::onRegisterLayerDefinitions);
        modEventBus.addListener(ClientRenderEvents::onRegisterRenderers);
    }

    private static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer playerRenderer) {
                ClientRendererSetup.applyPlayerRendererLayers(playerRenderer);
            }
        }

        // MCA Reborn MOD の村人にも服を着せたい
        for (String path : ClientRendererSetup.MOD_LIVING_ENTITY_IDS) {
            BuiltInRegistries.ENTITY_TYPE.getOptional(
                    ResourceLocation.fromNamespaceAndPath("mca", path))
                    .ifPresent(type -> {
                        if (event.getRenderer(type) instanceof LivingEntityRenderer<?, ?> r) {
                            ClientRendererSetup.applyMobLayers(r);
                        }
                    }
            );
        }
    }

    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModBlockEntities.MANNEQUIN.get(), MannequinRenderer::new);
    }

    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MannequinModel.LAYER_LOCATION, MannequinModel::createBodyLayer);
    }
}