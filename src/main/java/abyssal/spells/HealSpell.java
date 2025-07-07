package abyssal.spells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HealSpell extends Spell {

    private final int baseHeal;
    private final double apScaling;
    protected HealSpell(ResourceLocation key, SpellFuelQuantity cost, int baseDuration, double apScaling) {
        super(key, cost);
        this.baseHeal = baseDuration;
        this.apScaling = apScaling;
    }

    @Override
    public InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        player.heal((float) (baseHeal + apScaling * ap));
        return InteractionResultHolder.success(staff);
    }
}
