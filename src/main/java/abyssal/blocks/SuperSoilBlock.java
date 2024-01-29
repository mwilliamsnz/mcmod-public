package abyssal.blocks;

import abyssal.data.ModTags;
import abyssal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlock;


public class SuperSoilBlock extends Block implements IForgeBlock {
    public SuperSoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        boolean grass = false;
        for(BlockPos p : BlockPos.betweenClosed(pos.offset(-1,-1,-1), pos.offset(1,1,1))) {
            BlockState s = level.getBlockState(p);
            if(s.getBlock() instanceof BonemealableBlock b && rand.nextInt(20) == 0) {
                b.performBonemeal(level, rand, p, s);
            }
            if(s.is(ModTags.Blocks.GRASS_SPREADERS)) {
                grass = true;
            }
        }
        if(grass && GrassSuperSoilBlock.canBeGrass(state, level, pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.GRASS_SUPER_SOIL.get().defaultBlockState());
        }
    }
}
