package abyssal.init;

import abyssal.Main;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Main.MOD_ID)
public class ModBrewing {
    @SubscribeEvent
    public static void registerPotions(FMLCommonSetupEvent event) {
        event.enqueueWork(()-> {
            ItemStack awkward = Items.POTION.getDefaultInstance();
            PotionUtils.setPotion(awkward, Potions.AWKWARD);

            ItemStack luck = Items.POTION.getDefaultInstance();
            PotionUtils.setPotion(luck, Potions.LUCK);
            ItemStack luckSplash = Items.SPLASH_POTION.getDefaultInstance();
            PotionUtils.setPotion(luck, Potions.LUCK);
            ItemStack luckLinger = Items.LINGERING_POTION.getDefaultInstance();
            PotionUtils.setPotion(luck, Potions.LUCK);

            ItemStack regen = Items.POTION.getDefaultInstance();
            PotionUtils.setPotion(regen, Potions.STRONG_REGENERATION);
            ItemStack elixir = ModItems.ELIXIR.get().getDefaultInstance();

            BrewingRecipeRegistry.addRecipe(Ingredient.of(regen), Ingredient.of(ModItems.PHILO_STONE.get()), elixir);

            BrewingRecipeRegistry.addRecipe(Ingredient.of(awkward), Ingredient.of(ModItems.FOUR_LEAF_CLOVER.get()), luck);
            BrewingRecipeRegistry.addRecipe(Ingredient.of(luck), Ingredient.of(Items.GUNPOWDER), luckSplash);
            BrewingRecipeRegistry.addRecipe(Ingredient.of(luck), Ingredient.of(Items.DRAGON_BREATH), luckLinger);

        });
    }
}
