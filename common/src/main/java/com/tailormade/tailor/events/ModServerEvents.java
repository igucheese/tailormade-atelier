package com.tailormade.tailor.events;

import com.tailormade.tailor.Tailormade;
import com.tailormade.tailor.registries.ModDataComponents;
import com.tailormade.tailor.registries.ModItems;
import com.tailormade.tailor.utils.CatalogService;
import com.tailormade.tailor.utils.InitialTemplateLoader;
import com.tailormade.tailor.utils.files.TemplateLoader;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.LecternBlockEntity;

import java.nio.file.Path;
import java.util.UUID;

import static com.tailormade.tailor.events.SyncDataLayers.*;

public class ModServerEvents {
    public static void register() {
        PlayerEvent.PLAYER_JOIN.register(ModServerEvents::onPlayerLogin);
        LifecycleEvent.SERVER_STARTING.register(ModServerEvents::onServerStarting);
        InteractionEvent.RIGHT_CLICK_BLOCK.register(ModServerEvents::onRightClickBlock);

        // CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, selection) -> {
        //     Command.register(dispatcher);
        // });
    }

    private static void onPlayerLogin(ServerPlayer player) {
        Tailormade.LOGGER.info("[SYNC] Starting data sync...");

        addMeIfAbsent(player);
        syncSkinLayer(player);
        syncUnderwearLayer(player);
        syncDesignData(player);
        syncGlobalPlayers(player);
        syncDesignTemplates(player);
        syncCatalogs(player);
    }

    private static void onServerStarting(MinecraftServer server) {
        Path templatesDir = server.getServerDirectory().toAbsolutePath()
                .resolve("config").resolve("tailormade").resolve("templates");
        InitialTemplateLoader.installDefaults(templatesDir);
        int loaded = TemplateLoader.loadAll(templatesDir);
        Tailormade.LOGGER.info("Loaded {} design templates!", loaded);
    }

    private static EventResult onRightClickBlock(
            Player player,
            InteractionHand hand,
            BlockPos pos,
            net.minecraft.core.Direction face
    ) {
        var level = player.level();

        if (level.getBlockEntity(pos) instanceof LecternBlockEntity lectern) {
            System.out.println("[CHECK][onRightClickBlock] this is lectern!");
            ItemStack book = lectern.getBook();

            if (book.is(ModItems.CATALOG_BOOK.get())) {
                System.out.println("[CHECK][onRightClickBlock] this is catalog!");
                String catalogIdStr = book.get(ModDataComponents.CATALOG_ID.get());
                if (catalogIdStr == null || catalogIdStr.isBlank()) return EventResult.pass();
                UUID catalogId = UUID.fromString(catalogIdStr);

                if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                    System.out.println("[CHECK][onRightClickBlock] let's open!");
                    CatalogService.openClientScreen(serverPlayer, catalogId, true, pos);
                }
                return EventResult.interruptFalse();
            }
        }
        return EventResult.pass();
    }
}