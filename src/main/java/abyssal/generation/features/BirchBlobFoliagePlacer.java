package abyssal.generation.features;

import abyssal.init.ModGeneration;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class BirchBlobFoliagePlacer  extends FoliagePlacer {
    public static final MapCodec<BirchBlobFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> blobParts(instance).apply(instance, BirchBlobFoliagePlacer::new));
    protected final int height;

    protected static <P extends BirchBlobFoliagePlacer> Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider, Integer> blobParts(RecordCodecBuilder.Instance<P> pInstance) {
        return foliagePlacerParts(pInstance).and(Codec.intRange(0, 16).fieldOf("height").forGetter(p_68412_ -> p_68412_.height));
    }

    public BirchBlobFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModGeneration.BIRCH_BLOB.get();
    }

    @Override
    protected void createFoliage(
            LevelSimulatedReader level,
            FoliageSetter pBlockSetter,
            RandomSource rand,
            TreeConfiguration cfg,
            int pMaxFreeTreeHeight,
            FoliageAttachment pAttachment,
            int pFoliageHeight,
            int pFoliageRadius,
            int pOffset
    ) {

        this.placeLeavesRow(level, pBlockSetter, rand, cfg, pAttachment.pos(), 0, 2, pAttachment.doubleTrunk());
        this.placeLeavesRow(level, pBlockSetter, rand, cfg, pAttachment.pos(), 1, 1, pAttachment.doubleTrunk());
        this.placeLeavesRow(level, pBlockSetter, rand, cfg, pAttachment.pos(), 1, 0, pAttachment.doubleTrunk());
        this.placeLeavesRow(level, pBlockSetter, rand, cfg, pAttachment.pos(), 2, -1, pAttachment.doubleTrunk());
        for(int y = -1; y >= pOffset - pFoliageHeight+1; --y) {
            this.placeLeavesRow(level, pBlockSetter, rand, cfg, pAttachment.pos(), 1, y, pAttachment.doubleTrunk());
            this.placeLeavesRowExtras(level, pBlockSetter, rand, cfg, pAttachment.pos(), 1, y, pAttachment.doubleTrunk());
        }
        this.placeLeavesRow(level, pBlockSetter, rand, cfg, pAttachment.pos(), 1, pOffset - pFoliageHeight, pAttachment.doubleTrunk());

    }

    private void placeLeavesRowExtras(
            LevelSimulatedReader pLevel,
            FoliagePlacer.FoliageSetter pFoliageSetter,
            RandomSource rand,
            TreeConfiguration pTreeConfiguration,
            BlockPos attPos,
            int range,
            int pLocalY,
            boolean pLarge
    ) {
        int trunkShift = pLarge ? 1 : 0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for(Direction dir : Direction.Plane.HORIZONTAL) {
            Direction cw = dir.getClockWise();
            int realRange = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? range + trunkShift : range;
            pos.setWithOffset(attPos, 0, pLocalY, 0).move(dir, realRange+1);
            boolean hasAbove = pFoliageSetter.isSet(pos.above());
            if(hasAbove && tryPlaceLeaf(pLevel, pFoliageSetter, rand, pTreeConfiguration, pos)) {
                if(rand.nextInt(2) == 0) {
                    tryPlaceLeaf(pLevel, pFoliageSetter, rand, pTreeConfiguration, pos.move(cw));
                    pos.move(cw, -1);
                }
                if(rand.nextInt(2) == 0) {
                    tryPlaceLeaf(pLevel, pFoliageSetter, rand, pTreeConfiguration, pos.move(cw,-1));
                }
            } else if (rand.nextInt(4) == 0) {
                tryPlaceLeaf(pLevel, pFoliageSetter, rand, pTreeConfiguration, pos);
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource pRandom, int pHeight, TreeConfiguration pConfig) {
        int h = this.height;
        int spaceLeft = pHeight - h - 1;
        if(spaceLeft > 2) {
            h += Math.max(spaceLeft/2, 3);
            h +=  pRandom.nextInt(2);
        }
        return h  - 1 + pRandom.nextInt(2);
    }

    /**
     * Skips certain positions based on the provided shape, such as rounding corners randomly.
     * The coordinates are passed in as absolute value, and should be within [0, {@code range}].
     */
    @Override
    protected boolean shouldSkipLocation(RandomSource pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
        return switch (pLocalY) {
            case 2 -> pRandom.nextInt(2) == 0;
            case 1, -1 -> skipSomePlus(pRandom, pLocalX, pLocalZ, pRange);
            case 0 -> skipAllCorner(pRandom, pLocalX, pLocalZ, pRange);
            default -> false;
        };
    }

    private boolean skipAllCorner(RandomSource pRandom, int pLocalX, int pLocalZ, int pRange) {
        return pLocalX == pRange && pLocalZ == pRange;
    }

    private boolean skipSomeCorner(RandomSource pRandom, int pLocalX, int pLocalZ, int pRange) {
        return skipAllCorner(pRandom, pLocalX, pLocalZ, pRange)  && (pRandom.nextInt(2) == 0);
    }

    private boolean skipSomePlus(RandomSource pRandom, int pLocalX, int pLocalZ, int pRange) {
        if ((pLocalX == pRange) || (pLocalZ == pRange)) {
            return (pLocalX != 0) && (pLocalZ != 0) || (pRandom.nextInt(2) == 0);
        }
        return false;
    }
}
