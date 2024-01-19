package abyssal.generation.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

public class RaggedDiskFeature extends Feature<DiskConfiguration> {
   public RaggedDiskFeature(Codec<DiskConfiguration> codec) {
      super(codec);
   }

   public boolean place(FeaturePlaceContext<DiskConfiguration> ctx) {
      DiskConfiguration cfg = ctx.config();
      BlockPos blockpos = ctx.origin();
      WorldGenLevel level = ctx.level();
      RandomSource random = ctx.random();
      boolean placed = false;
      int y = blockpos.getY();
      int yPlus = y + cfg.halfHeight();
      int yMinus = y - cfg.halfHeight() - 1;
      int r = cfg.radius().sample(random);
      BlockPos.MutableBlockPos mPos = new BlockPos.MutableBlockPos();

      double ex = 0.8 + 0.4 * random.nextFloat();
      double ez = 0.8 + 0.4 * random.nextFloat();
      for(BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-r, 0, -r), blockpos.offset(r, 0, r))) {
         int dx = blockpos1.getX() - blockpos.getX();
         int dz = blockpos1.getZ() - blockpos.getZ();
         double d = dx * dx * ex + dz * dz * ez;
         if (d <= r * r) {
            if (d > (r-1)*(r-1)) {
               if(random.nextInt(2) == 0) {
                  continue;
               }
            }
            placed |= this.placeColumn(cfg, level, random, yPlus, yMinus, mPos.set(blockpos1));
         }
      }

      return placed;
   }

   protected boolean placeColumn(DiskConfiguration cfg, WorldGenLevel level, RandomSource random, int initialY, int minY, BlockPos.MutableBlockPos mPos) {
      boolean found = false;

      for(int y = initialY; y > minY; --y) {
         mPos.setY(y);
         if (cfg.target().test(level, mPos)) {
            BlockState blockstate = cfg.stateProvider().getState(level, random, mPos);
            level.setBlock(mPos, blockstate, 2);
            this.markAboveForPostProcessing(level, mPos);
            found = true;
         }
      }

      return found;
   }
}