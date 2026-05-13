package abyssal.components;

import abyssal.blocks.blockentities.WaspPortBlockEntity;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.function.Consumer;

public record WaspsComponent(List<WaspPortBlockEntity.Occupant> wasps) implements TooltipProvider {
    public static final Codec<WaspsComponent> CODEC = WaspPortBlockEntity.Occupant.LIST_CODEC.xmap(WaspsComponent::new, WaspsComponent::wasps);
    public static final StreamCodec<RegistryFriendlyByteBuf, WaspsComponent> STREAM_CODEC = WaspPortBlockEntity.Occupant.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(WaspsComponent::new, WaspsComponent::wasps);
    public static final WaspsComponent EMPTY = new WaspsComponent(List.of());

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(Component.translatable("container.beehive.bees", this.wasps.size(), 3).withStyle(ChatFormatting.GRAY));
    }
}
