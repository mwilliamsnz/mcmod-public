package abyssal.entity;

import abyssal.blocks.blockentities.WaspPortBlockEntity;
import abyssal.init.ModBlockEntityTypes;
import abyssal.init.ModBlocks;
import abyssal.init.ModPOIs;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Wasp extends PathfinderMob implements FlyingAnimal, NeutralMob {
    public static final float FLAP_DEGREES_PER_TICK = 120.32113F;
    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Wasp.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Long> DATA_ANGER_END_TIME = SynchedEntityData.defineId(Wasp.class, EntityDataSerializers.LONG);
    private static final int FLAG_ROLL = 2;
    private static final int FLAG_HAS_STUNG = 4;
    private static final int FLAG_HAS_NECTAR = 8;
    private static final int TICKS_BEFORE_GOING_TO_KNOWN_FLOWER = 600;
    private static final int TICKS_WITHOUT_NECTAR_BEFORE_GOING_HOME = 3600; // 180s
    private static final int MIN_ATTACK_DIST = 4;
    private static final int MAX_CROPS_GROWABLE = 10;
    private static final int POISON_SECONDS_NORMAL = 10;
    private static final int POISON_SECONDS_HARD = 18;
    private static final int TOO_FAR_DISTANCE = 48;
    private static final int HIVE_CLOSE_ENOUGH_DISTANCE = 2;
    private static final int RESTRICTED_WANDER_DISTANCE_REDUCTION = 24;
    private static final int DEFAULT_WANDER_DISTANCE_REDUCTION = 16;
    private static final int PATHFIND_TO_HIVE_WHEN_CLOSER_THAN = 16;
    private static final int HIVE_SEARCH_DISTANCE = 20;
    public static final String TAG_CROPS_GROWN_SINCE_POLLINATION = "CropsGrownSincePollination";
    public static final String TAG_CANNOT_ENTER_HIVE_TICKS = "CannotEnterHiveTicks";
    public static final String TAG_TICKS_SINCE_POLLINATION = "TicksSincePollination";
    public static final String TAG_HAS_NECTAR = "HasNectar";
    public static final String TAG_FLOWER_POS = "flower_pos";
    public static final String TAG_HIVE_POS = "hive_pos";
    public static final boolean DEFAULT_HAS_NECTAR = false;
    private static final int DEFAULT_TICKS_SINCE_POLLINATION = 0;
    private static final int DEFAULT_CANNOT_ENTER_HIVE_TICKS = 0;
    private static final int DEFAULT_CROPS_GROWN_SINCE_POLLINATION = 0;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private @Nullable EntityReference<LivingEntity> persistentAngerTarget;
    private float rollAmount;
    private float rollAmountO;
    private int ticksWithoutNectarSinceExitingHive = DEFAULT_TICKS_SINCE_POLLINATION;
    private int stayOutOfHiveCountdown = DEFAULT_CANNOT_ENTER_HIVE_TICKS;
    private int numCropsGrownSincePollination = DEFAULT_CROPS_GROWN_SINCE_POLLINATION;
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_HIVE = 200;
    private int remainingCooldownBeforeLocatingNewHive;
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_FLOWER = 200;
    private static final int MIN_FIND_FLOWER_RETRY_COOLDOWN = 20;
    private static final int MAX_FIND_FLOWER_RETRY_COOLDOWN = 60;
    private int remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
    private @Nullable BlockPos savedFlowerPos;
    private @Nullable BlockPos hivePos;
    private WaspHarvestGoal waspHarvestGoal;
    private WaspGoToPortGoal goToPortGoal;
    private WaspGoToKnownFlowerGoal goToKnownFlowerGoal;
    private int underWaterTicks;

    public static final int MAX_HP = 11;
    public static final int DAMAGE = 9;

    public Wasp(EntityType<? extends Wasp> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new WaspLookControl(this);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createWasp() {
        return  Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HP)
                .add(Attributes.FLYING_SPEED, 0.9F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, DAMAGE)
                .add(Attributes.TEMPT_RANGE, 3.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_FLAGS_ID, (byte)0);
        entityData.define(DATA_ANGER_END_TIME, -1L);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new WaspAttackGoal(this, 1.4F, true));
        this.goalSelector.addGoal(1, new WaspEnterHiveGoal());
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, i -> i.is(Tags.Items.DUSTS_GLOWSTONE), false));
        this.goalSelector.addGoal(3, new ValidateHiveGoal());
        this.goalSelector.addGoal(3, new ValidateFlowerGoal());
        this.waspHarvestGoal = new WaspHarvestGoal();
        this.goalSelector.addGoal(4, this.waspHarvestGoal);
        this.goalSelector.addGoal(5, new WaspLocatePortGoal());
        this.goToPortGoal = new WaspGoToPortGoal();
        this.goalSelector.addGoal(5, this.goToPortGoal);
        this.goToKnownFlowerGoal = new WaspGoToKnownFlowerGoal();
        this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
        this.goalSelector.addGoal(7, new WaspGrowCropGoal());
        this.goalSelector.addGoal(8, new WaspWanderGoal());
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.targetSelector.addGoal(1, new WaspHurtByOtherGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new WaspBecomeAngryTargetGoal(this));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.storeNullable(TAG_HIVE_POS, BlockPos.CODEC, this.hivePos);
        output.storeNullable(TAG_FLOWER_POS, BlockPos.CODEC, this.savedFlowerPos);
        output.putBoolean(TAG_HAS_NECTAR, this.hasNectar());
        output.putInt(TAG_TICKS_SINCE_POLLINATION, this.ticksWithoutNectarSinceExitingHive);
        output.putInt(TAG_CANNOT_ENTER_HIVE_TICKS, this.stayOutOfHiveCountdown);
        output.putInt(TAG_CROPS_GROWN_SINCE_POLLINATION, this.numCropsGrownSincePollination);
        this.addPersistentAngerSaveData(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setHasNectar(input.getBooleanOr(TAG_HAS_NECTAR, false));
        this.ticksWithoutNectarSinceExitingHive = input.getIntOr(TAG_TICKS_SINCE_POLLINATION, 0);
        this.stayOutOfHiveCountdown = input.getIntOr(TAG_CANNOT_ENTER_HIVE_TICKS, 0);
        this.numCropsGrownSincePollination = input.getIntOr(TAG_CROPS_GROWN_SINCE_POLLINATION, 0);
        this.hivePos = input.read(TAG_HIVE_POS, BlockPos.CODEC).orElse(null);
        this.savedFlowerPos = input.read(TAG_FLOWER_POS, BlockPos.CODEC).orElse(null);
        this.readPersistentAngerSaveData(this.level(), input);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        DamageSource damageSource = this.damageSources().sting(this);
        boolean wasHurt = target.hurtServer(level, damageSource, (int)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (wasHurt) {
            EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
            if (target instanceof LivingEntity livingTarget) {
                livingTarget.setStingerCount(livingTarget.getStingerCount() + 1);
                int poisonTime = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    poisonTime = POISON_SECONDS_NORMAL;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    poisonTime = POISON_SECONDS_HARD;
                }

                if (poisonTime > 0) {
                    // TODO change to healcut
                    livingTarget.addEffect(new MobEffectInstance(MobEffects.POISON, poisonTime * 20, 0), this);
                }
            }

            this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
        }

        return wasHurt;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
            for (int i = 0; i < this.random.nextInt(2) + 1; i++) {
                this.spawnFluidParticle(
                        this.level(), this.getX() - 0.3F, this.getX() + 0.3F, this.getZ() - 0.3F, this.getZ() + 0.3F, this.getY(0.5), ParticleTypes.FALLING_NECTAR
                );
            }
        }

        this.updateRollAmount();
    }

    private void spawnFluidParticle(Level level, double x1, double x2, double z1, double z2, double y, ParticleOptions dripParticle) {
        level.addParticle(dripParticle, Mth.lerp(level.getRandom().nextDouble(), x1, x2), y, Mth.lerp(level.getRandom().nextDouble(), z1, z2), 0.0, 0.0, 0.0);
    }

    private void pathfindRandomlyTowards(BlockPos targetPos) {
        Vec3 targetVec = Vec3.atBottomCenterOf(targetPos);
        int yAdjust = 0;
        BlockPos waspPos = this.blockPosition();
        int yDelta = (int)targetVec.y - waspPos.getY();
        if (yDelta > 2) {
            yAdjust = 4;
        } else if (yDelta < -2) {
            yAdjust = -4;
        }

        int xzDist = 6;
        int yDist = 8;
        int dist = waspPos.distManhattan(targetPos);
        if (dist < 15) {
            xzDist = dist / 2;
            yDist = dist / 2;
        }

        Vec3 nextPosTowards = AirRandomPos.getPosTowards(this, xzDist, yDist, yAdjust, targetVec, (float) (Math.PI / 10));
        if (nextPosTowards != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(nextPosTowards.x, nextPosTowards.y, nextPosTowards.z, 1.0);
        }
    }

    public @Nullable BlockPos getSavedFlowerPos() {
        return this.savedFlowerPos;
    }

    public boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    public void setSavedFlowerPos(BlockPos savedFlowerPos) {
        this.savedFlowerPos = savedFlowerPos;
    }

    @VisibleForDebug
    public int getTravellingTicks() {
        return Math.max(this.goToPortGoal.travellingTicks, this.goToKnownFlowerGoal.travellingTicks);
    }

    @VisibleForDebug
    public List<BlockPos> getBlacklistedHives() {
        return this.goToPortGoal.blacklistedTargets;
    }

    private boolean isTiredOfLookingForNectar() {
        return this.ticksWithoutNectarSinceExitingHive > TICKS_WITHOUT_NECTAR_BEFORE_GOING_HOME;
    }

    private void dropHive() {
        level().getBlockEntity(hivePos, ModBlockEntityTypes.WASP_PORT.get()).ifPresent(be -> {
            be.notifyLossOfWasp();
        });
        this.hivePos = null;
        this.remainingCooldownBeforeLocatingNewHive = COOLDOWN_BEFORE_LOCATING_NEW_HIVE;
    }

    private void dropFlower() {
        this.savedFlowerPos = null;
        this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, MIN_FIND_FLOWER_RETRY_COOLDOWN, MAX_FIND_FLOWER_RETRY_COOLDOWN);
    }

    private boolean wantsToEnterHive() {
        if (this.stayOutOfHiveCountdown <= 0 && !this.waspHarvestGoal.isPollinating() && this.getTarget() == null) {
            return this.hasNectar() || this.isTiredOfLookingForNectar();
        } else {
            return false;
        }
    }

    public void setStayOutOfHiveCountdown(int ticks) {
        this.stayOutOfHiveCountdown = ticks;
    }

    public float getRollAmount(float a) {
        return Mth.lerp(a, this.rollAmountO, this.rollAmount);
    }

    private void updateRollAmount() {
        this.rollAmountO = this.rollAmount;
        if (this.isRolling()) {
            this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
        } else {
            this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
        }
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        if (this.isInWater()) {
            this.underWaterTicks++;
        } else {
            this.underWaterTicks = 0;
        }

        if (this.underWaterTicks > 20) {
            this.hurtServer(level, this.damageSources().drown(), 3.0F);
        }

        if (!this.hasNectar()) {
            this.ticksWithoutNectarSinceExitingHive++;
        }

        this.updatePersistentAnger(level, false);
    }

    public void resetTicksWithoutNectarSinceExitingHive() {
        this.ticksWithoutNectarSinceExitingHive = DEFAULT_TICKS_SINCE_POLLINATION;
    }


    @Override
    public long getPersistentAngerEndTime() {
        return this.entityData.get(DATA_ANGER_END_TIME);
    }

    @Override
    public void setPersistentAngerEndTime(long endTime) {
        this.entityData.set(DATA_ANGER_END_TIME, endTime);
    }


    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }


    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public boolean isAngryAtAllPlayers(ServerLevel level) {
        return true; // TODO sometimes passive, e.g. if sending food home?
    }

    private boolean doesPortHaveSpace(BlockPos hivePos) {
        BlockEntity blockEntity = this.level().getBlockEntity(hivePos);
        return blockEntity instanceof WaspPortBlockEntity && !((WaspPortBlockEntity) blockEntity).isFull();
    }

    @VisibleForDebug
    public boolean hasHive() {
        return this.hivePos != null;
    }

    @VisibleForDebug
    public @Nullable BlockPos getHivePos() {
        return this.hivePos;
    }

    @VisibleForDebug
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    private int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }

    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = DEFAULT_CROPS_GROWN_SINCE_POLLINATION;
    }

    private void incrementNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination++;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            if (this.stayOutOfHiveCountdown > 0) {
                this.stayOutOfHiveCountdown--;
            }

            if (this.remainingCooldownBeforeLocatingNewHive > 0) {
                this.remainingCooldownBeforeLocatingNewHive--;
            }

            if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
                this.remainingCooldownBeforeLocatingNewFlower--;
            }

            boolean shouldRoll = this.isAngry() && this.getTarget() != null && this.getTarget().distanceToSqr(this) < 4.0;
            this.setRolling(shouldRoll);
            if (this.tickCount % 20 == 0 && !this.isHiveValid()) {
                this.hivePos = null;
            }
        }
    }

    private @Nullable WaspPortBlockEntity getWaspPortBlockEntity() {
        if (this.hivePos == null) {
            return null;
        } else {
            if (this.isTooFarAway(this.hivePos)) return null;
            return this.level().getBlockEntity(this.hivePos) instanceof WaspPortBlockEntity hive ? hive : null;
        }
    }

    private boolean isHiveValid() {
        return this.getWaspPortBlockEntity() != null;
    }

    public boolean hasNectar() {
        return this.getFlag(FLAG_HAS_NECTAR);
    }

    private void setHasNectar(boolean hasNectar) {
        if (hasNectar) {
            this.resetTicksWithoutNectarSinceExitingHive();
        }

        this.setFlag(FLAG_HAS_NECTAR, hasNectar);
    }

    private boolean isRolling() {
        return this.getFlag(FLAG_ROLL);
    }

    private void setRolling(boolean rolling) {
        this.setFlag(FLAG_ROLL, rolling);
    }

    private boolean isTooFarAway(BlockPos targetPos) {
        return !this.closerThan(targetPos, TOO_FAR_DISTANCE);
    }

    private void setFlag(int flag, boolean value) {
        if (value) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | flag));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~flag));
        }
    }

    private boolean getFlag(int flag) {
        return (this.entityData.get(DATA_FLAGS_ID) & flag) != 0;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level) {
            {
                Objects.requireNonNull(Wasp.this);
            }

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }

            @Override
            public void tick() {
                if (!Wasp.this.waspHarvestGoal.isPollinating()) {
                    super.tick();
                }
            }
        };
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(false);
        flyingPathNavigation.setRequiredPathLength(TOO_FAR_DISTANCE);
        return flyingPathNavigation;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BEE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % TICKS_PER_FLAP == 0;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    public void dropOffNectar() {
        this.setHasNectar(false);
        this.resetNumCropsGrownSincePollination();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.isInvulnerableTo(level, source)) {
            return false;
        } else {
            this.waspHarvestGoal.stopPollinating();
            return super.hurtServer(level, source, damage);
        }
    }

    private void jumpInLiquidInternal() {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.01, 0.0));
    }

    @Override
    public void jumpInFluid(FluidType type) {
        this.jumpInLiquidInternal();
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
    }

    private boolean closerThan(BlockPos targetPos, int distance) {
        return targetPos.closerThan(this.blockPosition(), distance);
    }

    public void setHivePos(BlockPos hivePos) {
        this.hivePos = hivePos;
    }

    public static boolean attractsWasps(BlockState state) {
        return state.is(Blocks.GLOWSTONE);
    }

   private abstract class BaseWaspGoal extends Goal {
        private BaseWaspGoal() {
            super();
        }

        public abstract boolean canWaspUse();

        public abstract boolean canWaspContinueToUse();

        @Override
        public boolean canUse() {
            return this.canWaspUse() && !Wasp.this.isAngry();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canWaspContinueToUse() && !Wasp.this.isAngry();
        }
    }

    private class WaspAttackGoal extends MeleeAttackGoal {
        WaspAttackGoal(PathfinderMob mob, double speedModifier, boolean trackTarget) {
            super(mob, speedModifier, trackTarget);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && Wasp.this.isAngry();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && Wasp.this.isAngry();
        }
    }

    private static class WaspBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
        WaspBecomeAngryTargetGoal(Wasp wasp) {
            super(wasp, Player.class, 10, true, false, wasp::isAngryAt);
        }

        @Override
        public boolean canUse() {
            return this.waspCanTarget() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            boolean waspCanTarget = this.waspCanTarget();
            if (waspCanTarget && this.mob.getTarget() != null) {
                return super.canContinueToUse();
            } else {
                this.targetMob = null;
                return false;
            }
        }

        private boolean waspCanTarget() {
            Wasp wasp = (Wasp)this.mob;
            return wasp.isAngry();
        }
    }

    private class WaspEnterHiveGoal extends BaseWaspGoal {
        private WaspEnterHiveGoal() {
            super();
        }

        @Override
        public boolean canWaspUse() {
            if (Wasp.this.hivePos != null && Wasp.this.wantsToEnterHive() 
                    && Wasp.this.hivePos.closerToCenterThan(Wasp.this.position(), HIVE_CLOSE_ENOUGH_DISTANCE)) {
                WaspPortBlockEntity waspPortBlockEntity = Wasp.this.getWaspPortBlockEntity();
                if (waspPortBlockEntity != null) {
                    if (!waspPortBlockEntity.isFull()) {
                        return true;
                    }

                    Wasp.this.hivePos = null;
                }
            }

            return false;
        }

        @Override
        public boolean canWaspContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            WaspPortBlockEntity waspPortBlockEntity = Wasp.this.getWaspPortBlockEntity();
            if (waspPortBlockEntity != null) {
                waspPortBlockEntity.addOccupant(Wasp.this);
            }
        }
    }

    @VisibleForDebug
    public class WaspGoToPortGoal extends BaseWaspGoal {
        public static final int MAX_TRAVELLING_TICKS = 2400;
        private int travellingTicks;
        private static final int MAX_BLACKLISTED_TARGETS = 3;
        private final List<BlockPos> blacklistedTargets;
        private @Nullable Path lastPath;
        private static final int TICKS_BEFORE_HIVE_DROP = 60;
        private int ticksStuck;

        WaspGoToPortGoal() {
            super();
            this.blacklistedTargets = Lists.newArrayList();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canWaspUse() {
            return Wasp.this.hivePos != null
                    && !Wasp.this.isTooFarAway(Wasp.this.hivePos)
                    && !Wasp.this.hasHome()
                    && Wasp.this.wantsToEnterHive()
                    && !this.hasReachedTarget(Wasp.this.hivePos)
                    && Wasp.this.level().getBlockState(Wasp.this.hivePos).is(ModBlocks.WASP_PORT);
        }

        @Override
        public boolean canWaspContinueToUse() {
            return this.canWaspUse();
        }

        @Override
        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            Wasp.this.navigation.stop();
            Wasp.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (Wasp.this.hivePos != null) {
                this.travellingTicks++;
                if (this.travellingTicks > this.adjustedTickDelay(MAX_TRAVELLING_TICKS)) {
                    this.dropAndBlacklistHive();
                } else if (!Wasp.this.navigation.isInProgress()) {
                    if (!Wasp.this.closerThan(Wasp.this.hivePos, PATHFIND_TO_HIVE_WHEN_CLOSER_THAN)) {
                        if (Wasp.this.isTooFarAway(Wasp.this.hivePos)) {
                            Wasp.this.dropHive();
                        } else {
                            Wasp.this.pathfindRandomlyTowards(Wasp.this.hivePos);
                        }
                    } else {
                        boolean canReachAllTheWayToTarget = this.pathfindDirectlyTowards(Wasp.this.hivePos);
                        if (!canReachAllTheWayToTarget) {
                            this.dropAndBlacklistHive();
                        } else if (this.lastPath != null && Wasp.this.navigation.getPath().sameAs(this.lastPath)) {
                            this.ticksStuck++;
                            if (this.ticksStuck > TICKS_BEFORE_HIVE_DROP) {
                                Wasp.this.dropHive();
                                this.ticksStuck = 0;
                            }
                        } else {
                            this.lastPath = Wasp.this.navigation.getPath();
                        }
                    }
                }
            }
        }

        private boolean pathfindDirectlyTowards(BlockPos targetPos) {
            int closeEnough = Wasp.this.closerThan(targetPos, 3) ? 1 : 2;
            Wasp.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
            Wasp.this.navigation.moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), closeEnough, 1.0);
            return Wasp.this.navigation.getPath() != null && Wasp.this.navigation.getPath().canReach();
        }

        private boolean isTargetBlacklisted(BlockPos targetPos) {
            return this.blacklistedTargets.contains(targetPos);
        }

        private void blacklistTarget(BlockPos targetPos) {
            this.blacklistedTargets.add(targetPos);

            while (this.blacklistedTargets.size() > MAX_BLACKLISTED_TARGETS) {
                this.blacklistedTargets.remove(0);
            }
        }

        private void clearBlacklist() {
            this.blacklistedTargets.clear();
        }

        private void dropAndBlacklistHive() {
            if (Wasp.this.hivePos != null) {
                this.blacklistTarget(Wasp.this.hivePos);
            }

            Wasp.this.dropHive();
        }

        private boolean hasReachedTarget(BlockPos targetPos) {
            if (Wasp.this.closerThan(targetPos, HIVE_CLOSE_ENOUGH_DISTANCE)) {
                return true;
            } else {
                Path path = Wasp.this.navigation.getPath();
                return path != null && path.getTarget().equals(targetPos) && path.canReach() && path.isDone();
            }
        }
    }

    public class WaspGoToKnownFlowerGoal extends BaseWaspGoal {
        private static final int MAX_TRAVELLING_TICKS = 2400;
        private int travellingTicks;

        WaspGoToKnownFlowerGoal() {
            super();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canWaspUse() {
            return Wasp.this.savedFlowerPos != null && !Wasp.this.hasHome() 
                    && this.wantsToGoToKnownFlower() && !Wasp.this.closerThan(Wasp.this.savedFlowerPos, 2);
        }

        @Override
        public boolean canWaspContinueToUse() {
            return this.canWaspUse();
        }

        @Override
        public void start() {
            this.travellingTicks = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.travellingTicks = 0;
            Wasp.this.navigation.stop();
            Wasp.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (Wasp.this.savedFlowerPos != null) {
                this.travellingTicks++;
                if (this.travellingTicks > this.adjustedTickDelay(MAX_TRAVELLING_TICKS)) {
                    Wasp.this.dropFlower();
                } else if (!Wasp.this.navigation.isInProgress()) {
                    if (Wasp.this.isTooFarAway(Wasp.this.savedFlowerPos)) {
                        Wasp.this.dropFlower();
                    } else {
                        Wasp.this.pathfindRandomlyTowards(Wasp.this.savedFlowerPos);
                    }
                }
            }
        }

        private boolean wantsToGoToKnownFlower() {
            return Wasp.this.ticksWithoutNectarSinceExitingHive > TICKS_BEFORE_GOING_TO_KNOWN_FLOWER;
        }
    }

    private class WaspGrowCropGoal extends BaseWaspGoal {
        static final int GROW_CHANCE = 30;

        private WaspGrowCropGoal() {
            super();
        }

        @Override
        public boolean canWaspUse() {
            if (Wasp.this.getCropsGrownSincePollination() >= MAX_CROPS_GROWABLE) {
                return false;
            } else {
                return Wasp.this.random.nextFloat() < 0.3F ? false : Wasp.this.hasNectar() && Wasp.this.isHiveValid();
            }
        }

        @Override
        public boolean canWaspContinueToUse() {
            return this.canWaspUse();
        }

        @Override
        public void tick() {
            if (Wasp.this.random.nextInt(this.adjustedTickDelay(GROW_CHANCE)) == 0) {
                for (int i = 1; i <= 2; i++) {
                    BlockPos belowPos = Wasp.this.blockPosition().below(i);
                    BlockState belowState = Wasp.this.level().getBlockState(belowPos);
                    Block belowBlock = belowState.getBlock();
                    BlockState growState = null;
                    if (belowState.is(Blocks.NETHER_WART)) {
                        if (belowBlock instanceof NetherWartBlock cropBlockBelow) {
                            int age = belowState.getValue(NetherWartBlock.AGE);
                            if (age < NetherWartBlock.MAX_AGE) {
                                growState = belowState.setValue(NetherWartBlock.AGE, age + 1);
                            }
                        }

                        if (growState != null) {
                            Wasp.this.level().levelEvent(2011, belowPos, 15);
                            Wasp.this.level().setBlockAndUpdate(belowPos, growState);
                            Wasp.this.incrementNumCropsGrownSincePollination();
                        }
                    }
                }
            }
        }
    }

    private class WaspHurtByOtherGoal extends HurtByTargetGoal {
        WaspHurtByOtherGoal(Wasp wasp) {
            super(wasp);
        }

        @Override
        public boolean canContinueToUse() {
            return Wasp.this.isAngry() && super.canContinueToUse();
        }

        @Override
        protected void alertOther(Mob other, LivingEntity hurtByMob) {
            if (other instanceof Wasp) {
                other.setTarget(hurtByMob);
            }
        }
    }

    private class WaspLocatePortGoal extends BaseWaspGoal {
        private WaspLocatePortGoal() {
            super();
        }

        @Override
        public boolean canWaspUse() {
            return Wasp.this.remainingCooldownBeforeLocatingNewHive == 0 && !Wasp.this.hasHive() && Wasp.this.wantsToEnterHive();
        }

        @Override
        public boolean canWaspContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Wasp.this.remainingCooldownBeforeLocatingNewHive = COOLDOWN_BEFORE_LOCATING_NEW_HIVE;
            List<BlockPos> hivesWithSpace = this.findNearbyPortsWithSpace();
            if (!hivesWithSpace.isEmpty()) {
                for (BlockPos posToCheck : hivesWithSpace) {
                    if (!Wasp.this.goToPortGoal.isTargetBlacklisted(posToCheck)) {
                        Wasp.this.hivePos = posToCheck;
                        return;
                    }
                }

                Wasp.this.goToPortGoal.clearBlacklist();
                Wasp.this.hivePos = hivesWithSpace.get(0);
            }
        }

        private List<BlockPos> findNearbyPortsWithSpace() {
            BlockPos waspPos = Wasp.this.blockPosition();
            PoiManager poiManager = ((ServerLevel)Wasp.this.level()).getPoiManager();
            Stream<PoiRecord> nearbyHives = poiManager.getInRange(p -> p.equals(ModPOIs.WASP_PORT), waspPos, 20, PoiManager.Occupancy.ANY);
            return nearbyHives.map(PoiRecord::getPos)
                    .filter(Wasp.this::doesPortHaveSpace)
                    .sorted(Comparator.comparingDouble(pos -> pos.distSqr(waspPos)))
                    .collect(Collectors.toList());
        }
    }

    private class WaspLookControl extends LookControl {
        WaspLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (!Wasp.this.isAngry()) {
                super.tick();
            }
        }

        @Override
        protected boolean resetXRotOnTick() {
            return !Wasp.this.waspHarvestGoal.isPollinating();
        }
    }

    private class WaspHarvestGoal extends BaseWaspGoal {
        private static final int MIN_POLLINATION_TICKS = 400;
        private static final double ARRIVAL_THRESHOLD = 0.1;
        private static final int POSITION_CHANGE_CHANCE = 25;
        private static final float SPEED_MODIFIER = 0.35F;
        private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;
        private static final float HOVER_POS_OFFSET = 0.33333334F;
        private static final int FLOWER_SEARCH_RADIUS = 5;
        private int successfulPollinatingTicks;
        private int lastSoundPlayedTick;
        private boolean pollinating;
        private @Nullable Vec3 hoverPos;
        private int pollinatingTicks;
        private static final int MAX_POLLINATING_TICKS = 600;
        private Long2LongOpenHashMap unreachableFlowerCache;

        WaspHarvestGoal() {
            super();
            this.unreachableFlowerCache = new Long2LongOpenHashMap();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canWaspUse() {
            if (Wasp.this.remainingCooldownBeforeLocatingNewFlower > 0) {
                return false;
            } else if (Wasp.this.hasNectar()) {
                return false;
            } else if (Wasp.this.level().isRaining()) {
                return false;
            } else {
                Optional<BlockPos> nearbyPos = this.findNearbyFlower();
                if (nearbyPos.isPresent()) {
                    Wasp.this.savedFlowerPos = nearbyPos.get();
                    Wasp.this.navigation
                            .moveTo(Wasp.this.savedFlowerPos.getX() + 0.5, Wasp.this.savedFlowerPos.getY() + 0.5, Wasp.this.savedFlowerPos.getZ() + 0.5, 1.2F);
                    return true;
                } else {
                    Wasp.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(Wasp.this.random, MIN_FIND_FLOWER_RETRY_COOLDOWN, MAX_FIND_FLOWER_RETRY_COOLDOWN);
                    return false;
                }
            }
        }

        @Override
        public boolean canWaspContinueToUse() {
            if (!this.pollinating) {
                return false;
            } else if (!Wasp.this.hasSavedFlowerPos()) {
                return false;
            } else if (Wasp.this.level().isRaining()) {
                return false;
            } else {
                return this.hasPollinatedLongEnough() ? Wasp.this.random.nextFloat() < 0.2F : true;
            }
        }

        private boolean hasPollinatedLongEnough() {
            return this.successfulPollinatingTicks > MIN_POLLINATION_TICKS;
        }

        private boolean isPollinating() {
            return this.pollinating;
        }

        private void stopPollinating() {
            this.pollinating = false;
        }

        @Override
        public void start() {
            this.successfulPollinatingTicks = 0;
            this.pollinatingTicks = 0;
            this.lastSoundPlayedTick = 0;
            this.pollinating = true;
            Wasp.this.resetTicksWithoutNectarSinceExitingHive();
        }

        @Override
        public void stop() {
            if (this.hasPollinatedLongEnough()) {
                Wasp.this.setHasNectar(true);
            }

            this.pollinating = false;
            Wasp.this.navigation.stop();
            Wasp.this.remainingCooldownBeforeLocatingNewFlower = COOLDOWN_BEFORE_LOCATING_NEW_FLOWER;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (Wasp.this.hasSavedFlowerPos()) {
                this.pollinatingTicks++;
                if (this.pollinatingTicks > MAX_POLLINATING_TICKS) {
                    Wasp.this.dropFlower();
                    this.pollinating = false;
                    Wasp.this.remainingCooldownBeforeLocatingNewFlower = COOLDOWN_BEFORE_LOCATING_NEW_FLOWER;
                } else {
                    Vec3 flowerPos = Vec3.atBottomCenterOf(Wasp.this.savedFlowerPos).add(0.0, 0.6F, 0.0);
                    if (flowerPos.distanceTo(Wasp.this.position()) > 1.0) {
                        this.hoverPos = flowerPos;
                        this.setWantedPos();
                    } else {
                        if (this.hoverPos == null) {
                            this.hoverPos = flowerPos;
                        }

                        boolean arrivedAtHoverPos = Wasp.this.position().distanceTo(this.hoverPos) <= 0.8; // Bee: 0.1
                        boolean shouldSetWantedPos = true;
                        if (!arrivedAtHoverPos && this.pollinatingTicks > MAX_POLLINATING_TICKS) {
                            Wasp.this.dropFlower();
                        } else {
                            if (arrivedAtHoverPos) {
                                boolean shouldChangeHoverPositions = Wasp.this.random.nextInt(25) == 0;
                                if (shouldChangeHoverPositions) {
                                    this.hoverPos = new Vec3(flowerPos.x() + this.getOffset(), flowerPos.y(), flowerPos.z() + this.getOffset());
                                    Wasp.this.navigation.stop();
                                } else {
                                    shouldSetWantedPos = false;
                                }

                                Wasp.this.getLookControl().setLookAt(flowerPos.x(), flowerPos.y(), flowerPos.z());
                            }

                            if (shouldSetWantedPos) {
                                this.setWantedPos();
                            }

                            this.successfulPollinatingTicks++;
                            if (Wasp.this.random.nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                                this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                                Wasp.this.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }

        private void setWantedPos() {
            Wasp.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.35F);
        }

        private float getOffset() {
            return (Wasp.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

        private Optional<BlockPos> findNearbyFlower() {
            Iterable<BlockPos> closestNearbyFlowers = BlockPos.withinManhattan(Wasp.this.blockPosition(), 5, 5, 5);
            Long2LongOpenHashMap tempCache = new Long2LongOpenHashMap();

            for (BlockPos pos : closestNearbyFlowers) {
                long unreachableUntilTime = this.unreachableFlowerCache.getOrDefault(pos.asLong(), Long.MIN_VALUE);
                if (Wasp.this.level().getGameTime() < unreachableUntilTime) {
                    tempCache.put(pos.asLong(), unreachableUntilTime);
                } else if (Wasp.attractsWasps(Wasp.this.level().getBlockState(pos))) {
                    Path path = Wasp.this.navigation.createPath(pos, 1);
                    if (path != null && path.canReach()) {
                        return Optional.of(pos);
                    }

                    tempCache.put(pos.asLong(), Wasp.this.level().getGameTime() + 600L);
                }
            }

            this.unreachableFlowerCache = tempCache;
            return Optional.empty();
        }
    }

    private class WaspWanderGoal extends Goal {
        WaspWanderGoal() {
            super();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return Wasp.this.navigation.isDone() && Wasp.this.random.nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return Wasp.this.navigation.isInProgress();
        }

        @Override
        public void start() {
            Vec3 targetPos = this.findPos();
            if (targetPos != null) {
                Wasp.this.navigation.moveTo(Wasp.this.navigation.createPath(BlockPos.containing(targetPos), 1), 1.0);
            }
        }

        private @Nullable Vec3 findPos() {
            Vec3 wanderDirection;
            if (Wasp.this.isHiveValid() && !Wasp.this.closerThan(Wasp.this.hivePos, this.getWanderThreshold())) {
                Vec3 hivePosVec = Vec3.atCenterOf(Wasp.this.hivePos);
                wanderDirection = hivePosVec.subtract(Wasp.this.position()).normalize();
            } else {
                wanderDirection = Wasp.this.getViewVector(0.0F);
            }

            int xzDist = 8;
            Vec3 groundBasedPosition = HoverRandomPos.getPos(Wasp.this, xzDist, 7, wanderDirection.x, wanderDirection.z, (float) (Math.PI / 2), 3, 1);
            return groundBasedPosition != null
                    ? groundBasedPosition
                    : AirAndWaterRandomPos.getPos(Wasp.this, xzDist, 4, -2, wanderDirection.x, wanderDirection.z, (float) (Math.PI / 2));
        }

        private int getWanderThreshold() {
            int distanceReduction = !Wasp.this.hasHive() && !Wasp.this.hasSavedFlowerPos() ? DEFAULT_WANDER_DISTANCE_REDUCTION : RESTRICTED_WANDER_DISTANCE_REDUCTION;
            return TOO_FAR_DISTANCE - distanceReduction;
        }
    }

    private class ValidateFlowerGoal extends BaseWaspGoal {
        private final int validateFlowerCooldown;
        private long lastValidateTick;

        private ValidateFlowerGoal() {
            super();
            this.validateFlowerCooldown = Mth.nextInt(Wasp.this.random, 20, 40);
            this.lastValidateTick = -1L;
        }

        @Override
        public void start() {
            if (Wasp.this.savedFlowerPos != null && Wasp.this.level().isLoaded(Wasp.this.savedFlowerPos) && !this.isFlower(Wasp.this.savedFlowerPos)) {
                Wasp.this.dropFlower();
            }

            this.lastValidateTick = Wasp.this.level().getGameTime();
        }

        @Override
        public boolean canWaspUse() {
            return Wasp.this.level().getGameTime() > this.lastValidateTick + this.validateFlowerCooldown;
        }

        @Override
        public boolean canWaspContinueToUse() {
            return false;
        }

        private boolean isFlower(BlockPos flowerPos) {
            return Wasp.attractsWasps(Wasp.this.level().getBlockState(flowerPos));
        }
    }

    private class ValidateHiveGoal extends BaseWaspGoal {
        private final int VALIDATE_HIVE_COOLDOWN;
        private long lastValidateTick;

        private ValidateHiveGoal() {
            super();
            this.VALIDATE_HIVE_COOLDOWN = Mth.nextInt(Wasp.this.random, 20, 40);
            this.lastValidateTick = -1L;
        }

        @Override
        public void start() {
            if (Wasp.this.hivePos != null && Wasp.this.level().isLoaded(Wasp.this.hivePos) && !Wasp.this.isHiveValid()) {
                Wasp.this.dropHive();
            }

            this.lastValidateTick = Wasp.this.level().getGameTime();
        }

        @Override
        public boolean canWaspUse() {
            return Wasp.this.level().getGameTime() > this.lastValidateTick + this.VALIDATE_HIVE_COOLDOWN;
        }

        @Override
        public boolean canWaspContinueToUse() {
            return false;
        }
    }
}
