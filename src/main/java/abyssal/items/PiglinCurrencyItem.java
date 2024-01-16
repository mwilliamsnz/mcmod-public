package abyssal.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PiglinCurrencyItem extends Item {
    public PiglinCurrencyItem(Properties props) {
        super(props);
    }

    @Override
    public boolean isPiglinCurrency(ItemStack stack) {
        return true;
    }
}
