package com.tailormade.tailor.registries;

import com.tailormade.tailor.data.constants.PatternType;
import com.tailormade.tailor.entities.items.CatalogBookItem;
import com.tailormade.tailor.entities.items.PatternItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import static com.tailormade.tailor.Tailormade.MODID;
import static com.tailormade.tailor.registries.ModBlocks.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MODID, Registries.ITEM);

    public static final RegistrySupplier<Item> PATTERN = ITEMS.register("pattern", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<PatternItem> PATTERN_DEFAULT = ITEMS.register("pattern_default", () -> new PatternItem(null, new Item.Properties()));
    public static final RegistrySupplier<PatternItem> PATTERN_HELMET = ITEMS.register("pattern_helmet", () -> new PatternItem(PatternType.HEAD, new Item.Properties()));
    public static final RegistrySupplier<PatternItem> PATTERN_CHESTPLATE = ITEMS.register("pattern_chestplate", () -> new PatternItem(PatternType.CHEST, new Item.Properties()));
    public static final RegistrySupplier<PatternItem> PATTERN_LEGGINGS = ITEMS.register("pattern_leggings", () -> new PatternItem(PatternType.LEGS, new Item.Properties()));
    public static final RegistrySupplier<PatternItem> PATTERN_BOOTS = ITEMS.register("pattern_boots", () -> new PatternItem(PatternType.FEET, new Item.Properties()));

    public static final RegistrySupplier<BlockItem> DESIGNER_ITEM = ITEMS.register("designer", () -> new BlockItem(DESIGNER.get(), new Item.Properties()));
    public static final RegistrySupplier<BlockItem> TAILOR_ITEM = ITEMS.register("tailor", () -> new BlockItem(TAILOR.get(), new Item.Properties()));
    public static final RegistrySupplier<BlockItem> BLEACHING_COUNTER_ITEM = ITEMS.register("bleaching_counter", () -> new BlockItem(BLEACHING_COUNTER.get(), new Item.Properties()));
    public static final RegistrySupplier<BlockItem> POWDER_ROOM_ITEM = ITEMS.register("powder_room", () -> new BlockItem(POWDER_ROOM.get(), new Item.Properties()));
    public static final RegistrySupplier<BlockItem> WARDROBE_ITEM = ITEMS.register("wardrobe", () -> new BlockItem(WARDROBE.get(), new Item.Properties()));
    public static final RegistrySupplier<BlockItem> MANAGER_ITEM = ITEMS.register("tailor_manager", () -> new BlockItem(MANAGER.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> BLEACH = ITEMS.register("bleach", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<BlockItem> MANNEQUIN_ITEM = ITEMS.register("mannequin", () -> new BlockItem(MANNEQUIN.get(), new Item.Properties()));
    public static final RegistrySupplier<BlockItem> SHOWCASE_ITEM = ITEMS.register("showcase", () -> new BlockItem(SHOWCASE.get(), new Item.Properties()));
    public static final RegistrySupplier<CatalogBookItem> CATALOG_BOOK = ITEMS.register("catalog_book", () -> new CatalogBookItem(new Item.Properties()));
}