package abyssal.spells;

import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HealSpell extends Spell {

    private final int baseHeal;
    private final double apScaling;
    protected HealSpell(Identifier key, SpellFuelQuantity cost, int baseDuration, double apScaling) {
        super(key, cost);
        this.baseHeal = baseDuration;
        this.apScaling = apScaling;
    }

    @Override
    public InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        player.heal((float) (baseHeal + apScaling * ap));
        return InteractionResult.SUCCESS;
    }
}
