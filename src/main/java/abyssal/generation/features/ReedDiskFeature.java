package abyssal.generation.features;

import abyssal.blocks.ReedBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

public class ReedDiskFeature extends Feature<DiskConfiguration> {
   public ReedDiskFeature(Codec<DiskConfiguration> codec) {
      super(codec);
   }

   public boolean place(FeaturePlaceContext<DiskConfiguration> ctx) {
      DiskConfiguration cfg = ctx.config();
      BlockPos pos = ctx.origin();
      pos = new BlockPos(pos.getX(), 63, pos.getZ());
      WorldGenLevel level = ctx.level();
      RandomSource random = ctx.random();
      boolean placed = false;
      int r = cfg.radius().sample(random);

      double ex = 0.8 + 0.4 * random.nextFloat();
      double ez = 0.8 + 0.4 * random.nextFloat();
      for(BlockPos placePos : BlockPos.betweenClosed(pos.offset(-r, 0, -r), pos.offset(r, 0, r))) {
         int dx = placePos.getX() - pos.getX();
         int dz = placePos.getZ() - pos.getZ();
         double d = dx * dx * ex + dz * dz * ez;
         if (d <= r * r) {
            if (d > (r-1)*(r-1)) {
               if(random.nextInt(2) == 0) {
                  continue;
               }
            }
            placed |= this.placeStack(cfg, level, random, placePos);
         }
      }

      return placed;
   }

   protected boolean placeStack(DiskConfiguration cfg, WorldGenLevel level, RandomSource random, BlockPos pos) {
      boolean canPutBelow = cfg.target().test(level, pos.below()) && level.getBlockState(pos.below()).is(Blocks.WATER) && level.getBlockState(pos).isAir();
      boolean canPutMid = cfg.target().test(level, pos);
      boolean emptyAbove = level.getBlockState(pos.above()).isAir();
      if((canPutBelow || canPutMid) && emptyAbove) {
         BlockState state = cfg.stateProvider().getState(level, random, pos);
         if(canPutBelow) {
            level.setBlock(pos.below(), state.setValue(ReedBlock.TYPE, 2).setValue(ReedBlock.WATERLOGGED, true), 2);
         }
         level.setBlock(pos, state.setValue(ReedBlock.TYPE, 1).setValue(ReedBlock.WATERLOGGED, false), 2);
         level.setBlock(pos.above(), state.setValue(ReedBlock.TYPE, 3).setValue(ReedBlock.WATERLOGGED, false), 2);

         return true;
      }
      return false;
   }
}