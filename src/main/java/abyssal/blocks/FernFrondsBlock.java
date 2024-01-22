package abyssal.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static abyssal.blocks.FernCentreBlock.*;

public class FernFrondsBlock extends Block {
    public FernFrondsBlock(Properties p_49795_) {
        super(p_49795_);
    }


    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
        return Block.box(state.getValue(WEST) ? 0 : 4,
                0.0D,
                state.getValue(NORTH) ? 0 : 4,
                state.getValue(EAST) ? 16 : 12,
                6.0,
                state.getValue(SOUTH) ? 16 : 12);
    }


    public boolean connectsTo(BlockState blockState, Direction direction) {
        Block block = blockState.getBlock();
        return block instanceof FernCentreBlock;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockGetter blockgetter = ctx.getLevel();
        BlockPos clickedPos = ctx.getClickedPos();
        BlockPos north = clickedPos.north();
        BlockPos east = clickedPos.east();
        BlockPos south = clickedPos.south();
        BlockPos west = clickedPos.west();
        BlockState northState = blockgetter.getBlockState(north);
        BlockState eastState = blockgetter.getBlockState(east);
        BlockState southState = blockgetter.getBlockState(south);
        BlockState westState = blockgetter.getBlockState(west);
        return super.getStateForPlacement(ctx)
                .setValue(NORTH, this.connectsTo(northState, Direction.SOUTH))
                .setValue(EAST, this.connectsTo(eastState, Direction.WEST))
                .setValue(SOUTH, this.connectsTo(southState, Direction.NORTH))
                .setValue(WEST, this.connectsTo(westState, Direction.EAST));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor levelAccessor, BlockPos pos1, BlockPos pos2) {
        BlockState s = direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? state.setValue(PROPERTY_BY_DIRECTION.get(direction), this.connectsTo(blockState, direction.getOpposite()))
                : super.updateShape(state, direction, blockState, levelAccessor, pos1, pos2);
        if(s.getValue(NORTH) || s.getValue(SOUTH) || s.getValue(EAST) || s.getValue(WEST)) {
            return s;
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorType) {
        switch (mirrorType) {
            case LEFT_RIGHT:
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default:
                return super.mirror(state, mirrorType);
        }
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FernCentreBlock.NORTH, EAST, WEST, SOUTH);
    }
}
