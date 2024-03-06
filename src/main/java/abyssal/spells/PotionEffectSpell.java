package abyssal.spells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PotionEffectSpell extends Spell {

    private final MobEffect mobEffect;
    private final int baseDuration;
    private final double apScaling;
    protected PotionEffectSpell(ResourceLocation key, SpellFuelQuantity cost, MobEffect effect, int baseDuration, double apScaling) {
        super(key, cost);
        this.mobEffect = effect;
        this.baseDuration = baseDuration;
        this.apScaling = apScaling;
    }

    @Override
    public InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        player.addEffect(new MobEffectInstance(mobEffect, baseDuration + (int)(ap * apScaling), 0, false, false));
        return InteractionResultHolder.success(staff);
    }
}
