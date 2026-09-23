package com.tailormade.tailor.network.payloads;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.tailormade.tailor.Tailormade.MODID;

public record RetrieveCatalogPayload(BlockPos pos) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MODID, "retrieve_catalog");

    public static final Type<RetrieveCatalogPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, RetrieveCatalogPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RetrieveCatalogPayload::pos,
            RetrieveCatalogPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RetrieveCatalogPayload packet, NetworkManager.PacketContext ctx) {
        ctx.queue(() -> {
            if (!(ctx.getPlayer() instanceof ServerPlayer player)) return;
            ServerLevel level = player.serverLevel();

            BlockEntity be = level.getBlockEntity(packet.pos());
            if (be instanceof LecternBlockEntity lbe) {
                ItemStack bookStack = lbe.getBook().copy();
                if (!bookStack.isEmpty()) {
                    BlockState state = level.getBlockState(packet.pos());
                    LecternBlock.resetBookState(player, level, packet.pos(), state, false);
                    lbe.clearContent();

                    if (!player.addItem(bookStack)) {
                        player.drop(bookStack, false);
                    }
                }
            }
        });
    }
}