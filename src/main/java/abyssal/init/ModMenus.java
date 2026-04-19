package abyssal.init;

import abyssal.Main;
import abyssal.client.HarmoniserScreen;
import abyssal.client.LapidaryScreen;
import abyssal.inventory.AlchemyMenu;
import abyssal.inventory.LapidaryMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =  DeferredRegister.create(BuiltInRegistries.MENU, Main.MOD_ID);

    public static final Supplier<MenuType<LapidaryMenu>> LAPIDARY = MENUS.register("lapidary", () -> new MenuType<>(LapidaryMenu::new, FeatureFlagSet.of()));
    public static final Supplier<MenuType<AlchemyMenu>> HARMONISER = MENUS.register("harmoniser", () -> new MenuType<>(AlchemyMenu::new, FeatureFlagSet.of()));

    // screens registered in ClientEventSubscriber
}
