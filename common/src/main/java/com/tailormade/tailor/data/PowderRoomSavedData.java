package com.tailormade.tailor.data;

import com.tailormade.tailor.data.records.PixelData;
import com.tailormade.tailor.data.records.SkinDataRecord;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PowderRoomSavedData extends SavedData {
    private static final String NAME = "tailormade_powder_room";
    private final Map<UUID, SkinDataRecord> skinLayers = new HashMap<>();
    public static PowderRoomSavedData get(ServerLevel level) {
        return level.getServer()
                .overworld()
                .getDataStorage()
                .computeIfAbsent(
                        new SavedData.Factory<>(
                                PowderRoomSavedData::new,
                                PowderRoomSavedData::load,
                                DataFixTypes.LEVEL
                        ),
                        NAME
                );
    }

    public SkinDataRecord setSkinLayer(UUID uuid, PixelData data, boolean isVisible) {
        SkinDataRecord skin = new SkinDataRecord(uuid, data, isVisible);
        skinLayers.put(uuid, skin);
        setDirty();
        return skin;
    }

    public PixelData getSkinLayer(UUID uuid) {
        SkinDataRecord data = skinLayers.get(uuid);
        return data != null ? data.pixelData() : null;
    }
    public Collection<PixelData> index() {
        return skinLayers.values().stream().map(SkinDataRecord::pixelData).toList();
    }
    public Collection<SkinDataRecord> getAll() {
        return skinLayers.values();
    }

    public void removeSkinLayer(UUID uuid) {
        skinLayers.remove(uuid);
        setDirty();
    }

    public PowderRoomSavedData() {}
    public static PowderRoomSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        System.out.println("[Tailormade][SKIN_DATA] loading data from file...");
        PowderRoomSavedData data = new PowderRoomSavedData();
        ListTag listTag = tag.getList("PowderRoom", Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            SkinDataRecord skinData = SkinDataRecord.load(listTag.getCompound(i));
            data.skinLayers.put(skinData.uuid(), skinData);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        System.out.println("[Tailormade][SKIN_DATA] Saving design data to file...");
        ListTag listTag = new ListTag();
        for (SkinDataRecord info : skinLayers.values()) {
            listTag.add(info.save());
        }
        tag.put("PowderRoom", listTag);
        System.out.println("[Tailormade][SKIN_DATA] saving skins; targets: " + this.skinLayers.size());
        return tag;
    }
}
