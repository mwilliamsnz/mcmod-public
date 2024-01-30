package abyssal.blocks;

import abyssal.data.ModTags;
import abyssal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.extensions.IForgeBlock;


public class SuperSoilBlock extends Block implements IForgeBlock {
    public SuperSoilBlock(Properties properties) {
        super(properties);
    }

    private static WeightedRandomList PLANT_OPTIONS;

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!level.isAreaLoaded(pos, 1)) return;

        fertiliseBlocks(state, level, pos, rand);

        boolean grass = false;
        for(BlockPos p : BlockPos.betweenClosed(pos.offset(-1,-1,-1), pos.offset(1,1,1))) {
            BlockState s = level.getBlockState(p);
            if(s.is(ModTags.Blocks.GRASS_SPREADERS)) {
                grass = true;
            }
        }
        if(grass && GrassSuperSoilBlock.canBeGrass(state, level, pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.GRASS_SUPER_SOIL.get().defaultBlockState());
        }
    }

    void fertiliseBlocks(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        BlockState s = level.getBlockState(pos.above());
        if(s.isAir()) {
            if(rand.nextInt(40) == 0) {
                getPlantOptions().getRandom(rand).ifPresent((wrapper) -> {
                    level.setBlockAndUpdate(pos.above(), wrapper.getData());
                });
            }
        } else {
            if(s.getBlock() instanceof BonemealableBlock b && rand.nextInt(20) == 0) {
                b.performBonemeal(level, rand, pos.above(), s);
            }
        }
    }

    WeightedRandomList<WeightedEntry.Wrapper<BlockState>> getPlantOptions() {
        if(PLANT_OPTIONS == null) {
            PLANT_OPTIONS = new SimpleWeightedRandomList.Builder<BlockState>()
                    .add(ModBlocks.FERN_CORE.get().defaultBlockState(), 25)
                    .add(ModBlocks.IVY.get().defaultBlockState().setValue(IvyBlock.DOWN, true), 20)
                    .add(Blocks.MOSS_CARPET.defaultBlockState(), 25)
                    .add(Blocks.PINK_PETALS.defaultBlockState(), 15)
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
                    .add(Blocks.BEETROOTS.defaultBlockState(), 5)
                    .build();
        }
        return PLANT_OPTIONS;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, IPlantable plantable) {
        return true;
    }

}
