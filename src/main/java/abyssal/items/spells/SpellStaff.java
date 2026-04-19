package abyssal.items.spells;

import abyssal.Main;
import abyssal.ModAttributes;
import abyssal.init.ModDataComponents;
import abyssal.spells.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellStaff extends Item {

    public SpellStaff(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack staff = player.getItemInHand(hand);
        ItemStack book = player.getItemInHand(otherHand);

        double ap = player.getAttributeValue(ModAttributes.ABILITY_POWER);

        InteractionResult result;
        boolean alt =  player.isShiftKeyDown();
        Spell spell;
        if(book.has(ModDataComponents.SPELLBOOK)) {
            SpellComponent component = book.get(ModDataComponents.SPELLBOOK);
            spell = component.get(alt);
        } else {
            if(alt) {
                spell = altSpell(staff);
            } else {
                spell = defaultSpell(staff);
            }
        }
        result = InteractionResult.FAIL;

        if(spell != null) {
            SpellFuelQuantity cost = spell.baseCost;
            if(cost.depleteIfSatisfied(player)) {
                result = spell.cast(level, player, staff, book, ap);
                onCast(level, player, staff, book, ap);
                player.getCooldowns().addCooldown(staff, 20);

            }
        }
        return result;
    }

    public Spell defaultSpell(ItemStack staff) {
        return null;
    }

    public Spell altSpell(ItemStack staff) {
        return defaultSpell(staff);
    }

    public void onCast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {

    }

}
