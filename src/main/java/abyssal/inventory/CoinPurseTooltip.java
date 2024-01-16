package abyssal.inventory;


import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class CoinPurseTooltip implements TooltipComponent {
    private final NonNullList<ItemStack> items;
    private final int weight;

    public CoinPurseTooltip(NonNullList<ItemStack> stacks, int weight) {
        this.items = stacks;
        this.weight = weight;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public int getWeight() {
        return this.weight;
    }
}