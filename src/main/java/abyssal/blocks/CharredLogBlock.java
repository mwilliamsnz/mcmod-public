package abyssal.blocks;

import abyssal.data.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;

public class CharredLogBlock extends RotatedPillarBlock {

    public static final BooleanProperty CHAR_BURNING = BooleanProperty.create("char_burning");

    public CharredLogBlock(Properties properties) {
        super(properties);
//        this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
        this.registerDefaultState(this.getStateDefinition().any().setValue(CHAR_BURNING, false).setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CHAR_BURNING);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
        chainBreak(level, pos);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (blockState.getValue(CHAR_BURNING)) {
            level.setBlockAndUpdate(pos, blockState.setValue(CHAR_BURNING, false));
            level.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            for(int y = 0; y <= 1; ++y) {
                for(int x = -1; x <= 1; ++x) {
                    for(int z = -1; z <= 1; ++z) {
                        if(x != 0 || y != 0 || z != 0) {
                            tryBurn(level, pos.offset(x,y,z));
                        }
                    }
                }
            }
        }
    }

    public void tryBurn(ServerLevel level, BlockPos pos) {
        BlockState originalState = level.getBlockState(pos);
        if(originalState.is(BlockTags.LOGS_THAT_BURN)) {
            BlockState newState = this.defaultBlockState().setValue(CHAR_BURNING, true);
            // TODO seems not to work
            if(originalState.hasProperty(RotatedPillarBlock.AXIS)) {
                newState = newState.setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS));
            }
            level.setBlockAndUpdate(pos, newState);
            level.scheduleTick(pos, this, 5);
        } else if(originalState.is(ModTags.Blocks.CHARRING_AXE_DESTROYS)) {
            level.removeBlock(pos, true);
        }
    }

    public void chainBreak(Level level, BlockPos pos) {
        for(int y = 0; y <= 1; ++y) {
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    if(x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    BlockPos chainPos = pos.offset(x, y, z);
                    BlockState originalState = level.getBlockState(chainPos);
                    if (originalState.is(this)) {
                        level.destroyBlock(chainPos, true);
                        chainBreak(level, chainPos);
                    }
                }
            }
        }
    }

}
