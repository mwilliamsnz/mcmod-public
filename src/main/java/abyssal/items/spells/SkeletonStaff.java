package abyssal.items.spells;

import abyssal.spells.Spell;
import abyssal.spells.Spells;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class SkeletonStaff extends SpellStaff {

    public SkeletonStaff(Properties properties, float abilityPower, UUID staffUUID) {
        super(properties, abilityPower, staffUUID);
    }

    @Override
    public Spell defaultSpell(ItemStack staff) {
        return Spells.SUMMON_SKELETON;
    }
}
