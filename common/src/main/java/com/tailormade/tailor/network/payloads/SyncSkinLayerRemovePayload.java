package com.tailormade.tailor.network.payloads;

import com.tailormade.tailor.Tailormade;
import com.tailormade.tailor.data.SkinDataClientCache;
import com.tailormade.tailor.data.records.SkinDataRecord;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static com.tailormade.tailor.Tailormade.MODID;

public record SyncSkinLayerRemovePayload(UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(MODID, "sync_skin_layer_removal");
    public static final Type<SyncSkinLayerRemovePayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSkinLayerRemovePayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeUUID(p.uuid());
                    },
                    buf -> {
                        UUID uuid = buf.readUUID();
                        return new SyncSkinLayerRemovePayload(uuid);
                    }
            );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SyncSkinLayerRemovePayload packet, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> {
            SkinDataClientCache.removeCache(packet.uuid());
            Tailormade.LOGGER.info("[CACHE_SYNC_SKIN] Cache removed: " + packet.uuid());
        });
    }
}