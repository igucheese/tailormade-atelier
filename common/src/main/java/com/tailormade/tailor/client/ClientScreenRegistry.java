package com.tailormade.tailor.client;

import com.tailormade.tailor.client.screen.BleachScreen;
import com.tailormade.tailor.client.screen.DesignerScreen;
import com.tailormade.tailor.client.screen.ManagerScreen;
import com.tailormade.tailor.client.screen.TailorScreen;
import com.tailormade.tailor.registries.ModMenuTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class ClientScreenRegistry {
    private ClientScreenRegistry() {}

    @FunctionalInterface
    public interface ScreenFactory<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> {
        S create(M menu, Inventory inventory, Component title);
    }

    public interface Entry {
        void registerTo(RegisterTarget target);
    }

    public interface RegisterTarget {
        <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
        void register(Supplier<? extends MenuType<M>> type, ScreenFactory<M, S> factory);
    }

    private static final List<Entry> ENTRIES = new ArrayList<>();

    static {
        add(ModMenuTypes.DESIGNER_MENU, DesignerScreen::new);
        add(ModMenuTypes.TAILOR_MENU, TailorScreen::new);
        add(ModMenuTypes.BLEACH_MENU, BleachScreen::new);
        add(ModMenuTypes.MANAGER_MENU, ManagerScreen::new);
    }

    private static <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void add(Supplier<? extends MenuType<M>> type, ScreenFactory<M, S> factory) {
        ENTRIES.add(target -> target.register(type, factory));
    }

    public static List<Entry> entries() {
        return ENTRIES;
    }
}