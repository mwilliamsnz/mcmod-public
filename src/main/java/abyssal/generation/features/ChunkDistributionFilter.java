package abyssal.generation.features;

import abyssal.Main;
import abyssal.generation.OreDist;
import abyssal.init.ModGeneration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class ChunkDistributionFilter extends PlacementFilter {

    public static final MapCodec<ChunkDistributionFilter> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.INT.fieldOf("chunk_type")
                            .forGetter(chunkDistributionFilter -> chunkDistributionFilter.chunkType))
                    .apply(instance, ChunkDistributionFilter::new)
    );
    
    private final int chunkType;

    private ChunkDistributionFilter(int type) {
        this.chunkType = type;
    }

    public static ChunkDistributionFilter forType(OreDist.OreChunkType type) {
        return new ChunkDistributionFilter(type.ordinal());
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource rand, BlockPos pos) {

        OreDist.OreChunkType type = OreDist.OreChunkType.fromOrdinal(chunkType);
        if(type == OreDist.OreChunkType.NONE) {
            return true;
        }
        OreDist.OreChunkType here = Main.oreDist.at(ChunkPos.containing(pos), context.getLevel().getSeed());
        return here == type;
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModGeneration.CHUNK_FILTER.get();
    }
}
