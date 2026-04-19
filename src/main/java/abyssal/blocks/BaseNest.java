package abyssal.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.IOwnedSpawner;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class BaseNest implements IOwnedSpawner {
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

    public void serverTick(ServerLevel serverLevel, BlockPos pos) {
        if (this.isNearPlayer(serverLevel, pos)) {
            if (this.spawnDelay == -1) {
                this.delay(serverLevel, pos);
            }

            if (this.spawnDelay > 0) {
                this.spawnDelay--;
            } else {
                boolean flag = false;
                RandomSource randomsource = serverLevel.getRandom();
                SpawnData spawndata = this.getOrCreateNextSpawnData(serverLevel, randomsource, pos);

                for (int i = 0; i < this.spawnCount; i++) {
                    CompoundTag compoundtag = spawndata.getEntityToSpawn();
                    Optional<EntityType<?>> optional = EntityType.by(compoundtag);
                    if (optional.isEmpty()) {
                        this.delay(serverLevel, pos);
                        return;
                    }

                    Vec3 vec3 = compoundtag.read("Pos", Vec3.CODEC)
                            .orElseGet(
                                    () -> new Vec3(
                                            pos.getX() + (randomsource.nextDouble() - randomsource.nextDouble()) * this.spawnRange + 0.5,
                                            pos.getY() + randomsource.nextInt(3) - 1,
                                            pos.getZ() + (randomsource.nextDouble() - randomsource.nextDouble()) * this.spawnRange + 0.5
                                    )
                            );
                    if (serverLevel.noCollision(optional.get().getSpawnAABB(vec3.x, vec3.y, vec3.z))) {
                        BlockPos blockpos = BlockPos.containing(vec3);
                        if (spawndata.getCustomSpawnRules().isPresent()) {
                            if (!optional.get().getCategory().isFriendly() && serverLevel.getDifficulty() == Difficulty.PEACEFUL) {
                                continue;
                            }

                            SpawnData.CustomSpawnRules spawndata$customspawnrules = spawndata.getCustomSpawnRules().get();
                            if (!spawndata$customspawnrules.isValidPosition(blockpos, serverLevel)) {
                                continue;
                            }
                        } else if (!SpawnPlacements.checkSpawnRules(optional.get(), serverLevel, EntitySpawnReason.SPAWNER, blockpos, serverLevel.getRandom())) {
                            continue;
                        }

                        Entity entity = EntityType.loadEntityRecursive(compoundtag, serverLevel, EntitySpawnReason.SPAWNER, p_404552_ -> {
                            p_404552_.snapTo(vec3.x, vec3.y, vec3.z, p_404552_.getYRot(), p_404552_.getXRot());
                            return p_404552_;
                        });
                        if (entity == null) {
                            this.delay(serverLevel, pos);
                            return;
                        }

                        int j = serverLevel.getEntities(
                                        EntityTypeTest.forExactClass(entity.getClass()),
                                        new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)
                                                .inflate(RANGE_TO_CHECK_FOR_EXISTING),
                                        EntitySelector.NO_SPECTATORS
                                )
                                .size();
                        if (j >= this.maxNearbyEntities) {
                            this.delay(serverLevel, pos);
                            return;
                        }

                        entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), randomsource.nextFloat() * 360.0F, 0.0F);
                        if (entity instanceof Mob mob) {
                            // event hook removed because it uses a BaseSpawner

                            boolean flag1 = spawndata.getEntityToSpawn().size() == 1 && spawndata.getEntityToSpawn().getString("id").isPresent();
                            // Neo: Patch in FinalizeSpawn for spawners so it may be fired unconditionally, instead of only when vanilla would normally call it.
                            // The local flag1 is the conditions under which the spawner will normally call Mob#finalizeSpawn.
                            net.neoforged.neoforge.event.EventHooks.finalizeMobSpawnSpawner(mob, serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.SPAWNER, null, this, flag1);

                            spawndata.getEquipment().ifPresent(mob::equip);
                        }

                        if (!serverLevel.tryAddFreshEntityWithPassengers(entity)) {
                            this.delay(serverLevel, pos);
                            return;
                        }

                        serverLevel.levelEvent(2004, pos, 0);
                        serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, blockpos);
                        if (entity instanceof Mob) {
                            ((Mob)entity).spawnAnim();
                        }

                        flag = true;
                    }
                }

                if (flag) {
                    this.delay(serverLevel, pos);
                }
            }
        }
    }

    private void delay(Level level, BlockPos pos) {
        RandomSource randomsource = level.random;
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            this.spawnDelay = this.minSpawnDelay + randomsource.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        }

        this.spawnPotentials.getRandom(randomsource).ifPresent(p_393311_ -> this.setNextSpawnData(level, pos, p_393311_));
        this.broadcastEvent(level, pos, 1);
    }

    public void load(@Nullable Level level, BlockPos pos, CompoundTag tag) {
        this.spawnDelay = tag.getShortOr("Delay", (short) DEFAULT_SPAWN_DELAY);
        tag.read(SPAWN_DATA_TAG, SpawnData.CODEC).ifPresent(p_400944_ -> this.setNextSpawnData(level, pos, p_400944_));
        this.spawnPotentials = tag.read("SpawnPotentials", SpawnData.LIST_CODEC)
                .orElseGet(() -> WeightedList.of(this.nextSpawnData != null ? this.nextSpawnData : new SpawnData()));
        this.minSpawnDelay = tag.getIntOr("MinSpawnDelay", DEFAULT_MIN_SPAWN_DELAY);
        this.maxSpawnDelay = tag.getIntOr("MaxSpawnDelay", DEFAULT_MAX_SPAWN_DELAY);
        this.spawnCount = tag.getIntOr("SpawnCount", DEFAULT_SPAWN_COUNT);
        this.maxNearbyEntities = tag.getIntOr("MaxNearbyEntities", DEFAULT_MAX_NEARBY_ENTITIES);
        this.requiredPlayerRange = tag.getIntOr("RequiredPlayerRange", DEFAULT_REQUIRED_PLAYER_RANGE);
        this.spawnRange = tag.getIntOr("SpawnRange", DEFAULT_SPAWN_RANGE);
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putShort("Delay", (short)this.spawnDelay);
        tag.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
        tag.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        tag.putShort("SpawnCount", (short)this.spawnCount);
        tag.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        tag.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
        tag.putShort("SpawnRange", (short)this.spawnRange);
        tag.storeNullable(SPAWN_DATA_TAG, SpawnData.CODEC, this.nextSpawnData);
        tag.store("SpawnPotentials", SpawnData.LIST_CODEC, this.spawnPotentials);
        return tag;
    }

    public boolean onEventTriggered(Level level, int id) {
        if (id == 1) {
            if (level.isClientSide) {
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
