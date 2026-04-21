package abyssal.blocks.blockentities;

import abyssal.blocks.BaseNest;
import abyssal.init.ModBlockEntityTypes;
import abyssal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;

public class SpiderNestBlockEntity extends BlockEntity implements Spawner {

    public SpiderNestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SPIDER_NEST.get(), pos, state);
    }

    private final BaseNest nest = new BaseNest() {
        public void broadcastEvent(Level level, BlockPos pos, int id) {
            level.blockEvent(pos, ModBlocks.SPIDER_NEST.get(), id, 0);
        }

        public void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData spawnData) {
            super.setNextSpawnData(level, pos, spawnData);
            if (level != null) {
                BlockState blockstate = level.getBlockState(pos);
                level.sendBlockUpdated(pos, blockstate, blockstate, 260);
            }
        }

        @Override
        public com.mojang.datafixers.util.Either<net.minecraft.world.level.block.entity.BlockEntity, net.minecraft.world.entity.Entity> getOwner() {
            return com.mojang.datafixers.util.Either.left(SpiderNestBlockEntity.this);
        }

    };

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.nest.load(this.level, this.worldPosition, tag);
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        this.nest.save(tag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider p_324015_) {
        CompoundTag compoundtag = this.saveCustomOnly(p_324015_);
        compoundtag.remove("SpawnPotentials");
        return compoundtag;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SpiderNestBlockEntity nestBE) {
        nestBE.nest.clientTick(level, pos);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SpiderNestBlockEntity nestBE) {
        nestBE.nest.serverTick((ServerLevel)level, pos);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean triggerEvent(int id, int p_59798_) {
        return this.nest.onEventTriggered(this.level, id) || super.triggerEvent(id, p_59798_);
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public BaseNest getNest() {
        return this.nest;
    }

    @Override
    public void setEntityId(EntityType<?> entityType, RandomSource random) {
        this.nest.setEntityId(entityType, this.level, random, this.worldPosition);
        this.setChanged();
    }
}
