package com.tailormade.tailor.utils.files;

import com.tailormade.tailor.data.records.DesignDataRecord;
import com.tailormade.tailor.network.payloads.ExportDesignDataToClientPayload;
import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public final class DesignDataExportSender {
    private DesignDataExportSender() {}

    public static void sendToClient(ServerPlayer player, DesignDataRecord record) {
        CompoundTag nbt = DesignDataNbtExporter.toNbt(record);
        String fileName = sanitizeFileName(record.name()) + " (" + record.type() + ")";
        NetworkManager.sendToPlayer(player, new ExportDesignDataToClientPayload(fileName, nbt));
    }

    private static String sanitizeFileName(String rawName) {
        String sanitized = rawName.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
        return sanitized.isEmpty() ? "unnamed" : sanitized;
    }
}
