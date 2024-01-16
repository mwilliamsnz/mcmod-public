package abyssal.blocks;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Map;

public class FernCentreBlock extends Block {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    protected static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((entry) -> {
        return entry.getKey().getAxis().isHorizontal();
    }).collect(Util.toMap());

    public FernCentreBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE));

    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getGameRules().getBoolean(GameRules.RULE_DO_VINES_SPREAD)) {
            if (level.random.nextInt(4) == 0 && level.isAreaLoaded(pos, 4)) { // Forge: check area to prevent loading unloaded chunks
                Direction direction = Direction.getRandom(random);
                if (direction.getAxis().isHorizontal()) {
                    BlockPos newPos = pos.relative(direction);
                    if (level.isEmptyBlock(newPos)) {
                        level.setBlock(newPos, this.defaultBlockState().setValue(PROPERTY_BY_DIRECTION.get(direction.getOpposite()), true), 2);
                    }
//                    else if(level.getBlockState(newPos).getBlock() instanceof FernFrondsBlock) {
//                        level.setBlock(newPos, this.defaultBlockState().setValue(PROPERTY_BY_DIRECTION.get(direction.getOpposite()), true), 2);
//                    }
                }
            }
        }
    }

    public boolean connectsTo(BlockState blockState, Direction direction) {
        Block block = blockState.getBlock();
        return block instanceof FernFrondsBlock;
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
        return direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? state.setValue(PROPERTY_BY_DIRECTION.get(direction), this.connectsTo(blockState, direction.getOpposite()))
                        : super.updateShape(state, direction, blockState, levelAccessor, pos1, pos2);
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
        builder.add(NORTH, EAST, WEST, SOUTH);
    }
}
