package com.tailormade.tailor.registries;

import com.tailormade.tailor.entities.blocks.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.tailormade.tailor.Tailormade.MODID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MODID, Registries.BLOCK);

    public static final RegistrySupplier<DesignerBlock> DESIGNER = BLOCKS.register("designer",
            () -> new DesignerBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<TailorBlock> TAILOR = BLOCKS.register("tailor",
            () -> new TailorBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<BleachingCounterBlock> BLEACHING_COUNTER = BLOCKS.register("bleaching_counter",
            () -> new BleachingCounterBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<ManagerBlock> MANAGER = BLOCKS.register("tailor_manager",
            () -> new ManagerBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<PowderRoomBlock> POWDER_ROOM = BLOCKS.register("powder_room",
            () -> new PowderRoomBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<WardrobeBlock> WARDROBE = BLOCKS.register("wardrobe",
            () -> new WardrobeBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<MannequinBlock> MANNEQUIN = BLOCKS.register("mannequin",
            () -> new MannequinBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<ShowcaseBlock> SHOWCASE = BLOCKS.register("showcase",
            () -> new ShowcaseBlock(BlockBehaviour.Properties.of()));
}