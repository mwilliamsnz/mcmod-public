package abyssal.init;

import abyssal.Main;
import abyssal.items.curios.CoinPurseBundleContents;
import abyssal.spells.SpellComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Main.MOD_ID);

    public static final Supplier<DataComponentType<CoinPurseBundleContents>> COIN_PURSE_BUNDLE_CONTENTS = DATA_COMPONENTS.register(
            "coin_purse_bundle_contents", () -> {
                DataComponentType.Builder<CoinPurseBundleContents> builder = DataComponentType.builder();
                return builder.persistent(CoinPurseBundleContents.CODEC).networkSynchronized(CoinPurseBundleContents.STREAM_CODEC).cacheEncoding().build();
            });

    public static final Supplier<DataComponentType<SpellComponent>> SPELLBOOK = DATA_COMPONENTS.register(
            "spellbook", () -> {
                DataComponentType.Builder<SpellComponent> builder = DataComponentType.builder();
                return builder.persistent(SpellComponent.CODEC).networkSynchronized(SpellComponent.STREAM_CODEC).cacheEncoding().build();
            });

}
