package abyssal.init;

import abyssal.Main;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid = Main.MOD_ID)
public class ModBrewing {
    @SubscribeEvent
    public static void registerPotions(RegisterBrewingRecipesEvent event) {
        ItemStack elixir = ModItems.ELIXIR.get().getDefaultInstance();

//        event.getBuilder().addMix(Potions.STRONG_REGENERATION, ModItems.PHILO_STONE.get(), elixir);
        event.getBuilder().addMix(Potions.AWKWARD, ModItems.FOUR_LEAF_CLOVER.get(), Potions.LUCK);
    }
}
