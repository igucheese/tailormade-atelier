package com.tailormade.tailor.network.payloads;

import com.tailormade.tailor.client.ClientHooks;
import com.tailormade.tailor.client.menu.BleachMenu;
import com.tailormade.tailor.registries.ModDataComponents;
import com.tailormade.tailor.registries.ModItems;
import com.tailormade.tailor.utils.ChatService;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import static com.tailormade.tailor.Tailormade.MODID;

public record CloseDesignScreenPayload() implements CustomPacketPayload {
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(MODID, "close_design_screen");

    public static final Type<CloseDesignScreenPayload> TYPE =
            new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, CloseDesignScreenPayload> STREAM_CODEC =
            StreamCodec.of(
                    CloseDesignScreenPayload::encode,
                    CloseDesignScreenPayload::decode
            );

    private static void encode(FriendlyByteBuf buf, CloseDesignScreenPayload packet) {}

    private static CloseDesignScreenPayload decode(FriendlyByteBuf buf) {
        return new CloseDesignScreenPayload();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CloseDesignScreenPayload packet, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> {
            ClientHooks.closeDesignScreen();
        });
    }
}