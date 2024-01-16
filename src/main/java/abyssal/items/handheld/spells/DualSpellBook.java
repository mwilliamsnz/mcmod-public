package abyssal.items.handheld.spells;

import abyssal.ModAttributes;
import abyssal.spells.ISpellProvider;
import abyssal.spells.Spell;
import abyssal.spells.Spells;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

import java.util.UUID;

public class DualSpellBook extends Item implements IForgeItem, ISpellProvider {
    private final ImmutableMultimap<Attribute, AttributeModifier> bookModifiers;


    private static final String TAG_PRIMARY_SPELL = "PrimarySpell";
    private static final String TAG_SECONDARY_SPELL = "SecondarySpell";

    public DualSpellBook(Properties properties, float abilityPower, UUID bookUUID) {
        super(properties);

        AttributeModifier ap = new AttributeModifier(bookUUID, "Ability power", abilityPower, AttributeModifier.Operation.ADDITION);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableListMultimap.builder();
        if(abilityPower != 0) {
            builder.put(ModAttributes.ABILITY_POWER.get(), ap);
        }
        bookModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.OFFHAND ? this.bookModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public Spell getActiveSpell(ItemStack book) {
        return getSpellWithTag(book, TAG_PRIMARY_SPELL);
    }

    @Override
    public Spell getSecondarySpell(ItemStack book) {
        return getSpellWithTag(book, TAG_SECONDARY_SPELL);
    }

    public static void setPrimarySpell(ItemStack book, Spell spell) {
        setSpellWithTag(book, TAG_PRIMARY_SPELL, spell);
    }

    public static void setSecondarySpell(ItemStack book, Spell spell) {
        setSpellWithTag(book, TAG_SECONDARY_SPELL, spell);
    }

    public static void setSpellWithTag(ItemStack book, String spellLocationTag, Spell spell) {
        CompoundTag bookTags = book.getOrCreateTag();
        if (!bookTags.contains(spellLocationTag)) {
            bookTags.put(spellLocationTag, Spells.toTag(spell));
        }
    }

    public static Spell getSpellWithTag(ItemStack book, String tag) {
        CompoundTag compoundtag = book.getOrCreateTag();
        if (!compoundtag.contains(tag)) {
            return Spells.getFallbackSpell();
        } else {
            CompoundTag spellTag = compoundtag.getCompound(tag);
            return Spells.fromTag(spellTag);
        }
    }


}
