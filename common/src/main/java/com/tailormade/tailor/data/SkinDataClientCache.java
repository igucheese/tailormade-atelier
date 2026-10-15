package com.tailormade.tailor.data;

import com.tailormade.tailor.data.records.PixelData;
import com.tailormade.tailor.data.records.SkinDataRecord;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SkinDataClientCache {
    private static final Map<UUID, SkinDataRecord> SkinDataCache = new ConcurrentHashMap<UUID, SkinDataRecord>();
    public static void updateCache(UUID uuid, SkinDataRecord skin){
        SkinDataCache.put(uuid, skin);
    }
    public static SkinDataRecord get(UUID id) {
        return SkinDataCache.get(id);
    }
    public static Collection<SkinDataRecord> index() {
        return SkinDataCache.values();
    }
    public static void removeCache(UUID id) { SkinDataCache.remove(id); }
}
