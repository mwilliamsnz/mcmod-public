package abyssal.init;

import abyssal.Main;
import abyssal.generation.SupplementNoiseProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockStateProviders {

    public static final DeferredRegister<BlockStateProviderType<?>> BSPT = DeferredRegister.create(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE, Main.MOD_ID);
    public static final Supplier<BlockStateProviderType<SupplementNoiseProvider>> SUPPLEMENT_NOISE_PROVIDER = BSPT.register("supplement_noise_provider", ()-> {
        return new BlockStateProviderType<>(SupplementNoiseProvider.CODEC);
    });

}
