package com.tailormade.tailor.fabric.client;

import com.tailormade.tailor.client.ClientScreenRegistry;
import com.tailormade.tailor.registries.ModNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class TailormadeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModNetworking.registerClientReceivers();
        ClientRenderEvents.register();
        ClientScreenRegistry.entries().forEach(entry ->
                entry.registerTo(new ClientScreenRegistry.RegisterTarget() {
                    @Override
                    public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
                    void register(Supplier<? extends MenuType<M>> type, ClientScreenRegistry.ScreenFactory<M, S> factory) {
                        registerInternal(type, factory);
                    }
                })
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerInternal(
            Supplier<? extends MenuType> type,
            ClientScreenRegistry.ScreenFactory factory
    ) {
        MenuType rawType = (MenuType) type.get();
        MenuScreens.ScreenConstructor rawConstructor = (menu, inv, title) -> factory.create(menu, inv, title);
        MenuScreens.register(rawType, rawConstructor);
    }
}