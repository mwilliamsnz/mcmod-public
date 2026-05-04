package abyssal.components;

import abyssal.Main;
import abyssal.spells.SpellFuelQuantity;
import abyssal.spells.SpellFuelType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record SpellBatteryComponent(SpellFuelQuantity capacity, int stored) implements TooltipProvider {

    public static final Codec<SpellBatteryComponent> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                    SpellFuelQuantity.CODEC.fieldOf("capacity").forGetter(SpellBatteryComponent::capacity),
                    Codec.intRange(0, 10000000).fieldOf("stored").forGetter(SpellBatteryComponent::stored)
            ).apply(i, SpellBatteryComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellBatteryComponent> STREAM_CODEC = StreamCodec.composite(
            SpellFuelType.STREAM_CODEC,
            SpellBatteryComponent::type,
            ByteBufCodecs.INT,
            SpellBatteryComponent::maxQuantity,
            ByteBufCodecs.INT,
            SpellBatteryComponent::stored,
            SpellBatteryComponent::new
    );

    public static final SpellBatteryComponent NONE = new SpellBatteryComponent(SpellFuelQuantity.NONE, 0);

    public SpellBatteryComponent(SpellFuelType type, int maxAmount, int currentAmount) {
        this(new SpellFuelQuantity(type, maxAmount), currentAmount);
    }

    public int maxQuantity() {
        return capacity.quantity();
    }

    public SpellFuelType type() {
        return capacity.type();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        MutableComponent c = Component.translatable("tooltips." + Main.MOD_ID + ".fuel_store");
        c.append(stored + "/" + maxQuantity() + " ");
        c.append(Component.translatable(type().getLanguageKey()));
        tooltipAdder.accept(c.withStyle(type().getColour()));
    }
}
