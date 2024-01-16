package abyssal.generation.features;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class ModFoliagePlacer extends FoliagePlacer {
    public static final Codec<ModFoliagePlacer> CODEC = RecordCodecBuilder.create((foliagePlacerInstance) -> {
        return blobParts(foliagePlacerInstance).apply(foliagePlacerInstance, ModFoliagePlacer::new);
    });
    protected final int height;

    protected static <P extends ModFoliagePlacer> Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider, Integer> blobParts(RecordCodecBuilder.Instance<P> p_68414_) {
        return foliagePlacerParts(p_68414_).and(Codec.intRange(0, 16).fieldOf("height").forGetter((p_68412_) -> {
            return p_68412_.height;
        }));
    }

    public ModFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset);
        this.height = height;
    }

    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.BLOB_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader lvlReader, FoliageSetter foliageSetter, RandomSource randomSource, TreeConfiguration treeConfiguration, int p_225524_, FoliageAttachment attachment, int diff, int num, int start) {
        for(int i = start; i >= start - diff; --i) {
            int j = Math.max(num + attachment.radiusOffset() - 1 - i / 2, 0);
            this.placeLeavesRow(lvlReader, foliageSetter, randomSource, treeConfiguration, attachment.pos(), j, i, attachment.doubleTrunk());
        }
    }

    public int foliageHeight(RandomSource randomSource, int p_225517_, TreeConfiguration treeConfiguration) {
        return this.height;
    }

    protected boolean shouldSkipLocation(RandomSource randomSource, int p_225510_, int p_225511_, int p_225512_, int p_225513_, boolean p_225514_) {
        return p_225510_ == p_225513_ && p_225512_ == p_225513_ && (randomSource.nextInt(2) == 0 || p_225511_ == 0);
    }
}
