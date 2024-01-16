package abyssal.generation.features;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class ModOreFeature extends OreFeature {

    public ModOreFeature(Codec<OreConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<OreConfiguration> context) {
//        OreConfiguration config = context.config();
//        if(config instanceof ModOreConfiguration) {
//            OreDist.OreChunkType type = ((ModOreConfiguration) config).type;
//            if(type != OreDist.OreChunkType.NONE) {
//                OreDist.OreChunkType here = Main.oreDist.at(new ChunkPos(context.origin()) , context.level().getSeed());
//                if(here != type) {
//                    return false;
//                }
//            }
//        }
        return super.place(context);
    }

}
