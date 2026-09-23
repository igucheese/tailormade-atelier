package com.tailormade.tailor.network.payloads;

import com.tailormade.tailor.data.CatalogSavedData;
import com.tailormade.tailor.data.records.CatalogData;
import com.tailormade.tailor.utils.CatalogService;
import com.tailormade.tailor.utils.SoundService;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;

import java.util.List;
import java.util.UUID;

import static com.tailormade.tailor.Tailormade.MODID;

public record SaveCatalogPayload(UUID id, String name, List<UUID> patterns) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MODID, "save_catalog");

    public static final Type<SaveCatalogPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, SaveCatalogPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, SaveCatalogPayload::id,
            ByteBufCodecs.STRING_UTF8, SaveCatalogPayload::name,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), SaveCatalogPayload::patterns,
            SaveCatalogPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SaveCatalogPayload packet, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> {
            if (!(ctx.getPlayer() instanceof ServerPlayer player)) return;
            CatalogData newCatalog = CatalogService.newRecord(packet.id(), player.getUUID(), packet.name(), packet.patterns());

            // 本体に保存＆クライアントにキャッシュ
            CatalogSavedData.get(player.serverLevel()).saveCatalog(newCatalog);
            NetworkManager.sendToPlayers(player.serverLevel().players(), new SyncCatalogPayload(newCatalog));
            SoundService.playSound(player, SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F);
        });
    }
}