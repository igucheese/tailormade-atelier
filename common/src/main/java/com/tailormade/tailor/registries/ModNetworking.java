package com.tailormade.tailor.registries;

import com.tailormade.tailor.network.ConfirmBleachPayloadHandler;
import com.tailormade.tailor.network.ConfirmTailorPayloadHandler;
import com.tailormade.tailor.network.SaveDesignPayloadHandler;
import com.tailormade.tailor.network.SaveSkinLayerPayloadHandler;
import com.tailormade.tailor.network.payloads.*;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

public class ModNetworking {
    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                SaveDesignPayload.TYPE, SaveDesignPayload.STREAM_CODEC,
                SaveDesignPayloadHandler::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                ConfirmTailorPayload.TYPE, ConfirmTailorPayload.STREAM_CODEC,
                ConfirmTailorPayloadHandler::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                SaveSkinLayerPayload.TYPE, SaveSkinLayerPayload.STREAM_CODEC,
                SaveSkinLayerPayloadHandler::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                SaveUnderwearPayload.TYPE, SaveUnderwearPayload.STREAM_CODEC,
                SaveUnderwearPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                ConfirmBleachPayload.TYPE, ConfirmBleachPayload.STREAM_CODEC,
                ConfirmBleachPayloadHandler::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                CopyDesignPayload.TYPE, CopyDesignPayload.STREAM_CODEC,
                CopyDesignPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                SavePatternLockPayload.TYPE, SavePatternLockPayload.STREAM_CODEC,
                SavePatternLockPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                RenamePatternPayload.TYPE, RenamePatternPayload.STREAM_CODEC,
                RenamePatternPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                ExportDesignDataStartPayload.TYPE, ExportDesignDataStartPayload.STREAM_CODEC,
                ExportDesignDataStartPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                ExtractDesignPayload.TYPE, ExtractDesignPayload.STREAM_CODEC,
                ExtractDesignPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                ImportDesignPayload.TYPE, ImportDesignPayload.STREAM_CODEC,
                ImportDesignPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                ChangeManagerMenuTabPayload.TYPE, ChangeManagerMenuTabPayload.STREAM_CODEC,
                ChangeManagerMenuTabPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                SaveCatalogPayload.TYPE, SaveCatalogPayload.STREAM_CODEC,
                SaveCatalogPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                PrepareCatalogScreenPayload.TYPE, PrepareCatalogScreenPayload.STREAM_CODEC,
                PrepareCatalogScreenPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                GetCatalogBookPayload.TYPE, GetCatalogBookPayload.STREAM_CODEC,
                GetCatalogBookPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S,
                RetrieveCatalogPayload.TYPE, RetrieveCatalogPayload.STREAM_CODEC,
                RetrieveCatalogPayload::handle);

        if (Platform.getEnvironment() == Env.SERVER) {
            registerServerS2CTypes();
        }
    }

    private static void registerServerS2CTypes() {
        NetworkManager.registerS2CPayloadType(SyncSkinLayerPayload.TYPE, SyncSkinLayerPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(SyncUnderwearPayload.TYPE, SyncUnderwearPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(SyncDesignPayload.TYPE, SyncDesignPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(SyncMannequinPayload.TYPE, SyncMannequinPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(ExportDesignDataToClientPayload.TYPE, ExportDesignDataToClientPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(SyncGlobalPlayerPayload.TYPE, SyncGlobalPlayerPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(SyncDesignTemplatePayload.TYPE, SyncDesignTemplatePayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(SyncCatalogPayload.TYPE, SyncCatalogPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(SyncAllCatalogsPayload.TYPE, SyncAllCatalogsPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(OpenCatalogPayload.TYPE, OpenCatalogPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CloseDesignScreenPayload.TYPE, CloseDesignScreenPayload.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(SyncSkinLayerRemovePayload.TYPE, SyncSkinLayerRemovePayload.STREAM_CODEC);
    }

    public static void registerClientReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                SyncSkinLayerPayload.TYPE, SyncSkinLayerPayload.STREAM_CODEC,
                SyncSkinLayerPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                SyncUnderwearPayload.TYPE, SyncUnderwearPayload.STREAM_CODEC,
                SyncUnderwearPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                SyncDesignPayload.TYPE, SyncDesignPayload.STREAM_CODEC,
                SyncDesignPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                SyncMannequinPayload.TYPE, SyncMannequinPayload.STREAM_CODEC,
                SyncMannequinPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                ExportDesignDataToClientPayload.TYPE, ExportDesignDataToClientPayload.STREAM_CODEC,
                ExportDesignDataToClientPayload::handleClient);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                SyncGlobalPlayerPayload.TYPE, SyncGlobalPlayerPayload.STREAM_CODEC,
                SyncGlobalPlayerPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                SyncDesignTemplatePayload.TYPE, SyncDesignTemplatePayload.STREAM_CODEC,
                SyncDesignTemplatePayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                SyncCatalogPayload.TYPE, SyncCatalogPayload.STREAM_CODEC,
                SyncCatalogPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                SyncAllCatalogsPayload.TYPE, SyncAllCatalogsPayload.STREAM_CODEC,
                SyncAllCatalogsPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                OpenCatalogPayload.TYPE, OpenCatalogPayload.STREAM_CODEC,
                OpenCatalogPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                CloseDesignScreenPayload.TYPE, CloseDesignScreenPayload.STREAM_CODEC,
                CloseDesignScreenPayload::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
                SyncSkinLayerRemovePayload.TYPE, SyncSkinLayerRemovePayload.STREAM_CODEC,
                SyncSkinLayerRemovePayload::handle);
    }
}