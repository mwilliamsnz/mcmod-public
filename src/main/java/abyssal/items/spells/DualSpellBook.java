package abyssal.items.spells;

import abyssal.Main;
import abyssal.spells.Spell;
import abyssal.spells.Spells;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class DualSpellBook extends Item {

    public static final String TAG_PRIMARY_SPELL = "PrimarySpell";
    public static final String TAG_SECONDARY_SPELL = "SecondarySpell";

    public DualSpellBook(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
//        Spell s1 = getActiveSpell(stack);
//        Spell s2 = getSecondarySpell(stack);
//        if(s1 != Spells.NO_OP) {
//            tooltipAdder.accept(Component.translatable("spell." + s1.key.toLanguageKey()).withStyle(ChatFormatting.GRAY));
//
//        } else if(s2 != Spells.NO_OP) {
//            tooltipAdder.accept(Component.translatable("spell." + Main.MOD_ID + ".empty_primary").withStyle(ChatFormatting.GRAY));
//        }
//        if(s2 != Spells.NO_OP) {
//            tooltipAdder.accept(Component.translatable("spell." + s2.key.toLanguageKey()).withStyle(ChatFormatting.GRAY));
//        }
    }

//    @Override
//    public Spell getActiveSpell(ItemStack book) {
//        return getSpellWithTag(book, TAG_PRIMARY_SPELL);
//    }
//
//    @Override
//    public Spell getSecondarySpell(ItemStack book) {
//        return getSpellWithTag(book, TAG_SECONDARY_SPELL);
//    }

    public static void setPrimarySpell(ItemStack book, Spell spell) {
        setSpellWithTag(book, TAG_PRIMARY_SPELL, spell);
    }

    public static void setSecondarySpell(ItemStack book, Spell spell) {
        setSpellWithTag(book, TAG_SECONDARY_SPELL, spell);
    }

    public static void setSpellWithTag(ItemStack book, String spellLocationTag, Spell spell) {
//        CompoundTag bookTags = book.getOrCreateTag();
//        if (!bookTags.contains(spellLocationTag)) {
//            bookTags.put(spellLocationTag, Spells.toTag(spell));
//        }
    }

    public static Spell getSpellWithTag(ItemStack book, String tag) {
//        CompoundTag compoundtag = book.getOrCreateTag();
//        if (!compoundtag.contains(tag)) {
            return Spells.getFallbackSpell();
//        } else {
//            CompoundTag spellTag = compoundtag.getCompound(tag);
//            return Spells.fromTag(spellTag);
//        }
    }


}
