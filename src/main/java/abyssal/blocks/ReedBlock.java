package abyssal.blocks;

import abyssal.Main;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;

public class ReedBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<ReedBlock> CODEC = simpleCodec(ReedBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3);
    public static final int TYPE_IMMATURE = 0;
    public static final int TYPE_MIDDLE = 1;
    public static final int TYPE_UNDERWATER = 2;
    public static final int TYPE_MATURE_TOP = 3;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final float AABB_OFFSET = 6.0F;
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    @Override
    public MapCodec<ReedBlock> codec() {
        return CODEC;
    }

    public ReedBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(TYPE, 0).setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos().below());
        if(state.is(this)) {
            return null;
        }
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos().below());
        boolean wet = fluidstate.getType() == Fluids.WATER;
        return super.getStateForPlacement(pContext).setValue(WATERLOGGED, wet);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(!pLevel.getBlockState(pPos.above()).is(this)) {
            BlockState below = pLevel.getBlockState(pPos.below());
            if(below.is(this)) {
                pLevel.setBlock(pPos.below(), below.setValue(TYPE, TYPE_MIDDLE), 4);
                pLevel.setBlock(pPos, pState.setValue(TYPE, TYPE_MATURE_TOP), 4);
            } else {
                pLevel.setBlock(pPos, pState.setValue(TYPE, TYPE_IMMATURE), 4);
            }
        }
        if (pLevel.isEmptyBlock(pPos.above())) {
            BlockState below = pLevel.getBlockState(pPos.below());
            if(below.is(this)) {
                if(!pLevel.getFluidState(pPos.below()).is(Fluids.WATER)) {
                    return;
                };
            }

            int age = pState.getValue(AGE);
            if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(pLevel, pPos, pState, true)) {
                if (age >= 15) {
                    pLevel.setBlockAndUpdate(pPos.above(), this.defaultBlockState().setValue(TYPE, TYPE_MATURE_TOP));
                    net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(pLevel, pPos.above(), this.defaultBlockState());
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(AGE, 0).setValue(TYPE, TYPE_MIDDLE));
                    if(below.is(this)) {
                        pLevel.setBlockAndUpdate(pPos.below(), below.setValue(TYPE, TYPE_UNDERWATER));
                    }
                } else {
                    pLevel.setBlock(pPos, pState.setValue(AGE, age + 1), 4);
                }
            }

        }
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos,
            Direction dir, BlockPos facingPos, BlockState facingState, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            tickAccess.scheduleTick(pos, this, 1);
        }
        if (state.getValue(WATERLOGGED)) {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, tickAccess, pos, dir, facingPos, facingState, random);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState soil = pLevel.getBlockState(pPos.below());
        if(!pLevel.getFluidState(pPos.above()).isEmpty()) {
            return false;
        }
        TriState canSustain = soil.canSustainPlant(pLevel, pPos.below(), Direction.UP, pState);
        if (!canSustain.isDefault()) return canSustain.isTrue();
        BlockState blockstate = pLevel.getBlockState(pPos.below());

        return blockstate.is(this) || blockstate.is(BlockTags.DIRT) || blockstate.is(BlockTags.SAND) || blockstate.is(Blocks.CLAY) || blockstate.is(Tags.Blocks.GRAVELS);

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE).add(TYPE).add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }
}
