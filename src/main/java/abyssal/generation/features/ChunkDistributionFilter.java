package abyssal.generation.features;

import abyssal.generation.OreDist;
import abyssal.init.ModAttachmentTypes;
import abyssal.init.ModGeneration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
        ServerLevel serverLevel = context.getLevel().getLevel();
        OreDist.OreChunkType here = serverLevel.getData(ModAttachmentTypes.ORE_DIST).at(ChunkPos.containing(pos));
        return here == type;
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModGeneration.CHUNK_FILTER.get();
    }
}
