package com.tailormade.tailor;

import com.mojang.logging.LogUtils;
import com.tailormade.tailor.events.ModServerEvents;
import com.tailormade.tailor.registries.ModBlockEntities;
import com.tailormade.tailor.registries.ModDataComponents;
import com.tailormade.tailor.registries.ModMenuTypes;
import com.tailormade.tailor.registries.ModNetworking;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;

import static com.tailormade.tailor.registries.ModBlocks.BLOCKS;
import static com.tailormade.tailor.registries.ModItems.*;

public class Tailormade {
    public static final String MODID = "tailormade";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> TAILORMADE_TAB = CREATIVE_MODE_TABS.register(
            "tailormade",
            () -> CreativeTabRegistry.create(builder -> {
                builder.title(Component.translatable("itemGroup.tailormade"));
                builder.icon(() -> DESIGNER_ITEM.get().getDefaultInstance());
                builder.displayItems((parameters, output) -> {
                    output.accept(PATTERN_HELMET.get());
                    output.accept(PATTERN_CHESTPLATE.get());
                    output.accept(PATTERN_LEGGINGS.get());
                    output.accept(PATTERN_BOOTS.get());
                    output.accept(DESIGNER_ITEM.get());
                    output.accept(TAILOR_ITEM.get());
                    output.accept(MANAGER_ITEM.get());
                    output.accept(BLEACHING_COUNTER_ITEM.get());
                    output.accept(POWDER_ROOM_ITEM.get());
                    output.accept(WARDROBE_ITEM.get());
                    output.accept(SHOWCASE_ITEM.get());
                    output.accept(CATALOG_BOOK.get());
                    output.accept(BLEACH.get());
                    output.accept(MANNEQUIN_ITEM.get());
                });
            })
    );

    public static void init() {
        BLOCKS.register();
        ITEMS.register();
        ModBlockEntities.BLOCK_ENTITIES.register();
        ModBlockEntities.ENTITY_TYPES.register();
        CREATIVE_MODE_TABS.register();
        ModDataComponents.COMPONENTS.register();
        ModMenuTypes.MENUS.register();
        ModNetworking.register();
        ModServerEvents.register();
        LifecycleEvent.SERVER_STARTING.register(server -> LOGGER.info("HELLO from server starting"));
    }
}