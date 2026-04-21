package abyssal.spells;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PotionEffectSpell extends Spell {

    private final Holder<MobEffect> mobEffect;
    private final int baseDuration;
    private final double apScaling;
    protected PotionEffectSpell(Identifier key, SpellFuelQuantity cost, Holder<MobEffect> effect, int baseDuration, double apScaling) {
        super(key, cost);
        this.mobEffect = effect;
        this.baseDuration = baseDuration;
        this.apScaling = apScaling;
    }

    @Override
    public InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        if (mobEffect.value().isInstantenous() && level instanceof ServerLevel serverLevel) {
            mobEffect.value().applyInstantenousEffect(serverLevel, player, player, player, 1,1.0);
        }
        player.addEffect(new MobEffectInstance(mobEffect, baseDuration + (int)(ap * apScaling), 0, false, false));
        return InteractionResult.SUCCESS;
    }
}
