package com.tailormade.tailor.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tailormade.tailor.utils.MannequinStylePreviewHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class TailorArmorRenderLayer<T extends LivingEntity, M extends HumanoidModel<T>>
        extends RenderLayer<T, M> {

    private static ResourceLocation previewOverride = null;

    public static void setPreviewOverride(ResourceLocation tex) { previewOverride = tex; }
    public static ResourceLocation getPreviewOverride() { return previewOverride; }
    public static void clearPreviewOverride() { previewOverride = null; }

    public TailorArmorRenderLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       T entity, float limbSwing, float limbSwingAmount, float partialTick,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        ResourceLocation texture = resolveTexture(entity);
        if (texture == null) return;
        if (MannequinStylePreviewHelper.isHideArmor()) return;

        getParentModel().renderToBuffer(
                poseStack,
                bufferSource.getBuffer(RenderType.entityTranslucentCull(texture)),
                packedLight,
                OverlayTexture.NO_OVERLAY
        );
    }

    private ResourceLocation resolveTexture(T entity) {
        if (previewOverride != null) return previewOverride;
        if (!TailorTextureCompositor.hasAnyTailorGear(entity) && !TailorTextureCompositor.isCached(entity.getUUID())) return null;
        return TailorTextureCompositor.getOrCreate(entity.getUUID()).getOrUpdate(entity);
    }
}