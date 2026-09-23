package com.tailormade.tailor.network.payloads;

import com.tailormade.tailor.Tailormade;
import com.tailormade.tailor.data.CatalogDataClientCache;
import com.tailormade.tailor.data.records.CatalogData;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.tailormade.tailor.Tailormade.MODID;

public record SyncAllCatalogsPayload(List<CatalogData> catalogs) implements CustomPacketPayload {
    public static final Type<SyncAllCatalogsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "sync_all_catalog"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAllCatalogsPayload> STREAM_CODEC = StreamCodec.composite(
            CatalogData.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)), SyncAllCatalogsPayload::catalogs,
            SyncAllCatalogsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncAllCatalogsPayload payload, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> {
            CatalogDataClientCache.bulkUpdate(payload.catalogs());
            Tailormade.LOGGER.info("[CACHE_SYNC_CATALOG] Sync completed for " + payload.catalogs().size() + " items.");
        });
    }
}