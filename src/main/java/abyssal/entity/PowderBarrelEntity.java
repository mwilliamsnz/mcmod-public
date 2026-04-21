package abyssal.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;

public class PowderBarrelEntity extends PrimedTnt {

    private float damageFactor;
    private float knockFactor;
    private float size;
    public PowderBarrelEntity(Level worldIn, double x, double y, double z, @Nullable LivingEntity igniter, float size, float damageFactor, float knockFactor) {
        super(worldIn, x, y, z, igniter);
        this.size = size;
        this.damageFactor = damageFactor;
        this.knockFactor = knockFactor;
    }

    public PowderBarrelEntity(EntityType<PowderBarrelEntity> type, Level world, float size, float damageFactor, float knockFactor) {
        super(type, world);
        this.size = size;
        this.damageFactor = damageFactor;
        this.knockFactor = knockFactor;
    }

    public PowderBarrelEntity(EntityType<PowderBarrelEntity> type, Level world) {
        super(type, world);
        this.size = 1;
        this.damageFactor = 1;
        this.knockFactor = 1;
    }

    @Override
    protected void explode() {
        if (this.level() instanceof ServerLevel serverlevel && serverlevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
            this.level()
                    .explode(
                            this,
                            Explosion.getDefaultDamageSource(this.level(), this),
                            new ExplosionDamageCalculator() {
                                @Override
                                public float getKnockbackMultiplier(Entity entity) {
                                    return super.getKnockbackMultiplier(entity) * knockFactor;
                                }

                                @Override
                                public float getEntityDamageAmount(Explosion explosion, Entity entity, float seenPercent) {
                                    return super.getEntityDamageAmount(explosion, entity, seenPercent) * damageFactor;
                                }
                            },
                            this.getX(),
                            this.getY(0.0625),
                            this.getZ(),
                            this.size,
                            false,
                            Level.ExplosionInteraction.TNT
                    );
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("explosion_power", this.size);
        tag.putFloat("knock_factor", this.knockFactor);
        tag.putFloat("damage_factor", this.damageFactor);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        this.size = Mth.clamp(tag.getFloatOr("explosion_power", 4.0F), 0.0F, 128.0F);
        this.knockFactor = Mth.clamp(tag.getFloatOr("knock_factor", 1.0F), 0.0F, 128.0F);
        this.damageFactor = Mth.clamp(tag.getFloatOr("damage_factor", 1.0F), 0.0F, 128.0F);
    }
}
