package abyssal.entity;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;

public class TreeSpider extends Spider {

    public TreeSpider(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
    }
    public static AttributeSupplier.Builder createTreeSpider() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 8.0D);
    }

    protected float getStandingEyeHeight(Pose p_32265_, EntityDimensions p_32266_) {
        return 0.45F;
    }

    protected int calculateFallDamage(float f1, float f2) {
        return super.calculateFallDamage(f1, f2) - 5;
    }

    public boolean doHurtTarget(Entity e) {
        if (super.doHurtTarget(e)) {
            if (e instanceof LivingEntity) {
                int durationSeconds = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    durationSeconds = 3;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    durationSeconds = 6;
                }

                if (durationSeconds > 0) {
                    ((LivingEntity) e).addEffect(new MobEffectInstance(MobEffects.WITHER, durationSeconds * 20, 0), this);
                }
            }

            return true;
        } else {
            return false;
        }
    }

}