package com.tailormade.tailor.network;

import com.tailormade.tailor.data.PowderRoomSavedData;
import com.tailormade.tailor.data.records.PixelData;
import com.tailormade.tailor.data.records.SkinDataRecord;
import com.tailormade.tailor.network.payloads.SaveSkinLayerPayload;
import com.tailormade.tailor.network.payloads.SyncSkinLayerPayload;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class SaveSkinLayerPayloadHandler {
    public static void handle(SaveSkinLayerPayload packet, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> {
            if (!(ctx.getPlayer() instanceof ServerPlayer player)) return;
            if (packet.pixels().length != 64 * 64) return;

            ServerLevel overworld = player.getServer().overworld();
            SkinDataRecord skin = PowderRoomSavedData.get(overworld).setSkinLayer(player.getUUID(), new PixelData(packet.pixels()), packet.isVisible());

            SyncSkinLayerPayload syncPacket = new SyncSkinLayerPayload(player.getUUID(), skin);
            NetworkManager.sendToPlayers(overworld.players(), syncPacket);
        });
    }
}
