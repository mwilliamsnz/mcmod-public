package abyssal.generation;//package cornmod.generation;
//
//import net.minecraft.util.Mth;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.levelgen.BaseStoneSource;
//import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
//import net.minecraft.world.level.levelgen.WorldgenRandom;
//
//public class StratifiedReplacingBaseStoneSource implements BaseStoneSource {
//   private static final int STRATUM_THICKNESS = 8;
//   private static final int STRATUM_1_START = 48;
//   private static final int STRATUM_2_START = 120;
//   private static final int STRATUM_3_START = 220;
//
//   private final WorldgenRandom random;
//   private final long seed;
//   private final BlockState topBlock;
//   private final BlockState midBlock;
//   private final BlockState lowerBlock;
//   private final BlockState deepBlock;
//   private final NoiseGeneratorSettings settings;
//
//   public StratifiedReplacingBaseStoneSource(long seed, BlockState topLayer, BlockState midLayer, BlockState lowerLayer, BlockState deepLayer, NoiseGeneratorSettings settings) {
//      this.random = new WorldgenRandom(seed);
//      this.seed = seed;
//      this.topBlock = topLayer;
//      this.midBlock = midLayer;
//      this.lowerBlock = lowerLayer;
//      this.deepBlock = deepLayer;
//      this.settings = settings;
//   }
//
//   public BlockState getBaseBlock(int x, int y, int z) {
//      if (!this.settings.isDeepslateEnabled()) {
//         return this.topBlock;
//      } else if (y < STRATUM_1_START) {
//         return this.deepBlock;
//      } else if (y < STRATUM_1_START + STRATUM_THICKNESS) {
//         double d0 = Mth.map((double)y, STRATUM_1_START, STRATUM_1_START + STRATUM_THICKNESS, 1.0D, 0.0D);
//         this.random.setBaseStoneSeed(this.seed, x, y, z);
//         return (double)this.random.nextFloat() < d0 ? this.deepBlock : this.lowerBlock;
//      } else if (y < STRATUM_2_START) {
//         return this.lowerBlock;
//      } else if (y < STRATUM_2_START + STRATUM_THICKNESS) {
//         double d0 = Mth.map((double)y, STRATUM_2_START, STRATUM_2_START + STRATUM_THICKNESS, 1.0D, 0.0D);
//         this.random.setBaseStoneSeed(this.seed, x, y, z);
//         return (double)this.random.nextFloat() < d0 ? this.lowerBlock : this.midBlock;
//      } else if (y < STRATUM_3_START) {
//         return this.midBlock;
//      } else if (y < STRATUM_3_START + STRATUM_THICKNESS) {
//         double d0 = Mth.map((double)y, STRATUM_3_START, STRATUM_3_START + STRATUM_THICKNESS, 1.0D, 0.0D);
//         this.random.setBaseStoneSeed(this.seed, x, y, z);
//         return (double)this.random.nextFloat() < d0 ? this.midBlock : this.topBlock;
//      } else {
//         return this.topBlock;
//      }
//   }
//}