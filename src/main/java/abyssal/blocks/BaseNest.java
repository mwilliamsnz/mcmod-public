package abyssal.blocks;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class BaseNest {private static final Logger LOGGER = LogUtils.getLogger();
    private static final int EVENT_SPAWN = 1;
    private int spawnDelay = 20;
    private SimpleWeightedRandomList<SpawnData> spawnPotentials = SimpleWeightedRandomList.empty();
    private SpawnData nextSpawnData = new SpawnData();
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;
    private int maxNearbyEntities = 8;
    private int requiredPlayerRange = 48;
    private int spawnRange = 5;

    public void setEntityId(EntityType<?> entityType) {
        this.nextSpawnData.getEntityToSpawn().putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString());
    }

    private boolean isNearPlayer(Level level, BlockPos pos) {
        return level.hasNearbyAlivePlayer((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, this.requiredPlayerRange);
    }

    public void clientTick(Level level, BlockPos pos) {
        if (this.isNearPlayer(level, pos)) {
            RandomSource randomsource = level.getRandom();
            double xpos = (double)pos.getX() + randomsource.nextDouble();
            double ypos = (double)pos.getY() + randomsource.nextDouble();
            double zpos = (double)pos.getZ() + randomsource.nextDouble();
            level.addParticle(ParticleTypes.SMOKE, xpos, ypos, zpos, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, xpos, ypos, zpos, 0.0D, 0.0D, 0.0D);
            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            }
        }

    }

    public void serverTick(ServerLevel level, BlockPos pos) {
        if (this.isNearPlayer(level, pos)) {
            if (this.spawnDelay == -1) {
                this.delay(level, pos);
            }

            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            } else {
                boolean spawnedAny = false;

                for(int numSpawned = 0; numSpawned < this.spawnCount; ++numSpawned) {
                    CompoundTag entityToSpawn = this.nextSpawnData.getEntityToSpawn();
                    Optional<EntityType<?>> typeGetter = EntityType.by(entityToSpawn);
                    if (typeGetter.isEmpty()) {
                        this.delay(level, pos);
                        return;
                    }

                    ListTag posTag = entityToSpawn.getList("Pos", 6);
                    int size = posTag.size();
                    RandomSource rand = level.getRandom();
                    double xpos = size >= 1 ? posTag.getDouble(0) : (double)pos.getX() + (rand.nextDouble() - rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    double ypos = size >= 2 ? posTag.getDouble(1) : (double)(pos.getY() + rand.nextInt(3) - 1);
                    double zpos = size >= 3 ? posTag.getDouble(2) : (double)pos.getZ() + (rand.nextDouble() - rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    if (level.noCollision(typeGetter.get().getAABB(xpos, ypos, zpos))) {
                        BlockPos spawnPos = BlockPos.containing(xpos, ypos, zpos);
                        if (this.nextSpawnData.getCustomSpawnRules().isPresent()) {
                            if (!typeGetter.get().getCategory().isFriendly() && level.getDifficulty() == Difficulty.PEACEFUL) {
                                continue;
                            }

                            SpawnData.CustomSpawnRules spawnRules = this.nextSpawnData.getCustomSpawnRules().get();
                            if (!spawnRules.blockLightLimit().isValueInRange(level.getBrightness(LightLayer.BLOCK, spawnPos)) || !spawnRules.skyLightLimit().isValueInRange(level.getBrightness(LightLayer.SKY, spawnPos))) {
                                continue;
                            }
                        } else if (!SpawnPlacements.checkSpawnRules(typeGetter.get(), level, MobSpawnType.SPAWNER, spawnPos, level.getRandom())) {
                            continue;
                        }

                        Entity freshSpawn = EntityType.loadEntityRecursive(entityToSpawn, level, (entity) -> {
                            entity.moveTo(xpos, ypos, zpos, entity.getYRot(), entity.getXRot());
                            return entity;
                        });
                        if (freshSpawn == null) {
                            this.delay(level, pos);
                            return;
                        }

                        int k = level.getEntitiesOfClass(freshSpawn.getClass(), (new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)).inflate(this.spawnRange)).size();
                        if (k >= this.maxNearbyEntities) {
                            this.delay(level, pos);
                            return;
                        }

                        freshSpawn.moveTo(freshSpawn.getX(), freshSpawn.getY(), freshSpawn.getZ(), rand.nextFloat() * 360.0F, 0.0F);
                        if (freshSpawn instanceof Mob mob) {
                            if (this.nextSpawnData.getCustomSpawnRules().isEmpty() && !mob.checkSpawnRules(level, MobSpawnType.SPAWNER) || !mob.checkSpawnObstruction(level)) {
                                continue;
                            }
                            if (this.nextSpawnData.getEntityToSpawn().size() == 1 && this.nextSpawnData.getEntityToSpawn().contains("id", 8)) {
                                ((Mob)freshSpawn).finalizeSpawn(level, level.getCurrentDifficultyAt(freshSpawn.blockPosition()), MobSpawnType.SPAWNER, null, null);
                            }
                        }

                        if (!level.tryAddFreshEntityWithPassengers(freshSpawn)) {
                            this.delay(level, pos);
                            return;
                        }

                        level.levelEvent(2004, pos, 0);
                        level.gameEvent(freshSpawn, GameEvent.ENTITY_PLACE, spawnPos);
                        if (freshSpawn instanceof Mob) {
                            ((Mob)freshSpawn).spawnAnim();
                        }

                        spawnedAny = true;
                    }
                }

                if (spawnedAny) {
                    this.delay(level, pos);
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

        this.spawnPotentials.getRandom(randomsource).ifPresent((dataWrapper) -> {
            this.setNextSpawnData(level, pos, dataWrapper.getData());
        });
        this.broadcastEvent(level, pos, 1);
    }


    public void load(@Nullable Level level, BlockPos pos, CompoundTag tag) {
        this.spawnDelay = tag.getShort("Delay");
        boolean hasSpawnData = tag.contains("SpawnData", 10);
        if (hasSpawnData) {
            SpawnData spawndata1 = SpawnData.CODEC.parse(NbtOps.INSTANCE, tag.getCompound("SpawnData")).resultOrPartial((s) -> {
                LOGGER.warn("Invalid SpawnData: {}", s);
            }).orElseGet(SpawnData::new);
            this.setNextSpawnData(level, pos, spawndata1);
        }

        boolean hasSpawnPotentials = tag.contains("SpawnPotentials", 9);
        if (hasSpawnPotentials) {
            ListTag spawnPotentialsTag = tag.getList("SpawnPotentials", 10);
            this.spawnPotentials = SpawnData.LIST_CODEC.parse(NbtOps.INSTANCE, spawnPotentialsTag).resultOrPartial((s) -> {
                LOGGER.warn("Invalid SpawnPotentials list: {}", s);
            }).orElseGet(SimpleWeightedRandomList::empty);
        } else {
            this.spawnPotentials = SimpleWeightedRandomList.single(this.nextSpawnData != null ? this.nextSpawnData : new SpawnData());
        }

        if (tag.contains("MinSpawnDelay", 99)) {
            this.minSpawnDelay = tag.getShort("MinSpawnDelay");
            this.maxSpawnDelay = tag.getShort("MaxSpawnDelay");
            this.spawnCount = tag.getShort("SpawnCount");
        }

        if (tag.contains("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = tag.getShort("MaxNearbyEntities");
            this.requiredPlayerRange = tag.getShort("RequiredPlayerRange");
        }

        if (tag.contains("SpawnRange", 99)) {
            this.spawnRange = tag.getShort("SpawnRange");
        }
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putShort("Delay", (short)this.spawnDelay);
        tag.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
        tag.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        tag.putShort("SpawnCount", (short)this.spawnCount);
        tag.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        tag.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
        tag.putShort("SpawnRange", (short)this.spawnRange);
        tag.put("SpawnData", SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, this.nextSpawnData).result().orElseThrow(() -> {
            return new IllegalStateException("Invalid SpawnData");
        }));
        tag.put("SpawnPotentials", SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.spawnPotentials).result().orElseThrow());
        return tag;
    }

    public boolean onEventTriggered(Level event, int id) {
        if (id == EVENT_SPAWN) {
            if (event.isClientSide) {
                this.spawnDelay = this.minSpawnDelay;
            }

            return true;
        } else {
            return false;
        }
    }

    public void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData spawnData) {
        this.nextSpawnData = spawnData;
    }

    public abstract void broadcastEvent(Level level, BlockPos pos, int id);

    @Nullable
    public Entity getSpawnerEntity() {
        return null;
    }

    @Nullable
    public net.minecraft.world.level.block.entity.BlockEntity getSpawnerBlockEntity(){ return null; }
}
