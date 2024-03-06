package abyssal.spells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AreaPotionEffectSpell extends Spell {


    private final MobEffect mobEffect;
    private final int baseDuration;
    private final double apScaling;
    private final boolean affectsCaster;
    private final double baseRadius;
    private final double apScalingRadius;
    protected AreaPotionEffectSpell(ResourceLocation key, SpellFuelQuantity cost, MobEffect effect, int baseDuration, double apScaling, double baseRadius, double apScalingRadius, boolean affectsCaster) {
        super(key, cost);

        this.mobEffect = effect;
        this.baseDuration = baseDuration;
        this.apScaling = apScaling;
        this.affectsCaster = affectsCaster;
        this.baseRadius = baseRadius;
        this.apScalingRadius = apScalingRadius;
    }

    @Override
    public InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        final double radius = Math.max(0.0001, baseRadius + ap * apScalingRadius); // Cap?
        double rsq = radius*radius;
        Vec3 pp = player.getEyePosition();
        AABB aabb = AABB.ofSize(pp, 2*radius, 2*radius, 2*radius);
        level.getEntities(affectsCaster ? null : player, aabb).forEach(((entity -> {
            if(entity instanceof LivingEntity e) {
                if(entity.distanceToSqr(player) < rsq) {
                    e.addEffect(new MobEffectInstance(mobEffect, baseDuration + (int)(ap*apScaling)));
                }
            }
        })));

        return InteractionResultHolder.success(staff);
    }
}
