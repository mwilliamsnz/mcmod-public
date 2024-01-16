package abyssal.blocks.blockentities;

import abyssal.blocks.BaseNest;
import abyssal.init.ModBlockEntityTypes;
import abyssal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SpiderNestBlockEntity extends BlockEntity {

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
                level.sendBlockUpdated(pos, blockstate, blockstate, 4);
            }

        }
        public BlockEntity getSpawnerBlockEntity(){ return SpiderNestBlockEntity.this; }

    };

    public void load(CompoundTag tag) {
        super.load(tag);
        this.nest.load(this.level, this.worldPosition, tag);
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.nest.save(tag);
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

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = this.saveWithoutMetadata();
        compoundtag.remove("SpawnPotentials");
        return compoundtag;
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
}
