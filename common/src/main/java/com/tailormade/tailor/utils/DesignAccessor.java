package com.tailormade.tailor.utils;

import com.tailormade.tailor.data.DesignDataClientCache;
import com.tailormade.tailor.data.records.DesignDataRecord;
import com.tailormade.tailor.data.records.PixelData;

import java.util.UUID;

public class DesignAccessor {
    public static DesignDataRecord getDesignDataFromId(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        UUID uuid = UUID.fromString(id);
        return DesignDataClientCache.get(uuid);
    }

    public static PixelData getPixelDataFromId(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        DesignDataRecord designData = getDesignDataFromId(id);
        if (designData == null) {
            return null;
        }
        return designData.pixelData();
    }
}
