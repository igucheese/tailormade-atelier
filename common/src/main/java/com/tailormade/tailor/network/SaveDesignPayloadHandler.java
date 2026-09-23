package com.tailormade.tailor.network;

import com.tailormade.tailor.Tailormade;
import com.tailormade.tailor.client.menu.DesignerMenu;
import com.tailormade.tailor.data.DesignData;
import com.tailormade.tailor.data.records.DesignDataRecord;
import com.tailormade.tailor.entities.items.PatternItem;
import com.tailormade.tailor.network.payloads.SaveDesignPayload;
import com.tailormade.tailor.network.payloads.SyncDesignPayload;
import com.tailormade.tailor.registries.ModDataComponents;
import com.tailormade.tailor.utils.ChatService;
import com.tailormade.tailor.utils.PatternDataSaver;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class SaveDesignPayloadHandler {
    public static void handle(SaveDesignPayload packet, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> handleOnMainThread(packet, ctx));
    }

    private static void handleOnMainThread(SaveDesignPayload packet, NetworkManager.PacketContext ctx) {
        if (!(ctx.getPlayer() instanceof ServerPlayer player)) return;
        ServerLevel level = player.serverLevel();

        if (!(player.containerMenu instanceof DesignerMenu menu)) {
            Tailormade.LOGGER.warn(
                    "SavePattern: DesignerMenu を開いていません"
            );
            return;
        }

        int slotIndex = packet.slotIndex();
        ItemStack stack = menu.getPatternContainer().getItem(slotIndex);
        if (stack.isEmpty() || !(stack.getItem() instanceof PatternItem patternItem)) {
            Tailormade.LOGGER.warn(
                    "SavePattern: スロット {} に PatternItem がありません",
                    slotIndex
            );
            ChatService.showMessage(player, Component.translatable("message.tailormade.pattern_manager.incompatible"), true);
            return;
        }

        // 名前
        String name = packet.name();

        // 既存データチェック
        if (stack.has(ModDataComponents.PATTERN_ID.get())) {
            String patternId = stack.get(ModDataComponents.PATTERN_ID.get());
            DesignDataRecord savedDesign = DesignData.get(level).get(UUID.fromString(patternId));
            if (savedDesign != null) {
                if (name.isBlank()) {
                    name = savedDesign.name();
                }
                if (savedDesign.isLocked() && !savedDesign.userId().equals(player.getUUID())) {
                    Tailormade.LOGGER.warn("SavePattern: ロックされた型紙を編集しようとしています");
                    ChatService.showMessage(player, Component.translatable("message.tailormade.pattern_manager.guarded"), true);
                    return;
                }
            }
        }

        boolean isValidSize = PatternDataSaver.isValidSize(stack, packet.pixelData(), patternItem);
        if (!isValidSize) { return; }

        // 保存実行
        DesignDataRecord newDesign = PatternDataSaver.saveDeign(level, stack, packet.pixelData(), packet.layers(), patternItem, player.getUUID(), name, packet.isSlim());
        System.out.println("[CHECK][NEW DESIGN] " + newDesign);
        if (newDesign != null) {
            player.containerMenu.broadcastChanges();
            ChatService.showMessage(player, Component.translatable("message.tailormade.pattern_manager.design_saved"), true);
            NetworkManager.sendToPlayers(level.players(), new SyncDesignPayload(newDesign.uuid(), newDesign));
        } else {
            ChatService.showMessage(player, Component.literal("だめでしたあ"), false);
        }
    }
}
