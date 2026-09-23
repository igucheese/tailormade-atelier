package com.tailormade.tailor.registries;

import com.tailormade.tailor.client.menu.BleachMenu;
import com.tailormade.tailor.client.menu.DesignerMenu;
import com.tailormade.tailor.client.menu.ManagerMenu;
import com.tailormade.tailor.client.menu.TailorMenu;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import static com.tailormade.tailor.Tailormade.MODID;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(MODID, Registries.MENU);

    public static final RegistrySupplier<MenuType<TailorMenu>> TAILOR_MENU =
            MENUS.register("tailor_menu", () -> MenuRegistry.ofExtended(TailorMenu::new));

    public static final RegistrySupplier<MenuType<DesignerMenu>> DESIGNER_MENU =
            MENUS.register("designer_menu", () -> new MenuType<>(DesignerMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final RegistrySupplier<MenuType<BleachMenu>> BLEACH_MENU =
            MENUS.register("bleach_menu", () -> new MenuType<>(BleachMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final RegistrySupplier<MenuType<ManagerMenu>> MANAGER_MENU =
            MENUS.register("manager_menu", () -> new MenuType<>(ManagerMenu::new, FeatureFlags.DEFAULT_FLAGS));
}