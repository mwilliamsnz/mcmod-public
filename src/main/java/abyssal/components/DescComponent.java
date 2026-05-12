package abyssal.components;

import abyssal.Main;
import abyssal.spells.Spell;
import abyssal.spells.SpellFuelQuantity;
import abyssal.spells.Spells;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record DescComponent(Identifier id) implements TooltipProvider {

    public static final Codec<DescComponent> CODEC = Identifier.CODEC.xmap(DescComponent::new, DescComponent::id);
    public static final StreamCodec<ByteBuf, DescComponent> STREAM_CODEC = Identifier.STREAM_CODEC
            .map(DescComponent::new, DescComponent::id);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        if(context.player() == null || flag.hasShiftDown()) {
            tooltipAdder.accept(Component.translatable("ttdesc." + id.toLanguageKey() + ".1").withStyle(ChatFormatting.GRAY));
            int i = 2;
            while(Language.getInstance().has("ttdesc." + id.toLanguageKey() + "." + i)) {
                tooltipAdder.accept(Component.translatable("ttdesc." + id.toLanguageKey() + "."  + i).withStyle(ChatFormatting.GRAY));
                i++;
            }
        } else {
            tooltipAdder.accept(Component.translatable("ttdesc.collapsed").withStyle(ChatFormatting.GRAY));
        }
    }
}
