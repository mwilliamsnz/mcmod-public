package abyssal.spells;

import abyssal.Main;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record SpellFuelType(String id) {
    public static final Codec<SpellFuelType> CODEC = Codec.STRING.xmap(SpellFuelType::new, SpellFuelType::id);

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellFuelType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SpellFuelType::id,
            SpellFuelType::new
    );

    public ChatFormatting getColour() {
        return SpellFuelTypes.COLOURS.getOrDefault(this, ChatFormatting.WHITE);
    }

    public String getLanguageKey() {
        return "spell." + Identifier.fromNamespaceAndPath(Main.MOD_ID, id).toLanguageKey();
    }
}
