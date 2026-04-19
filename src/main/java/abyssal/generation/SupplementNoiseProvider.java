package abyssal.generation;

import abyssal.init.ModBlockStateProviders;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseBasedStateProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SupplementNoiseProvider extends NoiseBasedStateProvider {

    public static final MapCodec<SupplementNoiseProvider> CODEC = RecordCodecBuilder.mapCodec((supplementNoiseProviderInstance) -> {
        return supplementNoiseProviderInstance.group(BlockState.CODEC.fieldOf("background_state").forGetter((supplementNoiseProvider) -> {
            return supplementNoiseProvider.backgroundState;
        }), InclusiveRange.codec(Codec.INT, 1, 64).fieldOf("variety").forGetter((supplementNoiseProvider) -> {
            return supplementNoiseProvider.variety;
        }), NormalNoise.NoiseParameters.DIRECT_CODEC.fieldOf("slow_noise").forGetter((supplementNoiseProvider) -> {
            return supplementNoiseProvider.slowNoiseParameters;
        }), ExtraCodecs.POSITIVE_FLOAT.fieldOf("slow_scale").forGetter((supplementNoiseProvider) -> {
            return supplementNoiseProvider.slowScale;
        }), Codec.list(BlockState.CODEC).fieldOf("states").forGetter((supplementNoiseProvider) -> {
            return supplementNoiseProvider.states;
        })).and(noiseCodec(supplementNoiseProviderInstance)).apply(supplementNoiseProviderInstance, SupplementNoiseProvider::new);
    });

    @Override
    protected @NotNull BlockStateProviderType<?> type() {
        return ModBlockStateProviders.SUPPLEMENT_NOISE_PROVIDER.get();
    }

    private final InclusiveRange<Integer> variety;
    private final float slowScale;
    private final NormalNoise slowNoise;
    private final NormalNoise.NoiseParameters slowNoiseParameters;

    private final BlockState backgroundState;
    private final List<BlockState> states;


    public SupplementNoiseProvider(BlockState backgroundState, InclusiveRange<Integer> variety, NormalNoise.NoiseParameters slowNoiseParams, float slowNoiseScale, List<BlockState> supplementStates,
                                   long sneed, NormalNoise.NoiseParameters fastNoiseParams, float fastNoiseScale) {
        super(sneed, fastNoiseParams, fastNoiseScale);

        this.variety = variety;
        this.backgroundState = backgroundState;
        this.states = supplementStates;
        this.slowScale = slowNoiseScale;
        this.slowNoiseParameters = slowNoiseParams;
        this.slowNoise = NormalNoise.create(new WorldgenRandom(new LegacyRandomSource(sneed)), slowNoiseParams);
    }

    @Override
    public @NotNull BlockState getState(RandomSource randomSource, BlockPos pos) {
        double d0 = this.getSlowNoiseValue(pos);
        int i = (int) Mth.clampedMap(d0, -1.0D, 1.0D, this.variety.minInclusive(), this.variety.maxInclusive() + 1);
        List<BlockState> list = Lists.newArrayListWithCapacity(i+1);

        list.add(backgroundState);
        for(int j = 0; j < i-1; ++j) {
            list.add(this.getRandomState(this.states, this.getSlowNoiseValue(pos.offset(j * '\ud511', 0, j * '\u85ba'))));
        }
        list.add(i/2, backgroundState);

//        if(randomSource.nextInt(100) == 1) {
//            Main.LOGGER.info(pos);
//            for(BlockState b : list) {
//                Main.LOGGER.info(b.getBlock());
//            }
//            Main.LOGGER.info("");
//        }

        return this.getRandomState(list, pos, this.scale);
    }

    protected BlockState getRandomState(List<BlockState> stateList, BlockPos pos, double scale) {
        double d0 = this.getNoiseValue(pos, scale);
        return this.getRandomState(stateList, d0);
    }

    protected BlockState getRandomState(List<BlockState> states, double randomValue) {
        double d0 = Mth.clamp((1.0D + randomValue) / 2.0D, 0.0D, 0.9999D);
        return states.get((int)(d0 * (double)states.size()));
    }

    protected double getSlowNoiseValue(BlockPos pos) {
        return this.slowNoise.getValue((float)pos.getX() * this.slowScale, (float)pos.getY() * this.slowScale, (float)pos.getZ() * this.slowScale);
    }

}
