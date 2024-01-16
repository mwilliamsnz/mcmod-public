package abyssal.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class RageCageBlockEntity extends SpawnerBlockEntity {

    private final BaseSpawner spawner = new BaseSpawner() {
        public void broadcastEvent(Level level, BlockPos pos, int p_155769_) {
            level.blockEvent(pos, Blocks.SPAWNER, p_155769_, 0);
        }

        public void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData spawnData) {
            super.setNextSpawnData(level, pos, spawnData);
            if (level != null) {
                BlockState blockstate = level.getBlockState(pos);
                level.sendBlockUpdated(pos, blockstate, blockstate, 4);
            }

        }

        @Nullable
        public BlockEntity getSpawnerBlockEntity(){ return RageCageBlockEntity.this; }
    };

    public RageCageBlockEntity(BlockPos p_155752_, BlockState p_155753_) {
        //super(BlockEntityType.MOB_SPAWNER, p_155752_, p_155753_);
        super(p_155752_, p_155753_);
    }

//    public void load(CompoundTag p_155760_) {
//        super.load(p_155760_);
//        this.spawner.load(this.level, this.worldPosition, p_155760_);
//    }
//
//    public CompoundTag save(CompoundTag p_59795_) {
//        super.save(p_59795_);
//        this.spawner.save(this.level, this.worldPosition, p_59795_);
//        return p_59795_;
//    }
//
//    public static void clientTick(Level p_155755_, BlockPos p_155756_, BlockState p_155757_, net.minecraft.world.level.block.entity.SpawnerBlockEntity p_155758_) {
//        p_155758_.spawner.clientTick(p_155755_, p_155756_);
//    }
//
//    public static void serverTick(Level p_155762_, BlockPos p_155763_, BlockState p_155764_, net.minecraft.world.level.block.entity.SpawnerBlockEntity p_155765_) {
//        p_155765_.spawner.serverTick((ServerLevel)p_155762_, p_155763_);
//    }

//    @Nullable
//    public ClientboundBlockEntityDataPacket getUpdatePacket() {
//        return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, this.getUpdateTag());
//    }
//
//    public CompoundTag getUpdateTag() {
//        CompoundTag compoundtag = this.save(new CompoundTag());
//        compoundtag.remove("SpawnPotentials");
//        return compoundtag;
//    }
//
//    public boolean triggerEvent(int p_59797_, int p_59798_) {
//        return this.spawner.onEventTriggered(this.level, p_59797_) ? true : super.triggerEvent(p_59797_, p_59798_);
//    }
//
//    public boolean onlyOpCanSetNbt() {
//        return true;
//    }
//
//    public BaseSpawner getSpawner() {
//        return this.spawner;
//    }
}
