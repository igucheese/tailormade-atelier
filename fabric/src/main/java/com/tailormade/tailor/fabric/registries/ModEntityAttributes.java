package com.tailormade.tailor.fabric.registries;

import com.tailormade.tailor.registries.ModBlockEntities;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.LivingEntity;

public class ModEntityAttributes {
    public static void init() {
        FabricDefaultAttributeRegistry.register(
                ModBlockEntities.MANNEQUIN.get(),
                LivingEntity.createLivingAttributes()
        );
    }
}