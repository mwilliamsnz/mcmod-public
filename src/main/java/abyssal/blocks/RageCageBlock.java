package abyssal.blocks;

import abyssal.blocks.blockentities.RageCageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RageCageBlock extends SpawnerBlock {

    public RageCageBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new RageCageBlockEntity(pos, blockState);
    }
}
