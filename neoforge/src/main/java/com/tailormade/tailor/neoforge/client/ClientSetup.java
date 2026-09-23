package com.tailormade.tailor.neoforge.client;

import com.tailormade.tailor.client.ClientScreenRegistry;
import com.tailormade.tailor.client.screen.BleachScreen;
import com.tailormade.tailor.client.screen.DesignerScreen;
import com.tailormade.tailor.client.screen.ManagerScreen;
import com.tailormade.tailor.client.screen.TailorScreen;
import com.tailormade.tailor.registries.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.function.Supplier;

import static com.tailormade.tailor.Tailormade.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        ClientScreenRegistry.entries().forEach(entry ->
                entry.registerTo(new ClientScreenRegistry.RegisterTarget() {
                    @Override
                    public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
                    void register(Supplier<? extends MenuType<M>> type, ClientScreenRegistry.ScreenFactory<M, S> factory) {
                        registerInternal(event, type, factory);
                    }
                })
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerInternal(
            RegisterMenuScreensEvent event,
            Supplier<? extends MenuType> type,
            ClientScreenRegistry.ScreenFactory factory
    ) {
        MenuType rawType = (MenuType) type.get();
        MenuScreens.ScreenConstructor rawConstructor = (menu, inv, title) -> factory.create(menu, inv, title);
        event.register(rawType, rawConstructor);
    }
}
