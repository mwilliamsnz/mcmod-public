package abyssal.generation;//package cornmod.generation;
//
//import com.mojang.serialization.Codec;
//import javax.annotation.Nullable;
//
//import it.unimi.dsi.fastutil.objects.ObjectArrays;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.LevelReader;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.levelgen.structure.templatesystem.*;
//
//import java.util.Random;
//
//public class LadderProcessor extends StructureProcessor {
//    public static final Codec<LadderProcessor> CODEC;
//    public static final LadderProcessor INSTANCE = new LadderProcessor();
//
//    @Nullable
//    public StructureTemplate.StructureBlockInfo processBlock(LevelReader reader, BlockPos pos1, BlockPos pos2, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructureTemplate.StructureBlockInfo structureBlockInfo2, StructurePlaceSettings placeSettings, @Nullable StructureTemplate template) {
//        BlockPos blockpos = structureBlockInfo2.pos;
//        BlockPos markerPos = blockpos.above().above();
//        if(reader.getBlockState(markerPos).is(Blocks.END_ROD)) {// End rod is the marker
//            placeLadder(reader, blockpos, placeSettings.getRandom(blockpos));
//        }
//        return structureBlockInfo2;
//    }
//
//    private static final int[] xCandidates = {-2, 0, 2,-2, 0, 2, 3, 3, 3,-3,-3,-3};
//    private static final int[] zCandidates = { 3, 3, 3,-3,-3,-3,-2, 0, 2,-2, 0, 2};
//    private static final Integer[] indices = {0,1,2,3,4,5,6,7,8,9,10,11};
//    public void placeLadder(LevelReader reader, BlockPos pos1, Random r) {
//        for(int idx : ObjectArrays.shuffle(indices,r)) {
//            int x = xCandidates[idx];
//            int z = zCandidates[idx];
//            if(reader.) {
//
//            }
//        }
//    }
//
//    protected StructureProcessorType<?> getType() {
//        return StructureProcessorType.LAVA_SUBMERGED_BLOCK;
//    }
//
//    static {
//        CODEC = Codec.unit(() -> {
//            return INSTANCE;
//        });
//    }
//}