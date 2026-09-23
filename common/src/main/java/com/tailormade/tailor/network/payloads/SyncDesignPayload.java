package com.tailormade.tailor.network.payloads;

import com.tailormade.tailor.Tailormade;
import com.tailormade.tailor.data.DesignDataClientCache;
import com.tailormade.tailor.data.records.DesignDataRecord;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static com.tailormade.tailor.Tailormade.MODID;

public record SyncDesignPayload(UUID uuid, DesignDataRecord design) implements CustomPacketPayload {
    public static final Type<SyncDesignPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "design_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncDesignPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, SyncDesignPayload::uuid,
            DesignDataRecord.STREAM_CODEC, SyncDesignPayload::design,
            SyncDesignPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncDesignPayload payload, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> {
            DesignDataClientCache.updateCache(payload.uuid(), payload.design());
            Tailormade.LOGGER.info("[CACHE_SYNC_DESIGN] Sync completed: " + payload.uuid());
        });
    }
}