package abyssal.entity;

import abyssal.PowderExplosion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class PowderBarrelEntity extends PrimedTnt {

    private final float damageFactor;
    private final float knockFactor;
    private final float size;
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

    protected void explode() {
        this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 1.8F, Level.ExplosionInteraction.TNT);
        PowderExplosion explosion = new PowderExplosion(this.level(), this, null, null, this.getX(), this.getY(0.0625D), this.getZ(), size, false, Explosion.BlockInteraction.KEEP, damageFactor, knockFactor);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }
}
