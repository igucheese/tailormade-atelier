package com.tailormade.tailor.registries;

import com.tailormade.tailor.entities.blockentities.MannequinEntity;
import com.tailormade.tailor.entities.blockentities.ShowcaseBlockEntity;
import com.tailormade.tailor.entities.blockentities.TailorBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static com.tailormade.tailor.Tailormade.MODID;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(MODID, Registries.BLOCK_ENTITY_TYPE);

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(MODID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<TailorBlockEntity>> TAILOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("tailor_block_entity", () ->
                    BlockEntityType.Builder.of(
                            TailorBlockEntity::new,
                            ModBlocks.TAILOR.get()
                    ).build(null)
            );

    public static final RegistrySupplier<EntityType<MannequinEntity>> MANNEQUIN =
            ENTITY_TYPES.register("mannequin", () ->
                    EntityType.Builder.<MannequinEntity>of(MannequinEntity::new, MobCategory.MISC)
                            .sized(0.6F, 1.975F)
                            .build("mannequin")
            );

    public static final RegistrySupplier<BlockEntityType<ShowcaseBlockEntity>> SHOWCASE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("showcase_block_entity", () ->
                    BlockEntityType.Builder.of(
                            ShowcaseBlockEntity::new,
                            ModBlocks.SHOWCASE.get()
                    ).build(null)
            );
}