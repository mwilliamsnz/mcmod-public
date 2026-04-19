package abyssal.blocks;

import abyssal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;

public class GrassSuperSoilBlock extends SuperSoilBlock {
    public GrassSuperSoilBlock(Properties properties) {
        super(properties);
    }

    private static WeightedList<BlockState> GRASS_PLANT_OPTIONS;

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!level.isAreaLoaded(pos, 1)) return;

        fertiliseBlocks(state, level, pos, rand);

        for(BlockPos p : BlockPos.betweenClosed(pos.offset(-1,-1,-1), pos.offset(1,1,1))) {
            BlockState s = level.getBlockState(p);
            if(s.is(Blocks.DIRT) && rand.nextInt(10) == 0 && canBeGrass(s, level, p)) {
                level.setBlockAndUpdate(p, Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        if (!canBeGrass(state, level, pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.SUPER_SOIL.get().defaultBlockState());
        }
    }

    public static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState stateAbove = levelReader.getBlockState(above);
        if (stateAbove.is(Blocks.SNOW) && stateAbove.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (stateAbove.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int light = LightEngine.getLightBlockInto(state, stateAbove, Direction.UP, stateAbove.getLightBlock());
            return light < 15;
        }
    }

    WeightedList<BlockState> getPlantOptions() {
        if(GRASS_PLANT_OPTIONS == null) {
            GRASS_PLANT_OPTIONS = new WeightedList.Builder<BlockState>()
                    .add(ModBlocks.CLOVER.get().defaultBlockState(), 20)
                    .add(ModBlocks.FERN_CORE.get().defaultBlockState(), 20)
                    .add(ModBlocks.IVY.get().defaultBlockState().setValue(IvyBlock.DOWN, true), 20)
                    .add(Blocks.SHORT_GRASS.defaultBlockState(), 10)
                    .add(Blocks.MOSS_CARPET.defaultBlockState(), 10)
                    .add(Blocks.PINK_PETALS.defaultBlockState(), 10)
                    .add(Blocks.SUGAR_CANE.defaultBlockState(), 4)
                    .add(Blocks.DANDELION.defaultBlockState(), 4)
                    .add(Blocks.POPPY.defaultBlockState(), 4)
                    .add(Blocks.BLUE_ORCHID.defaultBlockState(), 4)
                    .add(Blocks.ALLIUM.defaultBlockState(), 4)
                    .add(Blocks.RED_TULIP.defaultBlockState(), 1)
                    .add(Blocks.ORANGE_TULIP.defaultBlockState(), 1)
                    .add(Blocks.WHITE_TULIP.defaultBlockState(), 1)
                    .add(Blocks.PINK_TULIP.defaultBlockState(), 1)
                    .add(Blocks.OXEYE_DAISY.defaultBlockState(), 4)
                    .add(Blocks.CORNFLOWER.defaultBlockState(), 4)
                    .add(Blocks.LILY_OF_THE_VALLEY.defaultBlockState(), 4)
                    .add(Blocks.CARROTS.defaultBlockState(), 5)
                    .add(Blocks.POTATOES.defaultBlockState(), 5)
                    .add(Blocks.WHEAT.defaultBlockState(), 5)
                    .add(Blocks.BEETROOTS.defaultBlockState(), 5)
                    .build();
        }
        return GRASS_PLANT_OPTIONS;
    }
}
