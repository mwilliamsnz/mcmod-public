package abyssal.items.handheld.spells;

import abyssal.ModAttributes;
import abyssal.spells.ISpellProvider;
import abyssal.spells.Spell;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SpellStaff extends Item implements IForgeItem {


    private final ImmutableMultimap<Attribute, AttributeModifier> staffModifiers;

    public SpellStaff(Properties properties, float abilityPower, UUID staffUUID) {
        super(properties);

        AttributeModifier ap = new AttributeModifier(staffUUID, "Ability power", abilityPower, AttributeModifier.Operation.ADDITION);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableListMultimap.builder();
        if(abilityPower != 0) {
            builder.put(ModAttributes.ABILITY_POWER.get(), ap);
        }
        staffModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.OFFHAND ? this.staffModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack staff = player.getItemInHand(hand);
        ItemStack book = player.getItemInHand(otherHand);

        double ap = player.getAttributeValue(ModAttributes.ABILITY_POWER.get());

        InteractionResultHolder<ItemStack> result;
        boolean alt =  player.isShiftKeyDown();
        Spell spell;
        if(book.getItem() instanceof ISpellProvider spellBook) {
            if(alt) {
                spell = spellBook.getSecondarySpell(book);
            } else {
                spell = spellBook.getActiveSpell(book);
            }
        } else {
            if(alt) {
                spell = altSpell(staff);
            } else {
                spell = defaultSpell(staff);
            }
        }
        result = InteractionResultHolder.fail(staff);
        if(spell != null) {
            result = spell.cast(level, player, staff, book, ap);
            onCast(level, player, staff, book, ap);
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
