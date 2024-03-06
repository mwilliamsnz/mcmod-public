package abyssal.items.spells;

import abyssal.spells.Spell;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BasicStaff extends SpellStaff {

    private final Spell spell;

    public BasicStaff(Properties properties, float abilityPower, UUID staffUUID, Spell spell) {
        super(properties, abilityPower, staffUUID);
        this.spell = spell;
    }

    @Override
    public Spell defaultSpell(ItemStack staff) {
        return spell;
    }
}
