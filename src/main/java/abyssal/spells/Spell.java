package abyssal.spells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class Spell {

    public final ResourceLocation key;

    public final SpellFuelQuantity baseCost;

    protected Spell(ResourceLocation key) {
        this(key, SpellFuelQuantity.NONE);
    }

    protected Spell(ResourceLocation key, SpellFuelQuantity baseCost) {
        this.key = key;
        this.baseCost = baseCost;
    }


    public abstract InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap);

    public InteractionResultHolder<ItemStack> altBookCast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        return cast(level, player, staff, book, ap);
    }


}
