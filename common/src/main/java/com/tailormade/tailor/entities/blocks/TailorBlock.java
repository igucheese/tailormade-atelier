package com.tailormade.tailor.entities.blocks;

import com.tailormade.tailor.client.menu.ManagerMenu;
import com.tailormade.tailor.client.menu.TailorMenu;
import com.tailormade.tailor.entities.blockentities.TailorBlockEntity;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TailorBlock extends FacingBlock implements EntityBlock {
    public TailorBlock(BlockBehaviour.Properties properties) {
        super(
                properties.mapColor(MapColor.COLOR_YELLOW)
                    .strength(1.0f)
                    .noOcclusion()
        );
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof TailorBlockEntity be) {
            MenuProvider containerProvider = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("gui.tailormade.designer.title");
                }

                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
                    return new TailorMenu(windowId, playerInventory, be);
                }
            };
            MenuRegistry.openExtendedMenu((ServerPlayer) player, containerProvider, buf -> buf.writeBlockPos(pos));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TailorBlockEntity(blockPos, blockState);
    }
}
