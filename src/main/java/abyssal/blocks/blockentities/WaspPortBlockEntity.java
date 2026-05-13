package abyssal.blocks.blockentities;

import abyssal.blocks.WaspPortBlock;
import abyssal.components.WaspsComponent;
import abyssal.entity.Wasp;
import abyssal.init.ModBlockEntityTypes;
import abyssal.init.ModDataComponents;
import abyssal.init.ModEntityTypes;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityProcessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class WaspPortBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String TAG_FLOWER_POS = "flower_pos";
    private static final String TAG_OCCUPANCY = "occupancy";
    private static final String WASPS = "wasps";
    private static final List<String> IGNORED_BEE_TAGS = Arrays.asList(
            "Air",
            "drop_chances",
            "equipment",
            "Brain",
            "CanPickUpLoot",
            "DeathTime",
            "fall_distance",
            "FallFlying",
            "Fire",
            "HurtByTimestamp",
            "HurtTime",
            "LeftHanded",
            "Motion",
            "NoGravity",
            "OnGround",
            "PortalCooldown",
            "Pos",
            "Rotation",
            "sleeping_pos",
            "CannotEnterHiveTicks",
            "TicksSincePollination",
            "CropsGrownSincePollination",
            "hive_pos",
            "Passengers",
            "leash",
            "UUID"
    );
    public static final int MAX_OCCUPANTS = 3;
    private static final int MIN_TICKS_BEFORE_REENTERING_HIVE = 400;
    private static final int MIN_OCCUPATION_TICKS_NECTAR = 2400;
    public static final int MIN_OCCUPATION_TICKS_NECTARLESS = 600;
    private static final int ENERGY_PER_RETURN = 500;
    private final List<WaspData> stored = Lists.newArrayList();
    private @Nullable BlockPos savedFlowerPos;
    private @Nullable BlockPos savedHeartPos;
    private float estimatedOccupancy = MAX_OCCUPANTS;

    public WaspPortBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityTypes.WASP_PORT.get(), worldPosition, blockState);
    }

    public boolean isEmpty() {
        return this.stored.isEmpty();
    }

    public boolean isFull() {
        return this.stored.size() == MAX_OCCUPANTS;
    }

    public void emptyAllLivingFromPort(@Nullable Player player, BlockState state, WaspReleaseStatus releaseReason) {
        List<Entity> releasedFromHive = this.releaseAllOccupants(state, releaseReason);
        if (player != null) {
            for (Entity released : releasedFromHive) {
                if (released instanceof Wasp wasp && player.position().distanceToSqr(released.position()) <= 16.0) {
                    if (!this.isSedated()) {
                        wasp.setTarget(player);
                    } else {
                        wasp.setStayOutOfHiveCountdown(MIN_TICKS_BEFORE_REENTERING_HIVE);
                    }
                }
            }
        }
    }

    private List<Entity> releaseAllOccupants(BlockState state, WaspReleaseStatus releaseStatus) {
        List<Entity> spawned = Lists.newArrayList();
        this.stored
                .removeIf(
                        occupantEntry -> releaseOccupant(this.level, this.worldPosition, state, occupantEntry.toOccupant(), spawned, releaseStatus, this.savedFlowerPos, this.savedHeartPos)
                );
        if (!spawned.isEmpty()) {
            super.setChanged();
        }

        return spawned;
    }

    @VisibleForDebug
    public int getOccupantCount() {
        return this.stored.size();
    }

    public float getEstimatedOccupancy() {
        return estimatedOccupancy;
    }

    public void setEstimatedOccupancy(float occupancy) {
        estimatedOccupancy = occupancy;
    }

    @VisibleForDebug
    public boolean isSedated() {
        return false;
    }

    public void addOccupant(Wasp wasp) {
        if (this.stored.size() < MAX_OCCUPANTS) {
            wasp.stopRiding();
            wasp.ejectPassengers();
            wasp.dropLeash();
            this.storeWasp(Occupant.of(wasp));
            if (this.level != null) {
                if (wasp.hasSavedFlowerPos() && (!this.hasSavedFlowerPos() || this.level.getRandom().nextBoolean())) {
                    this.savedFlowerPos = wasp.getSavedFlowerPos();
                }

                BlockPos blockPos = this.getBlockPos();
                this.level
                        .playSound(
                                null,
                                (double)blockPos.getX(),
                                (double)blockPos.getY(),
                                (double)blockPos.getZ(),
                                SoundEvents.BEEHIVE_ENTER,
                                SoundSource.BLOCKS,
                                1.0F,
                                1.0F
                        );
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(wasp, this.getBlockState()));
            }

            wasp.discard();
            super.setChanged();
        }
    }

    public void storeWasp(Occupant occupant) {
        this.stored.add(new WaspData(occupant));
    }

    private static boolean releaseOccupant(
            Level level,
            BlockPos blockPos,
            BlockState state,
            Occupant waspData,
            @Nullable List<Entity> spawned,
            WaspReleaseStatus releaseStatus,
            @Nullable BlockPos savedFlowerPos,
            @Nullable BlockPos savedHeartPos
    ) {
        Direction facing = state.getValue(WaspPortBlock.FACING);
        for (Direction dir : Direction.values()) {
            BlockPos facingPos = blockPos.relative(dir);
            boolean frontBlocked = !level.getBlockState(facingPos).getCollisionShape(level, facingPos).isEmpty();
            if(!frontBlocked) {
                facing = dir;
                break;
            }
        }
        BlockPos facingPos = blockPos.relative(facing);
        boolean frontBlocked = !level.getBlockState(facingPos).getCollisionShape(level, facingPos).isEmpty();
        if (frontBlocked && releaseStatus != WaspReleaseStatus.EMERGENCY) {
            return false;
        } else {
            Entity entity = waspData.createEntity(level, facingPos);
            if (entity != null) {
                if (entity instanceof Wasp wasp) {
                    RandomSource random = level.getRandom();
                    if (savedFlowerPos != null && !wasp.hasSavedFlowerPos() && random.nextFloat() < 0.9F) {
                        wasp.setSavedFlowerPos(savedFlowerPos);
                    }

                    if (releaseStatus == WaspReleaseStatus.HONEY_DELIVERED) {
                        wasp.dropOffNectar();
                        if(savedHeartPos != null) {
                            if(level.getBlockEntity(savedHeartPos) instanceof HiveheartBlockEntity hive) {
                                hive.addEnergy(ENERGY_PER_RETURN);
                            } else {
                                // TODO look for a new heart
                            }
                        }

                    }

                    if (spawned != null) {
                        spawned.add(wasp);
                    }

                    float bbWidth = entity.getBbWidth();
                    double delta =  0.55 + bbWidth / 2.0F;
                    double spawnX = blockPos.getX() + 0.5 + delta * facing.getStepX();
                    double spawnY = blockPos.getY() + 0.5 - entity.getBbHeight() / 2.0F;
                    double spawnZ = blockPos.getZ() + 0.5 + delta * facing.getStepZ();
                    entity.snapTo(spawnX, spawnY, spawnZ, entity.getYRot(), entity.getXRot());
                }

                level.playSound(null, blockPos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, level.getBlockState(blockPos)));
                return level.addFreshEntity(entity);
            } else {
                return false;
            }

        }
    }

    private boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    private boolean hasSavedHeartPos() {
        return this.savedHeartPos != null;
    }

    private void tickOccupants(Level level, BlockPos pos, BlockState state) {
        boolean changed = false;
        Iterator<WaspData> iterator = stored.iterator();

        estimatedOccupancy += 2*(stored.size() - estimatedOccupancy)/1000; // 1000 tick average
        while (iterator.hasNext()) {
            WaspData data = iterator.next();
            if (data.tick()) {
                WaspReleaseStatus releaseStatus = data.hasNectar()
                        ? WaspReleaseStatus.HONEY_DELIVERED
                        : WaspReleaseStatus.WASP_RELEASED;
                if (releaseOccupant(level, pos, state, data.toOccupant(), null, releaseStatus, savedFlowerPos, savedHeartPos)) {
                    changed = true;
                    iterator.remove();
                }
            }
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, WaspPortBlockEntity entity) {
        entity.tickOccupants(level, blockPos, state);
        if (!entity.stored.isEmpty() && level.getRandom().nextDouble() < 0.005) {
            double x = blockPos.getX() + 0.5;
            double y = blockPos.getY();
            double z = blockPos.getZ() + 0.5;
            level.playSound(null, x, y, z, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.stored.clear();
        input.read(WASPS, Occupant.LIST_CODEC).orElse(List.of()).forEach(this::storeWasp);
        this.savedFlowerPos = input.read(TAG_FLOWER_POS, BlockPos.CODEC).orElse(null);
        this.estimatedOccupancy = input.read(TAG_OCCUPANCY, Codec.FLOAT).orElse(2.0f);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store(WASPS, Occupant.LIST_CODEC, this.getWasps());
        output.storeNullable(TAG_FLOWER_POS, BlockPos.CODEC, this.savedFlowerPos);
        output.storeNullable(TAG_OCCUPANCY, Codec.FLOAT, this.estimatedOccupancy);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.stored.clear();
        List<Occupant> wasps = components.getOrDefault(ModDataComponents.WASPS, WaspsComponent.EMPTY).wasps();
        wasps.forEach(this::storeWasp);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(ModDataComponents.WASPS, new WaspsComponent(this.getWasps()));
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard(WASPS);
    }

    private List<Occupant> getWasps() {
        return this.stored.stream().map(WaspData::toOccupant).toList();
    }

    public void notifyRemoval() {
        if(savedHeartPos != null && level.getBlockEntity(savedHeartPos) instanceof HiveheartBlockEntity hive) {
            hive.removeOrgan(worldPosition);
        }
    }

    public void linkToHeart(BlockPos pos) {
        savedHeartPos = pos;
    }

    public void notifyLossOfWasp() {
    }

    private static class WaspData {
        private final Occupant occupant;
        private int ticksInPort;

        private WaspData(Occupant occupant) {
            this.occupant = occupant;
            this.ticksInPort = occupant.ticksInPort();
        }

        public boolean tick() {
            return this.ticksInPort++ > this.occupant.minTicksInPort;
        }

        public Occupant toOccupant() {
            return new Occupant(this.occupant.entityData, this.ticksInPort, this.occupant.minTicksInPort);
        }

        public boolean hasNectar() {
            return this.occupant.entityData.getUnsafe().getBooleanOr("HasNectar", false);
        }
    }

    public static enum WaspReleaseStatus {
        HONEY_DELIVERED,
        WASP_RELEASED,
        EMERGENCY;
    }

    public record Occupant(TypedEntityData<EntityType<?>> entityData, int ticksInPort, int minTicksInPort) {
        public static final Codec<Occupant> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data").forGetter(Occupant::entityData),
                                Codec.INT.fieldOf("ticks_in_port").forGetter(Occupant::ticksInPort),
                                Codec.INT.fieldOf("min_ticks_in_port").forGetter(Occupant::minTicksInPort)
                        )
                        .apply(i, Occupant::new)
        );
        public static final Codec<List<Occupant>> LIST_CODEC = CODEC.listOf();
        public static final StreamCodec<RegistryFriendlyByteBuf, Occupant> STREAM_CODEC = StreamCodec.composite(
                TypedEntityData.streamCodec(EntityType.STREAM_CODEC),
                Occupant::entityData,
                ByteBufCodecs.VAR_INT,
                Occupant::ticksInPort,
                ByteBufCodecs.VAR_INT,
                Occupant::minTicksInPort,
                Occupant::new
        );

        public static Occupant of(Entity entity) {
            Occupant occupant;
            try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), WaspPortBlockEntity.LOGGER)) {
                TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
                entity.save(output);
                WaspPortBlockEntity.IGNORED_BEE_TAGS.forEach(output::discard);
                CompoundTag entityTag = output.buildResult();
                boolean hasNectar = entityTag.getBooleanOr("HasNectar", false);
                occupant = new Occupant(TypedEntityData.of(entity.getType(), entityTag), 0, hasNectar ? MIN_OCCUPATION_TICKS_NECTAR : MIN_OCCUPATION_TICKS_NECTARLESS);
            }

            return occupant;
        }

        public static Occupant create(int ticksInHive) {
            return new Occupant(TypedEntityData.of(ModEntityTypes.WASP.get(), new CompoundTag()), ticksInHive, MIN_OCCUPATION_TICKS_NECTARLESS);
        }

        public @Nullable Entity createEntity(Level level, BlockPos hivePos) {
            CompoundTag entityTag = this.entityData.copyTagWithoutId();
            WaspPortBlockEntity.IGNORED_BEE_TAGS.forEach(entityTag::remove);
            Entity entity = EntityType.loadEntityRecursive(this.entityData.type(), entityTag, level, EntitySpawnReason.LOAD, EntityProcessor.NOP);
            if (entity != null && entity.is(ModEntityTypes.WASP.get())) {
                entity.setNoGravity(true);
                if (entity instanceof Wasp wasp) {
                    wasp.setHivePos(hivePos);
                }

                return entity;
            } else {
                return null;
            }
        }
    }
}