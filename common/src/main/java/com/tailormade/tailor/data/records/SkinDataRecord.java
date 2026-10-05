package com.tailormade.tailor.data.records;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record SkinDataRecord(
        UUID uuid,
        PixelData pixelData,
        boolean isVisible
) {
    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("uuid", uuid);
        nbt.put("pixelData", new IntArrayTag(pixelData.getPixels()));
        nbt.putBoolean("isVisible", isVisible);
        return nbt;
    }

    public static SkinDataRecord load(CompoundTag nbt) {
        UUID uuid = nbt.getUUID("uuid");
        int[] pixels = nbt.getIntArray("pixelData");
        PixelData pixelData = new PixelData(pixels);
        boolean isVisible = !nbt.contains("isVisible") || nbt.getBoolean("isVisible");

        return new SkinDataRecord(uuid, pixelData, isVisible);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SkinDataRecord> STREAM_CODEC = StreamCodec.of(
            (buf, info) -> {
                buf.writeUUID(info.uuid());
                PixelData.STREAM_CODEC.encode(buf, info.pixelData());
                buf.writeBoolean(info.isVisible());
            },
            buf -> {
                return new SkinDataRecord(
                        buf.readUUID(),
                        PixelData.STREAM_CODEC.decode(buf),
                        buf.readBoolean()
                );
            }
    );
}
