package abyssal.spells;

import net.minecraft.world.item.ItemStack;

public interface ISpellProvider {

    Spell getActiveSpell(ItemStack stack);

    default Spell getSecondarySpell(ItemStack stack) {
        return getActiveSpell(stack);
    }

}
