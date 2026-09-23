package com.tailormade.tailor.network.payloads;

import com.tailormade.tailor.data.WardrobeSavedData;
import com.tailormade.tailor.data.records.UnderwearSetting;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import static com.tailormade.tailor.Tailormade.MODID;

public record SaveUnderwearPayload(UnderwearSetting setting) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(MODID, "save_underwear");
    public static final Type<SaveUnderwearPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, SaveUnderwearPayload> STREAM_CODEC = StreamCodec.composite(
            UnderwearSetting.STREAM_CODEC, SaveUnderwearPayload::setting,
            SaveUnderwearPayload::new
    );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SaveUnderwearPayload packet, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> {
            if (!(ctx.getPlayer() instanceof ServerPlayer player)) return;

            ServerLevel overworld = player.getServer().overworld();
            WardrobeSavedData.get(overworld).setSetting(player.getUUID(), packet.setting());

            // 全員に配信
            SyncUnderwearPayload syncPacket = new SyncUnderwearPayload(player.getUUID(), packet.setting());
            NetworkManager.sendToPlayers(overworld.players(), syncPacket);
        });
    }
}