package abyssal.spells;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
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

public record SpellComponent(Identifier primaryRL, Optional<Identifier> secondaryRL) implements TooltipProvider {

    public static final Codec<SpellComponent> CODEC = Codec.STRING
            .sizeLimitedListOf(2)
            .xmap(SpellComponent::new, SpellComponent::ordered);
    public static final StreamCodec<ByteBuf, SpellComponent> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
            .apply(ByteBufCodecs.list(2))
            .map(SpellComponent::new, SpellComponent::ordered);

    public SpellComponent(Identifier primary) {
        this(primary, Optional.empty());
    }

    public SpellComponent(Identifier primary, Identifier secondary) {
        this(primary, Optional.of(secondary));
    }

    public SpellComponent(Spell primary) {
        this(primary.key);
    }

    public SpellComponent(Spell primary, Spell secondary) {
        this(primary.key, secondary.key);
    }

    private SpellComponent(List<String> strings) {
        this(
                strings.size() > 0 ? Identifier.parse(strings.get(0)) : Spells.getFallbackSpell().key,
                strings.size() > 1 ? Optional.of(Identifier.parse(strings.get(1))) : Optional.empty()
        );
    }

    private static List<String> ordered(SpellComponent s) {
        if(s.secondaryRL.isPresent()) {
            return List.of(s.primaryRL.toString(), s.secondaryRL.get().toString());
        }
        return List.of(s.primaryRL.toString());
    }

    public Spell primary() {
        return Spells.getSpell(primaryRL);
    }

    public Spell secondary() {
        return Spells.getSpell(secondaryRL);
    }

    public Spell get(boolean alt) {
        return alt && secondaryRL.isPresent() ? secondary() : primary();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        tooltipAdder.accept(tooltip(primaryRL));
        secondaryRL.ifPresent(Identifier -> tooltipAdder.accept(tooltip(secondaryRL.get())));
    }

    private Component tooltip(Identifier rl) {
        return Component.translatable("spell." + rl.toLanguageKey()).withStyle(ChatFormatting.GOLD);
    }
}
