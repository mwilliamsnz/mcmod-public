package abyssal.spells;

import net.minecraft.ChatFormatting;

import java.util.HashMap;
import java.util.Map;

public class SpellFuelTypes {

    protected static final Map<SpellFuelType, ChatFormatting> COLOURS = new HashMap<>();

    public static final SpellFuelType FUEL_NONE = createSpellFuelType("none", ChatFormatting.WHITE);
    public static final SpellFuelType FUEL_COLOURLESS = createSpellFuelType("colourless", ChatFormatting.WHITE);
    public static final SpellFuelType FUEL_FIRE = createSpellFuelType("fire", ChatFormatting.GOLD);
    public static final SpellFuelType FUEL_FORCE = createSpellFuelType("force", ChatFormatting.DARK_RED);
    public static final SpellFuelType FUEL_LIGHT = createSpellFuelType("light", ChatFormatting.AQUA);

    public static SpellFuelType createSpellFuelType(String id, ChatFormatting colour) {
        SpellFuelType s = new SpellFuelType(id);
        COLOURS.put(s, colour);
        return s;
    }

}
