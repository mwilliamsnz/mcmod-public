package abyssal.items.spells;

import abyssal.spells.SpellFuelQuantity;
import abyssal.spells.SpellFuelType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;

public class FuelStorageItem extends Item implements IItemExtension, SpellFuelStorage {

    public FuelStorageItem(Properties props, SpellFuelType fuelType) {
        super(props);
        this.fuelType = fuelType;
    }
    
    public final SpellFuelType fuelType;


    @Override
    public SpellFuelQuantity getSpellFuelQuantity(ItemStack stack) {
        return new SpellFuelQuantity(fuelType, getMaxDamage(stack)-getDamage(stack));
    }

    @Override
    public SpellFuelQuantity changeSpellFuelQuantity(ItemStack stack, int q) {
        setDamage(stack, Mth.clamp(getDamage(stack) - q, 0, getMaxDamage(stack)));
        return getSpellFuelQuantity(stack);
    }
}
