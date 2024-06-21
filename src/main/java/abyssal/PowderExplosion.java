package abyssal;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PowderExplosion extends Explosion {

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
    private final Level level;
    private final boolean fire;
    private final Explosion.BlockInteraction blockInteraction;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity source;
    private final float radius;
    private final float damageFactor;
    private final float knockFactor;
    private final DamageSource damageSource;
    private final ExplosionDamageCalculator damageCalculator;
    private final ObjectArrayList<BlockPos> toBlow = new ObjectArrayList<>();
    private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();
    private final Vec3 position;
    private final RandomSource random = RandomSource.create();




    public PowderExplosion(Level world, @Nullable Entity exploder, @Nullable DamageSource source, @Nullable ExplosionDamageCalculator context, double x, double y, double z, float size, boolean causesFire, BlockInteraction mode, float damageFactor, float knockFactor) {
        super(world, exploder, x, y, z, size, causesFire, mode);
        this.level = world;
        this.source = exploder;
        this.radius = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.fire = causesFire;
        this.blockInteraction = mode;
        this.damageSource = source == null ? level.damageSources().explosion(this) : source;

        this.damageCalculator = context == null ? this.getEntityExplosionContext(exploder) : context;
        this.position = new Vec3(this.x, this.y, this.z);
        this.damageFactor = damageFactor;
        this.knockFactor = knockFactor;
    }

    private ExplosionDamageCalculator getEntityExplosionContext(@Nullable Entity entity) {
        return entity == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(entity);
    }

    /**
     * Does the first part of the explosion (destroy blocks, knock entities)
     */
    @Override
    public void explode() {
        this.level.gameEvent(this.source, GameEvent.EXPLODE, BlockPos.containing(this.x, this.y, this.z));
        Set<BlockPos> set = Sets.newHashSet();

        for(int j = 0; j < 16; ++j) {
            for(int k = 0; k < 16; ++k) {
                for(int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double dx = (float)j / 15.0F * 2.0F - 1.0F;
                        double dy = (float)k / 15.0F * 2.0F - 1.0F;
                        double dz = (float)l / 15.0F * 2.0F - 1.0F;
                        double blockDist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        dx = dx / blockDist;
                        dy = dy / blockDist;
                        dz = dz / blockDist;
                        float f = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double xpos = this.x;
                        double ypos = this.y;
                        double zpos = this.z;

                        for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = BlockPos.containing(xpos, ypos, zpos);
                            BlockState blockstate = this.level.getBlockState(blockpos);
                            FluidState fluidstate = this.level.getFluidState(blockpos);
                            if (!this.level.isInWorldBounds(blockpos)) {
                                break;
                            }

                            Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, this.level, blockpos, blockstate, fluidstate);
                            if (optional.isPresent()) {
                                f -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, blockpos, blockstate, f)) {
                                set.add(blockpos);
                            }

                            xpos += dx * (double)0.3F;
                            ypos += dy * (double)0.3F;
                            zpos += dz * (double)0.3F;
                        }
                    }
                }
            }
        }

        this.toBlow.addAll(set);
        float damageRad = this.radius * 2.0F * damageFactor; // Changed from TNT to add factor
        int bbxl = Mth.floor(this.x - (double)damageRad - 1.0D);
        int bbxu = Mth.floor(this.x + (double)damageRad + 1.0D);
        int bbyl = Mth.floor(this.y - (double)damageRad - 1.0D);
        int bbyu = Mth.floor(this.y + (double)damageRad + 1.0D);
        int bbzl = Mth.floor(this.z - (double)damageRad - 1.0D);
        int bbzu = Mth.floor(this.z + (double)damageRad + 1.0D);
        List<Entity> entitiesInBB = this.level.getEntities(this.source, new AABB(bbxl, bbyl, bbzl, bbxu, bbyu, bbzu));
        net.neoforged.neoforge.event.EventHooks.onExplosionDetonate(this.level, this, entitiesInBB, damageRad);
        Vec3 vec3 = new Vec3(this.x, this.y, this.z);

        for(int entityIndex = 0; entityIndex < entitiesInBB.size(); ++entityIndex) {
            Entity entity = entitiesInBB.get(entityIndex);
            if (!entity.ignoreExplosion(this)) {
                double radialProportion = Math.sqrt(entity.distanceToSqr(vec3)) / (double)damageRad;
                if (radialProportion <= 1.0D) {
                    double dx = entity.getX() - this.x;
                    double dy = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
                    double dz = entity.getZ() - this.z;
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (distance != 0.0D) {
                        dx = dx / distance;
                        dy = dy / distance;
                        dz = dz / distance;
                        double coverage = getSeenPercent(vec3, entity);
                        double strength = (1.0D - radialProportion) * coverage;
                        entity.hurt(this.damageSource, (float)((int)((strength * strength + strength) / 2.0D * 7.0D * (double)damageRad + 1.0D)));
                        double knockbackStrength = strength * knockFactor; // Changed from TNT to add factor
                        if (entity instanceof LivingEntity) {
                            knockbackStrength = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, strength);
                        }

                        entity.setDeltaMovement(entity.getDeltaMovement().add(dx * knockbackStrength, dy * knockbackStrength, dz * knockbackStrength));
                        if (entity instanceof Player player) {
                            if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                                this.hitPlayers.put(player, new Vec3(dx * strength, dy * strength, dz * strength));
                            }
                        }
                    }
                }
            }
        }
    }

    // It is necessary to override this despite it being verbatim identical,
    // as overridden explode is what populates toBlow (private)
    @Override
    public void finalizeExplosion(boolean p_46076_) {
        if (this.level.isClientSide) {
            this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean flag = this.interactsWithBlocks();
        if (p_46076_) {
            if (!(this.radius < 2.0F) && flag) {
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            }
        }

        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            boolean flag1 = this.getIndirectSourceEntity() instanceof Player;
            Util.shuffle(this.toBlow, this.level.random);

            for(BlockPos blockpos : this.toBlow) {
                BlockState blockstate = this.level.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (!blockstate.isAir()) {
                    BlockPos blockpos1 = blockpos.immutable();
                    this.level.getProfiler().push("explosion_blocks");
                    if (blockstate.canDropFromExplosion(this.level, blockpos, this)) {
                        Level $$9 = this.level;
                        if ($$9 instanceof ServerLevel) {
                            ServerLevel serverlevel = (ServerLevel)$$9;
                            BlockEntity blockentity = blockstate.hasBlockEntity() ? this.level.getBlockEntity(blockpos) : null;
                            LootParams.Builder lootparams$builder = (new LootParams.Builder(serverlevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                            if (this.blockInteraction == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                                lootparams$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                            }

                            blockstate.spawnAfterBreak(serverlevel, blockpos, ItemStack.EMPTY, flag1);
                            blockstate.getDrops(lootparams$builder).forEach((p_46074_) -> {
                                addBlockDrops(objectarraylist, p_46074_, blockpos1);
                            });
                        }
                    }

                    blockstate.onBlockExploded(this.level, blockpos, this);
                    this.level.getProfiler().pop();
                }
            }

            for(Pair<ItemStack, BlockPos> pair : objectarraylist) {
                Block.popResource(this.level, pair.getSecond(), pair.getFirst());
            }
        }

        if (this.fire) {
            for(BlockPos blockpos2 : this.toBlow) {
                if (this.random.nextInt(3) == 0 && this.level.getBlockState(blockpos2).isAir() && this.level.getBlockState(blockpos2.below()).isSolidRender(this.level, blockpos2.below())) {
                    this.level.setBlockAndUpdate(blockpos2, BaseFireBlock.getState(this.level, blockpos2));
                }
            }
        }
    }

    // See above
    //"@Override"
    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> p_46068_, ItemStack p_46069_, BlockPos p_46070_) {
        int i = p_46068_.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = p_46068_.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.areMergable(itemstack, p_46069_)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, p_46069_, 16);
                p_46068_.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (p_46069_.isEmpty()) {
                    return;
                }
            }
        }

        p_46068_.add(Pair.of(p_46069_, p_46070_));
    }
}
