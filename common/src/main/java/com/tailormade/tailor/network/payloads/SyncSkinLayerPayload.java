package com.tailormade.tailor.network.payloads;

import com.tailormade.tailor.Tailormade;
import com.tailormade.tailor.data.SkinDataClientCache;
import com.tailormade.tailor.data.records.PixelData;
import com.tailormade.tailor.data.records.SkinDataRecord;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static com.tailormade.tailor.Tailormade.MODID;

public record SyncSkinLayerPayload (UUID uuid, SkinDataRecord skin) implements CustomPacketPayload {
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(MODID, "sync_skin_layer");
    public static final Type<SyncSkinLayerPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSkinLayerPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeUUID(p.uuid());
                        SkinDataRecord.STREAM_CODEC.encode(buf, p.skin());
                    },
                    buf -> {
                        UUID uuid = buf.readUUID();
                        SkinDataRecord skin = SkinDataRecord.STREAM_CODEC.decode(buf);
                        return new SyncSkinLayerPayload(uuid, skin);
                    }
            );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SyncSkinLayerPayload packet, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> {
            SkinDataClientCache.updateCache(packet.uuid(), packet.skin());
            Tailormade.LOGGER.info("[CACHE_SYNC_SKIN] Sync completed: " + packet.uuid());
        });
    }
}