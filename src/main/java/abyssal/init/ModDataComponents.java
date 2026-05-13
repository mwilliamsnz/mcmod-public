package abyssal.init;

import abyssal.Main;
import abyssal.components.*;
import abyssal.items.curios.CoinPurseBundleContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Main.MOD_ID);
    public static final DeferredRegister<ConsumeEffect.Type<?>> CONSUMABLE_TYPES = DeferredRegister.create(BuiltInRegistries.CONSUME_EFFECT_TYPE, Main.MOD_ID);

    public static final Supplier<DataComponentType<DescComponent>> DESC = DATA_COMPONENTS.register(
            "desc", () -> {
                DataComponentType.Builder<DescComponent> builder = DataComponentType.builder();
                return builder.persistent(DescComponent.CODEC).networkSynchronized(DescComponent.STREAM_CODEC).cacheEncoding().build();
            });

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

    public static final Supplier<DataComponentType<SpellRefuelComponent>> SPELL_FUEL_RECHARGE = DATA_COMPONENTS.register(
            "spell_fuel_recharge", () -> {
                DataComponentType.Builder<SpellRefuelComponent> builder = DataComponentType.builder();
                return builder.persistent(SpellRefuelComponent.CODEC).networkSynchronized(SpellRefuelComponent.STREAM_CODEC).cacheEncoding().build();
            });

    public static final Supplier<DataComponentType<SpellBatteryComponent>> SPELL_BATTERY = DATA_COMPONENTS.register(
            "spell_fuel_store", () -> {
                DataComponentType.Builder<SpellBatteryComponent> builder = DataComponentType.builder();
                return builder.persistent(SpellBatteryComponent.CODEC).networkSynchronized(SpellBatteryComponent.STREAM_CODEC).cacheEncoding().build();
            });

    public static final Supplier<DataComponentType<WaspsComponent>> WASPS = DATA_COMPONENTS.register(
            "wasps", () -> {
                DataComponentType.Builder<WaspsComponent> builder = DataComponentType.builder();
                return builder.persistent(WaspsComponent.CODEC).networkSynchronized(WaspsComponent.STREAM_CODEC).cacheEncoding().build();
            });

    public static final Supplier<ConsumeEffect.Type<RestoreFuelConsumeEffect>> CONSUME_REFUEL = CONSUMABLE_TYPES.register(
            "refuel", () -> new ConsumeEffect.Type<>(RestoreFuelConsumeEffect.CODEC, RestoreFuelConsumeEffect.STREAM_CODEC));

}
