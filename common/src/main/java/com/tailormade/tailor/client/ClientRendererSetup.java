package com.tailormade.tailor.client;

import com.tailormade.tailor.Tailormade;
import com.tailormade.tailor.client.renderer.SkinLayerRenderLayer;
import com.tailormade.tailor.client.renderer.TailorArmorRenderLayer;
import com.tailormade.tailor.client.renderer.TailorHumanoidArmorLayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.model.ModelManager;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class ClientRendererSetup {
    private ClientRendererSetup() {}

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void applyPlayerRendererLayers(PlayerRenderer playerRenderer) {
        List<RenderLayer<AbstractClientPlayer, ?>> layers =
                (List<RenderLayer<AbstractClientPlayer, ?>>) (List<?>)
                        ((LivingEntityRenderer) playerRenderer).layers;

        for (int i = 0; i < layers.size(); i++) {
            if (!(layers.get(i) instanceof HumanoidArmorLayer<?, ?, ?> original)) continue;

            HumanoidModel innerModel = ((HumanoidArmorLayer) original).innerModel;
            HumanoidModel outerModel = ((HumanoidArmorLayer) original).outerModel;
            ModelManager modelManager = Minecraft.getInstance().getModelManager();

            layers.set(i, new TailorHumanoidArmorLayer(playerRenderer, innerModel, outerModel, modelManager));
            break;
        }

        playerRenderer.addLayer(new SkinLayerRenderLayer(playerRenderer));
        playerRenderer.addLayer(new TailorArmorRenderLayer(playerRenderer));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void applyMobLayers(LivingEntityRenderer mobRenderer) {
        if (!(mobRenderer.getModel() instanceof HumanoidModel)) return;

        List<RenderLayer<?, ?>> layers = (List) mobRenderer.layers;
        ModelManager modelManager = Minecraft.getInstance().getModelManager();

        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i) instanceof HumanoidArmorLayer<?, ?, ?> original) {
                HumanoidModel inner = ((HumanoidArmorLayer) original).innerModel;
                HumanoidModel outer = ((HumanoidArmorLayer) original).outerModel;
                layers.set(i, new TailorHumanoidArmorLayer(mobRenderer, inner, outer, modelManager));
                break;
            }
        }
        mobRenderer.addLayer(new TailorArmorRenderLayer(mobRenderer));
    }

    public static List<String> MOD_LIVING_ENTITY_IDS = List.of("male_villager", "female_villager");
}