package abyssal.items.spells;

import abyssal.spells.SpellFuelQuantity;
import net.minecraft.world.item.ItemStack;

public interface SpellFuelStorage {

    SpellFuelQuantity getSpellFuelQuantity(ItemStack stack);

    SpellFuelQuantity changeSpellFuelQuantity(ItemStack stack, int q);

}
