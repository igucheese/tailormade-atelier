package com.tailormade.tailor.network.payloads;

import com.tailormade.tailor.data.DesignData;
import com.tailormade.tailor.data.records.DesignDataRecord;
import com.tailormade.tailor.utils.ChatService;
import com.tailormade.tailor.utils.files.DesignDataExportSender;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import static com.tailormade.tailor.Tailormade.MODID;

public record ExportDesignDataStartPayload(UUID id) implements CustomPacketPayload {
    public static final Type<ExportDesignDataStartPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "export_design_data_start"));

    public static final StreamCodec<ByteBuf, ExportDesignDataStartPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, ExportDesignDataStartPayload::id,
            ExportDesignDataStartPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ExportDesignDataStartPayload packet, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> handleOnMainThread(packet, ctx));
    }

    private static void handleOnMainThread(ExportDesignDataStartPayload packet, NetworkManager.PacketContext ctx) {
        if (!(ctx.getPlayer() instanceof ServerPlayer player)) return;
        ServerLevel level = player.serverLevel();

        // デザインレコード取得
        DesignDataRecord record = DesignData.get(level).get(packet.id());
        if (record == null) return;
        // ロックされていて、かつ player = org.player ではない場合は弾く
        if (record.isLocked() && !player.getUUID().equals(record.userId())) {
            ChatService.showMessage(player, Component.translatable("message.tailormade.pattern_manager.guarded"), true);
            return;
        }

        // エクスポート開始
        DesignDataExportSender.sendToClient(player, record);
    }
}
