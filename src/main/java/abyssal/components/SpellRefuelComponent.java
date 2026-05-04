package abyssal.components;

import abyssal.Main;
import abyssal.spells.SpellFuelQuantity;
import abyssal.spells.SpellFuelType;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
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

public record SpellRefuelComponent(SpellFuelQuantity fuel) implements TooltipProvider {

    public static final Codec<SpellRefuelComponent> CODEC = Codec.pair(SpellFuelType.CODEC, Codec.intRange(-1000000, 1000000))
            .xmap(SpellRefuelComponent::new, SpellRefuelComponent::toPair);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellRefuelComponent> STREAM_CODEC = StreamCodec.composite(
            SpellFuelType.STREAM_CODEC,
            SpellRefuelComponent::type,
            ByteBufCodecs.INT,
            SpellRefuelComponent::quantity,
            SpellRefuelComponent::new
    );


    public SpellRefuelComponent(Pair<SpellFuelType, Integer> data) {
        this(data.getFirst(), data.getSecond());
    }

    public SpellRefuelComponent(SpellFuelType type, int amount) {
        this(new SpellFuelQuantity(type, amount));
    }

    private static Pair<SpellFuelType, Integer> toPair(SpellRefuelComponent comp) {
        return Pair.of(comp.fuel.type(), comp.fuel.quantity());
    }

    public int quantity() {
        return fuel.quantity();
    }

    public SpellFuelType type() {
        return fuel.type();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        MutableComponent c = Component.translatable("tooltips." + Main.MOD_ID + ".refuel");
        c.append(quantity() + " ");
        c.append(Component.translatable(type().getLanguageKey()));
        tooltipAdder.accept(c.withStyle(type().getColour()));
    }
}
