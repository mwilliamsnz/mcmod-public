package abyssal.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.extensions.IItemExtension;

public class BasicFuelItem extends Item implements IItemExtension {

    private final int burnTicks;
    public BasicFuelItem(Properties properties, int burnTicks) {
        super(properties);
        this.burnTicks = burnTicks;
    }

    @Override
    public int getBurnTime(ItemStack stack, RecipeType<?> recipeType) {
        return burnTicks;
    }
}
