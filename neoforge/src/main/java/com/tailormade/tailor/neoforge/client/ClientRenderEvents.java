package com.tailormade.tailor.neoforge.client;

import com.tailormade.tailor.client.ClientRendererSetup;
import com.tailormade.tailor.client.model.MannequinModel;
import com.tailormade.tailor.client.renderer.*;
import com.tailormade.tailor.registries.ModBlockEntities;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

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
    }

    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModBlockEntities.MANNEQUIN.get(), MannequinRenderer::new);
    }

    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MannequinModel.LAYER_LOCATION, MannequinModel::createBodyLayer);
    }
}