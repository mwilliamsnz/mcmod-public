package abyssal.init;

import abyssal.Main;
import abyssal.generation.SupplementNoiseProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProviders {

    public static final DeferredRegister<BlockStateProviderType<?>> BSPT = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, Main.MOD_ID);
    public static final RegistryObject<BlockStateProviderType<SupplementNoiseProvider>> SUPPLEMENT_NOISE_PROVIDER = BSPT.register("supplement_noise_provider", ()-> {
        return new BlockStateProviderType<>(SupplementNoiseProvider.CODEC);
    });

}
