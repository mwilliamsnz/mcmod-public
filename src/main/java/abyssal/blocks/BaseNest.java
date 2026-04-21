package abyssal.blocks;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.IOwnedSpawner;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class BaseNest implements IOwnedSpawner {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String SPAWN_DATA_TAG = "SpawnData";
    private static final int EVENT_SPAWN = 1;
    private static final int DEFAULT_SPAWN_DELAY = 20;
    private static final int DEFAULT_MIN_SPAWN_DELAY = 200;
    private static final int DEFAULT_MAX_SPAWN_DELAY = 800;
    private static final int DEFAULT_SPAWN_COUNT = 4;
    private static final int DEFAULT_MAX_NEARBY_ENTITIES = 8;
    private static final int DEFAULT_REQUIRED_PLAYER_RANGE = 48;
    private static final int DEFAULT_SPAWN_RANGE = 5;
    private static final int RANGE_TO_CHECK_FOR_EXISTING = 20;
    private int spawnDelay = DEFAULT_SPAWN_DELAY;
    private WeightedList<SpawnData> spawnPotentials = WeightedList.of();
    @Nullable
    private SpawnData nextSpawnData;
    private int minSpawnDelay = DEFAULT_MIN_SPAWN_DELAY;
    private int maxSpawnDelay = DEFAULT_MAX_SPAWN_DELAY;
    private int spawnCount = DEFAULT_SPAWN_COUNT;
    private int maxNearbyEntities = DEFAULT_MAX_NEARBY_ENTITIES;
    private int requiredPlayerRange = DEFAULT_REQUIRED_PLAYER_RANGE;
    private int spawnRange = DEFAULT_SPAWN_RANGE;

    public void setEntityId(EntityType<?> type, @Nullable Level level, RandomSource random, BlockPos pos) {
        this.getOrCreateNextSpawnData(level, random, pos)
                .getEntityToSpawn()
                .putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
    }

    private boolean isNearPlayer(Level level, BlockPos pos) {
        return level.hasNearbyAlivePlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, this.requiredPlayerRange);
    }

    public void clientTick(Level level, BlockPos pos) {
        RandomSource randomsource = level.getRandom();
        double xpos = (double)pos.getX() + randomsource.nextDouble();
        double ypos = (double)pos.getY() + randomsource.nextDouble();
        double zpos = (double)pos.getZ() + randomsource.nextDouble();
        level.addParticle(ParticleTypes.SMOKE, xpos, ypos, zpos, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, xpos, ypos, zpos, 0.0D, 0.0D, 0.0D);
    }

    public void serverTick(ServerLevel level, BlockPos pos) {
        if (this.isNearPlayer(level, pos) && level.isSpawnerBlockEnabled()) {
            if (this.spawnDelay == -1) {
                this.delay(level, pos);
            }

            if (this.spawnDelay > 0) {
                this.spawnDelay--;
            } else {
                boolean delay = false;
                RandomSource random = level.getRandom();
                SpawnData nextSpawnData = this.getOrCreateNextSpawnData(level, random, pos);

                for (int c = 0; c < this.spawnCount; c++) {
                    try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this::toString, LOGGER)) {
                        ValueInput input = TagValueInput.create(reporter, level.registryAccess(), nextSpawnData.getEntityToSpawn());
                        Optional<EntityType<?>> entityType = EntityType.by(input);
                        if (entityType.isEmpty()) {
                            this.delay(level, pos);
                            return;
                        }

                        Vec3 spawnPos = input.read("Pos", Vec3.CODEC)
                                .orElseGet(
                                        () -> new Vec3(
                                                pos.getX() + (random.nextDouble() - random.nextDouble()) * this.spawnRange + 0.5,
                                                pos.getY() + random.nextInt(3) - 1,
                                                pos.getZ() + (random.nextDouble() - random.nextDouble()) * this.spawnRange + 0.5
                                        )
                                );
                        if (level.noCollision(entityType.get().getSpawnAABB(spawnPos.x, spawnPos.y, spawnPos.z))) {
                            BlockPos spawnBlockPos = BlockPos.containing(spawnPos);
                            if (nextSpawnData.getCustomSpawnRules().isPresent()) {
                                if (!entityType.get().getCategory().isFriendly() && level.getDifficulty() == Difficulty.PEACEFUL) {
                                    continue;
                                }

                                SpawnData.CustomSpawnRules customSpawnRules = nextSpawnData.getCustomSpawnRules().get();
                                if (!customSpawnRules.isValidPosition(spawnBlockPos, level)) {
                                    continue;
                                }
                            } else if (!SpawnPlacements.checkSpawnRules(entityType.get(), level, EntitySpawnReason.SPAWNER, spawnBlockPos, level.getRandom())) {
                                continue;
                            }

                            Entity entity = EntityType.loadEntityRecursive(input, level, EntitySpawnReason.SPAWNER, e -> {
                                e.snapTo(spawnPos.x, spawnPos.y, spawnPos.z, e.getYRot(), e.getXRot());
                                return e;
                            });
                            if (entity == null) {
                                this.delay(level, pos);
                                return;
                            }

                            int nearBy = level.getEntities(
                                            EntityTypeTest.forExactClass(entity.getClass()),
                                            new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).inflate(this.spawnRange),
                                            EntitySelector.NO_SPECTATORS
                                    )
                                    .size();
                            if (nearBy >= this.maxNearbyEntities) {
                                this.delay(level, pos);
                                return;
                            }

                            entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), random.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof Mob mob) {
                                // Abyssal: Event hook here removed because it takes a BaseSpawner
//                                if (!net.neoforged.neoforge.event.EventHooks.checkSpawnPositionSpawner(mob, level, EntitySpawnReason.SPAWNER, nextSpawnData, this)) {
//                                    continue;
//                                }

                                boolean hasNoConfiguration = nextSpawnData.getEntityToSpawn().size() == 1
                                        && nextSpawnData.getEntityToSpawn().getString("id").isPresent();
                                // Neo: Patch in FinalizeSpawn for spawners so it may be fired unconditionally, instead of only when vanilla would normally call it.
                                // The local hasNoConfiguration is the conditions under which the spawner will normally call Mob#finalizeSpawn.
                                net.neoforged.neoforge.event.EventHooks.finalizeMobSpawnSpawner(mob, level, level.getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.SPAWNER, null, this, hasNoConfiguration);

                                nextSpawnData.getEquipment().ifPresent(mob::equip);
                            }

                            if (!level.tryAddFreshEntityWithPassengers(entity)) {
                                this.delay(level, pos);
                                return;
                            }

                            level.levelEvent(2004, pos, 0);
                            level.gameEvent(entity, GameEvent.ENTITY_PLACE, spawnBlockPos);
                            if (entity instanceof Mob) {
                                ((Mob)entity).spawnAnim();
                            }

                            delay = true;
                        }
                    }
                }

                if (delay) {
                    this.delay(level, pos);
                }

                return;
            }
        }
    }

    private void delay(Level level, BlockPos pos) {
        RandomSource randomsource = level.getRandom();
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            this.spawnDelay = this.minSpawnDelay + randomsource.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        }

        this.spawnPotentials.getRandom(randomsource).ifPresent(p_393311_ -> this.setNextSpawnData(level, pos, p_393311_));
        this.broadcastEvent(level, pos, 1);
    }

    public void load(@Nullable Level level, BlockPos pos, ValueInput input) {
        this.spawnDelay = input.getShortOr("Delay", (short) DEFAULT_SPAWN_DELAY);
        input.read(SPAWN_DATA_TAG, SpawnData.CODEC).ifPresent(p_400944_ -> this.setNextSpawnData(level, pos, p_400944_));
        this.spawnPotentials = input.read("SpawnPotentials", SpawnData.LIST_CODEC)
                .orElseGet(() -> WeightedList.of(this.nextSpawnData != null ? this.nextSpawnData : new SpawnData()));
        this.minSpawnDelay = input.getIntOr("MinSpawnDelay", DEFAULT_MIN_SPAWN_DELAY);
        this.maxSpawnDelay = input.getIntOr("MaxSpawnDelay", DEFAULT_MAX_SPAWN_DELAY);
        this.spawnCount = input.getIntOr("SpawnCount", DEFAULT_SPAWN_COUNT);
        this.maxNearbyEntities = input.getIntOr("MaxNearbyEntities", DEFAULT_MAX_NEARBY_ENTITIES);
        this.requiredPlayerRange = input.getIntOr("RequiredPlayerRange", DEFAULT_REQUIRED_PLAYER_RANGE);
        this.spawnRange = input.getIntOr("SpawnRange", DEFAULT_SPAWN_RANGE);
    }

    public void save(ValueOutput output) {
        output.putShort("Delay", (short)this.spawnDelay);
        output.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
        output.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        output.putShort("SpawnCount", (short)this.spawnCount);
        output.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        output.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
        output.putShort("SpawnRange", (short)this.spawnRange);
        output.storeNullable("SpawnData", SpawnData.CODEC, this.nextSpawnData);
        output.store("SpawnPotentials", SpawnData.LIST_CODEC, this.spawnPotentials);
    }

    public boolean onEventTriggered(Level level, int id) {
        if (id == 1) {
            if (level.isClientSide()) {
                this.spawnDelay = this.minSpawnDelay;
            }

            return true;
        } else {
            return false;
        }
    }

    protected void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData nextSpawnData) {
        this.nextSpawnData = nextSpawnData;
    }

    private SpawnData getOrCreateNextSpawnData(@Nullable Level level, RandomSource random, BlockPos pos) {
        if (this.nextSpawnData != null) {
            return this.nextSpawnData;
        } else {
            this.setNextSpawnData(level, pos, this.spawnPotentials.getRandom(random).orElseGet(SpawnData::new));
            return this.nextSpawnData;
        }
    }

    public abstract void broadcastEvent(Level level, BlockPos pos, int eventId);

    @Override
    @org.jetbrains.annotations.Nullable
    public com.mojang.datafixers.util.Either<net.minecraft.world.level.block.entity.BlockEntity, Entity> getOwner() {
        // The vanilla anonymous classes have proper overrides, but we return null here for compatibility.
        return null;
    }

}
