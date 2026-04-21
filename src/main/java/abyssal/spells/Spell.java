package abyssal.spells;

import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class Spell {

    public final Identifier key;

    public final SpellFuelQuantity baseCost;

    protected Spell(Identifier key) {
        this(key, SpellFuelQuantity.NONE);
    }

    protected Spell(Identifier key, SpellFuelQuantity baseCost) {
        this.key = key;
        this.baseCost = baseCost;
    }

    public abstract InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap);

    public InteractionResult altBookCast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        return cast(level, player, staff, book, ap);
    }


}
