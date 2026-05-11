package abyssal.init;

import abyssal.Main;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

import java.util.concurrent.atomic.AtomicBoolean;

@EventBusSubscriber(modid = Main.MOD_ID)
public class ModBrewing {
    @SubscribeEvent
    public static void registerPotions(RegisterBrewingRecipesEvent event) {

        event.getBuilder().addRecipe(new IBrewingRecipe() {
            @Override
            public boolean isInput(ItemStack input) {
                PotionContents comp = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                AtomicBoolean isRegen = new AtomicBoolean(false);
                comp.potion().ifPresent(ph -> {
                    ph.value().getEffects().forEach(mobEffectInstance -> {
                        if(mobEffectInstance.is(MobEffects.REGENERATION) && mobEffectInstance.getAmplifier() >= 1) {
                            isRegen.set(true);
                        }
                    });
                });

                return isRegen.get();
            }

            @Override
            public boolean isIngredient(ItemStack ingredient) {
                return ingredient.is(ModItems.PHILO_STONE);
            }

            @Override
            public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                if(isInput(input) && isIngredient(ingredient)) {
                    return ModItems.ELIXIR.get().getDefaultInstance();
                }
                return ItemStack.EMPTY;
            }
        });
        event.getBuilder().addMix(Potions.AWKWARD, ModItems.FOUR_LEAF_CLOVER.get(), Potions.LUCK);
    }
}
