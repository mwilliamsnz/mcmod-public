package abyssal.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.IShearable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IvyBlock extends Block implements IShearable {
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((entry) -> {
        return entry.getKey() != Direction.UP;
    }).collect(Util.toMap());
    private static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<BlockState, VoxelShape> shapesCache;

    public IvyBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(DOWN, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), IvyBlock::calculateShape)));
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape shape = Shapes.empty();
        if (state.getValue(DOWN)) {
            shape = DOWN_AABB;
        }

        if (state.getValue(NORTH)) {
            shape = Shapes.or(shape, NORTH_AABB);
        }

        if (state.getValue(SOUTH)) {
            shape = Shapes.or(shape, SOUTH_AABB);
        }

        if (state.getValue(EAST)) {
            shape = Shapes.or(shape, EAST_AABB);
        }

        if (state.getValue(WEST)) {
            shape = Shapes.or(shape, WEST_AABB);
        }

        return shape.isEmpty() ? Shapes.block() : shape;
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
        return this.shapesCache.get(state);
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return this.hasFaces(this.getUpdatedState(state, reader, pos));
    }

    private boolean hasFaces(BlockState state) {
        return this.countFaces(state) > 0;
    }


    private int countFaces(BlockState state) {
        int faces = 0;
        for(BooleanProperty dir : PROPERTY_BY_DIRECTION.values()) {
            if (state.getValue(dir)) {
                faces++;
            }
        }
        return faces;
    }

    public static boolean isAcceptableNeighbour(BlockGetter getter, BlockPos pos, Direction dir) {
        BlockState state = getter.getBlockState(pos);
        if(state.is(BlockTags.LEAVES)) {
            return false;
        }
        return Block.isFaceFull(state.getCollisionShape(getter, pos), dir.getOpposite());
    }

    private BlockState getUpdatedState(BlockState state, BlockGetter getter, BlockPos pos) {
        BlockPos below = pos.below();
        if (state.getValue(DOWN)) {
            state = state.setValue(DOWN, isAcceptableNeighbour(getter, below, Direction.UP));
        }

//        BlockState aboveState = null;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BooleanProperty dirProp = getPropertyForFace(direction);
            if (state.getValue(dirProp)) {
                boolean canSupport = this.canSupportAtFace(getter, pos, direction);
                // For hanging vines off other vines
//                if (!canSupport) {
//                    if (aboveState == null) {
//                        aboveState = getter.getBlockState(below);
//                    }
//
//                    canSupport = aboveState.is(this) && aboveState.getValue(dirProp);
//                }
                state = state.setValue(dirProp, canSupport);
            }
        }

        return state;
    }

    public BlockState updateShape(BlockState state, Direction dir, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
        if (dir == Direction.UP) { // Don't care if the block above the ivy changes
            return super.updateShape(state, dir, otherState, level, pos, otherPos);
        } else {
            BlockState updated = this.getUpdatedState(state, level, pos);
            return !this.hasFaces(updated) ? Blocks.AIR.defaultBlockState() : updated;
        }
    }

    public void randomTick(BlockState stateHere, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_DO_VINES_SPREAD)) {
            return;
        }
        if (level.random.nextInt(4) == 0 && level.isAreaLoaded(pos, 4)) { // Forge: check area to prevent loading unloaded chunks
            Direction spreadDir = Direction.getRandom(rand);
            if (spreadDir.getAxis().isHorizontal() && !stateHere.getValue(getPropertyForFace(spreadDir))) {
                if (this.canSpread(level, pos)) {
                    BlockPos posAdj = pos.relative(spreadDir);
                    BlockState stateAdj = level.getBlockState(posAdj);
                    if (stateAdj.isAir()) {
                        Direction right = spreadDir.getClockWise();
                        Direction left = spreadDir.getCounterClockWise();
                        boolean hereHasRight = stateHere.getValue(getPropertyForFace(right));
                        boolean hereHasLeft = stateHere.getValue(getPropertyForFace(left));
                        BlockPos posLeft = posAdj.relative(right);
                        BlockPos posRight = posAdj.relative(left);
                        if (hereHasRight && isAcceptableNeighbour(level, posLeft, right)) {
                            level.setBlock(posAdj, this.defaultBlockState().setValue(getPropertyForFace(right), true), 2);
                        } else if (hereHasLeft && isAcceptableNeighbour(level, posRight, left)) {
                            level.setBlock(posAdj, this.defaultBlockState().setValue(getPropertyForFace(left), true), 2);
                        } else {
                            Direction opposite = spreadDir.getOpposite();
                            if(hereHasRight && level.isEmptyBlock(posLeft) && isAcceptableNeighbour(level, pos.relative(right), opposite)) {
                                level.setBlock(posLeft, this.defaultBlockState().setValue(getPropertyForFace(opposite), true), 2);
                            } else if(hereHasLeft && level.isEmptyBlock(posRight) && isAcceptableNeighbour(level, pos.relative(left), opposite)) {
                                level.setBlock(posRight, this.defaultBlockState().setValue(getPropertyForFace(opposite), true), 2);
                            } else if(isAcceptableNeighbour(level, posAdj.below(), Direction.DOWN)) {
                                level.setBlock(posAdj, this.defaultBlockState().setValue(DOWN, true), 2);
                            }
                        }
                    } else if (isAcceptableNeighbour(level, posAdj, spreadDir)) {
                        level.setBlock(pos, stateHere.setValue(getPropertyForFace(spreadDir), true), 2);
                    }

                }
            } else {
                if (spreadDir == Direction.UP && pos.getY() < level.getMaxBuildHeight() - 1) {
                    if (this.canSupportAtFace(level, pos, spreadDir)) {
                        //level.setBlock(pos, stateHere.setValue(UP, true), 2);
                        return;
                    }
                    BlockPos posAbove = pos.above();
                    if (level.isEmptyBlock(posAbove)) {
                        if (!this.canSpread(level, pos)) {
                            return;
                        }

                        BlockState newState = stateHere;

                        for(Direction dirAroundNew : Direction.Plane.HORIZONTAL) {
                            if (rand.nextBoolean() || !isAcceptableNeighbour(level, posAbove.relative(dirAroundNew), dirAroundNew)) {
                                newState = newState.setValue(getPropertyForFace(dirAroundNew), false);
                            }
                        }

                        if (this.hasVerticalFace(newState)) {
                            level.setBlock(posAbove, newState, 2);
                        }

                        return;
                    }
                }

                // Spreading down
                if (pos.getY() > level.getMinBuildHeight()) {
                    BlockPos posBelow = pos.below();
                    BlockState stateBelow = level.getBlockState(posBelow);
                    if (stateBelow.isAir() || stateBelow.is(this)) {
                        BlockState newStateBelow = stateBelow.isAir() ? this.defaultBlockState() : stateBelow;
                        BlockState newBelowWithFaces = this.copyRandomFaces(level, stateHere, newStateBelow, posBelow, rand);
                        if (newStateBelow != newBelowWithFaces && this.hasVerticalFace(newBelowWithFaces)) {
                            level.setBlock(posBelow, newBelowWithFaces, 2);
                        }
                    }
                }

            }
        }
    }

    private boolean canSupportAtFace(BlockGetter getter, BlockPos pos, Direction dir) {
        if (dir == Direction.UP) {
            return false;
        } else {
            BlockPos adj = pos.relative(dir);
            return isAcceptableNeighbour(getter, adj, dir);
            // More hanging vines off vines
//            else if (dir.getAxis() == Direction.Axis.Y) {
//                return false;
//            } else {
//                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(dir);
//                BlockState blockstate = getter.getBlockState(pos.above());
//                return blockstate.is(this) && blockstate.getValue(booleanproperty);
//            }
        }
    }

    private BlockState copyRandomFaces(ServerLevel level, BlockState from, BlockState to, BlockPos toPos, RandomSource rand) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (rand.nextBoolean()) {
                BooleanProperty dirProp = getPropertyForFace(direction);
                if (from.getValue(dirProp) && canSupportAtFace(level, toPos, direction)) {
                    to = to.setValue(dirProp, true);
                }
            }
        }

        return to;
    }

    private boolean hasVerticalFace(BlockState state) {
        return state.getValue(NORTH) || state.getValue(EAST) || state.getValue(SOUTH) || state.getValue(WEST);
    }

    private boolean canSpread(BlockGetter level, BlockPos pos) {
        int i = 4;
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(pos.getX() - i, pos.getY() - 1, pos.getZ() - i, pos.getX() + i, pos.getY() + 1, pos.getZ() + i);
        int cap = 5;

        for(BlockPos blockpos : iterable) {
            if (level.getBlockState(blockpos).is(this)) {
                cap--;
                if (cap <= 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
        BlockState stateAtClicked = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (stateAtClicked.is(this)) {
            return this.countFaces(stateAtClicked) < PROPERTY_BY_DIRECTION.size(); // Hasn't got every face full
        } else {
            return super.canBeReplaced(state, ctx);
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState stateAtClicked = ctx.getLevel().getBlockState(ctx.getClickedPos());
        boolean alreadyIvy = stateAtClicked.is(this);
        BlockState state = alreadyIvy ? stateAtClicked : this.defaultBlockState();

        for(Direction direction : ctx.getNearestLookingDirections()) {
            if (direction != Direction.UP) {
                BooleanProperty dirProp = getPropertyForFace(direction);
                boolean alreadyIvyOnThisFace = alreadyIvy && stateAtClicked.getValue(dirProp);
                if (!alreadyIvyOnThisFace && this.canSupportAtFace(ctx.getLevel(), ctx.getClickedPos(), direction)) {
                    return state.setValue(dirProp, true);
                }
            }
        }

        return alreadyIvy ? state : null;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DOWN, NORTH, EAST, SOUTH, WEST);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return switch (rot) {
            case CLOCKWISE_180 ->
                    state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default -> state;
        };
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> super.mirror(state, mirror);
        };
    }

    public static BooleanProperty getPropertyForFace(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }

}
