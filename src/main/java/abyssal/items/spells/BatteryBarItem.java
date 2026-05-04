package abyssal.items.spells;

import abyssal.components.SpellBatteryComponent;
import abyssal.init.ModDataComponents;
import abyssal.items.curios.CoinPurseBundleContents;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

public class BatteryBarItem extends Item {

    public BatteryBarItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        SpellBatteryComponent batt = stack.getOrDefault(ModDataComponents.SPELL_BATTERY, SpellBatteryComponent.NONE);
        return batt.stored() != batt.maxQuantity();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        SpellBatteryComponent batt = stack.getOrDefault(ModDataComponents.SPELL_BATTERY, SpellBatteryComponent.NONE);
        return  Mth.clamp(Math.round(batt.stored() * 13.0f / batt.maxQuantity()), 0, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        SpellBatteryComponent batt = stack.getOrDefault(ModDataComponents.SPELL_BATTERY, SpellBatteryComponent.NONE);
        ChatFormatting formatting = batt.type().getColour();
        if(formatting.isColor()) {
            return formatting.getColor().intValue();
        }
        return ChatFormatting.GOLD.getColor();
    }
}
