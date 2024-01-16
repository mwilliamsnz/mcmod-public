package abyssal.init;

import abyssal.Main;
import abyssal.client.HarmoniserScreen;
import abyssal.client.LapidaryScreen;
import abyssal.inventory.AlchemyMenu;
import abyssal.inventory.LapidaryMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =  DeferredRegister.create(ForgeRegistries.MENU_TYPES, Main.MOD_ID);

    public static final RegistryObject<MenuType<LapidaryMenu>> LAPIDARY = MENUS.register("lapidary", () -> new MenuType<>(LapidaryMenu::new, FeatureFlagSet.of()));
    public static final RegistryObject<MenuType<AlchemyMenu>> HARMONISER = MENUS.register("harmoniser", () -> new MenuType<>(AlchemyMenu::new, FeatureFlagSet.of()));

    public static void registerMenuScreens() {
        Main.LOGGER.info("Registering screen");
        MenuScreens.register(LAPIDARY.get(), LapidaryScreen::new);
        MenuScreens.register(HARMONISER.get(), HarmoniserScreen::new);
        Main.LOGGER.info("Done registering screen");
    }
}
