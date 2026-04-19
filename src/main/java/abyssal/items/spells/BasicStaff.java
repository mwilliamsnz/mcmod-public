package abyssal.items.spells;

import abyssal.spells.Spell;
import net.minecraft.world.item.ItemStack;

public class BasicStaff extends SpellStaff {

    private final Spell spell;

    public BasicStaff(Properties properties, Spell spell) {
        super(properties);
        this.spell = spell;
    }

    @Override
    public Spell defaultSpell(ItemStack staff) {
        return spell;
    }
}
